package com.bitcnew.module.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.entity.AccountInfo;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.home.entity.UserFollow;
import com.bitcnew.module.home.trade.RechargeCoin1Activity;
import com.bitcnew.module.home.trade.RechargeCoinActivity;
import com.bitcnew.module.home.trade.TakeCoin1Activity;
import com.bitcnew.module.home.trade.TakeCoinActivity;
import com.bitcnew.module.home.trade.TransferCoinActivity;
import com.bitcnew.module.home.trade.USDTTradeActivity;
import com.bitcnew.module.legal.LegalMoneyActivity;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.TjrMinuteTaskPool;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 19-3-8.
 */

public class HomeAccountFragment extends UserBaseImmersionBarFragment implements View.OnClickListener {
    @BindView(R.id.tvTolAssets)
    TextView tvTolAssets;
    @BindView(R.id.tvTolAssetsCny)
    TextView tvTolAssetsCny;
    @BindView(R.id.vp_content)
    ViewPager vp_content;
    @BindView(R.id.llRechargeCoin)
    TextView llRechargeCoin;
    @BindView(R.id.llWithDrawCoin)
    TextView llWithDrawCoin;
    @BindView(R.id.llTransfer)
    TextView llTransfer;
    @BindView(R.id.llLegalMoney)
    TextView llLegalMoney;

    @BindView(R.id.tvYueAccount)
    TextView tvYueAccount;
    @BindView(R.id.tvTokenAccount)
    TextView tvTokenAccount;
    @BindView(R.id.tvGendanAccount)
    TextView tvGendanAccount;
    @BindView(R.id.tvQuanqiuzhishuAccount)
    TextView tvQuanqiuzhishuAccount;
    @BindView(R.id.tvHeyueAccount)
    TextView tvHeyueAccount;
    @BindView(R.id.tvBibiAccount)
    TextView tvBibiAccount;


    private boolean isRun = false;//定时器是否在跑
    private TjrMinuteTaskPool tjrMinuteTaskPool;
    private Group<Position> groupPostion;
    private MyPagerAdapter adapter;

    public static HomeAccountFragment newInstance() {
        HomeAccountFragment fragment = new HomeAccountFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeAccountFragment", "onResume   getUserVisibleHint()==" + getUserVisibleHint());
        if (getUserVisibleHint()) {
            startGetHomeAccount();
        } else {
            closeTimer();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeAccountFragment", "setUserVisibleHint==isVisibleToUser " + isVisibleToUser);
        if (isVisibleToUser) {
            startGetHomeAccount();
        } else {
            closeTimer();
        }
    }

    @Override
    public void onPause() {
        closeTimer();
        super.onPause();
        Log.d("setUserVisibleHint", "onPause=======");

    }

    @Override
    public void onDestroy() {
        closeTimer();
        releaseTimer();
        super.onDestroy();
    }


    private void startTimer() {
//        calculateTradeIncomeRunnable();//启动前也要先计算一次
        if (tjrMinuteTaskPool == null) {
            tjrMinuteTaskPool = new TjrMinuteTaskPool();
        }
        isRun = true;
        tjrMinuteTaskPool.startTime(getActivity(), task);

    }

    private void closeTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
    }

    private void releaseTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.release();
    }


    private Runnable task = new Runnable() {
        public void run() {
            try {
                startGetHomeAccount();
            } catch (Exception e) {
                CommonUtil.LogLa(2, "Exception is " + e.getMessage());
            }
        }
    };


    public void immersionbar() {
        if (mImmersionBar == null) return;
        mImmersionBar
//                .titleBar(ll_bar)
                .statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA)
                .navigationBarColor(R.color.white)
                .init();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        tjrMinuteTaskPool = new TjrMinuteTaskPool();

        tvYueAccount.setOnClickListener(this);
        tvTokenAccount.setOnClickListener(this);
        tvGendanAccount.setOnClickListener(this);
        tvQuanqiuzhishuAccount.setOnClickListener(this);
        tvHeyueAccount.setOnClickListener(this);
        tvBibiAccount.setOnClickListener(this);

        llRechargeCoin.setOnClickListener(this);
        llWithDrawCoin.setOnClickListener(this);
        llTransfer.setOnClickListener(this);
        llLegalMoney.setOnClickListener(this);

        adapter = new MyPagerAdapter(getChildFragmentManager());
        vp_content.setAdapter(adapter);
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d("slide", "onPageSelected  i==" + i);
                slide(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        vp_content.setOffscreenPageLimit(adapter.getCount() - 1);
        slide(0);
        return view;
    }

    private void slide(int pos) {
        switch (pos) {
            case 0:
                tvYueAccount.setSelected(true);
                tvTokenAccount.setSelected(false);
                tvGendanAccount.setSelected(false);
                tvQuanqiuzhishuAccount.setSelected(false);
                tvHeyueAccount.setSelected(false);
                tvBibiAccount.setSelected(false);
                break;
            case 1:
                tvYueAccount.setSelected(false);
                tvTokenAccount.setSelected(true);
                tvGendanAccount.setSelected(false);
                tvQuanqiuzhishuAccount.setSelected(false);
                tvHeyueAccount.setSelected(false);
                tvBibiAccount.setSelected(false);
                break;
            case 2:
                tvYueAccount.setSelected(false);
                tvTokenAccount.setSelected(false);
                tvGendanAccount.setSelected(true);
                tvQuanqiuzhishuAccount.setSelected(false);
                tvHeyueAccount.setSelected(false);
                tvBibiAccount.setSelected(false);
                break;
            case 3:
                tvYueAccount.setSelected(false);
                tvTokenAccount.setSelected(false);
                tvGendanAccount.setSelected(false);
                tvQuanqiuzhishuAccount.setSelected(true);
                tvHeyueAccount.setSelected(false);
                tvBibiAccount.setSelected(false);
                break;
            case 4:
                tvYueAccount.setSelected(false);
                tvTokenAccount.setSelected(false);
                tvGendanAccount.setSelected(false);
                tvQuanqiuzhishuAccount.setSelected(false);
                tvHeyueAccount.setSelected(true);
                tvBibiAccount.setSelected(false);
                break;
            case 5:
                tvYueAccount.setSelected(false);
                tvTokenAccount.setSelected(false);
                tvGendanAccount.setSelected(false);
                tvQuanqiuzhishuAccount.setSelected(false);
                tvHeyueAccount.setSelected(false);
                tvBibiAccount.setSelected(true);
                break;
            case 6:
                tvYueAccount.setSelected(false);
                tvTokenAccount.setSelected(false);
                tvGendanAccount.setSelected(false);
                tvQuanqiuzhishuAccount.setSelected(false);
                tvHeyueAccount.setSelected(false);
                tvBibiAccount.setSelected(false);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRecharge:
                PageJumpUtil.pageJump(getActivity(), USDTTradeActivity.class);
                break;
            case R.id.tvYueAccount:
                if (vp_content.getCurrentItem() != 0) {
                    vp_content.setCurrentItem(0);
                }
                break;
            case R.id.tvTokenAccount:
                if (vp_content.getCurrentItem() != 1) {
                    vp_content.setCurrentItem(1);
                }
                break;
            case R.id.tvGendanAccount:
                if (vp_content.getCurrentItem() != 2) {
                    vp_content.setCurrentItem(2);
                }
                break;
            case R.id.tvQuanqiuzhishuAccount:
                if (vp_content.getCurrentItem() != 3) {
                    vp_content.setCurrentItem(3);
                }
                break;
            case R.id.tvHeyueAccount:
                if (vp_content.getCurrentItem() != 4) {
                    vp_content.setCurrentItem(4);
                }
                break;
            case R.id.tvBibiAccount:
                if (vp_content.getCurrentItem() != 5) {
                    vp_content.setCurrentItem(5);
                }
                break;
            case R.id.llRechargeCoin:
//                PageJumpUtil.pageJump(getActivity(), RechargeCoinActivity.class);
                PageJumpUtil.pageJump(getActivity(), RechargeCoin1Activity.class);
                break;
            case R.id.llWithDrawCoin:
//                PageJumpUtil.pageJump(getActivity(), TakeCoinActivity.class);
                PageJumpUtil.pageJump(getActivity(), TakeCoin1Activity.class);
                break;
            case R.id.llTransfer:
                PageJumpUtil.pageJump(getActivity(), TransferCoinActivity.class);
                break;
            case R.id.llLegalMoney:
                PageJumpUtil.pageJump(getActivity(), LegalMoneyActivity.class);
                break;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).navigationBarColor(R.color.white).init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private Call<ResponseBody> getHomeAccountCall;

    private String tolAssets;
    private String tolAssetsCny;


    private void startGetHomeAccount() {
        CommonUtil.cancelCall(getHomeAccountCall);
        getHomeAccountCall = VHttpServiceManager.getInstance().getVService().homeAccount();
        getHomeAccountCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    tolAssets = resultData.getItem("tolAssets", String.class);
                    tolAssetsCny = resultData.getItem("tolAssetsCny", String.class);
                    tvTolAssets.setText(tolAssets);
                    tvTolAssetsCny.setText(tolAssetsCny);
                    AccountInfo balanceAccount = resultData.getObject("balanceAccount", AccountInfo.class);//余额账户
                    AccountInfo tokenAccount = resultData.getObject("tokenAccount", AccountInfo.class);//Token账户
                    AccountInfo followAccount = resultData.getObject("followAccount", AccountInfo.class);//跟单账户
                    AccountInfo stockAccount = resultData.getObject("stockAccount", AccountInfo.class);//全球指数账户
                    AccountInfo digitalAccount = resultData.getObject("digitalAccount", AccountInfo.class);//合约账户
                    AccountInfo spotAccount = resultData.getObject("spotAccount", AccountInfo.class);//币币账户

                    UserFollow followDv = resultData.getObject("followDv", UserFollow.class);//跟单dav

                    if (vp_content.getCurrentItem() == 0) {
                        setBalance(balanceAccount,0);
                        String s = balanceAccount.openList == null ? "null" : (balanceAccount.openList.size() + "");
                        Log.d("digitalAccount", "digitalAccount.openList11111==" + s);
                    } else if (vp_content.getCurrentItem() == 1) {
                        setTokenData(tokenAccount, 1, null);
                        String s = tokenAccount.openList == null ? "null" : (tokenAccount.openList.size() + "");
                        Log.d("digitalAccount", "stockAccount.openList11111==" + s);
                    } else if (vp_content.getCurrentItem() == 2) {
                        setDitigalData(followAccount, 2, followDv);
                        String s = followAccount.openList == null ? "null" : (followAccount.openList.size() + "");
                        Log.d("digitalAccount", "stockAccount.openList11111==" + s);
                    } else if (vp_content.getCurrentItem() == 3) {
                        setDitigalData(stockAccount, 3, null);
                        String s = stockAccount.openList == null ? "null" : (stockAccount.openList.size() + "");
                        Log.d("digitalAccount", "stockAccount.openList11111==" + s);
                    } else if (vp_content.getCurrentItem() == 4) {
                        setDitigalData(digitalAccount, 4, null);
                        String s = digitalAccount.openList == null ? "null" : (digitalAccount.openList.size() + "");
                        Log.d("digitalAccount", "stockAccount.openList11111==" + s);
                    } else if (vp_content.getCurrentItem() == 5) {
                        setBiBi(spotAccount,5);
                        String s = spotAccount.openList == null ? "null" : (spotAccount.openList.size() + "");
                        Log.d("digitalAccount", "stockAccount.openList11111==" + s);
                    }
                    if (!isRun) startTimer();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }
    private void setBalance(AccountInfo balanceAccount, int pos) {
        if (adapter != null && vp_content != null) {
            Object object = adapter.instantiateItem(vp_content, pos);
            if (object != null && object instanceof HomeBalanceAccountFragment) {
                Log.d("setCoinWallet", "////////");
                HomeBalanceAccountFragment homeBalanceAccountFragment = (HomeBalanceAccountFragment) object;
                homeBalanceAccountFragment.setData(balanceAccount);
            }
        }
    }
    private void setTokenData(AccountInfo accountInfo, int pos, UserFollow userFollow) {
        if (adapter != null && vp_content != null) {
            Object object = adapter.instantiateItem(vp_content, pos);
            if (object != null && object instanceof HomeTokenAccountFragment) {
                HomeTokenAccountFragment homeToekenAccountFragment = (HomeTokenAccountFragment) object;
                homeToekenAccountFragment.setData(accountInfo);
            }
        }
    }
    private void setDitigalData(AccountInfo accountInfo, int pos, UserFollow userFollow) {
        if (adapter != null && vp_content != null) {
            Object object = adapter.instantiateItem(vp_content, pos);
            if (object != null && object instanceof HomeDigitalAccountFragment) {
                HomeDigitalAccountFragment homeDigitalAccountFragment = (HomeDigitalAccountFragment) object;
                homeDigitalAccountFragment.setData(accountInfo, userFollow, pos);
            }
        }
    }
    private void setBiBi(AccountInfo balanceAccount, int pos) {
        if (adapter != null && vp_content != null) {
            Object object = adapter.instantiateItem(vp_content, pos);
            if (object != null && object instanceof HomeBibiAccountFragment) {
                Log.d("setCoinWallet", "////////");
                HomeBibiAccountFragment homeBibiAccountFragment = (HomeBibiAccountFragment) object;
                homeBibiAccountFragment.setData(balanceAccount);
            }
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                default:
                case 0:
                    return HomeBalanceAccountFragment.newInstance(0);
                case 1:
                    return HomeTokenAccountFragment.newInstance(1);
                case 2:
                    return HomeDigitalAccountFragment.newInstance(2);
                case 3:
                    return HomeDigitalAccountFragment.newInstance(3);
                case 4:
                    return HomeDigitalAccountFragment.newInstance(4);
                case 5:
                    return HomeBibiAccountFragment.newInstance();
            }
        }
    }
}

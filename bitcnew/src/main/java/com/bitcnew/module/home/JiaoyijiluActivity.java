package com.bitcnew.module.home;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.trade.fragment.TradeHistoryLeverBibiFragment;
import com.bitcnew.module.home.trade.fragment.TradeUndoneLeverFragment;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class JiaoyijiluActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener{
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.vp_content)
    ViewPager vpContent;

    @BindView(R.id.ll_zongjiaoyishoushu)
    LinearLayout ll_zongjiaoyishoushu;
    @BindView(R.id.txt_zongjiaoyishoushu)
    TextView txt_zongjiaoyishoushu;
    @BindView(R.id.ll_huode)
    LinearLayout ll_huode;
    @BindView(R.id.txt_huode)
    TextView txt_huode;

    @BindView(R.id.tvTab1)
    TextView tvTab1;
    @BindView(R.id.tvTab2)
    TextView tvTab2;
    @BindView(R.id.ivArrow)
    ImageView ivArrow;
    @BindView(R.id.llParams)
    LinearLayout llParams;
    @BindView(R.id.etCoin)
    EditText etCoin;
    //    @BindView(R.id.tvAllDeal)
//    TextView tvAllDeal;
//    @BindView(R.id.tvPartDeal)
//    TextView tvPartDeal;
//    @BindView(R.id.tvCancel)
//    TextView tvCancel;
    @BindView(R.id.tvReset)
    TextView tvReset;
    @BindView(R.id.tvQuery)
    TextView tvQuery;
    @BindView(R.id.llSelectParamsAnim)
    LinearLayout llSelectParamsAnim;
    @BindView(R.id.hideSelectParams)
    View hideSelectParams;
    @BindView(R.id.llSelectParams)
    LinearLayout llSelectParams;
    @BindView(R.id.tvAlreadyTrade)
    TextView tvAlreadyTrade;
    @BindView(R.id.tvAlreadyCancel)
    TextView tvAlreadyCancel;


    private MyPagerAdapter adapter;

    private String accountType = "follow";//follow跟单交易记录   stock全球指数交易记录   digital合约交易记录    spot币币交易记录
    private String title;
    public static void pageJump(Context context, String accountType) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.ACCOUNTTYPE, accountType);
        PageJumpUtil.pageJump(context, JiaoyijiluActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_jiaoyijilu;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ACCOUNTTYPE)) {
                accountType = bundle.getString(CommonConst.ACCOUNTTYPE, "");
                switch (accountType){
                    case "follow":
                        title = getResources().getString(R.string.gendanjiaoyijilu);
                        break;
                    case "stock":
                        title = getResources().getString(R.string.quanqiuzhishujiaoyijilu);
                        break;
                    case "digital":
                        title = getResources().getString(R.string.heyuejiaoyijilu);
                        break;
                    case "spot":
                        title = getResources().getString(R.string.bibijiaoyijilu);
                        break;
                    default:
                        title = getResources().getString(R.string.gendanjiaoyijilu);
                        break;
                }
            }else {
                title = getResources().getString(R.string.gendanjiaoyijilu);
            }
        }else {
            title = getResources().getString(R.string.gendanjiaoyijilu);
        }
        txtTitle.setText(title);
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(adapter);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d("slide", "onPageSelected  i==" + i);
                slideTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);
        txtTitle.setOnClickListener(this);
        llParams.setOnClickListener(this);
        tvAlreadyTrade.setOnClickListener(this);
        tvAlreadyCancel.setOnClickListener(this);
//        tvCancel.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        tvQuery.setOnClickListener(this);
        hideSelectParams.setOnClickListener(this);
        vpContent.setCurrentItem(1);
        slideTab(1);
        llParams.setVisibility(View.VISIBLE);

        setOrderStateBtnState();


        setFragmentData(accountType);
        if (!TextUtils.isEmpty(accountType)){
            if (accountType.equals("stock")||accountType.equals("digital")){//stock全球指数交易记录   digital合约交易记录
                ll_zongjiaoyishoushu.setVisibility(View.VISIBLE);
                ll_huode.setVisibility(View.VISIBLE);
                getQuerySum();
            }else {
                ll_zongjiaoyishoushu.setVisibility(View.INVISIBLE);
                ll_huode.setVisibility(View.INVISIBLE);
            }
        }
    }

    private String sumCount,sumToken;
    private Call<ResponseBody> getQuerySumCall;
    private void getQuerySum() {
        getQuerySumCall = VHttpServiceManager.getInstance().getVService().getquerySum(accountType);
        getQuerySumCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    sumCount = resultData.getItem("sumCount", String.class);
                    sumToken = resultData.getItem("sumToken", String.class);
                    txt_zongjiaoyishoushu.setText(sumCount);
                    txt_huode.setText(sumToken);
                }
            }
        });
    }

    private void slideTab(int arg0) {
//        currentTab = arg0;
        switch (arg0) {
            case 0:
                tvTab1.setSelected(true);
                tvTab2.setSelected(false);
                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                llParams.setVisibility(View.GONE);

                break;
            case 1:
                tvTab1.setSelected(false);
                tvTab2.setSelected(true);
                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                llParams.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_title:
                PageJumpUtil.pageJumpResult(JiaoyijiluActivity.this, SelectJiaoyijiluTypeActivity.class, new Intent(), 0x125);
                break;
            case R.id.tvTab1:
                if (vpContent.getCurrentItem() != 0) {
                    vpContent.setCurrentItem(0);
                }
                break;
            case R.id.tvTab2:
                if (vpContent.getCurrentItem() != 1) {
                    vpContent.setCurrentItem(1);
                }
                break;
            case R.id.llParams:
                if (llSelectParams.getVisibility() == View.INVISIBLE || llSelectParams.getVisibility() == View.GONE) {
                    showSelectParams();
                } else {
                    hideSelectParams();
                }
                break;
            case R.id.tvAlreadyTrade:
                //30：全部成交，24：部分成交，44：已撤销
                updateOrderState("filled");
                break;
            case R.id.tvAlreadyCancel:
                updateOrderState("canceled");
                break;
//            case R.id.tvCancel:
//                updateBuyOrderState("44");
//                break;
            case R.id.tvReset:
                hideSelectParams();
                reset();
                if (vpContent.getCurrentItem() == 1) {
                    TradeHistoryLeverBibiFragment tradeHistoryFragment = (TradeHistoryLeverBibiFragment) adapter.instantiateItem(vpContent, 1);
                    tradeHistoryFragment.reset();
                }
                break;
            case R.id.tvQuery:
                String symbol = etCoin.getText().toString();
//                if (TextUtils.isEmpty(symbol)) {
//                    CommonUtil.showmessage("请选择币种", this);
//                    return;
//                }
                hideSelectParams();
                this.symbol = symbol;
                if (vpContent.getCurrentItem() == 1) {
                    TradeHistoryLeverBibiFragment tradeHistoryFragment = (TradeHistoryLeverBibiFragment) adapter.instantiateItem(vpContent, 1);
                    tradeHistoryFragment.filter(this.symbol, orderState);
                }
                break;
            case R.id.hideSelectParams:
                hideSelectParams();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x125) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    accountType = data.getStringExtra("accountType");
                    title = data.getStringExtra("title");
                    if (!TextUtils.isEmpty(title)){
                        txtTitle.setText(title);
                    }
                    setFragmentData(accountType);
                    if (!TextUtils.isEmpty(accountType)){
                        if (accountType.equals("stock")||accountType.equals("digital")){//stock全球指数交易记录   digital合约交易记录
                            ll_zongjiaoyishoushu.setVisibility(View.VISIBLE);
                            ll_huode.setVisibility(View.VISIBLE);
                            getQuerySum();
                        }else {
                            ll_zongjiaoyishoushu.setVisibility(View.INVISIBLE);
                            ll_huode.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setFragmentData(String accountType) {
        if (adapter != null && vpContent != null) {
            Object object = adapter.instantiateItem(vpContent, 1);
            if (object != null && object instanceof TradeHistoryLeverBibiFragment) {
                TradeHistoryLeverBibiFragment bibiFragment = (TradeHistoryLeverBibiFragment) object;
                bibiFragment.setData(accountType);
            }
        }
    }
    private void setFragmentData2(String accountType) {
        if (adapter != null && vpContent != null) {
            Object object = adapter.instantiateItem(vpContent, 0);
            if (object != null && object instanceof TradeUndoneLeverFragment) {
                TradeUndoneLeverFragment tradeUndoneLeverFragment = (TradeUndoneLeverFragment) object;
                tradeUndoneLeverFragment.setData(accountType);
            }
        }
    }

    private void reset() {
        this.orderState = "";
        symbol = "";
        etCoin.setText("");
        setOrderStateBtnState();
    }

    private void updateOrderState(String orderState) {
        if (!orderState.equals(this.orderState)) {
            this.orderState = orderState;
        } else {
            this.orderState = "";
        }
        setOrderStateBtnState();
    }

    private void setOrderStateBtnState() {
        if (orderState.equals("filled")) {
            tvAlreadyTrade.setSelected(true);
            tvAlreadyCancel.setSelected(false);
        } else if (orderState.equals("canceled")) {
            tvAlreadyTrade.setSelected(false);
            tvAlreadyCancel.setSelected(true);
        } else {
            tvAlreadyTrade.setSelected(false);
            tvAlreadyCancel.setSelected(false);
        }
    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return position == 0 ? "　出售中　" : "　未上架　";
//        }

        @Override
        public Fragment getItem(int i) {
//            return UsdtOrCashTradeHisFragment.newInstance(SYMBOL,"",0);
            return i == 0 ? TradeUndoneLeverFragment.newInstance("", accountType): TradeHistoryLeverBibiFragment.newInstance("", accountType);
        }
    }

    private ObjectAnimator objectAnimatorShow;
    private ObjectAnimator objectAnimatorShowArrow;
    private ObjectAnimator objectAnimatorHide;
    private ObjectAnimator objectAnimatorHideArrow;
    private FastOutSlowInInterpolator fastOutSlowInInterpolator;
    private String symbol;
    private String orderState = "";//

    private void showSelectParams() {
        llSelectParams.setVisibility(View.VISIBLE);

        if (fastOutSlowInInterpolator == null)
            fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
        if (objectAnimatorShow == null) {
            objectAnimatorShow = ObjectAnimator.ofFloat(llSelectParamsAnim, "translationY", -llSelectParamsAnim.getHeight(), 0);
            objectAnimatorShow.setDuration(300);
            objectAnimatorShow.setInterpolator(fastOutSlowInInterpolator);
        }

        if (objectAnimatorShowArrow == null) {
            objectAnimatorShowArrow = ObjectAnimator.ofFloat(ivArrow, "rotation", 0.0f, -45.0f, -90.0f, -180.0f);
            objectAnimatorShowArrow.setDuration(300);
            objectAnimatorShowArrow.setInterpolator(fastOutSlowInInterpolator);
        }
        objectAnimatorShow.start();
        objectAnimatorShowArrow.start();

    }

    private void hideSelectParams() {
        closeKeyBoard();
        if (fastOutSlowInInterpolator == null)
            fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
        if (objectAnimatorHide == null) {
            objectAnimatorHide = ObjectAnimator.ofFloat(llSelectParamsAnim, "translationY", 0, -llSelectParamsAnim.getHeight());
            objectAnimatorHide.setDuration(300);
            objectAnimatorHide.setInterpolator(fastOutSlowInInterpolator);
            objectAnimatorHide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    llSelectParams.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        if (objectAnimatorHideArrow == null) {
            objectAnimatorHideArrow = ObjectAnimator.ofFloat(ivArrow, "rotation", -180.0f, -90.0f, -45.0f, 0.0f);
            objectAnimatorHideArrow.setDuration(300);
            objectAnimatorHideArrow.setInterpolator(fastOutSlowInInterpolator);
        }
        objectAnimatorHide.start();
        objectAnimatorHideArrow.start();
    }


    private void closeKeyBoard() {
        if (etCoin == null || etCoin.getWindowToken() == null) return;
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etCoin.getWindowToken(), 0);
    }
}

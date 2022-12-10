package com.bitcnew.module.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.web.CommonWebViewActivity;
import com.bitcnew.module.home.adapter.AccountBalanceListAdapter;
import com.bitcnew.module.home.entity.AccountBalance;
import com.bitcnew.module.myhome.AboutActivity;
import com.google.gson.reflect.TypeToken;
import com.bitcnew.R;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.entity.AccountInfo;
import com.bitcnew.module.home.trade.adapter.TakeCoinHistoryAdapter;
import com.bitcnew.module.home.trade.entity.TakeCoinHistory;
import com.bitcnew.module.home.trade.history.TakeCoinHistoryActivity;
import com.bitcnew.module.home.trade.history.TakeCoinHistoryDetailsActivity;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.SimpleRecycleDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnEditorAction;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 余额账户
 * Created by zhengmj on 19-3-8.
 */

public class HomeBalanceAccountFragment extends UserBaseFragment {

    @BindView(R.id.txt_chakanruhehuoqutoken)
    TextView txt_chakanruhehuoqutoken;
    @BindView(R.id.tvTolAssetsText)
    TextView tvTolAssetsText;
    @BindView(R.id.tvTolAssets)
    TextView tvTolAssets;
    @BindView(R.id.ll_shizhi)
    LinearLayout ll_shizhi;
    @BindView(R.id.tvShizhi)
    TextView tvShizhi;
    @BindView(R.id.tvTolAssetsCny)
    TextView tvTolAssetsCny;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.tvEableBalance)
    TextView tvEableBalance;
    @BindView(R.id.tvFrozenBalance)
    TextView tvFrozenBalance;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.hideLittleSwitch)
    SwitchCompat hideLittleSwitch;
    @BindView(R.id.keywordEt)
    EditText keywordEt;

//    private TakeCoinHistoryAdapter takeCoinHistoryAdapter;
    private List<AccountBalance> balanceList;
    private AccountBalanceListAdapter balanceListAdapter;


    private AccountInfo balanceAccount;

    private int pageSize = 15;
    private int pageNo = 1;
    private Call<ResponseBody> withdrawCoinListCall;
    private Group<TakeCoinHistory> group;

    private int type = 0;
    public static HomeBalanceAccountFragment newInstance(int type) {
        HomeBalanceAccountFragment fragment = new HomeBalanceAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnCheckedChanged(R.id.hideLittleSwitch)
    public void onHideLittleSwitchToggle(CompoundButton btn, boolean checked) {
        balanceListAdapter.setHideLittle(checked);
    }

    @OnEditorAction(R.id.keywordEt)
    public boolean onKeywordSearch(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
            balanceListAdapter.setKeyword(keywordEt.getText().toString());
            closeKeybord(getActivity());
        }
        return false;
    }

    public static void closeKeybord(Activity activity) {
        InputMethodManager imm =  (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        Log.d("HomeAccountFragment", "setUserVisibleHint==isVisibleToUser " + isVisibleToUser);
//        if (isVisibleToUser && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
//            startGetwithdrawCoinList();
//        }
//    }
//
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (getUserVisibleHint() && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
//            startGetwithdrawCoinList();
//        }
//    }

    //    @BindView(R.id.tvEableBalance)
//    TextView tvEableBalance;
//    @BindView(R.id.tvFrozenBalance)
//    TextView tvFrozenBalance;
    public void setData(AccountInfo balanceAccount) {
        if (rvList == null) return;
        this.balanceAccount = balanceAccount;
        if (balanceAccount != null) {
            tvTolAssets.setText(balanceAccount.assets);
            tvTolAssetsCny.setText(balanceAccount.assetsCny);
            tvEableBalance.setText(balanceAccount.holdAmount);
            tvFrozenBalance.setText(balanceAccount.frozenAmount);
            tvShizhi.setText(balanceAccount.assetsCny);


            balanceList.clear();
            if (null != balanceAccount.symbolList) {
                balanceList.addAll(balanceAccount.symbolList);
            }
            balanceListAdapter.notifyDataSetChanged();

            if (null != balanceAccount.symbolList && !balanceAccount.symbolList.isEmpty()) {
                rvList.setVisibility(View.VISIBLE);
                tvNoData.setVisibility(View.GONE);
            } else {
                rvList.setVisibility(View.GONE);
                tvNoData.setVisibility(View.VISIBLE);
            }
        } else {
            rvList.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_balance_account, container, false);
        ButterKnife.bind(this, view);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle.containsKey("type")) {
            type = bundle.getInt("type", 1);
        }
        if (type==1){
            tvTolAssetsText.setText(getActivity().getResources().getString(R.string.zongzichantoken));
            tvTolAssetsCny.setVisibility(View.INVISIBLE);
            txt_chakanruhehuoqutoken.setVisibility(View.VISIBLE);
            ll_shizhi.setVisibility(View.VISIBLE);
        }else{
            tvTolAssetsText.setText(getActivity().getResources().getString(R.string.yuezhanghuzongzichanusdt));
            tvTolAssetsCny.setVisibility(View.VISIBLE);
            txt_chakanruhehuoqutoken.setVisibility(View.GONE);
            ll_shizhi.setVisibility(View.INVISIBLE);
        }

//        takeCoinHistoryAdapter = new TakeCoinHistoryAdapter(getActivity());
        balanceList = new ArrayList<>();
        balanceListAdapter = new AccountBalanceListAdapter(balanceList);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
//        simpleRecycleDivider.setShowLastDivider(false);
//        rvList.addItemDecoration(simpleRecycleDivider);
//        rvList.setAdapter(takeCoinHistoryAdapter);
//        takeCoinHistoryAdapter.setOnItemClick(new OnItemClick() {
//            @Override
//            public void onItemClickListen(int pos, TaojinluType t) {
//                TakeCoinHistory takeCoinHistory = (TakeCoinHistory) t;
//                TakeCoinHistoryDetailsActivity.pageJump(getActivity(), takeCoinHistory);
//            }
//        });
        rvList.setAdapter(balanceListAdapter);
        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageJumpUtil.pageJump(getActivity(), TakeCoinHistoryActivity.class);
            }
        });
        txt_chakanruhehuoqutoken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), CommonConst.PASSGEDETAIL_222);
            }
        });

        return view;
    }

    TjrBaseDialog questionMarkDialog;

    private void showSubmitTipsDialog(String tips) {
        questionMarkDialog = new TjrBaseDialog(getActivity()) {
            @Override
            public void onclickOk() {
                dismiss();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        questionMarkDialog.setTitleVisibility(View.GONE);
        questionMarkDialog.setBtnColseVisibility(View.GONE);
        questionMarkDialog.setMessage(getResources().getString(R.string.shizhixiaoyu) + tips + getResources().getString(R.string.usdtdebizhong));
        questionMarkDialog.setBtnOkText(getResources().getString(R.string.zhidaole));
        questionMarkDialog.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void startGetwithdrawCoinList() {
//        CommonUtil.cancelCall(withdrawCoinListCall);
//        withdrawCoinListCall = VHttpServiceManager.getInstance().getVService().withdrawCoinList(pageNo);
//        withdrawCoinListCall.enqueue(new MyCallBack(getActivity()) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    pageSize = resultData.getPageSize(pageSize);
//                    group = resultData.getGroup("data", new TypeToken<Group<TakeCoinHistory>>() {
//                    }.getType());
//                    takeCoinHistoryAdapter.setGroup(group);
//                    if (group != null && group.size() > 0) {
//                        rvList.setVisibility(View.VISIBLE);
//                        tvNoData.setVisibility(View.GONE);
//                    } else {
//                        rvList.setVisibility(View.GONE);
//                        tvNoData.setVisibility(View.VISIBLE);
//                    }
////                    if (takeCoinHistoryAdapter.getRealItemCount() > 0) {
////                        takeCoinHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
////                    }
//                }
//            }
//
//        });
    }
}

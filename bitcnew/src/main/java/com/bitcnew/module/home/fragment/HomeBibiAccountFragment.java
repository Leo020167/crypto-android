package com.bitcnew.module.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.adapter.CaiwujiluAdapter;
import com.bitcnew.module.home.adapter.ChicangAdapter;
import com.bitcnew.module.home.entity.AccountInfo;
import com.bitcnew.module.home.entity.Caiwujilu;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.home.trade.adapter.TakeCoinHistoryAdapter;
import com.bitcnew.module.home.trade.adapter.TradeLeverHistoryAdapter;
import com.bitcnew.module.home.trade.entity.TakeCoinHistory;
import com.bitcnew.module.home.trade.history.TakeCoinHistoryActivity;
import com.bitcnew.module.home.trade.history.TakeCoinHistoryDetailsActivity;
import com.bitcnew.module.wallet.LeverInfoActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.LoadMoreRecycleView;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeBibiAccountFragment extends UserBaseFragment implements View.OnClickListener{


    @BindView(R.id.tvTolAssets)
    TextView tvTolAssets;
    @BindView(R.id.tvTolAssetsCny)
    TextView tvTolAssetsCny;
    @BindView(R.id.tvZongyingkui)
    TextView tvZongyingkui;


    @BindView(R.id.tvEableBalance)
    TextView tvEableBalance;
    @BindView(R.id.tvFrozenBalance)
    TextView tvFrozenBalance;
    @BindView(R.id.tvWeituojine)
    TextView tvWeituojine;
    @BindView(R.id.tvDangqianchicang)
    TextView tvDangqianchicang;


    @BindView(R.id.txt_chichang)
    TextView txt_chichang;
    @BindView(R.id.txt_cangwujilu)
    TextView txt_cangwujilu;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.fl_1)
    FrameLayout fl_1;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.rvList2)
    LoadMoreRecycleView rvList2;


    private ChicangAdapter tradeLeverHistoryAdapter;
    private AccountInfo balanceAccount;

    public static HomeBibiAccountFragment newInstance() {
        HomeBibiAccountFragment fragment = new HomeBibiAccountFragment();
        return fragment;
    }


    public void setData(AccountInfo balanceAccount) {
        if (rvList == null) return;
        this.balanceAccount = balanceAccount;
        if (balanceAccount != null) {
            tvTolAssets.setText(balanceAccount.assets);
            tvTolAssetsCny.setText(balanceAccount.assetsCny);
            tvEableBalance.setText(balanceAccount.holdAmount);
            tvFrozenBalance.setText(balanceAccount.frozenAmount);
            tvWeituojine.setText(balanceAccount.frozenBail);
            tvZongyingkui.setText(balanceAccount.profit);
            tvDangqianchicang.setText("当前持仓值");

            if (null!=balanceAccount){
                if (balanceAccount.openList != null&&balanceAccount.openList.size()>0) {
                    tradeLeverHistoryAdapter.setGroup(balanceAccount.openList);
                    rvList.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                }else {
                    rvList.setVisibility(View.GONE);
                    tvNoData.setVisibility(View.VISIBLE);
                }
            }
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bibi_account, container, false);
        ButterKnife.bind(this, view);
        txt_cangwujilu.setOnClickListener(this);
        txt_chichang.setOnClickListener(this);
        tvAll.setOnClickListener(this);

        tradeLeverHistoryAdapter = new ChicangAdapter(getActivity());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
        simpleRecycleDivider.setShowLastDivider(false);
        rvList.addItemDecoration(simpleRecycleDivider);
        rvList.setAdapter(tradeLeverHistoryAdapter);
        tradeLeverHistoryAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                Position order = (Position) t;
                LeverInfoActivity.pageJump(getActivity(),order.orderId,"2");
            }
        });



        adapter2 = new CaiwujiluAdapter(getActivity());
        rvList2.setLayoutManager(new LinearLayoutManager(getActivity()));
        SimpleRecycleDivider simpleRecycleDivider2 = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
        simpleRecycleDivider.setShowLastDivider(false);
        rvList2.addItemDecoration(simpleRecycleDivider2);
        rvList2.setAdapter(adapter2);
        adapter2.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                TakeCoinHistory takeCoinHistory = (TakeCoinHistory) t;
                TakeCoinHistoryDetailsActivity.pageJump(getActivity(), takeCoinHistory);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_cangwujilu:
                tvAll.setVisibility(View.VISIBLE);
                fl_1.setVisibility(View.GONE);
                rvList2.setVisibility(View.VISIBLE);
                txt_cangwujilu.setTextColor(getResources().getColor(R.color.c333333));
                txt_chichang.setTextColor(getResources().getColor(R.color.c999999));
                pageNo2=1;
                startGetMyUserProjectList();
                break;
            case R.id.txt_chichang:
                tvAll.setVisibility(View.INVISIBLE);
                fl_1.setVisibility(View.VISIBLE);
                rvList2.setVisibility(View.GONE);
                txt_cangwujilu.setTextColor(getResources().getColor(R.color.c999999));
                txt_chichang.setTextColor(getResources().getColor(R.color.c333333));
                break;
            case R.id.tvAll:
                PageJumpUtil.pageJump(getActivity(), TakeCoinHistoryActivity.class);
                break;
        }
    }

    private int pageNo2 = 1;
    private int pageSize2 = 15;
    private CaiwujiluAdapter adapter2;
    private Call<ResponseBody> realCall;
    private void startGetMyUserProjectList() {
        CommonUtil.cancelCall(realCall);
        realCall = VHttpServiceManager.getInstance().getVService().withdrawCoinList3(pageNo2,"","2");
        realCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<Caiwujilu> group = null;
                if (resultData.isSuccess()) {
                    pageSize2 = resultData.getPageSize(pageSize2);
                    group = resultData.getGroup("data", new TypeToken<Group<Caiwujilu>>() {
                    }.getType());
                    if (pageNo2 == 1) {
                        adapter2.setGroup(group);
                    } else {
                        adapter2.addItem(group);
                        adapter2.notifyDataSetChanged();
                    }
                    pageNo2++;
                }
                adapter2.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize2);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                adapter2.onLoadComplete(false, false);
            }
        });
    }
}


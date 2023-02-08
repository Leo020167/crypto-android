package com.bitcnew.module.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.bean.MiningInfoBean;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.HomeActivity;
import com.bitcnew.module.home.HomeCropyMeActivity;
import com.bitcnew.module.home.adapter.SlidingImage_Adapter;
import com.bitcnew.module.home.adapter.TradeCurrPositionAdapter2;
import com.bitcnew.module.home.bean.HomeBannerBean;
import com.bitcnew.module.home.entity.AccountInfo;
import com.bitcnew.module.home.entity.UserFollow;
import com.bitcnew.module.home.trade.adapter.TradeCurrPositionAdapter;
import com.bitcnew.module.myhome.UserHomeActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.StockChartUtil;
import com.bitcnew.widgets.CircleImageView;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 数字货币账户
 * Created by zhengmj on 19-3-8.
 */

public class HomeDigitalAccountFragment extends UserBaseFragment implements View.OnClickListener{

    @BindView(R.id.ivhead)
    CircleImageView ivhead;
    @BindView(R.id.txt_qubangding)
    TextView txt_qubangding;
    @BindView(R.id.txt_chakandaoshi)
    TextView txt_chakandaoshi;
    @BindView(R.id.txt_daoshi_name)
    TextView txt_daoshi_name;

    @BindView(R.id.tvTolAssetsText)
    TextView tvTolAssetsText;
    @BindView(R.id.tvTolAssets)
    TextView tvTolAssets;
    @BindView(R.id.tvTolAssetsCny)
    TextView tvTolAssetsCny;
    @BindView(R.id.tvRiskRateMark)
    TextView tvRiskRateMark;
    @BindView(R.id.tvRiskRate)
    TextView tvRiskRate;
    @BindView(R.id.tvEableBail)
    TextView tvEableBail;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.tvOpenBail)
    TextView tvOpenBail;
    @BindView(R.id.txt_dongjie)
    TextView txt_dongjie;
    @BindView(R.id.tvFrozenBail)
    TextView tvFrozenBail;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.llRiskRateDesc)
    LinearLayout llRiskRateDesc;
    @BindView(R.id.ll_gendandaoshi)
    LinearLayout ll_gendandaoshi;

    @BindView(R.id.ll_wakuangshouyi)
    LinearLayout ll_wakuangshouyi;
    @BindView(R.id.tvZuorishouyi)
    TextView tvZuorishouyi;
    @BindView(R.id.tvLishizongshouyi)
    TextView tvLishizongshouyi;
    @BindView(R.id.ll_keyongbaozhengjin)
    LinearLayout ll_keyongbaozhengjin;
    @BindView(R.id.ll_chicangbaozhengjin)
    LinearLayout ll_chicangbaozhengjin;


    private TradeCurrPositionAdapter homeDigitalAdapter;
    private TradeCurrPositionAdapter2 homeDigitalAdapter2;

    private AccountInfo accountInfo;

    private int type = 2;//2跟单账户


    public static HomeDigitalAccountFragment newInstance(int type) {
        HomeDigitalAccountFragment fragment = new HomeDigitalAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeAccountFragment", "setUserVisibleHint==isVisibleToUser " + isVisibleToUser);
        if (isVisibleToUser) {
//可见时加载数据相当于Fragment的onResume
            if (type==1){
                ll_keyongbaozhengjin.setVisibility(View.GONE);
                ll_chicangbaozhengjin.setVisibility(View.GONE);
                ll_wakuangshouyi.setVisibility(View.VISIBLE);
                GetMiningInfoCall();
                tvTolAssetsText.setText(getResources().getString(R.string.zhanghuyueusdt));
                tvRiskRateMark.setText(getResources().getString(R.string.dangqianshouyilv));
            }
        }else{
//不可见是不加载数据
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
        }
    }
    private TjrImageLoaderUtil tjrImageLoaderUtil;
    public void setData(AccountInfo digitalAccount, UserFollow userFollow,int pos) {
        if (rvList == null) return;
        this.accountInfo = digitalAccount;
        if (accountInfo != null) {
            tvTolAssets.setText(accountInfo.assets);
            tvTolAssetsCny.setText(accountInfo.assetsCny);
            tvRiskRate.setText(accountInfo.riskRate + "%");
            tvEableBail.setText(accountInfo.eableBail);
            tvProfit.setText(StockChartUtil.formatWithSign(accountInfo.profit));
            tvProfit.setTextColor(StockChartUtil.getRateTextColor(getActivity(), Double.parseDouble(accountInfo.profit)));
            tvOpenBail.setText(accountInfo.openBail);
            if (pos==1){//跟单账户
                txt_dongjie.setText(getActivity().getResources().getString(R.string.gendandongjiezichan));
                tvFrozenBail.setText(accountInfo.disableAmount);
                rvList.setVisibility(View.GONE);
                 if (null!=accountInfo){
                    if (accountInfo.openList != null&&accountInfo.openList.size()>0) {
                        homeDigitalAdapter.setGroup(accountInfo.openList);
                    }
                }
            }else if (pos==2){//全球指数账户
                txt_dongjie.setText(getActivity().getResources().getString(R.string.dongjiebaozhengjin));
                tvFrozenBail.setText(accountInfo.disableAmount);
                if (null!=accountInfo){
                    if (accountInfo.openList != null&&accountInfo.openList.size()>0) {
                        homeDigitalAdapter.setGroup(accountInfo.openList);
                    }
                }
            }else {
                txt_dongjie.setText(getActivity().getResources().getString(R.string.dongjiebaozhengjin));
                tvFrozenBail.setText(accountInfo.disableAmount);
                if (null!=accountInfo){
                    if (accountInfo.openList != null&&accountInfo.openList.size()>0) {
                        homeDigitalAdapter.setGroup(accountInfo.openList);
                    }
                }
            }
//            String s = accountInfo.openList == null ? "null" : (accountInfo.openList.size() + "");
//            CommonUtil.showmessage(s, getActivity());

            if (null!=userFollow){
                if (userFollow.dvUid>0){
                    dvUid = userFollow.dvUid;
                    if (!TextUtils.isEmpty(userFollow.dvHeadUrl)){
                        dvHeadUrl = userFollow.dvHeadUrl;
                        tjrImageLoaderUtil.displayImageForHead(dvHeadUrl, ivhead);
                    }
                    if (!TextUtils.isEmpty(userFollow.dvUserName)){
                        dvUserName = userFollow.dvUserName;
                        txt_daoshi_name.setText(dvUserName);
                    }
                    ivhead.setVisibility(View.VISIBLE);
                    txt_qubangding.setVisibility(View.GONE);
                    txt_chakandaoshi.setVisibility(View.VISIBLE);
                }else{
                    txt_daoshi_name.setText(getActivity().getResources().getString(R.string.weibangding));
                    ivhead.setVisibility(View.GONE);
                    txt_qubangding.setVisibility(View.VISIBLE);
                    txt_chakandaoshi.setVisibility(View.GONE);
                }
            }

        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_ditital_account, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle.containsKey("type")) {
            type = bundle.getInt("type", 1);
        }
        if (type==2){
            ll_gendandaoshi.setVisibility(View.VISIBLE);
        }else{
            ll_gendandaoshi.setVisibility(View.GONE);
        }
        txt_qubangding.setOnClickListener(this);
        txt_chakandaoshi.setOnClickListener(this);
        tjrImageLoaderUtil = new TjrImageLoaderUtil();

        if (type==1){
            homeDigitalAdapter2 = new TradeCurrPositionAdapter2(getActivity());
            rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
            SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
            simpleRecycleDivider.setShowLastDivider(false);
            rvList.addItemDecoration(simpleRecycleDivider);
            rvList.setAdapter(homeDigitalAdapter2);
        }else {
            homeDigitalAdapter = new TradeCurrPositionAdapter(getActivity());
            rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
            SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
            simpleRecycleDivider.setShowLastDivider(false);
            rvList.addItemDecoration(simpleRecycleDivider);
            rvList.setAdapter(homeDigitalAdapter);
        }

        llRiskRateDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    if (type==1){
                        if (!TextUtils.isEmpty(homeActivity.miningRateDesc)) {
                            showRiskRateDescDialog(homeActivity.miningRateDesc);
                        }
                    }else{
                        if (!TextUtils.isEmpty(homeActivity.riskRateDesc)) {
                            showRiskRateDescDialog(homeActivity.riskRateDesc);
                        }
                    }
                }
            }
        });
        if (type == 2) {
            tvTolAssetsText.setText(getResources().getString(R.string.zongzichanusdt));
        } else if (type == 3) {
            tvTolAssetsText.setText(getResources().getString(R.string.zongzichanusdt));
        }else if (type == 4) {
            tvTolAssetsText.setText(getResources().getString(R.string.zongzichanusdt));
        }else if (type == 5) {
            tvTolAssetsText.setText(getResources().getString(R.string.zongzichanusdt));
        }
        return view;
    }

    private Call<ResponseBody> getMiningInfoCall;
    private void GetMiningInfoCall() {
        CommonUtil.cancelCall(getMiningInfoCall);
        getMiningInfoCall = VHttpServiceManager.getInstance().getVService().getMiningInfo();
        getMiningInfoCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                try {
                    if (resultData.isSuccess()) {
                        Gson gson = new Gson();
                        MiningInfoBean bean = gson.fromJson(resultData.data, MiningInfoBean.class);
                        if (null != bean) {
                            if (!TextUtils.isEmpty(bean.getCalRate())){
                                double shou = Double.parseDouble(bean.getCalRate());
                                double shou2 = shou*100;
                                tvRiskRate.setText(shou2+"%");
                            }
                            tvZuorishouyi.setText(bean.getYesProfit());
                            tvLishizongshouyi.setText(bean.getSum());
                            if (null!=bean.getProfits()&&bean.getProfits().size()>0){
                                homeDigitalAdapter2.setGroup(bean.getProfits());
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    TjrBaseDialog showRiskRateDescDialog;

    private void showRiskRateDescDialog(String tips) {
        showRiskRateDescDialog = new TjrBaseDialog(getActivity()) {
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
        showRiskRateDescDialog.setTitleVisibility(View.GONE);
        showRiskRateDescDialog.setBtnColseVisibility(View.GONE);
        showRiskRateDescDialog.setMessage(tips);
        showRiskRateDescDialog.setBtnOkText(getResources().getString(R.string.zhidaole));
        showRiskRateDescDialog.show();
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

    private long dvUid;//导师ID
    private String dvHeadUrl,dvUserName;
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_qubangding:
                Intent intent = new Intent(getActivity(), HomeCropyMeActivity.class);
                startActivity(intent);
                break;
            case R.id.txt_chakandaoshi:
                UserHomeActivity.pageJump(getActivity(), dvUid);
                break;
        }
    }

}

package com.bitcnew.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.web.CommonWebViewActivity;
import com.bitcnew.module.WebActivity2;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.bitcnew.R;
import com.bitcnew.bean.AgentInfoBean;
import com.bitcnew.bean.DailiBannerBean;
import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.util.MyCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class DailiActivity extends TJRBaseToolBarActivity implements  View.OnClickListener {
    @BindView(R.id.view_top)
    View view_top;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.img_daili)
    ImageView imgDaili;
    @BindView(R.id.txt_chengweidaili)
    TextView txtChengweidaili;
    @BindView(R.id.txt_mingxi)
    TextView txt_mingxi;
    @BindView(R.id.re_daili)
    RelativeLayout reDaili;
    @BindView(R.id.txt_shenhezhong)
    TextView txtShenhezhong;
    @BindView(R.id.ll_dailiyongjin)
    LinearLayout ll_dailiyongjin;
    @BindView(R.id.txt_price)
    TextView txt_price;
    @BindView(R.id.txt_code)
    TextView txtCode;
    @BindView(R.id.txt_1)
    TextView txt_1;
    @BindView(R.id.txt_2)
    TextView txt_2;
    @BindView(R.id.txt_3)
    TextView txt_3;
    @BindView(R.id.txt_4)
    TextView txt_4;
    @BindView(R.id.txt_5)
    TextView txt_5;
    @BindView(R.id.txt_6)
    TextView txt_6;
    @BindView(R.id.txt_guize1)
    TextView txt_guize1;
    @BindView(R.id.txt_guize2)
    TextView txt_guize2;
    @BindView(R.id.txt_dengji)
    TextView txtDengji;
    @BindView(R.id.txt_lijishengji)
    TextView txtLijishengji;
    @BindView(R.id.txt_zongyaoqingrenshu)
    TextView txt_zongyaoqingrenshu;
    @BindView(R.id.txt_keyaoqingrenshu)
    TextView txt_keyaoqingrenshu;

    private String price= "1000";

    @Override
    protected int setLayoutId() {
        return R.layout.activity_daili;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.daili);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_chengweidaili){
            showCopyTipsDialog();
        }else if (view.getId() == R.id.img_back){
            finish();
        }else if (view.getId() == R.id.txt_guize1){
            CommonWebViewActivity.pageJumpCommonWebViewActivity(DailiActivity.this, CommonConst.DAILIGUIZE);
        }else if (view.getId() == R.id.txt_guize2){
            CommonWebViewActivity.pageJumpCommonWebViewActivity(DailiActivity.this, CommonConst.DAILIGUIZE);
        }else if (view.getId() == R.id.txt_lijishengji){
            shengjiTipsDialog();
        }else if (view.getId() == R.id.txt_mingxi){
            Intent intent3= new Intent(this, WebActivity2.class);
            startActivity(intent3);
        }
    }

    TjrBaseDialog shengjiTipsDialog;
    private void shengjiTipsDialog() {
        shengjiTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                getLijishengji();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        shengjiTipsDialog.setTvTitle(getResources().getString(R.string.wenxintishi));
        String title = String.format(getResources().getString(R.string.goumaivipx), upgradeAmout);
        String title2 = title+upgradeName+"?";
        shengjiTipsDialog.setMessage(title2);
        shengjiTipsDialog.setBtnOkText(getResources().getString(R.string.goumai));
        shengjiTipsDialog.setBtnColseText(getResources().getString(R.string.quxiao));
        shengjiTipsDialog.show();
    }
    TjrBaseDialog copyTipsDialog;
    private void showCopyTipsDialog() {
        copyTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                getAgentApply();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        copyTipsDialog.setTvTitle(getResources().getString(R.string.zhifudailifei));
        String title = String.format(getResources().getString(R.string.dailifeiwei), price);
        copyTipsDialog.setMessage(title);
        copyTipsDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        view_top.setLayoutParams(params);//将标题栏，加上顶部状态栏的高度
        img_back.setOnClickListener(this);
        txtChengweidaili.setOnClickListener(this);
        txt_guize1.setOnClickListener(this);
        txt_guize2.setOnClickListener(this);
        txtLijishengji.setOnClickListener(this);
        txt_mingxi.setOnClickListener(this);
        getAgentInfo();
        getAgentConfig();
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }



//    sumMyAdd 总邀请人数
//    remainInviteCount 可邀请人数
//    levelName 当前代理等级
//    upgradeFlag 是否显示按钮，false不显示
//    upgradeAmout 升级费用，upgradeName 升级后的等级名称
//    您是否需要花费XXXUSDT购买VIPX
//    取消  购买
    private String upgradeAmout,upgradeName;
    private Call<ResponseBody> getAgentInfoCall;
    private void getAgentInfo() {//获取我是否是代理
        getAgentInfoCall = VHttpServiceManager.getInstance().getVService().getAgentInfo();
        getAgentInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    AgentInfoBean bean = gson.fromJson(resultData.data,AgentInfoBean.class);
                    if (null!=bean){
                        String status = bean.getStatus();
                        if (status.equals("0")){
                            txt_title.setText(getResources().getString(R.string.jiaruwomen));
                            reDaili.setVisibility(View.VISIBLE);
                            txtChengweidaili.setVisibility(View.VISIBLE);
                            txtShenhezhong.setVisibility(View.GONE);
                            ll_dailiyongjin.setVisibility(View.GONE);
                        }else if (status.equals("1")){
                            txt_title.setText(getResources().getString(R.string.chengweidaili));
                            reDaili.setVisibility(View.VISIBLE);
                            txtChengweidaili.setVisibility(View.GONE);
                            txtShenhezhong.setVisibility(View.VISIBLE);
                            ll_dailiyongjin.setVisibility(View.GONE);
                        }else{
                            txt_title.setText(getResources().getString(R.string.wodeyongjin));
                            reDaili.setVisibility(View.GONE);
                            ll_dailiyongjin.setVisibility(View.VISIBLE);
                            if (bean.isUpgradeFlag()){
                                txtLijishengji.setVisibility(View.VISIBLE);
                            }else {
                                txtLijishengji.setVisibility(View.GONE);
                            }
                            if (null!=bean.getAgent()){
                                if (!TextUtils.isEmpty(bean.getAgent().getInviteCode())){
                                    txtCode.setText(getResources().getString(R.string.wodeyaoqingma)+bean.getAgent().getInviteCode());
                                }else{
                                    txtCode.setText(getResources().getString(R.string.wodeyaoqingma));
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getSumCommission())){
                                    txt_price.setText(bean.getAgent().getSumCommission());
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getLevelName())){
                                    txtDengji.setText(bean.getAgent().getLevelName());
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getSumMyAdd())){
                                    txt_zongyaoqingrenshu.setText(bean.getAgent().getSumMyAdd());
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getRemainInviteCount())){
                                    txt_keyaoqingrenshu.setText(bean.getAgent().getRemainInviteCount());
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getUpgradeAmout())){
                                    upgradeAmout = bean.getAgent().getUpgradeAmout();
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getUpgradeName())){
                                    upgradeName = bean.getAgent().getUpgradeName();
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getYesterdayAdd())){
                                    txt_1.setText(bean.getAgent().getYesterdayAdd());
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getTodayAdd())){
                                    txt_2.setText(bean.getAgent().getTodayAdd());
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getYesterdayCommission())){
                                    txt_3.setText(bean.getAgent().getYesterdayCommission());
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getTodayCommission())){
                                    txt_4.setText(bean.getAgent().getTodayCommission());
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getYesterdayTrade())){
                                    txt_5.setText(bean.getAgent().getYesterdayTrade());
                                }
                                if (!TextUtils.isEmpty(bean.getAgent().getTodayTrade())){
                                    txt_6.setText(bean.getAgent().getTodayTrade());
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private Call<ResponseBody> getAgentConfigCall;
    private void getAgentConfig() {//获取代理背景/代理费
        getAgentConfigCall = VHttpServiceManager.getInstance().getVService().getAgentConfig();
        getAgentConfigCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    DailiBannerBean bean = gson.fromJson(resultData.data,DailiBannerBean.class);
                    if (null!=bean){
                        if (!TextUtils.isEmpty(bean.getAgentFee())){
                            price = bean.getAgentFee();
                        }else{
                            price = "1000";
                        }
                        if (null!=bean.getBanner()&&bean.getBanner().size()>0){
                            if (!TextUtils.isEmpty(bean.getBanner().get(0).getImageUrl())){
                                Glide.with(DailiActivity.this).load(bean.getBanner().get(0).getImageUrl()).into(imgDaili);
                            }else{
                                Glide.with(DailiActivity.this).load(R.drawable.daili_bg).into(imgDaili);
                            }
                        }else{
                            Glide.with(DailiActivity.this).load(R.drawable.daili_bg).into(imgDaili);
                        }
                    }
                }
            }
        });
    }

    private Call<ResponseBody> getAgentApplyCall;
    private void getAgentApply() {//申请成为代理
        getAgentApplyCall = VHttpServiceManager.getInstance().getVService().getAgentApply();
        getAgentApplyCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    txt_title.setText(getResources().getString(R.string.chengweidaili));
                    reDaili.setVisibility(View.VISIBLE);
                    txtChengweidaili.setVisibility(View.GONE);
                    txtShenhezhong.setVisibility(View.VISIBLE);
                    ll_dailiyongjin.setVisibility(View.GONE);
                }
            }
        });
    }

    private Call<ResponseBody> getLijishengjiCall;
    private void getLijishengji() {//立即升级
        getLijishengjiCall = VHttpServiceManager.getInstance().getVService().getLijishengji();
        getLijishengjiCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    getAgentInfo();
                }
            }
        });
    }
}
package com.bitcnew.module.home;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.MainApplication;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.bean.ChicangDetailBean;
import com.bitcnew.module.home.trade.TradeLeverActivity;
import com.bitcnew.module.home.trade.TradeLeverActivity2;
import com.bitcnew.module.login.LoginActivity;
import com.bitcnew.module.wallet.dialog.SetLossFragmentDialog;
import com.bitcnew.module.wallet.dialog.SetWinFragmentDialog2;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.StockChartUtil;
import com.bitcnew.util.TjrMinuteTaskPool;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ChicangDetailActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.img_fanhui)
    ImageView imgFanhui;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.tvProfitRate)
    TextView tvProfitRate;
    @BindView(R.id.tvCostPrice)
    TextView tvCostPrice;
    @BindView(R.id.tvOpenDealAmount)
    TextView tvOpenDealAmount;
    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.tvLastPrice)
    TextView tvLastPrice;
    @BindView(R.id.ivMark)
    ImageView ivMark;
    @BindView(R.id.llLastPrice)
    LinearLayout llLastPrice;
    @BindView(R.id.tvCloseOrderCommon)
    TextView tvCloseOrderCommon;
    @BindView(R.id.tvCloseOrder)
    TextView tvCloseOrder;
    @BindView(R.id.txt_shezhi)
    TextView txtShezhi;


    private Call<ResponseBody> getPrybarDetailCall;
    private Call<ResponseBody> updateWinCall;
    private Call<ResponseBody> updateLossCall;
    private SetWinFragmentDialog2 SetWinFragmentDialog2;
    private SetLossFragmentDialog setLossFragmentDialog;
    private TjrMinuteTaskPool tjrMinuteTaskPool;

    private String symbol;
    private ChicangDetailBean orderInfo;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_chicang_detail;
    }

    @Override
    protected String getActivityTitle() {
        return symbol;
    }

    public static void pageJump(Context context, String symbol) {
        Bundle bundle = new Bundle();
        bundle.putString("symbol", symbol);
        PageJumpUtil.pageJump(context, ChicangDetailActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("symbol")) {
                symbol = bundle.getString("symbol", "");
                txtTitle.setText(symbol);
            }
        }

        imgFanhui.setOnClickListener(this);
        llLastPrice.setOnClickListener(this);
        txtShezhi.setOnClickListener(this);
        tvCloseOrderCommon.setOnClickListener(this);
        tvCloseOrder.setOnClickListener(this);
        tjrMinuteTaskPool = new TjrMinuteTaskPool();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetPrybarDetailCall();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        closeTimer();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseTimer();
        closeTimer();
    }

    boolean isRun;

    private void startTimer() {
        if (tjrMinuteTaskPool == null) {
            tjrMinuteTaskPool = new TjrMinuteTaskPool();
        }
        isRun = true;
        tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());

    }

    private void closeTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
    }

    private void releaseTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.release();
    }

    private Runnable scheduledTask = new Runnable() {
        public void run() {
            try {
                startGetPrybarDetailCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!MainApplication.isRun) {
                tjrMinuteTaskPool.closeTime();
            }
        }
    };


    private void startGetPrybarDetailCall() {
        CommonUtil.cancelCall(getPrybarDetailCall);
        getPrybarDetailCall = VHttpServiceManager.getInstance().getVService().prybarDetail2(symbol);
        getPrybarDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    orderInfo = gson.fromJson(resultData.data, ChicangDetailBean.class);
                    setData();
                }
            }
        });
    }

    private void startUpdateWinCall(final String stopWin,final String stopWin2) {
        CommonUtil.cancelCall(updateWinCall);
        updateWinCall = VHttpServiceManager.getInstance().getVService().prybarCreateOrder2(symbol, "sell", stopWin, stopWin2, "limit");
        updateWinCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (SetWinFragmentDialog2 != null) SetWinFragmentDialog2.dismiss();
                    CommonUtil.showmessage(resultData.msg, ChicangDetailActivity.this);
                    startGetPrybarDetailCall();
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startUpdateWinCall(stopWin,stopWin2);
            }
        });
    }

    private void startUpdateLossCall(final String stopLoss, String payPass) {
        CommonUtil.cancelCall(updateLossCall);
        updateLossCall = VHttpServiceManager.getInstance().getVService().updateLossPrice2(symbol, stopLoss, payPass);
        updateLossCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (setLossFragmentDialog != null) setLossFragmentDialog.dismiss();
                    CommonUtil.showmessage(resultData.msg, ChicangDetailActivity.this);
                    startGetPrybarDetailCall();
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startUpdateLossCall(stopLoss, pwString);
            }
        });
    }

    private void setData() {
        if (orderInfo != null) {
            if (!TextUtils.isEmpty(orderInfo.getData().getProfit())){
                tvProfit.setText(StockChartUtil.formatWithSign(orderInfo.getData().getProfit()));
                int color = StockChartUtil.getRateTextColor(this, Double.parseDouble(orderInfo.getData().getProfit()));
                tvProfit.setTextColor(color);
                tvProfitRate.setTextColor(color);
            }else {
                tvProfit.setText("");
            }
            if (!TextUtils.isEmpty(orderInfo.getData().getProfitRate())){
                tvProfitRate.setText(StockChartUtil.formatWithSign(orderInfo.getData().getProfitRate()) + "%");
            }else {
                tvProfitRate.setText("0%");
            }
            if (!TextUtils.isEmpty(orderInfo.getData().getPrice())){
                tvCostPrice.setText(orderInfo.getData().getPrice());
            }else {
                tvCostPrice.setText("-");
            }
            if (!TextUtils.isEmpty(orderInfo.getData().getAmount())){
                if (!TextUtils.isEmpty(orderInfo.getData().getAvailableAmount())){
                    tvOpenDealAmount.setText(orderInfo.getData().getAmount() + "/" + orderInfo.getData().getAvailableAmount());
                }else {
                    tvOpenDealAmount.setText(orderInfo.getData().getAmount() + "/0");
                }
            }else {
                tvOpenDealAmount.setText("0/0" );
            }
            if (!TextUtils.isEmpty(orderInfo.getData().getSymbol())){
                tvSymbol.setText(orderInfo.getData().getSymbol() + getResources().getString(R.string.xianjia));
            }else {
                tvSymbol.setText(getResources().getString(R.string.xianjia));
            }
            if (!TextUtils.isEmpty(orderInfo.getData().getLast())){
                if (!TextUtils.isEmpty(orderInfo.getData().getRate())){
                    tvLastPrice.setText(orderInfo.getData().getLast() + "   " + StockChartUtil.formatWithSign(orderInfo.getData().getRate()) + "%");
                }else {
                    tvLastPrice.setText(orderInfo.getData().getLast() + "   "  + "0%");
                }
            }else {
                tvLastPrice.setText("0%");
            }
            if (!TextUtils.isEmpty(orderInfo.getData().getRate())){
                tvLastPrice.setTextColor(StockChartUtil.getRateTextColor(this, Double.parseDouble(orderInfo.getData().getRate())));
                if (Double.parseDouble(orderInfo.getData().getRate()) >= 0) {
                    ivMark.setImageResource(R.drawable.ic_mark_red);
                } else {
                    ivMark.setImageResource(R.drawable.ic_mark_green);
                }
            }
            if (!isRun) {
                startTimer();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_fanhui:
                finish();
                break;
            case R.id.txt_shezhi:
                if (orderInfo == null) return;
                SetWinFragmentDialog2 = SetWinFragmentDialog2.newInstance();
                SetWinFragmentDialog2.setSetStopWinListen(new SetWinFragmentDialog2.SetStopWinListen() {
                    @Override
                    public void goSetStopWin(String stopWin,String stopWin2) {
                        startUpdateWinCall(stopWin,stopWin2);
                    }
                });
                SetWinFragmentDialog2.show(getSupportFragmentManager(), "");
                break;
            case R.id.tvCloseOrder:
                if (isLogin()) {
                    TradeLeverActivity2.pageJump(this, orderInfo.getData().getSymbol(), -1);
                } else {
                    LoginActivity.login(ChicangDetailActivity.this);
                }
                break;
            case R.id.tvCloseOrderCommon:
                if (isLogin()) {
                    TradeLeverActivity2.pageJump(this, orderInfo.getData().getSymbol(), 1);
                } else {
                    LoginActivity.login(ChicangDetailActivity.this);
                }
                break;
            case R.id.llLastPrice:
                if (orderInfo != null)
                    MarketActivity.pageJump(ChicangDetailActivity.this, orderInfo.getData().getSymbol(), 1, "", true);
                break;

        }
    }

}

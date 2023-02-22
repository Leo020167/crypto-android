package com.bitcnew.module.wallet;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.CommonToolbar;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.home.trade.TradeLeverActivity2;
import com.bitcnew.module.wallet.adapter.CloseListAdapter;
import com.bitcnew.module.wallet.dialog.CloseOrderFragmentDialog;
import com.bitcnew.module.wallet.dialog.SetLossFragmentDialog;
import com.bitcnew.module.wallet.dialog.SetWinFragmentDialog;
import com.bitcnew.module.wallet.entity.OrderInfo;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.TjrMinuteTaskPool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class LeverInfo1Activity extends TJRBaseToolBarSwipeBackActivity {

    public static void pageJump(Context context, Position data, long orderId,String type) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERID, orderId);
        bundle.putString("type",type);
        bundle.putSerializable("data", data);
        PageJumpUtil.pageJump(context, LeverInfo1Activity.class, bundle);
    }

    @BindView(R.id.commonToolbar)
    CommonToolbar commonToolbar;
    @BindView(R.id.balanceTv)
    TextView balanceTv;
    @BindView(R.id.ableBalanceTv)
    TextView ableBalanceTv;
    @BindView(R.id.freezeBalanceTv)
    TextView freezeBalanceTv;

    private Call<ResponseBody> getPrybarDetailCall;
    private Call<ResponseBody> updateWinCall;
    private Call<ResponseBody> updateLossCall;
    private Call<ResponseBody> prybarCloseOrderCall;
    private SetWinFragmentDialog setWinFragmentDialog;
    private SetLossFragmentDialog setLossFragmentDialog;
    private CloseOrderFragmentDialog closeOrderFragmentDialog;

    private TjrMinuteTaskPool tjrMinuteTaskPool;

    private Position position;
    private long orderId;
    private OrderInfo orderInfo;
    private String type;//币币的时候传2，其他不传
    private CloseListAdapter closeListAdapter;

    @OnClick(R.id.action_setWin)
    public void onSetWinClick() {
        if (orderInfo == null) return;
        setWinFragmentDialog = SetWinFragmentDialog.newInstance(orderInfo.stopWinPrice, orderInfo.priceDecimals);
        setWinFragmentDialog.setSetStopWinListen(new SetWinFragmentDialog.SetStopWinListen() {
            @Override
            public void goSetStopWin(String stopWin) {
                startUpdateWinCall(stopWin, "");
            }
        });
        setWinFragmentDialog.show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.action_setLoss)
    public void onSetLossClick() {
        if (orderInfo == null) return;
        setLossFragmentDialog = SetLossFragmentDialog.newInstance(orderInfo.stopLossPrice, orderInfo.priceDecimals);
        setLossFragmentDialog.setSetStopLossListen(new SetLossFragmentDialog.SetStopLossListen() {
            @Override
            public void goSetStopLoss(String stopLoss) {
                startUpdateLossCall(stopLoss, "");
            }
        });
        setLossFragmentDialog.show(getSupportFragmentManager(), "");
    }

    @OnClick(R.id.action_buy)
    public void onBuyClick() {
//        if (accountType.equals("local")){
//            TradeLeverActivity2.pageJump(this, data.symbol, 2);
//        }else{
            TradeLeverActivity2.pageJump(this, orderInfo.symbol, 1);
//        }
    }

    @OnClick(R.id.action_sell)
    public void onSellClick() {
        TradeLeverActivity2.pageJump(this, orderInfo.symbol, -1);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_wallet_lever_info;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    public void setData(Position data) {
        commonToolbar.setTitle(data.symbol);
        balanceTv.setText(data.amount);
        ableBalanceTv.setText(data.availableAmount);
        freezeBalanceTv.setText(data.frozenAmount);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ORDERID)) {
                orderId = bundle.getLong(CommonConst.ORDERID, 0);
            }
            if (bundle.containsKey("type")) {
                type = bundle.getString("type","");
            }
            if (bundle.containsKey("data")) {
                position = (Position) bundle.getSerializable("data");
                setData(position);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetPrybarDetailCall();
    }

    private void startGetPrybarDetailCall() {
        CommonUtil.cancelCall(getPrybarDetailCall);
        getPrybarDetailCall = VHttpServiceManager.getInstance().getVService().prybarDetail(orderId, position.symbol);
        getPrybarDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    orderInfo = resultData.getObject("data", OrderInfo.class);
                    setData();
                }
            }
        });
    }

    private void setData() {
    }

    private void startUpdateWinCall(final String stopWin, String payPass) {
        CommonUtil.cancelCall(updateWinCall);
        updateWinCall = VHttpServiceManager.getInstance().getVService().updateWinPrice(orderId, stopWin, payPass);
        updateWinCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (setWinFragmentDialog != null) setWinFragmentDialog.dismiss();
                    com.bitcnew.util.CommonUtil.showmessage(resultData.msg, getContext());
                    startGetPrybarDetailCall();
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startUpdateWinCall(stopWin, pwString);
            }
        });
    }

    private void startUpdateLossCall(final String stopLoss, String payPass) {
        CommonUtil.cancelCall(updateLossCall);
        updateLossCall = VHttpServiceManager.getInstance().getVService().updateLossPrice(orderId, stopLoss, payPass);
        updateLossCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (setLossFragmentDialog != null) setLossFragmentDialog.dismiss();
                    com.bitcnew.util.CommonUtil.showmessage(resultData.msg, getContext());
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

}

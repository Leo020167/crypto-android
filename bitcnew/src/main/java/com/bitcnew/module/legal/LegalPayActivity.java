package com.bitcnew.module.legal;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.photo.ViewPagerPhotoViewActivity;
import com.bitcnew.data.sharedpreferences.PrivateChatSharedPreferences;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.module.chat.ChatRoomActivity;
import com.bitcnew.module.home.trade.dialog.InoutMarkPayDialog;
import com.bitcnew.module.home.trade.entity.ChatStaff;
import com.bitcnew.module.legal.adapter.ShowUserTipAdapter;
import com.bitcnew.module.legal.dialog.OtcOrderExitPayDialog;
import com.bitcnew.module.legal.entity.OtcOrderInfo;
import com.bitcnew.module.legal.entity.OtcOrderToPayResult;
import com.bitcnew.subpush.ReceiveModel;
import com.bitcnew.subpush.ReceiveModelTypeEnum;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.BadgeView;
import com.bitcnew.widgets.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 向卖家付款页面
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class LegalPayActivity extends TJRBaseActionBarSwipeBackObserverActivity implements View.OnClickListener {


    @BindView(R.id.llChat)
    LinearLayout llChat;
    @BindView(R.id.tvTolCny)
    TextView tvTolCny;
    @BindView(R.id.llCopyTolCny)
    LinearLayout llCopyTolCny;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvSellerName)
    TextView tvSellerName;
    //    @BindView(R.id.tvShowUserTip)
//    TextView tvShowUserTip;
    @BindView(R.id.llCopyName)
    LinearLayout llCopyName;
    @BindView(R.id.tvAccountType)
    TextView tvAccountType;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvOrOrBank)
    TextView tvOrOrBank;
    @BindView(R.id.ivQr)
    ImageView ivQr;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.llBankName)
    LinearLayout llBankName;
    @BindView(R.id.llQrOrBank)
    LinearLayout llQrOrBank;
    @BindView(R.id.tvAlertTip)
    TextView tvAlertTip;
    @BindView(R.id.tvReceivedConfirm)
    TextView tvReceivedConfirm;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
    @BindView(R.id.rvShowUserTip)
    RecyclerView rvShowUserTip;
    @BindView(R.id.llSeller)
    LinearLayout llSeller;


    private InoutMarkPayDialog inoutMarkPayDialog;
    private OtcOrderExitPayDialog otcOrderExitPayDialog;


    private Call<ResponseBody> getInoutMarkPayCall;
    private Call<ResponseBody> orderCancelCall;
    private Call<ResponseBody> getOrderInfoCall;
    private Call<ResponseBody> getOrderCashDetailCall;

    private OtcOrderToPayResult otcOrderToPayResult;

    private long orderCashId;
    private long showUserId;
    private long showPaymentId;

    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private CountDownTimer timer;

    private ChatStaff chatStaff;//私聊用到


    private BadgeView badgeChat;


    @Override
    protected void handlerMsg(ReceiveModel model) {
        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
            case private_chat_record:  //收到一条新信息
                setChatNewsCount();
                break;

            default:
                break;
        }
    }

    public static void pageJump(Context context, long orderCashId, long showUserId, long showPaymentId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERCASHID, orderCashId);
        bundle.putLong(CommonConst.SHOWUSERID, showUserId);
        bundle.putLong(CommonConst.SHOWPAYMENTID, showPaymentId);
        PageJumpUtil.pageJump(context, LegalPayActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.legal_pay;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }


    @Override
    protected void showReConnection() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.unpaid_info);
        ButterKnife.bind(this);
        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ORDERCASHID)) {
                orderCashId = bundle.getLong(CommonConst.ORDERCASHID, 0l);
            }

            if (bundle.containsKey(CommonConst.SHOWUSERID)) {
                showUserId = bundle.getLong(CommonConst.SHOWUSERID, 0l);
            }

            if (bundle.containsKey(CommonConst.SHOWPAYMENTID)) {
                showPaymentId = bundle.getLong(CommonConst.SHOWPAYMENTID, 0l);
            }
        }
        if (orderCashId == 0l) {
            CommonUtil.showmessage(getResources().getString(R.string.canshucuowu), this);
            finish();
            return;
        }

        badgeChat = new BadgeView(this, llChat);
        badgeChat.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));
        badgeChat.setBadgeMargin(15, 10);
        badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        startOtcGetOrderDetail();
        startTradeCashOrderDetail();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("setChatNewsCount", "onResume==");
        setChatNewsCount();

    }

    public void showPrivateChatNewsCount(String chatTopic) {
        int chatCount = 0;
        if (getApplicationContext().getUser() != null) {
            chatCount = PrivateChatSharedPreferences.getPriChatRecordNum(getApplicationContext(), chatTopic, getApplicationContext().getUser().getUserId());
        }
        Log.d("setChatNewsCount", "chatCount==" + chatCount);
        if (chatCount > 0) {//显示
            badgeChat.show();
            badgeChat.setBadgeText(com.bitcnew.util.CommonUtil.setNewsCount(chatCount));
        } else {//不显示
            badgeChat.hide();
        }


    }


    private void setData() {
        setChatNewsCount();

        if (otcOrderToPayResult != null) {
            if (!TextUtils.isEmpty(currencySign)){
                tvTolCny.setText(currencySign + otcOrderToPayResult.tolPrice);
            }else {
                tvTolCny.setText(otcOrderToPayResult.tolPrice);
            }
            startCountDownTime();

            tvDesc.setText(otcOrderToPayResult.payTip);
            tjrImageLoaderUtil.displayImage(otcOrderToPayResult.showUserLogo, ivHead);
            tvUserName.setText(otcOrderToPayResult.showUserName);
            if (!TextUtils.isEmpty(otcOrderToPayResult.showUserTip)) {
                ShowUserTipAdapter showUserTipAdapter = new ShowUserTipAdapter(this);
                rvShowUserTip.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                rvShowUserTip.setAdapter(showUserTipAdapter);
                showUserTipAdapter.setData(otcOrderToPayResult.showUserTip.split("，"));
            }
            tvSellerName.setText(otcOrderToPayResult.showRealName);
            tvAccount.setText(otcOrderToPayResult.receiptNo);

            if (otcOrderToPayResult.receiptType == 1 || otcOrderToPayResult.receiptType == 2) {
                tvOrOrBank.setText(getResources().getString(R.string.shoukuanma));
                tvAccountType.setText(otcOrderToPayResult.receiptTypeValue + getResources().getString(R.string.zhanghao));
                tjrImageLoaderUtil.displayImage(otcOrderToPayResult.qrCode, ivQr);
                llBankName.setVisibility(View.GONE);
            } else if (otcOrderToPayResult.receiptType == 3) {
                tvOrOrBank.setText(getResources().getString(R.string.kaihuyinhang));
                tvAccountType.setText(otcOrderToPayResult.receiptTypeValue + getResources().getString(R.string.hao));
                llBankName.setVisibility(View.VISIBLE);
                tvBankName.setText(otcOrderToPayResult.bankName);
            }
            tvAlertTip.setText(otcOrderToPayResult.alertTip);
            llQrOrBank.setOnClickListener(this);
            llAccount.setOnClickListener(this);
            llCopyName.setOnClickListener(this);

            llCopyTolCny.setOnClickListener(this);
            tvReceivedConfirm.setOnClickListener(this);

            llChat.setOnClickListener(this);
            llSeller.setOnClickListener(this);
        }


    }

    private void startCountDownTime() {
        if (otcOrderToPayResult == null) return;
        if (otcOrderToPayResult.paySecondTime > 0) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new CountDownTimer(otcOrderToPayResult.paySecondTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] time = com.bitcnew.util.VeDate.formatSecToTime(millisUntilFinished / 1000);
                    if (time != null && time.length == 4) {
                        tvTime.setText(time[2] + ":" + time[3]);
                        if (otcOrderExitPayDialog != null && otcOrderExitPayDialog.isShowing()) {
                            otcOrderExitPayDialog.setTime(time[2] + ":" + time[3]);
                        }
                    }
                }

                public void onFinish() {
                    tvTime.setText("00:00");
                    com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.dingdanyichaoshi), LegalPayActivity.this);

                }
            };
            timer.start();
        } else {
            tvTime.setText("00:00");
        }
    }


    private void setChatNewsCount() {
        Log.d("setChatNewsCount", "setChatNewsCount.....chatStaff==" + chatStaff);

        if (chatStaff != null) {
            Log.d("setChatNewsCount", "setChatNewsCount.....chatStaff.chatTopic==" + chatStaff.chatTopic);
            showPrivateChatNewsCount(chatStaff.chatTopic);
        }
    }

    private void showInOutMarkDialog() {
        if (inoutMarkPayDialog == null) {
            inoutMarkPayDialog = new InoutMarkPayDialog(this) {
                @Override
                public void onclickOk() {
                    if (otcOrderToPayResult != null) {
                        startInoutMarkPay();
                    }
                }
            };
        }
        inoutMarkPayDialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            otcOrderToPayResult = null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCopyName:
                com.bitcnew.util.CommonUtil.copyText(this, tvSellerName.getText().toString());
                break;
            case R.id.llChat:
            case R.id.llSeller:
                if (chatStaff != null) {
                    ChatRoomActivity.pageJump(this, chatStaff.chatTopic, chatStaff.userName, chatStaff.headUrl);
                }
                break;
            case R.id.llQrOrBank:
                if (otcOrderToPayResult != null) {
                    if (otcOrderToPayResult.receiptType == 1 || otcOrderToPayResult.receiptType == 2) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(CommonConst.PAGETYPE, 6);
                        bundle.putString(CommonConst.SINGLEPICSTRING, otcOrderToPayResult.qrCode);
                        PageJumpUtil.pageJumpToData(LegalPayActivity.this, ViewPagerPhotoViewActivity.class, bundle);
                    } else if (otcOrderToPayResult.receiptType == 3) {
                        com.bitcnew.util.CommonUtil.copyText(this, tvBankName.getText().toString());
                    }
                }
                break;
            case R.id.llAccount:
                com.bitcnew.util.CommonUtil.copyText(this, tvAccount.getText().toString());
                break;
            case R.id.llCopyTolCny:
                if (otcOrderToPayResult != null)
                    com.bitcnew.util.CommonUtil.copyText(this, otcOrderToPayResult.tolPrice);
                break;
            case R.id.tvReceivedConfirm:
                showInOutMarkDialog();
                break;

        }
    }

    private void showExitTipsDialog() {
        otcOrderExitPayDialog = new OtcOrderExitPayDialog(this) {
            @Override
            public void onclickOk() {
                PageJumpUtil.finishCurr(LegalPayActivity.this);
            }
        };
        otcOrderExitPayDialog.show();
    }


    @Override
    public void onBackPressed() {
        showExitTipsDialog();
    }

    private void startInoutMarkPay() {
        CommonUtil.cancelCall(getInoutMarkPayCall);
        getInoutMarkPayCall = VHttpServiceManager.getInstance().getVService().otcToMarkPayOrderSuccess(orderCashId);
        getInoutMarkPayCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, LegalPayActivity.this);
                    PageJumpUtil.finishCurr(LegalPayActivity.this);

                }
            }
        });
    }

    private OtcOrderInfo orderCash;
    private String currencySign;
    private void startOtcGetOrderDetail() {
        CommonUtil.cancelCall(getOrderInfoCall);
        getOrderInfoCall = VHttpServiceManager.getInstance().getVService().otcGetOrderDetail(orderCashId);
        getOrderInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    orderCash = resultData.getObject("order", OtcOrderInfo.class);
                    currencySign = orderCash.currencySign;
                }
            }
        });
    }
    private void startTradeCashOrderDetail() {
        CommonUtil.cancelCall(getOrderCashDetailCall);
        getOrderCashDetailCall = VHttpServiceManager.getInstance().getVService().otcToPayOrder(orderCashId, showUserId, showPaymentId);
        getOrderCashDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    chatStaff = resultData.getObject("chatStaff", ChatStaff.class);
                    otcOrderToPayResult = resultData.getObject("orderToPayResult", OtcOrderToPayResult.class);
                    setData();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


}

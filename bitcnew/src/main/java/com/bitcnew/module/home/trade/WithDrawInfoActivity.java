package com.bitcnew.module.home.trade;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.chat.ChatRoomActivity;
import com.bitcnew.subpush.ReceiveModel;
import com.bitcnew.subpush.ReceiveModelTypeEnum;
import com.bitcnew.util.DateUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.BadgeView;
import com.bitcnew.R;
import com.bitcnew.data.sharedpreferences.PrivateChatSharedPreferences;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.trade.entity.ChatStaff;
import com.bitcnew.module.home.trade.entity.OrderCashStateEnum;
import com.bitcnew.module.myhome.entity.OrderCash;
import com.bitcnew.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提现详情页面
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class WithDrawInfoActivity extends TJRBaseActionBarSwipeBackObserverActivity implements View.OnClickListener {

    @BindView(R.id.ivState)
    ImageView ivState;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.llCopyTolBalance)
    LinearLayout llCopyTolBalance;
    @BindView(R.id.tvTolBalance)
    TextView tvTolBalance;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPayWayText)
    TextView tvPayWayText;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvPayAccount)
    TextView tvPayAccount;
    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.tvOrderTime)
    TextView tvOrderTime;

    @BindView(R.id.tvStateDetail)
    TextView tvStateDetail;

    @BindView(R.id.llChat)
    LinearLayout llChat;

    private Call<ResponseBody> getOrderCashDetailCall;

    private OrderCash orderCash;

    private long orderCashId;

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


    public static void pageJump(Context context, long orderCashId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERCASHID, orderCashId);
        PageJumpUtil.pageJump(context, WithDrawInfoActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.with_draw_info;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.unpaid_info);
        ButterKnife.bind(this);
        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ORDERCASHID)) {
                orderCashId = bundle.getLong(CommonConst.ORDERCASHID, 0l);
            }
        }
        if (orderCashId == 0l) {
            CommonUtil.showmessage(getResources().getString(R.string.canshucuowu), this);
            finish();
            return;
        }

        llCopyTolBalance.setOnClickListener(this);

        badgeChat = new BadgeView(this, llChat);
        badgeChat.setBadgeMargin(0, 0);
        badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);

        startTradeCashOrderDetail();

    }

    @Override
    protected void showReConnection() {
    }

    private void setChatNewsCount() {
        if (chatStaff != null) {
            showPrivateChatNewsCount(chatStaff.chatTopic);
        }
    }

    public void showPrivateChatNewsCount(String chatTopic) {
        int chatCount = 0;
        if (getApplicationContext().getUser() != null) {
            chatCount = PrivateChatSharedPreferences.getPriChatRecordNum(getApplicationContext(), chatTopic, getApplicationContext().getUser().getUserId());
        }
        if (chatCount > 0) {//显示
            badgeChat.show();
            badgeChat.setBadgeText(com.bitcnew.util.CommonUtil.setNewsCount(chatCount));
        } else {//不显示
            badgeChat.hide();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        setChatNewsCount();

    }

    private void setData() {
        setChatNewsCount();
        if (orderCash != null && orderCash.orderCashId > 0 && orderCash.receipt != null) {
            tvTolBalance.setText("HK$ " + orderCash.balanceCny);
            tvPrice.setText("HK$ " + orderCash.priceCny);
            tvNum.setText(orderCash.amount + " USDT");

            tvPayWay.setText(orderCash.receipt.receiptTypeValue);
            tvName.setText(orderCash.receipt.receiptName);
            tvPayAccount.setText(orderCash.receipt.receiptNo);
            tvOrderNo.setText(String.valueOf(orderCash.orderCashId));
            tvOrderTime.setText(DateUtils.getStringDateOfString2(String.valueOf(orderCash.createTime), DateUtils.TEMPLATE_yyyyMMdd_HHmmss));

            if (orderCash.receipt.receiptType == 1 || orderCash.receipt.receiptType == 2) {
                if (orderCash.receipt.receiptType == 1) {
                    tvPayWayText.setText(getResources().getString(R.string.zhifubaozhanghao));
                } else {
                    tvPayWayText.setText(getResources().getString(R.string.weixinzhanghao));
                }

            } else {
                tvPayWayText.setText(getResources().getString(R.string.yinhangkahao));
            }

            OrderCashStateEnum orderCashStateEnum = OrderCashStateEnum.getOrderCashState(orderCash.state);
            ivState.setImageResource(orderCashStateEnum.getIcon());
            tvState.setText(orderCash.stateDesc);

            if (orderCash.state == OrderCashStateEnum.wait_pay.getState()) {
                tvStateDetail.setText(getResources().getString(R.string.tixianshenqingyitijiao));
            } else {
                tvStateDetail.setText(getResources().getString(R.string.tixianyiwancheng));
            }

            llChat.setOnClickListener(this);

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            orderCash = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llChat:
                if (chatStaff != null) {
                    ChatRoomActivity.pageJump(this, chatStaff.chatTopic, chatStaff.userName, chatStaff.headUrl);
                }
                break;


        }
    }


    private void startTradeCashOrderDetail() {
        CommonUtil.cancelCall(getOrderCashDetailCall);
        getOrderCashDetailCall = VHttpServiceManager.getInstance().getVService().tradeCashOrderDetail(orderCashId);
        getOrderCashDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    chatStaff = resultData.getObject("chatStaff", ChatStaff.class);
                    orderCash = resultData.getObject("order", OrderCash.class);
                    setData();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

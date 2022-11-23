package com.bitcnew.module.legal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.data.sharedpreferences.PrivateChatSharedPreferences;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.legal.LegalOrderInfoActivity;
import com.bitcnew.module.legal.entity.OtcOrderHistory;
import com.bitcnew.util.DateUtils;
import com.bitcnew.widgets.BadgeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 历史记录
 * Created by zhengmj on 18-10-26.
 */

public class OtcOrderHistoryAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<OtcOrderHistory> {


    private Context context;
    private OnItemClick onItemClick;
    private long myUserId;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public OtcOrderHistoryAdapter(Context context, long myUserId) {
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
        this.myUserId = myUserId;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.otc_order_history_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tvInOut)
        TextView tvInOut;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvNum)
        TextView tvNum;
        @BindView(R.id.txt_jiaoyijinedanwei)
        TextView txt_jiaoyijinedanwei;
        @BindView(R.id.tvTolCny)
        TextView tvTolCny;
        @BindView(R.id.llItem)
        LinearLayout llItem;
        @BindView(R.id.llChat)
        LinearLayout llChat;


        private BadgeView badgeChat;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            badgeChat = new BadgeView(context, llChat);
            badgeChat.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));
            badgeChat.setBadgeMargin(15, 20);
            badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        }

        public void setData(final OtcOrderHistory data, final int pos) {

            tvInOut.setText(data.buySellValue);
            tvState.setText(data.stateValue);
            if (data.state == 0 || data.state == 1 || data.state == 2) {
                tvState.setTextColor(ContextCompat.getColor(context, R.color.c6175ae));
            } else {
                tvState.setTextColor(ContextCompat.getColor(context, R.color.c9a9a9a));
            }
            tvNum.setText(data.amount);
            txt_jiaoyijinedanwei.setText(context.getResources().getString(R.string.jiaoyijine)+"("+data.currencySign+")");
            tvTolCny.setText(data.tolPrice);
            tvTime.setText(DateUtils.getStringDateOfString2(data.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmm));
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("setChatNewsCount", "chatCount==" + data.chatTopic);
                    LegalOrderInfoActivity.pageJump(context, data.orderId);
//                    if (onItemClick != null) onItemClick.onItemClickListen(pos, data);
                }
            });
            showPrivateChatNewsCount(data.chatTopic);
//            io.reactivex.Observable.create(new ObservableOnSubscribe<Integer>() {
//                @Override
//                public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//                    int chatCount = 0;
//                    if (myUserId > 0 && !TextUtils.isEmpty(data.chatTopic)) {
//                        chatCount = PrivateChatSharedPreferences.getPriChatRecordNum(context, data.chatTopic, myUserId);
//                    }
//                     e.onNext(chatCount);
//                }
//            }).subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<Integer>() {
//                        @Override
//                        public void accept(Integer chatCount) throws Exception {
//                            Log.d("setChatNewsCount", "chatCount==" + chatCount+"  chatTopic=="+data.chatTopic);
//                            if (chatCount > 0) {//显示
//                                badgeChat.show();
//                                badgeChat.setBadgeText(com.bitcnew.util.CommonUtil.setNewsCount(chatCount));
//                            } else {//不显示
//                                badgeChat.hide();
//                            }
//                        }
//                    });

        }

        public void showPrivateChatNewsCount(String chatTopic) {
            int chatCount = 0;
            if (myUserId > 0 && !TextUtils.isEmpty(chatTopic)) {
                chatCount = PrivateChatSharedPreferences.getPriChatRecordNum(context, chatTopic, myUserId);
            }
            Log.d("setChatNewsCount", "chatCount==" + chatCount);
            if (chatCount > 0) {//显示
                badgeChat.show();
                badgeChat.setBadgeText(com.bitcnew.util.CommonUtil.setNewsCount(chatCount));
            } else {//不显示
                badgeChat.hide();
            }
        }
    }
//
//    private void showCancelTipsDialog(final TakeCoinHistory data, final int pos) {
//        cancelTipsDialog = new TjrBaseDialog(context) {
//            @Override
//            public void onclickOk() {
//                dismiss();
//                startWithdrawCoinCancel(data, pos);
//            }
//
//            @Override
//            public void onclickClose() {
//                dismiss();
//            }
//
//            @Override
//            public void setDownProgress(int progress) {
//
//            }
//        };
//        cancelTipsDialog.setMessage("确定撤销订单");
//        cancelTipsDialog.setBtnOkText("撤销");
//        cancelTipsDialog.setTitleVisibility(View.GONE);
//        cancelTipsDialog.show();
//    }


//    private void startWithdrawCoinCancel(final TakeCoinHistory data, final int pos) {
//        CommonUtil.cancelCall(tradeCancelCall);
//        tradeCancelCall = VHttpServiceManager.getInstance().getVService().withdrawCoinCancel(data.dwId);
//        tradeCancelCall.enqueue(new MyCallBack(context) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    CommonUtil.showmessage(resultData.msg, context);
//                    data.state = OrderStateEnum.canceled.getState();
//                    notifyItemChanged(pos);
////                    removeItem(pos);
//                }
//            }
//
//        });
//    }
}

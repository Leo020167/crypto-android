package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.util.DateUtils;
import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.data.sharedpreferences.PrivateChatSharedPreferences;
import com.bitcnew.module.home.trade.OrderCashInfoActivity;
import com.bitcnew.module.home.trade.WithDrawInfoActivity;
import com.bitcnew.module.myhome.entity.OrderCash;
import com.bitcnew.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class UsdtTradeHisAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<OrderCash> {


    private Context context;
    private long myUserId;

    public UsdtTradeHisAdapter(Context context, long myUserId) {
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
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.usdt_trade_his_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOrientation)
        TextView tvOrientation;

        @BindView(R.id.tvOrientationText)
        TextView tvOrientationText;

        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.tvMoney)
        TextView tvMoney;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.llItem)
        LinearLayout llItem;
        @BindView(R.id.ivArrow)
        ImageView ivArrow;
        @BindView(R.id.viewPoint)
        View viewPoint;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final OrderCash data) {
            if (data.buySell == 1) {
                if (data.symbol.equals("USDT")) {
                    tvOrientation.setText(context.getResources().getString(R.string.chongzhi));
                    tvOrientationText.setText(context.getResources().getString(R.string.chongzhijineyuan));
                } else {
                    tvOrientation.setText(context.getResources().getString(R.string.mairu));
                    tvOrientationText.setText(context.getResources().getString(R.string.mairujineyuan));
                }
                tvOrientation.setTextColor(ContextCompat.getColor(context, R.color.quotation_die_color));
//                tvMoney.setText(data.balanceCash);
            } else {
                if (data.symbol.equals("USDT")) {
                    tvOrientation.setText(context.getResources().getString(R.string.tixian));
                    tvOrientationText.setText(context.getResources().getString(R.string.tixianjineyuan));
                } else {
                    tvOrientation.setText(context.getResources().getString(R.string.maichu));
                    tvOrientationText.setText(context.getResources().getString(R.string.maichujineyuan));
                }

                tvOrientation.setTextColor(ContextCompat.getColor(context, R.color.quotation_zhang_color));

//                tvMoney.setText(data.amountCash);
            }

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.buySell == 1) {
                        OrderCashInfoActivity.pageJump(context, data.orderCashId);
                    } else {
                        WithDrawInfoActivity.pageJump(context, data.orderCashId);
                    }

                }
            });
            tvMoney.setText(data.balanceCny);
            tvSymbol.setText(data.symbol);
            tvState.setText(data.stateDesc);
            tvAmount.setText(data.amount);
            tvTime.setText(DateUtils.getStringDateOfString2(String.valueOf(data.createTime), DateUtils.TEMPLATE_yyyyMMdd_HHmm));

            String chatTopic = CommonUtil.getChatTop(myUserId, data.handleUid, data.orderCashId);
            int chatCount = PrivateChatSharedPreferences.getPriChatRecordNum(context, chatTopic, myUserId);
            viewPoint.setVisibility(chatCount > 0 ? View.VISIBLE : View.GONE);


        }
    }
}

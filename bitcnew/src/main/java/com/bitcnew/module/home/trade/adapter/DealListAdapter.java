package com.bitcnew.module.home.trade.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.AMBaseRecycleAdapter;
import com.bitcnew.module.home.trade.entity.MarkDeal;
import com.bitcnew.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class DealListAdapter extends AMBaseRecycleAdapter<MarkDeal> {


    private Context context;


    public DealListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.mark_deal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvBuySell)
        TextView tvBuySell;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvAmount)
        TextView tvAmount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(MarkDeal data) {

            tvTime.setText(DateUtils.getStringDateOfString2(data.time, DateUtils.TEMPLATE_HHmmss));
            tvBuySell.setText("buy".equals(data.buySell) ? context.getResources().getString(R.string.kanzhangzuoduo) : context.getResources().getString(R.string.kandiezuokong));
            if("buy".equals(data.buySell)){
                tvBuySell.setText(context.getResources().getString(R.string.kanzhangzuoduo));
                tvBuySell.setTextColor(ContextCompat.getColor(context,R.color.quotation_zhang_color));
            }else if("sell".equals(data.buySell)){
                tvBuySell.setText(context.getResources().getString(R.string.kandiezuokong));
                tvBuySell.setTextColor(ContextCompat.getColor(context,R.color.quotation_die_color));
            }
            tvPrice.setText(data.price);
            tvAmount.setText(data.amount);
        }
    }


}

package com.bitcnew.widgets.quotitian.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitcnew.common.base.adapter.AMBaseRecycleAdapter;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.widgets.quotitian.entity.StarArkBidBean;
import com.bitcnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class StarDetailBuySellAdapter extends AMBaseRecycleAdapter<StarArkBidBean> {


    private Context context;
    private int buySell;

    private OnItemClick onItemClick;

    public StarDetailBuySellAdapter(Context context, int buySell) {
        this.context = context;
        this.buySell = buySell;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.star_detail_buy_sell_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvBuyAmount)
        TextView tvBuyAmount;
        @BindView(R.id.tvBuyPrice)
        TextView tvBuyPrice;
        @BindView(R.id.llItem)
        FrameLayout llItem;
        @BindView(R.id.pb)
        ProgressBar pb;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final StarArkBidBean data, final int position) {
            if (data.isEmpty) {
                tvBuyPrice.setText("----");
                tvBuyAmount.setText("----");
                pb.setMax(100);
                pb.setProgress(0);
                llItem.setOnClickListener(null);
            } else {
                tvBuyPrice.setText(data.price);
                if (buySell == 1) {
                    tvBuyPrice.setTextColor(ContextCompat.getColor(context, R.color.quotation_zhang_color));
                } else {
                    tvBuyPrice.setTextColor(ContextCompat.getColor(context, R.color.quotation_die_color));
                }
                tvBuyAmount.setText(data.amount);
                llItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClick != null) onItemClick.onItemClickListen(position, data);
                    }
                });
                pb.setMax(100);
                pb.setProgress(data.depthRate);
            }

        }
    }
}

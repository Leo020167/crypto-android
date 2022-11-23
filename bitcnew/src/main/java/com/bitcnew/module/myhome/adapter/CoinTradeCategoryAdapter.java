package com.bitcnew.module.myhome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.AMBaseRecycleAdapter;
import com.bitcnew.module.myhome.entity.CoinTradeCount;
import com.bitcnew.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class CoinTradeCategoryAdapter extends AMBaseRecycleAdapter<CoinTradeCount> {


    private Context context;


    public CoinTradeCategoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.coin_trade_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCoinName)
        TextView tvCoinName;
        @BindView(R.id.tvCount)
        TextView tvCount;
        @BindView(R.id.tvProfit)
        TextView tvProfit;

        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(CoinTradeCount data, int pos) {
            tvCoinName.setText(data.symbol);
            tvCount.setText(data.num);
            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));

//            if (pos % 2 == 0) {
//                llItem.setBackgroundResource(R.color.cf4f5f6);
//            }else{
//                llItem.setBackgroundResource(R.color.white);
//
//            }
        }
    }


}

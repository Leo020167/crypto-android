package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.http.base.Group;
import com.bitcnew.module.home.ChicangDetailActivity;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.home.trade.adapter.TradeCurrPositionAdapter;
import com.bitcnew.module.wallet.LeverInfo1Activity;
import com.bitcnew.module.wallet.LeverInfoActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TradeCurrPositionAdapter3  extends BaseImageLoaderRecycleAdapter<Position> {

    private Context context;

    public TradeCurrPositionAdapter3(Context context) {
        super(R.drawable.ic_common_mic2);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_position_item3, parent, false));
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bibi, parent, false));
    }


    @Override
    public void setGroup(Group<Position> g) {

        super.setGroup(g);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.tvSymbol)
//        TextView tvSymbol;
//        @BindView(R.id.tvBuySell)
//        TextView tvBuySell;
//        @BindView(R.id.tvProfitCash)
//        TextView tvProfitCash;
//        @BindView(R.id.tvAmount)
//        TextView tvAmount;
//        @BindView(R.id.tvOpenPrice)
//        TextView tvOpenPrice;
//        @BindView(R.id.tvProfit)
//        TextView tvProfit;
//        @BindView(R.id.llItem)
//        LinearLayout llItem;

        final TextView coinTypeTv;
        final TextView usdtBalanceTv;
        final TextView ableBalanceTv;
        final TextView freezeBalanceTv;

        public ViewHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);

            coinTypeTv = itemView.findViewById(R.id.coinTypeTv);
            usdtBalanceTv = itemView.findViewById(R.id.usdtBalanceTv);
            ableBalanceTv = itemView.findViewById(R.id.ableBalanceTv);
            freezeBalanceTv = itemView.findViewById(R.id.freezeBalanceTv);
        }

        public void setData(final Position data) {
//            tvSymbol.setText(CommonUtil.getOriginSymbol(data.symbol));
//            if (!TextUtils.isEmpty( data.buySellValue)){
//                tvBuySell.setText( "•" + data.buySellValue);
//            }else {
//                tvBuySell.setText( "");
//            }
//
//            tvProfitCash.setText(StockChartUtil.formatWithSign(data.profitRate) + "%");
//            tvAmount.setText(""+data.amount);
//            tvOpenPrice.setText(String.valueOf(data.price));
//            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));
//
//            if (!TextUtils.isEmpty(data.profit)) {
//                int color=StockChartUtil.getRateTextColor(context, Double.parseDouble(data.profit));
//                tvProfitCash.setTextColor(color);
//                tvProfit.setTextColor(color);
//            }
//
//            llItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ChicangDetailActivity.pageJump(context,data.symbol);
//                }
//            });

            coinTypeTv.setText(data.symbol);
            coinTypeTv.setText(data.symbol);
            usdtBalanceTv.setText("≈ " + data.usdtAmount + " USDT");
            ableBalanceTv.setText(data.availableAmount);
            freezeBalanceTv.setText(data.frozenAmount);
            itemView.setOnClickListener(v -> {
//                ChicangDetailActivity.pageJump(context,data.symbol);
                LeverInfo1Activity.pageJump(context, data, data.orderId, "2");
            });
        }
    }
}

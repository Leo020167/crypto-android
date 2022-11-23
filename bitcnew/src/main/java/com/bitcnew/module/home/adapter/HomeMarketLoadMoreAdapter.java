package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.module.home.MarketActivity;
import com.bitcnew.module.home.entity.Market;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HomeMarketLoadMoreAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<Market> {


    private Context context;
    private int isLever;
//    private String accountType;

    public HomeMarketLoadMoreAdapter(Context context, int isLever) {
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
        this.context = context;
        this.isLever = isLever;
//        this.accountType = accountType;
    }


    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_market_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvSubSymbol)
        TextView tvSubSymbol;
        @BindView(R.id.tvTips)
        TextView tvTips;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvSymbolName)
        TextView tvSymbolName;
        @BindView(R.id.tvRate)
        TextView tvRate;
        @BindView(R.id.tv24H)
        TextView tv24H;

        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Market data, int position) {
            if (position % 2 == 0) {
                llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf6f7f8));
            } else {
                llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            Log.d("HomeMarketAdapter", "data.symbol==" + data.symbol);
            int index = data.symbol.indexOf("/");
            if (index > 0) {
                tvSymbol.setText(CommonUtil.getOriginSymbol(data.symbol));
                tvSubSymbol.setText("/" + CommonUtil.getUnitSymbol(data.symbol));
            } else {
                tvSymbol.setText(data.symbol);
                tvSubSymbol.setText("");
            }
            tvSymbolName.setText(data.name);

            if (!TextUtils.isEmpty(data.tip)) {
                tvTips.setVisibility(View.VISIBLE);
                tvTips.setText(data.tip);
            } else {
                tvTips.setVisibility(View.GONE);
            }

            tvPrice.setText(data.price);
            tvRate.setText(StockChartUtil.formatNumWithSign(2, Double.parseDouble(data.rate), true) + "%");
//            tvRate.setTextColor(StockChartUtil.getRateTextColor(context,Double.parseDouble(data.rate)));
            tvRate.setBackgroundResource(StockChartUtil.getRateBg(context,Double.parseDouble(data.rate)));
            tv24H.setText(context.getResources().getString(R.string.liang) + data.amount);


            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MarketActivity.pageJump(context, data.symbol, isLever,"");
                }
            });
        }
    }
}

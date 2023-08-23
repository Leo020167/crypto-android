package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.http.base.Group;
import com.bitcnew.module.home.MarketActivity;
import com.bitcnew.module.home.MarketActivity2;
import com.bitcnew.module.home.entity.Market;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.StockChartUtil;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HomeMarketAdapter extends BaseImageLoaderRecycleAdapter<Market> {

    private Context context;
    private int isLever;
    private String accountType;//
//    private String accountType;

    public HomeMarketAdapter(Context context, int isLever,String accountType) {
        super(R.drawable.ic_common_mic2);
        this.context = context;
        this.isLever = isLever;
        this.accountType = accountType;
//        this.accountType = accountType;
    }

    @Override
    public void setGroup(Group<Market> g) {
        group = g;
        for (int i = 0, count = getItemCount(); i < count; i++) {
            notifyItemInserted(i);
        }
    }

    public void update(Group<Market> group) {
        List<Market> currentGroup = getGroup();
        if (null == currentGroup || currentGroup.size() <= 0) {
            setGroup(group);
            return;
        }

        Market current;
        Market newer;
        for (int i = 0; i < currentGroup.size() && i < group.size(); i++) {
            current = currentGroup.get(i);
            newer = group.get(i);

            if (Objects.equals(current.symbol, newer.symbol) && Objects.equals(current.rate, newer.rate)) {
                continue;
            }
            getGroup().set(i, newer);
            if (Objects.equals(current.symbol, newer.symbol)) {
                notifyItemChanged(i, new String[]{current.rate, newer.rate});
            } else {
                notifyItemChanged(i);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_market_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgLogo)
        ImageView imgLogo;
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

        private Market data;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public double getRate() {
            if (null != data) {
                return Double.parseDouble(data.rate);
            }
            return 0;
        }

        public void setData(final Market data, int position) {
            this.data = data;

            if (position % 2 == 0) {
                llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf6f7f8));
            } else {
                llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            Log.d("HomeMarketAdapter", "data.symbol==" + data.symbol);
            if (null != data.image) {
                Glide.with(context).load(data.image).into(imgLogo);
                imgLogo.setVisibility(View.VISIBLE);
            } else {
                imgLogo.setVisibility(View.GONE);
            }
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
            tv24H.setText(context.getResources().getString(R.string.liang)+ data.amount);


            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(accountType)){
                        if (accountType.equals("spot")){//跳cayle的行情页
                            MarketActivity2.pageJump(context, data.symbol, isLever,"");
                        }else {
                            MarketActivity.pageJump(context, data.symbol, isLever,accountType);
                        }
                    }else {
                        MarketActivity.pageJump(context, data.symbol, isLever,accountType);
                    }
                }
            });
        }
    }
}

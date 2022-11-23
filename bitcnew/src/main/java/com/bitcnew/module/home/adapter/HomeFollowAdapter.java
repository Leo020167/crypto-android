package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.module.copy.CropyOrderInfoActivity;
import com.bitcnew.module.myhome.UserHomeActivity;
import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.module.home.entity.HomeCopyOrder;
import com.bitcnew.util.StockChartUtil;
import com.bitcnew.widgets.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HomeFollowAdapter extends BaseImageLoaderRecycleAdapter<HomeCopyOrder> {


    private Context context;

    public HomeFollowAdapter(Context context) {
        super(R.drawable.ic_common_mic2);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_follow_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivHead)
        CircleImageView ivHead;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvBalance)
        TextView tvBalance;
        @BindView(R.id.tvProfit)
        TextView tvProfit;
//        @BindView(R.id.tvProfitRate)
//        TextView tvProfitRate;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final HomeCopyOrder data) {

            displayImageForHead(data.copyHeadUrl, ivHead);
            tvName.setText(data.copyName);
            tvBalance.setText(data.tolBalance);
            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));
            if (!TextUtils.isEmpty(data.profit)) {
//                tvProfitRate.setText(StockChartUtil.formatNumWithSign(2, Double.parseDouble(data.profitRate), true) + "%");
                tvProfit.setTextColor(StockChartUtil.getRateTextColor(context, Double.parseDouble(data.profit)));
            }
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CropyOrderInfoActivity.pageJump(context, data.orderId);
                }
            });
            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserHomeActivity.pageJump(context,data.copyUid);
                }
            });


        }
    }
}

package com.bitcnew.module.copy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.module.copy.CropyOrderInfoActivity;
import com.bitcnew.util.DateUtils;
import com.bitcnew.widgets.CircleImageView;
import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.copy.entity.CopyOrderHistory;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 跟单历史记录
 * Created by zhengmj on 18-10-26.
 */

public class CopyOrderHistoryAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<CopyOrderHistory> {


    private Context context;
    private Call<ResponseBody> tradeCancelCall;

    private OnItemClick onItemClick;
    private TjrBaseDialog cancelTipsDialog;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public CopyOrderHistoryAdapter(Context context) {
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.copy_order_history_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivHead)
        CircleImageView ivHead;
        @BindView(R.id.tvName)
        TextView tvName;
        //        @BindView(R.id.tvProfitCash)
//        TextView tvProfitCash;
        @BindView(R.id.tvProfit)
        TextView tvProfit;
        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CopyOrderHistory data) {

            displayImageForHead(data.copyHeadUrl, ivHead);
            tvName.setText(data.copyName);
            tvProfit.setText(StockChartUtil.formatWithSign(Double.parseDouble(data.profit)));
//            tvProfitRate.setText(data.profitRate + "%");
//            tvProfitRate.setText(StockChartUtil.formatNumWithSign(2, Double.parseDouble(data.profitRate), true) + "%");
            tvProfit.setTextColor(StockChartUtil.getRateTextColor(context, Double.parseDouble(data.profit)));

            tvTime.setText(DateUtils.getStringDateOfString2(data.doneTime, DateUtils.TEMPLATE_yyyyMMdd_HHmmss));
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CropyOrderInfoActivity.pageJump(context, data.orderId);
                }
            });

        }
    }

}

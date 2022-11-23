package com.bitcnew.module.copy.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.http.widget.view.RoundAngleImageView;
import com.bitcnew.module.copy.entity.CopyOrderPieChart;
import com.bitcnew.widgets.PieChartView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class CopyPieChartAdapter extends BaseImageLoaderRecycleAdapter<CopyOrderPieChart> {


    private Context context;

    public CopyPieChartAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.copy_pie_chart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position),position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivColor)
        RoundAngleImageView ivColor;
        @BindView(R.id.tvRate)
        TextView tvRate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CopyOrderPieChart data,int pos) {
            tvRate.setText(data.symbol + "  " + data.rate + "%");
            ivColor.setImageDrawable(new ColorDrawable(PieChartView.colors[pos]));
            ivColor.setradis(4,4,4,4);
        }
    }
}

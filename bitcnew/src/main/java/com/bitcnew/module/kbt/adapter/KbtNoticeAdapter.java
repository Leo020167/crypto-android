package com.bitcnew.module.kbt.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.common.web.CommonWebViewActivity;
import com.bitcnew.module.kbt.entity.KbtNotice;
import com.bitcnew.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class KbtNoticeAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<KbtNotice> {


    private Context context;

    public KbtNoticeAdapter(Context context) {
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.kbt_notice_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvIsImportant)
        TextView tvIsImportant;
        @BindView(R.id.tvSubTitle)
        TextView tvSubTitle;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final KbtNotice data) {
            tvTitle.setText(data.title);
            if ("1".equals(data.isTop)) {
                tvIsImportant.setVisibility(View.VISIBLE);
            } else {
                tvIsImportant.setVisibility(View.GONE);
            }
            tvTime.setText(DateUtils.getStringDateOfString2(String.valueOf(data.createTime), DateUtils.TEMPLATE_yyyyMMdd_HHmm));
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(context, data.url);
                }
            });
//            tvKBTBalance.setText(StockChartUtil.formatWithSign(data.amount));
//            tvWay.setText(data.tradeTypeDesc);

        }
    }
}

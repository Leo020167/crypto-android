package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.module.home.entity.Attention;
import com.bitcnew.module.myhome.UserHomeActivity;
import com.bitcnew.util.DateUtils;
import com.bitcnew.widgets.CircleImageView;
import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HomeAttentionAdapter extends BaseImageLoaderRecycleAdapter<Attention> {


    private Context context;

    public HomeAttentionAdapter(Context context) {
        super(R.drawable.ic_common_mic2);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_attention_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivhead)
        CircleImageView ivhead;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDays)
        TextView tvDays;
        @BindView(R.id.tvRenew)
        TextView tvRenew;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.llRenew)
        LinearLayout llRenew;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Attention data) {
            displayImageForHead(data.headUrl, ivhead);
            tvName.setText(data.userName);
            tvDays.setText(context.getResources().getString(R.string.yijingruzhu) + data.days + context.getResources().getString(R.string.tian));

            if (data.subIsFee == 0) {
                llRenew.setVisibility(View.GONE);
            } else {
                llRenew.setVisibility(View.VISIBLE);
                if (data.isExpireTime == 0) {
                    tvTime.setText(context.getResources().getString(R.string.daoqi)+ DateUtils.getStringDateOfString2(String.valueOf(data.expireTime), DateUtils.TEMPLATE_yyyyMMdd_divide));
                    tvTime.setTextColor(ContextCompat.getColor(context, R.color.c1d3155));
                } else {
                    tvTime.setText(context.getResources().getString(R.string.dingyueyiguoqi));
                    tvTime.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
            }


            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserHomeActivity.pageJump(context, data.userId);
                }
            });

        }
    }
}

package com.bitcnew.module.legal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.myhome.entity.Receipt;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 修改收款方式
 * Created by zhengmj on 18-10-26.
 */

public class ResetReceiptTermAdapter extends BaseImageLoaderRecycleAdapter<Receipt> {


    private Context context;

    private OnItemClick onItemClick;


    public ResetReceiptTermAdapter(Context context) {
        super(R.drawable.ic_common_mic2);
        this.context = context;
    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.reset_receipt_term_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivReceiptLogo)
        ImageView ivReceiptLogo;
        @BindView(R.id.tvWay)
        TextView tvWay;
        @BindView(R.id.tvReceiptDesc)
        TextView tvReceiptDesc;
        @BindView(R.id.llWay)
        LinearLayout llWay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Receipt data, final int pos) {
            displayImageRound(data.receiptLogo, ivReceiptLogo);
            tvWay.setText(data.receiptTypeValue);
            if (!TextUtils.isEmpty(data.receiptDesc)) {
                tvReceiptDesc.setVisibility(View.VISIBLE);
                tvReceiptDesc.setText(data.receiptDesc);
            } else {
                tvReceiptDesc.setVisibility(View.GONE);
            }
            llWay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.onItemClickListen(pos, data);
                    }
                }
            });
        }
    }


}

package com.bitcnew.module.myhome.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.module.myhome.AddBankPayActivity;
import com.bitcnew.module.myhome.entity.AddPaymentTern;
import com.bitcnew.R;
import com.bitcnew.module.myhome.AddAliPayAndWechatPayActivity;
import com.bitcnew.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class AddReceiptAdapter extends BaseImageLoaderRecycleAdapter<AddPaymentTern> {


    private Context context;
    private int from;

    public AddReceiptAdapter(Context context,int from) {
        super(R.drawable.ic_common_mic2);
        this.context = context;
        this.from = from;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.add_receipt_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivReceiptLogo)
        ImageView ivReceiptLogo;
        @BindView(R.id.tvWay)
        TextView tvWay;
        @BindView(R.id.llWay)
        LinearLayout llWay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final AddPaymentTern data) {
            displayImageRound(data.receiptLogo, ivReceiptLogo);
            tvWay.setText(data.receiptTypeValue);
            llWay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.receiptType == 1 || data.receiptType == 2) {
                        AddAliPayAndWechatPayActivity.pageJump(context, data.receiptType,from);
                    } else if (data.receiptType == 3) {
                        AddBankPayActivity.pageJump(context, data.receiptType,from);
                    }else{
                        CommonUtil.showmessage(context.getResources().getString(R.string.weizhileixing),context);
                    }
                }
            });
        }
    }
}

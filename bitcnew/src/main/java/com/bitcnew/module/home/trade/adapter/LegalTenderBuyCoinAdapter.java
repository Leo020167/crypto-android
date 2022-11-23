package com.bitcnew.module.home.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.trade.entity.OtcEntity;
import com.bitcnew.util.DensityUtil;
import com.bitcnew.widgets.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class LegalTenderBuyCoinAdapter extends BaseImageLoaderRecycleAdapter<OtcEntity> {


    private Context context;
    private OnItemClick onItemClick;

    public LegalTenderBuyCoinAdapter(Context context) {
        super(R.drawable.ic_common_mic2);
        this.context = context;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.legal_tender_item, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivHead)
        CircleImageView ivHead;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvQuota)
        TextView tvQuota;
        @BindView(R.id.llPayWay)
        LinearLayout llPayWay;
        @BindView(R.id.tvPriceText)
        TextView tvPriceText;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvBuy)
        TextView tvBuy;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final OtcEntity data, final int pos) {
            displayImage(data.headUrl, ivHead);
            tvName.setText(data.userName);

//            tvAmount.setText("数量"+data.);

            tvQuota.setText(context.getResources().getString(R.string.xiane)+"HK$" + data.minCny + "- HK$" + data.maxCny );
            tvPrice.setText("HK$ " + data.price);

            llPayWay.removeAllViews();
            if (data.receiptList != null) {
                Log.d("LegalTenderAdapter", "data.receiptList==" + data.receiptList.size());
                for (int i = 0, m = data.receiptList.size(); i < m; i++) {
                    ImageView imageView = new ImageView(context);
                    displayImage(data.receiptList.get(i).receiptLogo, imageView);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 18), DensityUtil.dip2px(context, 18));
                    lp.rightMargin = DensityUtil.dip2px(context, 5);
                    llPayWay.addView(imageView, lp);

                }
            }
            tvBuy.setOnClickListener(new View.OnClickListener() {
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

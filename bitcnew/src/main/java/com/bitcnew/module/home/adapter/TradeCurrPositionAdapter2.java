package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.bean.MiningInfoBean;
import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.http.base.Group;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.wallet.LeverInfoActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.StockChartUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TradeCurrPositionAdapter2 extends BaseImageLoaderRecycleAdapter<MiningInfoBean.ProfitsBean> {

    private Context context;

    public TradeCurrPositionAdapter2(Context context) {
        super(R.drawable.ic_common_mic2);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TradeCurrPositionAdapter2.ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_position_item2, parent, false));
    }


    @Override
    public void setGroup(Group<MiningInfoBean.ProfitsBean> g) {

        super.setGroup(g);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TradeCurrPositionAdapter2.ViewHolder holder1 = (TradeCurrPositionAdapter2.ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_shuliang)
        TextView txt_shuliang;
        @BindView(R.id.txt_shijian)
        TextView txt_shijian;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final MiningInfoBean.ProfitsBean data) {
            long ti = Long.parseLong(data.getCreateTime());
            long ti2 = ti*1000;
            String time = getDateToString(ti2,"yyyy-MM-dd HH:mm");
            txt_shuliang.setText(data.getProfit());
            txt_shijian.setText(time);

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    long id = Long.getLong(data.getId());
//                    LeverInfoActivity.pageJump(context, id);
                }
            });
        }
    }
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
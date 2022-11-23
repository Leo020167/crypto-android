package com.bitcnew.module.login.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.AMBaseRecycleAdapter;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.login.entity.AreaCode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class CountryCodeAdapter extends AMBaseRecycleAdapter<AreaCode> {


    private OnItemClick onItemClick;
    private Context context;

    public CountryCodeAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick=onItemClick;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.country_code_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position),position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvCode)
        TextView tvCode;
        @BindView(R.id.llItem)
        LinearLayout llItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final AreaCode data, final int pos) {
            tvName.setText(data.tcName);
            tvCode.setText(data.areaCode);
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClick!=null){
                        onItemClick.onItemClickListen(pos,data);
                    }
                }
            });

        }
    }
}

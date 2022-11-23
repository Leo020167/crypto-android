package com.bitcnew.module.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bitcnew.R;
import com.bitcnew.module.home.bean.WithdrawGetInfoBean;

import java.util.ArrayList;
import java.util.List;

public class TakeCoinAdapter extends RecyclerView.Adapter<TakeCoinAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<WithdrawGetInfoBean.Infos> list = new ArrayList<>();
    private Context context;

    public TakeCoinAdapter(Context context, List<WithdrawGetInfoBean.Infos> list){
        this.mInflater=LayoutInflater.from(context);
        this.context = context;
        this.list=list;
    }

    /**
     * item显示类型
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.item_tale_coin,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    /**
     * 数据的绑定显示
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        WithdrawGetInfoBean.Infos item = list.get(position);
        if (item.isSel()){
            holder.tvKeyType.setTextColor(context.getResources().getColor(R.color.c6175ae));
            holder.tvKeyType.setBackgroundResource(R.drawable.border_lanse_2dpline);
        }else {
            holder.tvKeyType.setTextColor(context.getResources().getColor(R.color.gray));
            holder.tvKeyType.setBackgroundResource(R.drawable.border_huise_2dpline);
        }
        if (!TextUtils.isEmpty(item.getType())){
            holder.tvKeyType.setText("USDT-"+item.getType());
        }else{
            holder.tvKeyType.setText("--");
        }


        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemPlayClick!=null){
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSel(false);
                    }
                    list.get(position).setSel(true);
                    onItemPlayClick.onSelClick(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_item;
        public TextView tvKeyType;
        public ViewHolder(View view){
            super(view);
            ll_item= (LinearLayout)view.findViewById(R.id.ll_item);
            tvKeyType = (TextView)view.findViewById(R.id.tvKeyType);
        }
    }

    public interface OnPlayClickListener {
        void onSelClick(int pos);
    }

    OnPlayClickListener onItemPlayClick;

    public void setOnPlayClickListener(OnPlayClickListener onItemPlayClick) {
        this.onItemPlayClick = onItemPlayClick;
    }
}


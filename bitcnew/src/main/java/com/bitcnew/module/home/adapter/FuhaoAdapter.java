package com.bitcnew.module.home.adapter;

import android.annotation.SuppressLint;
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
import com.bitcnew.module.home.bean.FollowGetTypesBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FuhaoAdapter extends RecyclerView.Adapter<FuhaoAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<String> list = new ArrayList<>();
    private Context context;

    public FuhaoAdapter(Context context,List<String> list){
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
        View view=mInflater.inflate(R.layout.item_fuhao,parent,false);
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
        String item = list.get(position);
        if (!TextUtils.isEmpty(item)){
            holder.txt_title.setText(item);
        }else{
            holder.txt_title.setText("");
        }
        holder.txt_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemPlayClick!=null){
                    onItemPlayClick.onSelClick(holder.getAdapterPosition());
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
        public TextView txt_title;
        public ViewHolder(View view){
            super(view);
            txt_title = (TextView)view.findViewById(R.id.txt_title);
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

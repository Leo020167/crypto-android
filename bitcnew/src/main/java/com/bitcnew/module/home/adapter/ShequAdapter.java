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
import com.bitcnew.module.home.bean.InviteHomeBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShequAdapter  extends RecyclerView.Adapter<ShequAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<InviteHomeBean.InviteListBean> list = new ArrayList<>();
    private Context context;

    public ShequAdapter(Context context,List<InviteHomeBean.InviteListBean> list){
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
        View view=mInflater.inflate(R.layout.item_shequ,parent,false);
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
        InviteHomeBean.InviteListBean item = list.get(position);

        if (!TextUtils.isEmpty(item.getInviteCode())){
            holder.txt_1.setText(item.getInviteCode());
        }else{
            holder.txt_1.setText("-");
        }
        if (!TextUtils.isEmpty(item.getStatus())){
            if (item.getStatus().equals("0")){
                holder.txt_2.setText(context.getResources().getString(R.string.weishiyong));
                holder.txt_3.setText("-");
                holder.txt_4.setText("-");
            }else {
                holder.txt_2.setText(context.getResources().getString(R.string.yishiyong));
                if (!TextUtils.isEmpty(item.getInviteUserId())){
                    holder.txt_3.setText(item.getInviteUserId());
                }else{
                    holder.txt_3.setText("-");
                }
                if (!TextUtils.isEmpty(item.getAmount())){
                    holder.txt_4.setText(item.getAmount());
                }else{
                    holder.txt_4.setText("-");
                }
            }
        }else{
            holder.txt_2.setText("-");
            holder.txt_3.setText("-");
            holder.txt_4.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_1;
        public TextView txt_2;
        public TextView txt_3;
        public TextView txt_4;

        public ViewHolder(View view){
            super(view);
            txt_1 = (TextView)view.findViewById(R.id.txt_1);
            txt_2 = (TextView)view.findViewById(R.id.txt_2);
            txt_3 = (TextView)view.findViewById(R.id.txt_3);
            txt_4 = (TextView)view.findViewById(R.id.txt_4);
        }
    }
}


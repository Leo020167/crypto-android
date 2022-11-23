package com.bitcnew.module.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.bean.TransformersBean;
import com.bitcnew.module.home.bean.FollowGetTypesBean;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFirstButtonAdapter extends RecyclerView.Adapter<HomeFirstButtonAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<TransformersBean.TransformersBean1> list = new ArrayList<>();
    private Context context;

    public HomeFirstButtonAdapter(Context context,List<TransformersBean.TransformersBean1> list){
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
        View view=mInflater.inflate(R.layout.item_home_first_button,parent,false);
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
        TransformersBean.TransformersBean1 item = list.get(position);
        if (!TextUtils.isEmpty(item.getImageUrl())){
            Glide.with(context).load(item.getImageUrl()).into(holder.img_otc1);
        }else{
            Glide.with(context).load(R.drawable.border_white_10dp).into(holder.img_otc1);
        }

        holder.re_otc1.setOnClickListener(new View.OnClickListener() {
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
        public CardView re_otc1;
        public ImageView img_otc1;

        public ViewHolder(View view){
            super(view);
            re_otc1= (CardView)view.findViewById(R.id.re_otc1);
            img_otc1 = (ImageView)view.findViewById(R.id.img_otc1);
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


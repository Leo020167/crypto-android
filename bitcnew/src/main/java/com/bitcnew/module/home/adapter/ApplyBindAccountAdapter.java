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

public class ApplyBindAccountAdapter extends RecyclerView.Adapter<ApplyBindAccountAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private List<FollowGetTypesBean.Types> list = new ArrayList<>();
    private Context context;

    public ApplyBindAccountAdapter(Context context,List<FollowGetTypesBean.Types> list){
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
        View view=mInflater.inflate(R.layout.item_apply_bind_account,parent,false);
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
        FollowGetTypesBean.Types item = list.get(position);
        if (item.isSel()){
            holder.img_check.setImageResource(R.drawable.yuan_sel);
        }else {
            holder.img_check.setImageResource(R.drawable.yuan_nosel);
        }
        int  a  =  position+1;
        holder.txt_title.setText(context.getResources().getString(R.string.gendanleixing)+a);
        if (!TextUtils.isEmpty(item.getProfitRate())){
            holder.txt_yinglifencheng.setText(context.getResources().getString(R.string.yinglifencheng)+item.getProfitRate()+"%");
        }else{
            holder.txt_yinglifencheng.setText(context.getResources().getString(R.string.yinglifencheng));
        }
        if (!TextUtils.isEmpty(item.getLossRate())){
            holder.txt_kuisunbutie.setText(context.getResources().getString(R.string.kuisunbutie)+item.getLossRate()+"%");
        }else{
            holder.txt_kuisunbutie.setText(context.getResources().getString(R.string.kuisunbutie));
        }

        if (!TextUtils.isEmpty(item.getMaxMultiNum())){
            holder.txt_zuigaobeishu.setText(context.getResources().getString(R.string.zuigaobeishu)+item.getMaxMultiNum());
        }else{
            holder.txt_zuigaobeishu.setText(context.getResources().getString(R.string.zuigaobeishu));
        }
        if (!TextUtils.isEmpty(item.getTokenAmount())){
            holder.txt_huafeitoken.setText(context.getResources().getString(R.string.xiaohaotoken)+item.getTokenAmount());
        }else{
            holder.txt_huafeitoken.setText(context.getResources().getString(R.string.xiaohaotoken));
        }

        if (!TextUtils.isEmpty(item.getLimit())){
            holder.txt_jine.setText(item.getLimit()+"USDT");
        }else{
            holder.txt_jine.setText("");
        }
        if (item.getIsBind()==1){
                long atime = item.getExpireTime()*1000;
                Date date = new Date(atime);
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String day = sf.format(date);
                holder.txt_daoqiri.setText(day);
                holder.txt_daoqiri.setVisibility(View.VISIBLE);
                holder.txt_daoqishijian.setVisibility(View.VISIBLE);
        }else{
            holder.txt_daoqiri.setVisibility(View.INVISIBLE);
            holder.txt_daoqishijian.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(item.getDuration())){
            holder.txt_shijian.setText(item.getDuration()+context.getResources().getString(R.string.tian));
        }else{
            holder.txt_shijian.setText("");
        }

        holder.ll_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemPlayClick!=null){
                    if (!isclick){
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).setSel(false);
                        }
                        list.get(position).setSel(true);
                        onItemPlayClick.onSelClick(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private boolean isclick;
    public void setONclick(boolean isclick){
        this.isclick=isclick;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_all;
        public ImageView img_check;
        public TextView txt_title;
        public TextView txt_yinglifencheng;
        public TextView txt_kuisunbutie;
        public TextView txt_zuigaobeishu;
        public TextView txt_huafeitoken;

        public TextView txt_jine;
        public TextView txt_shijian;
        public TextView txt_daoqiri;
        public TextView txt_daoqishijian;
        public ViewHolder(View view){
            super(view);
            ll_all= (LinearLayout)view.findViewById(R.id.ll_all);
            img_check = (ImageView)view.findViewById(R.id.img_check);
            txt_title = (TextView)view.findViewById(R.id.txt_title);
            txt_yinglifencheng = (TextView)view.findViewById(R.id.txt_yinglifencheng);
            txt_kuisunbutie = (TextView)view.findViewById(R.id.txt_kuisunbutie);
            txt_zuigaobeishu = (TextView)view.findViewById(R.id.txt_zuigaobeishu);
            txt_huafeitoken = (TextView)view.findViewById(R.id.txt_huafeitoken);
            txt_jine = (TextView)view.findViewById(R.id.txt_jine);
            txt_shijian = (TextView)view.findViewById(R.id.txt_shijian);
            txt_daoqiri = (TextView)view.findViewById(R.id.txt_daoqiri);
            txt_daoqishijian= (TextView) view.findViewById(R.id.txt_daoqishijian);
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


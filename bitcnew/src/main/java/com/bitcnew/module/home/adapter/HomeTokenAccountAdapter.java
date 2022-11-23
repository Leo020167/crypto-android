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
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.module.home.bean.RecordListBean;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeTokenAccountAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<RecordListBean.DataBean> {

    private Context context;
    public int type;

    public HomeTokenAccountAdapter(Context context,int type){
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
        this.type=type;
    }


    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }


    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home_token_account, parent, false));
    }

    @Override
    protected void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.re_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=onItemPlayClick){
                    if (!TextUtils.isEmpty(getItem(position).getOrderId())){
                        if (!getItem(position).getOrderId().equals("0")){
                            onItemPlayClick.onSelClick(getItem(position).getOrderId());
                        }
                    }
                }
            }
        });
        holder1.setData(context,getItem(position),type);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout re_item;
        public TextView txt_type;
        public TextView txt_time;
        public TextView txt_money;
        public TextView txt_id;

        public ViewHolder(View view){
            super(view);
            re_item = (LinearLayout)view.findViewById(R.id.re_item);
            txt_type = (TextView)view.findViewById(R.id.txt_type);
            txt_time = (TextView)view.findViewById(R.id.txt_time);
            txt_money = (TextView)view.findViewById(R.id.txt_money);
            txt_id = (TextView)view.findViewById(R.id.txt_id);
        }

        public void setData(Context context,final RecordListBean.DataBean data,int type) {
            long ti = Long.parseLong(data.getCreateTime());
            long ti2 = ti*1000;
            String time = getDateToString(ti2,"yyyy-MM-dd HH:mm");
            txt_time.setText(time);
            txt_type.setText(data.getRemark());
            if (data.getInOut().equals("1")){//增加 1，扣减 -1
                txt_money.setText(data.getAmount());
                txt_money.setTextColor(context.getResources().getColor(R.color.c14cc4B));
            }else {
                txt_money.setText(data.getAmount());
                txt_money.setTextColor(context.getResources().getColor(R.color.cc4311d));
            }
        }
    }

    public interface OnPlayClickListener {
        void onSelClick(String orderId);
    }
    OnPlayClickListener onItemPlayClick;

    public void setOnPlayClickListener(OnPlayClickListener onItemPlayClick) {
        this.onItemPlayClick = onItemPlayClick;
    }
}


package com.bitcnew.module.pledge.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.util.DateUtils;
import com.bitcnew.module.pledge.entity.PledgeRecord;

import java.util.List;

public class PledgeRecordListAdapter extends RecyclerView.Adapter<PledgeRecordListAdapter.ViewHolder> {

    private final Context context;
    private final List<PledgeRecord> list;

    public PledgeRecordListAdapter(Context context, List<PledgeRecord> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_pledge_record_list_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PledgeRecord item = getItem(position);

        holder.coinTypeTv.setText(item.getSymbol());
        holder.leijishouyiTv.setText(item.getPreProfit());
        holder.zhiyashuliangTv.setText(item.getCount());
        holder.zhiyakaishishijianTv.setText(DateUtils.formatDatetime(item.getStartTime()));
        holder.zhiyadaoqishijianTv.setText(DateUtils.formatDatetime(item.getEndTime()));
        holder.tianshuTv.setText(context.getString(R.string.tianshu, item.getDuration()));
    }

    public PledgeRecord getItem(int position) {
        return null != list && position < list.size() ? list.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView coinTypeTv;
        final TextView leijishouyiTv;
        final TextView zhiyashuliangTv;
        final TextView zhiyakaishishijianTv;
        final TextView zhiyadaoqishijianTv;
        final TextView tianshuTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coinTypeTv = itemView.findViewById(R.id.coinTypeTv);
            leijishouyiTv = itemView.findViewById(R.id.leijishouyiTv);
            zhiyashuliangTv = itemView.findViewById(R.id.zhiyashuliangTv);
            zhiyakaishishijianTv = itemView.findViewById(R.id.zhiyakaishishijianTv);
            zhiyadaoqishijianTv = itemView.findViewById(R.id.zhiyadaoqishijianTv);
            tianshuTv = itemView.findViewById(R.id.tianshuTv);
        }

    }
}

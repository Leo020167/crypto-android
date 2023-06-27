package com.bitcnew.module.pledge.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.module.pledge.entity.PledgeCoin;

import java.util.List;

public class PledgeCoinListAdapter extends RecyclerView.Adapter<PledgeCoinListAdapter.ViewHolder> {

    public interface OnZhiyaClickListener {
        void onZhiyaClick(PledgeCoin item);
    }

    private final Context context;
    private final List<PledgeCoin> list;

    private OnZhiyaClickListener onZhiyaClickListener;

    public PledgeCoinListAdapter(Context context, List<PledgeCoin> list) {
        this.context = context;
        this.list = list;
    }

    public OnZhiyaClickListener getOnZhiyaClickListener() {
        return onZhiyaClickListener;
    }

    public void setOnZhiyaClickListener(OnZhiyaClickListener onZhiyaClickListener) {
        this.onZhiyaClickListener = onZhiyaClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_pledge_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PledgeCoin item = getItem(position);
        holder.zhuixiaozhiyashuliangTv.setText(item.minCount);
        holder.zhiyazhouqiTv.setText(item.duration);
//        holder.yujishouyiTv.setText("??");
        holder.meirilixiTv.setText(item.profitRate + "%");
        holder.bizhongTv.setText(item.pledgeName);
        holder.action_zhiya.setOnClickListener(v -> {
            if (null != getOnZhiyaClickListener()) {
                getOnZhiyaClickListener().onZhiyaClick(item);
            }
        });
    }

    public PledgeCoin getItem(int position) {
        return null != list && list.size() > position ? list.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView bizhongTv;
        final TextView meirilixiTv;
        final TextView yujishouyiTv;
        final TextView zhiyazhouqiTv;
        final TextView zhuixiaozhiyashuliangTv;
        final TextView action_zhiya;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bizhongTv = itemView.findViewById(R.id.bizhongTv);
            meirilixiTv = itemView.findViewById(R.id.meirilixiTv);
            yujishouyiTv = itemView.findViewById(R.id.yujishouyiTv);
            zhiyazhouqiTv = itemView.findViewById(R.id.zhiyazhouqiTv);
            zhuixiaozhiyashuliangTv = itemView.findViewById(R.id.zhuixiaozhiyashuliangTv);
            action_zhiya = itemView.findViewById(R.id.action_zhiya);
        }
    }

}

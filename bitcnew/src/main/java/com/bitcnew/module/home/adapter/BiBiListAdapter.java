package com.bitcnew.module.home.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.module.home.entity.Position;

import java.util.List;

public class BiBiListAdapter extends RecyclerView.Adapter<BiBiListAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Position item, int position);
    }

    private final List<Position> list;
    private OnItemClickListener onItemClickListener;

    public BiBiListAdapter(List<Position> list) {
        this.list = list;
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_bibi, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Position item = getItem(position);
        holder.coinTypeTv.setText(item.symbol);
        holder.usdtBalanceTv.setText("≈ " + item.usdtAmount + " USDT");
        holder.ableBalanceTv.setText(item.availableAmount);
        holder.freezeBalanceTv.setText(item.frozenAmount);
        holder.itemView.setOnClickListener(v -> {
            if (null != getOnItemClickListener()) {
                getOnItemClickListener().onItemClick(item, position);
            }
        });
    }

    public Position getItem(int position) {
        if (null == list || position < 0 || position >= list.size()) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView coinTypeTv;
        final TextView usdtBalanceTv;
        final TextView ableBalanceTv;
        final TextView freezeBalanceTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coinTypeTv = itemView.findViewById(R.id.coinTypeTv);
            usdtBalanceTv = itemView.findViewById(R.id.usdtBalanceTv);
            ableBalanceTv = itemView.findViewById(R.id.ableBalanceTv);
            freezeBalanceTv = itemView.findViewById(R.id.freezeBalanceTv);
        }
    }
}

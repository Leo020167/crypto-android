package com.bitcnew.module.home.trade.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.module.home.trade.entity.TakeCoinAddress;

import java.util.List;

public class TakeCoinAddressListAdapter extends RecyclerView.Adapter<TakeCoinAddressListAdapter.ViewHolder> {

    public interface OnItemDelListener {
        void onItemDel(final int position, final TakeCoinAddress item);
    }

    public interface OnItemClickListener {
        void onItemClick(final int position, final TakeCoinAddress item);
    }

    private final List<TakeCoinAddress> list;

    private OnItemDelListener onItemDelListener;
    private OnItemClickListener onItemClickListener;

    public TakeCoinAddressListAdapter(List<TakeCoinAddress> list) {
        this.list = list;
    }

    public OnItemDelListener getOnItemDelListener() {
        return onItemDelListener;
    }

    public void setOnItemDelListener(OnItemDelListener onItemDelListener) {
        this.onItemDelListener = onItemDelListener;
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
        View itemView = inflater.inflate(R.layout.take_coin_address_list_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        onBindViewHolder(viewHolder, position, getItem(position));
    }

    protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, final TakeCoinAddress item) {
        holder.coinTypeTv.setText(item.getSymbol());
        holder.dizhiTv.setText(item.getAddress());
        holder.beizhuTv.setText(item.getRemark());
        holder.action_del.setOnClickListener(v -> {
            if (null != getOnItemDelListener()) {
                getOnItemDelListener().onItemDel(position, item);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            if (null != getOnItemClickListener()) {
                getOnItemClickListener().onItemClick(position, item);
            }
        });
    }

    public TakeCoinAddress getItem(int position) {
        return null != list && position >= 0 && position < list.size() ? list.get(position) : null;
    }

    @Override
    public int getItemCount() {
        return null == list ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView coinTypeTv;
        final TextView dizhiTv;
        final TextView beizhuTv;
        final View action_del;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coinTypeTv = itemView.findViewById(R.id.coinTypeTv);
            dizhiTv = itemView.findViewById(R.id.dizhiTv);
            beizhuTv = itemView.findViewById(R.id.beizhuTv);
            action_del = itemView.findViewById(R.id.action_del);
        }
    }
}

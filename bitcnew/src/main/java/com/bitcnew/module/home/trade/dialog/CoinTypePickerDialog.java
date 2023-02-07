package com.bitcnew.module.home.trade.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcnew.R;
import com.bitcnew.module.home.trade.entity.CoinConfig;

import java.util.ArrayList;
import java.util.List;

public class CoinTypePickerDialog extends Dialog {

    public interface OnConfirmListener {
        void onConfirm(CoinConfig coinType);
    }

    private RecyclerView recyclerView;

    private final List<CoinConfig> coinTypeList;
    private final Adapter coinTypeListAdapter;

    public CoinTypePickerDialog(Context context, List<CoinConfig> coinTypeList1, OnConfirmListener onConfirmListener) {
        super(context, R.style.MyDialog);
        FrameLayout container = new FrameLayout(context);
        container.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        View view = LayoutInflater.from(context).inflate(R.layout.activity_coin_type_picker_dialog, container);
        setContentView(view);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        this.coinTypeList = new ArrayList<>();
        if (null != coinTypeList1) {
            this.coinTypeList.addAll(coinTypeList1);
        }
        coinTypeListAdapter = new Adapter(this.coinTypeList);
        recyclerView.setAdapter(coinTypeListAdapter);

        view.findViewById(R.id.action_add).setOnClickListener(v -> {
            int index = coinTypeListAdapter.getSelectedIndex();
            if (index < 0) {
                Toast.makeText(context, context.getString(R.string.qingxuanze), Toast.LENGTH_SHORT).show();
                return;
            }

            CoinConfig coinType = this.coinTypeList.get(index);
            if (null != onConfirmListener) {
                onConfirmListener.onConfirm(coinType);
            }
            dismiss();
        });
    }

    private static class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private final List<CoinConfig> list;
        private int current = -1;

        private Adapter(List<CoinConfig> list) {
            this.list = list;
        }

        public int getSelectedIndex() {
            return current;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View itemView = inflater.inflate(R.layout.activity_coin_type_picker_dialog_list_item, viewGroup, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
            final CoinConfig item = getItem(position);
            onBindViewHolder(viewHolder, position, item);
        }

        void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position, final CoinConfig item) {
            viewHolder.text1.setText(item.getSymbol());
            final int p = position;
            viewHolder.text1.setSelected(current == position);
            viewHolder.text1.setOnClickListener(v -> {
                current = p;
                notifyDataSetChanged();
            });
        }

        public CoinConfig getItem(int position) {
            return null != list && position >= 0 && position < list.size() ? list.get(position) : null;
        }

        @Override
        public int getItemCount() {
            return null == list ? 0 : list.size();
        }
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
        }

    }

}

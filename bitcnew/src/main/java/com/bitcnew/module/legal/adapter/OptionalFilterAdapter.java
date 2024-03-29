package com.bitcnew.module.legal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.R;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class OptionalFilterAdapter extends RecyclerView.Adapter {

    private Context context;
    private String[] data;

    private String selectedKeyType = "";

    private onItemclickListen onItemclickListen;

    public void setOnItemclickListen(OptionalFilterAdapter.onItemclickListen onItemclickListen) {
        this.onItemclickListen = onItemclickListen;
    }

    public void setData(List<String> data) {
        if (null == data) {
            return;
        }
        this.data = data.toArray(new String[0]);
        notifyDataSetChanged();
    }

    public void setData(String[] data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public void clearAllItem() {
        data = null;
        selectedKeyType = "";
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }


    public OptionalFilterAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.optional_filter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(data[position], position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNum)
        TextView tvNum;
//        @BindView(R.id.viewGap)
//        View viewGap;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final String keyType, int pos) {
            tvNum.setText(keyType);
            if (selectedKeyType.equals(keyType)) {
                tvNum.setSelected(true);
            } else {
                tvNum.setSelected(false);
            }
//            if (pos == getItemCount() - 1) {
//                viewGap.setVisibility(View.GONE);
//            } else {
//                viewGap.setVisibility(View.VISIBLE);
//            }
            tvNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(keyType);
                    if (onItemclickListen != null) {
                        onItemclickListen.onItemclick(keyType);
                    }
                }
            });
        }
    }

    public void setSelectedIndex(int position) {
        if (position < 0 || position >= data.length) {
            return;
        }
        this.selectedKeyType = data[position];
        notifyDataSetChanged();
    }

    public void setSelected(String keyType) {
        if (!this.selectedKeyType.equals(keyType)) {
            this.selectedKeyType = keyType;
            notifyDataSetChanged();
        }
    }

    public String getSelectedKeyType() {
        return selectedKeyType;
    }

    public void clearSelected() {
        this.selectedKeyType = "";
        notifyDataSetChanged();
    }


    public interface onItemclickListen {
        public void onItemclick(String symbol);
    }

}

package com.bitcnew.module.home.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.module.home.entity.AccountBalance;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class AccountBalanceListAdapter extends RecyclerView.Adapter<AccountBalanceListAdapter.ViewHolder> {

    private final List<AccountBalance> list;
    private boolean hideLittle = false;
    private String keyword = null;

    public AccountBalanceListAdapter(List<AccountBalance> list) {
        this.list = list;
    }

    public boolean isHideLittle() {
        return hideLittle;
    }

    public void setHideLittle(boolean hideLittle) {
        if (this.hideLittle == hideLittle) {
            return;
        }

        this.hideLittle = hideLittle;
        notifyDataSetChanged();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        if (Objects.equals(this.keyword, keyword)) {
            return;
        }
        this.keyword = keyword;
        notifyDataSetChanged();
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.fragment_home_balance_account_list_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccountBalance item = getItem(position);
        holder.coinTypeTv.setText(item.getSymbol());
        holder.usdtBalanceTv.setText("≈ " + item.getUsdtAmount() + " USDT");
        holder.ableBalanceTv.setText(item.getHoldAmount());
        holder.freezeBalanceTv.setText(item.getFrozenAmount());
    }

    public AccountBalance getItem(int position) {
        if (null == list || position < 0 || position >= list.size()) {
            return null;
        }
        if (null != getKeyword() && getKeyword().length() > 0 && hideLittle) {
            int index = 0;
            for (AccountBalance item : list) {
                if (null == item.getSymbol() || !item.getSymbol().toLowerCase().contains(getKeyword().toLowerCase())) {
                    continue;
                }
                if (null == item.getHoldAmount() || null == item.getFrozenAmount()) {
                    continue;
                }
                if (new BigDecimal(item.getHoldAmount()).compareTo(BigDecimal.ZERO) <= 0 && new BigDecimal(item.getFrozenAmount()).compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                if (index == position) {
                    return item;
                }
                index++;
            }
        } else if (null != getKeyword() && getKeyword().length() > 0) {
            int index = 0;
            for (AccountBalance item : list) {
                if (null == item.getSymbol() || !item.getSymbol().toLowerCase().contains(getKeyword().toLowerCase())) {
                    continue;
                }
                if (index == position) {
                    return item;
                }
                index ++;
            }
        } else if (isHideLittle()) {
            int index = 0;
            for (AccountBalance item : list) {
                if (null == item.getHoldAmount() || null == item.getFrozenAmount()) {
                    continue;
                }
                if (new BigDecimal(item.getHoldAmount()).compareTo(BigDecimal.ZERO) <= 0 && new BigDecimal(item.getFrozenAmount()).compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                if (index == position) {
                    return item;
                }
                index ++;
            }
        } else {
            return list.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (null == list) {
            return 0;
        }
        if (null != getKeyword() && getKeyword().length() > 0 && hideLittle) {
            int count = 0;
            for (AccountBalance item : list) {
                if (null == item.getSymbol() || !item.getSymbol().toLowerCase().contains(getKeyword().toLowerCase())) {
                    continue;
                }
                if (null == item.getHoldAmount() || null == item.getFrozenAmount()) {
                    continue;
                }
                if (new BigDecimal(item.getHoldAmount()).compareTo(BigDecimal.ZERO) <= 0 && new BigDecimal(item.getFrozenAmount()).compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                count ++;
            }
            return count;
        } else if (null != getKeyword() && getKeyword().length() > 0) {
            int count = 0;
            for (AccountBalance item : list) {
                if (null == item.getSymbol() || !item.getSymbol().toLowerCase().contains(getKeyword().toLowerCase())) {
                    continue;
                }
                count ++;
            }
            return count;
        } else if (isHideLittle()) {
            int count = 0;
            for (AccountBalance item : list) {
                if (null == item.getHoldAmount() || null == item.getFrozenAmount()) {
                    continue;
                }
                if (new BigDecimal(item.getHoldAmount()).compareTo(BigDecimal.ZERO) <= 0 && new BigDecimal(item.getFrozenAmount()).compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }
                count ++;
            }
            return count;
        } else {
            return list.size();
        }
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

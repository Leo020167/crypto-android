package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.ChicangDetailActivity;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ChicangAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<Position> {


    private Context context;
    private Call<ResponseBody> tradeCancelCall;

    private OnItemClick onItemClick;
    private TjrBaseDialog cancelTipsDialog;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public ChicangAdapter(Context context) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chicang, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {

        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvHand)
        TextView tvHand;
        @BindView(R.id.tvHand2)
        TextView tvHand2;
        @BindView(R.id.txt_chengben)
        TextView txt_chengben;
        @BindView(R.id.txt_yingkui)
        TextView txt_yingkui;
        @BindView(R.id.llItem)
        LinearLayout llItem;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Position data, final int pos) {
            tvSymbol.setText(CommonUtil.getOriginSymbol(data.symbol));
            tvHand.setText(data.amount);
            tvHand2.setText(data.availableAmount);
            txt_chengben.setText(data.price);
            txt_yingkui.setText(data.profit+"");
            double p = Double.parseDouble(data.profit);
            if (p<0){
                txt_yingkui.setTextColor(context.getResources().getColor(R.color.ccc1414));
            }else {
                txt_yingkui.setTextColor(context.getResources().getColor(R.color.c14cc4B));
            }

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChicangDetailActivity.pageJump(context,data.symbol);
                }
            });
        }
    }
}
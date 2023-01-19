package com.bitcnew.module.home.trade.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.MaichuDetailActivity;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.wallet.LeverInfoActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.DateUtils;
import com.bitcnew.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TradeLeverHistoryBibiAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<Position> {


    private String type;
    private Context context;
    private Call<ResponseBody> tradeCancelCall;

    private OnItemClick onItemClick;
    private TjrBaseDialog cancelTipsDialog;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public TradeLeverHistoryBibiAdapter(Context context) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_lever_history_bibi_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }
    public void setType(String type){
        this.type=type;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvBuySell)
        TextView tvBuySell;
        @BindView(R.id.ivArrow)
        ImageView ivArrow;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.tvHand)
        TextView tvHand;
        @BindView(R.id.tvOpenPrice)
        TextView tvOpenPrice;
        @BindView(R.id.tvOpenBail)
        TextView tvOpenBail;
        @BindView(R.id.tvOpenPriceText)
        TextView tvOpenPriceText;
        @BindView(R.id.tvOpenBailText)
        TextView tvOpenBailText;
        @BindView(R.id.llItem)
        LinearLayout llItem;


        @BindView(R.id.tvSymbol2)
        TextView tvSymbol2;
        @BindView(R.id.tvBuySell2)
        TextView tvBuySell2;
        @BindView(R.id.txt_shouxufei2)
        TextView txt_shouxufei2;
        @BindView(R.id.ivArrow2)
        ImageView ivArrow2;
        @BindView(R.id.tvTime2)
        TextView tvTime2;
        @BindView(R.id.tvState2)
        TextView tvState2;
        @BindView(R.id.tvHand2)
        TextView tvHand2;
        @BindView(R.id.tvJine2)
        TextView tvJine2;
        @BindView(R.id.tvOpenPrice2)
        TextView tvOpenPrice2;
        @BindView(R.id.llItem2)
        LinearLayout llItem2;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Position data, final int pos) {
            if (!TextUtils.isEmpty(type)&&type.equals("2")){//币币记录专用
                llItem.setVisibility(View.GONE);
                llItem2.setVisibility(View.VISIBLE);
                tvSymbol2.setText(CommonUtil.getOriginSymbol(data.symbol));
                if (!TextUtils.isEmpty(data.buySell)){
                    if (data.buySell.equals("buy")||data.buySell.equals("买入")){
                        tvBuySell2.setText("• 买入");
                        tvBuySell2.setTextColor(context.getResources().getColor(R.color.c14cc4B));
                    }else {
                        tvBuySell2.setText("• 卖出");
                        tvBuySell2.setTextColor(context.getResources().getColor(R.color.ccc1414));
                    }
                }else {
                    tvBuySell2.setText("");
                }
                txt_shouxufei2.setText(context.getResources().getString(R.string.shouxufei)+data.fee);
                tvTime2.setText(DateUtils.getStringDateOfString2(String.valueOf(data.updateTime), DateUtils.TEMPLATE_yyyyMMdd_HHmm));
                tvHand2.setText(data.amount+"");
                double p = Double.parseDouble(data.price);
                tvJine2.setText(data.amount*p+"");
                int sta = data.state;
                if (sta == -1){
                    tvState2.setText(context.getResources().getString(R.string.yichexiao));
                }else if (sta==0){
                    tvState2.setText(context.getResources().getString(R.string.weichengjiao));
                }else {
                    tvState2.setText(context.getResources().getString(R.string.yichengjiao));
                }
                tvOpenPrice2.setText(data.price);
//                llItem2.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!TextUtils.isEmpty(data.buySell)){
//                            if (data.buySell.equals("buy")||data.buySell.equals("买入")){
//
//                            }else {
//                                Intent in = new Intent(context, MaichuDetailActivity.class);
//                                in.putExtra("symbol",data.symbol);//币种
//                                in.putExtra("profit",data.profit);//盈亏
//                                in.putExtra("originPrice",data.originPrice);//成本
//                                in.putExtra("price",data.price);
//                                in.putExtra("amount",data.amount+"");
//                                in.putExtra("fee",data.fee+"");
//                                in.putExtra("updateTime",data.updateTime);
//                                context.startActivity(in);
//                            }
//                        }else {
//                            tvBuySell2.setText("");
//                        }
////                    LeverInfoActivity.pageJump(context, data.orderId);
//                    }
//                });
            }else {
                llItem.setVisibility(View.VISIBLE);
                llItem2.setVisibility(View.GONE);
                tvSymbol.setText(CommonUtil.getOriginSymbol(data.symbol));
                tvBuySell.setText("•" + data.buySellValue);
                tvTime.setText(DateUtils.getStringDateOfString2(String.valueOf(data.openTime), DateUtils.TEMPLATE_yyyyMMdd_HHmm));
                tvHand.setText(data.openHand);
                tvState.setText(data.nowStateDesc);

                if (!TextUtils.isEmpty(data.closeState)){
                    if (data.closeState.equals("canceled")) {
                        ivArrow.setVisibility(View.GONE);
                        tvOpenPriceText.setText(context.getResources().getString(R.string.weituojia));
                        tvOpenBailText.setText(context.getResources().getString(R.string.kaicangbaozhengjin));
                        tvOpenPrice.setText(data.price);
                        tvOpenBail.setText(data.openBail);
                        llItem.setOnClickListener(null);
                    } else {
                        ivArrow.setVisibility(View.VISIBLE);
                        tvOpenPriceText.setText(context.getResources().getString(R.string.kaicangjia));
                        tvOpenBailText.setText(context.getResources().getString(R.string.yingliusdt));
                        tvOpenPrice.setText(data.openPrice);
                        tvOpenBail.setText(StockChartUtil.formatWithSign(data.profit));
                        llItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LeverInfoActivity.pageJump(context, data.orderId,"");
                            }
                        });
                    }
                }

            }
        }
    }
}

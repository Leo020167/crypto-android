package com.bitcnew.module.home.trade.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.home.trade.TradeLeverActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.DateUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 币币交易未完成记录-杠杆
 * Created by zhengmj on 18-10-26.
 */

public class TradeUndoneLeverAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<Position> {


    private Context context;
    private Call<ResponseBody> tradeCancelCall;

    private OnItemClick onItemClick;
    private TjrBaseDialog cancelTipsDialog;
    private String accountType;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public TradeUndoneLeverAdapter(Context context, String accountType) {
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
        this.accountType = accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_undone_lever_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {

        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position, accountType);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvBuySell)
        TextView tvBuySell;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvCancel)
        TextView tvCancel;
        @BindView(R.id.tvHand_label)
        TextView tvHand_label;
        @BindView(R.id.tvHand)
        TextView tvHand;
        @BindView(R.id.tvOpenPrice)
        TextView tvOpenPrice;
        @BindView(R.id.tvOpenBail_label)
        TextView tvOpenBail_label;
        @BindView(R.id.tvOpenBail)
        TextView tvOpenBail;
        @BindView(R.id.llItem)
        LinearLayout llItem;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Position data, final int pos, String accountType) {
            tvSymbol.setText(CommonUtil.getOriginSymbol(data.symbol));

            if (!"spot".equalsIgnoreCase(accountType)) {
                if (data.buySell.equals("buy") || data.buySell.equals("买入")){
                    tvBuySell.setText(" • " + context.getString(R.string.kanzhangzuoduo));
                    tvBuySell.setTextColor(context.getResources().getColor(R.color.c14cc4B));
                } else {
                    tvBuySell.setText(" • " + context.getString(R.string.kandiezuokong));
                    tvBuySell.setTextColor(context.getResources().getColor(R.color.ccc1414));
                }

                tvTime.setText(DateUtils.getStringDateOfString2(String.valueOf(data.openTime), DateUtils.TEMPLATE_yyyyMMdd_HHmm));

                tvHand_label.setText(R.string.shoushu);
                tvHand.setText(data.openHand);
                tvOpenPrice.setText(data.price);
                tvOpenBail_label.setText(R.string.kaicangbaozhengjin);
                tvOpenBail.setText(data.openBail);
            } else {
                if (data.buySell.equals("buy") || data.buySell.equals("买入")){
                    tvBuySell.setText(" • " + context.getString(R.string.mairu));
                    tvBuySell.setTextColor(context.getResources().getColor(R.color.c14cc4B));
                } else {
                    tvBuySell.setText(" • " + context.getString(R.string.maichu));
                    tvBuySell.setTextColor(context.getResources().getColor(R.color.ccc1414));
                }

                tvTime.setText(DateUtils.getStringDateOfString2(String.valueOf(data.openTime), DateUtils.TEMPLATE_yyyyMMdd_HHmm));

                tvHand_label.setText(R.string.shuliang);
                tvHand.setText(data.amount+"");
                tvOpenPrice.setText(data.price);
                tvOpenBail_label.setText(R.string.jine);
                tvOpenBail.setText(data.sum);
            }

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCancelTipsDialog(data, pos);
                }
            });


        }


    }

    private void showCancelTipsDialog(final Position data, final int pos) {
        cancelTipsDialog = new TjrBaseDialog(context) {
            @Override
            public void onclickOk() {
                dismiss();
                startOrderCancel(data, pos, "");


            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        cancelTipsDialog.setMessage(context.getResources().getString(R.string.quedingchexiaodingdan));
        cancelTipsDialog.setBtnOkText(context.getResources().getString(R.string.chexiao));
        cancelTipsDialog.setTitleVisibility(View.GONE);
        cancelTipsDialog.show();
    }


    private void startOrderCancel(final Position data, final int pos, String payPass) {
        CommonUtil.cancelCall(tradeCancelCall);
        tradeCancelCall = VHttpServiceManager.getInstance().getVService().proOrderCancel(data.orderId, payPass);
        tradeCancelCall.enqueue(new MyCallBack(context) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, context);
                    removeItem(pos);
                    if (context instanceof TradeLeverActivity)//交易页面需要刷新checkout
                        ((TradeLeverActivity) context).onCancelOrder();
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startOrderCancel(data, pos, pwString);
            }


        });
    }

}

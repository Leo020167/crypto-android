package com.bitcnew.module.copy.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.entity.Order;
import com.bitcnew.module.home.entity.OrderStateEnum;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.DateUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.R;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.copy.entity.CopyOrder;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 交易明细列表
 * Created by zhengmj on 18-10-26.
 */

public class CopyOrderDetailsListAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<CopyOrder> {



    private Context context;
    private Call<ResponseBody> tradeCancelCall;

    private OnItemClick onItemClick;
    private TjrBaseDialog cancelTipsDialog;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public CopyOrderDetailsListAdapter(Context context) {
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.copy_order_his_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOrientation)
        TextView tvOrientation;
        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvEntrustAmountText)
        TextView tvEntrustAmountText;
        @BindView(R.id.tvEntrustAmount)
        TextView tvEntrustAmount;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvDealAvgPrice)
        TextView tvDealAvgPrice;
        @BindView(R.id.tvDealAmountText)
        TextView tvDealAmountText;
        @BindView(R.id.tvDealAmount)
        TextView tvDealAmount;
        @BindView(R.id.tvDealBalance)
        TextView tvDealBalance;
        @BindView(R.id.tvTolFeeText)
        TextView tvTolFeeText;
        @BindView(R.id.tvTolFee)
        TextView tvTolFee;
        @BindView(R.id.tvCopyFencheng)
        TextView tvCopyFencheng;
        @BindView(R.id.llCopyFencheng)
        LinearLayout llCopyFencheng;
        @BindView(R.id.tvProfit)
        TextView tvProfit;
        @BindView(R.id.llProfit)
        LinearLayout llProfit;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CopyOrder order, final int pos) {
            if (order.buySell == 1) {
                tvOrientation.setText(context.getResources().getString(R.string.mairu));
                tvOrientation.setTextColor(ContextCompat.getColor(context, R.color.quotation_zhang_color));
                llCopyFencheng.setVisibility(View.INVISIBLE);
                llProfit.setVisibility(View.INVISIBLE);

            } else {
                tvOrientation.setText(context.getResources().getString(R.string.maichu));
                tvOrientation.setTextColor(ContextCompat.getColor(context, R.color.quotation_die_color));
                llCopyFencheng.setVisibility(View.VISIBLE);
                llProfit.setVisibility(View.VISIBLE);

                tvCopyFencheng.setText(order.profitShare);
                tvProfit.setText(order.profit);
            }
            tvSymbol.setText(order.symbol);
            tvState.setText(order.stateDesc);

            //全部
            tvPrice.setText(order.price);

            tvEntrustAmountText.setText(context.getResources().getString(R.string.weituoshuliang)+"(" + order.symbol + ")");
            tvEntrustAmount.setText(order.amount);

            tvTime.setText(DateUtils.getStringDateOfString2(order.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmm));

            tvDealAvgPrice.setText(order.dealAvgPrice);

            tvDealAmountText.setText(context.getResources().getString(R.string.chengjiaoshuliang)+"(" + order.symbol + ")");
            tvDealAmount.setText(order.dealAmount);

            tvDealBalance.setText(order.dealBalance);

            if (order.buySell == 1) {//买入手续费是币，卖出手续费是usdt
                tvTolFeeText.setText(context.getResources().getString(R.string.shouxufei)+"(" + order.symbol + ")");
            } else {
                tvTolFeeText.setText(context.getResources().getString(R.string.shouxufeiusdt));
            }
            tvTolFee.setText(String.valueOf(order.dealFee));

        }
    }

    private void showCancelTipsDialog(final Order data, final int pos) {
        cancelTipsDialog = new TjrBaseDialog(context) {
            @Override
            public void onclickOk() {
                dismiss();
                startGetMyUserProjectList(data, pos);
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


    private void startGetMyUserProjectList(final Order data, final int pos) {
        CommonUtil.cancelCall(tradeCancelCall);
        tradeCancelCall = VHttpServiceManager.getInstance().getVService().tradeCancel(data.orderId);
        tradeCancelCall.enqueue(new MyCallBack(context) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, context);
                    data.state = OrderStateEnum.canceled.getState();
                    notifyItemChanged(pos);
//                    removeItem(pos);
                }
            }

        });
    }
}

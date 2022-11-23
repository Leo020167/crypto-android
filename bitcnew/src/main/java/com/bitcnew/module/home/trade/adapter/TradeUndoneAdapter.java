package com.bitcnew.module.home.trade.adapter;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 币币交易未完成记录
 * Created by zhengmj on 18-10-26.
 */

public class TradeUndoneAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<Order> {


    private Context context;
    private Call<ResponseBody> tradeCancelCall;

    private OnItemClick onItemClick;
    private TjrBaseDialog cancelTipsDialog;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public TradeUndoneAdapter(Context context) {
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_undone_item, parent, false));
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
        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.tvPriceText)
        TextView tvPriceText;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvEntrustAmountText)
        TextView tvEntrustAmountText;
        @BindView(R.id.tvEntrustAmount)
        TextView tvEntrustAmount;
        @BindView(R.id.tvDealAmountText)
        TextView tvDealAmountText;
        @BindView(R.id.tvDealAmount)
        TextView tvDealAmount;

        @BindView(R.id.llOpenCopy)
        LinearLayout llOpenCopy;

        @BindView(R.id.tvMyAmountText)
        TextView tvMyAmountText;
        @BindView(R.id.tvMyAmount)
        TextView tvMyAmount;
        @BindView(R.id.tvCopyAmountText)
        TextView tvCopyAmountText;
        @BindView(R.id.tvCopyAmount)
        TextView tvCopyAmount;

        @BindView(R.id.llItem)
        LinearLayout llItem;

        @BindView(R.id.tvCancel)
        TextView tvCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Order data, final int pos) {
            if (data.buySell == 1) {
                tvOrientation.setText(context.getResources().getString(R.string.mairu));
                tvOrientation.setTextColor(ContextCompat.getColor(context, R.color.quotation_zhang_color));
            } else {
                tvOrientation.setText(context.getResources().getString(R.string.maichu));
                tvOrientation.setTextColor(ContextCompat.getColor(context, R.color.quotation_die_color));
            }

            tvSymbol.setText(data.symbol);
            tvState.setText(data.stateDesc);
            tvTime.setText(DateUtils.getStringDateOfString2(data.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmm));

            String originSymbol = CommonUtil.getOriginSymbol(data.symbol);
            String unitSymbol = CommonUtil.getUnitSymbol(data.symbol);

            tvPriceText.setText(context.getResources().getString(R.string.weituojia)+"(" + unitSymbol + ")");
            tvPrice.setText(data.price);

            tvEntrustAmountText.setText(context.getResources().getString(R.string.weituoshuliang)+"(" + originSymbol + ")");
            tvEntrustAmount.setText(data.tolAmount);

            tvDealAmountText.setText(context.getResources().getString(R.string.chengjiaoshuliang)+"(" + originSymbol + ")");
            tvDealAmount.setText(data.dealTolAmount);


            if (data.openCopy == 0) {
                llOpenCopy.setVisibility(View.GONE);
            } else {
                llOpenCopy.setVisibility(View.VISIBLE);

                tvMyAmountText.setText(context.getResources().getString(R.string.wodeweituobtc)+"(" + originSymbol + ")");
                tvMyAmount.setText(data.amount);

                tvCopyAmountText.setText(context.getResources().getString(R.string.gendanweituobtc)+"(" + originSymbol + ")");
                tvCopyAmount.setText(data.copyAmount);
            }

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.onItemClickListen(pos, data);
                    }
                }
            });

            if (OrderStateEnum.isCancelEnable(data.state)) {
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCancelTipsDialog(data, pos);
                    }
                });
            } else {
                tvCancel.setVisibility(View.GONE);
            }


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
//                    data.state = OrderStateEnum.canceled.getState();
//                    notifyItemChanged(pos);
                    removeItem(pos);
                }
            }

        });
    }
}

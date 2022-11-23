package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.entity.Caiwujilu;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.home.trade.adapter.TradeLeverHistoryAdapter;
import com.bitcnew.module.wallet.LeverInfoActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.DateUtils;
import com.bitcnew.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class CaiwujiluAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<Caiwujilu> {


    private Context context;
    private Call<ResponseBody> tradeCancelCall;

    private OnItemClick onItemClick;
    private TjrBaseDialog cancelTipsDialog;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public CaiwujiluAdapter(Context context) {
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_caiwujilu, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llItem)
        LinearLayout llItem;
        @BindView(R.id.tvInOut)
        TextView tvInOut;
        @BindView(R.id.img_youjiantou)
        ImageView img_youjiantou;

        @BindView(R.id.ll_1)
        LinearLayout ll_1;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.ll_2)
        RelativeLayout ll_2;
        @BindView(R.id.tvAmount2)
        TextView tvAmount2;
        @BindView(R.id.tvState2)
        TextView tvState2;
        @BindView(R.id.tvTime2)
        TextView tvTime2;
        @BindView(R.id.tvJiecangTime2)
        TextView tvJiecangTime2;
        @BindView(R.id.ll_time2)
        LinearLayout ll_time2;
        @BindView(R.id.ll_shengoushijian2)
        LinearLayout ll_shengoushijian2;




        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Caiwujilu data, final int pos) {
            if (data.inOut.equals("1")) {//1：充币，-1：提币，2：申购冻结，3:申购成功转换，4：申购失败退回
                tvInOut.setText(context.getResources().getString(R.string.chongbi));
                ll_1.setVisibility(View.VISIBLE);
                ll_2.setVisibility(View.GONE);
                ll_shengoushijian2.setVisibility(View.INVISIBLE);
                img_youjiantou.setVisibility(View.VISIBLE);
            }else if (data.inOut.equals("2")){
                tvInOut.setText(context.getResources().getString(R.string.shengoudongjie)+"("+data.subSymbol+"-"+data.subTitle+")");
                ll_1.setVisibility(View.GONE);
                ll_2.setVisibility(View.VISIBLE);
                ll_shengoushijian2.setVisibility(View.VISIBLE);
                img_youjiantou.setVisibility(View.INVISIBLE);
            }else if (data.inOut .equals("3")){
                tvInOut.setText(context.getResources().getString(R.string.shengouchenggongzhuanhuan)+"("+data.subSymbol+"-"+data.subTitle+")");
                ll_1.setVisibility(View.GONE);
                ll_2.setVisibility(View.VISIBLE);
                ll_shengoushijian2.setVisibility(View.INVISIBLE);
                img_youjiantou.setVisibility(View.VISIBLE);
            }else if (data.inOut .equals("4")){
                tvInOut.setText(context.getResources().getString(R.string.shengoushibaizhuanhuan)+"("+data.subSymbol+"-"+data.subTitle+")");
                ll_1.setVisibility(View.GONE);
                ll_2.setVisibility(View.VISIBLE);
                ll_shengoushijian2.setVisibility(View.INVISIBLE);
                img_youjiantou.setVisibility(View.VISIBLE);
            } else {
                tvInOut.setText(context.getResources().getString(R.string.tibi));
                ll_1.setVisibility(View.VISIBLE);
                ll_2.setVisibility(View.GONE);
                ll_shengoushijian2.setVisibility(View.INVISIBLE);
                img_youjiantou.setVisibility(View.VISIBLE);
            }
            tvState.setText(data.stateDesc);
            tvState2.setText(data.stateDesc);
            tvAmount.setText(data.amount);
            tvAmount2.setText(data.amount);
            tvTime.setText(DateUtils.getStringDateOfString2(data.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmmss));
            tvTime2.setText(DateUtils.getStringDateOfString2(data.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmmss));
            if (!TextUtils.isEmpty(data.transferTime)){
                tvJiecangTime2.setText(DateUtils.getStringDateOfString2(data.transferTime, DateUtils.TEMPLATE_yyyyMMdd_HHmmss));
                ll_time2.setVisibility(View.VISIBLE);
            }else {
                ll_time2.setVisibility(View.INVISIBLE);
            }

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!data.inOut.equals("2")){
                        if (onItemClick != null) onItemClick.onItemClickListen(pos, data);
                    }
                }
            });
        }
    }
}

package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.module.home.entity.VUser;
import com.bitcnew.module.myhome.UserHomeActivity;
import com.bitcnew.widgets.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class VRankAdapter extends BaseImageLoaderRecycleAdapter<VUser> {

    private Context context;
//    private Call<ResponseBody> followAddCall;
//    private Call<ResponseBody> followCancelCall;
    private boolean isRequest;


    public VRankAdapter(Context context) {
//        super(context, R.drawable.ic_common_mic2);
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.v_rank_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivhead)
        CircleImageView ivhead;
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.tvAccuracyRate)
        TextView tvAccuracyRate;
        @BindView(R.id.tvTotalProfit)
        TextView tvTotalProfit;
        @BindView(R.id.tvLastMonthProfit)
        TextView tvLastMonthProfit;

        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final VUser data, final int pos) {
            displayImageForHead(data.headUrl, ivhead);
            tvName.setText(data.userName);
            tvTime.setText(context.getResources().getString(R.string.yiruzhu)+data.days+context.getResources().getString(R.string.tian));
            tvAccuracyRate.setText(data.correctRate+"%");
            tvTotalProfit.setText(data.totalProfit);
            tvLastMonthProfit.setText(data.monthProfit);

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserHomeActivity.pageJump(context, data.userId);
                }
            });

        }
    }


//    private void startFollowAdd(final SubUser data, final int pos) {
//        if (isRequest) return;
//        isRequest = true;
//        CommonUtil.cancelCall(followAddCall);
//        followAddCall = VHttpServiceManager.getInstance().getVService().followAdd(data.userId);
//        followAddCall.enqueue(new MyCallBack(context) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                isRequest = false;
//                if (resultData.isSuccess()) {
//                    data.isFollow = 1;
//                    notifyItemChanged(pos);
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                isRequest = false;
//            }
//        });
//    }
//
//    private void startFollowCancel(final SubUser data, final int pos) {
//        if (isRequest) return;
//        isRequest = true;
//        CommonUtil.cancelCall(followCancelCall);
//        followCancelCall = VHttpServiceManager.getInstance().getVService().followCancel(data.userId);
//        followCancelCall.enqueue(new MyCallBack(context) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                isRequest = false;
//                if (resultData.isSuccess()) {
//                    data.isFollow = 0;
//                    notifyItemChanged(pos);
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                isRequest = false;
//            }
//        });
//    }

}

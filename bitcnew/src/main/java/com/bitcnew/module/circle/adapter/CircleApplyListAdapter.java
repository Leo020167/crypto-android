package com.bitcnew.module.circle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.myhome.PersonalHomepageActivity;
import com.bitcnew.util.DateUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.CircleImageView;
import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.module.circle.entity.CircleApply;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.R;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 18-10-26.
 */

public class CircleApplyListAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<CircleApply> {


    private Context context;
    private String userName;

    public CircleApplyListAdapter(Context context, String userName) {
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
        this.userName = userName;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.circle_apply_record_item, parent, false));
    }

    @Override
    protected void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivHeader)
        CircleImageView ivHeader;
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvReason)
        TextView tvReason;
        @BindView(R.id.tvAllowApply)
        TextView tvAllowApply;
        @BindView(R.id.tvRefuseApply)
        TextView tvRefuseApply;
        @BindView(R.id.llBtn)
        LinearLayout llBtn;
        @BindView(R.id.tvHandler)
        TextView tvHandler;
        @BindView(R.id.llStateAllowApply)
        LinearLayout llStateAllowApply;
        @BindView(R.id.tvStatus)
        TextView tvStatus;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CircleApply data, int pos) {
            displayImage(data.headUrl, ivHeader);

            tvUsername.setText(data.userName);
            tvTime.setText(DateUtils.getChatTimeFormat4(DateUtils.strdate2Date(data.createTime)));
            tvReason.setText(context.getResources().getString(R.string.shenqingliyou)+ (TextUtils.isEmpty(data.reason) ? "" : data.reason));

            if (data.status == 0) {
                llBtn.setVisibility(View.VISIBLE);
                llStateAllowApply.setVisibility(View.GONE);
                tvHandler.setVisibility(View.GONE);
                tvStatus.setVisibility(View.GONE);
            } else {
                llBtn.setVisibility(View.GONE);
                llStateAllowApply.setVisibility(View.VISIBLE);
                tvHandler.setVisibility(View.VISIBLE);
                tvHandler.setText(context.getResources().getString(R.string.chuliren)+ data.handleUserName);

                tvStatus.setVisibility(View.VISIBLE);
                if (data.status == 1) {
                    tvStatus.setText(context.getResources().getString(R.string.yitongguo));
                } else {
                    tvStatus.setText(context.getResources().getString(R.string.yijujue));
                }
            }
            Myonclick onclick = new Myonclick(pos);
            tvAllowApply.setOnClickListener(onclick);
            tvRefuseApply.setOnClickListener(onclick);
            ivHeader.setOnClickListener(onclick);

        }

        Call<ResponseBody> approveApplyCall;

        private void startApproveApply(final CircleApply apply, final int status) {
            CommonUtil.cancelCall(approveApplyCall);
            approveApplyCall = VHttpServiceManager.getInstance().getVService().approveApply(apply.circleId, status, apply.applyId);
            approveApplyCall.enqueue(new MyCallBack(context) {
                @Override
                protected void callBack(ResultData resultData) {
                    if (resultData.isSuccess()) {
                        apply.status = status;
                        apply.handleUserName = userName;
                        notifyDataSetChanged();
                    }
                }
            });
        }

        public class Myonclick implements View.OnClickListener {
            private int pos;

            public Myonclick(int pos) {
                this.pos = pos;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tvAllowApply:
                        startApproveApply(getItem(pos), 1);
                        break;
                    case R.id.tvRefuseApply:
                        startApproveApply(getItem(pos), -1);
                        break;
                    case R.id.ivHeader:
                        PersonalHomepageActivity.pageJumpThis(context,getItem(pos).userId);
                        break;
                }

            }


        }
    }
}

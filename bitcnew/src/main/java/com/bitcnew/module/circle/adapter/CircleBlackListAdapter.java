package com.bitcnew.module.circle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.circle.entity.CircleMemberUser;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.CircleImageView;
import com.bitcnew.R;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 18-10-26.
 */

public class CircleBlackListAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<CircleMemberUser> {


    private Context context;
    private String circleId;
    private Call<ResponseBody> handleBlackCall;

    public CircleBlackListAdapter(Context context, String circleId) {
        super(context, R.drawable.ic_common_mic2);
        this.context = context;
        this.circleId = circleId;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.circle_black_list_item, parent, false));
    }

    @Override
    protected void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivHeader)
        CircleImageView ivHeader;
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.tvToWhite)
        TextView tvToWhite;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CircleMemberUser data) {
            displayImage(data.headUrl, ivHeader);
            tvUsername.setText(data.userName);
            tvToWhite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startHandleBlack(data, data.userId, 1);
                }
            });
        }


        private void startHandleBlack(final CircleMemberUser data, long blackUserId, int status) {
            CommonUtil.cancelCall(handleBlackCall);
            handleBlackCall = VHttpServiceManager.getInstance().getVService().handleBlack(circleId, blackUserId, status);
            handleBlackCall.enqueue(new MyCallBack(context) {
                @Override
                protected void callBack(ResultData resultData) {
                    if (resultData.isSuccess()) {
                        CommonUtil.showmessage(resultData.msg, context);
                        removeItem(data);
                        notifyDataSetChanged();
                    }
                }

            });
        }


    }
}

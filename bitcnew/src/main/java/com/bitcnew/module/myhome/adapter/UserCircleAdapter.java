package com.bitcnew.module.myhome.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.module.circle.CircleInfoActivity;
import com.bitcnew.module.circle.entity.CircleRoleEnum;
import com.bitcnew.R;
import com.bitcnew.http.widget.view.RoundAngleImageView;
import com.bitcnew.module.circle.entity.CircleInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class UserCircleAdapter extends BaseImageLoaderRecycleAdapter<CircleInfo> {


    private Context context;

    public UserCircleAdapter(Context context) {
        super(R.drawable.ic_common_mic2);
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_circle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_head)
        RoundAngleImageView ivHead;
        @BindView(R.id.tvCircleName)
        TextView tvCircleName;
//        @BindView(R.id.tvCircleId)
//        TextView tvCircleId;
        @BindView(R.id.tvMemberAmount)
        TextView tvMemberAmount;
        @BindView(R.id.tvCircleRoleName)
        TextView tvCircleRoleName;
        @BindView(R.id.home)
        LinearLayout home;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CircleInfo data) {
            displayImage(data.circleLogo, ivHead);
            tvCircleName.setText(data.circleName);
//            tvCircleId.setText("(ID: "+data.circleId+")");
            tvMemberAmount.setText(context.getResources().getString(R.string.huiyuan)+": "+data.memberAmount+context.getResources().getString(R.string.wei));
            tvCircleRoleName.setText(CircleRoleEnum.getRoleName(data.role));
            if (CircleRoleEnum.isRoot(data.role)) {
                tvCircleRoleName.setBackgroundResource(R.drawable.shape_circle_role_root);
                tvCircleRoleName.setTextColor(ContextCompat.getColor(context,R.color.black));
            } else {
                tvCircleRoleName.setBackgroundResource(R.drawable.shape_circle_role_admin);
                tvCircleRoleName.setTextColor(ContextCompat.getColor(context,R.color.white));
            }
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CircleInfoActivity.pageJumpThis(context,data.circleId);
                }
            });
        }

    }
}

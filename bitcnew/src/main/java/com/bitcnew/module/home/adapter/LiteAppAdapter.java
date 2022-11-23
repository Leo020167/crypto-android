package com.bitcnew.module.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.bitcnew.module.home.entity.LiteAppEntity;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.R;
import com.bitcnew.module.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class LiteAppAdapter extends BaseImageLoaderRecycleAdapter<LiteAppEntity> {


    private Context context;

    public LiteAppAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lite_app_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivLiteAppIcon)
        ImageView ivLiteAppIcon;
        @BindView(R.id.tvLiteAppName)
        TextView tvLiteAppName;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final LiteAppEntity liteAppEntity) {
            displayImage(liteAppEntity.logo, ivLiteAppIcon);
            tvLiteAppName.setText(liteAppEntity.name);
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((TJRBaseToolBarActivity) context).isLogin()) {
                        PageJumpUtil.jumpByPkg(context, liteAppEntity.pkg, liteAppEntity.cls, liteAppEntity.params);
                    } else {
                        LoginActivity.login((TJRBaseToolBarActivity) context);
                    }
                }
            });
        }
    }


}

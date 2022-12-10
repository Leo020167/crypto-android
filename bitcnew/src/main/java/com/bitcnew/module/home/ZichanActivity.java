package com.bitcnew.module.home;

import android.os.Bundle;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 资产界面
 */
public class ZichanActivity extends TJRBaseToolBarSwipeBackActivity {

    @OnClick(R.id.action_chongbi)
    void onChongbiClick() {

    }

    @OnClick(R.id.action_tibi)
    void onTibiClick() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_home_zichan;
    }

    @Override
    protected String getActivityTitle() {
        return getContext().getString(R.string.zichan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

}

package com.bitcnew.module.pledge;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.module.pledge.adapter.PledgeRecordListPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 质押记录
 */
public class PledgeRecordListActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private PledgeRecordListPagerAdapter pagerAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_pledge_record_list;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.zhiyajilu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        pagerAdapter = new PledgeRecordListPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}

package com.bitcnew.module.pledge.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bitcnew.R;
import com.bitcnew.module.pledge.PledgeRecordListFragment;

public class PledgeRecordListPagerAdapter extends FragmentPagerAdapter {

    private final Context context;

    public PledgeRecordListPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (0 == position) {
            return PledgeRecordListFragment.newInstance("0");
        } else if (1 == position) {
            return PledgeRecordListFragment.newInstance("1");
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (0 == position) {
            return context.getString(R.string.zhiyajilu_jinxingzhong);
        } else if (1 == position) {
            return context.getString(R.string.zhiyajilu_yijieshu);
        }
        return null;
    }
}

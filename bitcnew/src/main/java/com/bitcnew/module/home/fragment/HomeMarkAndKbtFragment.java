package com.bitcnew.module.home.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.R;
import com.bitcnew.module.home.HomeActivity;
import com.bitcnew.module.home.HomeMarkAndKbtFragmentTabConfig;
import com.bitcnew.module.home.OptionalDragSortActivity;
import com.bitcnew.module.home.SearchCoinActivity;
import com.bitcnew.module.login.LoginActivity;
import com.bitcnew.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 19-3-8.
 */
//tab行情页
public class HomeMarkAndKbtFragment extends UserBaseImmersionBarFragment implements View.OnClickListener {
    @BindView(R.id.view_top)
    View view_top;
    @BindView(R.id.ll_bar)
    LinearLayout ll_bar;


    @BindView(R.id.tvQQZS)
    TextView tvQQZS;
    @BindView(R.id.tvHY)
    TextView tvHY;
    @BindView(R.id.tvBB)
    TextView tvBB;


//    @BindView(R.id.tvMarket)
//    TextView tvMarket;
//    @BindView(R.id.tvBuyBack)
//    TextView tvBuyBack;

    @BindView(R.id.ivSearch)
    LinearLayout ivSearch;
    @BindView(R.id.ivOptionalSort)
    LinearLayout ivOptionalSort;


    @BindView(R.id.viewPager)
    ViewPager viewPager;

    HomeMarkPageAdapter homeMarkPageAdapter;

    public static HomeMarkAndKbtFragment newInstance() {
        HomeMarkAndKbtFragment fragment = new HomeMarkAndKbtFragment();
        return fragment;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (homeMarkPageAdapter != null && viewPager != null) {
            for (int i = 0; i < homeMarkPageAdapter.getCount(); i++) {
                Fragment fragment = (Fragment) homeMarkPageAdapter.instantiateItem(viewPager, i);
                fragment.setUserVisibleHint(isVisibleToUser && viewPager.getCurrentItem() == i);
            }
        }

        Log.d("HomeMarkAndKbtFragment", "setUserVisibleHint   isVisibleToUser==" + isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeMarkAndKbtFragment", "onResume   getUserVisibleHint()==" + getUserVisibleHint());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("setUserVisibleHint", "onPause=======");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void immersionbar() {
        Log.d("HomeMarket", "mImmersionBar==" + mImmersionBar + " ll_bar==" + ll_bar);
        if (mImmersionBar != null && ll_bar != null) {
            mImmersionBar
                    .titleBar(ll_bar)
                    .statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA)
                    .navigationBarColor(R.color.white)
                    .init();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_mark_and_kbt, container, false);
        ButterKnife.bind(this, view);

        homeMarkPageAdapter = new HomeMarkPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(homeMarkPageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                Log.d("HomeMarkAndKbtFragment", "onPageSelected  i==" + i);
                slideTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setOffscreenPageLimit(homeMarkPageAdapter.getCount() - 1);
        ivSearch.setOnClickListener(this);
        ivOptionalSort.setOnClickListener(this);
        tvQQZS.setOnClickListener(this);
        tvHY.setOnClickListener(this);
        tvBB.setOnClickListener(this);

        if (Build.VERSION.SDK_INT>=30){
            view_top.setVisibility(View.VISIBLE);
        }else {
            view_top.setVisibility(View.GONE);
        }

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(0, false);
                slideTab(0);
            }
        }, 300);

        return view;
    }

    public void setKbtSelected() {
        if (viewPager != null) {
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(HomeMarkAndKbtFragmentTabConfig.TAB_HE_YUE, false);
                    slideTab(HomeMarkAndKbtFragmentTabConfig.TAB_HE_YUE);
                }
            }, 300);
        }

    }

    public void slideTab(int pos) {
        switch (pos) {
            case HomeMarkAndKbtFragmentTabConfig.TAB_QAUAN_QIU:
                tvQQZS.setSelected(true);
                tvHY.setSelected(false);
                tvBB.setSelected(false);
                ivOptionalSort.setVisibility(View.GONE);
                break;
            case HomeMarkAndKbtFragmentTabConfig.TAB_HE_YUE:
                tvQQZS.setSelected(false);
                tvHY.setSelected(true);
                tvBB.setSelected(false);
                ivOptionalSort.setVisibility(View.GONE);
                break;
            case HomeMarkAndKbtFragmentTabConfig.TAB_BI_BI:
                tvQQZS.setSelected(false);
                tvHY.setSelected(false);
                tvBB.setSelected(true);
                ivOptionalSort.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
        immersionbar();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvQQZS:
                if (viewPager.getCurrentItem() != HomeMarkAndKbtFragmentTabConfig.TAB_QAUAN_QIU) {
                    viewPager.setCurrentItem(HomeMarkAndKbtFragmentTabConfig.TAB_QAUAN_QIU);
                }
                break;
            case R.id.tvHY:
                if (viewPager.getCurrentItem() != HomeMarkAndKbtFragmentTabConfig.TAB_HE_YUE) {
                    viewPager.setCurrentItem(HomeMarkAndKbtFragmentTabConfig.TAB_HE_YUE);
                }
                break;
            case R.id.tvBB:
                if (viewPager.getCurrentItem() != HomeMarkAndKbtFragmentTabConfig.TAB_BI_BI) {
                    viewPager.setCurrentItem(HomeMarkAndKbtFragmentTabConfig.TAB_BI_BI);
                }
                break;
            case R.id.ivSearch:
                int pos = viewPager.getCurrentItem();
                Bundle args = new Bundle();
                String accountType = null;
                if (pos == HomeMarkAndKbtFragmentTabConfig.TAB_BI_BI) {
                    accountType = "spot";
                } else if (pos == HomeMarkAndKbtFragmentTabConfig.TAB_HE_YUE) {
                    accountType = "digital";
                } else if (pos == HomeMarkAndKbtFragmentTabConfig.TAB_QAUAN_QIU) {
                    accountType = "stock";
                }
                args.putString("accountType", accountType);
                PageJumpUtil.pageJump(getActivity(), SearchCoinActivity.class, args);
                break;
            case R.id.ivOptionalSort:
                if (((HomeActivity) getActivity()).isLogin()) {
                    PageJumpUtil.pageJump(getActivity(), OptionalDragSortActivity.class);
                } else {
                    LoginActivity.login((HomeActivity) getActivity());
                }
                break;
        }

    }


    private class HomeMarkPageAdapter extends FragmentPagerAdapter {


        public HomeMarkPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public void notifyDataSetChanged() {
//            slideTab(0);
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

        //optional自选、digital数字货币、stock股指期货
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case HomeMarkAndKbtFragmentTabConfig.TAB_QAUAN_QIU:
                    return HomeStockDigitalMarkFragment.newInstance("stock", 1);
                case HomeMarkAndKbtFragmentTabConfig.TAB_HE_YUE:
                    return HomeStockDigitalMarkFragment.newInstance("digital", 1);
                case HomeMarkAndKbtFragmentTabConfig.TAB_BI_BI:
                    return HomeStockDigitalMarkFragment.newInstance("spot", 1);
                default:
                    return HomeStockDigitalMarkFragment.newInstance("stock", 1);
            }


        }
    }
}

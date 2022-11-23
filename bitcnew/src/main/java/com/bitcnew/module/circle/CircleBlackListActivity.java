package com.bitcnew.module.circle;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.circle.adapter.CircleBlackListAdapter;
import com.bitcnew.module.circle.entity.CircleMemberUser;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.LoadMoreRecycleView;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;
import com.bitcnew.R;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 *
 * 黑名单
 *
 * @author zhengmj
 */
public class CircleBlackListActivity extends TJRBaseToolBarSwipeBackActivity {
    @BindView(R.id.fl_no_content)
    FrameLayout fl_no_content;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    private CircleBlackListAdapter circleBlackListAdapter;

    private String circleId;
    private int pageSize = 15;

    private int pageNo = 1;

    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.guanliheimingdan);
    }

    public static void pageJumpThis(Context context, String circleId) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.CIRCLEID, circleId);
        PageJumpUtil.pageJump(context, CircleBlackListActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            circleId = getIntent().getExtras().getString(CommonConst.CIRCLEID);
            if (TextUtils.isEmpty(circleId)) {
                CommonUtil.showmessage(getResources().getString(R.string.canshucuowu), this);
                finish();
                return;
            }
        }
        ButterKnife.bind(this);
        circleBlackListAdapter = new CircleBlackListAdapter(this,circleId);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor)));
//        rvList.addItemDecoration(new SimpleRecycleDivider(this));
        rvList.setAdapter(circleBlackListAdapter);
        rvList.setRecycleViewLoadMoreCallBack(callBack);
        circleBlackListAdapter.setRecycleViewLoadMoreCallBack(callBack);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                startGetBlackList(circleId);
            }
        });
        startGetBlackList(circleId);
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (circleBlackListAdapter != null && circleBlackListAdapter.getRealItemCount() > 0) {
                CircleMemberUser circleMemberUser = circleBlackListAdapter.getItem(circleBlackListAdapter.getRealItemCount() - 1);
                if (circleMemberUser == null) {
                    pageNo = 1;
                }
                startGetBlackList(circleId);
            }
        }
    };


    Call<ResponseBody> getMemberListCall;

    private void startGetBlackList(String circleId) {
        com.bitcnew.http.util.CommonUtil.cancelCall(getMemberListCall);
        getMemberListCall = VHttpServiceManager.getInstance().getVService().getBlackList(circleId, pageNo);
        getMemberListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    Group<CircleMemberUser> group = resultData.getGroup("data", new TypeToken<Group<CircleMemberUser>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            circleBlackListAdapter.setGroup(group);
                        } else {
                            circleBlackListAdapter.addItem(group);
                            circleBlackListAdapter.notifyDataSetChanged();
                        }
                    }
                    pageNo++;
                    swiperefreshlayout.setRefreshing(false);
                    if (circleBlackListAdapter.getRealItemCount() > 0) {
                        circleBlackListAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
                    }
                }
            }
            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swiperefreshlayout.setRefreshing(false);
                circleBlackListAdapter.onLoadComplete(false,false);
            }
        });
    }


}

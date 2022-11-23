package com.bitcnew.module.kbt.app.lightningprediction;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.kbt.app.lightningprediction.adapter.LpHistoryAdapter;
import com.bitcnew.module.kbt.app.lightningprediction.entity.LpHistory;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.LoadMoreRecycleView;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 预测记录
 */
public class LpHistoryActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.fl_no_content)
    FrameLayout fl_no_content;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    private LpHistoryAdapter lpHistoryAdapter;

    private int pageSize = 15;
    private int pageNo = 1;

    private Call<ResponseBody> withdrawCoinListCall;
    private Group<LpHistory> group;



    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.yucejilu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        lpHistoryAdapter = new LpHistoryAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor)));
        rvList.setAdapter(lpHistoryAdapter);
        rvList.setRecycleViewLoadMoreCallBack(callBack);
        lpHistoryAdapter.setRecycleViewLoadMoreCallBack(callBack);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                startGetPredictRecordList();
            }
        });

        startGetPredictRecordList();
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (lpHistoryAdapter != null && lpHistoryAdapter.getRealItemCount() > 0) {
                LpHistory lpHistory = lpHistoryAdapter.getItem(lpHistoryAdapter.getRealItemCount() - 1);
                if (lpHistory == null) {
                    pageNo = 1;
                }
                startGetPredictRecordList();
            }
        }
    };

    private void startGetPredictRecordList() {
        CommonUtil.cancelCall(withdrawCoinListCall);
        withdrawCoinListCall = VHttpServiceManager.getInstance().getPredictGameService().getPredictRecord(pageNo);
        withdrawCoinListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<LpHistory>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            lpHistoryAdapter.setGroup(group);
                        } else {
                            lpHistoryAdapter.addItem(group);
                            lpHistoryAdapter.notifyDataSetChanged();
                        }
                    }
                    fl_no_content.setVisibility(lpHistoryAdapter.getRealItemCount() > 0 ? View.GONE : View.VISIBLE);
                    pageNo++;
                    swiperefreshlayout.setRefreshing(false);
                    if (lpHistoryAdapter.getRealItemCount() > 0) {
                        lpHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swiperefreshlayout.setRefreshing(false);
                lpHistoryAdapter.onLoadComplete(false, false);
            }
        });
    }


}

package com.bitcnew.module.home.trade.history;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.trade.adapter.TakeCoinHistoryAdapter;
import com.bitcnew.module.home.trade.entity.TakeCoinHistory;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.LoadMoreRecycleView;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.bitcnew.R;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提币记录
 */
public class TakeCoinHistoryActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.fl_no_content)
    FrameLayout fl_no_content;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    private TakeCoinHistoryAdapter takeCoinHistoryAdapter;

    private int pageSize = 15;
    private int pageNo = 1;

    private Call<ResponseBody> withdrawCoinListCall;
    private Group<TakeCoinHistory> group;


    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.caiwujilu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        takeCoinHistoryAdapter = new TakeCoinHistoryAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor)));
        rvList.setAdapter(takeCoinHistoryAdapter);
        rvList.setRecycleViewLoadMoreCallBack(callBack);
        takeCoinHistoryAdapter.setRecycleViewLoadMoreCallBack(callBack);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                startGetwithdrawCoinList();
            }
        });
        takeCoinHistoryAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                TakeCoinHistory takeCoinHistory = (TakeCoinHistory) t;
                TakeCoinHistoryDetailsActivity.pageJump(TakeCoinHistoryActivity.this, takeCoinHistory);
            }
        });

        startGetwithdrawCoinList();
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (takeCoinHistoryAdapter != null && takeCoinHistoryAdapter.getRealItemCount() > 0) {
                TakeCoinHistory takeCoinHistory = takeCoinHistoryAdapter.getItem(takeCoinHistoryAdapter.getRealItemCount() - 1);
                if (takeCoinHistory == null) {
                    pageNo = 1;
                }
                startGetwithdrawCoinList();
            }
        }
    };

    private void startGetwithdrawCoinList() {
        CommonUtil.cancelCall(withdrawCoinListCall);
        withdrawCoinListCall = VHttpServiceManager.getInstance().getVService().withdrawCoinList(pageNo);
        withdrawCoinListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<TakeCoinHistory>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            takeCoinHistoryAdapter.setGroup(group);
                        } else {
                            takeCoinHistoryAdapter.addItem(group);
                            takeCoinHistoryAdapter.notifyDataSetChanged();
                        }
                    }
                    fl_no_content.setVisibility(takeCoinHistoryAdapter.getRealItemCount() > 0 ? View.GONE : View.VISIBLE);
                    pageNo++;
                    swiperefreshlayout.setRefreshing(false);
                    if (takeCoinHistoryAdapter.getRealItemCount() > 0) {
                        takeCoinHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swiperefreshlayout.setRefreshing(false);
                takeCoinHistoryAdapter.onLoadComplete(false, false);
            }
        });
    }


}

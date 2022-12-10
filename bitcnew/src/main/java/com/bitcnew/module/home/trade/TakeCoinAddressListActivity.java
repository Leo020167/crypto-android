package com.bitcnew.module.home.trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.bitcnew.R;
import com.bitcnew.common.base.CommonToolbar;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.trade.adapter.TakeCoinAddressListAdapter;
import com.bitcnew.module.home.trade.entity.TakeCoinAddress;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.LoadMoreRecycleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提币地址管理
 */
public class TakeCoinAddressListActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.commonToolbar)
    CommonToolbar commonToolbar;
    @BindView(R.id.fl_no_content)
    FrameLayout fl_no_content;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    private List<TakeCoinAddress> listData;
    private TakeCoinAddressListAdapter listAdapter;
    private Call<ResponseBody> listCall;
    private Call<ResponseBody> delCall;

    @Override
    protected int setLayoutId() {
        return R.layout.take_coin_address_list_activity;
    }

    @Override
    protected String getActivityTitle() {
        return getContext().getString(R.string.tibidizhiguanli);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        commonToolbar.setOnMoreListener(v -> {
            PageJumpUtil.pageJump(getContext(), TakeCoinAddressAddActivity.class);
        });
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 加载列表
        loadListView();
    }

    private void initListView() {
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));

        listData = new ArrayList<>();
        listAdapter = new TakeCoinAddressListAdapter(listData);
        listAdapter.setOnItemDelListener((position, item) -> {
            delAddress(item, position);
        });
        listAdapter.setOnItemClickListener((position, item) -> {
            Intent data = new Intent();
            data.putExtra("data", item);
            setResult(RESULT_OK, data);
            finish();
        });
        rvList.setAdapter(listAdapter);

        swiperefreshlayout.setOnRefreshListener(() -> {
            // 加载列表
            loadListView();
        });
    }

    private void delAddress(TakeCoinAddress item, int position) {
        if (null == item) {
            return;
        }

        CommonUtil.cancelCall(delCall);
        delCall = VHttpServiceManager.getInstance().getVService().delTakeCoinAddress(item.getId());
        delCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (position >= 0 && position < listData.size()) {
                        listData.remove(position);
                        listAdapter.notifyDataSetChanged();
                    }
                } else {
                    showToast(resultData.msg);
                }
            }
        });
    }

    private void loadListView() {
        CommonUtil.cancelCall(listCall);
        listCall = VHttpServiceManager.getInstance().getVService().takeCoinAddressList(getUserIdLong());
        listCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    List<TakeCoinAddress> list = gson.fromJson(resultData.data, new TypeToken<List<TakeCoinAddress>>() {
                    }.getType());
                    listData.clear();
                    if (list != null && list.size() > 0) {
                        listData.addAll(list);
                    }
                    listAdapter.notifyDataSetChanged();
                    fl_no_content.setVisibility(listData.size() > 0 ? View.GONE : View.VISIBLE);
                    swiperefreshlayout.setRefreshing(false);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swiperefreshlayout.setRefreshing(false);
            }
        });
    }

}

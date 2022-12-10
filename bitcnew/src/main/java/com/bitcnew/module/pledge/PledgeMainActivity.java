package com.bitcnew.module.pledge;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bitcnew.R;
import com.bitcnew.common.base.CommonToolbar;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.util.GsonUtils;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.pledge.adapter.PledgeCoinListAdapter;
import com.bitcnew.module.pledge.dialog.PledgeApplyDialog;
import com.bitcnew.module.pledge.entity.PledgeCoin;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 质押生意
 */
public class PledgeMainActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.commonToolbar)
    CommonToolbar commonToolbar;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.flEmpty)
    View flEmpty;

    private List<PledgeCoin> coinList;
    private PledgeCoinListAdapter coinListAdapter;

    private Call<ResponseBody> pledgeListCall;
    private Call<ResponseBody> pledgeCommitCall;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_pledge_main;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.zhiyashengyi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        commonToolbar.setOnMoreListener(v -> {
            PageJumpUtil.pageJump(this, PledgeRecordListActivity.class);
        });
        swipeRefreshLayout.setOnRefreshListener(()->{
            loadPledgeList();
        });
        initRecyclerView();
        loadPledgeList();
    }

    // 质押
    private void zhiya(PledgeCoin pledgeCoin, String num) {
        CommonUtil.cancelCall(pledgeCommitCall);
        pledgeCommitCall = VHttpServiceManager.getInstance().getVService().pledgeCommit(num, Long.parseLong(pledgeCoin.id), getUserIdLong());
        pledgeCommitCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    showToast(R.string.canyuzhiyachenggong);
                } else {
                    showToast(resultData.msg);
                }
            }
        });
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        coinList = new ArrayList<>();
        coinListAdapter = new PledgeCoinListAdapter(this, coinList);
        coinListAdapter.setOnZhiyaClickListener(item -> {
            new PledgeApplyDialog(getContext(), item, this::zhiya).show();
        });
        recyclerView.setAdapter(coinListAdapter);
    }

    private void updateRecycleView(List<PledgeCoin> list) {
        if (null != coinList) {
            coinList.clear();
        }
        if (null != coinList && null != list && !list.isEmpty()) {
            coinList.addAll(list);
        }
        if (null != coinListAdapter) {
            coinListAdapter.notifyDataSetChanged();
        }
        if (null == coinList || coinList.isEmpty()) {
            flEmpty.setVisibility(View.VISIBLE);
        } else {
            flEmpty.setVisibility(View.GONE);
        }
    }

    private void loadPledgeList() {
        CommonUtil.cancelCall(pledgeListCall);
        pledgeListCall = VHttpServiceManager.getInstance().getVService().pledgeList();
        pledgeListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                swipeRefreshLayout.setRefreshing(false);
                if (resultData.isSuccess()) {
                    Gson gson = GsonUtils.createGson();
                    List<PledgeCoin> list = gson.fromJson(resultData.data, new TypeToken<List<PledgeCoin>>(){}.getType());
                    updateRecycleView(list);
                } else {
                    showToast(resultData.msg);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}

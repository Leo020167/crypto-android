package com.bitcnew.module.pledge;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitcnew.MainApplication;
import com.bitcnew.R;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.util.GsonUtils;
import com.bitcnew.common.util.ToastUtils;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.pledge.adapter.PledgeRecordListAdapter;
import com.bitcnew.module.pledge.entity.PledgeCoin;
import com.bitcnew.module.pledge.entity.PledgeRecord;
import com.bitcnew.util.MyCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class PledgeRecordListFragment extends Fragment {

    public static PledgeRecordListFragment newInstance(String status) {
        Bundle args = new Bundle();
        args.putString("status", status);
        PledgeRecordListFragment fragment = new PledgeRecordListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private View flEmpty;

    private String status;
    private List<PledgeRecord> listData;
    private PledgeRecordListAdapter listAdapter;
    private Call<ResponseBody> pledgeRecordListCall;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            status = getArguments().getString("status");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_pledge_record_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        flEmpty = view.findViewById(R.id.flEmpty);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        initRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadList();
        });
        loadList();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        listData = new ArrayList<>();
        listAdapter = new PledgeRecordListAdapter(getActivity(), listData);
        recyclerView.setAdapter(listAdapter);
    }

    private void updateRecycleView(List<PledgeRecord> list) {
        if (null != listData) {
            listData.clear();
        }
        if (null != listData && null != list && !list.isEmpty()) {
            listData.addAll(list);
        }
        if (null != listAdapter) {
            listAdapter.notifyDataSetChanged();
        }
        if (null == listData || listData.isEmpty()) {
            flEmpty.setVisibility(View.VISIBLE);
        } else {
            flEmpty.setVisibility(View.GONE);
        }
    }

    private void loadList() {
        CommonUtil.cancelCall(pledgeRecordListCall);
        Long userId = MainApplication.getInstance().getUser().getUserId();
        pledgeRecordListCall = VHttpServiceManager.getInstance().getVService().pledgeRecordList(status, userId);
        pledgeRecordListCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                swipeRefreshLayout.setRefreshing(false);

                if (resultData.isSuccess()) {
                    Gson gson = GsonUtils.createGson();
                    List<PledgeRecord> list = gson.fromJson(resultData.data, new TypeToken<List<PledgeRecord>>(){}.getType());
                    updateRecycleView(list);
                } else {
                    ToastUtils.showToast(getActivity(), resultData.msg);
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

package com.bitcnew.module.copy.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.copy.adapter.CopyHoldMarketValueAdapter;
import com.bitcnew.module.copy.adapter.CopyPieChartAdapter;
import com.bitcnew.module.copy.entity.CopyOrderPieChart;
import com.bitcnew.module.home.fragment.UserBaseFragment;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.PieChartView;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 持币成本
 * <p>
 * Created by zhengmj on 17-12-7.
 */

public class HoldMarketValueFragment extends UserBaseFragment {

    @BindView(R.id.tvTolMarket)
    TextView tvTolMarket;
    @BindView(R.id.tvTolMarketCny)
    TextView tvTolMarketCny;
    @BindView(R.id.pieChartView)
    PieChartView pieChartView;
    @BindView(R.id.rvPieChartList)
    RecyclerView rvPieChartList;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private boolean flag;

    private Call<ResponseBody> holdMarketValueCall;

    private CopyPieChartAdapter copyPieChartAdapter;

    private CopyHoldMarketValueAdapter copyHoldMarketValueAdapter;

    public static HoldMarketValueFragment newInstance() {
        HoldMarketValueFragment f = new HoldMarketValueFragment();
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !flag) {
            startHoldMarketValueCall();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hold_market_value, container, false);
        Log.d("HoldMarketValueFragment", "getUserVisibleHint==" + getUserVisibleHint());
        ButterKnife.bind(this, v);
        copyPieChartAdapter = new CopyPieChartAdapter(getActivity());
        rvPieChartList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPieChartList.setAdapter(copyPieChartAdapter);


        copyHoldMarketValueAdapter = new CopyHoldMarketValueAdapter(getActivity());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.addItemDecoration(new SimpleRecycleDivider(getActivity()));
        rvList.setAdapter(copyHoldMarketValueAdapter);


        return v;
    }


    private void startHoldMarketValueCall() {
        CommonUtil.cancelCall(holdMarketValueCall);
        holdMarketValueCall = VHttpServiceManager.getInstance().getVService().holdMarketValue();
        holdMarketValueCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    flag=true;
                    Group<CopyOrderPieChart> group = resultData.getGroup("holdList", new TypeToken<Group<CopyOrderPieChart>>() {
                    }.getType());
                    copyHoldMarketValueAdapter.setGroup(group);

                    Group<CopyOrderPieChart> group2 = resultData.getGroup("chartList", new TypeToken<Group<CopyOrderPieChart>>() {
                    }.getType());

                    if (group2 != null && group2.size() > 0) {
                        float[] data = new float[group2.size()];
                        for (int i = 0, m = group2.size(); i < m; i++) {
                            data[i] = group2.get(i).rate;
                        }
                        Log.d("CropyOrderInfo", "data==" + data.toString());
                        pieChartView.setData(data);
                        pieChartView.startAnimation(1500);

                        copyPieChartAdapter.setGroup(group2);

                    }

                    String totalMarketValue = resultData.getItem("totalMarketValue", String.class);
                    String totalMarketValueCny = resultData.getItem("totalMarketValueCny", String.class);

                    tvTolMarket.setText(getResources().getString(R.string.zongshizhi) + totalMarketValue + " USDT");
                    tvTolMarketCny.setText(totalMarketValueCny);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

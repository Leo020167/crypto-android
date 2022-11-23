package com.bitcnew.module.home.trade;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.trade.adapter.LegalTenderBuyCoinAdapter;
import com.bitcnew.module.home.trade.entity.OtcEntity;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 法币购买
 */
public class LegalTenderBuyCoinActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;


    private LegalTenderBuyCoinAdapter legalTenderBuyCoinAdapter;
    private Call<ResponseBody> getReceiptsCall;


    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview_2;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.fabigoumaiusdt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        legalTenderBuyCoinAdapter = new LegalTenderBuyCoinAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 20, 20, true));
        rvList.setAdapter(legalTenderBuyCoinAdapter);

        legalTenderBuyCoinAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                OtcEntity otcEntity=(OtcEntity)t;
                showRechargeUsdtDialogFragment(0,otcEntity);
            }
        });


        startGetotcList();
    }


    private void startGetotcList() {
        CommonUtil.cancelCall(getReceiptsCall);
        getReceiptsCall = VHttpServiceManager.getInstance().getVService().otcList();
        getReceiptsCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<OtcEntity> group = resultData.getGroup("otcList", new TypeToken<Group<OtcEntity>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        legalTenderBuyCoinAdapter.setGroup(group);
                    }
                }
            }
        });
    }


}

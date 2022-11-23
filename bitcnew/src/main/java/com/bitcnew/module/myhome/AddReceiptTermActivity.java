package com.bitcnew.module.myhome;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.myhome.adapter.AddReceiptAdapter;
import com.bitcnew.module.myhome.entity.AddPaymentTern;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 增加账户类型
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class AddReceiptTermActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private Call<ResponseBody> getPaymentOptionListCall;
    private AddReceiptAdapter addReceiptAdapter;
    private int from;//0 收款管理过来 1 添加广告过来 2快捷购买那里

    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview_2;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.tianjiazhanghuleixing);
    }


    public static void pageJump(Context context, int from) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.JUMPTYPE, from);
        PageJumpUtil.pageJump(context, AddReceiptTermActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.JUMPTYPE)) {
                from = bundle.getInt(CommonConst.JUMPTYPE, 0);
//                entrustAmount = bundle.getString(CommonConst.ENTRUSTAMOUNT, "0.00");
            }
        }

//        setContentView(R.layout.payment_trem);
        ButterKnife.bind(this);
        addReceiptAdapter = new AddReceiptAdapter(this,from);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
//        rv_list.addItemDecoration(new SimpleRecycleDivider(this, true));
        rv_list.setAdapter(addReceiptAdapter);

//        Group<AddPaymentTern> selectPayWayGroup = new Group<>();
//        AddPaymentTern selectPayWay = null;
//        for (int i = 0; i < 4; i++) {
//            selectPayWay = new AddPaymentTern();
//            selectPayWay.receiptTypeValue = "中国建设银行" + i;
//
//            selectPayWay.receiptType=i;
//            selectPayWayGroup.add(selectPayWay);
//        }
//        addReceiptAdapter.setGroup(selectPayWayGroup);
        startOtcFindPaymentOptionList();

    }

    private void startOtcFindPaymentOptionList() {
        CommonUtil.cancelCall(getPaymentOptionListCall);
        getPaymentOptionListCall = VHttpServiceManager.getInstance().getVService().otcFindPaymentOptionList();
        getPaymentOptionListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<AddPaymentTern> group = resultData.getGroup("paymentOptionList", new TypeToken<Group<AddPaymentTern>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        addReceiptAdapter.setGroup(group);
                    }
                }
            }
        });
    }

}

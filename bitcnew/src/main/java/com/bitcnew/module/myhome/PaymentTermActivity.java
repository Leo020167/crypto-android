package com.bitcnew.module.myhome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.module.myhome.entity.Receipt;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.myhome.adapter.PaymentTernAdapter;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.google.gson.reflect.TypeToken;
import com.bitcnew.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * (选择)收款方式 收款管理
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class PaymentTermActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.rvPaymentTremList)
    RecyclerView rvPaymentTremList;
    @BindView(R.id.tvAdd)
    TextView tvAdd;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;

    private Call<ResponseBody> otcFindMyPaymentListCall;
    //    private Call<ResponseBody> getInoutCreateCall;
    private PaymentTernAdapter paymentTernAdapter;
    private int jumpType;//0 我的收款方式 1选择我的收款方式
//    private String entrustAmount = "0.00";//jumpType==1时用到

    @Override
    protected int setLayoutId() {
        return R.layout.payment_trem;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }


    public static void pageJump(Context context, int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.JUMPTYPE, type);
//        bundle.putString(CommonConst.ENTRUSTAMOUNT, entrustAmount);
        PageJumpUtil.pageJump(context, PaymentTermActivity.class, bundle);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startOtcFindMyPaymentList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.payment_trem);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.JUMPTYPE)) {
                jumpType = bundle.getInt(CommonConst.JUMPTYPE, 0);
//                entrustAmount = bundle.getString(CommonConst.ENTRUSTAMOUNT, "0.00");
            }
        }

        ButterKnife.bind(this);

        paymentTernAdapter = new PaymentTernAdapter(this, jumpType);
        rvPaymentTremList.setLayoutManager(new LinearLayoutManager(this));
        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor), 10);
        simpleRecycleDivider.setShowLastDivider(true);
        rvPaymentTremList.addItemDecoration(simpleRecycleDivider);
        rvPaymentTremList.setAdapter(paymentTernAdapter);

        if (jumpType == 0) {
            mActionBar.setTitle(getResources().getString(R.string.shoukuanguanli));
//            tvAdd.setVisibility(View.VISIBLE);
        } else {
            mActionBar.setTitle(getResources().getString(R.string.xuanzeshoukuanfangshi));
//            tvAdd.setVisibility(View.GONE);
            paymentTernAdapter.setOnItemClick(new OnItemClick() {
                @Override
                public void onItemClickListen(int pos, TaojinluType t) {
                    Receipt receipt = (Receipt) t;
//                    startInoutCreate(receipt.receiptId);
                    if (jumpType == 0) {

                    } else {
                        Intent intent = new Intent();
                        Bundle resultBundle = new Bundle();
                        resultBundle.putParcelable("Receipt", receipt);
                        intent.putExtras(resultBundle);
                        setResult(0x789, intent);
                        finish();
                    }
                }
            });
        }
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PageJumpUtil.pageJump(PaymentTermActivity.this, AddReceiptTermActivity.class);
                AddReceiptTermActivity.pageJump(PaymentTermActivity.this, 0);
            }
        });

        startOtcFindMyPaymentList();

    }

    private void startOtcFindMyPaymentList() {
        CommonUtil.cancelCall(otcFindMyPaymentListCall);
        otcFindMyPaymentListCall = VHttpServiceManager.getInstance().getVService().otcFindMyPaymentList();
        otcFindMyPaymentListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<Receipt> groupMarket = resultData.getGroup("myPaymentList", new TypeToken<Group<Receipt>>() {
                    }.getType());
                    if (groupMarket != null && groupMarket.size() > 0) {
                        rvPaymentTremList.setVisibility(View.VISIBLE);
                        llNodata.setVisibility(View.GONE);
                        paymentTernAdapter.setGroup(groupMarket);
                    } else {
                        rvPaymentTremList.setVisibility(View.GONE);
                        llNodata.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

//    private void startInoutCreate(long receiptId) {
//        CommonUtil.cancelCall(getInoutCreateCall);
//        getInoutCreateCall = VHttpServiceManager.getInstance().getVService().inoutCreate(entrustAmount, receiptId, -1);
//        getInoutCreateCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    CommonUtil.showmessage(resultData.msg, PaymentTermActivity.this);
//                }
//            }
//        });
//    }

}

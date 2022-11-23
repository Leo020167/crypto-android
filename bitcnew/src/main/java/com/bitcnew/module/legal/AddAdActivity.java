package com.bitcnew.module.legal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.legal.adapter.MultiSelectQuickPayWayAdapter;
import com.bitcnew.module.legal.entity.OptionalOrder;
import com.bitcnew.module.myhome.AddReceiptTermActivity;
import com.bitcnew.module.myhome.entity.AddPaymentTern;
import com.bitcnew.module.myhome.entity.Receipt;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.SimpleSpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 添加广告
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class AddAdActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.etMinCny)
    EditText etMinCny;
    @BindView(R.id.etMaxCny)
    EditText etMaxCny;
    @BindView(R.id.rvType)
    RecyclerView rvType;
    //    @BindView(R.id.flAddPayMent)
//    FrameLayout flAddPayMent;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvBestPrice)
    TextView tvBestPrice;
    @BindView(R.id.tvBestPriceHint)
    TextView tvBestPriceHint;
    @BindView(R.id.tvIntoBestPrice)
    TextView tvIntoBestPrice;

    private int decimalCount = 2;//价格小数点

    private int amountDecimalCount = 4;//数量小数点

    private long adId;//>0就是编辑
    private OptionalOrder optionalOrder;

    private Call<ResponseBody> otcFindMyPaymentListCall, otcAddMyAdCall, otcUpdateMyAdCall, otcGetMyAdInfoCall, otcGetAdPriceCall;

    private MultiSelectQuickPayWayAdapter multiSelectQuickPayWayAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.add_ad;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.tianjiaguanggao);
    }


    public static void pageJump(Context context, long adId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ADID, adId);
//        bundle.putString(CommonConst.ENTRUSTAMOUNT, entrustAmount);
        PageJumpUtil.pageJump(context, AddAdActivity.class, bundle);


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
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ADID)) {
                adId = bundle.getLong(CommonConst.ADID, 0);
            }
        }
        multiSelectQuickPayWayAdapter = new MultiSelectQuickPayWayAdapter(this);
        rvType.setLayoutManager(new GridLayoutManager(this, 3));
        rvType.addItemDecoration(new SimpleSpaceItemDecoration(this, 3, 3, 3, 3));
        rvType.setAdapter(multiSelectQuickPayWayAdapter);
//        flAddPayMent.setOnClickListener(this);

        tvBuy.setSelected(true);
        tvSell.setSelected(false);

        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        tvIntoBestPrice.setOnClickListener(this);

        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int posDot = s.toString().indexOf(".");
                if (0 == posDot) {//去除首位的"."
                    s.delete(0, 1);
                } else if (posDot > 0) {
                    if (s.length() - 1 - posDot > decimalCount) {//最多2位小数
                        s.delete(posDot + (decimalCount + 1), posDot + (decimalCount + 2));
                    }
                }
            }
        });
        etAmount.addTextChangedListener(amountTextWatcher);
//        etMinCny.addTextChangedListener(amountTextWatcher);
//        etMaxCny.addTextChangedListener(amountTextWatcher);
        if (adId > 0) {
            mActionBar.setTitle(getResources().getString(R.string.bianjiguanggao));
            startOtcGetMyAdInfo(adId);
        } else {
            mActionBar.setTitle(getResources().getString(R.string.tianjiaguanggao));
            startOtcFindMyPaymentList();
            startOtcGetAdPrice();
        }
    }


    TextWatcher amountTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int posDot = s.toString().indexOf(".");
            if (0 == posDot) {//去除首位的"."
                s.delete(0, 1);
            } else if (posDot > 0) {
                if (s.length() - 1 - posDot > amountDecimalCount) {//最多2位小数
                    s.delete(posDot + (amountDecimalCount + 1), posDot + (amountDecimalCount + 2));
                }
            }
        }
    };

    private void startOtcGetAdPrice() {
        CommonUtil.cancelCall(otcGetAdPriceCall);
        final String buySell = tvBuy.isSelected() ? "buy" : "sell";
        otcGetAdPriceCall = VHttpServiceManager.getInstance().getVService().otcGetAdPrice(buySell);
        otcGetAdPriceCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String bestPrice = resultData.getItem("price", String.class);
                    tvBestPrice.setText(bestPrice);
                    Log.d("otcGetAdPrice","buySell=="+buySell);
                    if ("buy".equals(buySell)) {
                        tvBestPriceHint.setText(getResources().getString(R.string.dangqiangoumaizuigaojia));
                    } else {
                        tvBestPriceHint.setText(getResources().getString(R.string.dangqianchushouzuidijia));
                    }

                }
            }
        });
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
//                        rvType.setVisibility(View.VISIBLE);
//                        flAddPayMent.setVisibility(groupMarket.size() > 2 ? View.GONE : View.VISIBLE);
                        multiSelectQuickPayWayAdapter.setGroup(groupMarket);

                        if (adId > 0 && optionalOrder != null) {
                            Group<AddPaymentTern> payWay = new Gson().fromJson(optionalOrder.payWay, new TypeToken<Group<AddPaymentTern>>() {
                            }.getType());
                            for (AddPaymentTern addPaymentTern : payWay) {
                                multiSelectQuickPayWayAdapter.addSelectPayway(addPaymentTern.paymentId);
                            }
                            multiSelectQuickPayWayAdapter.notifyDataSetChanged();
                        }
                    } else {
//                        rvType.setVisibility(View.GONE);
//                        flAddPayMent.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void startOtcGetMyAdInfo(long adId) {
        CommonUtil.cancelCall(otcGetMyAdInfoCall);
        otcGetMyAdInfoCall = VHttpServiceManager.getInstance().getVService().otcGetMyAdInfo(adId);
        otcGetMyAdInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    optionalOrder = resultData.getObject(OptionalOrder.class);
                    setData(optionalOrder);
                    startOtcFindMyPaymentList();
                    startOtcGetAdPrice();//如果是编辑， startOtcGetAdPrice必须调用在otcGetMyAdInfo接口之后
                }
            }
        });
    }

    private void setData(OptionalOrder optionalOrder) {
        if (optionalOrder != null) {
            if ("buy".equals(optionalOrder.buySell)) {
                tvBuy.setSelected(true);
                tvSell.setSelected(false);
            } else {
                tvBuy.setSelected(false);
                tvSell.setSelected(true);
            }
            tvBuy.setEnabled(false);
            tvSell.setEnabled(false);
            etPrice.setText(optionalOrder.price);
            etMinCny.setText(optionalOrder.minCny);
            etMaxCny.setText(optionalOrder.maxCny);
            etAmount.setText(optionalOrder.amount);
            etContent.setText(optionalOrder.content);


        }
    }

    private void startOtcUpdateMyAd(long adId, String buySell, String price, String minCny, String maxCny, String amount, String payWay, String content) {
        CommonUtil.cancelCall(otcUpdateMyAdCall);
        otcUpdateMyAdCall = VHttpServiceManager.getInstance().getVService().otcUpdateMyAd(adId, buySell, price, minCny, maxCny, amount, payWay, content);
        otcUpdateMyAdCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, AddAdActivity.this);
                    PageJumpUtil.finishCurr(AddAdActivity.this);
                }
            }
        });
    }

    private void startOtcAddMyAd(String buySell, String price, String minCny, String maxCny, String amount, String payWay, String content) {
        CommonUtil.cancelCall(otcAddMyAdCall);
        otcAddMyAdCall = VHttpServiceManager.getInstance().getVService().otcAddMyAd(buySell, price, minCny, maxCny, amount, payWay, content);
        otcAddMyAdCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, AddAdActivity.this);
                    PageJumpUtil.finishCurr(AddAdActivity.this);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flAddPayMent:
                AddReceiptTermActivity.pageJump(AddAdActivity.this, 1);
                break;
            case R.id.tvBuy:
                tvBuy.setSelected(true);
                tvSell.setSelected(false);
                startOtcGetAdPrice();
                break;
            case R.id.tvSell:
                tvBuy.setSelected(false);
                tvSell.setSelected(true);
                startOtcGetAdPrice();
                break;
            case R.id.tvIntoBestPrice:
                etPrice.setText(tvBestPrice.getText().toString());
                etPrice.setSelection(etPrice.getText().length());
                break;
            case R.id.tvSubmit:
                String buySell = tvBuy.isSelected() ? "buy" : "sell";
                String price = etPrice.getText().toString().trim();
                if (TextUtils.isEmpty(price)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshurujiage), AddAdActivity.this);
                    return;
                }

                String amount = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshurushuliang), AddAdActivity.this);
                    return;
                }


                String minCny = etMinCny.getText().toString().trim();
                if (TextUtils.isEmpty(minCny)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshuruzuixiaoxiane), AddAdActivity.this);
                    return;
                }
                String maxCny = etMaxCny.getText().toString().trim();
                if (TextUtils.isEmpty(maxCny)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshuruzuidaxiane), AddAdActivity.this);
                    return;
                }


                String ids = multiSelectQuickPayWayAdapter.getSelectSet();
                if (TextUtils.isEmpty(ids)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingxuanzeshoufukuanfangshi), AddAdActivity.this);
                    return;
                }
                Log.d("startOtcAddMyAd", "ids==" + ids);

                String content = etContent.getText().toString().trim();
//                if (TextUtils.isEmpty(content)) {
//                    CommonUtil.showmessage("请输入留言", AddAdActivity.this);
//                    return;
//                }
                if (etContent.length() > 140) {
                    CommonUtil.showmessage(getResources().getString(R.string.liuyanchangdubunengdayu), AddAdActivity.this);
                    return;
                }
                if (adId > 0) {
                    startOtcUpdateMyAd(adId, buySell, price, minCny, maxCny, amount, ids, content);
                } else {
                    startOtcAddMyAd(buySell, price, minCny, maxCny, amount, ids, content);

                }
                break;
        }
    }
}

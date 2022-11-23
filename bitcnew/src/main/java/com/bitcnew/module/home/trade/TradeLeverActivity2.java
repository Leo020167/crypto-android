package com.bitcnew.module.home.trade;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bitcnew.MainApplication;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.web.CommonWebViewActivity;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.error.TaojinluException;
import com.bitcnew.http.tjrcpt.CropymelHttpSocket;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.copy.CropyUserDataActivity;
import com.bitcnew.module.home.JiaoyijiluActivity;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.adapter.DangqianweituoAdapter;
import com.bitcnew.module.home.adapter.TradeCurrPositionAdapter3;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.home.entity.Prybar;
import com.bitcnew.module.home.trade.adapter.TradeCurrPositionAdapter;
import com.bitcnew.module.home.trade.fragment.TradeLeverFragment2;
import com.bitcnew.module.home.trade.history.CoinTradeEntrustLeverActivity;
import com.bitcnew.module.login.LoginActivity;
import com.bitcnew.module.wallet.LeverInfoActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.DensityUtil;
import com.bitcnew.util.InflaterUtils;
import com.bitcnew.util.JsonParserUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.TjrMinuteTaskPool;
import com.bitcnew.widgets.LoadMoreRecycleView;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.bitcnew.widgets.quotitian.StarDetailPriceView_N2;
import com.bitcnew.widgets.quotitian.StarRunTimeManager;
import com.bitcnew.widgets.quotitian.entity.StarArkBidBean;
import com.bitcnew.widgets.quotitian.entity.StarProData;
import com.bitcnew.widgets.quotitian.entity.jsonparser.StarProDataParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TradeLeverActivity2 extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    private static final String TAG = "TradeLeverActivity2";

    @BindView(R.id.llMore)
    LinearLayout llMore;
    @BindView(R.id.tvCopy)
    TextView tvCopy;

    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;

    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;
    @BindView(R.id.sdpDetailPriceList)
    StarDetailPriceView_N2 sdpDetailPriceList;

    @BindView(R.id.rvEntrustlist)
    LoadMoreRecycleView rvEntrustlist;
    @BindView(R.id.tvAllHis)
    TextView tvAllHis;
    @BindView(R.id.tvNoDataEntrust)
    TextView tvNoDataEntrust;
    @BindView(R.id.llEntrust)
    LinearLayout llEntrust;

    @BindView(R.id.rvPositionList)
    RecyclerView rvPositionList;
    @BindView(R.id.tvNoDataPosition)
    TextView tvNoDataPosition;
    @BindView(R.id.llHoldPostion)
    LinearLayout llHoldPostion;

    @BindView(R.id.tvEntrust)
    TextView tvEntrust;
    @BindView(R.id.tvPosition)
    TextView tvPosition;


    private String symbol = "";//币种
    private StarRunTimeManager starRunTimeManager;
    private TjrMinuteTaskPool tjrMinuteTaskPool;
    private Handler handler = new Handler();
    private StarProData starProData;

    private TradeLeverFragment2 tradeBuyFragment;
    private TradeLeverFragment2 tradeSellFragment;

    private Call<ResponseBody> realCall;
    private Call<ResponseBody> currPositionCall;

    private TradeCurrPositionAdapter3 tradeCurrPositionAdapter;
    private DangqianweituoAdapter tradeUndoneLeverAdapter;

    private int buySell = 1;

    private int currPos = 0;

    private String accountType;


    public static void pageJump(Context context, String symbol, int buySell) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putInt(CommonConst.BUYSELL, buySell);
//        bundle.putString(CommonConst.ACCOUNTTYPE, accountType);
        PageJumpUtil.pageJump(context, TradeLeverActivity2.class, bundle);
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
        startGetTradeOrderList(currPos);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.trade_lever2;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

//    @Override
//    protected void handlerMsg(ReceiveModel model) {
//        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
//            case refresh_entrust_order_api:
//                if (currPos == 0) {
//                    startGetTradeOrderList(1);
//                } else {
//                    startGetTradeOrderList(0);
//                }
//                break;
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        if (!isLogin()) {//这个页面一定要登录
            LoginActivity.login((this));
            finish();
            return;
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                symbol = bundle.getString(CommonConst.SYMBOL, "");
            }
            if (bundle.containsKey(CommonConst.BUYSELL)) {
                buySell = bundle.getInt(CommonConst.BUYSELL, 1);
            }
//            if (bundle.containsKey(CommonConst.ACCOUNTTYPE)) {
//                accountType = bundle.getString(CommonConst.ACCOUNTTYPE, "");
//            }

        }
        mActionBar.setTitle(symbol);
        sdpDetailPriceList.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                StarArkBidBean starArkBidBean = (StarArkBidBean) t;
                if (tradeBuyFragment != null && tradeBuyFragment.getUserVisibleHint()) {
                    tradeBuyFragment.onClickPrice(starArkBidBean.price);
                } else if (tradeSellFragment != null && tradeSellFragment.getUserVisibleHint()) {
                    tradeSellFragment.onClickPrice(starArkBidBean.price);
                }
            }
        });
        starRunTimeManager = new StarRunTimeManager();
        tjrMinuteTaskPool = new TjrMinuteTaskPool();
        tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());
        if (buySell == 1) {
            showBuyFragment();
        } else if (buySell ==2){
//            tvSell.setVisibility(View.GONE);
            tvSell.setVisibility(View.VISIBLE);
            showBuyFragment();
        }else {
            showSellFragment();
        }

        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);
        tvAllHis.setOnClickListener(this);
        llMore.setOnClickListener(this);

        rvEntrustlist.setLayoutManager(new LinearLayoutManager(this));
        rvEntrustlist.addItemDecoration(new SimpleRecycleDivider(this, 15, 15));
        tradeUndoneLeverAdapter = new DangqianweituoAdapter(this);
        rvEntrustlist.setAdapter(tradeUndoneLeverAdapter);
        tradeUndoneLeverAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                Prybar order = (Prybar) t;
                LeverInfoActivity.pageJump(TradeLeverActivity2.this, order.orderId,"2");
//                Bundle bundle = new Bundle();
//                bundle.putLong(CommonConst.ORDERID, order.orderId);
//                bundle.putInt(CommonConst.POS, pos);
//                PageJumpUtil.pageJumpResult(TradeActivity.this, CoinTradeDetailsActivity.class, bundle);

            }
        });
//        coinTradeEntrustAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
//        rv_list.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
//        coinTradeEntrustAdapter.setOnItemClick(new OnItemClick() {
//            @Override
//            public void onItemClickListen(int pos, TaojinluType t) {
//                Order order = (Order) t;
//                Bundle bundle = new Bundle();
//                bundle.putLong(CommonConst.ORDERID, order.orderId);
//                bundle.putInt(CommonConst.POS, pos);
//                PageJumpUtil.pageJumpResult(CoinTradeEntrustFragment.this, CoinTradeDetailsActivity.class, bundle);
//            }
//        });


        rvPositionList.setLayoutManager(new LinearLayoutManager(this));
        rvPositionList.addItemDecoration(new SimpleRecycleDivider(this, 15, 15));
        tradeCurrPositionAdapter = new TradeCurrPositionAdapter3(this);
        rvPositionList.setAdapter(tradeCurrPositionAdapter);

        tvEntrust.setOnClickListener(this);
        tvPosition.setOnClickListener(this);
//        slide(0);

        getRight();
    }

    public void onCancelOrder() {
        if (tradeBuyFragment != null && tradeBuyFragment.getUserVisibleHint()) {
            tradeBuyFragment.startCheckOut();
        } else if (tradeSellFragment != null && tradeSellFragment.getUserVisibleHint()) {
            tradeSellFragment.startCheckOut();
        }
    }

    private void slide(int pos) {
        currPos = pos;
        switch (pos) {
            case 0:
                tvEntrust.setSelected(false);
                tvPosition.setSelected(true);
                tvEntrust.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvEntrust.setTextColor(ContextCompat.getColor(this, R.color.c666175ae));
                tvPosition.setTextColor(ContextCompat.getColor(this, R.color.c6175ae));
                showHold();
                startGetTradeOrderList(0);
                break;
            case 1:
                tvEntrust.setSelected(true);
                tvPosition.setSelected(false);
                tvEntrust.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvEntrust.setTextColor(ContextCompat.getColor(this, R.color.c6175ae));
                tvPosition.setTextColor(ContextCompat.getColor(this, R.color.c666175ae));
                showEntrust();//先还是调用一次，避免网络不好的时候看起来有问题
                startGetTradeOrderList(1);
                break;
        }
    }

    private void showEntrust() {
        llHoldPostion.setVisibility(View.GONE);
        llEntrust.setVisibility(View.VISIBLE);
        if (tradeUndoneLeverAdapter.getRealItemCount() > 0) {
            rvEntrustlist.setVisibility(View.VISIBLE);
            tvNoDataEntrust.setVisibility(View.GONE);
        } else {
            rvEntrustlist.setVisibility(View.GONE);
            tvNoDataEntrust.setVisibility(View.VISIBLE);
        }
    }

    private void showHold() {
        llHoldPostion.setVisibility(View.VISIBLE);
        llEntrust.setVisibility(View.GONE);
        try {
            if (null!=tradeCurrPositionAdapter){
                if (tradeCurrPositionAdapter.getItemCount() > 0) {
                    rvPositionList.setVisibility(View.VISIBLE);
                    tvNoDataPosition.setVisibility(View.GONE);
                } else {
                    rvPositionList.setVisibility(View.GONE);
                    tvNoDataPosition.setVisibility(View.VISIBLE);
                }
            }
        }catch (Exception e){

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        slide(currPos);
//        if (currPos == 0) {
//            startGetTradeOrderList(1);
//        } else {
//            startGetTradeOrderList(0);
//        }
        CommonUtil.LogLa(2, "OLStarHomeActivity onResume");
        starRunTimeManager.onResume();
        if (tjrMinuteTaskPool != null)
            tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());
    }


    @Override
    protected void onPause() {
        super.onPause();
        starRunTimeManager.onPause();
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();

    }

    @Override
    protected void onStop() {
        super.onStop();
        CommonUtil.LogLa(2, "OLStarHomeActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtil.LogLa(2, "OLStarHomeActivity onDestroy");
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
    }

    private Call<ResponseBody> getCoinGraphDataCall;
    private Runnable scheduledTask = new Runnable() {
        public void run() {
            try { //当前价格
                String procText = CropymelHttpSocket.getInstance().sendProdCode(symbol, 5);//5
                Log.d("190", "procText == " + procText);
                CommonUtil.LogLa(2, " procText is " + procText);
                pareserProCodeJsons(procText);
                try {
//                    minuteLineChart.parserJsonStock(starProData);
//                    minuteHourLineChart.parserJsonStock(starProData);
                } catch (Exception e) {
                    CommonUtil.LogLa(2, "Exception e is " + e.getMessage());
                }
                getRight();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (starProData != null) {
                            updateUI();
//                            autoShowTradeDialog();//防止
                        }
                    }
                });
                startGetTradeOrderList(currPos);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!MainApplication.isRun) {
                tjrMinuteTaskPool.closeTime();
            }
        }
    };

    private void getRight(){
        getCoinGraphDataCall = VHttpServiceManager.getInstance().getMarketService().getQuoteReal(symbol, 30, "min1");
        getCoinGraphDataCall.enqueue(new MyCallBack(TradeLeverActivity2.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    try {
                        pareserProCodeJsons(resultData.data);
                    } catch (TaojinluException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    public void refreshOnCreateOrder(){//创建订单成功刷新一下，避免延迟出现订单
        startGetTradeOrderList(currPos);
    }

    private void updateUI() {
        if (starProData == null) return;
//        tvPrice.setText("当前价:¥" + StockChartUtil.formatNumber(2, starProData.last));
//        tvPrice.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));
//        tvPriceRate.setText(StockChartUtil.formatNumWithSign(2, starProData.rate, true) + "%");
//        tvPriceRate.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));

        Log.d("TradeActivity", "starProData.rate==" + starProData.rate);
        sdpDetailPriceList.updateDateDetail(starProData.currency, starProData.buys, starProData.sells, starProData.last, starProData.lastCny, starProData.rate);
        if (tradeBuyFragment != null && tradeBuyFragment.getUserVisibleHint()) {
            tradeBuyFragment.setDefaultLast(starProData.last);
            tradeBuyFragment.setDecimals(starProData.priceDecimals, starProData.amountDecimals);
        } else if (tradeSellFragment != null && tradeSellFragment.getUserVisibleHint()) {
            tradeSellFragment.setDecimals(starProData.priceDecimals, starProData.amountDecimals);
            tradeSellFragment.setDefaultLast(starProData.last);
        }
    }

    private synchronized void pareserProCodeJsons(String json) throws TaojinluException, JSONException {
        if (json == null) return;
        JSONObject jsonObject = new JSONObject(json);
        if (JsonParserUtils.hasAndNotNull(jsonObject, "isRun")) {
            MainApplication.isRun = jsonObject.getBoolean("isRun");
        }
        if (JsonParserUtils.hasAndNotNull(jsonObject, symbol)) {
            starProData = new StarProDataParser().parse(jsonObject.getJSONObject(symbol));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuy:
                showBuyFragment();
                break;
            case R.id.tvSell:
                showSellFragment();
                break;
            case R.id.tvAllHis:
//                CoinTradeEntrustLeverActivity.pageJump(TradeLeverActivity2.this, accountType);
                JiaoyijiluActivity.pageJump(TradeLeverActivity2.this, "spot");
                break;
            case R.id.tvCopy:
                PageJumpUtil.pageJump(TradeLeverActivity2.this, CropyUserDataActivity.class);
                break;
            case R.id.tvFunction:
                dissPop();
                CommonWebViewActivity.pageJumpCommonWebViewActivity(TradeLeverActivity2.this, CommonConst.PASSGEDETAIL_28);
                break;
            case R.id.tvTrade:
                dissPop();
                CommonWebViewActivity.pageJumpCommonWebViewActivity(TradeLeverActivity2.this, CommonConst.PASSGEDETAIL_29);
                break;
            case R.id.llMore:
                showPopupMenu(llMore);
                break;
            case R.id.tvEntrust:
                slide(1);
//                startGetTradeOrderList();
                break;
            case R.id.tvPosition:
                slide(0);
//                startCurrHold();
                break;
        }
    }


    private void showBuyFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (tradeBuyFragment == null) {
            tradeBuyFragment = TradeLeverFragment2.newInstance(symbol, 1);
            transaction.add(R.id.fragment_container, tradeBuyFragment);
        }
        hideAllFragement();
        transaction.show(tradeBuyFragment);
        Log.d(TAG, "ShowBuyFragment ,fg_contacts is not null");
        transaction.commit();
        tradeBuyFragment.setUserVisibleHint(true);

        tvBuy.setSelected(true);
        tvSell.setSelected(false);

    }


    private void showSellFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (tradeSellFragment == null) {
            tradeSellFragment = TradeLeverFragment2.newInstance(symbol, -1);
            transaction.add(R.id.fragment_container, tradeSellFragment);
        }
        hideAllFragement();
        transaction.show(tradeSellFragment);
        Log.d(TAG, "ShowBuyFragment ,fg_contacts is not null");
        transaction.commit();
        tradeSellFragment.setUserVisibleHint(true);

        tvBuy.setSelected(false);
        tvSell.setSelected(true);

    }


    public void hideAllFragement() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (tradeBuyFragment != null) {
            transaction.hide(tradeBuyFragment);
            tradeBuyFragment.setUserVisibleHint(false);
        }
        if (tradeSellFragment != null) {
            transaction.hide(tradeSellFragment);
            tradeSellFragment.setUserVisibleHint(false);
        }

        transaction.commit();

    }

    String orderState;
    public void startGetTradeOrderList(int isDone) {
        CommonUtil.cancelCall(realCall);
        if (TextUtils.isEmpty(accountType)) {//因为这个accountType是里面fragment获取的，所以先判断一下
            return;
        }
        if (isDone == 0) {//持仓
            orderState = "1";
        }else {//委托
            orderState = "0";
        }
        realCall = VHttpServiceManager.getInstance().getVService().queryList2("", accountType, isDone, "", 1, orderState,"2");
        realCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<Position> group = null;
                if (resultData.isSuccess()) {
                    group = resultData.getGroup("data", new TypeToken<Group<Position>>() {
                    }.getType());
                    if (currPos == 0) {//开仓
                        tradeCurrPositionAdapter.setGroup(group);
                        showHold();
                    } else {//委托
                        tradeUndoneLeverAdapter.setGroup(group);
//                        tradeUndoneLeverAdapter.onLoadComplete(resultData.isSuccess(), true);
                        showEntrust();
                    }
//                    if (group != null && group.size() > 0) {
//                    tradeUndoneLeverAdapter.setGroup(group);
//                    }
                }

//                showEntrust();
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
//                tradeUndoneLeverAdapter.onLoadComplete(false, false);
            }
        });
    }

//    public void startCurrHold() {
//        CommonUtil.cancelCall(currPositionCall);
//        currPositionCall = VHttpServiceManager.getInstance().getVService().currHold(symbol);
//        currPositionCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//
//                if (resultData.isSuccess()) {
//                    Position position = resultData.getObject("hold", Position.class);
//                    if (position != null && !TextUtils.isEmpty(position.symbol)) {
//                        Group<Position> group = new Group<>();
//                        group.add(position);
//                        tradeCurrPositionAdapter.setGroup(group);
//                    } else {
//                        tradeCurrPositionAdapter.setGroup(null);
//                    }
//                    showHold();
//
//                }
//            }
//        });
//    }


    PopupWindow pop;
    TextView tvFunction, tvTrade;

    private void showPopupMenu(View parent) {
        if (pop == null) {
            View view = InflaterUtils.inflateView(TradeLeverActivity2.this, R.layout.lever_more_menu);

            tvFunction = view.findViewById(R.id.tvFunction);
            tvTrade = view.findViewById(R.id.tvTrade);

            tvFunction.setOnClickListener(this);
            tvTrade.setOnClickListener(this);

            pop = new PopupWindow(view, DensityUtil.dip2px(this, 140), ViewGroup.LayoutParams.WRAP_CONTENT);//

            pop.setOutsideTouchable(false);
            pop.setFocusable(false);//
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            pop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(TradeLeverActivity2.this, R.color.transparent)));
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

        }
        if (pop != null && !pop.isShowing()) {
//            pop.showAsDropDown(parent);
            pop.showAsDropDown(parent, -20, -20);
//            pop.showAtLocation(parent,Gravity.CENTER,50,50);
        }
    }

    private void dissPop() {
        if (pop != null & pop.isShowing()) {
            pop.dismiss();
        }
    }


}

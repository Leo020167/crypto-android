package com.bitcnew.module.home.trade;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.entity.Order;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.home.trade.adapter.TradeUndoneAdapter;
import com.bitcnew.module.home.trade.fragment.TradeBuyFragment;
import com.bitcnew.module.home.trade.fragment.TradeSellFragment;
import com.bitcnew.module.home.trade.history.CoinTradeEntrustActivity;
import com.bitcnew.module.login.LoginActivity;
import com.bitcnew.subpush.ReceiveModel;
import com.bitcnew.subpush.ReceiveModelTypeEnum;
import com.bitcnew.util.CommonUtil;
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
import com.bitcnew.MainApplication;
import com.bitcnew.R;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.error.TaojinluException;
import com.bitcnew.http.tjrcpt.CropymelHttpSocket;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.copy.CropyUserDataActivity;
import com.bitcnew.module.home.trade.adapter.TradeCurrPositionAdapter;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 买卖页面
 */
public class TradeActivity extends TJRBaseActionBarSwipeBackObserverActivity implements View.OnClickListener {


    private static final String TAG = "TradeActivity";

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

    private TradeBuyFragment tradeBuyFragment;
    private TradeSellFragment tradeSellFragment;

    private Call<ResponseBody> realCall;
    private Call<ResponseBody> currPositionCall;
    private TradeUndoneAdapter coinTradeEntrustAdapter;
    private TradeCurrPositionAdapter tradeCurrPositionAdapter;

    private int buySell = 1;


    private int currPos = 0;


    public static void pageJump(Context context, String symbol, int buySell) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putInt(CommonConst.BUYSELL, buySell);
        PageJumpUtil.pageJump(context, TradeActivity.class, bundle);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.trade;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void handlerMsg(ReceiveModel model) {
        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
            case refresh_entrust_order_api:
                if (currPos == 0) {
                    startGetTradeOrderList();
                } else {
                    startCurrHold();
                }
                if (tradeBuyFragment != null) {
                    tradeBuyFragment.onCancelOrder();
                }
                if (tradeSellFragment != null) {
                    tradeSellFragment.onCancelOrder();
                }
                break;
        }
    }


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
        } else {
            showSellFragment();
        }
        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);
        tvAllHis.setOnClickListener(this);

        rvEntrustlist.setLayoutManager(new LinearLayoutManager(this));
        rvEntrustlist.addItemDecoration(new SimpleRecycleDivider(this, 15, 15));
        coinTradeEntrustAdapter = new TradeUndoneAdapter(this);
        rvEntrustlist.setAdapter(coinTradeEntrustAdapter);
        coinTradeEntrustAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                Order order = (Order) t;
                CoinTradeDetailsActivity.pageJump(TradeActivity.this, order.orderId);
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
        tradeCurrPositionAdapter = new TradeCurrPositionAdapter(this);
        rvPositionList.setAdapter(tradeCurrPositionAdapter);

        tvEntrust.setOnClickListener(this);
        tvPosition.setOnClickListener(this);
//        slide(0);


    }

    private void slide(int pos) {
        currPos = pos;
        switch (pos) {
            case 0:
                tvEntrust.setSelected(true);
                tvPosition.setSelected(false);
                tvEntrust.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvEntrust.setTextColor(ContextCompat.getColor(this, R.color.c6175ae));
                tvPosition.setTextColor(ContextCompat.getColor(this, R.color.c666175ae));
                showEntrust();//先还是调用一次，避免网络不好的时候看起来有问题
                startGetTradeOrderList();

                break;
            case 1:

                tvEntrust.setSelected(false);
                tvPosition.setSelected(true);
                tvEntrust.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvEntrust.setTextColor(ContextCompat.getColor(this, R.color.c666175ae));
                tvPosition.setTextColor(ContextCompat.getColor(this, R.color.c6175ae));
                showHold();
                startCurrHold();

                break;
        }
    }

    private void showEntrust() {
        llEntrust.setVisibility(View.VISIBLE);
        llHoldPostion.setVisibility(View.GONE);
        if (coinTradeEntrustAdapter.getRealItemCount() > 0) {
            rvEntrustlist.setVisibility(View.VISIBLE);
            tvNoDataEntrust.setVisibility(View.GONE);
        } else {
            rvEntrustlist.setVisibility(View.GONE);
            tvNoDataEntrust.setVisibility(View.VISIBLE);
        }
    }

    private void showHold() {
        llEntrust.setVisibility(View.GONE);
        llHoldPostion.setVisibility(View.VISIBLE);
        if (tradeCurrPositionAdapter.getItemCount() > 0) {
            rvPositionList.setVisibility(View.VISIBLE);
            tvNoDataPosition.setVisibility(View.GONE);
        } else {
            rvPositionList.setVisibility(View.GONE);
            tvNoDataPosition.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        slide(currPos);
//        if(currPos==0){
//            startGetTradeOrderList();
//        }else{
//            startCurrHold();
//        }
        CommonUtil.LogLa(2, "OLStarHomeActivity onResume");
        starRunTimeManager.onResume();
        if (tjrMinuteTaskPool != null)
            tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());
    }

    public void showCopy(String copy) {
        tvCopy.setVisibility(View.VISIBLE);
        tvCopy.setText(copy);
        tvCopy.setOnClickListener(this);
    }

    public void hideCopy() {
        tvCopy.setVisibility(View.GONE);
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (starProData != null) {
                            updateUI();
//                            autoShowTradeDialog();//防止
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!MainApplication.isRun) {
                tjrMinuteTaskPool.closeTime();
            }
        }
    };

    private void updateUI() {
        if (starProData == null) return;
//        tvPrice.setText("当前价:HK$" + StockChartUtil.formatNumber(2, starProData.last));
//        tvPrice.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));
//        tvPriceRate.setText(StockChartUtil.formatNumWithSign(2, starProData.rate, true) + "%");
//        tvPriceRate.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));

        Log.d("TradeActivity", "starProData.rate==" + starProData.rate);
        sdpDetailPriceList.updateDateDetail(starProData.currency, starProData.buys, starProData.sells, starProData.last, starProData.lastCny, starProData.rate);
        if (tradeBuyFragment != null && tradeBuyFragment.getUserVisibleHint()) {
            tradeBuyFragment.setDefaultLast(starProData.last, starProData.lastCny);
            tradeBuyFragment.setDecimals(starProData.priceDecimals, starProData.amountDecimals);
        } else if (tradeSellFragment != null && tradeSellFragment.getUserVisibleHint()) {
            tradeSellFragment.setDefaultLast(starProData.last, starProData.lastCny);
            tradeSellFragment.setDecimals(starProData.priceDecimals, starProData.amountDecimals);
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
                PageJumpUtil.pageJump(TradeActivity.this, CoinTradeEntrustActivity.class);
                break;
            case R.id.tvCopy:
                PageJumpUtil.pageJump(TradeActivity.this, CropyUserDataActivity.class);
                break;
            case R.id.tvEntrust:
                slide(0);
//                startGetTradeOrderList();
                break;
            case R.id.tvPosition:
                slide(1);
//                startCurrHold();
                break;
        }
    }


    private void showBuyFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (tradeBuyFragment == null) {
            tradeBuyFragment = TradeBuyFragment.newInstance(symbol);
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
            tradeSellFragment = TradeSellFragment.newInstance(symbol);
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


    public void startGetTradeOrderList() {
        CommonUtil.cancelCall(realCall);
        realCall = VHttpServiceManager.getInstance().getVService().tradeOrderList(symbol, "", 0, "", 1);
        realCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<Order> group = null;
                if (resultData.isSuccess()) {
                    group = resultData.getGroup("data", new TypeToken<Group<Order>>() {
                    }.getType());
//                    if (group != null && group.size() > 0) {
                    coinTradeEntrustAdapter.setGroup(group);
//                    }
                }
                coinTradeEntrustAdapter.onLoadComplete(resultData.isSuccess(), true);
                showEntrust();
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                coinTradeEntrustAdapter.onLoadComplete(false, false);
            }
        });
    }

    public void startCurrHold() {
        CommonUtil.cancelCall(currPositionCall);
        currPositionCall = VHttpServiceManager.getInstance().getVService().currHold(symbol);
        currPositionCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {

                if (resultData.isSuccess()) {
                    Position position = resultData.getObject("hold", Position.class);
                    if (position != null && !TextUtils.isEmpty(position.symbol)) {
                        Group<Position> group = new Group<>();
                        group.add(position);
                        tradeCurrPositionAdapter.setGroup(group);
                    } else {
                        tradeCurrPositionAdapter.setGroup(null);
                    }
                    showHold();

                }
            }
        });
    }


}

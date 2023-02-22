package com.bitcnew.module.home.trade.fragment;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.bean.CheckOutBean;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.fragment.UserBaseFragment;
import com.bitcnew.module.home.trade.TradeLeverActivity2;
import com.bitcnew.module.home.trade.TransferCoinActivity;
import com.bitcnew.module.home.trade.adapter.LeverTypeAdapter;
import com.bitcnew.module.home.trade.adapter.MultiNumAdapter;
import com.bitcnew.module.wallet.dialog.BuyOrSellTipsDialog;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.InflaterUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.StockChartUtil;
import com.bitcnew.widgets.SimpleSpaceItemDecoration;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class TradeLeverFragment2 extends UserBaseFragment implements View.OnClickListener {

    @BindView(R.id.llMenu)
    FrameLayout llMenu;
    @BindView(R.id.rvHand)
    RecyclerView rvHand;
    @BindView(R.id.tvMultNum)
    TextView tvMultNum;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.tvOrderType)
    TextView tvOrderType;
    @BindView(R.id.llOrderType)
    FrameLayout llOrderType;
    @BindView(R.id.tvMarketHint)
    TextView tvMarketHint;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.txt_shou)
    TextView txt_shou;
    @BindView(R.id.tvMinus)
    TextView tvMinus;
    @BindView(R.id.tvPlus)
    TextView tvPlus;
    @BindView(R.id.llLimitHint)
    FrameLayout llLimitHint;
    @BindView(R.id.etHand)
    EditText etHand;
    @BindView(R.id.tvMaxHand)
    TextView tvMaxHand;
    @BindView(R.id.tvBail)
    TextView tvBail;
    @BindView(R.id.tvTransfer)
    TextView tvTransfer;
    @BindView(R.id.quickInputContainer)
    ViewGroup quickInputContainer;

    private Call<ResponseBody> getPrybarConfigCall;
    private Call<ResponseBody> getPrybarCheckOutCall;
    private Call<ResponseBody> getPrybarCreateOrderCall;

    private String symbol;
    private boolean setDefaultFlag = false;


    private BuyOrSellTipsDialog buyOrSellTipsDialog;
    private Call<ResponseBody> getTradeOrderCall;

    private LeverTypeAdapter leverTypeAdapter;
    private MultiNumAdapter multiNumAdapter;
    private String hand = "0";//手数
    private double d_hand,d_maxHand,d_availableAmount;//输入数量，可买入数量，可卖出数量
    private int buySell = 1;
    private String holdUsdt = "0.0";
    private String[] multiNumList;
    private String[] initHandList;
    private String[] openRateList;
    private String leverMultiple = "";

    private int priceDecimals = 2;//价格的小数点数量
    private int amountDecimals = 4;//数量的小数点数量
    private String openFeeScale = "";
    private String openBail = "";//开仓保证金

    private Handler handler = new Handler();


    public static TradeLeverFragment2 newInstance(String symbol, int buySell) {
        TradeLeverFragment2 fragment = new TradeLeverFragment2();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putInt(CommonConst.BUYSELL, buySell);
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick(value = {R.id.quick20Tv,R.id.quick40Tv,R.id.quick60Tv,R.id.quick80Tv,R.id.quick100Tv})
    public void handleQuickInputClick(View v) {
        if (!(v instanceof TextView)) {
            return;
        }

        String text = (String)v.getTag();
        if (null == text || text.length() == 0) {
            text = ((TextView) v).getText().toString().replace("%", "").trim();
        }
        if (text.length() == 0) {
            return;
        }

        try {
            int value = (int) (d_maxHand * Double.parseDouble(text) / 100);
            etHand.setText(String.valueOf(value));
        } catch (Exception e) {

        }
    }

    private void initQuickInput() {
        quickInputContainer.setVisibility(View.GONE);
        if (openRateList == null || openRateList.length == 0) {
            return;
        }

        quickInputContainer.removeAllViews();
        for (String quick : openRateList) {
            TextView text = new AppCompatTextView(getActivity());
            text.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 1));
            text.setGravity(Gravity.CENTER);
            text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,10);
            text.setTextColor(Color.parseColor("#333333"));
            text.setText(quick + "%");
            text.setTag(quick);
            text.setOnClickListener(this::handleQuickInputClick);

            if (quickInputContainer.getChildCount() != 0) {
                View line = new View(getActivity());
                line.setLayoutParams(new LinearLayout.LayoutParams(1, -1));
                line.setBackgroundColor(Color.parseColor("#DDDDDD"));
                quickInputContainer.addView(line);
            }

            quickInputContainer.addView(text);
        }
        quickInputContainer.setVisibility(View.VISIBLE);
    }

    public void setDecimals(int priceDecimals, int amountDecimals) {
        this.priceDecimals = priceDecimals;
        this.amountDecimals = amountDecimals;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (null == b) return;
        symbol = b.getString(CommonConst.SYMBOL, "");
        buySell = b.getInt(CommonConst.BUYSELL, 1);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("TradeLeverActivity2", "TradeBuyFragment setUserVisibleHint==" + isVisibleToUser + " getActivity()==" + getActivity());
        if (getActivity() == null) return;
        if (isVisibleToUser) {
            startPrybarConfig();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trade_buy_lever2, container, false);
        ButterKnife.bind(this, view);

        if (!TextUtils.isEmpty(symbol)){
            txt_shou.setText(symbol);
        }


        leverTypeAdapter = new LeverTypeAdapter(getActivity());
        rvHand.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rvHand.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 10, 0, 5, 5));
        rvHand.setAdapter(leverTypeAdapter);
//        leverTypeAdapter.setData(new String[]{"18", "20", "30", "50", "80", "100",});
        leverTypeAdapter.setOnItemclickListen(new LeverTypeAdapter.onItemclickListen() {
            @Override
            public void onItemclick(String leverType) {
                hand = leverType;
                etHand.setText(hand);
//                startCheckOut();
//                startGetCoinInfo();//改变键类型要刷新
            }
        });
        llMenu.setOnClickListener(this);
        llOrderType.setOnClickListener(this);

        tvPlus.setOnClickListener(this);
        tvMinus.setOnClickListener(this);

        tvTransfer.setOnClickListener(this);

        if (buySell == 1) {
            tvBuy.setVisibility(View.VISIBLE);
            tvSell.setVisibility(View.GONE);
            tvBuy.setOnClickListener(this);
            etPrice.setBackgroundResource(R.drawable.xml_et_bg_2);
            etHand.setBackgroundResource(R.drawable.xml_et_bg_2);
            tvTransfer.setVisibility(View.GONE);
        } else {
            tvBuy.setVisibility(View.GONE);
            tvSell.setVisibility(View.VISIBLE);
            tvSell.setOnClickListener(this);
            etPrice.setBackgroundResource(R.drawable.xml_et_bg_3);
            etHand.setBackgroundResource(R.drawable.xml_et_bg_3);
            tvTransfer.setVisibility(View.GONE);
        }

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
                    if (s.length() - 1 - posDot > priceDecimals) {//最多4位小数
                        s.delete(posDot + (priceDecimals + 1), posDot + (priceDecimals + 2));
                    }
                }
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 500);
            }
        });

        etHand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    leverTypeAdapter.setSelected(s.toString());
                    hand = s.toString().trim();
                    d_hand = Double.parseDouble(hand);
                } else {
                    hand = "0";
                }
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 500);
            }
        });
        switchMarketPrice();
        return view;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startCheckOut();
        }
    };

    private void startPrybarConfig() {
        CommonUtil.cancelCall(getPrybarConfigCall);
        getPrybarConfigCall = VHttpServiceManager.getInstance().getVService().prybarConfig(symbol);
        getPrybarConfigCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    holdUsdt = resultData.getItem("holdUsdt", String.class);
                    multiNumList = resultData.getStringArray("multiNumList");
                    initHandList = resultData.getStringArray("initHandList");
                    openRateList = resultData.getStringArray("openRateList");

                    priceDecimals = resultData.getItem("priceDecimals", Integer.class);
                    openFeeScale = resultData.getItem("openFeeScale", String.class);

                    String accountType = resultData.getItem("accountType", String.class);

                    if (getActivity() != null && getActivity() instanceof TradeLeverActivity2) {
                        TradeLeverActivity2 TradeLeverActivity2 = (TradeLeverActivity2) getActivity();
                        TradeLeverActivity2.setAccountType(accountType);
                    }

//                    tvEnableBalance.setText("可用" + holdUsdt + "USDT");
                    leverTypeAdapter.setData(initHandList);

                    if (TextUtils.isEmpty(leverMultiple)) {//第一次为null
                        if (multiNumList != null && multiNumList.length > 0) {
                            leverMultiple = multiNumList[0];
                            tvMultNum.setText(leverMultiple + "X");
                        }
                        if (initHandList != null && initHandList.length > 0) {
                            hand = initHandList[0];
                            leverTypeAdapter.setSelected(hand);
                            etHand.setText(hand);
                        }

                    }
                    startCheckOut();
                    initQuickInput();
                }
            }
        });
    }

    /**
     * 设置默认价格，只设置一次就好
     */
    public void setDefaultLast(double last) {
        if (getActivity() == null) return;
        if (setDefaultFlag) return;
        if (etPrice != null && TextUtils.isEmpty(etPrice.getText().toString().trim())) {
            etPrice.setText(String.valueOf(last));
            setDefaultFlag = true;
        }
    }

    private String getPrice() {
        if (orderType.equals("limit")) {
            String p = etPrice.getText().toString().trim();
            if (TextUtils.isEmpty(p)) {
                return "0";
            } else {
                return p;
            }

        } else if (orderType.equals("market")) {
            return "0";
        }
        return "0";
    }

    public void startCheckOut() {
        CommonUtil.cancelCall(getPrybarCheckOutCall);
        getPrybarCheckOutCall = VHttpServiceManager.getInstance().getVService().prybarCheckOut2(symbol, getPrice(), buySell == 1 ? "buy" : "sell", hand, leverMultiple, orderType,"2");
        getPrybarCheckOutCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (buySell==1){//买入
                        String maxHand = resultData.getItem("maxHand", String.class);
                        if (!TextUtils.isEmpty(maxHand)) {
                            d_maxHand = Double.parseDouble(maxHand);
                            tvMaxHand.setText(getActivity().getResources().getString(R.string.kekai1) + maxHand + symbol);
                        }
                        openBail = resultData.getItem("needMoney", String.class);
                        tvBail.setText(openBail);
                    }else {//卖出
                        String maxHand = resultData.getItem("availableAmount", String.class);
                        if (!TextUtils.isEmpty(maxHand)) {
                            d_maxHand = Double.parseDouble(maxHand);
                            tvMaxHand.setText(getActivity().getResources().getString(R.string.kekai2) + maxHand + symbol);
                        }
                        openBail = resultData.getItem("usdtAmount", String.class);
                        tvBail.setText(openBail);
                    }
                }
            }
        });
    }


    private void startCreateOrder(String payPass) {
        CommonUtil.cancelCall(getPrybarCreateOrderCall);

        getPrybarCreateOrderCall = VHttpServiceManager.getInstance().getVService().prybarCreateOrder3(symbol, getPrice(), buySell == 1 ? "buy" : "sell", hand, leverMultiple, orderType, payPass,"2");
        getPrybarCreateOrderCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, getActivity());
                    startCheckOut();
                    if (getActivity() != null && getActivity() instanceof TradeLeverActivity2) {
                        ((TradeLeverActivity2) getActivity()).refreshOnCreateOrder();
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startCreateOrder(pwString);
            }

        });
    }


    PopupWindow pop;
    RecyclerView rvMultiNumList;

    private void showPopupMenu(View parent) {
        if (pop == null) {
            View view = InflaterUtils.inflateView(getActivity(), R.layout.lever_menu);

            rvMultiNumList = view.findViewById(R.id.rvMultiNumList);

            multiNumAdapter = new MultiNumAdapter(getActivity());
            rvMultiNumList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvMultiNumList.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 0, 0, 0, 0));
            rvMultiNumList.setAdapter(multiNumAdapter);
            multiNumAdapter.setData(multiNumList);
            multiNumAdapter.setSelected(leverMultiple);
            multiNumAdapter.setOnItemclickListen(new MultiNumAdapter.onItemclickListen() {
                @Override
                public void onItemclick(String multnum) {
                    leverMultiple = multnum;
                    tvMultNum.setText(leverMultiple + "X");
                    startCheckOut();
                    dissPop();
                }
            });
            pop = new PopupWindow(view, parent.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);//
            pop.setOutsideTouchable(false);
            pop.setFocusable(false);//
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            pop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

        }
        if (pop != null && !pop.isShowing()) {
            pop.showAsDropDown(parent, 0, 5);
        }
    }

    private void dissPop() {
        if (pop != null & pop.isShowing()) {
            pop.dismiss();
        }
    }


    PopupWindow orderTypePop;

    TextView tvMarketPrice, tvLimitPrice;

    private void showOrderTypePopupMenu(View parent) {
        if (orderTypePop == null) {
            View view = InflaterUtils.inflateView(getActivity(), R.layout.lever_order_type_menu);

            tvMarketPrice = view.findViewById(R.id.tvMarketPrice);
            tvLimitPrice = view.findViewById(R.id.tvLimitPrice);
            tvMarketPrice.setOnClickListener(this);
            tvLimitPrice.setOnClickListener(this);
            tvMarketPrice.setSelected(true);

            orderTypePop = new PopupWindow(view, parent.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);//
            orderTypePop.setOutsideTouchable(false);
            orderTypePop.setFocusable(false);//
            orderTypePop.setOutsideTouchable(true);
            orderTypePop.setFocusable(true);
            orderTypePop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
            orderTypePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

        }
        if (orderTypePop != null && !orderTypePop.isShowing()) {
            orderTypePop.showAsDropDown(parent, 0, 5);
        }
    }

    private void dissOrderTypePop() {
        if (orderTypePop != null & orderTypePop.isShowing()) {
            orderTypePop.dismiss();
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startPrybarConfig();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private String orderType = "market";//limit：限价，market：市价

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuy:
                if (orderType.equals("limit") && TextUtils.isEmpty(getPrice())) {//如果是限价，但是还没有输入价格就不刷新
                    CommonUtil.showmessage(getActivity().getResources().getString(R.string.qingshurujiage), getActivity());
                    return;
                }
                startCreateOrder("");
//                showSubmitTipsDialog();
                break;
            case R.id.tvSell:
                if (orderType.equals("limit") && TextUtils.isEmpty(getPrice())) {//如果是限价，但是还没有输入价格就不刷新
                    CommonUtil.showmessage(getActivity().getResources().getString(R.string.qingshurujiage), getActivity());
                    return;
                }
//                if (d_hand>d_availableAmount){
//                    CommonUtil.showmessage(symbol+getActivity().getResources().getString(R.string.shuliangbuzu), getActivity());
//                    return;
//                }
                startCreateOrder("");
//                showSubmitTipsDialog();
                break;
            case R.id.llMenu:
                showPopupMenu(llMenu);
                break;
            case R.id.llOrderType:
                showOrderTypePopupMenu(llOrderType);
                break;
            case R.id.tvMarketPrice:
                tvMarketPrice.setSelected(true);
                tvLimitPrice.setSelected(false);
                switchMarketPrice();
                startCheckOut();
                dissOrderTypePop();
                break;
            case R.id.tvLimitPrice:
                tvMarketPrice.setSelected(false);
                tvLimitPrice.setSelected(true);
                switchLimitPrice();
                startCheckOut();
                dissOrderTypePop();
                break;
            case R.id.tvPlus:
                plusOrMinus(true);
                break;
            case R.id.tvMinus:
                plusOrMinus(false);
                break;
            case R.id.tvTransfer:
                PageJumpUtil.pageJump(getActivity(), TransferCoinActivity.class);
                break;

        }
    }

    private void plusOrMinus(boolean isPlus) {
        String price = etPrice.getText().toString().trim();
        if (TextUtils.isEmpty(price)) {
            return;
        }
        double p = Double.parseDouble(price);
        double unit = 1 / Math.pow(10, priceDecimals);
        if (isPlus) {
            p += unit;
        } else {
            p -= unit;
        }
        String ret = StockChartUtil.formatNumber(priceDecimals, p);
        etPrice.setText(ret);
        etPrice.setSelection(etPrice.getText().length());
    }


    private void switchMarketPrice() {
        tvMarketHint.setVisibility(View.VISIBLE);
        llLimitHint.setVisibility(View.GONE);
        orderType = "market";
        tvOrderType.setText(getActivity().getResources().getString(R.string.shijiaweituo));
    }

    private void switchLimitPrice() {
        tvMarketHint.setVisibility(View.GONE);
        llLimitHint.setVisibility(View.VISIBLE);
        orderType = "limit";
        tvOrderType.setText(getActivity().getResources().getString(R.string.xianjiaweituo));
    }

    ValueAnimator textSizeAnimator;

    public void onClickPrice(String price) {
        if (etPrice != null) {
            etPrice.setText(price);
            etPrice.setSelection(etPrice.getText().length());
            if (textSizeAnimator == null) {
                textSizeAnimator = ValueAnimator.ofFloat(14f, 15f, 16f, 17f, 18f, 17f, 16f, 15f, 14f);
                textSizeAnimator.setDuration(400);
                textSizeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                textSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    public void onAnimationUpdate(ValueAnimator animation) {
                        etPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, (Float) animation.getAnimatedValue());
                    }
                });
            }
            textSizeAnimator.start();
        }
    }

    private void showSubmitTipsDialog() {
        buyOrSellTipsDialog = new BuyOrSellTipsDialog(getActivity(), openBail, hand, leverMultiple, buySell) {
            @Override
            public void onclickOk() {
                startCreateOrder("");
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        buyOrSellTipsDialog.show();
    }


//

}

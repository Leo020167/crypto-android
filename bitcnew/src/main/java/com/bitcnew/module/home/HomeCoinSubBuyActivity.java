package com.bitcnew.module.home;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.trade.TradeActivity;
import com.bitcnew.module.kbt.adapter.KbtNoticeAdapter;
import com.bitcnew.module.kbt.dialog.KbtBuyBackDialogFragment;
import com.bitcnew.module.kbt.entity.KbtNotice;
import com.bitcnew.module.kbt.entity.KbtPool;
import com.bitcnew.module.kbt.entity.KbtTrend;
import com.bitcnew.module.login.LoginActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.StockChartUtil;
import com.bitcnew.util.VeDate;
import com.bitcnew.widgets.LoadMoreRecycleView;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.bitcnew.widgets.piechart.CardHolderChartView2;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 认购
 */

public class HomeCoinSubBuyActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvPriceCash)
    TextView tvPriceCash;
    @BindView(R.id.tvTotalAmount)
    TextView tvTotalAmount;
    @BindView(R.id.tvTotalAmountText)
    TextView tvTotalAmountText;

//    @BindView(R.id.tvMyAmount)
//    TextView tvMyAmount;
//    @BindView(R.id.tvMyAmountText)
//    TextView tvMyAmountText;

    @BindView(R.id.tvPbText)
    TextView tvPbText;
    @BindView(R.id.pb)
    ProgressBar pb;

    @BindView(R.id.tvHistory)
    TextView tvHistory;
    @BindView(R.id.cardHolderChartView)
    CardHolderChartView2 cardHolderChartView;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.tvBuyBack)
    TextView tvBuyBack;
    @BindView(R.id.tvAssignment)
    TextView tvAssignment;

    @BindView(R.id.tvTimeHint)
    TextView tvTimeHint;
    @BindView(R.id.tvTime)
    TextView tvTime;


    private String repoAmount;//剩余可回购KBT数量
    private String holdAmount;//持有KBT数量
    private String myEquityLevel;//权限等级
    private String myEquityTip;//
    private String subUrl;//

    private long countDownTimestamp = 0;
    private String timeTips = "";
    private String btnText = "";
    private boolean isOpenBuy;
    private boolean isOpenTrade;


//    private String kbtDescribe = "";//kbt说明

    private KbtPool kbtPool;

    private int pageNo = 1;
    private int pageSize = 15;


    private Call<ResponseBody> getKbtRepoHomeCall;
    private Call<ResponseBody> getKbtNoticeCall;

    private Call<ResponseBody> kbtRepoCall;

    private KbtNoticeAdapter kbtNoticeAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.kbt_buy_back;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.rengou);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_cropyme);
        ButterKnife.bind(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 15, 15));
        kbtNoticeAdapter = new KbtNoticeAdapter(this);
        rvList.setAdapter(kbtNoticeAdapter);
        kbtNoticeAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        rvList.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        tvBuyBack.setOnClickListener(this);
        tvAssignment.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        startGetKbtRepoHome();
        pageNo = 1;
        startGetKbtNoticeCall();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack loadMoreCallBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (kbtNoticeAdapter != null && kbtNoticeAdapter.getRealItemCount() > 0) {
                startGetKbtNoticeCall();
            } else {
                pageNo = 1;
                startGetKbtNoticeCall();
            }
        }
    };

    private void startGetKbtNoticeCall() {
        com.bitcnew.http.util.CommonUtil.cancelCall(getKbtNoticeCall);
        getKbtNoticeCall = VHttpServiceManager.getInstance().getVService().getCoinNotice(pageNo);
        getKbtNoticeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<KbtNotice> group = null;
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<KbtNotice>>() {
                    }.getType());
                    Log.d("HomeCoinSubBuyFragment", "group==" + (group == null ? "null" : group.size()));
                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            kbtNoticeAdapter.setGroup(group);
                        } else {
                            kbtNoticeAdapter.addItem(group);
                            kbtNoticeAdapter.notifyDataSetChanged();
                        }
                    }
                    pageNo++;
                }
                kbtNoticeAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                kbtNoticeAdapter.onLoadComplete(false, false);
            }
        });
    }


    KbtBuyBackDialogFragment kbtBuyBackDialogFragment;

    private void showKbtBuyBackDialogFragment() {
        kbtBuyBackDialogFragment = KbtBuyBackDialogFragment.newInstance(kbtPool, holdAmount, myEquityLevel, myEquityTip, subUrl);
        kbtBuyBackDialogFragment.setOnShareDialogCallBack(new KbtBuyBackDialogFragment.OnShareDialogCallBack() {
            @Override
            public void onDialogDismiss() {
                kbtBuyBackDialogFragment.dismiss();
            }

            @Override
            public void onSubmit(String amount) {
                startKbtRepoCall(amount);
            }
        });
        kbtBuyBackDialogFragment.showDialog(getSupportFragmentManager(), "");
    }

    private void startKbtRepoCall(String amount) {
        if (kbtPool == null) return;
        com.bitcnew.http.util.CommonUtil.cancelCall(kbtRepoCall);
        showProgressDialog();
        kbtRepoCall = VHttpServiceManager.getInstance().getVService().repo(kbtPool.subId, kbtPool.symbol, amount);
        kbtRepoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, HomeCoinSubBuyActivity.this);
                    if (kbtBuyBackDialogFragment != null) {
                        kbtBuyBackDialogFragment.dismiss();
                    }
                    startGetKbtRepoHome();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    private void startGetKbtRepoHome() {
        com.bitcnew.http.util.CommonUtil.cancelCall(getKbtRepoHomeCall);
        getKbtRepoHomeCall = VHttpServiceManager.getInstance().getVService().repoHome();
        getKbtRepoHomeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    repoAmount = resultData.getItem("repoAmount", String.class);
                    holdAmount = resultData.getItem("holdAmount", String.class);

                    myEquityLevel = resultData.getItem("myEquityLevel", String.class);
                    myEquityTip = resultData.getItem("myEquityTip", String.class);
                    subUrl = resultData.getItem("subUrl", String.class);

                    countDownTimestamp = resultData.getItem("countDownTimestamp", Long.class);
                    btnText = resultData.getItem("btnText", String.class);
                    timeTips = resultData.getItem("timeTips", String.class);


                    isOpenBuy = resultData.getItem("isOpenBuy", Boolean.class);
                    isOpenTrade = resultData.getItem("isOpenTrade", Boolean.class);

//                    String coinSubState = resultData.getItem("coinSubState", String.class);
//                    if (getActivity() != null && getActivity() instanceof HomeActivity) {
//                        ((HomeActivity) getActivity()).setShowCoin(coinSubState);
//                    }

                    startCountDownTime();
                    tvBuyBack.setText(btnText);
                    tvTimeHint.setText(timeTips + ": ");


//                    kbtDescribe = resultData.getItem("kbtDescribe", String.class);


                    Group<KbtTrend> group = resultData.getGroup("kbtTrendList", new TypeToken<Group<KbtTrend>>() {
                    }.getType());

                    kbtPool = resultData.getObject("coinSubBuy", KbtPool.class);

                    if (group != null && group.size() > 0) {
                        cardHolderChartView.setDataForKbt(group);
                    }

                    if (kbtPool != null) {

                        tvSymbol.setText(TextUtils.isEmpty(kbtPool.symbol) ? getResources().getString(R.string.zanwu) : kbtPool.symbol);
                        tvPrice.setText(kbtPool.price);
                        tvPriceCash.setText(kbtPool.priceCny);
                        tvTotalAmount.setText(kbtPool.totalAmount);
                        tvTotalAmountText.setText(getResources().getString(R.string.benlunrengou) + (TextUtils.isEmpty(kbtPool.symbol) ? "" : kbtPool.symbol) + getResources().getString(R.string.zongshuliang));

//                        tvMyAmount.setText(kbtPool.mySubAmount);
//                        tvMyAmountText.setText("我累计认购" + kbtPool.symbol + "数量");

                        if (Double.parseDouble(kbtPool.totalAmount) > 0) {
                            double p = Double.parseDouble(kbtPool.produceAmount) / (int) Double.parseDouble(kbtPool.totalAmount) * 100;
                            tvPbText.setText(getResources().getString(R.string.benlunrengoujindu) + StockChartUtil.formatNumber(2, p) + "%");
                            pb.setMax((int) Double.parseDouble(kbtPool.totalAmount));
                            pb.setProgress((int) Double.parseDouble(kbtPool.produceAmount));
                        }
                    }

                    if (kbtBuyBackDialogFragment != null)//这里主要更新USDT数量，避免充值完回来没有变化
                        kbtBuyBackDialogFragment.updateUSDT(holdAmount);


                }
            }
        });
    }

    private CountDownTimer timer;

    private void startCountDownTime() {
//        Long ss = VeDate.strLongToDate(String.valueOf(orderCash.expireTime)).getTime() - VeDate.strLongToDate(String.valueOf(orderCash.createTime)).getTime();
        Long ss = countDownTimestamp - System.currentTimeMillis() / 1000;
        Log.d("startCountDownTime", "countDownTimestamp==" + countDownTimestamp);
        Log.d("startCountDownTime", "System.currentTimeMillis()==" + System.currentTimeMillis());
        Log.d("startCountDownTime", "ss==" + ss);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (ss > 0) {
//            llTime.setVisibility(View.VISIBLE);
            timer = new CountDownTimer(ss * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] time = VeDate.formatSecToTime(millisUntilFinished / 1000);
//                    Log.d("startCountDownTime", "time0==" + time[0]+"time1==" + time[1]+"time2==" + time[2]+"time3==" + time[3]);
                    if (time != null && time.length == 4) {
                        tvTime.setText(time[0] + getResources().getString(R.string.tian) + time[1] + getResources().getString(R.string.shi) + time[2] + getResources().getString(R.string.fen) + time[3] + getResources().getString(R.string.miao));
                    }
                }

                public void onFinish() {
                    tvTime.setText(getResources().getString(R.string.lingtianlingshi));
                }
            };
            timer.start();
        } else {
            tvTime.setText(getResources().getString(R.string.lingtianlingshi));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuyBack:
                if (isLogin()) {
                    if (kbtPool == null) return;
                    if (!isOpenBuy) {
                        com.bitcnew.http.util.CommonUtil.showmessage(getResources().getString(R.string.zanweikaifang), this);
                        return;
                    }
                    if (TextUtils.isEmpty(kbtPool.symbol) && kbtPool.subId == 0) {
                        com.bitcnew.http.util.CommonUtil.showmessage(getResources().getString(R.string.dangqianzanwurengou), this);
                        return;
                    }
                    showKbtBuyBackDialogFragment();
                } else {
                    LoginActivity.login((TJRBaseToolBarActivity) this);
                }
                break;
//            case R.id.tvKbtRules:
//                if (!TextUtils.isEmpty(kbtDescribe)) {
//                    CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), kbtDescribe);
//                }
//                break;
            case R.id.tvAssignment:
                if (!isOpenTrade) {
                    com.bitcnew.http.util.CommonUtil.showmessage(getResources().getString(R.string.zanweikaifang), this);
                    return;
                }
                if (kbtPool != null)
                    TradeActivity.pageJump(this, kbtPool.symbol, -1);
                break;


        }

    }


}

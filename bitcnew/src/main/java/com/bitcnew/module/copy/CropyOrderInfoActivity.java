package com.bitcnew.module.copy;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.module.home.fragment.ShareDialogFragment;
import com.bitcnew.module.myhome.UserHomeActivity;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.data.sharedpreferences.NormalShareData;
import com.bitcnew.http.model.User;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.copy.adapter.CopyPieChartAdapter;
import com.bitcnew.module.copy.adapter.CopyPositionAdapter;
import com.bitcnew.module.copy.dialog.CopyOrderDialogFragment;
import com.bitcnew.module.copy.dialog.CopyOrderSettingDialogFragment;
import com.bitcnew.module.copy.entity.CopyOrderDetail;
import com.bitcnew.social.util.AvoidMultiClick;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.StockChartUtil;
import com.bitcnew.widgets.CircleImageView;
import com.bitcnew.widgets.PieChartView;
import com.bitcnew.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 产品详情
 */

public class CropyOrderInfoActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.llShare)
    LinearLayout llShare;
    @BindView(R.id.pieChartView)
    PieChartView pieChartView;
    @BindView(R.id.flCopySetting)
    FrameLayout flCopySetting;
    @BindView(R.id.tvCopySetting)
    TextView tvCopySetting;
    @BindView(R.id.tvCopyOrderBalance)
    TextView tvCopyOrderBalance;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvProfitBalance)
    TextView tvProfitBalance;
    @BindView(R.id.tvProfitMark)
    TextView tvProfitMark;
    @BindView(R.id.tvTradeHistory)
    TextView tvTradeHistory;
    @BindView(R.id.tvNoHold)
    TextView tvNoHold;
    @BindView(R.id.rvPositionList)
    RecyclerView rvPositionList;
    @BindView(R.id.tvOrderState)
    TextView tvOrderState;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.tvStopOrder)
    TextView tvStopOrder;
    @BindView(R.id.tvAppendBalance)
    TextView tvAppendBalance;
    @BindView(R.id.rvPieChartList)
    RecyclerView rvPieChartList;
    @BindView(R.id.llBottomBtn)
    LinearLayout llBottomBtn;
    @BindView(R.id.ivhead)
    CircleImageView ivhead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvGrade)
    TextView tvGrade;
    @BindView(R.id.ivRating1)
    AppCompatImageView ivRating1;
    @BindView(R.id.ivRating2)
    AppCompatImageView ivRating2;
    @BindView(R.id.ivRating3)
    AppCompatImageView ivRating3;
    @BindView(R.id.ivRating4)
    AppCompatImageView ivRating4;
    @BindView(R.id.ivRating5)
    AppCompatImageView ivRating5;
    @BindView(R.id.tvMaxAmount)
    TextView tvMaxAmount;
    @BindView(R.id.sbStopWin)
    TextView sbStopWin;
    @BindView(R.id.sbStopLoss)
    TextView sbStopLoss;
    @BindView(R.id.newPoint)
    View newPoint;
    @BindView(R.id.cbSign)
    CheckBox cbSign;
    @BindView(R.id.ivQuestionMark)
    ImageView ivQuestionMark;

    private CopyPieChartAdapter copyPieChartAdapter;
    private CopyPositionAdapter copyPositionAdapter;


    private Call<ResponseBody> copyOrderDetailCall;
    private Call<ResponseBody> getTradeConfigCall;
    private Call<ResponseBody> copyCloseorderCall;
    private Call<ResponseBody> copyAppendOrderCashCall;
    private Call<ResponseBody> closeOrderTipsCall;
    private Call<ResponseBody> copyUpdateOptionCall;


    private CopyOrderDialogFragment copyOrderDialogFragment;//追加金额
    private CopyOrderSettingDialogFragment copyOrderSettingDialogFragment;//跟单设置
    private TjrBaseDialog stopOrderDialog;//停止跟单提示

    private CopyOrderDetail copyOrderDetail;

    private String holdUsdt = "0.00";//持有USDT
    private double usdtRate = 0.00;//usdt市价


    private String minMarketBalance = "";

    private Onclick onclick;

    private long orderId;

    private TjrImageLoaderUtil tjrImageLoaderUtil;

    public static void pageJump(Context context, long orderId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERID, orderId);
        PageJumpUtil.pageJumpToData(context, CropyOrderInfoActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.cropy_order_info;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.gendanxiangqing);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
        Bundle b = null;
        if ((b = getIntent().getExtras()) != null) {
            if (b.containsKey(CommonConst.ORDERID)) {
                orderId = b.getLong(CommonConst.ORDERID, 0);
            } else {
                CommonUtil.showmessage("参数错误", this);
                finish();
            }
        }
        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();

        tjrImageLoaderUtil = new TjrImageLoaderUtil();

        copyPieChartAdapter = new CopyPieChartAdapter(this);
        rvPieChartList.setLayoutManager(new LinearLayoutManager(this));
        rvPieChartList.setAdapter(copyPieChartAdapter);


        copyPositionAdapter = new CopyPositionAdapter(this);
        rvPositionList.setLayoutManager(new LinearLayoutManager(this));
        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(this, 15, 15, ContextCompat.getColor(this, R.color.dividerColor));
        simpleRecycleDivider.setShowLastDivider(true);
        rvPositionList.addItemDecoration(simpleRecycleDivider);
        rvPositionList.setAdapter(copyPositionAdapter);

        onclick = new Onclick();
        tvCopySetting.setOnClickListener(onclick);
        tvAppendBalance.setOnClickListener(onclick);
        tvStopOrder.setOnClickListener(onclick);
        tvTradeHistory.setOnClickListener(onclick);
        llShare.setOnClickListener(onclick);
        tvProfitMark.setOnClickListener(onclick);

        cbSign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getUserIdLong() > 0) {
                    NormalShareData.saveIsHideSmall2(CropyOrderInfoActivity.this, getUserIdLong(), isChecked);
                }
                if (copyPositionAdapter != null && copyOrderDetail != null)
                    copyPositionAdapter.setGroupIsHide(copyOrderDetail.holdList, isChecked);
            }
        });
        ivQuestionMark.setOnClickListener(onclick);
        if (getUserIdLong() > 0)
            cbSign.setChecked(NormalShareData.getIsHideSmallFlag2(CropyOrderInfoActivity.this, getUserIdLong()));

        startCopyOrderDetail();
//        startGetTradeConfig();


    }


    public void setStopWinFlag() {
        if (getUserIdLong() > 0) {
            int editFlag = NormalShareData.getStopWinFlag(this, getUserIdLong());
            if (editFlag == 0) {
                newPoint.setVisibility(View.VISIBLE);
            } else {
                newPoint.setVisibility(View.GONE);
            }
        }
    }

    private class Onclick extends AvoidMultiClick {
        @Override
        public void click(View v) {
            switch (v.getId()) {
                case R.id.tvCopySetting://修改dialog
                    if (copyOrderDetail == null) return;
                    showCopyOrderSettingDialogFragment();
                    NormalShareData.saveStopWinFlag(CropyOrderInfoActivity.this, getUserIdLong(), 1);
                    newPoint.setVisibility(View.GONE);
                    break;
                case R.id.tvStopOrder:
                    startCloseOrderTips();
//                    showStopOrderDialog();
                    break;
                case R.id.tvAppendBalance:
                    if (copyOrderDetail == null) return;
                    showCopyOrderDialogFragment();
                    break;
                case R.id.tvTradeHistory:
                    CropyOrderDetailListActivity.pageJump(CropyOrderInfoActivity.this, orderId);
                    break;
                case R.id.llShare:
                    if (copyOrderDetail == null) return;
                    if (TextUtils.isEmpty(shareUrl)) {
                        startGetShareCall();
                    } else {
                        showShareDialogFragment();
                    }
                    break;
                case R.id.tvProfitMark:
                    showSubmitTipsDialog();
                    break;
                case R.id.ivhead:
                    if (copyOrderDetail != null) {
                        UserHomeActivity.pageJump(CropyOrderInfoActivity.this, copyOrderDetail.copyUid);
                    }
                    break;
                case R.id.ivQuestionMark:
                    if (!minMarketBalance.equals("0")) showSubmitTipsDialog2(minMarketBalance);
                    break;

            }
        }
    }


    TjrBaseDialog questionMarkDialog;

    private void showSubmitTipsDialog() {
        questionMarkDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        questionMarkDialog.setTitleVisibility(View.GONE);
        questionMarkDialog.setBtnColseVisibility(View.GONE);
        questionMarkDialog.setMessage(getResources().getString(R.string.yikouchugendanfenchengbufen));
        questionMarkDialog.setBtnOkText(getResources().getString(R.string.zhidaole));
        questionMarkDialog.show();
    }


    TjrBaseDialog questionMarkDialog2;

    private void showSubmitTipsDialog2(String tips) {
        questionMarkDialog2 = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        questionMarkDialog2.setTitleVisibility(View.GONE);
        questionMarkDialog2.setBtnColseVisibility(View.GONE);
        questionMarkDialog2.setMessage(getResources().getString(R.string.shizhixiaoyu) + tips + getResources().getString(R.string.usdtdebizhong));
        questionMarkDialog2.setBtnOkText(getResources().getString(R.string.zhidaole));
        questionMarkDialog2.show();
    }


    ShareDialogFragment shareDialogFragment;

    private void showShareDialogFragment() {
        shareDialogFragment = ShareDialogFragment.newInstance(new User(copyOrderDetail.copyUid, copyOrderDetail.copyName, copyOrderDetail.copyHeadUrl), shareUrl);
        shareDialogFragment.setOnShareDialogCallBack(new ShareDialogFragment.OnShareDialogCallBack() {
            @Override
            public void onDialogDismiss() {
                shareDialogFragment.dismiss();
            }
        });
        shareDialogFragment.showDialog(getSupportFragmentManager(), "");
    }

    private Call<ResponseBody> getShareCall;
    private String shareUrl;

    private void startGetShareCall() {
        CommonUtil.cancelCall(getShareCall);
        getShareCall = VHttpServiceManager.getInstance().getVService().getshareinfo(1, "");
        getShareCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    shareUrl = resultData.getItem("shareUrl", String.class);
                    showShareDialogFragment();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    private void showStopOrderDialog(String tips) {
        stopOrderDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startCopyCloseorder();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        stopOrderDialog.setTvTitle(getResources().getString(R.string.wenxintishi));
        stopOrderDialog.setMessage(com.bitcnew.util.CommonUtil.fromHtml(tips));
        stopOrderDialog.setBtnOkText(getResources().getString(R.string.queding));
        stopOrderDialog.show();
    }


    private void setData() {
        if (copyOrderDetail != null) {
            setUserInfo();
            setStopWin();

            tvProfitBalance.setText(StockChartUtil.formatNumWithSign(4, Double.parseDouble(copyOrderDetail.profitCash), true));
            tvProfitBalance.setTextColor(StockChartUtil.getRateTextColor(this, Double.parseDouble(copyOrderDetail.profitCash)));
            tvCopyOrderBalance.setText(copyOrderDetail.tolBalance);
            tvBalance.setText(copyOrderDetail.balance);


            if (copyOrderDetail.holdDistribute != null && copyOrderDetail.holdDistribute.size() > 0) {
                float[] data = new float[copyOrderDetail.holdDistribute.size()];
                for (int i = 0, m = copyOrderDetail.holdDistribute.size(); i < m; i++) {
                    data[i] = copyOrderDetail.holdDistribute.get(i).rate;
                }
                Log.d("CropyOrderInfo", "data==" + data.toString());
                pieChartView.setData(data);
                pieChartView.startAnimation(1500);

                copyPieChartAdapter.setGroup(copyOrderDetail.holdDistribute);

            }
            if (copyOrderDetail.holdList != null && copyOrderDetail.holdList.size() > 0) {
                tvNoHold.setVisibility(View.GONE);
                rvPositionList.setVisibility(View.VISIBLE);
                copyPositionAdapter.setGroupIsHide(copyOrderDetail.holdList,cbSign.isChecked());
            } else {
                tvNoHold.setVisibility(View.VISIBLE);
                rvPositionList.setVisibility(View.GONE);
            }
            setBtnState();

        }
    }

    private void setUserInfo() {
        if (copyOrderDetail == null) return;
        tvName.setText(copyOrderDetail.copyName);
        tvId.setText("ID: " + String.valueOf(copyOrderDetail.copyUid));
        tjrImageLoaderUtil.displayImageForHead(copyOrderDetail.copyHeadUrl, ivhead);
        ivhead.setOnClickListener(onclick);
        tvGrade.setText(copyOrderDetail.score);
        setRatingBarData(copyOrderDetail.score);

    }

    private void setStopWin() {
        tvMaxAmount.setText(String.valueOf(copyOrderDetail.maxCopyBalance) + " USDT");
        sbStopWin.setText(copyOrderDetail.stopWin == 0 ? getResources().getString(R.string.weishezhi) : (int) (copyOrderDetail.stopWin * 100) + "%");
        sbStopLoss.setText(copyOrderDetail.stopLoss == 0 ? getResources().getString(R.string.weishezhi) : (int) (copyOrderDetail.stopLoss * 100) + "%");
    }

    private void setRatingBarData(String score) {
        int rating = (int) (Double.parseDouble(score) / 20);
        if (rating == 0) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_off);
        } else if (rating == 1) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_off);
        } else if (rating == 2) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_off);
        } else if (rating == 3) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_off);
        } else if (rating == 4) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_off);
        } else if (rating == 5) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_on);
        }
    }

    private void setBtnState() {
        if (copyOrderDetail == null) return;
        if (copyOrderDetail.isDone == 0) {
            llBottomBtn.setVisibility(View.VISIBLE);
//            tvStopOrder.setEnabled(true);
//            tvAppendBalance.setEnabled(true);
            flCopySetting.setVisibility(View.VISIBLE);
            tvOrderState.setVisibility(View.GONE);
            setStopWinFlag();
        } else {
            if (copyOrderDetail.isDone == 1) {
                tvOrderState.setText(getResources().getString(R.string.zhengzaitingzhigendan)+"..." + copyOrderDetail.doneDegree + "%");
            } else {
                tvOrderState.setText(getResources().getString(R.string.yitingzhigendan));
            }
            llBottomBtn.setVisibility(View.GONE);
//            tvStopOrder.setEnabled(false);
//            tvAppendBalance.setEnabled(false);
            flCopySetting.setVisibility(View.GONE);
            tvOrderState.setVisibility(View.VISIBLE);
        }
    }


    private void showCopyOrderDialogFragment() {

        copyOrderDialogFragment = CopyOrderDialogFragment.newInstance(copyOrderDetail.copyUid, 1);
        copyOrderDialogFragment.setOnShareDialogCallBack(new CopyOrderDialogFragment.OnShareDialogCallBack() {
            @Override
            public void onDialogDismiss() {
                copyOrderDialogFragment.dismiss();
            }

            @Override
            public void onSubmit(double cash) {
                startCopyAppendOrderCash(cash);
            }
        });
        copyOrderDialogFragment.showDialog(getSupportFragmentManager(), "");
    }


    private void showCopyOrderSettingDialogFragment() {

        copyOrderSettingDialogFragment = CopyOrderSettingDialogFragment.newInstance(copyOrderDetail.stopWin, copyOrderDetail.stopLoss, copyOrderDetail.maxCopyBalance);
        copyOrderSettingDialogFragment.setOnSettingDialogCallBack(new CopyOrderSettingDialogFragment.OnSettingDialogCallBack() {

            @Override
            public void onSubmit(double atMaxCash, double stopWin, double stopLoss) {

                if (atMaxCash > Double.parseDouble(copyOrderDetail.tolBalance)) {
                    com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.zuidajinebunengdayugendanzongjine), CropyOrderInfoActivity.this);
                    return;
                }
                startUpdateStopTips(atMaxCash,stopWin,stopLoss);
//                startCopyUpdateOption(atMaxCash, stopWin, stopLoss);
            }
        });
        copyOrderSettingDialogFragment.showDialog(getSupportFragmentManager(), "");
    }


    private void startCopyAppendOrderCash(double cash) {
        CommonUtil.cancelCall(copyAppendOrderCashCall);
        copyAppendOrderCashCall = VHttpServiceManager.getInstance().getVService().copyAppendOrderCash(orderId, String.valueOf(cash));
        copyAppendOrderCashCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, CropyOrderInfoActivity.this);
                    if (copyOrderDialogFragment != null) copyOrderDialogFragment.dismiss();
                    startCopyOrderDetail();
//                    startGetTradeConfig();
                }
            }
        });
    }


//    private void startGetTradeConfig() {
//        CommonUtil.cancelCall(getTradeConfigCall);
//        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeConfig("");
//        getTradeConfigCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    holdUsdt = resultData.getItem("holdUsdt", String.class);
//                    usdtRate = resultData.getItem("usdtRate", Double.class);
//                }
//            }
//        });
//    }

    private void startUpdateStopTips(final double atMaxCash, final double stopWin, final double stopLoss) {
        CommonUtil.cancelCall(copyUpdateOptionCall);
        copyUpdateOptionCall = VHttpServiceManager.getInstance().getVService().updateStopTips(orderId, stopWin, stopLoss);
        copyUpdateOptionCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String msg = resultData.getItem("msg", String.class);
                    Log.d("startUpdateStopTips", "msg==" + msg);
                    if (!TextUtils.isEmpty(msg)) {
                        showUpdateTipsDialog(msg, atMaxCash, stopWin, stopLoss);
                    } else {
                        startCopyUpdateOption(atMaxCash, stopWin, stopLoss);
                    }
                }
            }
        });
    }


    TjrBaseDialog updateTipsDialog;

    private void showUpdateTipsDialog(String msg, final double atMaxCash, final double stopWin, final double stopLoss) {
        updateTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startCopyUpdateOption(atMaxCash, stopWin, stopLoss);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        updateTipsDialog.setMessage(msg);
        updateTipsDialog.setBtnOkText(getResources().getString(R.string.queding));
        updateTipsDialog.setBtnColseText(getResources().getString(R.string.quxiao));
        updateTipsDialog.setTitleVisibility(View.GONE);
        updateTipsDialog.show();
    }


    private void startCopyUpdateOption(final double atMaxCash, final double stopWin, final double stopLoss) {
        CommonUtil.cancelCall(copyUpdateOptionCall);
        copyUpdateOptionCall = VHttpServiceManager.getInstance().getVService().copyUpdateOption(orderId, String.valueOf(atMaxCash), stopWin, stopLoss);
        copyUpdateOptionCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (copyOrderDetail != null) {
                        copyOrderDetail.stopWin = stopWin;
                        copyOrderDetail.stopLoss = stopLoss;
                        copyOrderDetail.maxCopyBalance = atMaxCash;

                        Log.d("startCopyUpdateOption", "stopWin==" + stopWin + "  stopLoss==" + stopLoss + "  atMaxCash==" + atMaxCash);

                        setStopWin();
                    }
                    CommonUtil.showmessage(resultData.msg, CropyOrderInfoActivity.this);
                    if (copyOrderSettingDialogFragment != null) {
                        copyOrderSettingDialogFragment.dismiss();
                    }
//                    startCopyOrderDetail();//无需刷新
                }
            }
        });
    }

    private void startCloseOrderTips() {
        CommonUtil.cancelCall(closeOrderTipsCall);
        closeOrderTipsCall = VHttpServiceManager.getInstance().getVService().closeOrderTips(orderId);
        closeOrderTipsCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String tips = resultData.getItem("tips", String.class);
                    showStopOrderDialog(tips);
                }
            }
        });
    }


    private void startCopyCloseorder() {
        CommonUtil.cancelCall(copyCloseorderCall);
        copyCloseorderCall = VHttpServiceManager.getInstance().getVService().copyCloseorder(orderId);
        copyCloseorderCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.getItem("tips", String.class), CropyOrderInfoActivity.this);
                    //重新刷新
                    if (copyOrderDetail != null) {
                        copyOrderDetail.isDone = 1;
                        setBtnState();
                    }
                    startCopyOrderDetail();
                }
            }
        });
    }

    private void startCopyOrderDetail() {
        CommonUtil.cancelCall(copyOrderDetailCall);
        copyOrderDetailCall = VHttpServiceManager.getInstance().getVService().copyOrderDetail(orderId);
        copyOrderDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    minMarketBalance = resultData.getItem("minMarketBalance", String.class);

                    copyOrderDetail = resultData.getObject("orderDetail", CopyOrderDetail.class);
                    if (copyOrderDetail != null) {
                        setData();
                    }
                }
            }
        });
    }


}

package com.bitcnew.module.home.trade.history;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.entity.OrderStateEnum;
import com.bitcnew.module.home.trade.entity.TakeCoinHistory;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.DateUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提币充币详情
 */
public class TakeCoinHistoryDetailsActivity extends TJRBaseToolBarSwipeBackActivity {


    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvAccountType)
    TextView tvAccountType;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.tvTakeCoinAddress)
    TextView tvTakeCoinAddress;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvCancel)
    TextView tvCancel;

    private TakeCoinHistory takeCoinHistory;

    private Call<ResponseBody> tradeCancelCall;

    private TjrBaseDialog cancelTipsDialog;

    public static void pageJump(Context context, TakeCoinHistory takeCoinHistory) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("details", takeCoinHistory);
        PageJumpUtil.pageJump(context, TakeCoinHistoryDetailsActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.take_coin_his_details;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.xiangqing);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("details")) {
                takeCoinHistory = bundle.getParcelable("details");
            }
        }
        if (takeCoinHistory != null) {
            tvAmount.setText(takeCoinHistory.amount + takeCoinHistory.symbol);
            if (takeCoinHistory.inOut == 1) {
                tvAccountType.setText(getResources().getString(R.string.chongbi));
            } else {
                tvAccountType.setText(getResources().getString(R.string.tibi));
            }
            tvFee.setText(takeCoinHistory.fee + takeCoinHistory.symbol);
            tvTakeCoinAddress.setText(takeCoinHistory.address);
            tvState.setText(takeCoinHistory.stateDesc);
            tvTime.setText(DateUtils.getStringDateOfString2(takeCoinHistory.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmm));


            if (takeCoinHistory.state == 0) {
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCancelTipsDialog();
                    }
                });
            } else {
                tvCancel.setVisibility(View.GONE);
            }
        }
    }

    private void showCancelTipsDialog() {
        cancelTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startWithdrawCoinCancel();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        cancelTipsDialog.setMessage(getResources().getString(R.string.quedingchexiaodingdan));
        cancelTipsDialog.setBtnOkText(getResources().getString(R.string.chexiao));
        cancelTipsDialog.setTitleVisibility(View.GONE);
        cancelTipsDialog.show();
    }


    private void startWithdrawCoinCancel() {
        if (takeCoinHistory == null) return;
        CommonUtil.cancelCall(tradeCancelCall);
        tradeCancelCall = VHttpServiceManager.getInstance().getVService().withdrawCoinCancel(takeCoinHistory.dwId);
        tradeCancelCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, TakeCoinHistoryDetailsActivity.this);
                    takeCoinHistory.state = OrderStateEnum.canceled.getState();
                    finish();
                }
            }

        });
    }


}

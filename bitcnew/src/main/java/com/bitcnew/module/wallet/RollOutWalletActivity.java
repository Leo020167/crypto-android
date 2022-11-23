package com.bitcnew.module.wallet;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.entity.Store;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 转出存币宝
 */
public class RollOutWalletActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvAmountSymbol)
    TextView tvAmountSymbol;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.llSymbol)
    LinearLayout llSymbol;
//    @BindView(R.id.cbSign)
//    CheckBox cbSign;
    @BindView(R.id.tvProfitSymbol)
    TextView tvProfitSymbol;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.llProfit)
    LinearLayout llProfit;
//    @BindView(R.id.cbSign2)
//    CheckBox cbSign2;
    @BindView(R.id.tvTargetSymbol)
    TextView tvTargetSymbol;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvCurrSymbol)
    TextView tvCurrSymbol;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvEnableAmount)
    TextView tvEnableAmount;
    @BindView(R.id.tvFrozenAmount)
    TextView tvFrozenAmount;
    @BindView(R.id.tvRollOutWallet)
    TextView tvRollOutWallet;
    @BindView(R.id.ivSeclected1)
    ImageView ivSeclected1;
    @BindView(R.id.ivSeclected2)
    ImageView ivSeclected2;

    private String symbol = "";//币种

    private Call<ResponseBody> getPrybarStoreConfigCall;
    private Call<ResponseBody> getPrybarStoreCreateOutCall;
    private Store store;

    private int type = 0;//0转出币，1转出收益


    @Override
    protected int setLayoutId() {
        return R.layout.roll_out_wallet;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.zhuanchu);
    }

    public static void pageJump(Context context, String symbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        PageJumpUtil.pageJump(context, RollOutWalletActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
//        tvKbtRules.setOnClickListener(this);
//        tvBuyBack.setOnClickListener(this);
//        tvAssignment.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                symbol = bundle.getString(CommonConst.SYMBOL, "");
            }
        }
        mActionBar.setTitle(getResources().getString(R.string.zhuanchu)+ symbol);

        llSymbol.setOnClickListener(this);
        llProfit.setOnClickListener(this);
        tvRollOutWallet.setOnClickListener(this);
        tvAll.setOnClickListener(this);

        ivSeclected1.setVisibility(View.VISIBLE);
        ivSeclected2.setVisibility(View.GONE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetPrybarStoreAssetConfig();

    }


    private void startGetPrybarStoreAssetConfig() {
        CommonUtil.cancelCall(getPrybarStoreConfigCall);
        getPrybarStoreConfigCall = VHttpServiceManager.getInstance().getVService().prybarStoreConfig(symbol, -1);
        getPrybarStoreConfigCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    store = resultData.getObject("storeResult", Store.class);
                    setStore();
                }
            }
        });
    }


    private void setStore() {
        if (store != null) {
            tvAmountSymbol.setText(store.amountSymbol + getResources().getString(R.string.shuliang));
            tvAmount.setText(store.amount);

            tvProfitSymbol.setText(store.profitSymbol + getResources().getString(R.string.shouyi));
            tvProfit.setText(store.profit);

            if (type == 0) {
                tvTargetSymbol.setText(getResources().getString(R.string.zhuanchu) + store.amountSymbol + getResources().getString(R.string.shuliangdaochicang));
                etAmount.setHint(getResources().getString(R.string.qingshuruzhuanchu)+ store.amountSymbol + getResources().getString(R.string.shuliang));
                tvCurrSymbol.setText(store.amountSymbol);
                tvEnableAmount.setText(getResources().getString(R.string.kezhuanchushuliang) + store.holdAmount + " " + store.amountSymbol);
                tvFrozenAmount.setVisibility(View.VISIBLE);
                tvFrozenAmount.setText(getResources().getString(R.string.dongjieshuliang) + store.frozenAmount + " " + store.amountSymbol);

            } else {
                tvTargetSymbol.setText(getResources().getString(R.string.zhuanchu) + store.profitSymbol + getResources().getString(R.string.shuliangdaochicang));
                etAmount.setHint(getResources().getString(R.string.qingshuruzhuanchu)+ store.profitSymbol + getResources().getString(R.string.shuliang));
                tvCurrSymbol.setText(store.profitSymbol);
                tvEnableAmount.setText(getResources().getString(R.string.kezhuanchushuliang) + store.profit + " " + store.profitSymbol);
                tvFrozenAmount.setVisibility(View.INVISIBLE);
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSymbol:
                ivSeclected1.setVisibility(View.VISIBLE);
                ivSeclected2.setVisibility(View.GONE);
                type = 0;
                etAmount.setText("");
                setStore();
                break;
            case R.id.llProfit:
                ivSeclected1.setVisibility(View.GONE);
                ivSeclected2.setVisibility(View.VISIBLE);
                type = 1;
                etAmount.setText("");
                setStore();
                break;
            case R.id.tvAll:
                if (store == null) return;
                if (type == 0) {
                    etAmount.setText(store.holdAmount);
                    etAmount.setSelection(store.holdAmount.length());
                } else {
                    etAmount.setText(store.profit);
                    etAmount.setSelection(store.profit.length());
                }
                break;
            case R.id.tvRollOutWallet:
                if (store == null) return;
                String amountText = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amountText)) {
                    com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.qingshuruzhuanchushuliang), this);
                    return;
                }
                if (type == 0) {
                    if (Double.parseDouble(amountText) > Double.parseDouble(store.holdAmount)) {
                        CommonUtil.showmessage(getResources().getString(R.string.kehzuanchushuliangbuzu), this);
                        return;
                    }
                } else {
                    if (Double.parseDouble(amountText) > Double.parseDouble(store.profit)) {
                        CommonUtil.showmessage(getResources().getString(R.string.kehzuanchushuliangbuzu), this);
                        return;
                    }
                }
                startGetPrybarStoreAssetCreateOut(amountText);
                break;


        }
    }


    private void startGetPrybarStoreAssetCreateOut(String amount) {
        CommonUtil.cancelCall(getPrybarStoreCreateOutCall);
        getPrybarStoreCreateOutCall = VHttpServiceManager.getInstance().getVService().prybarStoreCreateOut(symbol, amount, type);
        getPrybarStoreCreateOutCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    showSuccessDialog(resultData.msg);
                }
            }
        });
    }

    TjrBaseDialog delDialog;

    private void showSuccessDialog(String msg) {
        delDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                PageJumpUtil.finishCurr(RollOutWalletActivity.this);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        delDialog.setMessage(msg);
        delDialog.setBtnOkText(getResources().getString(R.string.queding));
        delDialog.setBtnColseVisibility(View.GONE);
        delDialog.setTitleVisibility(View.GONE);
        delDialog.show();
    }

}

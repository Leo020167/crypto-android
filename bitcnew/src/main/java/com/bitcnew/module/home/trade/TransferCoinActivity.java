package com.bitcnew.module.home.trade;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.util.GsonUtils;
import com.bitcnew.module.home.trade.dialog.CoinTypePickerDialog;
import com.bitcnew.module.home.trade.entity.CoinConfig;
import com.google.gson.reflect.TypeToken;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.trade.entity.AccountType;
import com.bitcnew.module.home.trade.history.TransferCoinHistoryActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 划转
 */
public class TransferCoinActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.switchIv)
    View switchIv;
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvEnableAmount)
    TextView tvEnableAmount;
    @BindView(R.id.amountCoinTypeTv)
    TextView amountCoinTypeTv;
    @BindView(R.id.tvEnableAmountCoinType)
    TextView tvEnableAmountCoinType;
    @BindView(R.id.tvTransferCoin)
    TextView tvTransferCoin;
    @BindView(R.id.llFrom)
    LinearLayout llFrom;
    @BindView(R.id.llTo)
    LinearLayout llTo;
    @BindView(R.id.tvFrom)
    TextView tvFrom;
    @BindView(R.id.tvTo)
    TextView tvTo;
    @BindView(R.id.tvCoinType)
    TextView tvCoinType;

    private Call<ResponseBody> getCoinTypeCall;
    private Call<ResponseBody> listAccountTypeCall;
    private Call<ResponseBody> outHoldAmountCall;
    private Call<ResponseBody> transferCall;
    private Group<AccountType> group;
    private AccountType accountTypeFrom;
    private AccountType accountTypeTo;


    private String holdAmount = "";
    private int amountDecimals = 6;

//    private ArrayList<String> data = new ArrayList<>();

    private List<CoinConfig> coinTypeList;
    private CoinConfig coinType;

    @Override
    protected int setLayoutId() {
        return R.layout.transfer_coin;
    }


    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.huazhuan);
    }

    public static void pageJump(Context context, String defaultSymbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, defaultSymbol);
        PageJumpUtil.pageJump(context, TransferCoinActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        switchIv.setOnClickListener(this);
        llFrom.setOnClickListener(this);
        llTo.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        tvMenu.setOnClickListener(this);
        tvTransferCoin.setOnClickListener(this);
        tvCoinType.setOnClickListener(this);
        etAmount.addTextChangedListener(new TextWatcher() {
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
                    if (s.length() - 1 - posDot > amountDecimals) {//
                        s.delete(posDot + (amountDecimals + 1), posDot + (amountDecimals + 2));
                    }
                }
            }
        });

        startListAccountTypeCall();
    }

    public void startGetCoinTypeCall() {
        CommonUtil.cancelCall(getCoinTypeCall);
        tvCoinType.setText("");
        coinType = null;
        if (null == accountTypeFrom || null == accountTypeTo) {
            return;
        }
        getCoinTypeCall = VHttpServiceManager.getInstance().getVService().getTransferSymbols(accountTypeFrom.accountType, accountTypeTo.accountType);
        getCoinTypeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    try {
                        if (null != resultData.data && resultData.data.startsWith("[{")) {
                            coinTypeList = GsonUtils.createGson().fromJson(resultData.data, new TypeToken<List<CoinConfig>>() {
                            }.getType());
                        } else if (null != resultData.data) {
                            coinTypeList = new ArrayList<>();
                            String[] arr = GsonUtils.createGson().fromJson(resultData.data, String[].class);
                            if (null != arr) {
                                for (String arrItem : arr) {
                                    coinTypeList.add(new CoinConfig(arrItem));
                                }
                            }
                        } else {
                            coinTypeList = null;
                        }

                        if (coinTypeList != null && coinTypeList.size() == 1) {
                            coinType = coinTypeList.get(0);
                            tvCoinType.setText(coinType.getSymbol());
                            amountCoinTypeTv.setText(coinType.getSymbol());
                            tvEnableAmountCoinType.setText(coinType.getSymbol());
                        }
                    } catch (Exception e) {
                        coinTypeList = null;
                        showToast(e.getMessage());
                    }
                } else {
                    showToast(resultData.msg);
                }
            }
        });
    }

    public void startListAccountTypeCall() {
        CommonUtil.cancelCall(listAccountTypeCall);
        listAccountTypeCall = VHttpServiceManager.getInstance().getVService().listAccountType();
        listAccountTypeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    group = resultData.getGroup("accountTypeList", new TypeToken<Group<AccountType>>() {
                    }.getType());
                    if (group != null) {
                        if (group.size() > 0) {
                            if (accountTypeFrom == null) {
                                accountTypeFrom = group.get(0);
                                tvFrom.setText(accountTypeFrom.accountName);
                            }
                            startOutHoldAmountCall(accountTypeFrom.accountType);
                        }
                        if (group.size() > 1) {
                            if(accountTypeTo==null){
                                accountTypeTo = group.get(1);
                                tvTo.setText(accountTypeTo.accountName);
                            }
                        }
                        getApplicationContext().accountTypeGroup = group;
                        startGetCoinTypeCall();
                    }
                }
            }
        });
    }

    public void startOutHoldAmountCall(String accountType) {
        CommonUtil.cancelCall(outHoldAmountCall);
        outHoldAmountCall = VHttpServiceManager.getInstance().getVService().outHoldAmount(accountType);
        outHoldAmountCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    amountDecimals = resultData.getItem("amountDecimals", Integer.class);
                    holdAmount = resultData.getItem("holdAmount", String.class);
                    tvEnableAmount.setText(getResources().getString(R.string.keyongshuliang)+": " + holdAmount);
                }
            }
        });
    }


    public void startTransferCall(final String amount, final String accountTypeFrom, final String accountTypeTo, String payPass) {
        CommonUtil.cancelCall(transferCall);
        transferCall = VHttpServiceManager.getInstance().getVService().transfer(coinType.getSymbol(), amount, accountTypeFrom, accountTypeTo,payPass);
        transferCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, TransferCoinActivity.this);
                    etAmount.setText("");
                    startListAccountTypeCall();
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startTransferCall(amount, accountTypeFrom,accountTypeTo,pwString);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data != null) {
                String accountName = data.getStringExtra("accountName");
                String accountType = data.getStringExtra("accountType");
                AccountType account = new AccountType(accountName, accountType);
                if (requestCode == 0x123) {
                    accountTypeFrom = account;
                    tvFrom.setText(accountName);
                    startOutHoldAmountCall(accountTypeFrom.accountType);
                    startGetCoinTypeCall();
                } else if (requestCode == 0x456) {
                    accountTypeTo = account;
                    tvTo.setText(accountName);
                    startGetCoinTypeCall();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switchIv:
                AccountType temp = accountTypeTo;
                accountTypeTo = accountTypeFrom;
                accountTypeFrom = temp;

                tvFrom.setText(accountTypeFrom == null ? null: accountTypeFrom.accountName);
                tvTo.setText(accountTypeTo == null ? null : accountTypeTo.accountName);

                if (null != accountTypeFrom) {
                    startOutHoldAmountCall(accountTypeFrom.accountType);
                }

                startGetCoinTypeCall();
                break;
            case R.id.llFrom:
                if (group == null || group.size() == 0) return;
                PageJumpUtil.pageJumpResult(TransferCoinActivity.this, TransferSelectAccountActivity.class, new Intent(), 0x123);
                break;
            case R.id.llTo:
                if (group == null || group.size() == 0) return;
                PageJumpUtil.pageJumpResult(TransferCoinActivity.this, TransferSelectAccountActivity.class, new Intent(), 0x456);
                break;
            case R.id.tvAll:
                etAmount.setText(holdAmount);
                etAmount.setSelection(holdAmount.length());
                break;
            case R.id.tvTransferCoin:
                String amount = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshuruhuazhuanshuliang), TransferCoinActivity.this);
                    return;
                }

                if (accountTypeFrom != null && accountTypeTo != null) {
                    if (accountTypeFrom.accountType.equals(accountTypeTo.accountType)) {
                        CommonUtil.showmessage(getResources().getString(R.string.xiangtongzhanghuzhijianbunenghuazhuan), TransferCoinActivity.this);
                        return;
                    }
                    if (null == coinType) {
                        CommonUtil.showmessage(getResources().getString(R.string.qingxuanzebizhong), TransferCoinActivity.this);
                        return;
                    }
                    startTransferCall(amount, accountTypeFrom.accountType, accountTypeTo.accountType,"");
                }
                break;
            case R.id.tvMenu:
                PageJumpUtil.pageJump(TransferCoinActivity.this, TransferCoinHistoryActivity.class);
                break;
            case R.id.tvCoinType:
                pickCoinType();
                break;
        }
    }

    private void pickCoinType() {
        if (null == coinTypeList) {
            return;
        }

        new CoinTypePickerDialog(getContext(), coinTypeList, coinType -> {
            this.coinType = coinType;
            tvCoinType.setText(coinType.getSymbol());
            amountCoinTypeTv.setText(coinType.getSymbol());
            tvEnableAmountCoinType.setText(coinType.getSymbol());
        }).show();
    }

    @Override
    public void finish() {
        getApplicationContext().accountTypeGroup = null;
        super.finish();
    }

}

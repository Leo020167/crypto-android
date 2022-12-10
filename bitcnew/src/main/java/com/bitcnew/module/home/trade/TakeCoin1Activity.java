package com.bitcnew.module.home.trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.CommonToolbar;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.text.MoneyTextWatcher;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.trade.dialog.CoinTypePickerDialog;
import com.bitcnew.module.home.trade.entity.CoinChains;
import com.bitcnew.module.home.trade.entity.TakeCoinAddress;
import com.bitcnew.module.home.trade.entity.TakeCoinConfig;
import com.bitcnew.module.home.trade.history.TakeCoinHistoryActivity;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提币
 */
public class TakeCoin1Activity extends TJRBaseToolBarSwipeBackActivity {

    private static final int RC_ADDRESS = 11;

    @BindView(R.id.commonToolbar)
    CommonToolbar commonToolbar;
    @BindView(R.id.keyongLabel)
    TextView keyongLabel;
    @BindView(R.id.keyongTv)
    TextView keyongTv;
    @BindView(R.id.dongjieLabel)
    TextView dongjieLabel;
    @BindView(R.id.dongjieTv)
    TextView dongjieTv;
    @BindView(R.id.xuanzetibiwangluoLabel)
    TextView xuanzetibiwangluoLabel;
    @BindView(R.id.coinTypeTv)
    TextView coinTypeTv;
    @BindView(R.id.coinSubTypeRadioGroup)
    RadioGroup coinSubTypeRadioGroup;
    @BindView(R.id.tibidizhiTv)
    TextView tibidizhiTv;
    @BindView(R.id.tibishuliangEt)
    EditText tibishuliangEt;
    @BindView(R.id.shouxufeiLabel)
    TextView shouxufeiLabel;
    @BindView(R.id.shouxufeiTv)
    TextView shouxufeiTv;
    @BindView(R.id.daozhangshuliangLabel)
    TextView daozhangshuliangLabel;
    @BindView(R.id.daozhangshuliangTv)
    TextView daozhangshuliangTv;

    private Call<ResponseBody> coinListCall;
    private CoinChains coinChains;

    private String coinType;
    private String chain;
    private TakeCoinAddress address;

    private Call<ResponseBody> configCall;
    private TakeCoinConfig config;

    private Call<ResponseBody> submitCall;

    // 提交
    @OnClick(R.id.action_submit)
    public void onSubmitClick() {
        if (coinType == null) {
            showToast(R.string.xuanzetibibizhong);
            return;
        }
        if (needChain(coinType) && null == chain) {
            showToast(R.string.xuanzetibiwangluo);
            return;
        }
        if (null == address) {
            showToast(R.string.qingxuanzetibidizhi);
            return;
        }
        if (null == config) {
            loadConfig(coinType);
            return;
        }
        String amount = tibishuliangEt.getText().toString().trim();
        if (amount.length() == 0) {
            showToast(R.string.qingshurutibishuliang);
            return;
        }

        try {
            BigDecimal available = new BigDecimal(config.getAvailableAmount()).subtract(new BigDecimal(config.getFee()));
            if (available.compareTo(BigDecimal.ZERO) <= 0) {
                showToast(getString(R.string._yuebuzu, coinType));
                return;
            }
            if (new BigDecimal(amount).compareTo(available) > 0) {
                showToast(getString(R.string._yuebuzu, coinType));
                return;
            }

            CommonUtil.cancelCall(submitCall);
            submitCall = VHttpServiceManager.getInstance().getVService().withdrawSubmit(getUserIdLong(), address.getId(), amount);
            submitCall.enqueue(new MyCallBack(this) {
                @Override
                protected void callBack(ResultData resultData) {
                    if (resultData.isSuccess()) {
                        showToast(getString(R.string.success, getString(R.string.tibishenqing)));
                    } else {
                        showToast(resultData.msg);
                    }
                }
            });
        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    // 选择币种
    @OnClick(R.id.coinTypeTv)
    void onCoinTypeClick() {
        // 选择币种
        showCoinTypePickerDialog();
    }

    // 提币地址管理
    @OnClick({R.id.tibidizhiguanliTv, R.id.tibidizhiTv})
    void onTibidizhiguanliClick() {
        Intent intent = new Intent(getContext(), TakeCoinAddressListActivity.class);
        startActivityForResult(intent, RC_ADDRESS);
//        PageJumpUtil.pageJump(getContext(), TakeCoinAddressListActivity.class);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.take_coin1;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.tibi);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RC_ADDRESS == requestCode) {
            handleAddressPick(resultCode, data);
        }
    }

    private void handleAddressPick(int resultCode, @Nullable Intent data) {
        if (RESULT_OK != resultCode || null == data) {
            return;
        }

        TakeCoinAddress address = (TakeCoinAddress) data.getSerializableExtra("data");
        if (null == address) {
            return;
        }

        setCoinType(address.getSymbol());
        setChain(address.getChainType());

        tibidizhiTv.setText(address.getAddress());
        this.address = address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        commonToolbar.setOnMoreListener(v -> {
            PageJumpUtil.pageJump(getContext(), TakeCoinHistoryActivity.class);
        });

        coinSubTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton btn = radioGroup.findViewById(i);
                if (null != btn) {
                    setChain(btn.getText().toString());
                }
            }
        });

        tibishuliangEt.addTextChangedListener(new MoneyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);

                if (tibishuliangEt.length() == 0 || null == config) {
                    daozhangshuliangTv.setText("-");
                } else {
                    try {
                        daozhangshuliangTv.setText(new BigDecimal(tibishuliangEt.getText().toString()).subtract(new BigDecimal(config.getFee())).toPlainString());
                    } catch (Exception e) {
                        daozhangshuliangTv.setText("-");
                    }
                }
            }
        });

        // 加载币种列表
        loadCoinTypeList(false);
    }

    private void showCoinTypePickerDialog() {
        if (null == coinChains) {
            loadCoinTypeList(true);
            return;
        }

        _showCoinTypePickerDialog();
    }

    private void _showCoinTypePickerDialog() {
        if (null == coinChains) {
            return;
        }
        new CoinTypePickerDialog(getContext(), coinChains.getCoinList(), coinType -> {
            setCoinType(coinType);
        }).show();
    }

    private void loadCoinTypeList(boolean showDialogFlag) {
        CommonUtil.cancelCall(coinListCall);
        coinListCall = VHttpServiceManager.getInstance().getVService().coinList(-1, getUserIdLong());
        coinListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    coinChains = gson.fromJson(resultData.data, CoinChains.class);
                    initChainRadioGroup();

                    if (showDialogFlag) {
                        _showCoinTypePickerDialog();
                    } else if (null == coinType && null != coinChains && null != coinChains.getCoinList() && !coinChains.getCoinList().isEmpty()) {
                        setCoinType(coinChains.getCoinList().get(0));
                    }
                } else {
                    showToast(resultData.msg);
                }
            }
        });
    }

    private void initChainRadioGroup() {
        if (null == coinChains) {
            return;
        }

        coinSubTypeRadioGroup.removeAllViews();

        RadioButton btn;
        boolean first = true;
        for (String chain : coinChains.getChainTypeList()) {
            btn = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.take_coin1_radio_item, coinSubTypeRadioGroup, false);
            btn.setId(ViewCompat.generateViewId());
            btn.setText(chain);
            if (first) {
                setChain(chain);
                btn.setChecked(true);
                first = false;
            }
            coinSubTypeRadioGroup.addView(btn);
        }
    }

    private void setCoinType(String coinType) {
        if (Objects.equals(coinType, this.coinType)) {
            return;
        }

        this.coinType = coinType;
        coinTypeTv.setText(coinType);

        if (needChain(coinType)) {
            xuanzetibiwangluoLabel.setVisibility(View.VISIBLE);
            coinSubTypeRadioGroup.setVisibility(View.VISIBLE);
        } else {
            xuanzetibiwangluoLabel.setVisibility(View.GONE);
            coinSubTypeRadioGroup.setVisibility(View.GONE);
        }

        keyongLabel.setText(getString(R.string.keyongyue_, coinType));
        dongjieLabel.setText(getString(R.string.dongjiejine_, coinType));
        shouxufeiLabel.setText(getString(R.string.tibishouxufei, coinType));
        daozhangshuliangLabel.setText(getString(R.string.tibidaozhangshuliang, coinType));

        loadConfig(coinType);
    }

    private void setChain(String chain) {
        this.chain = chain;

        if (null != chain && coinSubTypeRadioGroup.getChildCount() > 0) {
            RadioButton btn;
            for (int i = coinSubTypeRadioGroup.getChildCount() - 1; i >= 0; i--) {
                btn = (RadioButton) coinSubTypeRadioGroup.getChildAt(i);
                if (btn.getText().toString().equalsIgnoreCase(chain) && !btn.isChecked()) {
                    btn.setChecked(true);
                }
            }
        }
    }

    private boolean needChain(String coinType) {
        return "usdt".equalsIgnoreCase(coinType);
    }

    private void loadConfig(String coinType) {
        if (null == coinType || coinType.length() == 0) {
            return;
        }
        CommonUtil.cancelCall(configCall);
        configCall = VHttpServiceManager.getInstance().getVService().getWithdrawConfigs(getUserIdLong(), coinType);
        configCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    config = gson.fromJson(resultData.data, TakeCoinConfig.class);
                    keyongTv.setText(config.getAvailableAmount());
                    dongjieTv.setText(config.getFrozenAmount());
                    shouxufeiTv.setText(config.getFee());
                } else {
                    showToast(resultData.msg);
                }
            }
        });
    }

}

package com.bitcnew.module.home.trade;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.trade.dialog.CoinTypePickerDialog;
import com.bitcnew.module.home.trade.entity.CoinChains;
import com.bitcnew.module.home.trade.entity.CoinConfig;
import com.bitcnew.util.MyCallBack;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提币地址添加
 */
public class TakeCoinAddressAddActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.xuanzetibiwangluoLabel)
    TextView xuanzetibiwangluoLabel;
    @BindView(R.id.coinTypeTv)
    TextView coinTypeTv;
    @BindView(R.id.coinSubTypeRadioGroup)
    RadioGroup coinSubTypeRadioGroup;
    @BindView(R.id.addressEt)
    EditText addressEt;
    @BindView(R.id.beizhuEt)
    EditText beizhuEt;

    private Call<ResponseBody> coinListCall;
    private CoinChains coinChains;

    private Call<ResponseBody> addCall;
    private CoinConfig coinType;
    private String chain;

    @OnClick(R.id.coinTypeTv)
    public void onCoinTypeClick() {
        // 选择币种
        showCoinTypePickerDialog();
    }

    @OnClick(R.id.action_copy)
    public void onCopyClick() {
        addressEt.setText(com.bitcnew.util.CommonUtil.getClipboardText(getContext()));
    }

    @OnClick(R.id.action_add)
    public void onAddClick() {
        if (null == coinType) {
            showToast(R.string.xuanzetibibizhong);
            return;
        }
        if (needChain(coinType) && null == chain) {
            showToast(R.string.xuanzetibiwangluo);
            return;
        }
        String address = addressEt.getText().toString();
        if (address.isEmpty()) {
            showToast(R.string.qingshurudizhi);
            return;
        }
        String beizhu = beizhuEt.getText().toString();

        doAddTakeCoinAddress(address, beizhu, null);
    }

    private void doAddTakeCoinAddress(String address, String beizhu, String payPass) {
        CommonUtil.cancelCall(addCall);
        addCall = VHttpServiceManager.getInstance().getVService().addTakeCoinAddress(getUserIdLong(), coinType.getSymbol(), chain, address, beizhu, payPass);
        addCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    showToast(getString(R.string.success, getString(R.string.tianjiatibidizhi)));
                    finish();
                } else {
                    showToast(resultData.msg);
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                doAddTakeCoinAddress(address, beizhu, pwString);
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.take_coin_address_add_activity;
    }

    @Override
    protected String getActivityTitle() {
        return getContext().getString(R.string.tianjiatibidizhi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        coinSubTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton btn = radioGroup.findViewById(i);
                if (null != btn) {
                    chain = btn.getText().toString();
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
        new CoinTypePickerDialog(getContext(), coinChains.getCoinConfigList(), coinType -> {
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
                        setCoinType(coinChains.getCoinConfigList().get(0));
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
                this.chain = chain;
                btn.setChecked(true);
                first = false;
            }
            coinSubTypeRadioGroup.addView(btn);
        }
    }

    private void setCoinType(CoinConfig coinType) {
        this.coinType = coinType;
        coinTypeTv.setText(coinType.getSymbol());

        if (needChain(coinType)) {
            xuanzetibiwangluoLabel.setVisibility(View.VISIBLE);
            coinSubTypeRadioGroup.setVisibility(View.VISIBLE);
        } else {
            xuanzetibiwangluoLabel.setVisibility(View.GONE);
            coinSubTypeRadioGroup.setVisibility(View.GONE);
        }
    }

    private boolean needChain(CoinConfig coinType) {
        return null != coinType && "usdt".equalsIgnoreCase(coinType.getSymbol());
    }

}

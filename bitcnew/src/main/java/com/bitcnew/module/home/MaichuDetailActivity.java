package com.bitcnew.module.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaichuDetailActivity extends TJRBaseToolBarSwipeBackActivity {


    @BindView(R.id.txt_Symbol)
    TextView txt_Symbol;
    @BindView(R.id.txt_Yingkui)
    TextView txt_Yingkui;
    @BindView(R.id.txt_Chengben)
    TextView txt_Chengben;
    @BindView(R.id.txt_Maichujiage)
    TextView txt_Maichujiage;
    @BindView(R.id.txt_Maichushuliang)
    TextView txt_Maichushuliang;
    @BindView(R.id.txt_Shouxufei)
    TextView txt_Shouxufei;
    @BindView(R.id.txt_Shijian)
    TextView txt_Shijian;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_maichu_detail;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.jiaoyixiangqing);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        String symbol = getIntent().getStringExtra("symbol");
        String profit = getIntent().getStringExtra("profit");
        String originPrice = getIntent().getStringExtra("originPrice");
        String price = getIntent().getStringExtra("price");
        String amount = getIntent().getStringExtra("amount");
        String fee = getIntent().getStringExtra("fee");
        String updateTime = getIntent().getStringExtra("updateTime");
        if (!TextUtils.isEmpty(symbol)){
            txt_Symbol.setText(""+symbol);
        }else {
            txt_Symbol.setText("");
        }
        if (!TextUtils.isEmpty(profit)){
            if (profit.contains("-")){
                txt_Yingkui.setTextColor(getResources().getColor(R.color.ccc1414));
            }else {
                txt_Yingkui.setTextColor(getResources().getColor(R.color.c14cc4B));
            }
            txt_Yingkui.setText(profit);
        }else {
            txt_Yingkui.setText("");
        }
        if (!TextUtils.isEmpty(originPrice)){
            txt_Chengben.setText(originPrice);
        }else {
            txt_Chengben.setText("");
        }
        if (!TextUtils.isEmpty(price)){
            txt_Maichujiage.setText(price);
        }else {
            txt_Maichujiage.setText("");
        }
        if (!TextUtils.isEmpty(amount)){
            txt_Maichushuliang.setText(amount);
        }else {
            txt_Maichushuliang.setText("");
        }
        if (!TextUtils.isEmpty(fee)){
            txt_Shouxufei.setText(fee);
        }else {
            txt_Shouxufei.setText("");
        }
        if (!TextUtils.isEmpty(updateTime)){
            txt_Shijian.setText(DateUtils.getStringDateOfString2(String.valueOf(updateTime), DateUtils.TEMPLATE_yyyyMMdd_HHmm));
        }else {
            txt_Shijian.setText("");
        }
    }
}
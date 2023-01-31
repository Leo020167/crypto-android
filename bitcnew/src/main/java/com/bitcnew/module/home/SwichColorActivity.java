package com.bitcnew.module.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwichColorActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener{

    public static final String COLOR_GREEN_UP_RED_DOWN = "1";
    public static final String COLOR_RED_UP_GREEN_DOWN = "0";

    @BindView(R.id.txt_color1)
    TextView txt_color1;
    @BindView(R.id.txt_color2)
    TextView txt_color2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        txt_color1.setOnClickListener(this);
        txt_color2.setOnClickListener(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_swich_color;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.zhangdieyanse);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_color1:
                SPUtils.put(this,"swichColor",COLOR_GREEN_UP_RED_DOWN);
                CommonUtil.showmessage(getResources().getString(R.string.shezhichenggong),this);
                finish();
                break;
            case R.id.txt_color2:
                SPUtils.put(this,"swichColor",COLOR_RED_UP_GREEN_DOWN);
                CommonUtil.showmessage(getResources().getString(R.string.shezhichenggong),this);
                finish();
                break;
        }
    }
}
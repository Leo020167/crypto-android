package com.bitcnew.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

//跟单选择账户
public class SelectAccountActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener{
    @BindView(R.id.txt_content10)
    TextView txtContent1;
    @BindView(R.id.txt_content20)
    TextView txtContent2;
    @BindView(R.id.txt_content30)
    TextView txtContent3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        txtContent1.setOnClickListener(this);
        txtContent2.setOnClickListener(this);
        txtContent3.setOnClickListener(this);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_select_account;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.xuanzezhanghu);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_content10){
            Intent intent=new Intent();
            Bundle bundle1=new Bundle();
            bundle1.putString("ty","0");
            intent.putExtras(bundle1);
            setResult(RESULT_OK,intent);
            finish();
        }else if (view.getId() == R.id.txt_content20){
            Intent intent=new Intent();
            Bundle bundle1=new Bundle();
            bundle1.putString("ty","1");
            intent.putExtras(bundle1);
            setResult(RESULT_OK,intent);
            finish();
        }else if (view.getId() == R.id.txt_content30){
            Intent intent=new Intent();
            Bundle bundle1=new Bundle();
            bundle1.putString("ty","2");
            intent.putExtras(bundle1);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
package com.bitcnew.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectJiaoyijiluTypeActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener{
    @BindView(R.id.txt_type1)
    TextView txt_type1;
    @BindView(R.id.txt_type2)
    TextView txt_type2;
    @BindView(R.id.txt_type3)
    TextView txt_type3;
    @BindView(R.id.txt_type4)
    TextView txt_type4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        txt_type1.setOnClickListener(this);
        txt_type2.setOnClickListener(this);
        txt_type3.setOnClickListener(this);
        txt_type4.setOnClickListener(this);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_select_jiaoyijilu_type;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.xuanzejiaoyileixing);
    }

    @Override
    public void onClick(View view) {//follow跟单交易记录   stock全球指数交易记录   digital合约交易记录    spot币币交易记录
        if (view.getId() == R.id.txt_type1){
            Intent intent=new Intent();
            Bundle bundle1=new Bundle();
            bundle1.putString("accountType","follow");
            bundle1.putString("title",txt_type1.getText().toString());
            intent.putExtras(bundle1);
            setResult(RESULT_OK,intent);
            finish();
        }else if (view.getId() == R.id.txt_type2){
            Intent intent=new Intent();
            Bundle bundle1=new Bundle();
            bundle1.putString("accountType","stock");
            bundle1.putString("title",txt_type2.getText().toString());
            intent.putExtras(bundle1);
            setResult(RESULT_OK,intent);
            finish();
        }else if (view.getId() == R.id.txt_type3){
            Intent intent=new Intent();
            Bundle bundle1=new Bundle();
            bundle1.putString("accountType","digital");
            bundle1.putString("title",txt_type3.getText().toString());
            intent.putExtras(bundle1);
            setResult(RESULT_OK,intent);
            finish();
        }else if (view.getId() == R.id.txt_type4){
            Intent intent=new Intent();
            Bundle bundle1=new Bundle();
            bundle1.putString("accountType","spot");
            bundle1.putString("title",txt_type4.getText().toString());
            intent.putExtras(bundle1);
            setResult(RESULT_OK,intent);
            finish();
        }
    }
}
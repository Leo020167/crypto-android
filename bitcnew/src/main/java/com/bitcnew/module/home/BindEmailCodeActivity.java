package com.bitcnew.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.util.MyCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class BindEmailCodeActivity extends TJRBaseToolBarActivity implements  View.OnClickListener {
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.txt_sure)
    TextView txtSure;

    private int type =0;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        txtSure.setOnClickListener(this);

        type = getIntent().getIntExtra("type",0);
        email=getIntent().getStringExtra("email");
        txtEmail.setText(getResources().getString(R.string.yanzhengmayifasongzhi)+email);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_bind_email_code;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.youxiangyanzhengma);
    }

    @Override
    public void onClick(View view) {
         if (view.getId() == R.id.txt_sure){
             if (TextUtils.isEmpty(etCode.getText().toString().trim())){
                 Toast.makeText(BindEmailCodeActivity.this, getResources().getString(R.string.qingshuruyouxiangyanzhengma), Toast.LENGTH_SHORT).show();
                 return;
             }
             getUpdateEmail(etCode.getText().toString().trim());
         }
    }

    private Call<ResponseBody> getUpdateEmailCall;
    private void getUpdateEmail(String code) {//发送邮箱验证码
        getUpdateEmailCall = VHttpServiceManager.getInstance().getVService().getUpdateEmail(email,code);
        getUpdateEmailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Toast.makeText(BindEmailCodeActivity.this, getResources().getString(R.string.bangdingchenggong), Toast.LENGTH_SHORT).show();
                    if (type==1){
                        Intent intent = new Intent();
                        intent.putExtra("success","1");
                        setResult(1,intent);
                        finish();
                    }else {
                        finish();
                    }
                }
            }
        });
    }
}
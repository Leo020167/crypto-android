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

public class EmailAuthCodeActivity extends TJRBaseToolBarActivity implements  View.OnClickListener {
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.txt_sure)
    TextView txtSure;

    private String email;
    private int mode;//1更改绑定邮箱
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        txtSure.setOnClickListener(this);
        email = getIntent().getStringExtra("email");
        mode = getIntent().getIntExtra("mode",0);
        txtEmail.setText(getResources().getString(R.string.yanzhengmayifasongzhi)+email);
        getEmailGet(email);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_email_auth_code;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.youxiangyanzhengma);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_sure){
            if (TextUtils.isEmpty(etCode.getText().toString().trim())){
                Toast.makeText(EmailAuthCodeActivity.this, getResources().getString(R.string.qingshuruyouxiangyanzhengma), Toast.LENGTH_SHORT).show();
                return;
            }
            getCheckEmailCode(etCode.getText().toString().trim());
        }
    }

    private Call<ResponseBody> getEmailGetCall;
    private void getEmailGet(String email) {//发送邮箱验证码
        getEmailGetCall = VHttpServiceManager.getInstance().getVService().getEmailGet(email);
        getEmailGetCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {

                }
            }
        });
    }
    private Call<ResponseBody> getCheckEmailCodeCall;
    private void getCheckEmailCode(String code) {//验证邮箱
        getCheckEmailCodeCall = VHttpServiceManager.getInstance().getVService().getCheckEmailCode(email,code);
        getCheckEmailCodeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (mode==1){
                        Intent intent = new Intent(EmailAuthCodeActivity.this, BindEmailActivity.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(EmailAuthCodeActivity.this, getResources().getString(R.string.yanzhengchenggong), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}
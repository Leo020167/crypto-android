package com.bitcnew.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.util.MyCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class EmailAuthActivity extends TJRBaseToolBarActivity implements  View.OnClickListener {
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.txt_send_code)
    TextView txtSendCode;

    private int mode;//1更改绑定邮箱

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        txtSendCode.setOnClickListener(this);
        mode = getIntent().getIntExtra("mode",0);
        startGetMyHomeCallCall();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_email_auth;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.anquanyanzheng);
    }

    @Override
    public void onClick(View view) {
       if (view.getId() == R.id.txt_send_code){
           if (!TextUtils.isEmpty(email)){
               Intent intent = new Intent(EmailAuthActivity.this, EmailAuthCodeActivity.class);
               intent.putExtra("email",email);
               intent.putExtra("mode",mode);
               startActivity(intent);
               finish();
           }
       }
    }

    private String email;
    private Call<ResponseBody> getUserInfoCall;
    private void startGetMyHomeCallCall() {
        getUserInfoCall = VHttpServiceManager.getInstance().getVService().myUserInfo();
        getUserInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    email = resultData.getItem("email", String.class);
                    txtEmail.setText(email);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


}
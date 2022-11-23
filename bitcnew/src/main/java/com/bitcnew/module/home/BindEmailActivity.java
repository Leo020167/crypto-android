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

public class BindEmailActivity extends TJRBaseToolBarActivity implements  View.OnClickListener {
    @BindView(R.id.txt_email)
    TextView txtEmail;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.txt_send_code)
    TextView txtSendCode;

    private int type =0;
    private String email;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_bind_email;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.qingshuruxinyouxiang);
    }

    @Override
    public void onClick(View view) {
         if (view.getId()==R.id.txt_send_code){
             if (TextUtils.isEmpty(etEmail.getText().toString())){
                 Toast.makeText(this,getResources().getString(R.string.qingshuruyouxiang),Toast.LENGTH_LONG);
                 return;
             }
             getEmailGet(etEmail.getText().toString());
         }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        txtSendCode.setOnClickListener(this);
        type = getIntent().getIntExtra("type",0);
        email = getIntent().getStringExtra("email");
        if (!TextUtils.isEmpty(email)){
            String title = String.format(getResources().getString(R.string.ninmuqiandeyouxiangshi), "email");
            txtEmail.setText(title);
            txtEmail.setVisibility(View.VISIBLE);
        }else{
            txtEmail.setVisibility(View.INVISIBLE);
        }
    }


    private Call<ResponseBody> getEmailGetCall;
    private void getEmailGet(String email) {//发送邮箱验证码
        getEmailGetCall = VHttpServiceManager.getInstance().getVService().getEmailGet(email);
        getEmailGetCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (type==1){
                        Intent intent = new Intent(BindEmailActivity.this, BindEmailCodeActivity.class);
                        intent.putExtra("email",email);
                        intent.putExtra("type",1);
                        startActivityForResult(intent,108);
                    }else {
                        Intent intent = new Intent(BindEmailActivity.this, BindEmailCodeActivity.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==108){
            if (resultCode == 1&&data!=null){
                String  a = data.getStringExtra("success");
                if (!TextUtils.isEmpty(a)){
                    Intent intent = new Intent();
                    intent.putExtra("success","1");
                    setResult(1,intent);
                    finish();
                }
            }
        }
    }
}
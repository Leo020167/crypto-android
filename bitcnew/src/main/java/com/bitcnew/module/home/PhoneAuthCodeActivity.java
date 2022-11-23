package com.bitcnew.module.home;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.model.User;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.trade.TakeCoinActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class PhoneAuthCodeActivity extends TJRBaseToolBarActivity implements  View.OnClickListener {
    @BindView(R.id.txt_phone)
    TextView txtPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.txt_sure)
    TextView txtSure;

    private int type=0;//0单独验证手机号，1验证完手机号继续验证邮箱
    private String phone,email;
    private String key;
    private int location;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        txtSure.setOnClickListener(this);
        user = getApplicationContext().getUser();
        phone = getIntent().getStringExtra("phone");
        email = getIntent().getStringExtra("email");
        type = getIntent().getIntExtra("type",0);
        if (!TextUtils.isEmpty(phone)){
            txtPhone.setText(getResources().getString(R.string.yanzhengmayifasongzhi)+phone);
            mActionBar.setTitle(getResources().getString(R.string.duanxinyanzhengma));
        }else{
            txtPhone.setText(getResources().getString(R.string.yanzhengmayifasongzhi)+email);
            mActionBar.setTitle(getResources().getString(R.string.youxiangyanzhengma));
        }
        startGetVCode(key, location);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_phone_auth_code;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.yanzhengma);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_sure){
            if (TextUtils.isEmpty(etCode.getText().toString().trim())){
                Toast.makeText(PhoneAuthCodeActivity.this, getResources().getString(R.string.qingshuruduanxinyanzhengma), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(phone)) {
                checkIdentity("", 0);
            }else{
                getCheckEmailCode(etCode.getText().toString().trim());
            }
        }
    }

    private Call<ResponseBody> smsCall;
    public void startGetVCode(String key, int location) {
        CommonUtil.cancelCall(smsCall);

        Log.d("154", " phone == " + phone + " email ==" + email + "key == " + key + " location == " + location);

        if (phone != null && !phone.isEmpty()) {
            smsCall = VHttpServiceManager.getInstance().getVService().getSms(key, location,"android",1,user.getCountryCode(),phone );
        } else {
            smsCall = VHttpServiceManager.getInstance().getVService().getSms(key,location,"android",2,"",email);
        }

        smsCall.enqueue(new MyCallBack(PhoneAuthCodeActivity.this, 2) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {

                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                CommonUtil.showmessage(getResources().getString(R.string.huoquchenggong), PhoneAuthCodeActivity.this);
                PhoneAuthCodeActivity.this.key = dragImgKey;
                PhoneAuthCodeActivity.this.location = locationx;
                startGetVCode(dragImgKey, locationx);
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
                    CommonUtil.showmessage(resultData.msg, PhoneAuthCodeActivity.this);
                    Intent intent = new Intent();
                    intent.putExtra("success","1");
                    setResult(1,intent);
                    finish();
                }
            }
        });
    }

    private String dragImgKey = "";
    private int locationx = 0;
    private String smsCode = "";
    private Call<ResponseBody> checkIdentityCall;
    public void checkIdentity(String dragImgKey, int locationx) {
        this.dragImgKey = dragImgKey;
        this.locationx = locationx;
        smsCode = etCode.getText().toString().trim();
        CommonUtil.cancelCall(checkIdentityCall);
        checkIdentityCall = VHttpServiceManager.getInstance().getVService().checkIdentity(phone, smsCode, dragImgKey, locationx);
        checkIdentityCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (type==1){//继续验证邮箱
                        if (!TextUtils.isEmpty(email)){//如果邮箱不为空，去验证邮箱
                            Intent intent = new Intent(PhoneAuthCodeActivity.this, PhoneAuthCodeActivity.class);
                            intent.putExtra("email",email);
                            startActivityForResult(intent,107);
                        }else{//如果邮箱为空，先跳绑定邮箱页面去绑定
                            Intent intent = new Intent(PhoneAuthCodeActivity.this, BindEmailActivity.class);
                            intent.putExtra("type",1);
                            startActivityForResult(intent,107);
                        }
                    }else {
                        CommonUtil.showmessage(resultData.msg, PhoneAuthCodeActivity.this);
                        Intent intent = new Intent();
                        intent.putExtra("success","1");
                        setResult(1,intent);
                        finish();
                    }
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                checkIdentity(dragImgKey,locationx);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==107){
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
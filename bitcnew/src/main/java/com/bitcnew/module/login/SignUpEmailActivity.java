package com.bitcnew.module.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bitcnew.R;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.common.TJrLoginTypeEnum;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.MD5;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class SignUpEmailActivity extends AppCompatActivity {
    private EditText EmailTextField;
    private EditText PasswordTextField;
    private EditText ConfirmPasswordTextField;
    private EditText InviteCodeTextField;

    public static final String emailParam = "EMAIL";
    public static final String UserPass = "UP";
    public static final String CUserPass = "DOWN";
    public static final String CountryCode = "COUNTRYCODE";
    public static final String INVITECODE = "InviteCode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_email);
        ButterKnife.bind(this);

        EmailTextField = (EditText) findViewById(R.id.et_email);
        PasswordTextField = (EditText) findViewById(R.id.et_password);
        ConfirmPasswordTextField = (EditText) findViewById(R.id.et_confirmpassword);
        InviteCodeTextField = (EditText) findViewById(R.id.etInviteCode);
    }

    @OnClick(R.id.register_phone_text)
    public void otcNextClick(View view) {
        startActivity(new Intent(SignUpEmailActivity.this, SignUpActivity.class));
    }

    @OnClick(R.id.tv_register)
    public void onRegisterClick(View view) {
        final String email = EmailTextField.getText().toString();
        final String password = PasswordTextField.getText().toString();
        final String confirmPassword = ConfirmPasswordTextField.getText().toString();
        final String inviteCode = InviteCodeTextField.getText().toString();

        if (TextUtils.isEmpty(email)) {
            CommonUtil.showmessage("請輸入郵箱號", this);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            CommonUtil.showmessage("請輸入密碼", this);
            return;
        }

        if (!password.equals(confirmPassword)) {
            CommonUtil.showmessage("請再次輸入密碼", this);
            return;
        }

//        if (TextUtils.isEmpty(inviteCode)) {
//            CommonUtil.showmessage("Missing Invite Code", this);
//            return;
//        }

        Bundle bundle = new Bundle();
        bundle.putString(emailParam, email);
        bundle.putString(UserPass, password);
        bundle.putString(CUserPass, confirmPassword);
        bundle.putString(INVITECODE, inviteCode);
//        PageJumpUtil.pageJumpResult(SignUpEmailActivity.this, VerifyActivity.class, bundle);
        startRegiste(email, password, confirmPassword, inviteCode);
    }

    public Context getContext() {
        return this;
    }

    private Call<ResponseBody> registeCall;

    public void startRegiste(String email, String pass, String cpass, String inviteCode) {
        CommonUtil.cancelCall(registeCall);
        if (email != null && !email.isEmpty()) {
            registeCall = VHttpServiceManager.getInstance().getVService().doRegisteEmail(
                    email,"", "", MD5.getMessageDigest(pass).toUpperCase(),
                    MD5.getMessageDigest(cpass).toUpperCase(), inviteCode, 2, "", 0, "", "", "", 0);
        }

        registeCall.enqueue(new MyCallBack(getContext()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, getContext());
                    Bundle bundle = new Bundle();
                    String userAccount;
                    if (email != null && !email.isEmpty()) {
                        userAccount = email;
                    } else {
                        return;
                    }

                    bundle.putString(CommonConst.USERACCOUNT, userAccount);
                    bundle.putString(CommonConst.PASSWORD, pass);// password
                    bundle.putString(CommonConst.LOGIN_TYPE, TJrLoginTypeEnum.mb.type());
                    bundle.putBoolean(CommonConst.IS_LOGIN, true);
                    bundle.putString(CommonConst.MYINFO, getRawResult());
                    PageJumpUtil.pageJump(getContext(), LoginActivity.class, bundle);
                }
            }
        });
    }
}
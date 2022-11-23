package com.bitcnew.module.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bitcnew.R;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.PageJumpUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

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
        PageJumpUtil.pageJumpResult(SignUpEmailActivity.this, VerifyActivity.class, bundle);
    }
}
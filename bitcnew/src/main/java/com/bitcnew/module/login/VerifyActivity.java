package com.bitcnew.module.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.R;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.common.TJrLoginTypeEnum;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.MD5;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * Created by zhengmj on 18-10-11.
 */

public class VerifyActivity extends TJRBaseToolBarSwipeBackActivity {
    private final String TAG = "VerifyActivity";
    private TextView tv_login;
    private TextView tv_sign;
    private TextView tv_verify;
    private EditText et_verify;

    //    private SharedPreferences sharedata;
    private CountDownTimer countDownTimer;
    public static boolean isCounting;
    private Call<ResponseBody> smsCall;
    private Call<ResponseBody> registeCall;
    private long COUNT = 60000;
    //    private String from;
    private String phone;
    private String email;
    private String countryCode;
    private String key;
    private String pass;
    private String cpass;
    private String inviteCode;
    private boolean isFail;
    private int location;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_login_verify;
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey(SignUpActivity.UserPass))
                phone = bundle.getString(SignUpActivity.phoneParam);
            if (bundle.containsKey(SignUpEmailActivity.emailParam))
                email = bundle.getString(SignUpEmailActivity.emailParam);
            if (bundle.containsKey(SignUpActivity.UserPass))
                pass = bundle.getString(SignUpActivity.UserPass);
            if (bundle.containsKey(SignUpActivity.CUserPass))
                cpass = bundle.getString(SignUpActivity.CUserPass);
            if (bundle.containsKey(SignUpActivity.CountryCode))
                countryCode = bundle.getString(SignUpActivity.CountryCode);
            if (bundle.containsKey(SignUpActivity.INVITECODE))
                inviteCode = bundle.getString(SignUpActivity.INVITECODE);

//            if (bundle.containsKey(inviteCode))invite = bundle.getString(inviteCode);
            Log.d("200", "登录 bundle\n phone: " + phone + "\nemail: " + email + "\npass: " + pass + "\ncpass: " + cpass);
        }

        tv_login = (TextView) findViewById(R.id.tv_login);
//        if (!TextUtils.isEmpty(from) && from.equals("login")){
//            tv_login.setText("登录");
//        }
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                test();
//                if (!TextUtils.isEmpty(from) && from.equals("login")){
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Code",et_verify.getText().toString());
//                    intent.putExtras(bundle);
//                    setResult(RESULT_OK,intent);
//                    PageJumpUtil.finishCurr(VerifyActivity.this);
//                }else {
                startRegiste(key, location);
//                }
            }
        });
        tv_login.setEnabled(false);
        et_verify = (EditText) findViewById(R.id.et_verify);
        et_verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_login.setEnabled(!TextUtils.isEmpty(et_verify.getText()) && et_verify.getText().length() == 6);
            }
        });
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        tv_verify = (TextView) findViewById(R.id.tv_verify);
        et_verify = (EditText) findViewById(R.id.et_verify);
        et_verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_verify.setEnabled(!TextUtils.isEmpty(et_verify.getText()));
            }
        });
        tv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFail) {
                    if (isCounting) {
                        Counting();
                    } else {
//                        DragFragment dragFragment = DragFragment.newInstance(new DragFragment.OnSuccessCallback() {
//                            @Override
//                            public void onSuccess(String dragImgKey, int locationx) {
//                                key = dragImgKey;
//                                location = locationx;
                        startGetVCode(key, location);
//                            }
//                        });
//                        dragFragment.show(getSupportFragmentManager(),"");
                    }
                } else {
                    if (isCounting) {
                        Counting();
                    } else {
                        startGetVCode(key, location);
                    }
                }
            }
        });
    }

    public void startGetVCode(String key, int location) {
        CommonUtil.cancelCall(smsCall);

        Log.d("154", " phone == " + phone + " email ==" + email + "key == " + key + " location == " + location);

        if (phone != null && !phone.isEmpty()) {
            smsCall = VHttpServiceManager.getInstance().getVService().getSms(key, location,"android",1,countryCode,phone );
        } else {
            smsCall = VHttpServiceManager.getInstance().getVService().getSms(key,location,"android",2,"",email);
        }

        smsCall.enqueue(new MyCallBack(VerifyActivity.this, 2) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (!TextUtils.isEmpty(phone)) {
                        StringBuilder stringBuilder = new StringBuilder(phone);
                        stringBuilder.replace(3, 7, "****");
                        tv_sign.setText(getResources().getString(R.string.duanxinyanzhengmayijingfasongzhi) + stringBuilder.toString());
                    }
                    Counting();
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                CommonUtil.showmessage(getResources().getString(R.string.huoquchenggong), VerifyActivity.this);
                VerifyActivity.this.key = dragImgKey;
                VerifyActivity.this.location = locationx;
                startGetVCode(dragImgKey, locationx);
            }
        });
    }

    private void test() {
        Bundle testBundle = new Bundle();
        testBundle.putString(CommonConst.USERACCOUNT, "123123");
        testBundle.putString(CommonConst.PASSWORD, "qweasd");// password
        testBundle.putString(CommonConst.LOGIN_TYPE, TJrLoginTypeEnum.mb.type());
        testBundle.putBoolean(CommonConst.IS_LOGIN, true);
        testBundle.putString(CommonConst.MYINFO, "myInfo");
        PageJumpUtil.pageJump(VerifyActivity.this, LoginActivity.class, testBundle);
    }

    public void startRegiste(String key, int location) {
        CommonUtil.cancelCall(registeCall);
        if (phone != null && !phone.isEmpty()) {
            registeCall = VHttpServiceManager.getInstance().getVService().doRegistePhone(phone, countryCode, "", MD5.getMessageDigest(pass).toUpperCase(), MD5.getMessageDigest(cpass).toUpperCase(), inviteCode, 1, "", 0, "", et_verify.getText().toString(), key, location);
        } else {
            registeCall = VHttpServiceManager.getInstance().getVService().doRegisteEmail(
                    email,"", "", MD5.getMessageDigest(pass).toUpperCase(), MD5.getMessageDigest(cpass).toUpperCase(), inviteCode, 2, "", 0, "", et_verify.getText().toString(), key, location);
        }

        registeCall.enqueue(new MyCallBack(VerifyActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, VerifyActivity.this);
                    Bundle bundle = new Bundle();
                    String userAccount;
                    if (phone != null && !phone.isEmpty()) {
                        userAccount = phone;
                    } else {
                        userAccount = email;
                    }

                    bundle.putString(CommonConst.USERACCOUNT, userAccount);
                    bundle.putString(CommonConst.PASSWORD, pass);// password
                    bundle.putString(CommonConst.LOGIN_TYPE, TJrLoginTypeEnum.mb.type());
                    bundle.putBoolean(CommonConst.IS_LOGIN, true);
                    bundle.putString(CommonConst.MYINFO, getRawResult());
                    PageJumpUtil.pageJump(VerifyActivity.this, LoginActivity.class, bundle);
                } else {
                    isFail = true;
                    if (resultData.code == 40016) {
                        CommonUtil.showmessage(getResources().getString(R.string.qingchongxinhuoquyanzhengma), VerifyActivity.this);
                    }
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                VerifyActivity.this.key = dragImgKey;
                VerifyActivity.this.location = locationx;
                startRegiste(dragImgKey, locationx);
            }
        });
    }

    private void Counting() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                tv_verify.setEnabled(false);
                tv_verify.setText(getResources().getString(R.string.shengyu) + l / 1000 + getResources().getString(R.string.miao));
                isCounting = true;
            }

            @Override
            public void onFinish() {
                isCounting = false;
                tv_verify.setEnabled(true);
                tv_verify.setText(getResources().getString(R.string.chongxinhuoqu));
            }
        }.start();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_login, menu);
//        return true;
//    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.id.action_title);
//        item.setTitle("注册");
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_title){
////            PageJumpUtil.pageJump(VerifyActivity.this,SignUpActivity.class);
//        }
//        return super.onOptionsItemSelected(item);
//    }

}

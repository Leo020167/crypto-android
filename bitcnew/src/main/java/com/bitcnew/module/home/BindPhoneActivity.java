package com.bitcnew.module.home;

import android.content.Intent;
import android.os.CountDownTimer;
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
import com.bitcnew.module.login.AllCountryCodeListActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class BindPhoneActivity extends TJRBaseToolBarActivity implements View.OnClickListener{
    @BindView(R.id.tvCountryName)
    TextView tvCountryName;
    @BindView(R.id.tvCountryCode)
    TextView tvCountryCode;
    @BindView(R.id.et_newPhone)
    EditText et_newPhone;
    @BindView(R.id.et_newSms)
    EditText et_newSms;
    @BindView(R.id.tv_newVerfiy)
    TextView tv_newVerfiy;
    @BindView(R.id.txt_wancheng)
    TextView txt_wancheng;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ButterKnife.bind(this);
            tv_newVerfiy.setOnClickListener(this);
            tvCountryName.setOnClickListener(this);
            tvCountryCode.setOnClickListener(this);
            tvCountryCode.setText("+852");//默认中国
            tvCountryName.setText(getResources().getString(R.string.zhongguo));//默认中国
            txt_wancheng.setOnClickListener(this);
        }

        @Override
        protected int setLayoutId() {
            return R.layout.activity_bind_phone;
        }

        @Override
        protected String getActivityTitle() {
            return getResources().getString(R.string.qingshurushoujihaoma);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.txt_send_code:
                    if (TextUtils.isEmpty(et_newPhone.getText().toString())){
                        Toast.makeText(this,getResources().getString(R.string.qingshurushoujihaoma),Toast.LENGTH_LONG);
                        return;
                    }
                    startGetSms("", 0);
                    break;
                case R.id.tv_newVerfiy:
                    startGetSms("", 0);
                    break;
                case R.id.tvCountryCode:
                case R.id.tvCountryName:
                    PageJumpUtil.pageJumpResult(BindPhoneActivity.this, AllCountryCodeListActivity.class, new Intent(), 0x456);
                    break;
                case R.id.txt_wancheng:
                    if (TextUtils.isEmpty(et_newPhone.getText().toString())){
                        Toast.makeText(this,getResources().getString(R.string.qingshurushoujihaoma),Toast.LENGTH_LONG);
                        return;
                    }
                    if (TextUtils.isEmpty(et_newSms.getText().toString())){
                        Toast.makeText(this,getResources().getString(R.string.qingshuruyanzhengma),Toast.LENGTH_LONG);
                        return;
                    }
                    changePhone();
                    break;
            }
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x456) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String code = bundle.getString("code");
                String name = bundle.getString("name");

                if (!TextUtils.isEmpty(code)) {
                    tvCountryCode.setText(code);
                }
                if (!TextUtils.isEmpty(name)) {
                    tvCountryName.setText(name);
                }
            }
        }
    }


    private String dragImgKey = "";
    private int locationx = 0;
    private Call<ResponseBody> smsCall;
    public void startGetSms(String dragImgKey, int locationx) {
        if (TextUtils.isEmpty(et_newPhone.getText().toString())) {
            CommonUtil.showmessage(getResources().getString(R.string.qingshurushoujihaoma), BindPhoneActivity.this);
            return;
        }
        this.dragImgKey = dragImgKey;
        this.locationx = locationx;
        CommonUtil.cancelCall(smsCall);
        smsCall = VHttpServiceManager.getInstance().getVService().getSms(dragImgKey, locationx,"android",1,tvCountryCode.getText().toString(),et_newPhone.getText().toString() );
        smsCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(getResources().getString(R.string.duanxinyanzhengmayijingfasongzhi) + et_newPhone.getText().toString(), BindPhoneActivity.this);
                    Counting();
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                startGetSms(dragImgKey, locationx);
            }
        });
    }
    private CountDownTimer countDownTimer;
    private void Counting() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                tv_newVerfiy.setEnabled(false);
                tv_newVerfiy.setText(getResources().getString(R.string.shengyu) + l / 1000 + getResources().getString(R.string.miao));
            }

            @Override
            public void onFinish() {
                tv_newVerfiy.setEnabled(true);
                tv_newVerfiy.setText(getResources().getString(R.string.chongxinhuoqu));
            }
        };
        countDownTimer.start();
    }

    private Call<ResponseBody> changeCall;
    public void changePhone() {
        CommonUtil.cancelCall(changeCall);
        changeCall = VHttpServiceManager.getInstance().getVService().updatePhone(
                et_newPhone.getText().toString(),
                et_newSms.getText().toString());
        changeCall.enqueue(new MyCallBack(BindPhoneActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, BindPhoneActivity.this);
                    finish();
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                changePhone();
            }
        });
    }
}
package com.bitcnew.module.legal;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.web.CommonWebViewActivity;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.legal.entity.OtcCertification;
import com.bitcnew.module.login.SignUpActivity;
import com.bitcnew.module.myhome.IdentityAuthenActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AuthenticationActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvAuthenState)
    TextView tvAuthenState;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvCardNo)
    TextView tvCardNo;
    @BindView(R.id.tvBond)
    TextView tvBond;
    @BindView(R.id.llCb)
    LinearLayout llCb;
    @BindView(R.id.cbSign)
    CheckBox cbSign;
    @BindView(R.id.tvProtocol)
    TextView tvProtocol;
    @BindView(R.id.tvApply)
    TextView tvApply;
    @BindView(R.id.tvApplyCancel)
    TextView tvApplyCancel;
    @BindView(R.id.tvReason)
    TextView tvReason;
    @BindView(R.id.llReason)
    LinearLayout llReason;

    private Handler handler = new Handler();


    private Call<ResponseBody> otcGetCertificationInfoCall;
    private Call<ResponseBody> otcAuthenticateCall;
    private Call<ResponseBody> otcApplyForCancellationCall;


    @Override
    protected int setLayoutId() {
        return R.layout.authentication;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.shangjiarenzheng);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


        cbSign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tvApply.setEnabled(isChecked);
            }
        });

        tvApply.setOnClickListener(this);
        tvApplyCancel.setOnClickListener(this);

        startGetotcCertificationInfoCall();
    }


    TjrBaseDialog TipsDialog;

    private void showTipsDialog() {
        TipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                PageJumpUtil.pageJump(AuthenticationActivity.this, IdentityAuthenActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PageJumpUtil.finishCurr(AuthenticationActivity.this);
                    }
                }, 500);
            }

            @Override
            public void onclickClose() {
                dismiss();
                PageJumpUtil.finishCurr(AuthenticationActivity.this);
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        TipsDialog.setTvTitle(getResources().getString(R.string.weishimingbunengshenqingshangjia));
        TipsDialog.setMessage(getResources().getString(R.string.weishimingbunengshenqingshangjiarenzheng));
        TipsDialog.setBtnOkText(getResources().getString(R.string.qianwangshiming));
        TipsDialog.setBtnColseText(getResources().getString(R.string.quxiao));
        TipsDialog.show();
    }

    private void setProtocol(String bond) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString normalText = new SpannableString(getResources().getString(R.string.tongyidongjie));
        normalText.setSpan(new ForegroundColorSpan(Color.parseColor("#3d3a50")), 0, normalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString normalText2 = new SpannableString(bond + " USDT");
        normalText2.setSpan(new ForegroundColorSpan(Color.parseColor("#6175ae")), 0, normalText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString normalText3 = new SpannableString(getResources().getString(R.string.zuoweiguanggaobaozhengzichanqietongyi));
        normalText3.setSpan(new ForegroundColorSpan(Color.parseColor("#3d3a50")), 0, normalText3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString clickText = new SpannableString(getResources().getString(R.string.shanjiafuwuxieyi));
        clickText.setSpan(new ForegroundColorSpan(Color.parseColor("#6175ae")), 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                // 用户协议
                String lang = (String) SPUtils.get(getContext(),"myLanguage1","");
                if ("zh-cn".equalsIgnoreCase(lang)) {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(AuthenticationActivity.this, CommonConst.USER_PROTOCOL_CN);
                } else if ("zh-tw".equalsIgnoreCase(lang)) {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(AuthenticationActivity.this, CommonConst.USER_PROTOCOL_TW);
                } else {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(AuthenticationActivity.this, CommonConst.USER_PROTOCOL_OTHER);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(AuthenticationActivity.this, R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(normalText);
        spannableStringBuilder.append(normalText2);
        spannableStringBuilder.append(normalText3);
        spannableStringBuilder.append(clickText);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        tvProtocol.setText(spannableStringBuilder);
    }


    private void startGetotcCertificationInfoCall() {
        CommonUtil.cancelCall(otcGetCertificationInfoCall);
        otcGetCertificationInfoCall = VHttpServiceManager.getInstance().getVService().otcGetCertificationInfo();
        otcGetCertificationInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    OtcCertification otcCertification = resultData.getObject("otcCertification", OtcCertification.class);
                    if (otcCertification.idCertify == 0) {
                        tvAuthenState.setText(getResources().getString(R.string.weishiming));
                        tvApply.setVisibility(View.GONE);
                        tvApplyCancel.setVisibility(View.GONE);
                        llCb.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showTipsDialog();
                            }
                        }, 500);

                    } else {
                        tvAuthenState.setText(getResources().getString(R.string.yishiming));
                        setState(otcCertification);
                        setProtocol(otcCertification.securityDeposit);

                        if (!TextUtils.isEmpty(otcCertification.realName)) {
                            tvName.setText(otcCertification.realName);
                        }
                        if (!TextUtils.isEmpty(otcCertification.certNo)) {
                            tvCardNo.setText(otcCertification.certNo);
                        }
                        if (!TextUtils.isEmpty(otcCertification.securityDeposit)) {
                            tvBond.setText(otcCertification.securityDeposit + " USDT");
                        }

                    }


                }
            }

        });
    }


    private void startOtcAuthenticateCall() {
        CommonUtil.cancelCall(otcAuthenticateCall);
        otcAuthenticateCall = VHttpServiceManager.getInstance().getVService().otcAuthenticate();
        otcAuthenticateCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, AuthenticationActivity.this);
                    startGetotcCertificationInfoCall();
                }
            }

        });
    }


    private void startOtcApplyForCancellationCall() {
        CommonUtil.cancelCall(otcApplyForCancellationCall);
        otcApplyForCancellationCall = VHttpServiceManager.getInstance().getVService().otcApplyForCancellation();
        otcApplyForCancellationCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, AuthenticationActivity.this);
                    startGetotcCertificationInfoCall();
                }
            }

        });
    }

    //0未认证，1认证中，2认证通过，3申请取消认证，4取消认证通过，-1认证失败
    private String setState(OtcCertification otcCertification) {
        switch (otcCertification.state) {
            case 0:
            case 4:
            case -1:
                tvApply.setVisibility(View.VISIBLE);
                tvApplyCancel.setVisibility(View.GONE);
                tvApply.setText(getResources().getString(R.string.shenqingshangjiarenzheng));
                llCb.setVisibility(View.VISIBLE);
                tvApply.setEnabled(cbSign.isChecked());
                if (otcCertification.state == -1 && !TextUtils.isEmpty(otcCertification.reason)) {
                    llReason.setVisibility(View.VISIBLE);
                    tvReason.setText(otcCertification.reason);
                }
                break;
            case 1:
                llReason.setVisibility(View.GONE);
                tvApply.setVisibility(View.VISIBLE);
                tvApplyCancel.setVisibility(View.GONE);
                tvApply.setText(getResources().getString(R.string.renzhengzhong));
                llCb.setVisibility(View.GONE);
                tvApply.setEnabled(false);
                break;
            case 2:
                llReason.setVisibility(View.GONE);
                tvApply.setVisibility(View.GONE);
                tvApplyCancel.setVisibility(View.VISIBLE);
                tvApplyCancel.setText(getResources().getString(R.string.shenqingquxiaoshangjiarenzheng));
                llCb.setVisibility(View.GONE);
                tvApplyCancel.setEnabled(true);
                break;
            case 3:
                llReason.setVisibility(View.GONE);
                tvApply.setVisibility(View.GONE);
                tvApplyCancel.setVisibility(View.VISIBLE);
                tvApplyCancel.setText(getResources().getString(R.string.zhengzaishenqingquxiaoshangjiarenzheng));
                llCb.setVisibility(View.GONE);
                tvApplyCancel.setEnabled(false);
                break;
            default:
        }
        return "";
    }

    TjrBaseDialog cancelDialog;

    private void showCancelDialog() {
        cancelDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startOtcApplyForCancellationCall();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        cancelDialog.setMessage(getResources().getString(R.string.quedingshenqingquxiaoshangjiarenzheng));
        cancelDialog.setBtnOkText(getResources().getString(R.string.queding));
        cancelDialog.setBtnColseText(getResources().getString(R.string.quxiao));
        cancelDialog.setTitleVisibility(View.GONE);
        cancelDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvApply:
                startOtcAuthenticateCall();
                break;
            case R.id.tvApplyCancel:
                showCancelDialog();
                break;
        }
    }
}

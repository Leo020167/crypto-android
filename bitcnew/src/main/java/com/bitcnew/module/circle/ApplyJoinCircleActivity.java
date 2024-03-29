package com.bitcnew.module.circle;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.JsonParserUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.R;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 申请加入圈子
 */
public class ApplyJoinCircleActivity extends TJRBaseToolBarSwipeBackActivity {
    @BindView(R.id.etEdit)
    EditText etEdit;
    @BindView(R.id.tvApplyJoin)
    TextView tvApplyJoin;

    private String circleId;
    private Call<ResponseBody> applyJoinCirlceCall;
    private TjrBaseDialog applySucTipsDialog;

    private String reason = "";

    public static void pageJumpThis(Context context, String circleId) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.CIRCLEID, circleId);
        PageJumpUtil.pageJump(context, ApplyJoinCircleActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.circle_join_reason;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.shenqingjiaru);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            circleId = getIntent().getExtras().getString(CommonConst.CIRCLEID);
            if (TextUtils.isEmpty(circleId)) {
                String params = getIntent().getExtras().getString(CommonConst.PARAMS);
                if (!TextUtils.isEmpty(params)) {
                    try {
                        JSONObject jsonObject = new JSONObject(params);
                        if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.CIRCLEID)) {
                            circleId = jsonObject.getString(CommonConst.CIRCLEID);
                        }
                        if (JsonParserUtils.hasAndNotNull(jsonObject, "reason")) {
                            reason = jsonObject.getString("reason");
                        }
                    } catch (Exception e) {
                    }

                }
            }


            if (TextUtils.isEmpty(circleId)) {
                CommonUtil.showmessage(getResources().getString(R.string.canshucuowu), this);
                finish();
                return;
            }
        }
        ButterKnife.bind(this);
        tvApplyJoin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String reason = etEdit.getText().toString().trim();
                if (TextUtils.isEmpty(reason)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshuruneirong), ApplyJoinCircleActivity.this);
                    return;
                }
                startApplyJoinCirlceTask(circleId, reason);

            }
        });
        if (!TextUtils.isEmpty(reason)) {
            etEdit.append(reason);
            etEdit.setSelection(reason.length());
        }
    }


    private void startApplyJoinCirlceTask(String circleId, String reason) {
        com.bitcnew.http.util.CommonUtil.cancelCall(applyJoinCirlceCall);
        showProgressDialog();
        applyJoinCirlceCall = VHttpServiceManager.getInstance().getVService().applyJoinCirlce(circleId, reason);
        applyJoinCirlceCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    showTipsDialog(resultData.msg);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    private void showTipsDialog(String msg) {
        applySucTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                PageJumpUtil.finishCurr(ApplyJoinCircleActivity.this);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        applySucTipsDialog.setMessage(msg);
        applySucTipsDialog.setBtnOkText(getResources().getString(R.string.queding));
        applySucTipsDialog.setCloseVisibility(View.GONE);
        applySucTipsDialog.setTitleVisibility(View.GONE);
        applySucTipsDialog.show();
    }

}

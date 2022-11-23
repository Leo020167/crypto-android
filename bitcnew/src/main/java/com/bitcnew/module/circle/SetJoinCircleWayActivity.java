package com.bitcnew.module.circle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.R;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 设置入圈方式
 */
public class SetJoinCircleWayActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.cbWay1)
    RadioButton cbWay1;
    @BindView(R.id.cbWay2)
    RadioButton cbWay2;
    @BindView(R.id.rg)
    RadioGroup rg;
    private String circleId;
    private int joinMode;
    private Call<ResponseBody> applyJoinCirlceCall;


    public static void pageJumpThisForResult(Context context, String circleId, int joinMode) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.CIRCLEID, circleId);
        bundle.putInt(CommonConst.CIRCLEJOINMODE, joinMode);
        PageJumpUtil.pageJumpResult((Activity) context, SetJoinCircleWayActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.circle_join_way;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.shezhijiarufangshi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            circleId = getIntent().getExtras().getString(CommonConst.CIRCLEID);
            joinMode = getIntent().getExtras().getInt(CommonConst.CIRCLEJOINMODE);
            if (TextUtils.isEmpty(circleId)) {
                CommonUtil.showmessage(getResources().getString(R.string.canshucuowu), this);
                finish();
                return;
            }
        }
        ButterKnife.bind(this);
        if (joinMode == 0) {
            cbWay2.setChecked(true);
        } else if (joinMode == 1) {
            cbWay1.setChecked(true);
        }
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.cbWay1) {
                    startApplyJoinCirlceTask(circleId, 1);
                } else if (checkedId == R.id.cbWay2) {
                    startApplyJoinCirlceTask(circleId, 0);
                }
            }
        });
    }

    @Override
    public void finish() {
        Intent intent=new Intent();
        intent.putExtra(CommonConst.CIRCLEJOINMODE,joinMode);
        setResult(0x456,intent);
        super.finish();
    }

    private void startApplyJoinCirlceTask(String circleId, final int mode) {
        com.bitcnew.http.util.CommonUtil.cancelCall(applyJoinCirlceCall);
        applyJoinCirlceCall = VHttpServiceManager.getInstance().getVService().setupJoinMode(circleId, mode);
        applyJoinCirlceCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    joinMode = mode;
                }
            }
        });
    }

}

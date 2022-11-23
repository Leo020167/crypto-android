package com.bitcnew.module.legal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.retrofitservice.UploadFileUtils;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.legal.adapter.AppealListAdapter;
import com.bitcnew.module.myhome.MyhomeMultiSelectImageActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 申诉
 */
public class OtcAppealActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.rvReason)
    RecyclerView rvReason;
    @BindView(R.id.flAddPic1)
    FrameLayout flAddPic1;
    @BindView(R.id.flAddPic2)
    FrameLayout flAddPic2;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvSubmitAppeal)
    TextView tvSubmitAppeal;
    @BindView(R.id.ivPic1)
    ImageView ivPic1;
    @BindView(R.id.ivPic2)
    ImageView ivPic2;
    private Call<ResponseBody> otcGetInitAppealListCall, otcSubmitAppealCall;
    private String pic1;
    private String pic2;

    private Call<ResponseBody> uploadPic1Call;
    private Call<ResponseBody> uploadPic2Call;

    private long orderCashId;
    private AppealListAdapter appealListAdapter;

    private TjrImageLoaderUtil tjrImageLoaderUtil;


    public static void pageJump(Context context, long orderCashId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERCASHID, orderCashId);
        PageJumpUtil.pageJump(context, OtcAppealActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.appeal;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.shensu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ORDERCASHID)) {
                orderCashId = bundle.getLong(CommonConst.ORDERCASHID, 0l);
            }
        }
        if (orderCashId == 0l) {
            com.bitcnew.http.util.CommonUtil.showmessage(getResources().getString(R.string.canshucuowu), this);
            finish();
            return;
        }

        appealListAdapter = new AppealListAdapter(this);
        rvReason.setLayoutManager(new LinearLayoutManager(this));
//        rvReason.addItemDecoration(new SimpleRecycleDivider(this, true));
        rvReason.setAdapter(appealListAdapter);


        tjrImageLoaderUtil = new TjrImageLoaderUtil();

        flAddPic1.setOnClickListener(this);
        flAddPic2.setOnClickListener(this);
        tvSubmitAppeal.setOnClickListener(this);
        startOtcGetInitAppealListCall();

    }


    private void startOtcGetInitAppealListCall() {
        CommonUtil.cancelCall(otcGetInitAppealListCall);
        otcGetInitAppealListCall = VHttpServiceManager.getInstance().getVService().otcGetInitAppealList(orderCashId);
        otcGetInitAppealListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String[] str = resultData.getStringArray(getRawResult(), "data");
                    Log.d("resultData", "str==" + str.length);
                    appealListAdapter.setData(str);
                }
            }
        });
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            int type = intent.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, 0);
            ArrayList<String> flist = intent.getStringArrayListExtra(CommonConst.PICLIST);
            if (flist == null || flist.size() == 0) return;
            String file = flist.get(0);
            if (type == 1) {
                startUploadFront(file);
            } else if (type == 2) {
                startUploadBack(file);
            }
        }
    }

    private void startUploadFront(String file) {
        com.bitcnew.http.util.CommonUtil.cancelCall(uploadPic1Call);
        showProgressDialog();
        uploadPic1Call = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetCompressed", "userImage", UploadFileUtils.getImageFilesMap(file));
//        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, 1, 0, fileZone, UploadFileUtils.getFilesMap(bitmapFile.getAbsolutePath()));
        uploadPic1Call.enqueue(new MyCallBack(OtcAppealActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    String imageUrl = resultData.getItem("imageUrlList", String.class);
                    Log.d("120", "imageUrl == " + imageUrl);
                    try {
                        JSONArray jsonArray = new JSONArray(imageUrl);
                        String url = jsonArray.getString(0);
                        if (!TextUtils.isEmpty(url)) {
                            pic1 = url;
                            tjrImageLoaderUtil.displayImage(pic1, ivPic1);//原图要这样显示
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }


    private void startUploadBack(String file) {
        com.bitcnew.http.util.CommonUtil.cancelCall(uploadPic2Call);
        showProgressDialog();
        uploadPic2Call = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetCompressed", "identityImage", UploadFileUtils.getImageFilesMap(file));
        uploadPic2Call.enqueue(new MyCallBack(OtcAppealActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    String imageUrl = resultData.getItem("imageUrlList", String.class);
                    Log.d("120", "imageUrl == " + imageUrl);
                    try {
                        JSONArray jsonArray = new JSONArray(imageUrl);
                        String url = jsonArray.getString(0);
                        if (!TextUtils.isEmpty(url)) {
                            pic2 = url;
                            tjrImageLoaderUtil.displayImage(pic2, ivPic2);//原图要这样显示
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    private void startOtcSubmitAppealCall(String reason, String content) {
        CommonUtil.cancelCall(otcSubmitAppealCall);
        otcSubmitAppealCall = VHttpServiceManager.getInstance().getVService().otcSubmitAppeal(orderCashId, reason, pic1, pic2, content);
        otcSubmitAppealCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, OtcAppealActivity.this);
                    PageJumpUtil.finishCurr(OtcAppealActivity.this);
                }
            }
        });
    }

    TjrBaseDialog alertDialog;

    private void showAlertDialog(final String reason,final String content) {
        alertDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startOtcSubmitAppealCall(reason, content);
            }
            @Override
            public void onclickClose() {
                dismiss();
            }
            @Override
            public void setDownProgress(int progress) {
            }
        };
        alertDialog.setTitleVisibility(View.VISIBLE);
        alertDialog.setTvTitle(getResources().getString(R.string.shensutixing));
        alertDialog.setMessage(getString(R.string.appealMessage));
        alertDialog.setBtnOkText(getResources().getString(R.string.shensu));
        alertDialog.setBtnColseText(getResources().getString(R.string.quxiao));
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmitAppeal:
                if (appealListAdapter == null) return;
                String reason = appealListAdapter.getCurSelected();
                if (TextUtils.isEmpty(reason)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingxuanzeshensuliyou), OtcAppealActivity.this);
                    return;
                }
                String content = etContent.getText().toString().trim();
                if (content.length() > 300) {
                    CommonUtil.showmessage(getResources().getString(R.string.liuyanbunengchaoguosanbaigezifu), OtcAppealActivity.this);
                    return;
                }
                showAlertDialog(reason,content);

                break;
            case R.id.flAddPic1:
                MyhomeMultiSelectImageActivity.pageJumpThis(OtcAppealActivity.this, 1, true, false, OtcAppealActivity.class.getName(), 1, false);//需要设置android:launchMode="singleTask"
                break;
            case R.id.flAddPic2:
                MyhomeMultiSelectImageActivity.pageJumpThis(OtcAppealActivity.this, 1, true, false, OtcAppealActivity.class.getName(), 2, false);
                break;

        }
    }
}

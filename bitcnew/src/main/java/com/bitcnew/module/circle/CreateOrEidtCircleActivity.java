package com.bitcnew.module.circle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.circle.entity.CircleRoleEnum;
import com.bitcnew.module.myhome.MyhomeMultiSelectImageActivity;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.module.circle.entity.CircleInfo;
import com.bitcnew.R;
import com.bitcnew.http.retrofitservice.UploadFileUtils;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.http.util.TjrImageLoaderUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 创建圈子
 * Created by zhengmj on 18-11-14.
 */

public class CreateOrEidtCircleActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.etTitle)
    EditText etTitle;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvCreate)
    TextView tvCreate;

    @BindView(R.id.ivSelectHead)
    ImageView ivSelectHead;

    @BindView(R.id.llSelectHead)
    LinearLayout llSelectHead;

    @BindView(R.id.flConver)
    FrameLayout flConver;
    @BindView(R.id.ivSelectCover)
    ImageView ivSelectCover;

    private String circleHeadPath = "";
    private String circleConverPath = "";
    private CircleInfo circleInfo;
    private String circleId = "";

    private boolean isEdit = false;

    private TjrImageLoaderUtil tjrImageLoaderUtil;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_create_community;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.chuangjianquanzi);
    }


    public static void pageJumpThis(Context context, CircleInfo circleInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConst.CIRCLEINFO, circleInfo);
        PageJumpUtil.pageJumpResult((Activity) context, CreateOrEidtCircleActivity.class, bundle);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            ArrayList<String> flist = intent.getStringArrayListExtra(CommonConst.PICLIST);
            if (flist != null && flist.size() > 0) {
                int type = intent.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, 0);
                if (type == 0) {
                    circleConverPath = flist.get(0);
                    ivSelectCover.setImageURI(Uri.fromFile(new File(circleConverPath)));
                } else if (type == 1) {
                    circleHeadPath = flist.get(0);
                    ivSelectHead.setImageURI(Uri.fromFile(new File(circleHeadPath)));
                }

            }

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_community);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.CIRCLEINFO)) {
                circleInfo = bundle.getParcelable(CommonConst.CIRCLEINFO);
            }
            if (bundle.containsKey(CommonConst.CIRCLEID)) {
                circleId = bundle.getString(CommonConst.CIRCLEID);
            }
        }
        isEdit = circleInfo != null && !TextUtils.isEmpty(circleInfo.circleId);
        ButterKnife.bind(this);
        llSelectHead.setOnClickListener(this);
        tvCreate.setOnClickListener(this);
        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        if (isEdit) {
            mActionBar.setTitle(getResources().getString(R.string.bianjiquanzi));
            flConver.setVisibility(View.VISIBLE);
            ivSelectCover.setOnClickListener(this);
            tjrImageLoaderUtil.displayImage(circleInfo.circleBg, ivSelectCover);
            tjrImageLoaderUtil.displayImage(circleInfo.circleLogo, ivSelectHead);
            etTitle.setText(circleInfo.circleName);
            etContent.setText(circleInfo.brief);
            tvCreate.setText(getResources().getString(R.string.wancheng));
        } else {
            mActionBar.setTitle(getResources().getString(R.string.chuangjianquanzi));
            tvCreate.setText(getResources().getString(R.string.chuangjian));
            flConver.setVisibility(View.GONE);
        }
    }

    private Call<ResponseBody> uploadCall;
    private Call<ResponseBody> createCommnuityCall;

    //上传图片后在创建TOKA
    private void startUpload(final String communityName, final String brief, final String... picPaths) {
        showProgressDialog();
        com.bitcnew.util.CommonUtil.cancelCall(uploadCall);
        Log.d("startUpload", "communityLogo==" + circleConverPath + "   circleHead==" + circleHeadPath);
        uploadCall = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetCompressed", "circle", UploadFileUtils.getImageFilesMap(picPaths));
        uploadCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
//                        String logo = resultData.getItem("imgUrl", String.class);
//                        startCreateCommunity(communityName,brief,logo);
                    String[] urlList = resultData.getStringArray("imageUrlList");
                    Log.d("startUpload", "urlList==" + urlList[0]);
                    if (isEdit && circleInfo != null) {
                        if (urlList.length == 2) {
                            startUpdateCommunity(circleInfo.circleId, communityName, brief, urlList[0], urlList[1]);
                        } else {
                            if (!TextUtils.isEmpty(circleConverPath)) {
                                startUpdateCommunity(circleInfo.circleId, communityName, brief, circleInfo.circleLogo, urlList[0]);
                            } else {
                                startUpdateCommunity(circleInfo.circleId, communityName, brief, urlList[0], circleInfo.circleBg);
                            }
                        }
                    } else {
                        if (urlList != null && urlList.length > 0) {
                            startCreateCommunity(communityName, brief, urlList[0]);
                        }
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

    private void startCreateCommunity(String communityName, String useBrief, String communityLogo) {
        createCommnuityCall = VHttpServiceManager.getInstance().getVService().createCommunity(communityName, useBrief, communityLogo);
        createCommnuityCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, CreateOrEidtCircleActivity.this);
                    CircleInfo circleInfo = resultData.getObject("circle", CircleInfo.class);
                    Log.d("CreateCommunity", "circleInfo==" + circleInfo.toString());
                    long result = getApplicationContext().getmDb().insertMyCircle(circleInfo);
                    getApplicationContext().getmDb().insertAndUpdateCircleRel(circleInfo.circleId, Long.parseLong(getUserId()), CircleRoleEnum.root.role());
                    Log.d("CreateCommunity", "result==" + result);
                    PageJumpUtil.finishCurr(CreateOrEidtCircleActivity.this);
//                    setResult(0x456);
//                    finish();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    private void startUpdateCommunity(String circleId, final String communityName, final String useBrief, final String circleLogo, final String circleBg) {
        createCommnuityCall = VHttpServiceManager.getInstance().getVService().updateCommunity(circleId, communityName, useBrief, circleLogo, circleBg);
        createCommnuityCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, CreateOrEidtCircleActivity.this);
                    if (circleInfo != null) {
                        circleInfo.circleName = communityName;
                        circleInfo.brief = useBrief;
                        circleInfo.circleLogo = circleLogo;
                        circleInfo.circleBg = circleBg;
                        long result = getApplicationContext().getmDb().insertMyCircle(circleInfo);
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(CommonConst.CIRCLEINFO, circleInfo);
                        intent.putExtras(bundle);
                        setResult(0x123, intent);
                    }
                    PageJumpUtil.finishCurr(CreateOrEidtCircleActivity.this);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ivSelectCover:
                MyhomeMultiSelectImageActivity.pageJumpThis(this, 1, true, false, CreateOrEidtCircleActivity.class.getName(), 0);
                break;
            case R.id.llSelectHead:
                MyhomeMultiSelectImageActivity.pageJumpThis(this, 1, true, false, CreateOrEidtCircleActivity.class.getName(), 1);
                break;
            case R.id.tvCreate:
                String communityName = etTitle.getText().toString().trim();
                if (TextUtils.isEmpty(communityName)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshuruquanzimingzi), this);
                    return;
                }
                if (CommonUtil.getGbkStringLength(communityName, 12)) {
                    CommonUtil.showmessage(getResources().getString(R.string.quanzimingzitaichangle), this);
                    return;
                }

                String communityBrife = etContent.getText().toString().trim();
                if (TextUtils.isEmpty(communityBrife)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshuruquanzijianjie), this);
                    return;
                }
                if (isEdit) {
                    if (circleInfo == null) return;
                    if (TextUtils.isEmpty(circleHeadPath) && TextUtils.isEmpty(circleConverPath)) {
                        startUpdateCommunity(circleInfo.circleId, communityName, communityBrife, circleInfo.circleLogo, circleInfo.circleBg);
                    } else {
                        startUpload(communityName, communityBrife, circleHeadPath, circleConverPath);
                    }

                } else {
                    if (TextUtils.isEmpty(circleHeadPath)) {
                        CommonUtil.showmessage(getResources().getString(R.string.qingshangchuanquanzitouxiang), this);
                        return;
                    }
                    startUpload(communityName, communityBrife, circleHeadPath);
                }

                break;
        }
    }
}

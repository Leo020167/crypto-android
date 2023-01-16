package com.bitcnew.module.myhome;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.home.TakePhotoActivity;
import com.bitcnew.module.myhome.entity.IdentityAuthen;
import com.bitcnew.util.DynamicPermission;
import com.bitcnew.util.ImageUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.PermissionUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.bitcnew.http.retrofitservice.UploadFileUtils;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.R;

import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 实名认证页面
 */
public class IdentityAuthenActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etNo)
    EditText etNo;
    @BindView(R.id.ivFrontImg)
    ImageView ivFrontImg;
    @BindView(R.id.ivFrontTake)
    ImageView ivFrontTake;
    @BindView(R.id.ivBackImg)
    ImageView ivBackImg;
    @BindView(R.id.ivBackTake)
    ImageView ivBackTake;
    @BindView(R.id.ivHoldImg)
    ImageView ivHoldImg;
    @BindView(R.id.ivHoldTake)
    ImageView ivHoldTake;
    @BindView(R.id.tvComplete)
    TextView tvComplete;
    @BindView(R.id.tvState)
    TextView tvState;

    @BindView(R.id.tvViewDemoFrontImg)
    TextView tvViewDemoFrontImg;
    @BindView(R.id.tvViewDemoBackImg)
    TextView tvViewDemoBackImg;
    @BindView(R.id.tvViewHoldImgImg)
    TextView tvViewHoldImgImg;


    //这个3个才是上传的路径
    private String frontImgFile;
    private String backImgFile;
//    private String holdImgFile;

    //这个3个是拍照路径
    private String frontTake;
    private String backTake;
//    private String holdTake;


    private Call<ResponseBody> getIdentityAuthenCall;
    private Call<ResponseBody> uploadFrontCall;
    private Call<ResponseBody> uploadBackCall;
    private Call<ResponseBody> submitIdentityAuthenCall;
    private IdentityAuthen identityAuthen;

    private DynamicPermission dynamicPermission;
    private TjrImageLoaderUtil tjrImageLoaderUtil;

//    private ArrayList<String> viewDemoUrls;

    protected DisplayImageOptions imageOptions;//显示原图用到

    @Override
    protected int setLayoutId() {
        return R.layout.activity_identity_authen;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.identityAuthen);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_common_mic2)
                .showImageOnFail(R.drawable.ic_common_mic2)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(false)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ivFrontTake.setOnClickListener(this);
        ivBackTake.setOnClickListener(this);
        ivHoldTake.setOnClickListener(this);
        tvComplete.setOnClickListener(this);
        tvViewDemoFrontImg.setOnClickListener(this);
        tvViewDemoBackImg.setOnClickListener(this);
        tvViewHoldImgImg.setOnClickListener(this);


        startGetIdentityAuthen();
    }

    private void startGetIdentityAuthen() {
        getIdentityAuthenCall = VHttpServiceManager.getInstance().getVService().getIdentityAuthen();
        getIdentityAuthenCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
//                    String frontImgDemoUrl = resultData.getItem("frontImgDemoUrl", String.class);
//                    String backImgDemoUrl = resultData.getItem("backImgDemoUrl", String.class);
//                    String holdImgDemoUrl = resultData.getItem("holdImgDemoUrl", String.class);
//                    if (!TextUtils.isEmpty(frontImgDemoUrl) && !TextUtils.isEmpty(backImgDemoUrl) && !TextUtils.isEmpty(holdImgDemoUrl)) {
//                        viewDemoUrls = new ArrayList<>();
//                        viewDemoUrls.add(frontImgDemoUrl);
//                        viewDemoUrls.add(backImgDemoUrl);
//                        viewDemoUrls.add(holdImgDemoUrl);
//                    }

                    identityAuthen = resultData.getObject("identityAuth", IdentityAuthen.class);

                    if (identityAuthen == null) {//还没有提交过
                        tvState.setVisibility(View.GONE);
                    } else {
                        setData();
                    }
                }

            }
        });
    }

    private void setData() {
        if (identityAuthen != null) {
            etName.setText(identityAuthen.name);
            etNo.setText(identityAuthen.certNo);
            tvState.setVisibility(View.VISIBLE);

            tjrImageLoaderUtil.displayImage(identityAuthen.frontImgUrl, ivFrontImg);
            tjrImageLoaderUtil.displayImage(identityAuthen.backImgUrl, ivBackImg);
//            tjrImageLoaderUtil.displayImage(identityAuthen.images.holdImgUrl, ivHoldImg);

            if (identityAuthen.state == 0 || identityAuthen.state == 1) {
                etName.setEnabled(false);
                etNo.setEnabled(false);
                ivFrontTake.setVisibility(View.GONE);
                ivBackTake.setVisibility(View.GONE);
                ivHoldTake.setVisibility(View.GONE);
                tvComplete.setEnabled(false);
                tvState.setText(getResources().getString(R.string.zhuangtai)+": " + identityAuthen.stateDesc);

            } else if (identityAuthen.state == 2) {
                etName.setEnabled(true);
                etNo.setEnabled(true);
                ivFrontTake.setVisibility(View.VISIBLE);
                ivBackTake.setVisibility(View.VISIBLE);
                ivHoldTake.setVisibility(View.VISIBLE);
                tvComplete.setEnabled(true);
                tvState.setText(getResources().getString(R.string.zhuangtai)+": " + identityAuthen.stateDesc + "(" + getResources().getString(R.string.canshucuowu)+": " + identityAuthen.checkMsg + ")");
            }


        }

    }

    private void initDynamicPermission() {
        if (dynamicPermission == null) {
            dynamicPermission = new DynamicPermission(this, new DynamicPermission.RequestPermissionsCallBack() {
                @Override
                public void onRequestSuccess(String[] permissions, int requestCode) {
                    if (requestCode == 100) {
//                        frontTake = ImageUtil.getNewPhotoPath();
//                        Log.d("initDynamicPermission","frontTake=="+frontTake);
//                        Intent intent = ImageUtil.takeBigPicture(IdentityAuthenActivity.this, frontTake);
//                        startActivityForResult(intent, 0x123);

//                        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//用来打开相机的Intent
//                        if(takePhotoIntent.resolveActivity(getPackageManager())!=null){//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
//                            startActivityForResult(takePhotoIntent,0x1231);//启动相机
//                        }

                        requestCamera(TAKE_PHOTO);

                    } else if (requestCode == 200) {
//                        backTake = ImageUtil.getNewPhotoPath();
//                        Intent intent = ImageUtil.takeBigPicture(IdentityAuthenActivity.this, backTake);
//                        startActivityForResult(intent, 0x456);

//                        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//用来打开相机的Intent
//                        if(takePhotoIntent.resolveActivity(getPackageManager())!=null){//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
//                            startActivityForResult(takePhotoIntent,0x4561);//启动相机
//                        }

                        requestCamera2(TAKE_PHOTO2);

                    } else if (requestCode == 300) {
//                        holdTake = ImageUtil.getNewPhotoPath();
//                        Intent intent = ImageUtil.takeBigPicture(IdentityAuthenActivity.this, holdTake);
//                        startActivityForResult(intent, 0x789);
                    }

                }

                @Override
                public void onRequestFail(String[] permissions, int requestCode) {
                }
            });
        }
    }

    private File outputImage,outputImage2;
    private Uri imageUri,imageUri2;
    public static final int TAKE_PHOTO = 1,TAKE_PHOTO2=2;//声明一个请求码，用于识别返回的结果
    public static final int REQ_PHOTO1 = 11,REQ_PHOTO2=12;//声明一个请求码，用于识别返回的结果
    private void requestCamera(int toke_photo) {
////创建file对象，用于储存拍照后的图片，getExternalCacheDir() : 将照片存放在手机的关联缓存目录下
//        outputImage = new File(getExternalCacheDir(), "output_image.jpg");
//        try {
//            if (outputImage.exists()) {
//                outputImage.delete();
//            }
//            outputImage.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (Build.VERSION.SDK_INT >= 24) {
////如果系统版本大于7.0，调用FileProvider的getUriForFile()方法将File对象转换成一个封装过的Uri对象
////getUriForFile()参数说明
////第一个参数：Context对象
////第二个参数：任意唯一的字符串
////第三个参数：创建的File对象
//            imageUri = FileProvider.getUriForFile(IdentityAuthenActivity.this, getPackageName() + ".fileprovider", outputImage);
//        } else {
////否则，调用Uri的fromFile()方法将File对象转换成Uri对象
//            imageUri = Uri.fromFile(outputImage);
//        }
////启动相机程序
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
////设定图片的输出地址
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
////使用startActivityForResult()启动Intent时，Intent结束后会有结果返回到onActivityResult()方法中。
//        startActivityForResult(intent, toke_photo);

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
//                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .minSelectNum(1)
                .freeStyleCropEnabled(true) // 裁切框可拖动
                .circleDimmedLayer(false) // 圆形头像裁切
                .showCropFrame(true) // 是否显示裁剪矩形边框
                .showCropGrid(true) // 是否显示裁剪矩形网格
                .selectionMode(PictureConfig.SINGLE)
//                .isSingleDirectReturn(true)
//                .isAndroidQTransform(true)
                .isGif(false)
                .forResult(REQ_PHOTO1);
    }
    private void requestCamera2(int toke_photo) {
////创建file对象，用于储存拍照后的图片，getExternalCacheDir() : 将照片存放在手机的关联缓存目录下
//        outputImage2 = new File(getExternalCacheDir(), "output_image2.jpg");
//        try {
//            if (outputImage2.exists()) {
//                outputImage2.delete();
//            }
//            outputImage2.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (Build.VERSION.SDK_INT >= 24) {
////如果系统版本大于7.0，调用FileProvider的getUriForFile()方法将File对象转换成一个封装过的Uri对象
////getUriForFile()参数说明
////第一个参数：Context对象
////第二个参数：任意唯一的字符串
////第三个参数：创建的File对象
//            imageUri2 = FileProvider.getUriForFile(IdentityAuthenActivity.this, getPackageName() + ".fileprovider", outputImage2);
//        } else {
////否则，调用Uri的fromFile()方法将File对象转换成Uri对象
//            imageUri2 = Uri.fromFile(outputImage2);
//        }
////启动相机程序
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
////设定图片的输出地址
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri2);
////使用startActivityForResult()启动Intent时，Intent结束后会有结果返回到onActivityResult()方法中。
//        startActivityForResult(intent, toke_photo);

        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
//                .imageEngine(GlideEngine.createGlideEngine())
                .maxSelectNum(1)
                .minSelectNum(1)
                .freeStyleCropEnabled(true) // 裁切框可拖动
                .circleDimmedLayer(false) // 圆形头像裁切
                .showCropFrame(true) // 是否显示裁剪矩形边框
                .showCropGrid(true) // 是否显示裁剪矩形网格
                .selectionMode(PictureConfig.SINGLE)
//                .isSingleDirectReturn(true)
//                .isAndroidQTransform(true)
                .isGif(false)
                .forResult(REQ_PHOTO2);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (dynamicPermission != null)
            dynamicPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivFrontTake:
                initDynamicPermission();
                dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_EXTERNAL_STORAGE, 100);
//                MyhomeMultiSelectImageActivity.pageJumpThis(IdentityAuthenActivity.this, 1, true, false, IdentityAuthenActivity.class.getName(), 1, true);//需要设置android:launchMode="singleTask"
                break;
            case R.id.ivBackTake:
                initDynamicPermission();
                dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_EXTERNAL_STORAGE, 200);
//                MyhomeMultiSelectImageActivity.pageJumpThis(IdentityAuthenActivity.this, 1, true, false, IdentityAuthenActivity.class.getName(), 2, true);
                break;
//            case R.id.ivHoldTake:
//                initDynamicPermission();
//                dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_EXTERNAL_STORAGE, 300);
//                MyhomeMultiSelectImageActivity.pageJumpThis(IdentityAuthenActivity.this, 1, true, false, IdentityAuthenActivity.class.getName(), 3,true);
//                break;
            case R.id.tvComplete:
                String name = etName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshurushenfenzhengxingming), IdentityAuthenActivity.this);
                    return;
                }
                String no = etNo.getText().toString().trim();
                if (TextUtils.isEmpty(no)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshurushenfenzhenghaoma), IdentityAuthenActivity.this);
                    return;
                }
//                String invidateNo = IDCardValidate.validate_effective(no, false);
//                if (!invidateNo.equals(no)) {
//                    CommonUtil.showmessage(invidateNo, IdentityAuthenActivity.this);
//                    return;
//                }
                if (TextUtils.isEmpty(frontImgFile)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshangchuanrenxiangzhao), IdentityAuthenActivity.this);
                    return;
                }

                if (TextUtils.isEmpty(backImgFile)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshangchuanguohuizhao), IdentityAuthenActivity.this);
                    return;
                }
//                if (TextUtils.isEmpty(holdImgFile) || !new File(holdImgFile).exists()) {
//                    CommonUtil.showmessage("请上传手持身份证照", IdentityAuthenActivity.this);
//                    return;
//                }
//                startUpload(name, no);
                startSubmitIdentityAuthen(name, no, frontImgFile, backImgFile);
                break;
//            case R.id.tvViewDemoFrontImg:
//                goViewPagerPhotoViewActivity(0);
//                break;
//            case R.id.tvViewDemoBackImg:
//                goViewPagerPhotoViewActivity(1);
//                break;
//            case R.id.tvViewHoldImgImg:
//                goViewPagerPhotoViewActivity(2);
//                break;
        }
    }


//    private void goViewPagerPhotoViewActivity(int pos) {
//        if (viewDemoUrls != null && viewDemoUrls.size() == 3) {
//            Bundle bundle = new Bundle();
//            bundle.putStringArrayList("imageUrls", viewDemoUrls);
//            bundle.putInt(CommonConst.DEFAULTPOS, pos);
//            bundle.putInt(CommonConst.PAGETYPE, 5);
//            PageJumpUtil.pageJump(this, ViewPagerPhotoViewActivity.class, bundle);
//        }
//
//    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            int type = intent.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, 0);
            ArrayList<String> flist = intent.getStringArrayListExtra(CommonConst.PICLIST);
            if (flist == null || flist.size() == 0) return;
            String file = flist.get(0);
            if (type == 1) {
//                String frontImgFile = file;
//                ivFrontImg.setImageURI(Uri.fromFile(new File(frontImgFile)));
//                tjrImageLoaderUtil.displayImage("file://" + file, ivFrontImg, imageOptions);//原图要这样显示
//                startUploadFront(file);
            } else if (type == 2) {
//                String backImgFile = file;
//                ivBackImg.setImageURI(Uri.fromFile(new File(backImgFile)));
//                tjrImageLoaderUtil.displayImage("file://" + file, ivBackImg, imageOptions);
//                startUploadBack(file);
            } else if (type == 3) {
//                holdImgFile = file;
//                ivHoldImg.setImageURI(Uri.fromFile(new File(holdImgFile)));
//                tjrImageLoaderUtil.displayImage("file://" + file, ivHoldImg, imageOptions);
            }
        }
    }

    private void startUploadFront(String file) {
        CommonUtil.cancelCall(uploadFrontCall);
        showProgressDialog();
        uploadFrontCall = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetCompressed", "userImage", UploadFileUtils.getImageFilesMap(file));
//        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, 1, 0, fileZone, UploadFileUtils.getFilesMap(bitmapFile.getAbsolutePath()));
        uploadFrontCall.enqueue(new MyCallBack(IdentityAuthenActivity.this) {
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("120", "imageUrl == onFailure");
            }
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
                            frontImgFile = url;
                            tjrImageLoaderUtil.displayImage(frontImgFile, ivFrontImg);//原图要这样显示
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
        CommonUtil.cancelCall(uploadBackCall);
        showProgressDialog();
        uploadBackCall = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetCompressed", "identityImage", UploadFileUtils.getImageFilesMap(file));
        uploadBackCall.enqueue(new MyCallBack(IdentityAuthenActivity.this) {
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                Log.d("120", "imageUrl == onFailure");
            }

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
                            backImgFile = url;
                            tjrImageLoaderUtil.displayImage(backImgFile, ivBackImg);//原图要这样显示
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
//如果拍照成功
                    try {
//将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        ivFrontImg.setImageBitmap(bitmap);
                        startUploadFront(outputImage.getAbsolutePath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case TAKE_PHOTO2:
                if (resultCode == RESULT_OK) {
//如果拍照成功
                    try {
//将拍摄的照片显示出来
                        Bitmap bitmap2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri2));
                        ivBackImg.setImageBitmap(bitmap2);
                        startUploadBack(outputImage2.getAbsolutePath());
//                        File f1 = saveBitmapFile(bitmap);
//                        try {
//                            File f2 = CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmap2, true, getUserId());
//                            startUploadBack(f2.getAbsolutePath());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
//        switch (requestCode) {
//            case 0x1231:
//                try {
////                    Log.d("onActivityResult"," frontTake=="+frontTake);
////                    Bitmap bitmapOfFile = CommonUtil.getSmallBitmap(frontTake, true);
////                    File f = CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmapOfFile, true, getUserId());
////                    startUploadFront(f.getAbsolutePath());
//////                    frontImgFile = f.getAbsolutePath();
//////                    ivFrontImg.setImageURI(Uri.fromFile(new File(frontImgFile)));
//
//
//                    if(resultCode==RESULT_OK){
//                        /*缩略图信息是储存在返回的intent中的Bundle中的，
//                         * 对应Bundle中的键为data，因此从Intent中取出
//                         * Bundle再根据data取出来Bitmap即可*/
//                        Bundle extras = data.getExtras();
//                        Bitmap bitmap = (Bitmap) extras.get("data");
////                        ivFrontImg.setImageBitmap(bitmap);
//                        File f1 = saveBitmapFile(bitmap);
////                        File f = CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmap, true, getUserId());
//                        startUploadFront(f1.getAbsolutePath());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.d("onActivityResult","e=="+e);
//                }
//                break;
//            case 0x4561:
//                try {
////                    Bitmap bitmapOfFile = CommonUtil.getSmallBitmap(backTake, true);
////                    File f = CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmapOfFile, true, getUserId());
////                    startUploadBack(f.getAbsolutePath());
//////                    backImgFile = f.getAbsolutePath();
//////                    ivBackImg.setImageURI(Uri.fromFile(new File(backImgFile)));
//
//                    if(resultCode==RESULT_OK){
//                        /*缩略图信息是储存在返回的intent中的Bundle中的，
//                         * 对应Bundle中的键为data，因此从Intent中取出
//                         * Bundle再根据data取出来Bitmap即可*/
//                        Bundle extras = data.getExtras();
//                        Bitmap bitmap = (Bitmap) extras.get("data");
////                        ivBackImg.setImageBitmap(bitmap);
//
//                        File f1 = saveBitmapFile(bitmap);
////                        File f = CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmap, true, getUserId());
//                        startUploadBack(f1.getAbsolutePath());
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
////            case 0x789:
////                try {
////                    Bitmap bitmapOfFile = CommonUtil.getSmallBitmap(holdTake, true);
////                    File f = CommonUtil.saveBitmapToFile(getApplicationContext().getRemoteResourceChatManager(), bitmapOfFile, true, getUserId());
////                    holdImgFile = f.getAbsolutePath();
////                    ivHoldImg.setImageURI(Uri.fromFile(new File(holdImgFile)));
////                } catch (Exception e) {
////
////                }
////
////                break;
//
//        }

        if (REQ_PHOTO1 == requestCode && RESULT_OK == resultCode) {
            List<Uri> list = obtainChooseImage(data);
            if (!list.isEmpty()) {
                try {
                    Uri photo = list.get(0);
                    ivFrontImg.setImageURI(photo);

                    File file = getFileFromUri(getContext(), photo);
                    startUploadFront(file.getAbsolutePath());
                } catch (Exception e) {
                    Log.e("", e.getMessage(), e);
                }
            }
        } else if (REQ_PHOTO2 == requestCode) {
            List<Uri> list = obtainChooseImage(data);
            if (!list.isEmpty()) {
                try {
                    Uri photo = list.get(0);
                    ivBackImg.setImageURI(photo);
                    File file = getFileFromUri(getContext(), photo);
                    startUploadBack(file.getAbsolutePath());
                } catch (Exception e) {
                    Log.e("", e.getMessage(), e);
                }
            }
        }
    }

    private static File getFileFromUri(Context context, Uri uri) {
        String schema = uri.getScheme();

        if (ContentResolver.SCHEME_CONTENT.equals(schema)) {
            return ImageUtil.getFileFromContentUri(context, uri);
        }

        if (ContentResolver.SCHEME_FILE.equals(schema) || null == schema) {
            return new File(uri.getPath());
        }

        return null;
    }

    public static List<Uri> obtainChooseImage(Intent data) {
        List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
        if (null == selectList) {
            return new ArrayList<>(0);
        }
        List<Uri> list = new ArrayList<>(selectList.size());
        for (LocalMedia media : selectList) {
            String path;
            if (media.isCut() && !media.isCompressed()) {
                // 裁剪过
                path = media.getCutPath();
            } else if (media.isCompressed() || (media.isCut() && media.isCompressed())) {
                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                path = media.getCompressPath();
            } else {
                // 原图
                path = media.getPath();
            }
            list.add(Uri.parse(path));
        }
        return list;
    }

    public File saveBitmapFile(Bitmap bitmap){
        File file=new File("/storage/emulated/0/procoin/chat/"+System.currentTimeMillis()+".jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

//    //上传图片
//    private void startUpload(final String name, final String no) {
//        showProgressDialog();
//        CommonUtil.cancelCall(uploadCall);
//        uploadCall = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, 1, 5, FileZoneEnum.IMAGE_IDENTITY.type, UploadFileUtils.getIdentityAuthen(frontImgFile, backImgFile, holdImgFile));
//        uploadCall.enqueue(new MyCallBack(IdentityAuthenActivity.this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    String frontUrl = resultData.getItem("imgUrl", String.class);
//                    String backUrl = resultData.getItem("otherUrl", String.class);
////                    String holdUrl = resultData.getItem("videoUrl", String.class);
//                    startSubmitIdentityAuthen(name, no, frontUrl, backUrl);
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                dismissProgressDialog();
//
//            }
//        });
//    }

    private void startSubmitIdentityAuthen(String name, String no, String frontUrl, String backUrl) {
        CommonUtil.cancelCall(submitIdentityAuthenCall);
        submitIdentityAuthenCall = VHttpServiceManager.getInstance().getVService().submitIdentityAuthen(name, no, frontUrl, backUrl);
        submitIdentityAuthenCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    showSuccessDialog(resultData.msg);
                }

            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                dismissProgressDialog();
            }
        });
    }

    TjrBaseDialog successDialog;

    private void showSuccessDialog(String msg) {
        if (successDialog == null) {
            successDialog = new TjrBaseDialog(this) {
                @Override
                public void onclickOk() {
                    dismiss();
                    PageJumpUtil.finishCurr(IdentityAuthenActivity.this);
                }

                @Override
                public void onclickClose() {
                    dismiss();
                }

                @Override
                public void setDownProgress(int progress) {

                }
            };
        }
        successDialog.setTvTitle(getResources().getString(R.string.tishi));
        successDialog.setMessage(msg);
        successDialog.setBtnColseVisibility(View.GONE);
        successDialog.show();


    }
}

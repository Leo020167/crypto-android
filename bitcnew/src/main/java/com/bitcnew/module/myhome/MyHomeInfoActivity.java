package com.bitcnew.module.myhome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.myhome.dialog.TimeSelectDialogFragment;
import com.bitcnew.widgets.CircleImageView;
import com.bitcnew.widgets.dialog.TjrBase2Dialog;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.photo.ViewPagerPhotoViewActivity;
import com.bitcnew.util.DateUtils;
import com.bitcnew.util.MatchesUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.http.model.User;
import com.bitcnew.http.resource.BaseRemoteResourceManager;
import com.bitcnew.http.retrofitservice.UploadFileUtils;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.ShareData;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.social.util.CommentWatcher;
import com.bitcnew.social.util.CommonUtil;
import com.bitcnew.R;
import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;


@SuppressLint("NewApi")
public class MyHomeInfoActivity extends TJRBaseToolBarSwipeBackActivity {

    public final static int EDIT_MYHOME_INFO = 0x911;

    @BindView(R.id.ivOlstarHead)
    CircleImageView ivOlstarHead;
    @BindView(R.id.rlUserInfo)
    LinearLayout rlUserInfo;
    //    @BindView(R.id.tvUserRealName)
//    TextView tvUserRealName;
//    @BindView(R.id.tvIdCertify)
//    TextView tvIdCertify;
//    @BindView(R.id.ivArrow)
//    ImageView ivArrow;
//    @BindView(R.id.rlVerify)
//    LinearLayout rlVerify;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.rlNameinfo)
    LinearLayout rlNameinfo;
    @BindView(R.id.tvID)
    TextView tvID;
    @BindView(R.id.tvSextitle)
    TextView tvSextitle;
    @BindView(R.id.tv_boy)
    TextView tvBoy;
    @BindView(R.id.tv_girl)
    TextView tvGirl;
    @BindView(R.id.llQRCode)
    LinearLayout llQRCode;
    @BindView(R.id.tvBirthDay)
    TextView tvBirthDay;
    @BindView(R.id.llBirthday)
    LinearLayout llBirthday;
    @BindView(R.id.llDescribes)
    LinearLayout llDescribes;
    @BindView(R.id.tvDescribes)
    TextView tvDescribes;
//    @BindView(R.id.iv_bg)
//    ImageView iv_bg;
//    @BindView(R.id.personalBg)
//    LinearLayout personalBg;

    protected TjrImageLoaderUtil mTjrImageLoaderUtil;
    private BaseRemoteResourceManager mRrm;
    private User user;
    // 上传头像使用
    private final int NONE = 0;
    private final int PHOTORESOULT = 3;// 结果
    // 上传头像结束
    private File bitmapFile;
    private int hasFile;// type=0代表没头像上传,type=1代表有头像上传的
    private boolean needFirsh;// 是否
    //上传背景图片
    private File bgFile;
    private boolean uploadHeadOK;
    private boolean uploadBgOK;
//    private String strImageName;

    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private MyhomeInfoOnclick onclick;
    private CommentWatcher commentWatcher;
    private String isSex = "";


    private String uploadHeadurl;//头像上传后后台返回的头像路径
    private Call<ResponseBody> callUploadFile;
    private Call<ResponseBody> callUpdateUserInfo;

    private InputMethodManager im;

    private TjrBase2Dialog nameDialog;
    private String uploadBgurl;
    private Call<ResponseBody> uploadBgCall;

    @Override
    protected int setLayoutId() {
        return R.layout.myhomepage_myinfo;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.gerenshezhi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.myhomepage_myinfo);
        ButterKnife.bind(this);
        mTjrImageLoaderUtil = new TjrImageLoaderUtil();
        user = getApplicationContext().getUser();
        if (user == null) {
            onBackPressed();
            com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.canshucuowu), MyHomeInfoActivity.this);
            return;
        }
        tjrImageLoaderUtil = new TjrImageLoaderUtil();

        mRrm = getApplicationContext().getRemoteResourceChatManager();
        showView();
        setData();

    }

    /**
     * 保存按钮可用
     *
     * @author zhengmj
     */
    protected void commitbutton() {
        needFirsh = true;
    }

    private void uncommitbutton() {
        needFirsh = false;
    }

    /**
     * 设置用户的头像
     */
    private void setData() {
        if (user == null) return;
        tvName.setText(user.getUserName());// 名字
        tvID.setText(String.valueOf(user.getUserId()));
        if (MatchesUtil.isMatchesIntOrLong(String.valueOf(user.getSex()))) {
            if (user.getSex() == 0) {
                tvBoy.setSelected(true);
                tvGirl.setSelected(false);
                isSex = "0";
            } else {
                tvBoy.setSelected(false);
                tvGirl.setSelected(true);
                isSex = "1";
            }
        }
        tvBirthDay.setText(user.getBirthday());
        tvDescribes.setText(user.getDescribes());
        showHeadIcon(ivOlstarHead, user.getHeadUrl());
//        tjrImageLoaderUtil.displayImage(user.getBgUrl(),iv_bg);
        ivOlstarHead.setOnClickListener(onclick);
//        CommonUtil.setIdCertifyText(tvIdCertify,user.getIdCertify(),true);
//        if(user.getIdCertify()==1){
//            tvUserRealName.setText(user.getUserRealName());
//        }else{
//            tvUserRealName.setText("");
//        }

        uncommitbutton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showView() {
        onclick = new MyhomeInfoOnclick();
        commentWatcher = new CommentWatcher(6, null);
        rlUserInfo.setOnClickListener(onclick);
        rlNameinfo.setOnClickListener(onclick);
        tvBoy.setOnClickListener(onclick);
        tvGirl.setOnClickListener(onclick);
        llBirthday.setOnClickListener(onclick);
        llDescribes.setOnClickListener(onclick);
        tvDescribes.setOnClickListener(onclick);
//        rlVerify.setOnClickListener(onclick);
//        personalBg.setOnClickListener(onclick);

        uncommitbutton();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, -1) == 0) {
                ArrayList<String> flist = intent.getStringArrayListExtra(CommonConst.PICLIST);
                if (flist != null && flist.size() > 0) {
                    String filePath = flist.get(0);
                    tjrImageLoaderUtil.displayImageForHead("file://" + filePath, ivOlstarHead);
                    bitmapFile = new File(filePath);
                    hasFile += 1;
                    Log.d("CropActivity", "bitmapFile=" + bitmapFile.getAbsolutePath());
                    ivOlstarHead.setOnClickListener(onclick);
                    commitbutton();
                }
            }
//            else if (intent.getIntExtra(CommonConst.KEY_EXTRAS_TYPE,-1) == 1){
//                ArrayList<String> flist = intent.getStringArrayListExtra(CommonConst.PICLIST);
//                if (flist != null && flist.size() > 0) {
//                    String filePath = flist.get(0);
//                    tjrImageLoaderUtil.displayImage("file://" + filePath, iv_bg);
//                    bgFile = new File(filePath);
//                    hasFile += 1;
//                    Log.d("CropActivity", "bitmapFile=" + bgFile.getAbsolutePath());
//                    commitbutton();
//                }
//            }

        }
    }

    private class MyhomeInfoOnclick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.personalBg:
//                    MyhomeMultiSelectImageActivity.pageJumpThis(MyHomeInfoActivity.this, 1, true, true, MyHomeInfoActivity.class.getName(),1);
//                    break;
                case R.id.rlUserInfo:
//                    MyhomeMultiSelectImageActivity.pageJumpThis(MyHomeInfoActivity.this, 1, true, true, MyHomeInfoActivity.class.getName());
                    pickImage();
                    break;
                case R.id.btnBack:
                    finish();
                    break;
                case R.id.ivOlstarHead:
                    if (user != null) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(CommonConst.PAGETYPE, 0);
                        bundle.putString(CommonConst.SINGLEPICSTRING, user.getHeadUrl());
                        PageJumpUtil.pageJumpToData(MyHomeInfoActivity.this, ViewPagerPhotoViewActivity.class, bundle);
                    }
                    break;
                case R.id.rlNameinfo:
                    nameDialog = new TjrBase2Dialog(MyHomeInfoActivity.this, tvName.getText().toString()) {
                        @Override
                        public void onclickOk() {
                            String myname = this.tvEdit.getText().toString();
                            try {
                                if (myname.getBytes("GBK").length < 2) {
                                    com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.yonghumingguoduan), MyHomeInfoActivity.this);
                                    return;
                                }
                                if (myname.getBytes("GBK").length > 12) {
                                    com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.yonghumingguochang), MyHomeInfoActivity.this);
                                    return;
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            if (!TextUtils.isEmpty(myname)) {
                                if (commentWatcher.getNumber()) {
                                    tvName.setText(myname);
                                    commitbutton();
                                    closeSoftKeyboard();
                                    this.dismiss();
                                    // 关闭对话框
                                } else {
                                    com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.qingshuruyidaoliugezhongming), MyHomeInfoActivity.this);
                                    return;
                                }
                            } else {
                                com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.qingshurumingzi), MyHomeInfoActivity.this);
                                return;
                            }
                        }

                        @Override
                        public void onclickClose() {
                            closeSoftKeyboard();
                            this.dismiss();
                        }

                        @Override
                        public void setDownProgress(int progress) {
                        }
                    };
                    nameDialog.tvTitle.setVisibility(View.GONE);
                    nameDialog.setBtnOkText(getResources().getString(R.string.queding));
                    nameDialog.setBtnCloseText(getResources().getString(R.string.quxiao));
                    nameDialog.setMaxLines(1);
                    nameDialog.setInputType(InputType.TYPE_CLASS_TEXT);
                    nameDialog.show();
                    break;
                case R.id.tv_boy:
                    tvBoy.setSelected(false);
                    tvGirl.setSelected(true);
                    if (isSex.equals("1")) {
                        Log.d("ddd", getResources().getString(R.string.weixiugai));
                    } else {
                        isSex = "1";
                        Log.d("ddd isSex = ", isSex);
                        commitbutton();
                    }
                    break;
                case R.id.tv_girl:
                    tvBoy.setSelected(true);
                    tvGirl.setSelected(false);
                    if (isSex.equals("0")) {
                        Log.d("ddd", getResources().getString(R.string.weixiugai));
                    } else {
                        isSex = "0";
                        Log.d("ddd isSex = ", isSex);
                        commitbutton();

                    }
                    break;
                case R.id.llBirthday:
                    showDatePicker(llBirthday, tvBirthDay.getText().toString().trim());
                    break;
                case R.id.llDescribes:
                case R.id.tvDescribes:
                    createMyInfo();
                    break;
//                case R.id.rlVerify:
//                    PageJumpUtil.pageJump(MyHomeInfoActivity.this, IdentityAuthenActivity.class);
//                    break;
            }

        }
    }
    private void pickImage() {
        //动态申请权限
        if (ContextCompat.checkSelfPermission(MyHomeInfoActivity.this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MyHomeInfoActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            //执行启动相册的方法
            openAlbum();
        }
    }
    //启动相册的方法
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED) openAlbum();
            else Toast.makeText(MyHomeInfoActivity.this,"Sie haben diese Erlaubnis verweigert.",Toast.LENGTH_SHORT).show();
        }
    }
    TimeSelectDialogFragment timeSelectDialogFragment;

    private void showDatePicker(View v, String birthDay) {
        Date date = null;
        if (TextUtils.isEmpty(birthDay)) {
            date = new Date();
        } else {
            date = DateUtils.strdate2Date(birthDay, DateUtils.TEMPLATE_yyyyMMdd_divide);
            if (date == null) date = new Date();
        }
        if (timeSelectDialogFragment == null) {
            timeSelectDialogFragment = TimeSelectDialogFragment.newInstance(new TimeSelectDialogFragment.OnDateButtonClickListener() {
                @Override
                public void onSureClick(Date result, String year, String month, String day) {
                    String date = DateUtils.date2strdate(result, DateUtils.TEMPLATE_yyyyMMdd_divide);
                    if (date == null) date = "";
                    tvBirthDay.setText(date);
                    if (!date.equals(user.getBirthday())) {
                        commitbutton();
                    }
                }

            });
        }
        timeSelectDialogFragment.setShowDate(date);
        timeSelectDialogFragment.show(getSupportFragmentManager(), "");

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x252) {
            if (data != null && data.getStringExtra(CommonConst.MYINFO) != null) {
                needFirsh = true;
                tvDescribes.setText(data.getStringExtra(CommonConst.MYINFO));
            }
        }else if (requestCode == 2){
            //判断安卓版本
            if (resultCode == RESULT_OK&&data!=null){
                if (Build.VERSION.SDK_INT>=19)
                    handImage(data);
                else handImageLow(data);
            }
        }
    }
    //安卓版本大于4.4的处理方法
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handImage(Intent data){
        String path =null;
        Uri uri = data.getData();
        //根据不同的uri进行不同的解析
        if (DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID+"="+id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                path = getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            path = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            path = uri.getPath();
        }
        //展示图片
        displayImage(path);
    }


    //安卓小于4.4的处理方法
    private void handImageLow(Intent data){
        Uri uri = data.getData();
        String path = getImagePath(uri,null);
        displayImage(path);
    }

    //content类型的uri获取图片路径的方法
    private String getImagePath(Uri uri,String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    private Uri mAvatarUrl;
    //根据路径展示图片的方法
    private void displayImage(String imagePath){
        if (imagePath != null){
//            tjrImageLoaderUtil.displayImageForHead("file://" + imagePath, ivOlstarHead);
            bitmapFile = new File(imagePath);
            hasFile += 1;
            Log.d("CropActivity", "bitmapFile=" + bitmapFile.getAbsolutePath());
            ivOlstarHead.setOnClickListener(onclick);
            commitbutton();
            mAvatarUrl = Uri.fromFile(bitmapFile);
            Glide.with(this).load(mAvatarUrl).into(ivOlstarHead);
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            ivOlstarHead.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"fail to set image",Toast.LENGTH_SHORT).show();
        }
    }




    private void createMyInfo() {
        Intent intent = new Intent();
        intent.putExtra(CommonConst.MYINFO, tvDescribes.getText().toString().trim());
        PageJumpUtil.pageJumpResult(this, MyhomeInfoTextActivity.class, intent);
    }

    private void startUpdateUserInfo() {
        if (hasFile > 0) {//如果修改了头像先上传，获得头像的路径后在更新user信息
            if (bitmapFile != null) {
                startUploadUserHead();
            }
//            if (bgFile!=null){
//                startUploadBgHead();
//            }
        } else {
            startRealUpdateUserInfo();
        }
    }

    private void startUploadUserHead() {
        CommonUtil.cancelCall(callUploadFile);
//        int fileZone = 0;
//            fileZone = FileZoneEnum.IMAGE_HEAD.type;
        showProgressDialog();
        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetCompressed", "identityImage", UploadFileUtils.getImageFilesMap(bitmapFile.getAbsolutePath()));
//        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, 1, 0, fileZone, UploadFileUtils.getFilesMap(bitmapFile.getAbsolutePath()));
        callUploadFile.enqueue(new MyCallBack(MyHomeInfoActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    uploadHeadurl = resultData.getItem("imageUrlList", String.class);
                    Log.d("120", "uploadHeadurl == " + uploadHeadurl);
                    try {
                        JSONArray jsonArray = new JSONArray(uploadHeadurl);
                        String url = jsonArray.getString(0);
                        if (!TextUtils.isEmpty(url)) {
                            uploadHeadurl = url;
                            uploadHeadOK = true;
                            startRealUpdateUserInfo();
                        }
                    } catch (Exception e) {

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
//    private void startUploadBgHead(){
//        CommonUtil.cancelCall(uploadBgCall);
//        int fileZone = FileZoneEnum.IMAGE_HEAD.type;
//        uploadBgCall = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL,
//                1,
//                0,
//                fileZone,
//                UploadFileUtils.getFilesMap(bgFile.getAbsolutePath()));
//        uploadBgCall.enqueue(new MyCallBack(MyHomeInfoActivity.this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                uploadBgurl = resultData.getItem("imgUrl",String.class);
//                if (!TextUtils.isEmpty(uploadBgurl)) {
//                    uploadBgOK = true;
//                    startRealUpdateUserInfo();
//                }
//            }
//        });
//
//    }

    private void startRealUpdateUserInfo() {
        CommonUtil.cancelCall(callUpdateUserInfo);
        if (hasFile == 2 && (!uploadBgOK || !uploadHeadOK)) {
            return;
        }
        String modifyName = tvName.getText().toString().trim();
        String modifyDescribes = tvDescribes.getText().toString().trim();
        String modifyBirthDay = tvBirthDay.getText().toString().trim();
        callUpdateUserInfo = VHttpServiceManager
                .getInstance()
                .getVService()
                .updateUserInfo(modifyName,
                        isSex,
                        modifyBirthDay,
                        modifyDescribes,
                        TextUtils.isEmpty(uploadHeadurl) ? user.getHeadUrl() : uploadHeadurl,
                        TextUtils.isEmpty(uploadBgurl) ? user.getBgUrl() : uploadBgurl);
        callUpdateUserInfo.enqueue(new MyCallBack(MyHomeInfoActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Log.d("MyHomeInfo", getResources().getString(R.string.gengxinxinxichenggong));
                    if (resultData.isSuccess()) {
                        User u = null;
                        try {
                            u = resultData.getObject(User.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("MyHomeInfo", "u==" + u.toString());
                        if (u == null) return;
                        user.setBgUrl(u.getBgUrl());
                        user.setBirthday(u.getBirthday());
                        user.setHeadUrl(u.getHeadUrl());
                        user.setUserName(u.getUserName());
                        user.setDescribes(u.getDescribes());
                        user.setSex(u.getSex());
                        com.bitcnew.util.CommonUtil.showmessage(getResources().getString(R.string.gengxinxinxichenggong), MyHomeInfoActivity.this);
                        ShareData.saveUser(ShareData.getUserSharedPreferences(MyHomeInfoActivity.this), null, null, null, null, user);
                        getApplicationContext().setUser(user);
                        uncommitbutton();
                        finish();
                    }

                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.finish_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                if (needFirsh) {
                    startUpdateUserInfo();
                } else {
                    finish();
                }
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    TjrBaseDialog quitConfirmDialog;

    @Override
    public void onBackPressed() {
        if (needFirsh) {
            if (quitConfirmDialog == null) {
                quitConfirmDialog = new TjrBaseDialog(this) {
                    @Override
                    public void setDownProgress(int progress) {
                    }

                    @Override
                    public void onclickOk() {
                        quitConfirmDialog.dismiss();
                        startUpdateUserInfo();
                    }

                    @Override
                    public void onclickClose() {
                        quitConfirmDialog.dismiss();
                        MyHomeInfoActivity.super.onBackPressed();
                    }
                };
            }
            quitConfirmDialog.setTvTitle(getResources().getString(R.string.tishi));
            quitConfirmDialog.setMessage(getResources().getString(R.string.nindeziliaoyijingxiugaishifoubaocun));
            if (!quitConfirmDialog.isShowing()) quitConfirmDialog.show();
        } else {
            super.onBackPressed();
        }
    }

    private void showHeadIcon(final CircleImageView civ, String url) {
        mTjrImageLoaderUtil.displayImageForHead(url, civ);
    }

    public void closeSoftKeyboard() {

        if (nameDialog.getCurrentFocus() != null && nameDialog.getCurrentFocus().getWindowToken() != null) {
            if (im == null)
                im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(nameDialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

}

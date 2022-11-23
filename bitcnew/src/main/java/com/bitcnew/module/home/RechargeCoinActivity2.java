package com.bitcnew.module.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.common.ConfigTjrInfo;
import com.bitcnew.http.retrofitservice.UploadFileUtils;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class RechargeCoinActivity2 extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.et_jine)
    EditText et_jine;
    @BindView(R.id.txt_shangchuan)
    TextView txt_shangchuan;
    @BindView(R.id.img_tu)
    ImageView img_tu;

    private String chainType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        txt_shangchuan.setOnClickListener(this);
        img_tu.setOnClickListener(this);
        tvMenu.setText(getResources().getString(R.string.tijiao));
        tvMenu.setOnClickListener(this);

        chainType = getIntent().getStringExtra("chainType");
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_recharge_coin2;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.chongzhi2);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_shangchuan) {
            pickImage();
        } else if (view.getId() == R.id.img_tu) {
//            pickImage();
        } else if (view.getId() == R.id.tvMenu) {
            if (TextUtils.isEmpty(et_jine.getText().toString().trim())) {
                Toast.makeText(this, getResources().getString(R.string.qingshuruchongzhijine), Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(uploadHeadurl)) {
                Toast.makeText(this, "請先上傳充值截圖", Toast.LENGTH_LONG).show();
                return;
            }
            startGetInfo();
        }
    }


    private File bitmapFile;
    private Call<ResponseBody> getGetInfoCall;
    private void startGetInfo() {
        CommonUtil.cancelCall(getGetInfoCall);
        long userId = Long.parseLong(ConfigTjrInfo.getInstance().getUserId());
        getGetInfoCall = VHttpServiceManager.getInstance().getVService().getWithdrawLocalSubmit(userId,et_jine.getText().toString().trim(), "", uploadHeadurl, 1, chainType);
        getGetInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Toast.makeText(RechargeCoinActivity2.this, "提交申請成功", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }


    private String uploadHeadurl;
    private Call<ResponseBody> callUploadFile;
    private void startUploadUserHead() {
        CommonUtil.cancelCall(callUploadFile);
        showProgressDialog();
        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetOriginal", "common", UploadFileUtils.getImageFilesMap(bitmapFile.getAbsolutePath()));
//        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, 1, 0, fileZone, UploadFileUtils.getFilesMap(bitmapFile.getAbsolutePath()));
        callUploadFile.enqueue(new MyCallBack(RechargeCoinActivity2.this) {
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
                            Glide.with(RechargeCoinActivity2.this).load(uploadHeadurl).into(img_tu);
                            txt_shangchuan.setVisibility(View.GONE);
                            img_tu.setVisibility(View.VISIBLE);
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


    private void pickImage() {
        //动态申请权限
        if (ContextCompat.checkSelfPermission(RechargeCoinActivity2.this,Manifest.permission
                .WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(RechargeCoinActivity2.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
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
            else Toast.makeText(RechargeCoinActivity2.this,"你拒絕了那個許可。",Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<String> mSelectPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2){
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

    //根据路径展示图片的方法
    private void displayImage(String imagePath){
        if (imagePath != null){
            bitmapFile = new File(imagePath);
            startUploadUserHead();
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            imageView.setImageBitmap(bitmap);
        }else{
            Toast.makeText(this,"無法設定影像",Toast.LENGTH_SHORT).show();
        }
    }
}
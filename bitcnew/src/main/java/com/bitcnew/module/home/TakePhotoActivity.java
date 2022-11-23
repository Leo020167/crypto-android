package com.bitcnew.module.home;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitcnew.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TakePhotoActivity extends AppCompatActivity {
    public static final int TAKE_PHOTO = 1;
    private ImageView picture,picture2;
    private Uri imageUri;
    public static final int CHOOSE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        Button takePhoto = (Button) findViewById(R.id.take_photo);

        picture = (ImageView) findViewById(R.id.picture);
        picture2 = (ImageView) findViewById(R.id.picture2);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {


            }

        });

//从相册中选择照片逻辑

        final Button chooseFromAlbum = (Button) findViewById(R.id.choose_from_album);

        chooseFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
//如果未授权

                if (ContextCompat.checkSelfPermission(TakePhotoActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//发起授权询问

                    ActivityCompat.requestPermissions(TakePhotoActivity.this, new String[]{ WRITE_EXTERNAL_STORAGE }, 1);

                } else {
//如果已授权，直接打开相册

                    openAlbum();

                }

            }

        });

    }

///打开相册的方法

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");

        intent.setType("image/*");

        startActivityForResult(intent, CHOOSE_PHOTO);

    }

///用户选择完授权询问后调用

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//如果用户选择授权，打开相册

                    openAlbum();

                } else {
//如果用户拒绝，弹出提示框

                    Toast.makeText(this, "获取相册失败，你拒绝了授权请求", Toast.LENGTH_SHORT).show();

                }

                break;

            default:

                break;

        }

    }

//Activity跳转之后再返回时调用

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:

                if (resultCode == RESULT_OK) {
//如果拍照成功

                    try {
//将拍摄的照片显示出来

                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

                        picture.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    }

                }

                break;

            case CHOOSE_PHOTO:

                if (resultCode == RESULT_OK) {
//判断手机系统版本号

                    if (Build.VERSION.SDK_INT >= 19) {
//4.4及以上系统使用这个方法处理图片

                        handleImageOnKitKat(data);

                    } else {
//4.4及以下系统使用这个方法处理图片

                        handleImageBeforeKitkat(data);

                    }

                }

            default:

                break;

        }

    }

    @TargetApi(19)

    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;

        Uri uri = data.getData();

        if (DocumentsContract.isDocumentUri(this, uri)) {
//如果是document类型的Uri,则通过document id处理

            String docId = DocumentsContract.getDocumentId(uri);

            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; //解析出数字格式的id

                String selection = MediaStore.Images.Media._ID + "=" + id;

                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            } else if ("com.android.provides.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));

                imagePath = getImagePath(contentUri, null);

            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//如果是content类型的Uri，则使用普通方式处理

            imagePath = getImagePath(uri, null);

        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//如果是file类型的Uri，直接获取图片的路径即可

            imagePath = uri.getPath();

        }

//根据图片路径显示图片

        displayImage(imagePath);

    }

    private void handleImageBeforeKitkat(Intent data) {
        Uri uri = data.getData();

        String imagePath = getImagePath(uri, null);

        displayImage(imagePath);

    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;

//通过Uri和selection来获取真实的图片路径

        Cursor cursor = getContentResolver().query(uri, null, selection,null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            }

            cursor.close();

        }

        return path;

    }

//设置图片

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            picture2.setImageBitmap(bitmap);

        } else {
            Toast.makeText(this,"获取图片失败",Toast.LENGTH_SHORT).show();

        }

    }

}
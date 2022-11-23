package com.bitcnew.module.home.trade;


import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcnew.module.home.BindPhoneActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.common.ConfigTjrInfo;
import com.bitcnew.http.retrofitservice.UploadFileUtils;
import com.bitcnew.module.home.BindEmailActivity;
import com.bitcnew.module.home.PhoneAuthCodeActivity;
import com.bitcnew.module.home.adapter.TakeCoinAdapter;
import com.bitcnew.module.home.bean.WithdrawGetInfoBean;
import com.bitcnew.module.home.trade.history.TakeCoinHistoryActivity;
import com.bitcnew.util.DensityUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.R;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.widgets.SimpleSpaceItemDecoration;

import org.json.JSONArray;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.MultiImageSelector;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提币
 */
public class TakeCoinActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.llSelectCoin)
    LinearLayout llSelectCoin;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvSymbol2)
    TextView tvSymbol2;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.etFee)
    TextView etFee;
    @BindView(R.id.tvSymbol3)
    TextView tvSymbol3;
    @BindView(R.id.tvScope)
    TextView tvScope;
    @BindView(R.id.tvLastAmount)
    TextView tvLastAmount;
    @BindView(R.id.tvTakeCoin)
    TextView tvTakeCoin;
    @BindView(R.id.tvMinWithdrawAmt)
    TextView tvMinWithdrawAmt;
    @BindView(R.id.tvEnableAmount)
    TextView tvEnableAmount;

    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.llKeyType)
    LinearLayout llKeyType;
    @BindView(R.id.rvKeyType)
    RecyclerView rvKeyType;
    @BindView(R.id.txt_erweima)
    TextView txt_erweima;
    @BindView(R.id.img_erweima)
    ImageView img_erweima;



    private TakeCoinAdapter adapter;
    private int amount_decimalcount = 8;//数量的小数点数量

    @Override
    protected int setLayoutId() {
        return R.layout.take_coin;
    }

    public static void pageJump(Context context, String defaultSymbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, defaultSymbol);
        PageJumpUtil.pageJump(context, TakeCoinActivity.class, bundle);
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.tibi);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetMyHomeCallCall();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        String defaultSymbol = "";
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                defaultSymbol = bundle.getString(CommonConst.SYMBOL, "");
            }
        }

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int posDot = s.toString().indexOf(".");
                if (0 == posDot) {//去除首位的"."
                    s.delete(0, 1);
                } else if (posDot > 0) {
                    if (s.length() - 1 - posDot > amount_decimalcount) {
                        s.delete(posDot + (amount_decimalcount + 1), posDot + (amount_decimalcount + 2));
                    }
                }
                setLastAmountText(s.toString());
            }
        });
        llSelectCoin.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        txt_erweima.setOnClickListener(this);
        img_erweima.setOnClickListener(this);
        tvTakeCoin.setOnClickListener(this);
        tvMenu.setText(getResources().getString(R.string.jilu));
        tvMenu.setOnClickListener(this);
        adapter = new TakeCoinAdapter(this,list);
        rvKeyType.setLayoutManager(new GridLayoutManager(this, 3));
        rvKeyType.addItemDecoration(new SimpleSpaceItemDecoration(this, 0, 5, 5, 5));
        rvKeyType.setAdapter(adapter);
        adapter.setOnPlayClickListener(new TakeCoinAdapter.OnPlayClickListener() {
            @Override
            public void onSelClick(int pos) {
                fee = list.get(pos).getFee();
                chainType = list.get(pos).getType();
                etFee.setText(""+fee);
            }
        });

        startGetInfo();
    }

    private List<WithdrawGetInfoBean.Infos> list = new ArrayList<>();
    private Call<ResponseBody> getGetInfoCall;
    private void startGetInfo() {
        CommonUtil.cancelCall(getGetInfoCall);
        getGetInfoCall = VHttpServiceManager.getInstance().getVService().withdrawGetInfo();
        getGetInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    WithdrawGetInfoBean bean = gson.fromJson(resultData.data,WithdrawGetInfoBean.class);
                    if (null!=bean){
                        balance = bean.getBalance();
                        tvEnableAmount.setText(getResources().getString(R.string.ketibishuliang)+balance);
                        if (null!=bean.getInfos()&&bean.getInfos().size()>0){
                            list.clear();
                            list.addAll(bean.getInfos());
                            list.get(0).setSel(true);
                            fee = list.get(0).getFee();
                            chainType = list.get(0).getType();
                            adapter.notifyDataSetChanged();
                            etFee.setText(""+fee);
                        }
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAll:
                etAmount.setText(""+balance);
                etAmount.setSelection(etAmount.getText().length());
                break;
            case R.id.tvMenu:
                PageJumpUtil.pageJump(TakeCoinActivity.this, TakeCoinHistoryActivity.class);
                break;
            case R.id.txt_erweima:
                pickImage();
                break;
            case R.id.img_erweima:
                pickImage();
                break;
            case R.id.tvTakeCoin:
                if (TextUtils.isEmpty(chainType)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingxuazejianleixing), this);
                    return;
                }
                address = etAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingtianxietibidizhi), this);
                    return;
                }

                amount = etAmount.getText().toString();
                if (TextUtils.isEmpty(amount)) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingshurutibishuliang), this);
                    return;
                }
                if (Double.parseDouble(amount) < fee) {
                    CommonUtil.showmessage(getResources().getString(R.string.tibishuliangbuzu), this);
                    return;
                }
                if (Double.parseDouble(amount) > balance) {
                    CommonUtil.showmessage(getResources().getString(R.string.tibishuliangbuzu), this);
                    return;
                }
                if (isAuth){//已通过验证
                    showTipsDialog(""+lastAmount,address, chainType);//弹窗提示金额，是否确定提现
                }else{//如果未通过手机验证
                    if (!TextUtils.isEmpty(phone)){//如果手机不为空，去验证手机
                        Intent intent = new Intent(TakeCoinActivity.this, PhoneAuthCodeActivity.class);
                        intent.putExtra("phone",phone);
                        intent.putExtra("email",email);
                        intent.putExtra("type",1);
                        startActivityForResult(intent,105);
                    }else {//如果手机为空，先跳绑定手机页面去绑定
                        Intent intent = new Intent(TakeCoinActivity.this, BindPhoneActivity.class);
                        startActivity(intent);
                    }
                }
                break;
        }
    }
    private double balance,fee;//可提币数量，手续费
    private boolean isAuth=false;
    private String email,phone;
    private String amount,address,image,chainType="";
    private Call<ResponseBody> getUserInfoCall;
    private void startGetMyHomeCallCall() {
        getUserInfoCall = VHttpServiceManager.getInstance().getVService().myUserInfo();
        getUserInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    email = resultData.getItem("email", String.class);
                    phone = resultData.getItem("phone", String.class);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    private double lastAmount;
    private void setLastAmountText(String amountText) {
        if (!TextUtils.isEmpty(amountText)) {
            double amount = Double.parseDouble(amountText);//把输入内容换成double
            lastAmount = amount;
            if (lastAmount >= 0) {
                tvLastAmount.setText(lastAmount+"USDT");
            }
//            if (amount > 0) {
//                BigDecimal bigDecimal = BigDecimal.valueOf(amount).subtract(new BigDecimal(fee)).setScale(amount_decimalcount, BigDecimal.ROUND_FLOOR);//amount减去fee，并且保留8位小数
//                lastAmount = bigDecimal.doubleValue();//big值 转为double
//                if (lastAmount >= 0) {
//                    tvLastAmount.setText(bigDecimal.toPlainString()+"USDT");
//                }
//            }
        } else {
            tvLastAmount.setText("0.00000000" + "USDT");
        }
    }


    TjrBaseDialog TipsDialog;
    private void showTipsDialog(final String amount, final String address, final String chainType) {
        TipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                Log.d("CoinSubmit", "address==" + address);
                startWithdrawCoinSubmit();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        TipsDialog.setTitleVisibility(View.GONE);
        TipsDialog.setMessage(getResources().getString(R.string.tibibizhong)+":USDT\n"+getResources().getString(R.string.tibishuliang)+":" + amount + "\n\n" + getResources().getString(R.string.querenqianqingzixiheduitibidizhixinxi));
        TipsDialog.setBtnOkText(getResources().getString(R.string.queding));
        TipsDialog.show();
    }

    private String uploadHeadurl;
    private Call<ResponseBody> callUploadFile;
    private void startUploadUserHead() {
        CommonUtil.cancelCall(callUploadFile);
        showProgressDialog();
        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetOriginal", "common", UploadFileUtils.getImageFilesMap(bitmapFile.getAbsolutePath()));
//        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, 1, 0, fileZone, UploadFileUtils.getFilesMap(bitmapFile.getAbsolutePath()));
        callUploadFile.enqueue(new MyCallBack(TakeCoinActivity.this) {
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                Toast.makeText(TakeCoinActivity.this,"onFailure",Toast.LENGTH_LONG).show();
            }

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
                            Glide.with(TakeCoinActivity.this).load(uploadHeadurl).into(img_erweima);
                            txt_erweima.setVisibility(View.GONE);
                            img_erweima.setVisibility(View.VISIBLE);
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
    private File bitmapFile;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            if (intent.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, -1) == 0) {
                ArrayList<String> flist = intent.getStringArrayListExtra(CommonConst.PICLIST);
                if (flist != null && flist.size() > 0) {
                    String filePath = flist.get(0);
                    bitmapFile = new File(filePath);
                    startUploadUserHead();
                }
            }
        }
    }


    private Call<ResponseBody> getCoinSubmitCall;
    private void startWithdrawCoinSubmit() {
        CommonUtil.cancelCall(getCoinSubmitCall);
        long userId = Long.parseLong(ConfigTjrInfo.getInstance().getUserId());
        getCoinSubmitCall = VHttpServiceManager.getInstance().getVService().getWithdrawLocalSubmit(userId,lastAmount+"",etAddress.getText().toString().trim(),uploadHeadurl,-1,chainType);
        getCoinSubmitCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Toast.makeText(TakeCoinActivity.this,"申請成功提交",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private void pickImage() {
        //动态申请权限
        if (ContextCompat.checkSelfPermission(TakeCoinActivity.this,Manifest.permission
                .WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(TakeCoinActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
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
            else Toast.makeText(TakeCoinActivity.this,"你拒絕了這個許可",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2){
            //判断安卓版本
            if (resultCode == RESULT_OK&&data!=null){
                if (Build.VERSION.SDK_INT>=19)
                    handImage(data);
                else handImageLow(data);
            }
        }else if (requestCode ==105){
            if (resultCode == 1&&data!=null){
                String  a = data.getStringExtra("success");
                if (!TextUtils.isEmpty(a)){
                    isAuth=true;
                }
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
            Toast.makeText(this,"影像設定失敗",Toast.LENGTH_SHORT).show();
        }
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = DensityUtil.dip2px(TakeCoinActivity.this, 5);
            outRect.right = DensityUtil.dip2px(TakeCoinActivity.this, 5);
            outRect.top = DensityUtil.dip2px(TakeCoinActivity.this, 5);
            outRect.bottom = DensityUtil.dip2px(TakeCoinActivity.this, 5);

        }
    }
}

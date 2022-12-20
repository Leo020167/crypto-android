package com.bitcnew.module.home.trade;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcnew.R;
import com.bitcnew.common.base.CommonToolbar;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.text.MoneyTextWatcher;
import com.bitcnew.http.common.ConfigTjrInfo;
import com.bitcnew.http.retrofitservice.UploadFileUtils;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.RechargeCoinActivity2;
import com.bitcnew.module.home.trade.dialog.CoinTypePickerDialog;
import com.bitcnew.module.home.trade.entity.CoinChains;
import com.bitcnew.module.home.trade.entity.CoinConfig;
import com.bitcnew.module.home.trade.entity.RechargeCoinAddress;
import com.bitcnew.module.home.trade.entity.RechargeCoinConfig;
import com.bitcnew.module.home.trade.history.TakeCoinHistoryActivity;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONArray;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class RechargeCoin1Activity extends TJRBaseToolBarSwipeBackActivity {

    private static final int RC_PERMISSION = 11;
    private static final int RC_IMAGE = 12;

    @BindView(R.id.commonToolbar)
    CommonToolbar commonToolbar;
    @BindView(R.id.keyongyueLabel)
    TextView keyongyueLabel;
    @BindView(R.id.keyongyueTv)
    TextView keyongyueTv;
    @BindView(R.id.zuixiaochongzhijineLabel)
    TextView zuixiaochongzhijineLabel;
    @BindView(R.id.zuixiaochongzhijineTv)
    TextView zuixiaochongzhijineTv;
    @BindView(R.id.xuanzetibiwangluoLabel)
    TextView xuanzetibiwangluoLabel;
    @BindView(R.id.coinSubTypeRadioGroup)
    RadioGroup coinSubTypeRadioGroup;
    @BindView(R.id.coinTypeTv)
    TextView coinTypeTv;
    @BindView(R.id.ivQr)
    ImageView ivQr;
    @BindView(R.id.zhiyunxuchongzhiLabel)
    TextView zhiyunxuchongzhiLabel;
    @BindView(R.id.chainLabel)
    TextView chainLabel;
    @BindView(R.id.chainTv)
    TextView chainTv;
    @BindView(R.id.addressTv)
    TextView addressTv;
    @BindView(R.id.amountEt)
    EditText amountEt;
    @BindView(R.id.uploadPlaceholderView)
    View uploadPlaceholderView;
    @BindView(R.id.uploadImageView)
    ImageView uploadImageView;
    @BindView(R.id.action_delUploadImage)
    View action_delUploadImage;

    private Call<ResponseBody> coinListCall;
    private CoinChains coinChains;
    private CoinConfig coinType;
    private String chain;

    private Call<ResponseBody> configCall;
    private RechargeCoinConfig config;
    private RechargeCoinAddress address;

    // 选择币种
    @OnClick(R.id.coinTypeTv)
    public void onCoinTypeClick() {
        // 选择币种
        showCoinTypePickerDialog();
    }

    // 复制
    @OnClick(R.id.action_copy)
    public void onCopyClick() {
        com.bitcnew.util.CommonUtil.copyText(getContext(), addressTv.getText());
    }

    @OnClick(R.id.action_uploadImage)
    public void onUploadClick() {
        pickImage();
    }

    @OnClick(R.id.action_delUploadImage)
    public void onDelClick() {
        bitmapFile = null;
        uploadHeadurl = null;

        uploadPlaceholderView.setVisibility(View.VISIBLE);
        uploadImageView.setVisibility(View.GONE);
        action_delUploadImage.setVisibility(View.GONE);
    }

    @OnClick(R.id.action_add)
    public void onAddClick() {
        submit();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.recharge_coin1;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.chongbi);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        commonToolbar.setOnMoreListener(v -> {
            PageJumpUtil.pageJump(getContext(), TakeCoinHistoryActivity.class);
        });

        coinSubTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton btn = radioGroup.findViewById(i);
                if (null != btn) {
                    setChain(btn.getText().toString());
                }
            }
        });

        amountEt.addTextChangedListener(new MoneyTextWatcher());

        // 加载币种列表
        loadCoinTypeList(false);
    }

    private void showCoinTypePickerDialog() {
        if (null == coinChains) {
            loadCoinTypeList(true);
            return;
        }

        _showCoinTypePickerDialog();
    }

    private void _showCoinTypePickerDialog() {
        if (null == coinChains) {
            return;
        }
        new CoinTypePickerDialog(getContext(), coinChains.getCoinConfigList(), coinType -> {
            setCoinType(coinType);
        }).show();
    }

    private void loadCoinTypeList(boolean showDialogFlag) {
        CommonUtil.cancelCall(coinListCall);
        coinListCall = VHttpServiceManager.getInstance().getVService().coinList(1, getUserIdLong());
        coinListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    coinChains = gson.fromJson(resultData.data, CoinChains.class);
                    initChainRadioGroup();

                    if (showDialogFlag) {
                        _showCoinTypePickerDialog();
                    } else if (null == coinType && null != coinChains && null != coinChains.getCoinList() && !coinChains.getCoinList().isEmpty()) {
                        setCoinType(coinChains.getCoinConfigList().get(0));
                    }
                } else {
                    showToast(resultData.msg);
                }
            }
        });
    }

    private void initChainRadioGroup() {
        if (null == coinChains) {
            return;
        }

        coinSubTypeRadioGroup.removeAllViews();

        RadioButton btn;
        boolean first = true;
        for (String chain : coinChains.getChainTypeList()) {
            btn = (RadioButton) LayoutInflater.from(getContext()).inflate(R.layout.take_coin1_radio_item, coinSubTypeRadioGroup, false);
            btn.setId(ViewCompat.generateViewId());
            btn.setText(chain);
            if (first) {
                setChain(chain);
                btn.setChecked(true);
                first = false;
            }
            coinSubTypeRadioGroup.addView(btn);
        }
    }

    private void setCoinType(CoinConfig coinType) {
        this.coinType = coinType;
        coinTypeTv.setText(coinType.getSymbol());
        zhiyunxuchongzhiLabel.setText(getString(R.string.zhiyunxuchongzhi_, coinType.getSymbol()));

        if (needChain(coinType)) {
            xuanzetibiwangluoLabel.setVisibility(View.VISIBLE);
            coinSubTypeRadioGroup.setVisibility(View.VISIBLE);

            chainLabel.setVisibility(View.VISIBLE);
            chainTv.setVisibility(View.VISIBLE);
        } else {
            xuanzetibiwangluoLabel.setVisibility(View.GONE);
            coinSubTypeRadioGroup.setVisibility(View.GONE);

            chainLabel.setVisibility(View.GONE);
            chainTv.setVisibility(View.GONE);
        }

        keyongyueLabel.setText(getString(R.string.keyongyue_, coinType.getSymbol()));
        zuixiaochongzhijineLabel.setText(getString(R.string.dongjiejine_, coinType.getSymbol()));

        loadConfig(coinType);
    }

    private boolean needChain(CoinConfig coinType) {
        return null != coinType && "usdt".equalsIgnoreCase(coinType.getSymbol());
    }

    private void setChain(String chain) {
        this.chain = chain;
        this.chainTv.setText(chain);

        if (null != chain && coinSubTypeRadioGroup.getChildCount() > 0) {
            RadioButton btn;
            for (int i = coinSubTypeRadioGroup.getChildCount() - 1; i >= 0; i--) {
                btn = (RadioButton) coinSubTypeRadioGroup.getChildAt(i);
                if (btn.getText().toString().equalsIgnoreCase(chain) && !btn.isChecked()) {
                    btn.setChecked(true);
                }
            }
        }

        bindAddress();
    }

    private void loadConfig(final CoinConfig coinType) {
        if (null == coinType || null == coinType.getSymbol() || coinType.getSymbol().length() == 0) {
            return;
        }
        CommonUtil.cancelCall(configCall);
        configCall = VHttpServiceManager.getInstance().getVService().getChargeConfigs(getUserIdLong(), coinType.getSymbol());
        configCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    config = gson.fromJson(resultData.data, RechargeCoinConfig.class);
                    keyongyueTv.setText(config.getAvailableAmount());
                    zuixiaochongzhijineTv.setText(config.getMinChargeAmount());

                    bindAddress();
                } else {
                    showToast(resultData.msg);
                }
            }
        });
    }

    private void bindAddress() {
        if (null == config || null == coinType) {
            return;
        }

        if (!needChain(coinType)) {
            if (null != config.getAddressList() && !config.getAddressList().isEmpty()) {
                setAddress(config.getAddressList().get(0));
            } else {
                clearAddress();
            }
        } else if (null != chain) {
            if (null != config.getAddressList() && !config.getAddressList().isEmpty()) {
                boolean found = false;
                for (RechargeCoinAddress item : config.getAddressList()) {
                    if (chain.equalsIgnoreCase(item.getChainType())) {
                        setAddress(item);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    clearAddress();
                }
            } else {
                clearAddress();
            }
        } else {
            clearAddress();
        }
    }

    private void setAddress(RechargeCoinAddress address) {
        this.address = address;
        String addressString = address.getAddress();
        addressTv.setText(addressString);
        // 二维码
        showQrCode(addressString);
    }

    private void showQrCode(String address) {
        Bitmap mBitmap = CodeUtils.createImage(address, 360, 360, null);
        ivQr.setImageBitmap(mBitmap);
        ivQr.setVisibility(View.VISIBLE);
    }

    private void clearAddress() {
        addressTv.setText(null);
        // 二维码
        ivQr.setVisibility(View.INVISIBLE);
    }

    private void pickImage() {
        //动态申请权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_PERMISSION);
        } else {
            //执行启动相册的方法
            openAlbum();
        }
    }

    //启动相册的方法
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, RC_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                openAlbum();
            else {
                showToast(R.string.jujuexuke);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMAGE) {
            //判断安卓版本
            if (resultCode == RESULT_OK && data != null) {
                if (Build.VERSION.SDK_INT >= 19) {
                    handImage(data);
                } else {
                    handImageLow(data);
                }
            }
        }
    }

    //安卓版本大于4.4的处理方法
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handImage(Intent data) {
        String path = null;
        Uri uri = data.getData();
        //根据不同的uri进行不同的解析
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        //展示图片
        displayImage(path);
    }

    //安卓小于4.4的处理方法
    private void handImageLow(Intent data) {
        Uri uri = data.getData();
        String path = getImagePath(uri, null);
        displayImage(path);
    }

    //content类型的uri获取图片路径的方法
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    //根据路径展示图片的方法
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            bitmapFile = new File(imagePath);
            startUploadUserHead();
        } else {
            showToast(R.string.wufashedingyingxiang);
        }
    }

    private File bitmapFile;
    private Call<ResponseBody> callUploadFile;
    private String uploadHeadurl;
    private void startUploadUserHead() {
        com.bitcnew.util.CommonUtil.cancelCall(callUploadFile);
        showProgressDialog();
        callUploadFile = UploadFileUtils.uploadFiles(VHttpServiceManager.UPLOADFILE_URL, "imageRetOriginal", "common", UploadFileUtils.getImageFilesMap(bitmapFile.getAbsolutePath()));
        callUploadFile.enqueue(new MyCallBack(getContext()) {
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
                            Glide.with(getContext()).load(uploadHeadurl).into(uploadImageView);

                            uploadPlaceholderView.setVisibility(View.GONE);
                            uploadImageView.setVisibility(View.VISIBLE);
                            action_delUploadImage.setVisibility(View.VISIBLE);
                        } else {
                            uploadPlaceholderView.setVisibility(View.VISIBLE);
                            uploadImageView.setVisibility(View.GONE);
                            action_delUploadImage.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        showToast(e.getMessage());
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

    private Call<ResponseBody> submitCall;
    private void submit() {
        if (null == coinType) {
            showToast(R.string.xuanzechongzhibizhong);
            return;
        }
        if (needChain(coinType)) {
            showToast(R.string.xuanzechongzhiwangluo);
            return;
        }
        if (null == address) {
            showToast(R.string.qingxuanzechongzhidizhi);
            return;
        }

        String amount = amountEt.getText().toString().trim();
        if (amount.length() == 0) {
            showToast(R.string.qingshuruchongzhishuliang);
            return;
        }

        if (null == uploadHeadurl) {
            showToast(R.string.qingshangchuanzhifupinzheng);
            return;
        }

        com.bitcnew.util.CommonUtil.cancelCall(submitCall);
        long userId = Long.parseLong(ConfigTjrInfo.getInstance().getUserId());
        submitCall = VHttpServiceManager.getInstance().getVService().chargeSubmit(userId,
                coinType.getSymbol(), chain, address.getAddress(), amount, uploadHeadurl);
        submitCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    showToast(R.string.tijiaoshenqingchenggong);
                    finish();
                }
            }
        });
    }

}

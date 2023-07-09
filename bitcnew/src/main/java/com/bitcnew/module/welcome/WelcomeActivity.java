package com.bitcnew.module.welcome;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.bitcnew.MainApplication;
import com.bitcnew.R;
import com.bitcnew.SpUtils;
import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.util.UIHandler;
import com.bitcnew.data.sharedpreferences.SysShareData;
import com.bitcnew.http.TjrBaseApi;
import com.bitcnew.http.model.User;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.HomeActivity;
import com.bitcnew.nsk.TjrStarNSKManager;
import com.bitcnew.social.TjrSocialMTAUtil;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.SPUtils;

import java.util.Locale;

import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class WelcomeActivity extends TJRBaseToolBarActivity {
    private boolean isNewversion;//判断是不是新版本
    private String userId = "";
    private MainApplication application;
    private Resources res;

    private Call<ResponseBody> bootPageCall, getDnsCall;

    private void jumpNextPage() {
        if (isNewversion) {
            Bundle bundle = new Bundle();
            bundle.putInt(CommonConst.JUMPTYPE, 1);
            PageJumpUtil.pageJump(WelcomeActivity.this, GuideActivity.class, bundle);
        } else {
//            if (application.getUser() == null || application.getUser().getUserId() == 0) {
//                PageJumpUtil.pageJump(WelcomeActivity.this, LoginActivity.class, null);
//            } else {
//                PageJumpUtil.pageJump(WelcomeActivity.this, HomeActivity.class, null);
//            }
            String lang = (String) SPUtils.get(this,"myLanguage1","");
            switchLanguage(lang);
//            PageJumpUtil.pageJump(WelcomeActivity.this, HomeActivity.class, null);
        }
        overridePendingTransition(R.anim.alpha_from0_to1, R.anim.alpha_from1_to0);
        WelcomeActivity.this.finish();
    }

    private void switchLanguage(String lang) {//设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if ("zh-cn".equals(lang)) {//简体中文
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if ("zh-tw".equals(lang)) {//繁体中文
            config.locale = Locale.TRADITIONAL_CHINESE;
        } else if ("ko".equals(lang)) {//韩语
            config.locale = Locale.KOREA;
        } else if ("jp".equals(lang)) {//日语
            config.locale = Locale.JAPANESE;
        } else if ("ru".equals(lang)) {//俄语
            config.locale = new Locale("ru");
        } else if ("fr".equals(lang)) {//法语
            config.locale = Locale.FRENCH;
        } else if ("es".equals(lang)) {//西班牙语
            config.locale = new Locale("es");
        } else if ("pt".equals(lang)) {//葡萄牙语
            config.locale = new Locale("pt");
        } else {
//            config.locale = Locale.getDefault();
            if ("ts".equalsIgnoreCase(com.bitcnew.http.BuildConfig.DEFAULT_LNG)) {
                config.locale = Locale.TRADITIONAL_CHINESE;
            } else if ("cn".equalsIgnoreCase(com.bitcnew.http.BuildConfig.DEFAULT_LNG)) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
            } else {
                config.locale = Locale.ENGLISH;
            }
        }

        resources.updateConfiguration(config, dm);
        //更新语言后，destroy当前页面，重新绘制
        finish();
        Intent it = new Intent(WelcomeActivity.this, HomeActivity.class);
        //清空任务栈确保当前打开activit为前台任务栈栈顶
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.welcome_main;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    // android:theme="@android:style/Theme.Black.NoTitleBar"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TjrSocialMTAUtil.initMTAConfig(TjrBaseApi.isDebug, this);
//        mActionBar.hide();
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        //之前经常碰到有时候按了home键之后app会重启,就是因为WelcomeActivity经常重新生成实例导致的
        if (!this.isTaskRoot()) { // 判断当前activity是不是所在任务栈的根
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        immersionBar.navigationBarColor(R.color.white).init();
        application = (MainApplication) getApplicationContext();

        ButterKnife.bind(this);
        res = this.getResources(); //这句放在onCreate中

        String version = SysShareData.getVersion(this);
        if (!version.equals(((MainApplication) getApplicationContext()).getmVersion()) && CommonConst.ISSHOWNEWVER) {
            isNewversion = true;
        }

        VHttpServiceManager.getInstance().resetService(MainApplication.getCtx());
        VHttpServiceManager.getInstance().test();
        TjrStarNSKManager.getInstance().resetInit(MainApplication.getCtx());
        User user = ((MainApplication) getApplicationContext()).getUser();
        if (user != null) userId = String.valueOf(user.getUserId());

        UIHandler.postDelay(()-> {
//        getDns();
            jumpNextPage();
        }, 2000);
    }

    private int urlNum=1,wNum,listSize=1;//网络请求次数,   尾数
    private String[] items;
    private void getDns() {
        CommonUtil.cancelCall(getDnsCall);
        getDnsCall = VHttpServiceManager.getInstance().getVService().getActivityList(com.bitcnew.http.BuildConfig.SERVER_TYPE);
        getDnsCall.enqueue(new MyCallBack(WelcomeActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (null!=resultData.data){
                        SpUtils.getInstance(WelcomeActivity.this).put( "APP_SERVICE_CONFIG", resultData.data);
                    }


//                    list = gson.fromJson(resultData.data,String.class);
//                    String quoteSocket = resultData.getItem("quoteSocket", String.class);
//                    String pushSocket = resultData.getItem("pushSocket", String.class);
//                    String api = resultData.getItem("api", String.class);
//
//                    TjrBaseApi.stockHomeUri.setUri(quoteSocket);
//                    TjrBaseApi.mApiSubPushUrl.setUri(pushSocket);
//                    TjrBaseApi.mApiCropymeBaseUri.setUri(api);

//                    NormalShareData.saveDnsInfo(WelcomeActivity.this,  quoteSocket,  pushSocket,  api);


//                    Log.d("ReceivedManager", String.format("[quoteSocket:%s] [pushSocket:%s] [api:%s]", TjrBaseApi.stockHomeUri,TjrBaseApi.mApiSubPushUrl,TjrBaseApi.mApiCropymeBaseUri));

                    jumpNextPage();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                super.onFailure(call, t);
                if (null==items){
                    String appUrl = SpUtils.getInstance(WelcomeActivity.this).getString("APP_SERVICE_CONFIG","");
                    if (!TextUtils.isEmpty(appUrl)){
                        appUrl = appUrl.substring(1,appUrl.length()-1);
                        appUrl = appUrl.replace("\"","");
                        items = appUrl.split(",");
                        listSize = items.length;
                    }else {
                        items = res.getStringArray(R.array.yuming);
                        listSize = items.length;
                    }
                    swichHttp();
                }else {
                    swichHttp();
                }

            }
        });
    }

    private void swichHttp(){
        TjrBaseApi.setCtx(MainApplication.getCtx());
        wNum = urlNum%listSize;//网络请求次数,   求余数
        SpUtils.getInstance(WelcomeActivity.this).put( "stockHomeUri", "market."+items[wNum]);
        SpUtils.getInstance(WelcomeActivity.this).put( "stockHomeUriHttp", "http://market."+items[wNum]);
        SpUtils.getInstance(WelcomeActivity.this).put( "mApiSubPushUrl", "api."+items[wNum]);
        SpUtils.getInstance(WelcomeActivity.this).put( "mApiCropymeBaseUri", "http://api."+items[wNum]);
        SpUtils.getInstance(WelcomeActivity.this).put( "gamePredictSocket", "predict."+items[wNum]);
        SpUtils.getInstance(WelcomeActivity.this).put( "gamePredictHttp", "http://predict."+items[wNum]);
        SpUtils.getInstance(WelcomeActivity.this).put( "mApiCropymeBaseUploadFile","http://upload."+items[wNum]);

        urlNum = urlNum+1;
        Toast.makeText(WelcomeActivity.this, getResources().getString(R.string.qiehuanxianlu), Toast.LENGTH_SHORT).show();
        getDns();
    }
}

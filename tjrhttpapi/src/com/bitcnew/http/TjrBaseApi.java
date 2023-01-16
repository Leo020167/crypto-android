package com.bitcnew.http;

import android.content.Context;
import android.text.TextUtils;

public class TjrBaseApi {
    public static Context ctx;
//    //正式环境
//    public static String  stockHomeUri ="market.piglobalexchanges.com";
//    public static String  stockHomeUriHttp="http://market.piglobalexchanges.com";//retrofit的baseurl必须以"/"结束否则报错
//    public static String  mApiSubPushUrl="api.piglobalexchanges.com";
////    public static String  mApiCropymeBaseUri="http://api.piglobalexchanges.com";
//    public static String  mApiCropymeBaseUri="http://api.piglobalexchanges.com";
//    public static String  gamePredictSocket="predict.piglobalexchanges.com";
//    public static String  gamePredictHttp="http://predict.piglobalexchanges.com";
//    public static String  mApiCropymeBaseUploadFile="http://upload.piglobalexchanges.com";
//    public static String  mApiWebUri = "http://coupon.piglobalexchanges.com";


//    private static final String BASE_URL = "encryptedex.com";
    private static final String BASE_URL = "worldcoinservice.com";
    //测试环境
    public static String  stockHomeUri = "market." + BASE_URL;
    public static String  stockHomeUriHttp = "http://market." + BASE_URL;//retrofit的baseurl必须以"/"结束否则报错
    public static String  mApiSubPushUrl = "api." + BASE_URL;
//    public static String  mApiCropymeBaseUri = "http://api." + BASE_URL;
    public static String  mApiCropymeBaseUri = "http://api." + BASE_URL;
    public static String  gamePredictSocket = "predict." + BASE_URL;
    public static String  gamePredictHttp = "http://predict." + BASE_URL;
    public static String  mApiCropymeBaseUploadFile = "http://upload." + BASE_URL;
    public static String  mApiWebUri = "http://coupon." + BASE_URL;


    // 出外网一定要修改成false
    public static final boolean isDebug;
    public static final int debugType;//当debug为true时候才有效, debugType==0 连接青爷  debugType==1 连接科城
    public static final boolean isLog;

    static {
        isDebug = false;
        debugType =0;//debugType==0 连接青爷  debugType==1 连接科城
        isLog = false;
    }

    private String uri;

    TjrBaseApi(String uri, String debugUri, String debugUri2) {
        this.uri = uri;
    }

    public void setUri(String uri) {
        if (!TextUtils.isEmpty(uri)) {
            this.uri = uri;
        }
    }

    public String uri() {
        return uri;
    }
    public static void setCtx(Context context) {
        ctx = context;
    }

    public static Context getCtx() {
        return ctx;
    }
}
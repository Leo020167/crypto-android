package com.bitcnew.util;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * 主要功能:App网络管理
 *
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil
 * @author: zsj
 * @date: 2019年08月01日
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */

public class NetWorkUtil {
    //未找到合适匹配网络类型
    public static final int TYPE_NO = 0;

    //中国移动CMNET网络(中国移动GPRS接入方式之一, 主要为PC、笔记本电脑、PDA设立)
    public static final int TYPE_MOBILE_CMNET = 1;

    //中国移动CMWAP网络(中国移动GPRS接入方式之一,主要为手机WAP上网而设立)
    public static final int TYPE_MOBILE_CMWAP = 2;

    //中国联通UNIWAP网络(中国联通划分GPRS接入方式之一, 主要为手机WAP上网而设立)
    public static final int TYPE_MOBILE_UNIWAP = 3;

    //中国联通3GWAP网络
    public static final int TYPE_MOBILE_3GWAP = 4;

    //中国联通3HNET网络
    public static final int TYPE_MOBLIE_3GNET = 5;

    //中国联通UNINET网络(中国联通划分GPRS接入方式之一, 主要为PC、笔记本电脑、PDA设立)
    public static final int TYPE_MOBILE_UNINET = 6;

    //中国电信CTWAP网络
    public static final int TYPE_MOBILE_CTWAP = 7;

    //中国电信CTNET网络
    public static final int TYPE_MOBILE_CTNET = 8;

    //WIFI网络
    public static final int TYPE_WIFI = 10;

    /**
     * 获取当前手机连接的网络类型
     *
     * @param context 上下文
     * @return int 网络类型
     */
    public static int getNetworkState(Context context) {
        //获取ConnectivityManager对象
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        //获得当前网络信息
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            //获取网络类型
            int currentNetWork = networkInfo.getType();
            //手机网络类型
            if (currentNetWork == ConnectivityManager.TYPE_MOBILE) {
                if (networkInfo.getExtraInfo() != null) {
                    if (networkInfo.getExtraInfo().equals("cmnet")) {
                        Log.i("NetWorkUtil", "当前网络为中国移动CMNET网络");
                        return TYPE_MOBILE_CMNET;
                    }
                    if (networkInfo.getExtraInfo().equals("cmwap")) {
                        Log.i("NetWorkUtil", "当前网络为中国移动CMWAP网络");
                        return TYPE_MOBILE_CMWAP;
                    }
                    if (networkInfo.getExtraInfo().equals("uniwap")) {
                        Log.i("NetWorkUtil", "当前网络为中国联通UNIWAP网络");
                        return TYPE_MOBILE_UNIWAP;
                    }
                    if (networkInfo.getExtraInfo().equals("3gwap")) {
                        Log.i("NetWorkUtil", "当前网络为中国联通3GWAP网络");
                        return TYPE_MOBILE_3GWAP;
                    }
                    if (networkInfo.getExtraInfo().equals("3gnet")) {
                        Log.i("NetWorkUtil", "当前网络为中国联通3GNET网络");
                        return TYPE_MOBLIE_3GNET;
                    }
                    if (networkInfo.getExtraInfo().equals("uninet")) {
                        Log.i("NetWorkUtil", "当前网络为中国联通UNINET网络");
                        return TYPE_MOBILE_UNINET;
                    }
                    if (networkInfo.getExtraInfo().equals("ctwap")) {
                        Log.i("NetWorkUtil", "当前网络为中国电信CTWAP网络");
                        return TYPE_MOBILE_UNINET;
                    }
                    if (networkInfo.getExtraInfo().equals("ctnet")) {
                        Log.i("NetWorkUtil", "当前网络为中国电信CTNET网络");
                        return TYPE_MOBILE_UNINET;
                    }
                }
                //WIFI网络类型
            } else if (currentNetWork == ConnectivityManager.TYPE_WIFI) {
                Log.i("NetWorkUtil", "当前网络为WIFI网络");
                return TYPE_WIFI;
            }
        }
        return TYPE_NO;
    }

    /**
     * 判断网络是否连接
     *
     * @param context 上下文
     * @return boolean 网络连接状态
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            //获取连接对象
            if (mNetworkInfo != null) {
                //判断是TYPE_MOBILE网络
                if (ConnectivityManager.TYPE_MOBILE == mNetworkInfo.getType()) {
                    Log.i("NetWorkUtil", "网络连接类型为：TYPE_MOBILE");
                    //判断移动网络连接状态
                    State STATE_MOBILE = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                    if (STATE_MOBILE == State.CONNECTED) {
                        Log.i("AppNetworkMgrd", "网络连接类型为：TYPE_MOBILE, 网络连接状态CONNECTED成功！");
                        return mNetworkInfo.isAvailable();
                    }
                }
                //判断是TYPE_WIFI网络
                if (ConnectivityManager.TYPE_WIFI == mNetworkInfo.getType()) {
                    Log.i("NetWorkUtil", "网络连接类型为：TYPE_WIFI");
                    //判断WIFI网络状态
                    State STATE_WIFI = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                    if (STATE_WIFI == State.CONNECTED) {
                        Log.i("NetWorkUtil", "网络连接类型为：TYPE_WIFI, 网络连接状态CONNECTED成功！");
                        return mNetworkInfo.isAvailable();
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断网络是否连接
     *
     * @param activity Activity
     * @return boolean 网络连接状态
     */
    public static boolean isNetworkConnected(Activity activity) {
        boolean falg = false;
        ConnectivityManager mConnectivityManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        if (mConnectivityManager == null) {
            return falg;
        }
        NetworkInfo[] arrayOfNetworkInfo = mConnectivityManager.getAllNetworkInfo();
        if (arrayOfNetworkInfo != null) {
            for (int j = 0; j < arrayOfNetworkInfo.length; j++) {
                if (arrayOfNetworkInfo[j].getState() == State.CONNECTED) {
                    falg = true;
                    break;
                }
            }
        }
        return falg;
    }

    /**
     * 打开网络设置界面
     *
     * @param activity Activity
     */
    public static void openNetSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 检测3G是否连接
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean is3gConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            }
        }
        return false;
    }
//
//    /*参数令牌，每次必传*/
//    public static Map<String, String> tokenMap() {
//        Map<String, String> map = new HashMap<>();
//        String AppAuthorization = (String) SPUtils.get(BaseApp.getInstance(), "AppAuthorization", "");
//        boolean language = (boolean) SPUtils.get(BaseApp.getInstance(), "is_language", true);
//        String registration_id = (String) SPUtils.get(BaseApp.getInstance(), "registration_id", "");
//        if (language) {
//            map.put("lang", "2");
//        } else {
//            map.put("lang", "1");
//        }
//        map.put("version", Constants.API_VERSION);
//        map.put("platform", Constants.FROM);
//        map.put("AppAuthorization", "" + AppAuthorization);
//        map.put("registration_id", registration_id);
//        map.put("os", StringUtils.getSystemVersion());
//        map.put("device", StringUtils.getSystemModel());
//        return map;
//    }
//
//    /*公用参数*/
//    public static Map<String, String> common() {
//        Map<String, String> map = new HashMap<>();
//        boolean language = (boolean) SPUtils.get(BaseApp.getInstance(), "is_language", true);
//        if (language) {
//            map.put("lang", "2");
//        } else {
//            map.put("lang", "1");
//        }
//        map.put("timestamp", "" + System.currentTimeMillis() / 1000);
//        map.put("platform", Constants.FROM);
//        map.put("version", Constants.API_VERSION);
//        return map;
//    }

}


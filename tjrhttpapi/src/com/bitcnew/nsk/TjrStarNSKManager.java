package com.bitcnew.nsk;

import android.content.Context;
import android.util.Log;

import com.bitcnew.SpUtils;
import com.bitcnew.http.TjrBaseApi;
import com.bitcnew.nsk.exceptions.TjrNSKConnectionException;

public class TjrStarNSKManager {
    private TjrNSKPool pool;
    private volatile static TjrStarNSKManager instance;
    private Context context;
    public TjrStarNSKManager() {
        init();
    }

    private void init(){
        TjrNSKPoolConfig config = new TjrNSKPoolConfig();
        // 最大分配的对象数
        config.setMaxActive(10);
        // 允许最大空闲对象数
        config.setMaxIdle(5);
        //允许最小空闲对象数
        config.setMinIdle(1);
        // 当池内没有返回对象时，最大等待时间
        config.setMaxWait(1000);
        // 当调用borrow Object方法时，是否进行有效性检查
        config.setTestOnBorrow(false);
        // 当调用return Object方法时，是否进行有效性检查
        config.setTestOnReturn(false);
        //
//        TjrBaseApi.stockHomeUri = SpUtils.getInstance(context).getString("stockHomeUri",TjrBaseApi.stockHomeUri);
        pool = new TjrNSKPool(config, TjrBaseApi.stockHomeUri, 9669, 4000, 8000);
    }

    /**
     * 单例化一个CacheManager
     *
     * @return CacheManager对象
     */
    public static TjrStarNSKManager getInstance() {
        if (instance == null) {
            synchronized (TjrStarNSKManager.class) {
                if (instance == null) {
                    instance = new TjrStarNSKManager();
                }
            }
        }
        return instance;
    }

    public synchronized void resetInit(Context context){
        this.context = context;
        init();
    }

    public String sendCommand(String cmd) {
        String obj = null;
        if (cmd == null) return obj;
        TjrNSK tjrnsk = null;
        try {
            tjrnsk = pool.getResource();
//            if (TjrBaseApi.isDebug)
                Log.d("Protocol", "==1==sendCommand...host=" + tjrnsk.getHost() + "  send=" + cmd);
            return tjrnsk.sendCommand(cmd);
        } catch (Exception e) {
//            if (TjrBaseApi.isDebug)
                Log.d("Protocol", "==4==sendCommand...Exception:" + e.getMessage());
            throw new TjrNSKConnectionException(e);
        } finally {
            if (tjrnsk != null) pool.returnResource(tjrnsk);
        }
    }

    public void clearPool() {
        if (TjrBaseApi.isDebug) Log.d("Protocol", "====clearPool:");
        if (pool != null) pool.clear();
    }
}

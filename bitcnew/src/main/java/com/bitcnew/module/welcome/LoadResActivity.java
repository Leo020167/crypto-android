package com.bitcnew.module.welcome;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.view.Window;
import android.view.WindowManager;

import com.bitcnew.MainApplication;
import com.bitcnew.R;


public class LoadResActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        Log.d("ddd", "LoadResActivity is start");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
        setContentView(R.layout.welcome_loadres);
        new LoadDexTask().execute();
    }

    class LoadDexTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                MultiDex.install(getApplication());
//                CommonUtil.LogLa(2, "LoadResActivity is install finish");
//                Log.d("loadDex", "install finish");
                ((MainApplication) getApplication()).installFinish(getApplication());
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            finish();
            System.exit(0);
        }
    }


}
package com.bitcnew.module.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.bitcnew.MainApplication;
import com.bitcnew.R;
import com.bitcnew.data.sharedpreferences.SysShareData;
import com.bitcnew.http.TjrBaseApi;
import com.bitcnew.http.common.ConfigTjrInfo;
import com.bitcnew.http.model.User;

import butterknife.BindView;

public class WebActivity extends AppCompatActivity implements View.OnClickListener {

    TextView view_back;

    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        url = TjrBaseApi.mApiWebUri+ "/index-zh-TW.html?userId="+getUserId()+"&token="+ ConfigTjrInfo.getInstance().getSessionid();
//        url ="https:www.baidu.com/";
        //获得控件
        WebView webView = (WebView) findViewById(R.id.wv_webview);
        view_back = (TextView) findViewById(R.id.view_back);
        view_back.setOnClickListener(this);
        //解决webview加载的网页上的按钮点击失效  以及有些图标显示不出来的问题
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //默认不使用缓存！
        //访问网页
        webView.loadUrl(url);


        // 将webView的横向竖向的scrollBar都禁用掉，将不再与ScrollView冲突，解决了大面积空白的问题。
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.setVerticalScrollBarEnabled(false);
        webView.setVerticalScrollbarOverlay(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setHorizontalScrollbarOverlay(false);
        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //使用WebView加载显示url
                view.loadUrl(url);
                //返回true
                return true;
            }
        });
    }

    protected String getUserId() {
        MainApplication application = (MainApplication) getApplicationContext();
        User user = application.getUser();
        Log.d("getUserId","user=="+(user==null?"null":user.userId));
        if (user != null && user.getUserId() > 0) {
            return String.valueOf(user.getUserId());
        } else {
            return "";
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_back:
                finish();
                break;
        }
    }

}
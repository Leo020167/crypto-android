package com.bitcnew.module.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.webkit.WebView;
import android.widget.TextView;
import com.bitcnew.R;
import com.bitcnew.SpUtils;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.http.TjrBaseApi;

public class WebDialog extends Dialog {

    public WebView mWebView;
    public TextView txtSure;
    Context context;

    public WebDialog(@NonNull Context context) {
        super(context, R.style.progress_style);
        this.context = context;
        setContentView(R.layout.dialog_web);
        //使得点击对话框外部不消失对话框
        setCanceledOnTouchOutside(false);
        mWebView = (WebView) findViewById(R.id.webView);
        txtSure = (TextView) findViewById(R.id.txt_sure);

        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.getSettings().setSaveFormData(false);
//        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString() + "TOKA");

        TjrBaseApi.mApiCropymeBaseUri = SpUtils.getInstance(context).getString("mApiCropymeBaseUri",TjrBaseApi.mApiCropymeBaseUri);
        mWebView.loadUrl(TjrBaseApi.mApiCropymeBaseUri+CommonConst.FENGXIANSHU);
    }

    public final void dismiss() {
        super.dismiss();
    }

    public final void show() {
        super.show();
    }
}


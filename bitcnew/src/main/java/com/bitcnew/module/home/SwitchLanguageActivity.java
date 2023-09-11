package com.bitcnew.module.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitcnew.BuildConfig;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.module.dialog.TwoBtnDialog;
import com.bitcnew.util.SPUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwitchLanguageActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.iv_en)
    View iv_en;
    @BindView(R.id.iv_zhCn)
    View iv_zhCn;
    @BindView(R.id.iv_zhTw)
    View iv_zhTw;
    @BindView(R.id.iv_ko)
    View iv_ko;
    @BindView(R.id.iv_fr)
    View iv_fr;
    @BindView(R.id.iv_ru)
    View iv_ru;
    @BindView(R.id.iv_es)
    View iv_es;
    @BindView(R.id.iv_jp)
    View iv_jp;
    @BindView(R.id.iv_pt)
    View iv_pt;

    private String originalLang;
    private String lang;

    @OnClick(R.id.ll_en)
    public void onEnClick() {
        lang = "en";
        setLangUISelect(lang);
    }

    @OnClick(R.id.ll_zhCn)
    public void onZhCnClick() {
        lang = "zh-cn";
        setLangUISelect(lang);
    }

    @OnClick(R.id.ll_zhTw)
    public void onCnHkClick() {
        lang = "zh-tw";
        setLangUISelect(lang);
    }

    @OnClick(R.id.ll_ko)
    public void onKoClick() {
        lang = "ko";
        setLangUISelect(lang);
    }

    @OnClick(R.id.ll_fr)
    public void onFrClick() {
        lang = "fr";
        setLangUISelect(lang);
    }

    @OnClick(R.id.ll_ru)
    public void onRuClick() {
        lang = "ru";
        setLangUISelect(lang);
    }

    @OnClick(R.id.ll_es)
    public void onEsClick() {
        lang = "es";
        setLangUISelect(lang);
    }

    @OnClick(R.id.ll_jp)
    public void onJpClick() {
        lang = "jp";
        setLangUISelect(lang);
    }

    @OnClick(R.id.ll_pt)
    public void onPtClick() {
        lang = "pt";
        setLangUISelect(lang);
    }

    private void setLangUISelect(String lang) {
        iv_en.setVisibility(View.GONE);
        iv_zhCn.setVisibility(View.GONE);
        iv_zhTw.setVisibility(View.GONE);
        iv_ko.setVisibility(View.GONE);
        iv_fr.setVisibility(View.GONE);
        iv_ru.setVisibility(View.GONE);
        iv_es.setVisibility(View.GONE);
        iv_jp.setVisibility(View.GONE);
        iv_pt.setVisibility(View.GONE);

        if ("zh-cn".equals(lang)) {
            iv_zhCn.setVisibility(View.VISIBLE);
        } else if ("zh-tw".equals(lang)) {
            iv_zhTw.setVisibility(View.VISIBLE);
        } else if ("ko".equals(lang)) {
            iv_ko.setVisibility(View.VISIBLE);
        } else if ("jp".equals(lang)) {
            iv_jp.setVisibility(View.VISIBLE);
        } else if ("ru".equals(lang)) {
            iv_ru.setVisibility(View.VISIBLE);
        } else if ("fr".equals(lang)) {
            iv_fr.setVisibility(View.VISIBLE);
        } else if ("es".equals(lang)) {
            iv_es.setVisibility(View.VISIBLE);
        } else if ("pt".equals(lang)) {
            iv_pt.setVisibility(View.VISIBLE);
        } else if ("en".equals(lang)) {
            iv_en.setVisibility(View.VISIBLE);
        } else {
            if ("ts".equalsIgnoreCase(com.bitcnew.http.BuildConfig.DEFAULT_LNG)) {
                iv_zhTw.setVisibility(View.VISIBLE);
            } else if ("cn".equalsIgnoreCase(com.bitcnew.http.BuildConfig.DEFAULT_LNG)) {
                iv_zhCn.setVisibility(View.VISIBLE);
            } else {
                iv_en.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick(R.id.action_save)
    public void onSaveClick() {
        if (null == lang || lang.equals(originalLang)) {
            return;
        }

//        Locale.ENGLISH
//        Locale.KOREAN
//        Locale.JAPANESE
//        new Locale("ru")
//        Locale.FRENCH
//        new Locale("es")
//        new Locale("pt")

        SPUtils.put(this, "myLanguage1", lang);
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
            config.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(config, dm);
        //更新语言后，destroy当前页面，重新绘制
        finish();
        Intent it = new Intent(SwitchLanguageActivity.this, HomeActivity.class);
        //清空任务栈确保当前打开activit为前台任务栈栈顶
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        String lang = (String) SPUtils.get(this,"myLanguage1","");
        setLangUISelect(lang);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_switch_language;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.qiehuanyuyan);
    }
}
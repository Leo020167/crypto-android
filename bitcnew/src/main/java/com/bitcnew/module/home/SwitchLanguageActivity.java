package com.bitcnew.module.home;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.module.dialog.TwoBtnDialog;
import com.bitcnew.util.SPUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SwitchLanguageActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener{
    @BindView(R.id.txt_language1)
    TextView txt_language1;
    @BindView(R.id.txt_language2)
    TextView txt_language2;
    @BindView(R.id.txt_language3)
    TextView txt_language3;
    @BindView(R.id.txt_language4)
    TextView txt_language4;
    @BindView(R.id.txt_language5)
    TextView txt_language5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        txt_language1.setOnClickListener(this);
        txt_language2.setOnClickListener(this);
        txt_language3.setOnClickListener(this);
        txt_language4.setOnClickListener(this);
        txt_language5.setOnClickListener(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_switch_language;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.qiehuanyuyan);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_language1:
                TwoBtnDialog dialog = new TwoBtnDialog(SwitchLanguageActivity.this,"提示","确认切换到简体中文吗？","确认");
                dialog.txtSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        switchLanguage("jianti");
                    }
                });
                dialog.show();
                break;
            case R.id.txt_language2:
                TwoBtnDialog dialog2 = new TwoBtnDialog(SwitchLanguageActivity.this,"提示","確認切換到繁體中文嗎？","確認");
                dialog2.txtSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog2.dismiss();
                        switchLanguage("fanti");
                    }
                });
                dialog2.show();
                break;
            case R.id.txt_language3:
                TwoBtnDialog dialog3 = new TwoBtnDialog(SwitchLanguageActivity.this,"Tips","Are you sure to switch to English?","Sure");
                dialog3.txtSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog3.dismiss();
                        switchLanguage("yingyu");
                    }
                });
                dialog3.show();
                break;
            case R.id.txt_language4:
                TwoBtnDialog dialog4 = new TwoBtnDialog(SwitchLanguageActivity.this,"ヒント","日本語への切り替えを確認しますか？","確認");
                dialog4.txtSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog4.dismiss();
                        switchLanguage("riyu");
                    }
                });
                dialog4.show();
                break;
            case R.id.txt_language5:
                TwoBtnDialog dialog5 = new TwoBtnDialog(SwitchLanguageActivity.this,"Tipps","Sind Sie sicher, auf Deutsch zu wechseln","bestätigen");
                dialog5.txtSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog5.dismiss();
                        switchLanguage("deyu");
                    }
                });
                dialog5.show();
                break;
        }
    }

    private void switchLanguage(String language) {//设置应用语言类型
        SPUtils.put(this, "myLanguage", language);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("jianti")) {//简体中文
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals("fanti")) {//繁体中文
            config.locale = Locale.TRADITIONAL_CHINESE;
        } else if (language.equals("yingyu")) {//英语
            config.locale = Locale.ENGLISH;
        } else if (language.equals("riyu")) {//日语
            config.locale = Locale.JAPANESE;
        } else if (language.equals("deyu")) {//德语
            config.locale = Locale.GERMAN;
        }else {
            config.locale = Locale.getDefault();
        }
        resources.updateConfiguration(config, dm);
        //更新语言后，destroy当前页面，重新绘制
        finish();
        Intent it = new Intent(SwitchLanguageActivity.this, HomeActivity.class);
        //清空任务栈确保当前打开activit为前台任务栈栈顶
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it);
    }
}
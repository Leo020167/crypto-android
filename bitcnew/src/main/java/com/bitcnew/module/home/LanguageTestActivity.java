package com.bitcnew.module.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.R;

import java.util.Locale;

/**
 * Created by zhengmj on 18-10-10.
 */

public class LanguageTestActivity extends TJRBaseToolBarSwipeBackActivity {


    @Override
    protected int setLayoutId() {
        return R.layout.language_test;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.test);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale locale = Locale.getDefault();
        Log.d("getLocaleByLanguage", "LanguageTestActivity locale.getLanguage()==" + locale.getLanguage());
        findViewById(R.id.tvSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageJumpUtil.pageJump(LanguageTestActivity.this, LanguageSettingActivity.class);
            }
        });

    }


}

package com.bitcnew.module.dialog;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.R;

public class TextDialog extends Dialog {

    public TextView txt1,txt2,txt3;
    Context context;

    public TextDialog(@NonNull Context context, String title1, String title2, String title3) {
        super(context, R.style.progress_style);
        this.context = context;
        setContentView(R.layout.dialog_text);
        //使得点击对话框外部不消失对话框
        setCanceledOnTouchOutside(false);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        txt3 = (TextView) findViewById(R.id.txt3);

        if (!TextUtils.isEmpty(title1)){
            txt1.setText(title1);
        }
        if (!TextUtils.isEmpty(title2)){
            txt2.setText(title2);
        }
        if (!TextUtils.isEmpty(title3)){
            txt3.setText(title3);
        }
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy("MD5:"+title1);
            }
        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy("SHA1"+title2);
            }
        });
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy("SHA256:"+title3);
            }
        });
    }

    public final void dismiss() {
        super.dismiss();
    }

    public final void show() {
        super.show();
    }


    private void copy(String data) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
        // newHtmlText、
        // newIntent、
        // newUri、
        // newRawUri
        ClipData clipData = ClipData.newPlainText(null, data);

        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }
}

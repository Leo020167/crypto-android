package com.bitcnew.module.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.R;

public class TwoBtnDialog extends Dialog {

    public TextView txtTitle,txtContent,txtClose,txtSure;
    Context context;

    public TwoBtnDialog(@NonNull Context context, String title, String content, String sure) {
        super(context, R.style.progress_style);
        this.context = context;
        setContentView(R.layout.dialog_two_btn);
        //使得点击对话框外部不消失对话框
        setCanceledOnTouchOutside(false);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        txtContent = (TextView) findViewById(R.id.txt_content);
        txtSure = (TextView) findViewById(R.id.txt_sure);
        txtClose = (TextView) findViewById(R.id.txt_close);

        if (!TextUtils.isEmpty(title)){
            txtTitle.setText(title);
        }
        if (!TextUtils.isEmpty(content)){
            txtContent.setText(content);
        }
        if (!TextUtils.isEmpty(sure)){
            txtSure.setText(sure);
        }
        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public final void dismiss() {
        super.dismiss();
    }

    public final void show() {
        super.show();
    }
}


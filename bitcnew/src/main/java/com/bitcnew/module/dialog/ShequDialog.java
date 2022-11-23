package com.bitcnew.module.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bitcnew.R;

public class ShequDialog  extends Dialog {

    public EditText et_shuliang;
    public TextView txtContent,txtClose,txtSure;
    Context context;


    public ShequDialog(@NonNull Context context,String inviteCodePrice) {
        super(context, R.style.progress_style);
        this.context = context;
        setContentView(R.layout.dialog_shequ);
        //使得点击对话框外部不消失对话框
        setCanceledOnTouchOutside(false);
        et_shuliang = (EditText) findViewById(R.id.et_shuliang);
        txtContent = (TextView) findViewById(R.id.txt_content);
        txtSure = (TextView) findViewById(R.id.txt_sure);
        txtClose = (TextView) findViewById(R.id.txt_close);

        if (!TextUtils.isEmpty(inviteCodePrice)){
            txtContent.setText(context.getResources().getString(R.string.keduihuanshuliang)+inviteCodePrice);
        }else {
            txtContent.setText(context.getResources().getString(R.string.keduihuanshuliang)+"0");
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

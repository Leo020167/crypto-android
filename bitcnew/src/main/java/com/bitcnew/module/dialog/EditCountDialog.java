package com.bitcnew.module.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bitcnew.R;

public class EditCountDialog extends Dialog {

    public TextView txtClose,txtSure,txt_quaneshengou;
    public EditText etContent;
    Context context;

    public EditCountDialog(@NonNull Context context) {
        super(context, R.style.progress_style);
        this.context = context;
        setContentView(R.layout.dialog_edit_count);
        //使得点击对话框外部不消失对话框
        setCanceledOnTouchOutside(false);
        etContent = (EditText) findViewById(R.id.et_content);
        txtSure = (TextView) findViewById(R.id.txt_sure);
        txtClose = (TextView) findViewById(R.id.txt_close);
        txt_quaneshengou = (TextView) findViewById(R.id.txt_quaneshengou);

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


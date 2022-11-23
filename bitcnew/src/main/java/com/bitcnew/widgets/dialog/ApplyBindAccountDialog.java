package com.bitcnew.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitcnew.R;

public class ApplyBindAccountDialog extends Dialog {
    public TextView txt_content1,txt_content2, txt_content3;
    public ImageView img_back;
    Context context;

    public ApplyBindAccountDialog(@NonNull Context context) {
        super(context, R.style.progress_style);
        this.context = context;
        setContentView(R.layout.dialog_apply_bind_account);
        //使得点击对话框外部不消失对话框
        setCanceledOnTouchOutside(true);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_content1 = (TextView) findViewById(R.id.txt_content1);
        txt_content2 = (TextView) findViewById(R.id.txt_content2);
        txt_content3 = (TextView) findViewById(R.id.txt_content3);

        img_back.setOnClickListener(new View.OnClickListener() {
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

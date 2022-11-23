package com.bitcnew.widgets.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.R;

public class DailiDialog extends Dialog {
    public TextView txt_content,txt_quxiao, txt_queding;
    Context context;

    public DailiDialog(@NonNull Context context,String price) {
        super(context, R.style.progress_style);
        this.context = context;
        setContentView(R.layout.dialog_daili);
        //使得点击对话框外部不消失对话框
        setCanceledOnTouchOutside(false);
        txt_content = (TextView) findViewById(R.id.txt_content);
        txt_quxiao = (TextView) findViewById(R.id.txt_quxiao);
        txt_queding = (TextView) findViewById(R.id.txt_queding);

        if (!TextUtils.isEmpty(price)){
            String title = String.format(context.getResources().getString(R.string.dailifeiwei), price);
            txt_content.setText(title);
        }
        txt_quxiao.setOnClickListener(new View.OnClickListener() {
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

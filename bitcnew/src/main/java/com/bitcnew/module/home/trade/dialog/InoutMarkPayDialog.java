package com.bitcnew.module.home.trade.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.http.widget.dialog.base.AbstractBaseDialog;

/**
 * Created by zhengmj on 19-7-26.
 */

public abstract class InoutMarkPayDialog extends AbstractBaseDialog implements View.OnClickListener {


    private TextView btnClose;
    private TextView btnOk;

    private Context context;

    public InoutMarkPayDialog(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.inout_mark_pay_dialog);
        btnClose = findViewById(R.id.btnClose);
        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                onclickClose();
                break;
            case R.id.btnOk:
                dismiss();
                onclickOk();
                break;
        }
    }

    @Override
    public void onclickClose() {
        dismiss();
    }

    @Override
    public void setDownProgress(int progress) {

    }
}

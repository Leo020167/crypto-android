package com.bitcnew.module.home.trade.dialog;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.http.widget.dialog.base.AbstractBaseDialog;
import com.bitcnew.util.CommonUtil;

/**
 * Created by zhengmj on 19-7-26.
 */

public abstract class CancelOrderDialog extends AbstractBaseDialog implements View.OnClickListener {


    private CheckBox cbSign;
    private TextView btnClose;
    private TextView btnOk;

    private Context context;

    public CancelOrderDialog(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.cancel_order_dialog);
        cbSign = findViewById(R.id.cbSign);
        cbSign.setChecked(true);
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
                if (!cbSign.isChecked()) {
                    CommonUtil.showmessage(context.getResources().getString(R.string.qingquerenmeiyoufukuangeiduifang), context);
                    return;
                }
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

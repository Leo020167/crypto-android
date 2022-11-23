package com.bitcnew.module.legal.dialog;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.http.widget.dialog.base.AbstractBaseDialog;
import com.bitcnew.util.CommonUtil;

/**
 * 退出支付提示
 * Created by zhengmj on 19-7-26.
 */

public abstract class OtcOrderExitPayDialog extends AbstractBaseDialog implements View.OnClickListener {


    private CheckBox cbSign;
    private TextView btnClose;
    private TextView btnOk;
    private TextView tvTime;

    private Context context;

    public OtcOrderExitPayDialog(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.exit_order_pay_tip_dialog);
        cbSign = findViewById(R.id.cbSign);
        btnClose = findViewById(R.id.btnClose);
        btnOk = findViewById(R.id.btnOk);
        tvTime = findViewById(R.id.tvTime);
        btnOk.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    public void setTime(String time) {
        tvTime.setText(context.getResources().getString(R.string.dingdanjiangzai) + time + context.getResources().getString(R.string.houchaoshiquxiao));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                onclickClose();
                break;
            case R.id.btnOk:
                if (!cbSign.isChecked()) {
                    CommonUtil.showmessage(context.getResources().getString(R.string.qingquerenhaimeiyoufukuangeiduifang), context);
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

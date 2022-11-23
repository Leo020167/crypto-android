package com.bitcnew.module.copy.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bitcnew.util.CommonUtil;
import com.bitcnew.R;
import com.bitcnew.http.widget.dialog.base.AbstractBaseDialog;

/**
 * Created by zhengmj on 19-7-26.
 */

public abstract class PayOrderTipsDialog extends AbstractBaseDialog implements View.OnClickListener {


    private TextView btnCopy;
    private TextView btnOk;

    private TextView tvBalanceCny;

    private String balanceCny;
    private Context context;


    public PayOrderTipsDialog(Context context, String balanceCny) {
        super(context);
        this.context = context;
        setContentView(R.layout.pay_order_tips_dialog);

        this.balanceCny = balanceCny;

        btnCopy = findViewById(R.id.btnCopy);
        btnOk = findViewById(R.id.btnOk);

        tvBalanceCny = findViewById(R.id.tvBalanceCny);

        btnOk.setOnClickListener(this);
        btnCopy.setOnClickListener(this);

        tvBalanceCny.setText("HK$ " + balanceCny);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCopy:
                CommonUtil.copyText(context, balanceCny);
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

package com.bitcnew.module.pledge.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcnew.R;
import com.bitcnew.module.pledge.entity.PledgeCoin;

import java.math.BigDecimal;

/**
 * 参与质押
 */
public class PledgeApplyDialog extends Dialog {

    public interface OnConfirmListener {
        void onConfirm(PledgeCoin pledgeCoin, String num);
    }

    private TextView dayCountTv;
    private TextView coinTv;
    private TextView coinTypeTv;
    private TextView minCountTv;
    private EditText numEt;
    private View action_ok;

    private final PledgeCoin pledgeCoin;

    private OnConfirmListener onConfirmListener;

    public PledgeApplyDialog(@NonNull Context context, @NonNull PledgeCoin pledgeCoin, OnConfirmListener onConfirmListener) {
        super(context, R.style.MyDialog);
        this.pledgeCoin = pledgeCoin;
        this.onConfirmListener = onConfirmListener;
        setContentView(R.layout.activity_pledge_apply_dialog);
        dayCountTv = findViewById(R.id.dayCountTv);
        coinTv = findViewById(R.id.coinTv);
        coinTypeTv = findViewById(R.id.coinTypeTv);
        minCountTv = findViewById(R.id.minCountTv);
        numEt = findViewById(R.id.numEt);
        action_ok = findViewById(R.id.action_ok);
        action_ok.setOnClickListener(this::onOkClick);

        dayCountTv.setText(pledgeCoin.duration);
        coinTv.setText(pledgeCoin.profitRate);
        minCountTv.setText(getContext().getString(R.string.zuixiaozhiyashuliang__, pledgeCoin.minCount, pledgeCoin.symbol));
    }

    private void onOkClick(View v) {
        String num = numEt.getText().toString().trim();
        if (num.isEmpty()) {
            Toast.makeText(getContext(), R.string.qingshuruzihyashuliang, Toast.LENGTH_SHORT).show();
            return;
        }

        if (new BigDecimal(num).compareTo(new BigDecimal(pledgeCoin.minCount)) < 0) {
            Toast.makeText(getContext(), getContext().getString(R.string.zuixiaozhiyashuliang__, pledgeCoin.minCount, pledgeCoin.symbol), Toast.LENGTH_SHORT).show();
            return;
        }

        if (null != onConfirmListener) {
            onConfirmListener.onConfirm(pledgeCoin, num);
        }

        dismiss();
    }

}

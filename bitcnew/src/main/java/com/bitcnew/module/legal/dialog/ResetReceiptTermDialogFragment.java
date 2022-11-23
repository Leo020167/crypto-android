package com.bitcnew.module.legal.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bitcnew.R;
import com.bitcnew.http.base.Group;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.legal.adapter.ResetReceiptTermAdapter;
import com.bitcnew.module.myhome.entity.Receipt;
import com.bitcnew.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订单详情修改收款方式
 */

public class
ResetReceiptTermDialogFragment extends DialogFragment {

    @BindView(R.id.rvType)
    RecyclerView rvType;
    private ResetReceiptTermAdapter selectPayWayAdapter;
    private OnItemClick onItemClick;
    private Group<Receipt> receipts;

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static ResetReceiptTermDialogFragment newInstance(Group<Receipt> receipts, OnItemClick onItemClick) {
        ResetReceiptTermDialogFragment dialog = new ResetReceiptTermDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(CommonConst.RECHARGE, recharge);
//        dialog.setArguments(bundle);
        dialog.onItemClick = onItemClick;
        dialog.receipts = receipts;
        return dialog;
    }

    public void showDialog(FragmentManager manager, String tag) {
        this.show(manager, tag);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreate ");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        //入参处理
        Bundle b = getArguments();
        if (null == b) return;

    }

    @Override
    public void onStart() {
        CommonUtil.LogLa(2, "OLStarHomeDialogFragment                      ---> onStart ");
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreateView ");
        View v = inflater.inflate(R.layout.reset_receipt_term_dialog, container, false);
        ButterKnife.bind(this, v);

        selectPayWayAdapter = new ResetReceiptTermAdapter(getActivity());
        rvType.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvType.setAdapter(selectPayWayAdapter);
        selectPayWayAdapter.setOnItemClick(onItemClick);
        selectPayWayAdapter.setGroup(receipts);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}

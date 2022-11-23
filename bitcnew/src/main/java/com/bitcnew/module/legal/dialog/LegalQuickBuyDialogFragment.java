package com.bitcnew.module.legal.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.legal.LegalOrderInfoActivity;
import com.bitcnew.module.legal.adapter.SelectQuickPayWayAdapter;
import com.bitcnew.module.legal.entity.OptionalOrder;
import com.bitcnew.module.myhome.entity.AddPaymentTern;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.SimpleSpaceItemDecoration;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 快捷购买弹出框
 */

public class LegalQuickBuyDialogFragment extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rvType)
    RecyclerView rvType;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    private SelectQuickPayWayAdapter selectPayWayAdapter;


    private OptionalOrder optionalOrder;
    private String amount = "0.00",fuhao;
    private Call<ResponseBody> otcCreateOrderCall;

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static LegalQuickBuyDialogFragment newInstance(OptionalOrder optionalOrder, String amount,String fuhao) {
        LegalQuickBuyDialogFragment dialog = new LegalQuickBuyDialogFragment();
        dialog.optionalOrder = optionalOrder;
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.ENTRUSTAMOUNT, amount);
        bundle.putString("fuhao",fuhao);
        dialog.setArguments(bundle);
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
        View v = inflater.inflate(R.layout.legal_quick_buy_dialog, container, false);
        ButterKnife.bind(this, v);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ENTRUSTAMOUNT)) {
                amount = bundle.getString(CommonConst.ENTRUSTAMOUNT, "0.00");
            }
            if (bundle.containsKey("fuhao")) {
                fuhao = bundle.getString("fuhao", "");
            }
        }


        selectPayWayAdapter = new SelectQuickPayWayAdapter(getActivity());
        rvType.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvType.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 3, 3, 3, 3));
        rvType.setAdapter(selectPayWayAdapter);
        selectPayWayAdapter.setOnItemclickListen(new SelectQuickPayWayAdapter.onItemclickListen() {
            @Override
            public void onItemclick(AddPaymentTern receipt) {
            }
        });

        if (optionalOrder != null) {
            Group<AddPaymentTern> payWay = new Gson().fromJson(optionalOrder.payWay, new TypeToken<Group<AddPaymentTern>>() {
            }.getType());
            selectPayWayAdapter.setGroup(payWay);
        }

        tvPrice.setText(optionalOrder.price + fuhao+"/USDT");
        tvAmount.setText(amount + " USDT");

        BigDecimal tolBalanceBD = new BigDecimal(amount).multiply(new BigDecimal(optionalOrder.price)).setScale(2, BigDecimal.ROUND_FLOOR);
        if (!TextUtils.isEmpty(fuhao)){
            if (fuhao.equals("CNY")){
                tvMoney.setText("￥" + tolBalanceBD.toPlainString());
            }else if (fuhao.equals("HKD")){
                tvMoney.setText("HK＄" + tolBalanceBD.toPlainString());
            }else if (fuhao.equals("USD")){
                tvMoney.setText("＄" + tolBalanceBD.toPlainString());
            }else {
                tvMoney.setText(tolBalanceBD.toPlainString());
            }
        }else {
            tvMoney.setText(tolBalanceBD.toPlainString());
        }


        tvSubmit.setOnClickListener(this);

//        Group<Receipt> selectPayWayGroup=new Group<>();
//        Receipt selectPayWay=null;
//        for (int i = 0; i < 3; i++) {
//             selectPayWay=new Receipt();
//            selectPayWay.bankName="银行卡"+i;
//            selectPayWay.paymentId=i;
//            selectPayWayGroup.add(selectPayWay);
//        }
//        selectPayWayAdapter.setGroup(selectPayWayGroup);
//
//        for (Receipt receipt : selectPayWayAdapter.getGroup()) {
//            if ("1".equals(receipt.paymentId)) {
//                selectPayWayAdapter.setSelected(receipt);
//                break;
//            }
//        }
        return v;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void startOtcCreateOrderCall(String buySell, long adId, String amount, String price, int receiptType) {
        CommonUtil.cancelCall(otcCreateOrderCall);
        ((TJRBaseToolBarActivity) getActivity()).showProgressDialog();
        otcCreateOrderCall = VHttpServiceManager.getInstance().getVService().otcCreateOrder(buySell, adId, amount, price, String.valueOf(receiptType));
        otcCreateOrderCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (getActivity() == null) return;
                ((TJRBaseToolBarActivity) getActivity()).dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, getActivity());
                    long orderId = resultData.getItem("orderId", Long.class);
                    LegalOrderInfoActivity.pageJump(getActivity(), orderId);
                    dismiss();

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                if (getActivity() != null) {
                    ((TJRBaseToolBarActivity) getActivity()).dismissProgressDialog();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                if (optionalOrder == null) return;

                int receiptType = selectPayWayAdapter.getSelectReceiptType();
                if (receiptType == -1) {
                    CommonUtil.showmessage(getResources().getString(R.string.qingxuanzeshangjiashoukuanfangshi), getActivity());
                    return;
                }
                startOtcCreateOrderCall("buy", optionalOrder.adId, amount, optionalOrder.price, receiptType);
                break;

            default:
                break;
        }
    }


}

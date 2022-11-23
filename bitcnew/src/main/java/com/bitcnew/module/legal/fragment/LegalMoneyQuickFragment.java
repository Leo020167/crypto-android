package com.bitcnew.module.legal.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bitcnew.module.dialog.TwoBtnDialog;
import com.bitcnew.module.home.ShimingrenzhengtongguoActivity;
import com.bitcnew.module.home.adapter.FuhaoAdapter;
import com.bitcnew.module.home.bean.OtcConfigBean;
import com.bitcnew.module.legal.entity.OtcCertification;
import com.bitcnew.module.myhome.IdentityAuthenActivity;
import com.bitcnew.module.myhome.SettingActivity;
import com.bitcnew.module.myhome.entity.IdentityAuthen;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bitcnew.R;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.data.sharedpreferences.PrivateChatSharedPreferences;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.BindEmailActivity;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.PhoneAuthCodeActivity;
import com.bitcnew.module.home.fragment.UserBaseFragment;
import com.bitcnew.module.home.trade.TransferCoinActivity;
import com.bitcnew.module.legal.AuthenticationActivity;
import com.bitcnew.module.legal.MyAdListActivity;
import com.bitcnew.module.legal.OtcOrderHistoryActivity;
import com.bitcnew.module.legal.dialog.ConfirmSellDialogFragment;
import com.bitcnew.module.legal.dialog.LegalQuickBuyDialogFragment;
import com.bitcnew.module.legal.dialog.SelectReceiptTermDialogFragment;
import com.bitcnew.module.legal.entity.OptionalOrder;
import com.bitcnew.module.myhome.PaymentTermActivity;
import com.bitcnew.module.myhome.entity.Receipt;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.DensityUtil;
import com.bitcnew.util.InflaterUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.BadgeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 快捷区
 * Created by zhengmj on 19-3-8.
 */

public class LegalMoneyQuickFragment extends UserBaseFragment implements View.OnClickListener {


    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.tvOrientationText)
    TextView tvOrientationText;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvBalance)
    TextView tvBalance;

    @BindView(R.id.ivHistory)
    AppCompatImageView ivHistory;
    @BindView(R.id.ivMore)
    AppCompatImageView ivMore;
    @BindView(R.id.llTransfer)
    LinearLayout llTransfer;
    @BindView(R.id.ll_fuhao)
    LinearLayout ll_fuhao;
    @BindView(R.id.txt_fuhao)
    TextView txt_fuhao;
    @BindView(R.id.txt_danwei)
    TextView txt_danwei;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvAll)
    TextView tvAll;

    private int type = 0;//0代表买，1卖
    private String holdAmount = "0.00";
    private int amount_decimalcount = 2;//数量的小数点数量

    private Call<ResponseBody> otcFindAdListCall;
    private Call<ResponseBody> outHoldAmountCall;


    private LegalQuickBuyDialogFragment legalQuickBuyDialogFragment;
    private SelectReceiptTermDialogFragment selectReceiptTermDialogFragment;


    private ConfirmSellDialogFragment confirmSellDialogFragment;

    private OptionalOrder optionalOrder;

    private BadgeView badgeChat;

    public static LegalMoneyQuickFragment newInstance() {
        LegalMoneyQuickFragment fragment = new LegalMoneyQuickFragment();
        Bundle bundle = new Bundle();
//        bundle.putString("tab", tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        startGetMyHomeCallCall();
        if (getUserVisibleHint()) {
            startOtcFindAdListCall();
            showPrivateChatNewsCount();

        }
        Log.d("onresumeTest", "onResume/////////=" + getClass() + "  getUserVisibleHint==" + getUserVisibleHint()+"  badgeChat=="+badgeChat);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("setUserVisibleHint", "isVisibleToUser==" + isVisibleToUser + "      " + getClass()+"  badgeChat=="+badgeChat);
        if (isVisibleToUser) {
            startOtcFindAdListCall();
            showPrivateChatNewsCount();
        }
    }

    private void startOtcFindAdListCall() {
        CommonUtil.cancelCall(otcFindAdListCall);
        otcFindAdListCall = VHttpServiceManager.getInstance().getVService().otcFindAdList(type == 0 ? "buy" : "sell", 0, "", 1, "fast",fuhao);
        otcFindAdListCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<OptionalOrder> group = resultData.getGroup("data", new TypeToken<Group<OptionalOrder>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        optionalOrder = group.get(0);
                        if (optionalOrder != null) {
                            tvPrice.setText(optionalOrder.price);
                            tvLimit.setText(getResources().getString(R.string.xiane) + optionalOrder.minCny+"USDT" + "-" + optionalOrder.maxCny+"USDT");
                        }
                    }

                    if (type == 1) {
                        startOutHoldAmountCall();
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }

        });
    }

    public void startOutHoldAmountCall() {
        CommonUtil.cancelCall(outHoldAmountCall);
        outHoldAmountCall = VHttpServiceManager.getInstance().getVService().outHoldAmount("balance");
        outHoldAmountCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    holdAmount = resultData.getItem("holdAmount", String.class);
                    tvBalance.setText(getResources().getString(R.string.yue) + holdAmount + " USDT");
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal_money_quick, container, false);
        ButterKnife.bind(this, view);
//        Bundle bundle = getArguments();
//        if (bundle != null && bundle.containsKey("tab")) {
//            tab = bundle.getString("tab");
//            Log.d("MarkOptionalFragment", "tab==" + tab);
//        }
        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        ivHistory.setOnClickListener(this);
        llTransfer.setOnClickListener(this);
        ll_fuhao.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                int posDot = s.toString().indexOf(".");
                if (0 == posDot) {//去除首位的"."
                    s.delete(0, 1);
                } else if (posDot > 0) {
                    if (s.length() - 1 - posDot > amount_decimalcount) {
                        s.delete(posDot + (amount_decimalcount + 1), posDot + (amount_decimalcount + 2));
                    }
                }
//                if (currCoin == null) return;
//                setLastAmountText(s.toString());
            }
        });
        if (savedInstanceState != null) {
            type = savedInstanceState.getInt(CommonConst.KEY_EXTRAS_TYPE);
        }
        switchType(type);

        badgeChat = new BadgeView(getActivity(), ivHistory);
        badgeChat.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));
        badgeChat.setBadgeMargin(15, 10);
        badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        showPrivateChatNewsCount();

        getOtcConfig();
        return view;
    }


    public void showPrivateChatNewsCount() {
        if(badgeChat==null)return;
        int chatCount = 0;
        if (getUser() != null) {
            chatCount = PrivateChatSharedPreferences.getAllPriChatRecordNum(getContext(),  getUser().getUserId());
        }
        Log.d("setChatNewsCount", "chatCount==" + chatCount);
        if (chatCount > 0) {//显示
            badgeChat.show();
            badgeChat.setBadgeText(com.bitcnew.util.CommonUtil.setNewsCount(chatCount));
        } else {//不显示
            badgeChat.hide();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CommonConst.KEY_EXTRAS_TYPE, type);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void refresh() {
        if (selectReceiptTermDialogFragment != null && selectReceiptTermDialogFragment.isAdded()) {
            selectReceiptTermDialogFragment.startOtcFindMyPaymentList();
        }
    }

    public void showLegalQuickSellDialogFragment(final String amount) {
        selectReceiptTermDialogFragment = SelectReceiptTermDialogFragment.newInstance(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                if (selectReceiptTermDialogFragment != null) {
                    selectReceiptTermDialogFragment.dismiss();
                }

                Receipt receipt = (Receipt) t;
                showConfirmSellDialogFragment(receipt, amount);
            }
        });

        selectReceiptTermDialogFragment.showDialog(getChildFragmentManager(), "");
    }


    public void showLegalQuickBuyDialogFragment(String amount) {
        legalQuickBuyDialogFragment = LegalQuickBuyDialogFragment.newInstance(optionalOrder, amount,fuhao);
        legalQuickBuyDialogFragment.showDialog(getChildFragmentManager(), "");
    }


    public void showConfirmSellDialogFragment(Receipt receipt, String amount) {
        confirmSellDialogFragment = ConfirmSellDialogFragment.newInstance(optionalOrder, receipt, amount,fuhao);
        confirmSellDialogFragment.showDialog(getChildFragmentManager(), "");
    }

    private String amount;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuy:
                switchType(0);
                break;
            case R.id.tvSell:
                switchType(1);
                break;
            case R.id.tvSubmit:
                if (optionalOrder == null) {
                    return;
                }
                amount = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount) || Double.parseDouble(amount) == 0) {
                    if (type == 0) {
                        CommonUtil.showmessage(getResources().getString(R.string.qingshurugoumaishuliang), getActivity());
                    } else {
                        CommonUtil.showmessage(getResources().getString(R.string.qingshurumaichushuliang), getActivity());

                    }
                    return;
                }
                startGetIdentityAuthen();
                break;
            case R.id.ivMore:
                showPopMoreMenu(v);
                break;
            case R.id.ivHistory:
                PageJumpUtil.pageJump(getActivity(), OtcOrderHistoryActivity.class);
                break;
            case R.id.llPublish://我的广告
                startGetotcCertificationInfoCall();
                break;
            case R.id.llAuthen:
                dissPopMore();
                PageJumpUtil.pageJump(getActivity(), AuthenticationActivity.class);
                break;
            case R.id.llReceiptManager:
                dissPopMore();
                PageJumpUtil.pageJump(getActivity(), PaymentTermActivity.class);
                break;
            case R.id.llTransfer:
                PageJumpUtil.pageJump(getActivity(), TransferCoinActivity.class);
                break;
            case R.id.ll_fuhao:
                if (null!=fuhao_list&&fuhao_list.size()>0){
                    showPopMoreMenu2(v);
                }
                break;
            case R.id.tvAll:
                etAmount.setText(holdAmount);
                etAmount.setSelection(etAmount.getText().toString().length());
                break;
        }
    }
    private IdentityAuthen identityAuthen;
    private Call<ResponseBody> getIdentityAuthenCall;
    private void startGetIdentityAuthen() {//是否已实名认证
        getIdentityAuthenCall = VHttpServiceManager.getInstance().getVService().getIdentityAuthen();
        getIdentityAuthenCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    identityAuthen = resultData.getObject("identityAuth", IdentityAuthen.class);//0：审核中，1：已通过，2：未通过
                    if (null!=identityAuthen){
                        if (identityAuthen.state==1){
                            goSub();
                        }else{
                            goAuth();
                        }
                    }else{
                        goAuth();
                    }
                }
            }
        });
    }
    private void goAuth(){
        TwoBtnDialog dialog = new TwoBtnDialog(getActivity(),getActivity().getResources().getString(R.string.tishi),"帳戶未實名","去認證");
        dialog.txtSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PageJumpUtil.pageJump(getActivity(), IdentityAuthenActivity.class);
            }
        });
        dialog.show();
    }
    private void goSub(){
        if (type == 0) {
            showLegalQuickBuyDialogFragment(amount);
        } else {//我要卖
            showLegalQuickSellDialogFragment(amount);
//                    if (!TextUtils.isEmpty(phone)){//如果绑定了手机号，去验证手机号
//                        Intent intent = new Intent(getActivity(), PhoneAuthCodeActivity.class);
//                        intent.putExtra("phone",phone);
//                        startActivityForResult(intent,101);
//                    }else{
//                        if (!TextUtils.isEmpty(email)){//如果绑定了邮箱，去验证邮箱
//                            Intent intent = new Intent(getActivity(), PhoneAuthCodeActivity.class);
//                            intent.putExtra("email",email);
//                            startActivityForResult(intent,101);
//                        }else{//先去绑定
//                            Intent intent = new Intent(getActivity(), BindEmailActivity.class);
//                            startActivity(intent);
//                        }
//                    }
        }
    }


    private String fuhao="CNY";
    private Call<ResponseBody> getOtcConfigCall;
    private void getOtcConfig() {//获取币种的符号
        CommonUtil.cancelCall(getOtcConfigCall);
        getOtcConfigCall = VHttpServiceManager.getInstance().getVService().otcConfig();
        getOtcConfigCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    OtcConfigBean bean = gson.fromJson(resultData.data, OtcConfigBean.class);
                    if (null!=bean){
                        if (null!=bean.getCurrencies()&&bean.getCurrencies().size()>0){
                            fuhao_list.clear();
                            fuhao_list.addAll(bean.getCurrencies());
                            fuhao = fuhao_list.get(0);
                            txt_fuhao.setText(fuhao);
                            txt_danwei.setText(fuhao+"/USDT");
                        }else {
                            fuhao_list.clear();
                        }
                    }else {
                        fuhao_list.clear();
                    }
                }
            }
        });
    }


    private FuhaoAdapter fuhaoAdapter;
    private PopupWindow popMore2;
    private List<String> fuhao_list = new ArrayList<>();
    private RecyclerView recyclerView_fuhao;
    private void showPopMoreMenu2(View parent) {
        if (popMore2 == null) {
            View view = InflaterUtils.inflateView(getActivity(), R.layout.pop_legal_more2);
            popMore2 = new PopupWindow(view, DensityUtil.dip2px(getActivity(), 150), ViewGroup.LayoutParams.WRAP_CONTENT);//

            recyclerView_fuhao = view.findViewById(R.id.recyclerView_fuhao);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            recyclerView_fuhao.setLayoutManager(linearLayoutManager);
            fuhaoAdapter = new FuhaoAdapter(getActivity(),fuhao_list);
            recyclerView_fuhao.setAdapter(fuhaoAdapter);
            fuhaoAdapter.setOnPlayClickListener(new FuhaoAdapter.OnPlayClickListener() {
                @Override
                public void onSelClick(int pos) {
                    dissPopMore2();
                    fuhao = fuhao_list.get(pos);
                    txt_fuhao.setText(fuhao);
                    txt_danwei.setText(fuhao+"/USDT");
                    startOtcFindAdListCall();
                }
            });
            fuhaoAdapter.notifyDataSetChanged();

            popMore2.setOutsideTouchable(false);
            popMore2.setFocusable(false);//
            popMore2.setOutsideTouchable(true);
            popMore2.setFocusable(true);
            popMore2.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
            popMore2.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

        }
        if (popMore2 != null && !popMore2.isShowing()) {
//            pop.showAsDropDown(parent);
            popMore2.showAsDropDown(parent, -20, -20);
//            pop.showAtLocation(parent, Gravity.TOP,0,0);
        }
    }

    private void dissPopMore2() {
        if (popMore2 != null & popMore2.isShowing()) {
            popMore2.dismiss();
        }
    }
    private Call<ResponseBody> otcGetCertificationInfoCall;
    private void startGetotcCertificationInfoCall() {//获取是否已商家认证
        CommonUtil.cancelCall(otcGetCertificationInfoCall);
        otcGetCertificationInfoCall = VHttpServiceManager.getInstance().getVService().otcGetCertificationInfo();
        otcGetCertificationInfoCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    OtcCertification otcCertification = resultData.getObject("otcCertification", OtcCertification.class);
                    if (otcCertification.state == 2) {
                        dissPopMore();
                        PageJumpUtil.pageJump(getActivity(), MyAdListActivity.class);
                    } else {
                        CommonUtil.showmessage("請先進行商家認證", getActivity());
                    }
                }
            }
        });
    }


    private String email,phone;
    private Call<ResponseBody> getUserInfoCall;
    private void startGetMyHomeCallCall() {
        getUserInfoCall = VHttpServiceManager.getInstance().getVService().myUserInfo();
        getUserInfoCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    email = resultData.getItem("email", String.class);
                    phone = resultData.getItem("phone", String.class);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101&&resultCode==1){
            String success = data.getStringExtra("success");
            if (!TextUtils.isEmpty(success)){
                showLegalQuickSellDialogFragment(amount);
            }
        }
    }

    private PopupWindow popMore;
    private LinearLayout llPublish, llAuthen, llReceiptManager;

    private void showPopMoreMenu(View parent) {
        if (popMore == null) {
            View view = InflaterUtils.inflateView(getActivity(), R.layout.pop_legal_more);
            popMore = new PopupWindow(view, DensityUtil.dip2px(getActivity(), 150), ViewGroup.LayoutParams.WRAP_CONTENT);//
            llPublish = view.findViewById(R.id.llPublish);
            llAuthen = view.findViewById(R.id.llAuthen);
            llReceiptManager = view.findViewById(R.id.llReceiptManager);

            llPublish.setOnClickListener(this);
            llAuthen.setOnClickListener(this);
            llReceiptManager.setOnClickListener(this);

            popMore.setOutsideTouchable(false);
            popMore.setFocusable(false);//
            popMore.setOutsideTouchable(true);
            popMore.setFocusable(true);
            popMore.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
            popMore.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

        }
        if (popMore != null && !popMore.isShowing()) {
//            pop.showAsDropDown(parent);
            popMore.showAsDropDown(parent, -20, -20);
//            pop.showAtLocation(parent, Gravity.TOP,0,0);
        }
    }

    private void dissPopMore() {
        if (popMore != null & popMore.isShowing()) {
            popMore.dismiss();
        }
    }

    private void switchType(int type) {
        this.type = type;
        if (type == 0) {
            tvBuy.setSelected(true);
            tvSell.setSelected(false);
            tvBuy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            tvSell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tvOrientationText.setText(getResources().getString(R.string.goumaishuliang));
            tvSubmit.setText(getResources().getString(R.string.lingshouxufeigoumai));
            llTransfer.setVisibility(View.GONE);
            tvBalance.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
            tvAll.setVisibility(View.GONE);
        } else {
            tvBuy.setSelected(false);
            tvSell.setSelected(true);
            tvBuy.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tvSell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            tvOrientationText.setText(getResources().getString(R.string.chushoushuliang));
            tvSubmit.setText(getResources().getString(R.string.lingshouxufeigoumai));
            llTransfer.setVisibility(View.VISIBLE);
            tvBalance.setVisibility(View.VISIBLE);
            vLine.setVisibility(View.VISIBLE);
            tvAll.setVisibility(View.VISIBLE);
        }

        startOtcFindAdListCall();
    }


}

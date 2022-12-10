package com.bitcnew.module.home.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.MainApplication;
import com.bitcnew.R;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.web.CommonWebViewActivity;
import com.bitcnew.data.sharedpreferences.NormalShareData;
import com.bitcnew.data.sharedpreferences.PrivateChatSharedPreferences;
import com.bitcnew.http.model.User;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.chat.ChatRoomActivity;
import com.bitcnew.module.home.BindEmailActivity;
import com.bitcnew.module.home.DailiActivity;
import com.bitcnew.module.home.EmailAuthActivity;
import com.bitcnew.module.home.HomeActivity;
import com.bitcnew.module.home.JiaoyijiluActivity;
import com.bitcnew.module.home.ShequActivity;
import com.bitcnew.module.home.ShimingrenzhengtongguoActivity;
import com.bitcnew.module.home.TakePhotoActivity;
import com.bitcnew.module.home.WebActivity;
import com.bitcnew.module.home.ZichanActivity;
import com.bitcnew.module.home.trade.RechargeCoinActivity;
import com.bitcnew.module.home.trade.TakeCoinActivity;
import com.bitcnew.module.home.trade.TransferCoinActivity;
import com.bitcnew.module.home.trade.history.CoinFollowHistoryActivity;
import com.bitcnew.module.home.trade.history.CoinTradeEntrustLeverActivity;
import com.bitcnew.module.legal.LegalMoneyActivity;
import com.bitcnew.module.myhome.IdentityAuthenActivity;
import com.bitcnew.module.myhome.MyHomeInfoActivity;
import com.bitcnew.module.myhome.MyMessageActivity;
import com.bitcnew.module.myhome.PaymentTermActivity;
import com.bitcnew.module.myhome.SettingActivity;
import com.bitcnew.module.myhome.UserHomeActivity;
import com.bitcnew.module.myhome.entity.IdentityAuthen;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.BadgeView;
import com.bitcnew.widgets.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 19-1-29.
 */

public class HomeMineFragment extends UserBaseImmersionBarFragment implements View.OnClickListener {
    @BindView(R.id.llIdentityAuthen)
    LinearLayout llIdentityAuthen;
    @BindView(R.id.llBindEmail)
    LinearLayout llBindEmail;

    @BindView(R.id.ll_bar)
    LinearLayout ll_bar;
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvDesc)
    TextView tvDesc;

    @BindView(R.id.llHead)
    LinearLayout llHead;

    @BindView(R.id.llNews)
    LinearLayout llNews;
    @BindView(R.id.ivSetting)
    LinearLayout ivSetting;

    @BindView(R.id.llZichan)
    LinearLayout llZichan;

    @BindView(R.id.llRechargeCoin)
    LinearLayout llRechargeCoin;
    @BindView(R.id.llWithDrawCoin)
    LinearLayout llWithDrawCoin;
    @BindView(R.id.llTransfer)
    LinearLayout llTransfer;
    @BindView(R.id.llLegal)
    LinearLayout llLegal;

    @BindView(R.id.llFollowHis)
    LinearLayout llFollowHis;

    @BindView(R.id.ivGoHome)
    ImageView ivGoHome;


    @BindView(R.id.llFeedback)
    LinearLayout llFeedback;
    @BindView(R.id.tvFeedback)
    TextView tvFeedback;
    @BindView(R.id.llHelp)
    LinearLayout llHelp;
    //    @BindView(R.id.vPointOfEdit)
//    View vPointOfEdit;
    @BindView(R.id.tvLatestMsgTitle)
    TextView tvLatestMsgTitle;
    @BindView(R.id.flLatestMsgTitle)
    FrameLayout flLatestMsgTitle;

    @BindView(R.id.tvUsdtBalance)
    TextView tvUsdtBalance;
    @BindView(R.id.llFuturesHis)
    LinearLayout llFuturesHis;
    @BindView(R.id.llJiaoyijilu)
    LinearLayout llJiaoyijilu;
    @BindView(R.id.llDaili)
    LinearLayout llDaili;
    @BindView(R.id.llYouhuiquan)
    LinearLayout llYouhuiquan;
    @BindView(R.id.llDigitalHis)
    LinearLayout llDigitalHis;

    @BindView(R.id.llShareApp)
    LinearLayout llShareApp;
    @BindView(R.id.ivLegal)
    AppCompatImageView ivLegal;


    private String shareUrl;
    private String latestMsgTitle = "";

    private String symbol = "";
    private String byyAmount = "";
    private String usdtBalance = "";
    private String helpCenterUrl = "";


    private MainApplication application;
    private TjrImageLoaderUtil tjrImageLoaderUtil;

    private BadgeView badgePrivateChat;
    private BadgeView badgeChat;

    private Call<ResponseBody> createChatTopicCall;
    private String chatTopic;
    private String headUrl;
    private String userName;
    private int type;

    private Call<ResponseBody> getMyHomeCall;

    private TjrBaseDialog abilityDialog;

    private String showCoin;


    public static HomeMineFragment newInstance() {
        HomeMineFragment fragment = new HomeMineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        tjrImageLoaderUtil = new TjrImageLoaderUtil(R.drawable.ic_common_mic2);
        try {
            application = (MainApplication) getActivity().getApplicationContext();
//            user = application.getUser();
        } catch (Exception e) {

        }
    }

    public void setShowCoin(String showCoin) {
        this.showCoin = showCoin;
//        if(tvShowCoin!=null){
//            if (!TextUtils.isEmpty(showCoin)) {
//                tvShowCoin.setVisibility(View.VISIBLE);
//                tvShowCoin.setText(showCoin);
//            } else {
//                tvShowCoin.setVisibility(View.GONE);
//            }
//
//        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
//        flHead.setOnClickListener(this);
        llHead.setOnClickListener(this);
        llNews.setOnClickListener(this);
        ivSetting.setOnClickListener(this);
//        llPrivateChat.setOnClickListener(this);
        llWithDrawCoin.setOnClickListener(this);
        llRechargeCoin.setOnClickListener(this);
        llTransfer.setOnClickListener(this);
        llLegal.setOnClickListener(this);
        llFollowHis.setOnClickListener(this);
        ivGoHome.setOnClickListener(this);
        llFeedback.setOnClickListener(this);
        llHelp.setOnClickListener(this);
        llFuturesHis.setOnClickListener(this);
        llJiaoyijilu.setOnClickListener(this);
        llDaili.setOnClickListener(this);
        llYouhuiquan.setOnClickListener(this);
        llDigitalHis.setOnClickListener(this);
        llShareApp.setOnClickListener(this);
        llIdentityAuthen.setOnClickListener(this);
        llBindEmail.setOnClickListener(this);
        llZichan.setOnClickListener(this);

        badgePrivateChat = new BadgeView(getActivity(), tvFeedback);
        badgePrivateChat.setBadgeMargin(0, 0);
        badgePrivateChat.setBadgePosition(BadgeView.POSITION_VERTICAL_RIGHT);
        badgePrivateChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        badgeChat = new BadgeView(getActivity(), ivLegal);
        badgeChat.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));
        badgeChat.setBadgeMargin(15, 10);
        badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        setUserInfo(getUser());//先初始化
        setPrivateChatNewsCount();
        showLegalPrivateChatNewsCount();
//        setShowCoin(showCoin);
//        setEditPointFlag();


        Log.d("V_AccountFragment", "onCreateView  getUserVisibleHint==" + getUserVisibleHint());
        return view;
    }


    private void startGetMyHomeCallCall() {
        if (getUser() == null || TextUtils.isEmpty(getUserId())) return;
        CommonUtil.cancelCall(getMyHomeCall);
        getMyHomeCall = VHttpServiceManager.getInstance().getVService().myHome();
        getMyHomeCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    latestMsgTitle = "";
                    shareUrl = resultData.getItem("shareUrl", String.class);
                    latestMsgTitle = resultData.getItem("latestMsgTitle", String.class);

                    symbol = resultData.getItem("symbol", String.class);
                    byyAmount = resultData.getItem("byyAmount", String.class);
                    usdtBalance = resultData.getItem("usdtBalance", String.class);

                    helpCenterUrl = resultData.getItem("helpCenterUrl", String.class);

                    if (!TextUtils.isEmpty(latestMsgTitle)) {
                        flLatestMsgTitle.setVisibility(View.VISIBLE);
                        tvLatestMsgTitle.setText(latestMsgTitle);
                    } else {
                        flLatestMsgTitle.setVisibility(View.GONE);
                    }
                    tvUsdtBalance.setText(usdtBalance);
//                    tvKBTBalance.setText(kbtAmount);

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    public void showLegalPrivateChatNewsCount() {
        if(badgeChat==null)return;
        int chatCount = 0;
        if (getUser() != null) {
            chatCount = PrivateChatSharedPreferences.getAllPriChatRecordNum(getActivity(), getUser().getUserId());
        }
        Log.d("setChatNewsCount", "chatCount==" + chatCount);
        if (chatCount > 0) {//显示
            badgeChat.show();
            badgeChat.setBadgeText(com.bitcnew.util.CommonUtil.setNewsCount(chatCount));
        } else {//不显示
            badgeChat.hide();
        }
    }

    public void setPrivateChatNewsCount() {
        if (getUser() == null) return;
        if (getActivity() == null) return;
        if (!(getActivity() instanceof HomeActivity)) return;
        if (badgePrivateChat == null) return;
        int serviceChatCount = 0;
        if (getUser() != null) {//现在客服是弹出二维码，青爷说先不加消息数
//            serviceChatCount = PrivateChatSharedPreferences.getAllPriChatRecordNum(getActivity(), getUser().getUserId());
        }
        if (serviceChatCount > 0) {//显示
            badgePrivateChat.show();
            badgePrivateChat.setBadgeText(CommonUtil.setNewsCount(serviceChatCount));
        } else {//不显示
            badgePrivateChat.hide();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llIdentityAuthen:
                if (null!=identityAuthen){
                    if (identityAuthen.state==1){
                        Intent intent = new Intent(getActivity(), ShimingrenzhengtongguoActivity.class);
                        startActivity(intent);
                    }else{
                        PageJumpUtil.pageJump(getActivity(), IdentityAuthenActivity.class);
                    }
                }else{
                    PageJumpUtil.pageJump(getActivity(), IdentityAuthenActivity.class);
                }
                break;
            case R.id.llBindEmail:
                if (!TextUtils.isEmpty(email)){
                    Intent intent = new Intent(getActivity(), EmailAuthActivity.class);
                    intent.putExtra("mode",1);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), BindEmailActivity.class);
                    startActivity(intent);
                }
                break;
//            case R.id.flHead:
            case R.id.llHead:
                PageJumpUtil.pageJump(getActivity(), MyHomeInfoActivity.class);
                if (getUser() != null)
                    NormalShareData.saveUserEditFlag(getActivity(), getUser().userId, 1);
                break;
            case R.id.ivSetting:
                PageJumpUtil.pageJump(getActivity(), SettingActivity.class);
//                Intent intent = new Intent(getActivity(), TakePhotoActivity.class);
//                startActivity(intent);
                break;
            case R.id.llNews:
                PageJumpUtil.pageJump(getActivity(), MyMessageActivity.class);
                break;

            case R.id.llRechargeCoin:
                PageJumpUtil.pageJump(getActivity(), RechargeCoinActivity.class);
//                RechargeCoinActivity.pageJump(getActivity(),"USDT");
                break;
            case R.id.llWithDrawCoin:
                PageJumpUtil.pageJump(getActivity(), TakeCoinActivity.class);
                break;
            case R.id.llTransfer:
                PageJumpUtil.pageJump(getActivity(), TransferCoinActivity.class);
                break;
            case R.id.llLegal:
                PageJumpUtil.pageJump(getActivity(), LegalMoneyActivity.class);
                break;

            case R.id.ivGoHome:
                if (getUser() != null)
                    UserHomeActivity.pageJump(getActivity(), getUser().userId);
                break;
            case R.id.llFeedback:
                if (!TextUtils.isEmpty(chatTopic) && !TextUtils.isEmpty(userName)) {
                    goChatOrWechatQr();
                } else {
                    startCreateChat();
                }
                break;
            case R.id.llPaymentTerm:
                PaymentTermActivity.pageJump(getActivity(), 0);
                break;
            case R.id.llHelp:
                if (!TextUtils.isEmpty(helpCenterUrl)) {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), helpCenterUrl);
                }
                break;
            case R.id.llDigitalHis:
                CoinTradeEntrustLeverActivity.pageJump(getActivity(), "digital");
                break;
            case R.id.llFuturesHis:
                CoinTradeEntrustLeverActivity.pageJump(getActivity(), "stock");
                break;
            case R.id.llJiaoyijilu://交易记录
                JiaoyijiluActivity.pageJump(getActivity(), "follow");
                break;
            case R.id.llDaili://代理
                Intent intent2 = new Intent(getActivity(), ShequActivity.class);
                startActivity(intent2);
                break;
            case R.id.llYouhuiquan:
                Intent intent3= new Intent(getActivity(), WebActivity.class);
                startActivity(intent3);
                break;
            case R.id.llFollowHis:
//                PageJumpUtil.pageJump(getActivity(), CropyOrderHistoryActivity.class);
                PageJumpUtil.pageJump(getActivity(), CoinFollowHistoryActivity.class);
                break;
            case R.id.llShareApp:
                if (!TextUtils.isEmpty(shareUrl)) {
                    showShareDialogFragment();
                }
                break;
            case R.id.llZichan: // 资产
                PageJumpUtil.pageJump(getActivity(), ZichanActivity.class);
                break;
        }
    }

    private IdentityAuthen identityAuthen;
    private Call<ResponseBody> getIdentityAuthenCall;
    private void startGetIdentityAuthen() {
        getIdentityAuthenCall = VHttpServiceManager.getInstance().getVService().getIdentityAuthen();
        getIdentityAuthenCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
//                    String frontImgDemoUrl = resultData.getItem("frontImgDemoUrl", String.class);
//                    String backImgDemoUrl = resultData.getItem("backImgDemoUrl", String.class);
//                    String holdImgDemoUrl = resultData.getItem("holdImgDemoUrl", String.class);
//                    if (!TextUtils.isEmpty(frontImgDemoUrl) && !TextUtils.isEmpty(backImgDemoUrl) && !TextUtils.isEmpty(holdImgDemoUrl)) {
//                        viewDemoUrls = new ArrayList<>();
//                        viewDemoUrls.add(frontImgDemoUrl);
//                        viewDemoUrls.add(backImgDemoUrl);
//                        viewDemoUrls.add(holdImgDemoUrl);
//                    }

                    identityAuthen = resultData.getObject("identityAuth", IdentityAuthen.class);//0：审核中，1：已通过，2：未通过
                }

            }
        });
    }

    private String email,phone;
    private Call<ResponseBody> getUserInfoCall;
    private void getMyUserInfo() {
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

    String btnOktext = "";
    private void showAbilityDialog(String abilityValue) {
        abilityDialog = new TjrBaseDialog(getActivity()) {
            @Override
            public void onclickOk() {
                if (btnOktext.equals(getResources().getString(R.string.duihuan))) {
                    startAbilityValueToAward();
                } else {
                    dismiss();
//                    PageJumpUtil.pageJump(getActivity(), LPHomeActivity.class);
                }

            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        String msg = Double.parseDouble(abilityValue) > 0 ? getResources().getString(R.string.shifoubaquanbukedouduihuanchengkbt) : getResources().getString(R.string.muqianzanwukedou);
        btnOktext = Double.parseDouble(abilityValue) > 0 ? getResources().getString(R.string.duihuan) : getResources().getString(R.string.queding);
        abilityDialog.setTvTitle(getResources().getString(R.string.duihuan));
        abilityDialog.setMessage(msg);
        abilityDialog.setBtnOkText(btnOktext);
        abilityDialog.show();
    }


    private void startAbilityValueToAward() {
        CommonUtil.cancelCall(createChatTopicCall);
        ((HomeActivity) getActivity()).showProgressDialog();
        createChatTopicCall = VHttpServiceManager.getInstance().getVService().abilityValueToAward();
        createChatTopicCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                ((HomeActivity) getActivity()).dismissProgressDialog();
                if (resultData.isSuccess()) {
                    startGetMyHomeCallCall();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                ((HomeActivity) getActivity()).dismissProgressDialog();
            }
        });
    }

    private void goChatOrWechatQr() {
        if (type == 0) {
            if (!TextUtils.isEmpty(chatTopic) && !TextUtils.isEmpty(userName)) {
                ChatRoomActivity.pageJump(((HomeActivity) getActivity()), chatTopic, userName, headUrl);
            } else {
                CommonUtil.showmessage(getResources().getString(R.string.weihuoqudaokefuxinxi), ((HomeActivity) getActivity()));
            }
        } else if (type == 1) {
            if (!TextUtils.isEmpty(headUrl)) {
                showWechatQRCodeFragment(chatTopic, userName, headUrl);
            } else {
                CommonUtil.showmessage(getResources().getString(R.string.weihuoqudaokefuxinxi), ((HomeActivity) getActivity()));
            }

        }
    }

    private void startCreateChat() {
        CommonUtil.cancelCall(createChatTopicCall);
        ((HomeActivity) getActivity()).showProgressDialog();
        createChatTopicCall = VHttpServiceManager.getInstance().getVService().createChatTopic();
        createChatTopicCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                ((HomeActivity) getActivity()).dismissProgressDialog();
                if (resultData.isSuccess()) {
                    chatTopic = resultData.getItem("chatTopic", String.class);
                    headUrl = resultData.getItem("headUrl", String.class);
                    userName = resultData.getItem("userName", String.class);
                    type = resultData.getItem("type", Integer.class);//0跳转到聊天页面  1弹出微信二维码

                    goChatOrWechatQr();

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                ((HomeActivity) getActivity()).dismissProgressDialog();
            }
        });
    }


    WechatQRCodeFragment wechatQRCodeFragment;

    private void showWechatQRCodeFragment(String title, String subTitle, String qrUrl) {
        wechatQRCodeFragment = WechatQRCodeFragment.newInstance(title, subTitle, qrUrl);
        wechatQRCodeFragment.showDialog(getActivity().getSupportFragmentManager(), "");
    }

    ShareDialogFragment shareDialogFragment;

    private void showShareDialogFragment() {
        shareDialogFragment = ShareDialogFragment.newInstance(getUser(), shareUrl);
        shareDialogFragment.setOnShareDialogCallBack(new ShareDialogFragment.OnShareDialogCallBack() {
            @Override
            public void onDialogDismiss() {
                shareDialogFragment.dismiss();
            }
        });
        shareDialogFragment.showDialog(getActivity().getSupportFragmentManager(), "");
    }

    boolean refresh = false;


    @Override
    public void onResume() {
        super.onResume();
        Log.d("V_AccountFragment", "onResume   getUserVisibleHint()==" + getUserVisibleHint());
        if (getUserVisibleHint()) {
            getMyUserInfo();
            startGetIdentityAuthen();
            startGetMyHomeCallCall();
//            startGetHomeAccount();
            setUserInfo(getUser());
            showLegalPrivateChatNewsCount();
//            setEditPointFlag();
        } else {
            refresh = true;
        }
    }

//    public void setEditPointFlag() {
//        if (getUser() != null) {
//            int editFlag = NormalShareData.getUserEditFlag(getActivity(), getUser().userId);
//            if (editFlag == 0) {
//                vPointOfEdit.setVisibility(View.VISIBLE);
//            } else {
//                vPointOfEdit.setVisibility(View.GONE);
//            }
//        }
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() == null) return;
        if (isVisibleToUser) {
            if (tvName != null && TextUtils.isEmpty(tvName.getText().toString())) {
                setUserInfo(getUser());
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startGetMyHomeCallCall();
                }
            }, 500);

            showLegalPrivateChatNewsCount();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).navigationBarColor(R.color.white).init();

    }

    public void immersionbar() {
        if (mImmersionBar != null && ll_bar != null) {
            mImmersionBar
                    .titleBar(ll_bar)
                    .statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA)
                    .navigationBarColor(R.color.white)
                    .init();
        }
    }


    Call<ResponseBody> getHomeAccountCall;

//    private void startGetHomeAccount() {
//        refresh = false;
//        CommonUtil.cancelCall(getHomeAccountCall);
//        getHomeAccountCall = VHttpServiceManager.getInstance().getVService().homeAccount();
//        getHomeAccountCall.enqueue(new MyCallBack(getActivity()) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    String balance = resultData.getItem("balance", String.class);
//                    User u = resultData.getObject("user", User.class);
//                    tvTaojinzhiValue.setText(balance);
//                    if (u != null) {
//                        if (u.roomId == 0) {
//                            llMyLive.setVisibility(View.GONE);
//                        } else {
//                            llMyLive.setVisibility(View.VISIBLE);
//                        }
//                        roomId = u.roomId;
//                        tvName.setText(u.getUserName());
//                        if (!TextUtils.isEmpty(u.headUrl) && !u.headUrl.equals(ivHead.getTag())) {//防止闪烁
//                            tjrImageLoaderUtil.displayImage(u.headUrl, ivHead);
//                            ivHead.setTag(u.headUrl);
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//            }
//        });
//    }

    private void setUserInfo(User u) {
        if (u == null) return;
        tvName.setText(u.getUserName());
        tvId.setText("ID：" + String.valueOf(u.getUserId()));
        tvDesc.setText(u.getDescribes());
        tjrImageLoaderUtil.displayImageForHead(u.headUrl, ivHead);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

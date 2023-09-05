package com.bitcnew.module.home;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.data.sharedpreferences.CircleSharedPreferences;
import com.bitcnew.data.sharedpreferences.NormalShareData;
import com.bitcnew.data.sharedpreferences.SysShareData;
import com.bitcnew.module.home.fragment.HomeAccountFragment;
import com.bitcnew.module.home.fragment.HomeCropyNewFragment;
import com.bitcnew.module.home.fragment.HomeMineFragment;
import com.bitcnew.module.home.fragment.HomeMarkAndKbtFragment;
import com.bitcnew.module.home.fragment.HomeShequFragment;
import com.bitcnew.subpush.ReceiveModel;
import com.bitcnew.subpush.ReceiveModelTypeEnum;
import com.bitcnew.subpush.ReceivedManager;
import com.bitcnew.subpush.notify.NotifyModel;
import com.bitcnew.updatedialog.DownAndNoticeDialogManager;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.DynamicPermission;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.NotificationsUtils;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.PermissionUtils;
import com.bitcnew.widgets.BadgeView;
import com.bitcnew.module.login.LoginActivity;
import com.bitcnew.util.VeDate;
import com.bitcnew.widgets.NoScrollViewPager;
import com.bitcnew.http.model.Components;
import com.bitcnew.http.model.Placard;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.bitcnew.http.tjrcpt.SubPushHttp;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.R;
import com.bitcnew.subpush.IRemoteService;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JzvdStd;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * home 页
 */

public class HomeActivity extends TJRBaseToolBarActivity implements View.OnClickListener, Observer {


    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    @BindView(R.id.llHome)
    LinearLayout llHome;
    @BindView(R.id.llAttention)
    LinearLayout llAttention;
    @BindView(R.id.llCropyme)
    LinearLayout llCropyme;
    @BindView(R.id.llMarket)
    LinearLayout llMarket;

    @BindView(R.id.tvMine)
    TextView tvMine;
    @BindView(R.id.llMine)
    LinearLayout llMine;

    @BindView(R.id.flHome)
    FrameLayout flHome;
    @BindView(R.id.flAttention)
    FrameLayout flAttention;
    @BindView(R.id.flCropyme)
    FrameLayout flCropyme;
    @BindView(R.id.flMarket)
    FrameLayout flMarket;
    @BindView(R.id.flMine)
    FrameLayout flMine;


    @BindView(R.id.llBottom)
    LinearLayout llBottom;


    @BindView(R.id.tvShowCoin)
    TextView tvShowCoin;


    private HomePageAdapter homePageAdapter;

    private int currentTab = 0;
    private int eventCache;
    private boolean tapDragFinish = true;
    private float preX;
    private Handler mHandler = new Handler();
    private BadgeView badgeAccount;
    private BadgeView badgeCommunity;

    private IRemoteService mIRemoteService;
    private ArrayList<Integer> argList;


    private TjrBaseDialog tjrNotifactionSettingDialog;

    private DownAndNoticeDialogManager downAndNoticeDiglogManage;
    private DynamicPermission dynamicPermission;
    private Call<ResponseBody> homeGet;

    private boolean first = true;

    public String riskRateDesc = "",miningRateDesc="";


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                com.bitcnew.http.util.CommonUtil.showmessage(getResources().getString(R.string.lianjieshibai), HomeActivity.this);
            }
            Object obj = msg.obj;
            if (obj instanceof ReceiveModel) {
                ReceiveModel d = (ReceiveModel) obj;
                Log.d("HomeActivity", "ReceiveModel.type=" + d.type);
                switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(d.type)) {
                    case private_chat_record://这个是私聊
                        showPrivateChatNewsCount();
                        break;
                    case sys_auto_push:
                        if (d.obj != null && d.obj instanceof NotifyModel) {
                            NotifyModel notifyModel = (NotifyModel) d.obj;
                            if ("re".equals(notifyModel.getT())) {//只有回复评论需要消息提示
                                getApplicationContext().msgCount = getApplicationContext().msgCount + 1;
//                                setAllNewsCount();
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }

    };

//    private void checkPermission() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.System.canWrite(this)) {
//                Uri selfPackageUri = Uri.parse("package:"
//                        + getPackageName());
//                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
//                        selfPackageUri);
//                startActivity(intent);
//            } else {
//                //有了权限，你要做什么呢？具体的动作
//            }
//        }
//    }

    @Override
    protected int setLayoutId() {
        return R.layout.home;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        SysShareData.saveVersion(HomeActivity.this, getApplicationContext().getmVersion());// 保存最新
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
//            for (String key : bundle.keySet()) {
//                Log.d("154", key + "==" + bundle.getString(key));
//            }
            String pkg = bundle.getString(CommonConst.KEY_EXTRAS_PKG);
            String cls = bundle.getString(CommonConst.KEY_EXTRAS_CLS);
            if (!TextUtils.isEmpty(pkg) && !TextUtils.isEmpty(cls) && !cls.equals("HomeActivity")) {
                String params = bundle.getString(CommonConst.MSG_PARAMS);
                Intent intent = new Intent();
                Bundle articleBundle = new Bundle();
                articleBundle.putString(CommonConst.MSG_PARAMS, params);
                intent.putExtras(articleBundle);
                intent.setClassName(pkg, cls);
                PageJumpUtil.pageJump(this, intent);
            }

        }

        viewPager = (NoScrollViewPager) findViewById(R.id.viewPager);
        homePageAdapter = new HomePageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(homePageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d("slide", "onPageSelected  i==" + i);
                slideTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
//

        viewPager.setOffscreenPageLimit(homePageAdapter.getCount() - 1);
        immersionBar.keyboardEnable(false).init();

//        llDynmic.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                DoDragAnima(motionEvent.getX(),motionEvent.getY(),motionEvent);
//                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
//                    llDynmic.setSelected(true);
//                    llFollow.setSelected(false);
//                    llAssets.setSelected(false);
//                    llMine.setSelected(false);
//                    if (viewPager.getCurrentItem() != 1) {
//                        viewPager.setCurrentItem(1, false);
//                        startScaleAnimation(llDynmic);
//                    } else {
//
//                    }
//                }
//                return true;
//            }
//        });
        flCropyme.setOnClickListener(this);
        flHome.setOnClickListener(this);
        flAttention.setOnClickListener(this);
        flMine.setOnClickListener(this);
        flMarket.setOnClickListener(this);

        if (savedInstanceState != null) {
            currentTab = savedInstanceState.getInt(CommonConst.POS);
            viewPager.setCurrentItem(currentTab, false);
            slideTab(currentTab);
        } else {
            if (isLogin()) {
                currentTab = 0;
                llCropyme.setSelected(true);
            } else {
                currentTab = 0;
                viewPager.setCurrentItem(currentTab, false);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slideTab(currentTab);
                    }
                }, 300);
//                llMarket.setSelected(true);
            }
        }


        badgeAccount = new BadgeView(HomeActivity.this, llMine);
        badgeAccount.setBadgeMargin(0, 10);
        badgeAccount.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeAccount.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        badgeCommunity = new BadgeView(HomeActivity.this, llHome);
        badgeCommunity.setBadgeMargin(0, 10);
        badgeCommunity.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeCommunity.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 11);
        mHandler.postDelayed(new Runnable() {//检查是否开启通知权限
            @Override
            public void run() {
//
                if (!NotificationsUtils.isNotificationEnabled(HomeActivity.this)) {
                    showNotifactionDialog();
                }
            }
        }, 1000);
        mHandler.postDelayed(new Runnable() {//
            @Override
            public void run() {
                initDynamicPermission();
//                getApplicationContext().startGetClientInfo(HomeActivity.this, ShareData.getPlacardTime(getApplicationContext()));

            }
        }, 2000);
        ReceivedManager.getInstance(this).addObserver(this);
        getApplicationContext().startSubPushService();

        mHandler.postDelayed(new Runnable() {//防止有时候退出的时候没有收到后台的推送，这里是像后台拉取未收到的私聊数据
            @Override
            public void run() {
                try {
                    if (mIRemoteService == null)
                        mIRemoteService = ReceivedManager.getInstance(HomeActivity.this).getiRemoteService();
                    if (mIRemoteService != null && !TextUtils.isEmpty(getUserId()))
                        mIRemoteService.send(SubPushHttp.getInstance().connectGetDataUrl(Long.parseLong(getUserId())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 2000);

//        PageJumpUtil.pageJump(this, LanguageTestActivity.class);
    }

    private void initDynamicPermission() {
        if (dynamicPermission == null) {
            dynamicPermission = new DynamicPermission(this, new DynamicPermission.RequestPermissionsCallBack() {
                @Override
                public void onRequestSuccess(String[] permissions, int requestCode) {
                    getApplicationContext().loadResourceManagers();
                }

                @Override
                public void onRequestFail(String[] permissions, int requestCode) {
                }
            });
        }
        dynamicPermission.checkSelfPermission(PermissionUtils.READ_PHONE_STATE_EXTERNAL_STORAGE, 200);
    }

    //首页HomeActivity必须实现这个方法，用于更新app时候下载时候
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        getApplicationContext().onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (downAndNoticeDiglogManage != null)
            downAndNoticeDiglogManage.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (dynamicPermission != null)
            dynamicPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x123) {//开启安装权限
            Log.d("HomeActivity", "resultCode==" + resultCode);
            if (downAndNoticeDiglogManage != null)
                downAndNoticeDiglogManage.onActivityResult(resultCode);
        } else if (requestCode == 0x234) {
//            if (resultCode == 0x234) {
//                viewPager.setCurrentItem(homePageAdapter.getMarketPos());
//                HomeMarkAndKbtFragment homeMarkAndKbtFragment = (HomeMarkAndKbtFragment) homePageAdapter.instantiateItem(viewPager, homePageAdapter.getMarketPos());
//                homeMarkAndKbtFragment.setKbtSelected();
//            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void showNotifactionDialog() {

        String countStr = SysShareData.getSharedDate(HomeActivity.this, CommonConst.NOTIFICATION_SETTING + getApplicationContext().getmVersion());
        int count = TextUtils.isEmpty(countStr) ? 0 : Integer.parseInt(countStr);
        if (count % 10 == 0) {//不用每次都弹出来,这里先设置10次弹出来一次
            if (tjrNotifactionSettingDialog == null) {
                tjrNotifactionSettingDialog = new TjrBaseDialog(this) {
                    @Override
                    public void setDownProgress(int progress) {
                    }

                    @Override
                    public void onclickOk() {
                        tjrNotifactionSettingDialog.dismiss();
                        NotificationsUtils.notificationSetting(HomeActivity.this);
                    }

                    @Override
                    public void onclickClose() {
                        tjrNotifactionSettingDialog.dismiss();
                    }
                };
                tjrNotifactionSettingDialog.setTvTitle(getResources().getString(R.string.kaiqitongzhituisong));
                tjrNotifactionSettingDialog.setMessage(getResources().getString(R.string.kaiqitongzhituisongjiangyouzhuyu));//""
                tjrNotifactionSettingDialog.setBtnColseText(getResources().getString(R.string.xiacizaishuo));
                tjrNotifactionSettingDialog.setBtnOkText(getResources().getString(R.string.qukaiqi));
            }
            if (!tjrNotifactionSettingDialog.isShowing()) tjrNotifactionSettingDialog.show();
        }
        SysShareData.setSharedDate(HomeActivity.this, CommonConst.NOTIFICATION_SETTING + getApplicationContext().getmVersion(), String.valueOf(count + 1));


    }

    android.animation.AnimatorSet animatorSet;

    private void startScaleAnimation(View view) {
        if (animatorSet == null) {
            animatorSet = new android.animation.AnimatorSet();
            ObjectAnimator xAnime = ObjectAnimator.ofFloat(view, "scaleX", 1.1f, 0.9f, 1);
            ObjectAnimator yAnime = ObjectAnimator.ofFloat(view, "scaleY", 1.1f, 0.9f, 1);
            animatorSet.playTogether(xAnime, yAnime);
            animatorSet.setInterpolator(new FastOutSlowInInterpolator());
            animatorSet.setDuration(500);
        }
        animatorSet.end();
        animatorSet.setTarget(view);
        animatorSet.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CircleSharedPreferences.saveCircleSpChatRoomid(this, "0");
        startHomeGet();
        if (TextUtils.isEmpty(getUserId())) {
//            finish();
            return;
        }
        showPrivateChatNewsCount();

    }

    public void switchHomeCropyMeFragment() {
        if (viewPager != null) {
            viewPager.setCurrentItem(2, true);
        }
    }


    public void slideTab(int arg0) {
        currentTab = arg0;
        switch (arg0) {
            case 0:
                llMarket.setSelected(false);
                llHome.setSelected(false);
                llCropyme.setSelected(true);
                llAttention.setSelected(false);
                llMine.setSelected(false);
//                HomeCropyNewFragment homeCropyNewFragment = (HomeCropyNewFragment) homePageAdapter.instantiateItem(viewPager, 1);
//                homeCropyNewFragment.immersionbar();
                break;
            case 1:
                llMarket.setSelected(true);
                llHome.setSelected(false);
                llCropyme.setSelected(false);
                llAttention.setSelected(false);
                llMine.setSelected(false);
                HomeMarkAndKbtFragment homeMarkAndKbtFragment = (HomeMarkAndKbtFragment) homePageAdapter.instantiateItem(viewPager, 1);
                homeMarkAndKbtFragment.immersionbar();
                break;
            case 2:
                llMarket.setSelected(false);
                llHome.setSelected(true);
                llCropyme.setSelected(false);
                llAttention.setSelected(false);
                llMine.setSelected(false);

                HomeAccountFragment homeFragment = (HomeAccountFragment) homePageAdapter.instantiateItem(viewPager, 2);
                homeFragment.immersionbar();
                break;

            case 3:
                llMarket.setSelected(false);
                llHome.setSelected(false);
                llCropyme.setSelected(false);
                llAttention.setSelected(true);
                llMine.setSelected(false);
                HomeShequFragment homeAttentionFragment = (HomeShequFragment) homePageAdapter.instantiateItem(viewPager, 3);
//                homeAttentionFragment.immersionbar();
                break;
            case 4:
                llMarket.setSelected(false);
                llHome.setSelected(false);
                llCropyme.setSelected(false);
                llAttention.setSelected(false);
                llMine.setSelected(true);
                HomeMineFragment homeAccountFragment = (HomeMineFragment) homePageAdapter.instantiateItem(viewPager, 4);
                homeAccountFragment.immersionbar();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flAttention:
                if (isLogin()) {
                    if (viewPager.getCurrentItem() != homePageAdapter.getAttentionPos()) {
                        viewPager.setCurrentItem(homePageAdapter.getAttentionPos(), false);
                        startScaleAnimation(v);
                    }
                } else {
                    LoginActivity.login(HomeActivity.this);
                }
                break;
            case R.id.flMarket:
                if (viewPager.getCurrentItem() != homePageAdapter.getMarketPos()) {
                    viewPager.setCurrentItem(homePageAdapter.getMarketPos(), false);
                    startScaleAnimation(v);
                }
                break;

            case R.id.flHome:
                if (isLogin()) {
                    if (viewPager.getCurrentItem() != homePageAdapter.getHomePos()) {
                        viewPager.setCurrentItem(homePageAdapter.getHomePos(), false);
                        startScaleAnimation(v);
                    }
                } else {
                    LoginActivity.login(HomeActivity.this);
                }

                break;


            case R.id.flCropyme:
                if (viewPager.getCurrentItem() != homePageAdapter.getCropymePos()) {
                    viewPager.setCurrentItem(homePageAdapter.getCropymePos(), false);
                    startScaleAnimation(v);
                }
                break;

            case R.id.flMine:
                if (isLogin()) {
                    if (viewPager.getCurrentItem() != homePageAdapter.getMinePos()) {
                        viewPager.setCurrentItem(homePageAdapter.getMinePos(), false);
                        startScaleAnimation(v);
                    }
                } else {
                    LoginActivity.login(HomeActivity.this);
                }

                break;
        }
    }
    public void swicht4(){
        if (isLogin()) {
            if (viewPager.getCurrentItem() != homePageAdapter.getMinePos()) {
                viewPager.setCurrentItem(homePageAdapter.getMinePos(), false);
                startScaleAnimation(flMine);
            }
        } else {
            LoginActivity.login(HomeActivity.this);
        }
    }
    private class HomePageAdapter extends FragmentPagerAdapter {
        private int cropymePos = 0;
        private int marketPos = 1;
        private int homePos = 2;
        private int attentionPos = 3;
        private int minePos = 4;

        public int getHomePos() {
            return homePos;
        }

        public int getAttentionPos() {
            return attentionPos;
        }

        public int getCropymePos() {
            return cropymePos;
        }

        public int getMarketPos() {
            return marketPos;
        }

        public int getMinePos() {
            return minePos;
        }

        public HomePageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public void notifyDataSetChanged() {
//            slideTab(0);
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeCropyNewFragment.newInstance();
                case 1:
                    return HomeMarkAndKbtFragment.newInstance();
                case 2:
                    return HomeAccountFragment.newInstance();//HomeAttentionFragment.newInstance();
                case 3:
                    return HomeShequFragment.newInstance();
                default:
                    return HomeMineFragment.newInstance();
            }
        }
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CommonConst.POS, viewPager == null ? 0 : viewPager.getCurrentItem());
        super.onSaveInstanceState(outState);
    }


    public void setShowCoin(String showCoin) {
        if (tvShowCoin == null) return;
        if (!TextUtils.isEmpty(showCoin)) {
            tvShowCoin.setVisibility(View.VISIBLE);
            tvShowCoin.setText(showCoin);
        } else {
            tvShowCoin.setVisibility(View.GONE);
        }
        HomeMineFragment homeAccountFragment = (HomeMineFragment) homePageAdapter.instantiateItem(viewPager, homePageAdapter.getMinePos());
        homeAccountFragment.setShowCoin(showCoin);

    }

    private void showRepo(String coinSubRound) {
        String oldCoinSubRound = NormalShareData.getCoinSubRound(this);
        if (!oldCoinSubRound.equals(coinSubRound)) {
            NormalShareData.saveCoinSubRound(this, coinSubRound);
            HomeMarkAndKbtFragment homeMarkAndKbtFragment = (HomeMarkAndKbtFragment) homePageAdapter.instantiateItem(viewPager, 0);
            homeMarkAndKbtFragment.setKbtSelected();
        }
    }

    private void startHomeGet() {
        CommonUtil.cancelCall(homeGet);
//        showProgressDialog();
        homeGet = VHttpServiceManager.getInstance().getVService().homeGet(VeDate.getyyyyMMddHHmmss(VeDate.getNow()));
        homeGet.enqueue(new MyCallBack(HomeActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
//                    setAllNewsCount();
                    Components componentsinfo = resultData.getObject("version", Components.class);
                    Placard placard = resultData.getObject("notice", Placard.class);
                    getApplicationContext().components = componentsinfo;
                    getApplicationContext().placard = placard;

                    getApplicationContext().coinSubState = resultData.getItem("coinSubState", String.class);
                    setShowCoin(getApplicationContext().coinSubState);

                    riskRateDesc = resultData.getItem("riskRateDesc", String.class);
                    miningRateDesc = resultData.getItem("miningRateDesc",String.class);

//                    String coinSubRound = resultData.getItem("coinSubRound", String.class);
//                    showRepo(coinSubRound);

                    if (first) {//更新框和公告只弹一次就好
                        first = false;
                        if (componentsinfo != null) {
                            if (getApplicationContext().mVersion != null && getApplicationContext().mVersion.matches("[0-9]+$")) {
                                if ((componentsinfo.version > Integer.parseInt(getApplicationContext().mVersion))) {
                                    if (downAndNoticeDiglogManage == null) {
                                        downAndNoticeDiglogManage = new DownAndNoticeDialogManager(HomeActivity.this, componentsinfo, placard);
                                    } else {
                                        downAndNoticeDiglogManage.setUpdateManager(HomeActivity.this, componentsinfo, placard);
                                    }
                                    downAndNoticeDiglogManage.checkUpdateDialog();
                                    getApplicationContext().isNewVersion = true;
                                } else {
                                    if (downAndNoticeDiglogManage == null) {
                                        downAndNoticeDiglogManage = new DownAndNoticeDialogManager(HomeActivity.this, componentsinfo, placard);
                                    } else {
                                        downAndNoticeDiglogManage.setUpdateManager(HomeActivity.this, componentsinfo, placard);
                                    }
                                    downAndNoticeDiglogManage.checkNoticeDialog();// 检查
                                    getApplicationContext().isNewVersion = false;
                                }
                            }
                        } else if (placard != null) {// 单独只有公告
                            if (downAndNoticeDiglogManage == null) {
                                downAndNoticeDiglogManage = new DownAndNoticeDialogManager(HomeActivity.this, componentsinfo, placard);
                            } else {
                                downAndNoticeDiglogManage.setUpdateManager(HomeActivity.this, componentsinfo, placard);
                            }
                            downAndNoticeDiglogManage.checkNoticeDialog();
                            getApplicationContext().isNewVersion = false;
                        }
                    }


                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                if (first) {
                }

            }
        });
    }


    /**
     * 圈子消息数量
     * 因为circlefragment里面有接收消息，所以是从fragment调用这个方法
     *
     * @param count
     */
    public void showCircleChatNewsCount(int count) {
        if (count > 0) {
            badgeCommunity.show();
            badgeCommunity.setBadgeText(String.valueOf(count));
        } else {
            badgeCommunity.hide();
        }
    }


    /**
     * 私聊消息数量
     * 因为accountfragment里面没有接收消息，所以是fragment的消息数量是从activity里面传过去
     */
    public void showPrivateChatNewsCount() {
        if (TextUtils.isEmpty(getUserId())) {
            badgeAccount.hide();
            Log.d("154", "success type -> return");
            return;
        }
        int serviceChatCount = 0;
        if (getApplicationContext().getUser() != null) {//现在客服是弹出二维码，青爷说先不加消息数
//            serviceChatCount = PrivateChatSharedPreferences.getAllPriChatRecordNum(getApplicationContext(), getApplicationContext().getUser().getUserId());
        }
        if (serviceChatCount > 0) {//显示
            badgeAccount.show();
            badgeAccount.setBadgeText(CommonUtil.setNewsCount(serviceChatCount));
        } else {//不显示
            badgeAccount.hide();
        }
        if (homePageAdapter != null && viewPager != null) {
            Object obj = homePageAdapter.instantiateItem(viewPager, homePageAdapter.getMinePos());
            if (obj != null && obj instanceof HomeMineFragment) {
                HomeMineFragment accountFragment = (HomeMineFragment) obj;
                accountFragment.setPrivateChatNewsCount();
            }
        }


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private boolean exitHint = false;
    private static final long INTERVALTIME = 1500;

    @Override
    public void onBackPressed() {
        if (JzvdStd.backPress()) {
            return;
        }
        if (!exitHint) {
            CommonUtil.showmessage(getResources().getString(R.string.zaianyicituichu), this);
            exitHint = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exitHint = false;
                }
            }, INTERVALTIME);
        } else {
            super.onBackPressed();
        }


    }

    @Override
    public void update(Observable observable, Object data) {
        Log.d("HomeActivity", "update==");

        Message message = new Message();
        message.obj = data;
        handler.sendMessage(message);
    }


    @Override
    protected void onDestroy() {
        ReceivedManager.getInstance(this).deleteObserver(this);
        SharedPreferences sharedPreferences = getSharedPreferences("com.bitcnew.util.InputActivity", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.releaseAllVideos();
    }
}

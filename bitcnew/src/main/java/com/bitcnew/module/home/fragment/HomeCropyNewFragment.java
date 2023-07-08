package com.bitcnew.module.home.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bitcnew.BuildConfig;
import com.bitcnew.MainApplication;
import com.bitcnew.bean.TransformersBean;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.http.common.ConfigTjrInfo;
import com.bitcnew.http.model.Components;
import com.bitcnew.http.model.Placard;
import com.bitcnew.module.chat.ChatRoomActivity;
import com.bitcnew.module.home.MarketActivity2;
import com.bitcnew.module.home.NoticeListActivity;
import com.bitcnew.module.home.SwichColorActivity;
import com.bitcnew.module.home.SwitchLanguageActivity;
import com.bitcnew.module.home.XinbishengouActivity;
import com.bitcnew.module.home.adapter.HomeFirstButtonAdapter;
import com.bitcnew.module.login.LoginActivity;
import com.bitcnew.module.myhome.AboutActivity;
import com.bitcnew.module.myhome.SettingActivity;
import com.bitcnew.module.pledge.PledgeMainActivity;
import com.bitcnew.updatedialog.DownAndNoticeDialogManager;
import com.bitcnew.util.VeDate;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bitcnew.R;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.common.web.CommonWebViewActivity;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.model.User;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.module.home.HomeActivity;
import com.bitcnew.module.home.HomeCropyMeActivity;
import com.bitcnew.module.home.MarketActivity;
import com.bitcnew.module.home.adapter.MyListData;
import com.bitcnew.module.home.adapter.NewsAdapter;
import com.bitcnew.module.home.adapter.SlidingImage_Adapter;
import com.bitcnew.module.home.bean.HomeBannerBean;
import com.bitcnew.module.home.entity.Market;
import com.bitcnew.module.home.entity.Notice;
import com.bitcnew.module.legal.LegalMoneyActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.InflaterUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.util.SPUtils;
import com.bitcnew.util.StockChartUtil;
import com.bitcnew.util.TjrMinuteTaskPool;
import com.bitcnew.widgets.CircleImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeCropyNewFragment extends UserBaseImmersionBarFragment implements View.OnClickListener {

    @BindView(R.id.langIv)
    ImageView langIv;

    @OnClick(R.id.switchFl)
    public void onSwitchLangClick() {
        PageJumpUtil.pageJump(getContext(), SwitchLanguageActivity.class);
    }

    @BindView(R.id.ivHead)
    CircleImageView ivHead;

    @BindView(R.id.ll_dav)
    LinearLayout ll_dav;
    @BindView(R.id.ll_xinbishengou)
    LinearLayout ll_xinbishengou;
    @BindView(R.id.ll_otc)
    LinearLayout ll_otc;
    @BindView(R.id.ll_zaixiankefu)
    LinearLayout ll_zaixiankefu;
    @BindView(R.id.ll_zhiyashengyi)
    LinearLayout ll_zhiyashengyi;
    @BindView(R.id.re_otc)
    RelativeLayout re_otc;
    @BindView(R.id.symbol_txt)
    TextView symbol_txt;
    @BindView(R.id.symbol_txt0)
    TextView symbol_txt0;
    @BindView(R.id.currency_txt)
    TextView currency_txt;
    @BindView(R.id.percent_txt)
    TextView percent_txt;

    @BindView(R.id.symbol_txt1)
    TextView symbol_txt1;
    @BindView(R.id.symbol_txt10)
    TextView symbol_txt10;
    @BindView(R.id.currency_txt1)
    TextView currency_txt1;
    @BindView(R.id.percent_txt1)
    TextView percent_txt1;


    @BindView(R.id.symbol_txt2)
    TextView symbol_txt2;
    @BindView(R.id.symbol_txt20)
    TextView symbol_txt20;
    @BindView(R.id.currency_txt2)
    TextView currency_txt2;
    @BindView(R.id.percent_txt2)
    TextView percent_txt2;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.flipper)
    ViewFlipper flipper;

    @BindView(R.id.tvZhangfubang)
    TextView tvZhangfubang;
    @BindView(R.id.tvDiefubang)
    TextView tvDiefubang;
    @BindView(R.id.viewZhangfubang)
    View viewZhangfubang;
    @BindView(R.id.viewDiefubang)
    View viewDiefubang;
    @BindView(R.id.recyclerViewBang)
    RecyclerView recyclerViewBang;

    // buttons click are at the bottom with butterknife

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    View view;
    private Call<ResponseBody> getHomeMarketCall;
    private Call<ResponseBody> getNewsCall;
    private static final Integer[] IMAGES = {R.drawable.banner, R.drawable.banner, R.drawable.banner, R.drawable.banner};
    private ArrayList<HomeBannerBean.BannerBean> ImagesArray = new ArrayList<>();
    HomeActivity activity;

    private boolean isRun = false;//定时器是否在跑
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getCryptoPrice();
            handler.postDelayed(this, 1000);
        }
    };

    public static HomeCropyNewFragment newInstance() {
        return new HomeCropyNewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home_cropy_new_fragment, container, false);
        ButterKnife.bind(this, view);
        activity = (HomeActivity) getActivity();
        ll_dav.setOnClickListener(this);
        re_otc.setOnClickListener(this);
        ll_xinbishengou.setOnClickListener(this);
        ll_otc.setOnClickListener(this);
        ll_zaixiankefu.setOnClickListener(this);
        if (BuildConfig.FLAVOR.startsWith("fwdetsc")) {
            ll_zaixiankefu.setVisibility(View.GONE);
        }
        ll_zhiyashengyi.setOnClickListener(this);
        initBang();
        init();
        tjrImageLoaderUtil = new TjrImageLoaderUtil(R.drawable.ic_common_mic2);
        setUserInfo(getUser());//先初始化
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String lang = (String) SPUtils.get(getActivity(),"myLanguage1","");
        setLangUISelect(lang);
    }

    private void setLangUISelect(String lang) {
        langIv.setVisibility(View.VISIBLE);
        if ("zh-cn".equals(lang)) {
            langIv.setImageResource(R.drawable.icon_lang_cn_zh);
        } else if ("zh-tw".equals(lang)) {
            langIv.setImageResource(R.drawable.icon_lang_cn_hk);
        } else if ("ko".equals(lang)) {
            langIv.setImageResource(R.drawable.icon_lang_kor);
        } else if ("jp".equals(lang)) {
            langIv.setImageResource(R.drawable.icon_lang_jp);
        } else if ("ru".equals(lang)) {
            langIv.setImageResource(R.drawable.icon_lang_rs);
        } else if ("fr".equals(lang)) {
            langIv.setImageResource(R.drawable.icon_lang_fra);
        } else if ("es".equals(lang)) {
            langIv.setImageResource(R.drawable.icon_lang_sp);
        } else if ("pt".equals(lang)) {
            langIv.setImageResource(R.drawable.icon_lang_pt);
        } else {
            if ("ts".equalsIgnoreCase(com.bitcnew.http.BuildConfig.DEFAULT_LNG)) {
                langIv.setImageResource(R.drawable.icon_lang_cn_hk);
            } else if ("cn".equalsIgnoreCase(com.bitcnew.http.BuildConfig.DEFAULT_LNG)) {
                langIv.setImageResource(R.drawable.icon_lang_cn_zh);
            } else {
                langIv.setImageResource(R.drawable.icon_lang_uk);
            }
        }
    }


    private HomeMarketAdapter0 homeMarketAdapter;

    private void initBang() {
        tvZhangfubang.setOnClickListener(this);
        tvDiefubang.setOnClickListener(this);
        tvZhangfubang.setSelected(true);
        tvDiefubang.setSelected(false);
        homeMarketAdapter = new HomeMarketAdapter0(getActivity(), li, 1, "digital");
        recyclerView.setHasFixedSize(true);
        recyclerViewBang.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewBang.setAdapter(homeMarketAdapter);
        homeMarketAdapter.setOnPlayClickListener(new HomeMarketAdapter0.OnPlayClickListener() {
            @Override
            public void onSelClick(int pos, String symbol) {
                if ("encrypted".equalsIgnoreCase(BuildConfig.FLAVOR)) {
                    MarketActivity2.pageJump(getActivity(), symbol, 1,"spot");
                } else {
                    MarketActivity.pageJump(getActivity(), symbol, 1, "digital");
                }
            }
        });


        startGetHomeConfig();
        startGetHomeAccount();
        startGetMarket();
    }

    private Call<ResponseBody> getHomeConfigCall;
    private void startGetHomeConfig() {
        CommonUtil.cancelCall(getHomeConfigCall);
        getHomeConfigCall = VHttpServiceManager.getInstance().getVService().homeConfig();
        getHomeConfigCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                try {
                    if (resultData.isSuccess()) {
                        Gson gson = new Gson();
                        HomeBannerBean bean = gson.fromJson(resultData.data, HomeBannerBean.class);
                        if (null != bean) {
                            if (null != bean.getBanner() && bean.getBanner().size() > 0) {
                                ImagesArray.clear();
                                ImagesArray.addAll(bean.getBanner());
                                mPager.setAdapter(new SlidingImage_Adapter(getActivity(), ImagesArray));
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    private Call<ResponseBody> getHomeAccountCall;
    private void startGetHomeAccount() {
        CommonUtil.cancelCall(getHomeAccountCall);
        getHomeAccountCall = VHttpServiceManager.getInstance().getVService().homeAccount();
        getHomeAccountCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                try {
                    if (resultData.isSuccess()) {
                        Gson gson = new Gson();
                        HomeBannerBean bean = gson.fromJson(resultData.data, HomeBannerBean.class);
                        if (null != bean) {
                            if (null != bean.getBanner() && bean.getBanner().size() > 0) {
                                ImagesArray.clear();
                                ImagesArray.addAll(bean.getBanner());
                                mPager.setAdapter(new SlidingImage_Adapter(getActivity(), ImagesArray));
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    private String sortType = "1";//0默认 1 倒序 2正序
    private Call<ResponseBody> GetMarketCall;
    private List<Market> li = new ArrayList<>();
    private void startGetMarket() {
        CommonUtil.cancelCall(GetMarketCall);
        GetMarketCall = VHttpServiceManager.getInstance().getMarketService().marketData2("rate", sortType, "stock");
        GetMarketCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<Market> groupMarket = resultData.getGroup("quotes", new TypeToken<Group<Market>>() {
                    }.getType());
                    if (groupMarket != null && groupMarket.size() > 0) {
                        int a = groupMarket.size();
                        li.clear();
                        if (a >= 10) {
                            for (int i = 0; i < 10; i++) {
                                li.add(groupMarket.get(i));
                            }
                        } else {
                            li.addAll(groupMarket);
                        }
                        if (!isc) {
                            homeMarketAdapter.notifyDataSetChanged();
                        }

                    }
                    int size = (groupMarket == null ? 0 : groupMarket.size());
                    if (!isRun && size > 0) startTimer();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    private void getCryptoPrice() {
        getHomeMarketCall = VHttpServiceManager.getInstance().getMarketService().getQuoteHomePage();
        getHomeMarketCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                try {
                    JSONObject obj = resultData.returnJSONObject();
                    JSONArray quotes = obj.getJSONArray("quotes");
                    JSONObject curr = (JSONObject) quotes.get(0);
                    btcStr = curr.getString("symbol");

                    symbol_txt.setText(curr.getString("symbol") + "/" + curr.getString("currency"));
                    String rate = curr.get("rate").toString();
                    symbol_txt0.setText(rate + "%");
                    String col = (String) SPUtils.get(getActivity(), "swichColor", SwichColorActivity.COLOR_GREEN_UP_RED_DOWN);
                    if (!TextUtils.isEmpty(col) && SwichColorActivity.COLOR_GREEN_UP_RED_DOWN.equalsIgnoreCase(col)) {
                        if (rate.contains("-")) {
                            symbol_txt0.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                            currency_txt.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                        } else {
                            symbol_txt0.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                            currency_txt.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                        }
                    } else {
                        if (rate.contains("-")) {
                            symbol_txt0.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                            currency_txt.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                        } else {
                            symbol_txt0.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                            currency_txt.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                        }
                    }

                    currency_txt.setText(curr.get("price").toString());
                    percent_txt.setText(curr.get("priceCny").toString());

                    JSONObject curr1 = (JSONObject) quotes.get(1);
                    btcStr1 = curr1.getString("symbol");
                    symbol_txt1.setText(curr1.getString("symbol") + "/" + curr1.getString("currency"));
                    String rate1 = curr1.get("rate").toString();
                    symbol_txt10.setText(rate1 + "%");
                    if (!TextUtils.isEmpty(col) && col.equals("1")) {
                        if (rate1.contains("-")) {
                            symbol_txt10.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                            currency_txt1.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                        } else {
                            symbol_txt10.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                            currency_txt1.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                        }
                    } else {
                        if (rate1.contains("-")) {
                            symbol_txt10.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                            currency_txt1.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                        } else {
                            symbol_txt10.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                            currency_txt1.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                        }
                    }

                    currency_txt1.setText(curr1.get("price").toString());
                    percent_txt1.setText(curr1.get("priceCny").toString());

                    JSONObject curr2 = (JSONObject) quotes.get(2);
                    btcStr2 = curr2.getString("symbol");
                    symbol_txt2.setText(curr2.getString("symbol") + "/" + curr2.getString("currency"));
                    String rate2 = curr2.get("rate").toString();
                    symbol_txt20.setText(rate2 + "%");
                    if (!TextUtils.isEmpty(col) && col.equals("1")) {
                        if (rate2.contains("-")) {
                            symbol_txt20.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                            currency_txt2.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                        } else {
                            symbol_txt20.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                            currency_txt2.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                        }
                    } else {
                        if (rate2.contains("-")) {
                            symbol_txt20.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                            currency_txt2.setTextColor(getActivity().getResources().getColor(R.color.greencolor2));
                        } else {
                            symbol_txt20.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                            currency_txt2.setTextColor(getActivity().getResources().getColor(R.color.redcolor2));
                        }
                    }

                    currency_txt2.setText(curr2.get("price").toString());
                    percent_txt2.setText(curr2.get("priceCny").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void handleError(Call call) {

            }
        });
    }

    private void getNews() {
        getNewsCall = VHttpServiceManager.getInstance().getVService().noticeTop(1);
        getNewsCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                try {
//                    JSONObject obj = resultData.returnJSONObject();
//                    JSONArray quotes = obj.getJSONArray("data");
                    Gson gson = new Gson();
                    List<MyListData> list1 = gson.fromJson(resultData.data, new TypeToken<List<MyListData>>() {
                    }.getType());
                    NewsAdapter adapter = new NewsAdapter(list1);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void handleError(Call call) {
                //   Log.e("Lavesh", call.toString());
                Log.e("Laveshhhhh", call.toString());
            }
        });
    }

    private void init() {
        getCryptoPrice();
        getNews();
//        for (int i = 0; i < IMAGES.length; i++)
//            ImagesArray.add(IMAGES[i]);
        mPager = view.findViewById(R.id.pager);

        final float density = getResources().getDisplayMetrics().density;

        flipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noticeList != null && noticeList.size() > 0) {
                    TextView tv = (TextView) flipper.getCurrentView();
                    int position = (int) tv.getTag();
                    Notice notice = noticeList.get(position);
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), notice.url);
                }
            }
        });


        NUM_PAGES = IMAGES.length;
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        startGetCropyme();
// 启动计时器
        handler.postDelayed(runnable, 1000);
    }

    private Call<ResponseBody> cropymeCall;
    private Group<Notice> noticeList;
    private String rankRulesUrl;

    private void startGetCropyme() {
        CommonUtil.cancelCall(cropymeCall);
        cropymeCall = VHttpServiceManager.getInstance().getVService().cropyme();
        cropymeCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {

                    rankRulesUrl = resultData.getItem("rankRulesUrl", String.class);


                    noticeList = resultData.getGroup("noticeList", new TypeToken<Group<Notice>>() {
                    }.getType());

                    if (noticeList != null && noticeList.size() > 0) {
                        TextView tv = null;
                        flipper.removeAllViews();
                        for (int i = 0, m = noticeList.size(); i < m; i++) {
                            tv = (TextView) InflaterUtils.inflateView(getActivity(), R.layout.notice_text);
                            tv.setText(noticeList.get(i).title);
                            tv.setTag(i);
                            flipper.addView(tv);
                        }
                        if (noticeList.size() > 1) {
                            flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
                                    R.anim.push_up_in));
                            flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
                                    R.anim.push_up_out));
                            flipper.startFlipping();
                        }
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    private TjrImageLoaderUtil tjrImageLoaderUtil;

    private void setUserInfo(User u) {
        if (u == null) return;
        tjrImageLoaderUtil.displayImageForHead(u.headUrl, ivHead);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.bigv_img)
    public void bigvnextClick(View view) {
        Intent intent = new Intent(getActivity(), HomeCropyMeActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.news_next_img)
    public void newsnextClick(View view) {
        Intent intent = new Intent(getActivity(), NoticeListActivity.class);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ivHead)
    public void headClick(View view) {
        activity.swicht4();
//        if (isLogin()) {
//            PageJumpUtil.pageJump(getActivity(), MyHomeInfoActivity.class);
//            if (getUser() != null)
//                NormalShareData.saveUserEditFlag(getActivity(), getUser().userId, 1);
//        } else {
//            PageJumpUtil.pageJump(getActivity(), LoginActivity.class);
//            getActivity().overridePendingTransition(R.anim.login_in_bottom_to_top, 0);
//        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.otc_card)
    public void otc1Click(View view) {
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.otcnext_image)
    public void otcNextClick(View view) {
        PageJumpUtil.pageJump(getActivity(), LegalMoneyActivity.class);
    }


    private String btcStr = "BTC", btcStr1 = "DOGE", btcStr2 = "ETH";

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_btc)
    public void btcClick(View view) {
        MarketActivity.pageJump(getActivity(), btcStr, 1, "");
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_btc1)
    public void btc1Click(View view) {
        MarketActivity.pageJump(getActivity(), btcStr1, 1, "");
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.ll_btc2)
    public void btc2Click(View view) {
        MarketActivity.pageJump(getActivity(), btcStr2, 1, "");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_xinbishengou:
                if (isLogin()) {
                    Intent intent = new Intent(getActivity(), XinbishengouActivity.class);
                    startActivity(intent);
                } else {
                    PageJumpUtil.pageJump(getActivity(), LoginActivity.class);
                    getActivity().overridePendingTransition(R.anim.login_in_bottom_to_top, 0);
                }
                break;
            case R.id.tvZhangfubang:
                tvZhangfubang.setSelected(true);
                tvDiefubang.setSelected(false);
                viewZhangfubang.setBackgroundResource(R.color.greencolor2);
                viewDiefubang.setBackgroundResource(R.color.white);
                sortType = "1";
                startGetMarket();
                break;
            case R.id.tvDiefubang:
                tvZhangfubang.setSelected(false);
                tvDiefubang.setSelected(true);
                viewDiefubang.setBackgroundResource(R.color.greencolor2);
                viewZhangfubang.setBackgroundResource(R.color.white);
                sortType = "2";
                startGetMarket();
                break;
            case R.id.ll_dav:
                Intent intent = new Intent(getActivity(), HomeCropyMeActivity.class);
                startActivity(intent);
                break;
            case R.id.re_otc:
                PageJumpUtil.pageJump(getActivity(), LegalMoneyActivity.class);
                break;
            case R.id.ll_otc:
                PageJumpUtil.pageJump(getActivity(), LegalMoneyActivity.class);
                break;
            case R.id.ll_zaixiankefu:
                if (!TextUtils.isEmpty(chatTopic) && !TextUtils.isEmpty(userName)) {
                    goChatOrWechatQr();
                } else {
                    startCreateChat();
                }
                break;
            case R.id.ll_zhiyashengyi:
                PageJumpUtil.pageJump(getActivity(), PledgeMainActivity.class);
                break;
        }
    }
    private String chatTopic;
    private String headUrl;
    private String userName;
    private int type;
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
    WechatQRCodeFragment wechatQRCodeFragment;
    private void showWechatQRCodeFragment(String title, String subTitle, String qrUrl) {
        wechatQRCodeFragment = WechatQRCodeFragment.newInstance(title, subTitle, qrUrl);
        wechatQRCodeFragment.showDialog(getActivity().getSupportFragmentManager(), "");
    }
    private Call<ResponseBody> createChatTopicCall;
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


    public boolean isLogin() {
        MainApplication application = getApplicationContext();
        return application != null && application.getUser() != null && application.getUser().getUserId() > 0;
    }

    public MainApplication getApplicationContext() {
        return (MainApplication) super.getActivity().getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeMarkAndKbtFragment", "HomeMarkFragment  onResume   getUserVisibleHint==" + getUserVisibleHint() + "  getParentFragment().getUserVisibleHint()==" + (getParentFragment() == null ? "null" : getParentFragment().getUserVisibleHint()));
        if (getUserVisibleHint() && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
            startGetMarket();
//            startTimer();
        } else {
            closeTimer();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeMarkAndKbtFragment", "HomeMarkFragment setUserVisibleHint   isVisibleToUser==" + isVisibleToUser + "  getParentFragment().getUserVisibleHint()==" + (getParentFragment() == null ? "null" : getParentFragment().getUserVisibleHint()));
        if (isVisibleToUser && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
            startGetMarket();
//            startTimer();
        } else {
            closeTimer();
        }
    }

    @Override
    public void onPause() {
        closeTimer();
        super.onPause();
        Log.d("setUserVisibleHint", "onPause=======");

    }

    @Override
    public void onDestroy() {
        // 停止计时器
        handler.removeCallbacks(runnable);
        closeTimer();
        releaseTimer();
        super.onDestroy();
    }


    private void startTimer() {
//        calculateTradeIncomeRunnable();//启动前也要先计算一次
        if (tjrMinuteTaskPool == null) {
            tjrMinuteTaskPool = new TjrMinuteTaskPool();
        }
        isRun2 = true;
        tjrMinuteTaskPool.startTime(getActivity(), task);

    }

    private void closeTimer() {
        isRun2 = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
    }

    private void releaseTimer() {
        isRun2 = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.release();
    }

    private Runnable task = new Runnable() {
        public void run() {
            try {
                startGetMarket();
//                String result = CropymelHttpSocket.getInstance().marketData("", sortField, sortType, String.valueOf(accountType));
//                setData(result);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("marketData", "Exception is==" + e);
            }
        }
    };
    private boolean isRun2 = false;//定时器是否在跑
    private TjrMinuteTaskPool tjrMinuteTaskPool;

    public static boolean isc = false;

    public static class HomeMarketAdapter0 extends RecyclerView.Adapter<HomeMarketAdapter0.ViewHolder> {

        private LayoutInflater mInflater;
        private List<Market> list = new ArrayList<>();
        private Context context;
        private int isLever;
        private String accountType;

        public HomeMarketAdapter0(Context context, List<Market> list, int isLever, String accountType) {
            this.mInflater = LayoutInflater.from(context);
            this.context = context;
            this.list = list;
            this.isLever = isLever;
            this.accountType = accountType;
        }

        /**
         * item显示类型
         *
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_zhangdiebang, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        /**
         * 数据的绑定显示
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

            Market data = list.get(position);

            if (position % 2 == 0) {
                holder.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.cf6f7f8));
            } else {
                holder.llItem.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }
            int a = position + 1;
            holder.tvXuhao.setText("" + a);
            if (!TextUtils.isEmpty(data.logo)) {
                Glide.with(context).load(data.logo).into(holder.imgLogo);
            } else {
                Glide.with(context).load(R.drawable.ic_account_chat).into(holder.imgLogo);
            }
            Log.d("HomeMarketAdapter", "data.symbol==" + data.symbol);
            int index = data.symbol.indexOf("/");
            if (index > 0) {
                holder.tvSymbol.setText(CommonUtil.getOriginSymbol(data.symbol));
                holder.tvSubSymbol.setText("/" + CommonUtil.getUnitSymbol(data.symbol));
            } else {
                holder.tvSymbol.setText(data.symbol);
                holder.tvSubSymbol.setText("");
            }
            holder.tvSymbolName.setText(data.name);

            if (!TextUtils.isEmpty(data.tip)) {
                holder.tvTips.setVisibility(View.VISIBLE);
                holder.tvTips.setText(data.tip);
            } else {
                holder.tvTips.setVisibility(View.GONE);
            }

            holder.tvPrice.setText(data.price);
            holder.tvRate.setText(StockChartUtil.formatNumWithSign(2, Double.parseDouble(data.rate), true) + "%");
//            tvRate.setTextColor(StockChartUtil.getRateTextColor(context,Double.parseDouble(data.rate)));
            holder.tvRate.setBackgroundResource(StockChartUtil.getRateBg(context, Double.parseDouble(data.rate)));
            holder.tv24H.setText(context.getResources().getString(R.string.liang) + data.amount);

            holder.llItem.setClickable(true);

            holder.llItem.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            isc = true;
                            Log.e("marktiao=======", "ACTION_DOWN");
                            break;
                        case MotionEvent.ACTION_UP:
                            isc = false;
                            Log.e("marktiao=======", "ACTION_UP");
                            if (onItemPlayClick != null) {
                                onItemPlayClick.onSelClick(holder.getAdapterPosition(), data.symbol);
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            isc = false;
                            Log.e("marktiao=======", "ACTION_CANCEL");
                            break;
                    }
                    return false;
                }
            });
//        holder.llItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//

//            }
//        });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tvXuhao)
            TextView tvXuhao;
            @BindView(R.id.imgLogo)
            CircleImageView imgLogo;
            @BindView(R.id.tvSymbol)
            TextView tvSymbol;
            @BindView(R.id.tvSubSymbol)
            TextView tvSubSymbol;
            @BindView(R.id.tvTips)
            TextView tvTips;
            @BindView(R.id.tvPrice)
            TextView tvPrice;
            @BindView(R.id.tvSymbolName)
            TextView tvSymbolName;
            @BindView(R.id.tvRate)
            TextView tvRate;
            @BindView(R.id.tv24H)
            TextView tv24H;

            @BindView(R.id.llItem0)
            LinearLayout llItem;


            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public interface OnPlayClickListener {
            void onSelClick(int pos, String symbol);
        }

        OnPlayClickListener onItemPlayClick;

        public void setOnPlayClickListener(OnPlayClickListener onItemPlayClick) {
            this.onItemPlayClick = onItemPlayClick;
        }
    }

}

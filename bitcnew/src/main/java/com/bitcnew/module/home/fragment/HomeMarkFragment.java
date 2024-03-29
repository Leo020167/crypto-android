package com.bitcnew.module.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.home.adapter.HomeMarketAdapter;
import com.bitcnew.module.home.entity.Market;
import com.bitcnew.util.JsonParserUtils;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.TjrMinuteTaskPool;
import com.bitcnew.R;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.CropymelHttpSocket;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 火币
 * Created by zhengmj on 19-3-8.
 */

public class HomeMarkFragment extends UserBaseFragment implements View.OnClickListener {
    //    @BindView(R.id.ll_bar)
//    LinearLayout ll_bar;
    @BindView(R.id.rvMarketList)
    RecyclerView rvMarketList;
//    @BindView(R.id.tvUsdt)
//    TextView tvUsdt;
//    @BindView(R.id.tvRechargeUsdt)
//    TextView tvRechargeUsdt;
//    @BindView(R.id.tvWithDrawUsdt)
//    TextView tvWithDrawUsdt;


    //    @BindView(R.id.llSearch)
//    LinearLayout llSearch;
    @BindView(R.id.ivSort1)
    AppCompatImageView ivSort1;
    @BindView(R.id.llSort1)
    LinearLayout llSort1;
    @BindView(R.id.ivSort2)
    AppCompatImageView ivSort2;
    @BindView(R.id.llSort2)
    LinearLayout llSort2;
    @BindView(R.id.ivSort3)
    AppCompatImageView ivSort3;
    @BindView(R.id.llSort3)
    LinearLayout llSort3;

    private int tab;//tab==0 自选 tab=1是BYY，tab=2是火币，tab=3是杠杆

    private HomeMarketAdapter homeMarketAdapter;

    private Call<ResponseBody> getHomeMarketCall;


    private Gson gsonMark = new Gson();
    private boolean isRun = false;//定时器是否在跑
    private TjrMinuteTaskPool tjrMinuteTaskPool;


    //排序用到
    private String sortField;//1：symbol,2：价格,3：涨跌幅
    private String sortType;//0默认 1 倒序 2正序

    private int isLever;//1杠杆 0不是

    private Handler handler = new Handler();

    public static HomeMarkFragment newInstance(int tab, int isLever) {
        HomeMarkFragment fragment = new HomeMarkFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        bundle.putInt("isLever", isLever);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeMarkAndKbtFragment", "HomeMarkFragment  onResume   getUserVisibleHint==" + getUserVisibleHint() + "  getParentFragment().getUserVisibleHint()==" + (getParentFragment() == null ? "null" : getParentFragment().getUserVisibleHint()));
        if (getUserVisibleHint() && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
//            startGetMarket();
            startTimer();
        } else {
            closeTimer();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeMarkAndKbtFragment", "HomeMarkFragment setUserVisibleHint   isVisibleToUser==" + isVisibleToUser + "  getParentFragment().getUserVisibleHint()==" + (getParentFragment() == null ? "null" : getParentFragment().getUserVisibleHint()));
        if (isVisibleToUser && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
//            startGetMarket();
            startTimer();
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
        closeTimer();
        releaseTimer();
        super.onDestroy();
    }


    private void startTimer() {
//        calculateTradeIncomeRunnable();//启动前也要先计算一次
        if (tjrMinuteTaskPool == null) {
            tjrMinuteTaskPool = new TjrMinuteTaskPool();
        }
        isRun = true;
        tjrMinuteTaskPool.startTime(getActivity(), task);

    }

    private void closeTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
    }

    private void releaseTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.release();
    }

    private Runnable task = new Runnable() {
        public void run() {
            try {
//                startGetMarket();
                String result = CropymelHttpSocket.getInstance().marketData("", sortField, sortType, String.valueOf(tab));
                setData(result);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("marketData", "Exception is==" + e);
            }
        }
    };

    private void setData(String result) throws Exception {
        Log.d("marketData", "result==" + result);
        if (!TextUtils.isEmpty(result)) {
            JSONObject jsonObject = new JSONObject(result);
            if (JsonParserUtils.hasAndNotNull(jsonObject, "quotes")) {
                final Group<Market> groupMarket = gsonMark.fromJson(jsonObject.getString("quotes"), new TypeToken<Group<Market>>() {
                }.getType());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (groupMarket != null && groupMarket.size() > 0) {
                            homeMarketAdapter.setGroup(groupMarket);
                        }
                        int size = (groupMarket == null ? 0 : groupMarket.size());
                        if (!isRun && size > 0) startTimer();
                    }
                });

            }
        }
    }

    public void immersionbar() {
//        Log.d("HomeMarket", "mImmersionBar==" + mImmersionBar + " ll_bar==" + ll_bar);
//        if (mImmersionBar != null && ll_bar != null) {
//            mImmersionBar
//                    .titleBar(ll_bar)
//                    .statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA)
//                    .init();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_mark, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("tab")) {
            tab = bundle.getInt("tab");
            isLever = bundle.getInt("isLever");
            Log.d("homeMarkFragment", "tab==" + tab);
        }
        tjrMinuteTaskPool = new TjrMinuteTaskPool();

        homeMarketAdapter = new HomeMarketAdapter(getActivity(), isLever,"");
        rvMarketList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvMarketList.addItemDecoration(new SimpleRecycleDivider(getActivity(), true));
        rvMarketList.setAdapter(homeMarketAdapter);
//        llSearch.setOnClickListener(this);
        llSort1.setOnClickListener(this);
        llSort2.setOnClickListener(this);
        llSort3.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void startGetMarket() {
        CommonUtil.cancelCall(getHomeMarketCall);
        getHomeMarketCall = VHttpServiceManager.getInstance().getVService().market();
        getHomeMarketCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
//                    Usdt usdt = resultData.getObject("usdt", Usdt.class);
//                    if (usdt != null && !TextUtils.isEmpty(usdt.symbol)) {
//                        tvUsdt.setText(usdt.symbol + "$" + usdt.price);
//                    }
                    Group<Market> groupMarket = resultData.getGroup("quotes", new TypeToken<Group<Market>>() {
                    }.getType());
                    if (groupMarket != null && groupMarket.size() > 0) {
                        homeMarketAdapter.setGroup(groupMarket);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.llSearch:
//                PageJumpUtil.pageJump(getActivity(), SearchCoinActivity.class);
//                break;

            case R.id.llSort1:
                if (!TextUtils.isEmpty(sortField)){
                    if (!sortField.equals("1")) {
                        sortField = "1";
                        sortType = "2";//默认正序
                    } else {
                        skipSortType();
                    }
                }

                setIconAndSort();

                break;
            case R.id.llSort2:
                if (!TextUtils.isEmpty(sortField)){
                if (!sortField.equals("2")) {
                    sortField = "2";
                    sortType = "1";//默认倒序
                } else {
                    skipSortType();
                }
                }
                setIconAndSort();
                break;
            case R.id.llSort3:
                if (!TextUtils.isEmpty(sortField)){
                if (!sortField.equals("3")) {
                    sortField = "3";
                    sortType = "1";//默认倒序
                } else {
                    skipSortType();
                }
                }
                setIconAndSort();
                break;
        }
    }

    private void skipSortType() {
        if (!TextUtils.isEmpty(sortField)){
        if (sortType.equals("1")) {
            sortType = "2";
        } else if (sortType.equals("2")) {
            sortType = "0";
        } else {
            sortType = "1";
        }
        }
    }

    private void setIconAndSort() {
        if (!TextUtils.isEmpty(sortType)){
            if (sortType.equals(0)) {
                ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
                ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
                ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
            } else {
                if (sortField.equals(1)) {
                    ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
                    ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
                    if (sortType.equals(1)) {
                        ivSort1.setImageResource(R.drawable.ic_svg_sort_down);
                    } else {
                        ivSort1.setImageResource(R.drawable.ic_svg_sort_up);
                    }
                } else if (sortField.equals(2)) {
                    ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
                    ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
                    if (sortType.equals("1")) {
                        ivSort2.setImageResource(R.drawable.ic_svg_sort_down);
                    } else {
                        ivSort2.setImageResource(R.drawable.ic_svg_sort_up);
                    }
                } else {
                    ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
                    ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
                    if (sortType.equals("1")) {

                        ivSort3.setImageResource(R.drawable.ic_svg_sort_down);
                    } else {
                        ivSort3.setImageResource(R.drawable.ic_svg_sort_up);
                    }
                }
            }
        }else{
            ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
        }

//        handler.post(task);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String result = CropymelHttpSocket.getInstance().marketData("", sortField, sortType, String.valueOf(tab));
                if (!TextUtils.isEmpty(result)) {
                    e.onNext(result);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        setData(result);
                    }
                });

    }
}

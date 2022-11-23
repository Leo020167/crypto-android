package com.bitcnew.module.home.trade.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitcnew.R;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.home.fragment.UserBaseFragment;
import com.bitcnew.module.home.trade.adapter.TradeLeverHistoryAdapter;
import com.bitcnew.module.home.trade.adapter.TradeLeverHistoryBibiAdapter;
import com.bitcnew.module.wallet.LeverInfoActivity;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.LoadMoreRecycleView;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 币币交易记录-杠杆（历史记录）
 * Created by zhengmj on 17-12-7.
 */

public class TradeHistoryLeverFragment extends UserBaseFragment {
    private boolean hasOnpause = false;
    private int pageNo = 1;
    private int pageSize = 15;
    private LoadMoreRecycleView listViewAutoLoadMore;
    private Call<ResponseBody> realCall;
    private TradeLeverHistoryAdapter tradeLeverHistoryAdapter;
    private TradeLeverHistoryBibiAdapter bibiAdapter;

    private String symbol = "";
    private String accountType = "";
    private String orderState="";//当isDone=-1才有效，filled（已成交），canceled（已撤销）

    public static TradeHistoryLeverFragment newInstance(String symbol,String accountType) {
        TradeHistoryLeverFragment f = new TradeHistoryLeverFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putString(CommonConst.ACCOUNTTYPE, accountType);
//        bundle.putInt(CommonConst.ISDONE, isDone);
        f.setArguments(bundle);
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (null == b) return;
        symbol = b.getString(CommonConst.SYMBOL, "USDT");
        accountType= b.getString(CommonConst.ACCOUNTTYPE, "");

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && listViewAutoLoadMore != null && onSale == 1) {
//            MainApplication mainApplication = ((MainApplication) getActivity().getApplicationContext());
//            if (mainApplication.projectAdd) {//上架
//                mainApplication.projectAdd = false;
//                pageNo = 1;
//                startGetMyUserProjectList();
//            }
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 0x123) {
//            if (data != null) {
//                int pos = data.getIntExtra(CommonConst.POS, -1);
//                if (pos >= 0) {
//                    Prybar order = TradeUndoneLeverAdapter.getItem(pos);
//                    order.state = OrderStateEnum.canceled.getState();
//                    coinTradeEntrustAdapter.notifyDataSetChanged();
//                }
//            }
//        }


    }

    public void reset() {
        this.symbol = "";
        orderState="";
        refresh();
    }

    public void filter(String symbol, String orderState) {
        this.symbol = symbol;
        this.orderState=orderState;
        refresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_usdt_trade_his, container, false);
        listViewAutoLoadMore = (LoadMoreRecycleView) v.findViewById(R.id.rv_list);
//        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefreshlayout);
        listViewAutoLoadMore.setLayoutManager(new LinearLayoutManager(getActivity()));
        listViewAutoLoadMore.addItemDecoration(new SimpleRecycleDivider(getActivity(), 15, 15));

//        listViewAutoLoadMore.setAdapter(adapter);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                startfindEntrustbslist();
//            }
//        });
        tradeLeverHistoryAdapter = new TradeLeverHistoryAdapter(getActivity());
        listViewAutoLoadMore.setAdapter(tradeLeverHistoryAdapter);
        tradeLeverHistoryAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        listViewAutoLoadMore.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        tradeLeverHistoryAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                Position order = (Position) t;
                LeverInfoActivity.pageJump(getActivity(),order.orderId,"");
//                CoinTradeDetailsActivity.pageJump(context,data.orderId);
//                Order order = (Order) t;
//                Bundle bundle = new Bundle();
//                bundle.putLong(CommonConst.ORDERID, order.orderId);
//                bundle.putInt(CommonConst.POS, pos);
//                PageJumpUtil.pageJumpResult(TradeHistoryLeverFragment.this, CoinTradeDetailsActivity.class, bundle);
            }
        });
        refresh();
        return v;
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack loadMoreCallBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (tradeLeverHistoryAdapter != null && tradeLeverHistoryAdapter.getRealItemCount() > 0) {
                startGetMyUserProjectList();
            } else {
                pageNo = 1;
                startGetMyUserProjectList();
            }
        }
    };

    public void setData(String accountType){
        this.accountType=accountType;
        pageNo = 1;
        startGetMyUserProjectList();
    }


    public void refresh() {
        pageNo = 1;
        if (listViewAutoLoadMore != null) {
            listViewAutoLoadMore.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startGetMyUserProjectList();
                }
            }, 500);
        }
    }


    private void startGetMyUserProjectList() {
        CommonUtil.cancelCall(realCall);
        if (!TextUtils.isEmpty(accountType)&&accountType.equals("spot")){
            realCall = VHttpServiceManager.getInstance().getVService().queryList2(symbol,accountType, -1, "", pageNo,orderState,"2");
        }else {
            realCall = VHttpServiceManager.getInstance().getVService().queryList(symbol,accountType, -1, "", pageNo,orderState);
        }

        realCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<Position> group = null;
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<Position>>() {
                    }.getType());
//                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            tradeLeverHistoryAdapter.setGroup(group);
                        } else {
                            tradeLeverHistoryAdapter.addItem(group);
                            tradeLeverHistoryAdapter.notifyDataSetChanged();
                        }
//                    }
                    pageNo++;
                }
                tradeLeverHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                tradeLeverHistoryAdapter.onLoadComplete(false, false);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}

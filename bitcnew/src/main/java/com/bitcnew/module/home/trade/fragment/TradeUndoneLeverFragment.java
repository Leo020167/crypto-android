package com.bitcnew.module.home.trade.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bitcnew.R;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.entity.Position;
import com.bitcnew.module.home.fragment.UserBaseFragment;
import com.bitcnew.module.home.trade.adapter.TradeUndoneLeverAdapter;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.LoadMoreRecycleView;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 币币交易记录-杠杆（未完成）
 * Created by zhengmj on 17-12-7.
 */

public class TradeUndoneLeverFragment extends UserBaseFragment {
    private boolean hasOnpause = false;
    private int pageNo = 1;
    private int pageSize = 15;
    private LoadMoreRecycleView listViewAutoLoadMore;
    private Call<ResponseBody> realCall;
    private TradeUndoneLeverAdapter coinTradeEntrustAdapter;

    private String symbol = "";
    private String accountType = "";

    public static TradeUndoneLeverFragment newInstance(String symbol, String accountType) {
        TradeUndoneLeverFragment f = new TradeUndoneLeverFragment();
        Bundle bundle = new Bundle();
//        bundle.putString(CommonConst.STATE, state);
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
        accountType = b.getString(CommonConst.ACCOUNTTYPE, "");

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
//                    coinTradeEntrustAdapter.removeItem(pos);
//                }
//            }
//        }


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
        coinTradeEntrustAdapter = new TradeUndoneLeverAdapter(getActivity(), accountType);
        listViewAutoLoadMore.setAdapter(coinTradeEntrustAdapter);
        coinTradeEntrustAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        listViewAutoLoadMore.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
//        coinTradeEntrustAdapter.setOnItemClick(new OnItemClick() {
//            @Override
//            public void onItemClickListen(int pos, TaojinluType t) {
//                Position order = (Position) t;
//                LeverInfoActivity.pageJump(getActivity(),order.orderId);
//            }
//        });
        refresh();
        return v;
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack loadMoreCallBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (coinTradeEntrustAdapter != null && coinTradeEntrustAdapter.getRealItemCount() > 0) {
                startGetTradeOrderList();
            } else {
                pageNo = 1;
                startGetTradeOrderList();
            }
        }
    };

    public void setData(String accountType){
        this.accountType=accountType;
        pageNo = 1;
        if (null != coinTradeEntrustAdapter) {
            coinTradeEntrustAdapter.setAccountType(accountType);
        }
        startGetTradeOrderList();
    }

    public void refresh() {
        pageNo = 1;
        if (listViewAutoLoadMore != null) {
            listViewAutoLoadMore.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startGetTradeOrderList();
                }
            }, 500);
        }
    }


    private void startGetTradeOrderList() {
        String type = null;
        String orderState = null;
        int isDone = 0;
        if ("spot".equalsIgnoreCase(accountType)) { // 币币
            type = "2";
            orderState = "0";
            isDone = 1;
        }
        CommonUtil.cancelCall(realCall);
        realCall = VHttpServiceManager.getInstance().getVService().queryList2(symbol,accountType, isDone, "", pageNo,orderState, type);
        realCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<Position> group = null;
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<Position>>() {
                    }.getType());
                    if (pageNo == 1) {
                        coinTradeEntrustAdapter.setGroup(group);
                    } else {
                        if (group != null && group.size() > 0) {
                            coinTradeEntrustAdapter.addItem(group);
                            coinTradeEntrustAdapter.notifyDataSetChanged();
                        }
                    }
                    pageNo++;
                }
                coinTradeEntrustAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                coinTradeEntrustAdapter.onLoadComplete(false, false);
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

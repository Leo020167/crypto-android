package com.bitcnew.module.myhome;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.data.sharedpreferences.SysShareData;
import com.bitcnew.module.myhome.entity.MyMessage;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.LoadMoreRecycleView;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.bitcnew.module.myhome.adapter.MyMessageAdapter;
import com.google.gson.reflect.TypeToken;
import com.bitcnew.http.base.Group;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.bitcnew.R;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;

import okhttp3.ResponseBody;

/**
 * 我的消息
 */
public class MyMessageActivity extends TJRBaseToolBarSwipeBackActivity {
//    private GetMyMsgTask task;
    private MyMessageAdapter adapter;
    private LoadMoreRecycleView listViewAutoLoadMore;
    private SwipeRefreshLayout swipeRefreshLayout;
//    private RelativeLayout rl_comment_no_data;
//    private RelativeLayout rl_web_no_data;
    private int pageSize=20;
    private int pageNo=1;

    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview;
    }
    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.xitongtongzhi);
    }

    private retrofit2.Call<ResponseBody> getMyMsgCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        findViewById(R.id.tv_web_refresh).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startGetMyMsgTask(getUserId(), "0", "0", 0);
//            }
//        });
//        rl_comment_no_data = (RelativeLayout) findViewById(R.id.rl_comment_no_data);
//        rl_web_no_data = (RelativeLayout) findViewById(R.id.rl_web_no_data);
//        rl_web_no_data.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //防止误触
//            }
//        });
        SysShareData.cleanSocialNews(MyMessageActivity.this,getUserId());
        adapter = new MyMessageAdapter(this);
        listViewAutoLoadMore = (LoadMoreRecycleView) findViewById(R.id.rv_list);
//        listViewAutoLoadMore.addItemDecoration(new SimpleRecycleDivider(this,12,12, ContextCompat.getColor(this,R.color.pageBackground)));
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefreshlayout);
        listViewAutoLoadMore.setLayoutManager(new LinearLayoutManager(this));
        listViewAutoLoadMore.setAdapter(adapter);
        listViewAutoLoadMore.setRecycleViewLoadMoreCallBack(callBack);
        listViewAutoLoadMore.addItemDecoration(new SimpleRecycleDivider(this));
        adapter.setRecycleViewLoadMoreCallBack(callBack);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo=1;
                startGetMyMsgTask();
            }
        });
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(swipeRefreshLayout!=null)swipeRefreshLayout.setRefreshing(true);//前面都已经判断过了,不知道为何这里有时候会报null,所以在判断一下
                startGetMyMsgTask();
            }
        }, 500);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (adapter != null && adapter.getRealItemCount() > 0) {
                MyMessage myAnswerEntity=adapter.getItem(adapter.getItemCount()-2);
                if(myAnswerEntity!=null){
                    startGetMyMsgTask();

                }
            }
        }
    };
    private void startGetMyMsgTask() {
        CommonUtil.cancelCall(getMyMsgCall);
        getMyMsgCall = VHttpServiceManager.getInstance().getVService().findMyMessageList(pageNo);
        getMyMsgCall.enqueue(new MyCallBack(MyMessageActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<MyMessage> group=null;
                if(resultData.isSuccess()){
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data",new TypeToken<Group<MyMessage>>(){}.getType());
                    Log.d("Mymessage","group=="+(group==null?"null":group.size()));
                    if (group != null && group.size()>0){
                        if (pageNo == 1) {
                            adapter.setGroup(group);
                        } else {
                            adapter.addItem(group);
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                    }
                    pageNo++;
                }

                swipeRefreshLayout.setRefreshing(false);
                adapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(retrofit2.Call<ResponseBody> call) {
                super.handleError(call);
                swipeRefreshLayout.setRefreshing(false);
//                rl_web_no_data.setVisibility(View.VISIBLE);
                adapter.onLoadComplete(false, false);
            }
        });
    }


}

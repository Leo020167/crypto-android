package com.bitcnew.module.login;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.gson.Gson;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.login.adapter.CountryCodeAdapter;
import com.bitcnew.module.login.entity.AreaCode;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * 国家代码列表
 *
 * @author zhengmj
 */
public class AllCountryCodeListActivity extends TJRBaseToolBarSwipeBackActivity {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private CountryCodeAdapter countryCodeAdapter;

    private Group<AreaCode> group;
    private Group<AreaCode> searchResultGroup;


    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview_2;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.xuanzeguojiahuodiqu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        countryCodeAdapter = new CountryCodeAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor)));
//        rvList.addItemDecoration(new SimpleRecycleDivider(this));
        rvList.setAdapter(countryCodeAdapter);
        countryCodeAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                AreaCode countryCode = (AreaCode) t;
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("name", countryCode.tcName);
                bundle.putString("code", countryCode.areaCode);
                intent.putExtras(bundle);
                setResult(0x456, intent);
                finish();
            }
        });
        startGetCountrycodeinfolist();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.circle_manager_member_menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint(getResources().getString(R.string.sousuoguojiahuodiqu));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("setOnQueryTextListener", "onQueryTextSubmit   s==" + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("setOnQueryTextListener", "onQueryTextChange   s==" + s);
                if (TextUtils.isEmpty(s)) {
                    countryCodeAdapter.setGroup(group);
                } else {
                    searchResultGroup = searchUser(s);
                    countryCodeAdapter.setGroup(searchResultGroup);
                }
                return false;
            }
        });

        return true;
    }

    public Group<AreaCode> searchUser(String s) {
        Group<AreaCode> searchResult = null;
        if (group != null && group.size() > 0) {
            searchResult = new Group<AreaCode>();
            for (int i = 0; i < group.size(); i++) {
                AreaCode countryCode = group.get(i);
                if (countryCode != null) {
                    if (countryCode.tcName.contains(s) || countryCode.areaCode.contains(s))
                        searchResult.add(countryCode);
                }
            }
        }
        return searchResult;
    }


    Call<ResponseBody> getMemberListCall;

    private void startGetCountrycodeinfolist() {
        CommonUtil.cancelCall(getMemberListCall);
        getMemberListCall = VHttpServiceManager.getInstance().getVService().countrycodeinfolist();
        getMemberListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    group = gson.fromJson(resultData.data,new TypeToken<Group<AreaCode>>(){}.getType());
//                    group = resultData.getGroup(resultData.data, new TypeToken<Group<AreaCode>>() {
//                    }.getType());
                    if (group != null && group.size() > 0) {
                        countryCodeAdapter.setGroup(group);
                    }
                }
            }
        });
    }


}

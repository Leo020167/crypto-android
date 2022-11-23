package com.bitcnew.module.home.trade;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.module.home.OnItemClick;
import com.bitcnew.module.home.trade.adapter.TransferAccountSelectAdapter;
import com.bitcnew.module.home.trade.entity.AccountType;
import com.bitcnew.widgets.SimpleRecycleDivider;

import java.util.ArrayList;


/**
 * 划转选择账户
 */
public class TransferSelectAccountActivity extends TJRBaseToolBarSwipeBackActivity {

    protected RecyclerView listView;
    private TransferAccountSelectAdapter transferAccountSelectAdapter;
    private ArrayList<String> data = new ArrayList<>();

    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview_2;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.xuanzezhanghu);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("accountList")) {
                data = bundle.getStringArrayList("accountList");
            }
        }

        listView = (RecyclerView) findViewById(R.id.rv_list);
        transferAccountSelectAdapter = new TransferAccountSelectAdapter(this);
        transferAccountSelectAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                AccountType accountType=(AccountType)t;
                Intent intent=new Intent();
                Bundle bundle1=new Bundle();
                bundle1.putString("accountName",accountType.accountName);
                bundle1.putString("accountType",accountType.accountType);
                intent.putExtras(bundle1);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        listView.setAdapter(transferAccountSelectAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.addItemDecoration(new SimpleRecycleDivider(this));
        listView.setBackgroundColor(ContextCompat.getColor(this, R.color.pageBackground));
        transferAccountSelectAdapter.setGroup(getApplicationContext().accountTypeGroup);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


}

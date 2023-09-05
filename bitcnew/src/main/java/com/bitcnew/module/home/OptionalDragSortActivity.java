package com.bitcnew.module.home;


import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.widget.dialog.ui.TjrBaseDialog;
import com.bitcnew.module.home.adapter.OptioalDragSortAdapter;
import com.bitcnew.module.home.entity.Market;
import com.bitcnew.http.base.Group;
import com.bitcnew.R;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.module.home.adapter.ItemDragCallback;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 我的自选拖动排序
 */

public class OptionalDragSortActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.cbAll)
    CheckBox cbAll;
    @BindView(R.id.tvDelete)
    TextView tvDelete;
    private OptioalDragSortAdapter optioalDragSortAdapter;

    private Call<ResponseBody> getHomeOptionalCall;
    private Call<ResponseBody> delOptionalCall;
    private Call<ResponseBody> sortOptionalCall;
    private ItemTouchHelper helper;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.optional_sort;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.bianjizixuan);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        rvList.setBackgroundColor(ContextCompat.getColor(this, R.color.pageBackground));
        optioalDragSortAdapter = new OptioalDragSortAdapter(this);
        optioalDragSortAdapter.setOnStartDragListener(new OptioalDragSortAdapter.OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                helper.startDrag(viewHolder);
            }


            @Override
            public void onCheckedChanged() {

                Group<Market> group = optioalDragSortAdapter.getGroup();
                int checkedCount = 0;
                boolean isAllChecked = true;
                for (Market market : group) {
                    if (!market.checked) {
                        isAllChecked = false;
                    } else {
                        checkedCount += 1;
                    }
                }
                cbAll.setChecked(isAllChecked);
                tvDelete.setEnabled(checkedCount > 0);

            }
        });
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(optioalDragSortAdapter);

        ItemDragCallback callback = new ItemDragCallback();
        helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rvList);


        rvList.postDelayed(new Runnable() {
            @Override
            public void run() {
                startGetOptional();
            }
        }, 500);

        cbAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("cbAll", "isChecked==" + isChecked + "  buttonView==" + (buttonView.isPressed()));
                if (buttonView.isPressed()) {
                    optioalDragSortAdapter.setAllChecked(isChecked);
                    tvDelete.setEnabled(isChecked && optioalDragSortAdapter.getItemCount() > 0);
                }
            }
        });

        tvMenu.setText(getResources().getString(R.string.wancheng));
        tvMenu.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tvMenu.setTextColor(ContextCompat.getColor(this, R.color.c6175ae));
        tvMenu.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
    }

    private String getCheckedSymols() {
        Group<Market> group = optioalDragSortAdapter.getGroup();
        StringBuilder stringBuilder = null;
        for (Market market : group) {
            if (market.checked) {
                if (stringBuilder == null) {
                    stringBuilder = new StringBuilder(market.symbol);
                } else {
                    stringBuilder.append(",");
                    stringBuilder.append(market.symbol);
                }
            }
        }
        if (stringBuilder != null) {
            return stringBuilder.toString();
        }
        return "";
    }

    private String getSortedSymols() {
        Group<Market> group = optioalDragSortAdapter.getGroup();
        StringBuilder stringBuilder = null;
        for (int i = 0, m = group.size(); i < m; i++) {
            Market market = group.get(i);
            if (stringBuilder == null) {
                stringBuilder = new StringBuilder(market.symbol + ":" + i);
            } else {
                stringBuilder.append(",");
                stringBuilder.append(market.symbol + ":" + i);
            }
        }
        if (stringBuilder != null) {
            return stringBuilder.toString();
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMenu:
                startSortOptional(getSortedSymols());
                break;
            case R.id.tvDelete:
                showDelTokaDialog(getCheckedSymols());
                break;
        }
    }

    private TjrBaseDialog delDialog;

    private void showDelTokaDialog(final String symbols) {
        delDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startDelOptional(symbols);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        delDialog.setTvTitle(getResources().getString(R.string.tishi));
        delDialog.setMessage(getResources().getString(R.string.quedingshanchuzixuan));
        delDialog.show();
    }


    private void startDelOptional(final String symbols) {
        Log.d("startDelOptional", "symbols==" + symbols);
        com.bitcnew.http.util.CommonUtil.cancelCall(delOptionalCall);
        delOptionalCall = VHttpServiceManager.getInstance().getVService().optionalDel(symbols);
        delOptionalCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, OptionalDragSortActivity.this);
                    getApplicationContext().optionalFlag = true;
                    String[] symbolsArrays = symbols.split(",");
                    for (String s : symbolsArrays) {
                        for (Market market : optioalDragSortAdapter.getGroup()) {
                            if (market.symbol.equals(s)) {
                                optioalDragSortAdapter.removeItem(market);
                                break;
                            }
                        }
                    }
                    optioalDragSortAdapter.notifyDataSetChanged();
                }
            }

        });
    }


    private void startSortOptional(final String symbols) {
        com.bitcnew.http.util.CommonUtil.cancelCall(sortOptionalCall);
        sortOptionalCall = VHttpServiceManager.getInstance().getVService().sortOptional(symbols);
        sortOptionalCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, OptionalDragSortActivity.this);
                    getApplicationContext().optionalFlag = true;
                    finish();
                }
            }

        });
    }


    private void startGetOptional() {
        com.bitcnew.http.util.CommonUtil.cancelCall(getHomeOptionalCall);
        getHomeOptionalCall = VHttpServiceManager.getInstance().getVService().optionalCoinFindAll();
        getHomeOptionalCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {

                    Group<Market> group = resultData.getGroup("symbols", new TypeToken<Group<Market>>() {
                    }.getType());

                    optioalDragSortAdapter.setGroup(group);

//                    String[] symbolArrays = resultData.getStringArray("symbols");
//                    if (symbolArrays != null && symbolArrays.length > 0) {
//                        Group<Market> markets = new Group<>();
//                        Market market = null;
//                        for (String s : symbolArrays) {
//                            market = new Market();
//                            market.symbol = s;
//                            markets.add(market);
//                        }
//                        optioalDragSortAdapter.setGroup(markets);
//                    }

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }
}

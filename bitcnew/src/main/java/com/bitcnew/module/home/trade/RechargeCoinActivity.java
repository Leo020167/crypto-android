package com.bitcnew.module.home.trade;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.CommonUtil;
import com.bitcnew.module.home.RechargeCoinActivity2;
import com.bitcnew.module.home.adapter.RechargeCoinAdapter;
import com.bitcnew.module.home.bean.WithdrawGetInfoBean;
import com.bitcnew.module.home.trade.history.TakeCoinHistoryActivity;
import com.bitcnew.util.DonwloadSaveImg;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.SimpleSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 充币
 */
public class RechargeCoinActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.llSelectCoin)
    LinearLayout llSelectCoin;
    @BindView(R.id.llKeyType)
    LinearLayout llKeyType;
    @BindView(R.id.tvSaveQr)
    TextView tvSaveQr;
    @BindView(R.id.ivQr)
    ImageView ivQr;
    @BindView(R.id.tvCopyAddress)
    TextView tvCopyAddress;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.rvKeyType)
    RecyclerView rvKeyType;
    @BindView(R.id.txt_sure)
    TextView txt_sure;


    private String address,image,chainType = "";
    private Bitmap mBitmap;
    private RechargeCoinAdapter adapter;

    @Override
    protected int setLayoutId() {
        return R.layout.recharge_coin;
    }


    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.chongbi);
    }

    public static void pageJump(Context context, String defaultSymbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, defaultSymbol);
        PageJumpUtil.pageJump(context, RechargeCoinActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        String defaultSymbol = "";
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                defaultSymbol = bundle.getString(CommonConst.SYMBOL, "");
            }
        }
//        llSelectCoin.setOnClickListener(this);
        tvSaveQr.setOnClickListener(this);
        txt_sure.setOnClickListener(this);
        tvCopyAddress.setOnClickListener(this);
        rvKeyType.setLayoutManager(new GridLayoutManager(this, 3));
        rvKeyType.addItemDecoration(new SimpleSpaceItemDecoration(this, 0, 5, 5, 5));
        adapter = new RechargeCoinAdapter(this,list);
        rvKeyType.setAdapter(adapter);
        adapter.setOnPlayClickListener(new RechargeCoinAdapter.OnPlayClickListener() {
            @Override
            public void onSelClick(int pos) {
                address = list.get(pos).getAddress();
                image = list.get(pos).getImage();
                chainType = list.get(pos).getType();
                if (!TextUtils.isEmpty(address)){
                    tvAddress.setText(address);
                }
                if (!TextUtils.isEmpty(image)){
                    Glide.with(RechargeCoinActivity.this).load(image).into(ivQr);
                }
            }
        });
        tvMenu.setText(getResources().getString(R.string.jilu));
        tvMenu.setOnClickListener(this);
        startGetInfo();
    }


    private List<WithdrawGetInfoBean.Infos> list = new ArrayList<>();
    private Call<ResponseBody> getGetInfoCall;
    private void startGetInfo() {
        CommonUtil.cancelCall(getGetInfoCall);
        getGetInfoCall = VHttpServiceManager.getInstance().getVService().withdrawGetInfo();
        getGetInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    WithdrawGetInfoBean bean = gson.fromJson(resultData.data,WithdrawGetInfoBean.class);
                    if (null!=bean){
                        if (null!=bean.getInfos()&&bean.getInfos().size()>0){
                            list.clear();
                            list.addAll(bean.getInfos());
                            list.get(0).setSel(true);
                            address = list.get(0).getAddress();
                            image = list.get(0).getImage();
                            chainType = list.get(0).getType();
                            if (!TextUtils.isEmpty(address)){
                                tvAddress.setText(address);
                            }
                            if (!TextUtils.isEmpty(image)){
                                Glide.with(RechargeCoinActivity.this).load(image).into(ivQr);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.llSelectCoin:
//                if (coinGroup == null) {
//                    startGetCoinList();
//                } else {
//                    showTakeCoinSelectFragment();
//                }
//                break;
            case R.id.tvSaveQr:
                try {
                    if (image != null && !TextUtils.isEmpty(address)) {
                        DonwloadSaveImg.donwloadImg(this,image);//iPath
                    }
                }catch (Exception e){

                }
                break;
            case R.id.tvCopyAddress:
                if (image != null && !TextUtils.isEmpty(address)) {
                    com.bitcnew.util.CommonUtil.copyText(RechargeCoinActivity.this, address);
                }
                break;
            case R.id.tvMenu:
                PageJumpUtil.pageJump(RechargeCoinActivity.this, TakeCoinHistoryActivity.class);
                break;
            case R.id.txt_sure:
                Intent intent = new Intent(this, RechargeCoinActivity2.class);
                intent.putExtra("chainType",chainType);
                startActivity(intent);
                break;
        }
    }

}

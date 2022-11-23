package com.bitcnew.module.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.model.User;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.module.dialog.ShequDialog;
import com.bitcnew.module.home.adapter.ShequAdapter;
import com.bitcnew.module.home.bean.InviteHomeBean;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.widgets.CircleImageView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ShequActivity extends TJRBaseToolBarActivity implements View.OnClickListener {

    @BindView(R.id.view_top)
    View viewTop;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_shequrenshu)
    TextView txtShequrenshu;
    @BindView(R.id.txt_yaoqingmashuliang)
    TextView txtYaoqingmashuliang;
    @BindView(R.id.txt_v1)
    TextView txtV1;
    @BindView(R.id.txt_zongyongjinshouru)
    TextView txtZongyongjinshouru;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.txt_buy)
    TextView txtBuy;


    private List<InviteHomeBean.InviteListBean> list = new ArrayList();
    private ShequAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tjrImageLoaderUtil = new TjrImageLoaderUtil(R.drawable.ic_common_mic2);
        imgBack.setOnClickListener(this);
        txtBuy.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new ShequAdapter(this,list);
        recyclerView.setAdapter(adapter);


        setUserInfo(this.getUser());//先初始化
        getInviteHome();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_shequ;
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_buy:
                ShequDialog dialog = new ShequDialog(this,inviteCodePrice);
                dialog.txtSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(dialog.et_shuliang.getText().toString())){
                            dialog.dismiss();
                            getInviteBuy(dialog.et_shuliang.getText().toString());
                        }else {
                            CommonUtil.showmessage(getResources().getString(R.string.qingshuruduihuanshuliang), ShequActivity.this);
                        }
                    }
                });
                dialog.show();
                break;
        }
    }

    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private void setUserInfo(User u) {
        if (u == null) return;
        txtName.setText(u.getUserName());
        tjrImageLoaderUtil.displayImageForHead(u.headUrl, ivHead);
    }


    private String inviteCodePrice;//邀请码单价
    private Call<ResponseBody> getInviteHomeCall;
    private void getInviteHome() {
        getInviteHomeCall = VHttpServiceManager.getInstance().getVService().getInviteHome();
        getInviteHomeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    InviteHomeBean bean = gson.fromJson(resultData.data,InviteHomeBean.class);
                    if (null!=bean){
                        txtYaoqingmashuliang.setText(getResources().getString(R.string.yaoqingmashuliang)+bean.getInviteCount());
                        txtShequrenshu.setText(getResources().getString(R.string.shequrenshu)+bean.getTeamCount());
                        txtZongyongjinshouru.setText(bean.getSumAmount());
                        inviteCodePrice = bean.getInviteCodePrice();
                        if (null!=bean.getInviteList()&&bean.getInviteList().size()>0){
                              list.clear();
                              list.addAll(bean.getInviteList());
                              adapter.notifyDataSetChanged();
                        }else {
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }


    private Call<ResponseBody> getInviteBuyCall;
    private void getInviteBuy(String count ) {
        getInviteBuyCall = VHttpServiceManager.getInstance().getVService().getInviteBuy(count);
        getInviteBuyCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, ShequActivity.this);
                    getInviteHome();
                }
            }
        });
    }
}
package com.bitcnew.module.home.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.constant.CommonConst;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.model.User;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.module.dialog.ShequDialog;
import com.bitcnew.module.home.SearchActivity;
import com.bitcnew.module.home.ShequActivity;
import com.bitcnew.module.home.adapter.HomeAttentionAdapter;
import com.bitcnew.module.home.adapter.ShequAdapter;
import com.bitcnew.module.home.bean.InviteHomeBean;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;
import com.bitcnew.widgets.CircleImageView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class HomeShequFragment extends UserBaseImmersionBarFragment implements View.OnClickListener{
    @BindView(R.id.view_top)
    View viewTop;
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
    @BindView(R.id.txt_buy2)
    TextView txtBuy2;


    private List<InviteHomeBean.InviteListBean> list = new ArrayList();
    private ShequAdapter adapter;
    private TjrImageLoaderUtil tjrImageLoaderUtil;


    public static HomeShequFragment newInstance() {
        HomeShequFragment fragment = new HomeShequFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shequ, container, false);
        ButterKnife.bind(this, view);
        tjrImageLoaderUtil = new TjrImageLoaderUtil(R.drawable.ic_common_mic2);
        txtBuy2.setOnClickListener(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        adapter = new ShequAdapter(getActivity(),list);
        recyclerView.setAdapter(adapter);


        setUserInfo(this.getUser());//先初始化
        getInviteHome();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_buy2:
                ShequDialog dialog = new ShequDialog(getActivity(),inviteCodePrice);
                dialog.txtSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(dialog.et_shuliang.getText().toString())){
                            dialog.dismiss();
                            getInviteBuy(dialog.et_shuliang.getText().toString());
                        }else {
                            CommonUtil.showmessage(getResources().getString(R.string.qingshuruduihuanshuliang), getActivity());
                        }
                    }
                });
                dialog.show();
                break;
        }
    }



    private void setUserInfo(User u) {
        if (u == null) return;
        txtName.setText(u.getUserName());
        tjrImageLoaderUtil.displayImageForHead(u.headUrl, ivHead);
    }


    private String inviteCodePrice;//邀请码单价
    private Call<ResponseBody> getInviteHomeCall;
    private void getInviteHome() {
        getInviteHomeCall = VHttpServiceManager.getInstance().getVService().getInviteHome();
        getInviteHomeCall.enqueue(new MyCallBack(getActivity()) {
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
        getInviteBuyCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, getActivity());
                    getInviteHome();
                }
            }
        });
    }
}

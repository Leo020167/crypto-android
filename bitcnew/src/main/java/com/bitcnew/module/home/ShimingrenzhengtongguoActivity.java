package com.bitcnew.module.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.myhome.entity.IdentityAuthen;
import com.bitcnew.util.MyCallBack;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ShimingrenzhengtongguoActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.txt_zhenshixingming)
    TextView txt_zhenshixingming;
    @BindView(R.id.txt_zhengjianhao)
    TextView txt_zhengjianhao;
    @BindView(R.id.txt_renzhengriqi)
    TextView txt_renzhengriqi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        img_back.setOnClickListener(this);

        startGetIdentityAuthen();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_shimingrenzhengtongguo;
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }


    private IdentityAuthen identityAuthen;
    private Call<ResponseBody> getIdentityAuthenCall;
    private void startGetIdentityAuthen() {
        getIdentityAuthenCall = VHttpServiceManager.getInstance().getVService().getIdentityAuthen();
        getIdentityAuthenCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
//                    String frontImgDemoUrl = resultData.getItem("frontImgDemoUrl", String.class);
//                    String backImgDemoUrl = resultData.getItem("backImgDemoUrl", String.class);
//                    String holdImgDemoUrl = resultData.getItem("holdImgDemoUrl", String.class);
//                    if (!TextUtils.isEmpty(frontImgDemoUrl) && !TextUtils.isEmpty(backImgDemoUrl) && !TextUtils.isEmpty(holdImgDemoUrl)) {
//                        viewDemoUrls = new ArrayList<>();
//                        viewDemoUrls.add(frontImgDemoUrl);
//                        viewDemoUrls.add(backImgDemoUrl);
//                        viewDemoUrls.add(holdImgDemoUrl);
//                    }

                    identityAuthen = resultData.getObject("identityAuth", IdentityAuthen.class);//0：审核中，1：已通过，2：未通过
                    if (null!=identityAuthen){
                        try {
                            txt_zhenshixingming.setText(identityAuthen.name);
                            txt_zhengjianhao.setText(identityAuthen.certNo);

                            long t = identityAuthen.createTime*1000;
                            Date date = new Date(t);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            String a = format.format(date);
                            txt_renzhengriqi.setText(a);
                        }catch (Exception e){

                        }
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:
                finish();
                break;
        }

    }



}
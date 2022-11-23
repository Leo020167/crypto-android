package com.bitcnew.module.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcnew.module.dialog.WebDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.adapter.ApplyBindAccountAdapter;
import com.bitcnew.module.home.bean.FollowGetTypesBean;
import com.bitcnew.module.home.entity.VUser;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.PageJumpUtil;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class ApplyBindAccountActivity extends TJRBaseToolBarActivity implements View.OnClickListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.txt_sure)
    TextView txtSure;

    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        userId = getIntent().getLongExtra("userId",0);
        txtSure.setOnClickListener(this);


        initRv();
        startFollowGetTypesCall();
        startGetCropyme();
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_apply_bind_account;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.shenqingbangding);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txt_sure){
            WebDialog dialog = new WebDialog(this);
            dialog.txtSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    showBottomDialog();
                }
            });
            dialog.show();
        }
    }


    private long typeId;
    private void initRv(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new ApplyBindAccountAdapter(this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnPlayClickListener(new ApplyBindAccountAdapter.OnPlayClickListener() {
            @Override
            public void onSelClick(int pos) {
                try {
                    if (null!=list&&list.size()>0){
                        maxMultiNum=list.get(pos).getMaxMultiNum();
                        minMultiNum=list.get(pos).getMinMultiNum();
                        typeId = list.get(pos).getId();
                    }
                }catch (Exception e){

                }
            }
        });
    }

    private boolean isRequest;
    private Call<ResponseBody> applyForFollowCall;
    private void startApplyForFollowCallCall(int multiNum) {
        if (isRequest) return;
        isRequest = true;
        CommonUtil.cancelCall(applyForFollowCall);
        applyForFollowCall = VHttpServiceManager.getInstance().getVService().applyForFollow(userId,typeId,multiNum);
        applyForFollowCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                isRequest = false;
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, ApplyBindAccountActivity.this);
                    txtSure.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                isRequest = false;
            }
        });
    }

    private boolean isclick;
    private List<FollowGetTypesBean.Types> list = new ArrayList<>();
    private ApplyBindAccountAdapter adapter;
    private Call<ResponseBody> followGetTypesCall;
    private void startFollowGetTypesCall() {
        com.bitcnew.http.util.CommonUtil.cancelCall(followGetTypesCall);
        followGetTypesCall = VHttpServiceManager.getInstance().getVService().followGetTypes(userId);
        followGetTypesCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Gson gson = new Gson();
                    FollowGetTypesBean bean = gson.fromJson(resultData.data,FollowGetTypesBean.class);
                    if (null!=bean){
                        if (!TextUtils.isEmpty(bean.getData().getShowBind())){
                            if (bean.getData().getShowBind().equals("1")){
                                txtSure.setVisibility(View.VISIBLE);
                            }else{
                                txtSure.setVisibility(View.INVISIBLE);
                            }
                        }else {
                            txtSure.setVisibility(View.INVISIBLE);
                        }


                        if (null!=bean.getData().getTypes()&&bean.getData().getTypes().size()>0){
                            maxMultiNum=bean.getData().getTypes().get(0).getMaxMultiNum();
                            minMultiNum=bean.getData().getTypes().get(0).getMinMultiNum();
                            isclick=false;
                            list.clear();
                            list.addAll(bean.getData().getTypes());
                            int a = list.size();
                            for (int i = 0; i < a; i++) {
                                if (list.get(i).getIsBind()==1){
                                    list.get(i).setSel(true);
                                    isclick=true;
                                }
                            }
                            adapter.setONclick(isclick);
                            adapter.notifyDataSetChanged();
                        }else{
                            list.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    private String maxMultiNum;
    private String minMultiNum;
    private String rankRulesUrl;
    private Call<ResponseBody> cropymeCall;
    private void startGetCropyme() {
        CommonUtil.cancelCall(cropymeCall);
        cropymeCall = VHttpServiceManager.getInstance().getVService().cropyme();
        cropymeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    rankRulesUrl = resultData.getItem("rankRulesUrl", String.class);
                    Group<VUser> group = resultData.getGroup("scoreRank", new TypeToken<Group<VUser>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {

                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    private void showBottomDialog(){
        //1、使用Dialog、设置style
        final Dialog dialog = new Dialog(this,R.style.DialogTheme);
        //2、设置布局
        View view = View.inflate(this,R.layout.dialog_bottom_layout,null);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        //设置弹出位置
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
        EditText etMultiple = dialog.findViewById(R.id.etMultiple);

        dialog.findViewById(R.id.txt_queding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etMultiple.getText().toString().trim())){
                    Toast.makeText(ApplyBindAccountActivity.this, getResources().getString(R.string.qingshurubeishu),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                int a = Integer.parseInt(etMultiple.getText().toString().trim());
                int max = Integer.parseInt(maxMultiNum);
                int min = Integer.parseInt(minMultiNum);
                if (a>max){
                    Toast.makeText(ApplyBindAccountActivity.this, getResources().getString(R.string.bunengdayu)+maxMultiNum+"!",
                            Toast.LENGTH_SHORT).show();
                }else if (a<min){
                    Toast.makeText(ApplyBindAccountActivity.this, getResources().getString(R.string.bunengxiaoyu)+minMultiNum+"!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    startApplyForFollowCallCall(a);
                }
            }
        });
    }
}
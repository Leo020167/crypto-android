package com.bitcnew.module.home;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.dialog.EditCountDialog;
import com.bitcnew.module.home.bean.SubAllInBean;
import com.bitcnew.module.home.bean.XinbishengouDetailBean;
import com.bitcnew.module.home.trade.TradeActivity;
import com.bitcnew.module.home.trade.TradeLeverActivity2;
import com.bitcnew.module.richeditor.RichEditor;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class XinbishengouDetailActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener{
    @BindView(R.id.img_tu)
    ImageView img_tu;
    @BindView(R.id.txt_title)
    TextView txt_title;
    @BindView(R.id.txt_status)
    TextView txt_status;
    @BindView(R.id.richEditor_jianjie)
    RichEditor richEditor_jianjie;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.txt_baifenbi)
    TextView txt_baifenbi;
    @BindView(R.id.txt_huodongzongliang)
    TextView txt_huodongzongliang;
    @BindView(R.id.txt_zhuangtai)
    TextView txt_zhuangtai;
    @BindView(R.id.txt_bencikeshengouzongliang)
    TextView txt_bencikeshengouzongliang;
    @BindView(R.id.txt_bencishengyushengouliang)
    TextView txt_bencishengyushengouliang;
    @BindView(R.id.txt_start_time)
    TextView txt_start_time;
    @BindView(R.id.txt_end_time)
    TextView txt_end_time;
    @BindView(R.id.txt_huodongbizhong)
    TextView txt_huodongbizhong;

    @BindView(R.id.txt_status2)
    TextView txt_status2;

    private String id;
    private String symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        txt_status2.setOnClickListener(this);
        startSubscribe_allin();
        startSubscribe_getDetail();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_xinbishengou_detail;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.xinbishengou);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_status2:
                if (isCan){
//                    TradeActivity.pageJump(this, symbol, 1);
                    TradeLeverActivity2.pageJump(this, symbol, 1);

//                    EditCountDialog dialog = new EditCountDialog(this);
//                    dialog.txt_quaneshengou.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!TextUtils.isEmpty(maxCount)){
//                                dialog.etContent.setText(maxCount);
//                            }else {
//                                dialog.etContent.setText("");
//                            }
//                        }
//                    });
//                    dialog.txtSure.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (TextUtils.isEmpty(dialog.etContent.getText().toString().trim())){
//                                CommonUtil.showmessage(getResources().getString(R.string.qingshurushengoushuliang),XinbishengouDetailActivity.this);
//                                return;
//                            }
//                            startSubscribe_getApply(dialog.etContent.getText().toString().trim());
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog.show();
                }
                break;
        }

    }

    private String maxCount;
    private Call<ResponseBody> getSubscribeAllInall;
    private void startSubscribe_allin() {
        CommonUtil.cancelCall(getSubscribeAllInall);
        getSubscribeAllInall = VHttpServiceManager.getInstance().getVService().subscribe_allin(id);
        getSubscribeAllInall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                try {
                    if (resultData.isSuccess()) {
                        Gson gson = new Gson();
                        SubAllInBean bean = gson.fromJson(resultData.data,SubAllInBean.class);
                        if (null!=bean){
                            if (!TextUtils.isEmpty(bean.getMaxCount())){
                                maxCount  = bean.getMaxCount();
                            }else{
                                maxCount = "";
                            }
                        }
                    }
                }catch (Exception e){

                }
            }

        });
    }


    private boolean isCan;
    private long userCount;
    private Call<ResponseBody> getSubscribeGetDetailCall;
    private void startSubscribe_getDetail() {
        CommonUtil.cancelCall(getSubscribeGetDetailCall);
        getSubscribeGetDetailCall = VHttpServiceManager.getInstance().getVService().subscribe_getDetail(id);
        getSubscribeGetDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                try {
                    if (resultData.isSuccess()) {
                        Gson gson = new Gson();
                        XinbishengouDetailBean bean = gson.fromJson(resultData.data,XinbishengouDetailBean.class);
                        if (null!=bean){
                            userCount = bean.getUserCount();
                            if (null!=bean.getDetail()){
                                symbol = bean.getDetail().getSymbol();
                                if (!TextUtils.isEmpty(bean.getDetail().getImage())){
                                    Glide.with(XinbishengouDetailActivity.this).load(bean.getDetail().getImage()).into(img_tu);
                                }
                                if (!TextUtils.isEmpty(bean.getDetail().getTitle())){
                                    txt_title.setText(bean.getDetail().getTitle());
                                }else{
                                }
                                switch (bean.getDetail().getState()){/// 状态：0 待开始，1 申购中，2 已结束
                                    case 0:
                                        isCan = false;
                                        txt_status.setBackgroundResource(R.drawable.border_status_1);
                                        txt_status.setText(getResources().getString(R.string.daikaishi));
                                        txt_zhuangtai.setText(getResources().getString(R.string.daikaishi));
                                        txt_status2.setBackgroundResource(R.drawable.border_status_1);
                                        txt_status2.setText(getResources().getString(R.string.yurezhong));
                                        userTime = bean.getDetail().getStartTime()*1000-System.currentTimeMillis();
                                        if (!isApp){
                                            startTime();
                                        }
                                        break;
                                    case 1:
                                        isCan = true;
                                        txt_status.setBackgroundResource(R.drawable.border_status_0);
                                        txt_status.setText(getResources().getString(R.string.shengouzhong));
                                        txt_zhuangtai.setText(getResources().getString(R.string.shengouzhong));
                                        txt_status2.setBackgroundResource(R.drawable.border_status_0);
                                        txt_status2.setText(getResources().getString(R.string.lijicanyu));
                                        userTime = bean.getDetail().getEndTime()*1000-System.currentTimeMillis();
                                        if (!isApp){
                                            startTime();
                                        }
                                        break;
                                    case 2:
                                        isCan = false;
                                        txt_status.setBackgroundResource(R.drawable.border_status_2);
                                        txt_status.setText(getResources().getString(R.string.yijieshu));
                                        txt_zhuangtai.setText(getResources().getString(R.string.yijieshu));
                                        txt_status2.setBackgroundResource(R.drawable.border_status_2);
                                        txt_status2.setText(getResources().getString(R.string.yijieshu));
                                        break;
                                }
                                if (!TextUtils.isEmpty(bean.getDetail().getContent())){
                                    richEditor_jianjie.loadDataWithBaseURL(null,bean.getDetail().getContent(), "text/html" , "utf-8", null);
                                    richEditor_jianjie.setVisibility(View.VISIBLE);
                                }else{
                                    richEditor_jianjie.setVisibility(View.GONE);
                                }

//                                double p = 0.00;
//                                if (bean.getDetail().getSum() != 0) {
//                                    double a = Double.longBitsToDouble(bean.getDetail().getAlCount());
//                                    double b = Double.longBitsToDouble(bean.getDetail().getSum());
//                                    p = a/b;
//                                } else {
//                                    p = 0.00;
//                                }
//                                int p2 = (int) Math.round(p*100);
//                                progress.setProgress(p2);
//                                txt_baifenbi.setText(p2+"%");
                                progress.setProgress(bean.getDetail().getProgressInt());
                                txt_baifenbi.setText(bean.getDetail().getProgressInt() + "%");
                                txt_huodongzongliang.setText(bean.getDetail().getSum()+"");

                                txt_bencikeshengouzongliang.setText(bean.getDetail().getSum()+"");
                                txt_bencishengyushengouliang.setText(bean.getDetail().getRate());
                                txt_huodongbizhong.setText(bean.getDetail().getSymbol());
                                txt_start_time.setText(getDateToString(bean.getDetail().getStartTime()));
                                txt_end_time.setText(getDateToString(bean.getDetail().getEndTime()));
                            }
                        }
                    }
                }catch (Exception e){

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    public static String getDateToString(long milSecond) {
        Date date = new Date(milSecond* 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
    public static String getDateToString2(long milSecond) {
        Date date = new Date(milSecond* 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    /**
     * 列表倒计时
     */
    private long userTime;
    private boolean isApp=false;
    private void startTime() {
        isApp=true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) XinbishengouDetailActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userTime > 1000) {
                            userTime -= 1000;
                            try {
                                setTime();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }, 0, 1000);
    }
    private void setTime() throws ParseException {
        if (userTime > 1000) {
            long useTime1 = userTime / 1000;
            setTimeShow(useTime1);
        }else {
        }
    }

    private void setTimeShow(long useTime) {
        int hour = (int) (useTime / 3600 );
        int min = (int) (useTime / 60 % 60);
        int second = (int) (useTime % 60);
        int day = (int) (useTime / 3600 / 24);
        String mDay, mHour, mMin, mSecond;//天，小时，分钟，秒
        second--;
        if (second < 0) {
            min--;
            second = 59;
            if (min < 0) {
                min = 59;
                hour--;
            }
        }
        if (hour < 10) {
            mHour = "0" + hour;
        } else {
            mHour = "" + hour;
        }
        if (min < 10) {
            mMin = "0" + min;
        } else {
            mMin = "" + min;
        }
        if (second < 10) {
            mSecond = "0" + second;
        } else {
            mSecond = "" + second;
        }
        String strTime = mHour + ":" + mMin + ":" + mSecond + "";
    }


    private Call<ResponseBody> getApplyCall;
    private void startSubscribe_getApply(String count) {
        CommonUtil.cancelCall(getApplyCall);
        getApplyCall = VHttpServiceManager.getInstance().getVService().subscribe_getApply(id,count);
        getApplyCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                try {
                    if (resultData.isSuccess()) {
                        CommonUtil.showmessage("购买成功",XinbishengouDetailActivity.this);
                        startSubscribe_getDetail();
                    }
                }catch (Exception e){

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }
}
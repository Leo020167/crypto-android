package com.bitcnew.module.home;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitcnew.BuildConfig;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.dialog.EditCountDialog;
import com.bitcnew.module.home.bean.SubAllInBean;
import com.bitcnew.module.home.bean.XinbishengouDetailBean;
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
    @BindView(R.id.txt_bencikeshengouzongliang)
    TextView txt_bencikeshengouzongliang;
    @BindView(R.id.text_dancishengoufanwei_label)
    TextView text_dancishengoufanwei_label;
    @BindView(R.id.text_dancishengoufanwei)
    TextView text_dancishengoufanwei;
    @BindView(R.id.txt_benciyishengou)
    TextView txt_benciyishengou;
    @BindView(R.id.txt_bencishengyushengouliang)
    TextView txt_bencishengyushengouliang;
    @BindView(R.id.txt_kaishi_time_tit)
    TextView txt_kaishi_time_tit;
    @BindView(R.id.txt_kaishi_time)
    TextView txt_kaishi_time;
    @BindView(R.id.txt_jieshu_time_tit)
    TextView txt_jieshu_time_tit;
    @BindView(R.id.txt_jieshu_time)
    TextView txt_jieshu_time;
    @BindView(R.id.txt_shengouzongliang)
    TextView txt_shengouzongliang;
    @BindView(R.id.txt_start_time)
    TextView txt_start_time;
    @BindView(R.id.txt_end_time)
    TextView txt_end_time;
    @BindView(R.id.txt_shangshijianyishijian)
    TextView txt_shangshijianyishijian;
    @BindView(R.id.txt_shangshijiecangshijian)
    TextView txt_shangshijiecangshijian;
    @BindView(R.id.txt_duihuanbili)
    TextView txt_duihuanbili;
    @BindView(R.id.txt_woyishengou)
    TextView txt_woyishengou;
    @BindView(R.id.txt_zuixiaoshengou)
    TextView txt_zuixiaoshengou;

    @BindView(R.id.txt_status2)
    TextView txt_status2;

    @BindView(R.id.txt_faqichengyuan)
    RichEditor txt_faqichengyuan;
    @BindView(R.id.txt_xiangmucanyutiaojian)
    RichEditor txt_xiangmucanyutiaojian;
    @BindView(R.id.txt_fengxiantishi)
    RichEditor txt_fengxiantishi;
    @BindView(R.id.txt_bizhongjieshao)
    RichEditor txt_bizhongjieshao;
    @BindView(R.id.txt_xiangmujieshao)
    RichEditor txt_xiangmujieshao;
    @BindView(R.id.txt_shengoushuoming)
    RichEditor txt_shengoushuoming;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        txt_status2.setOnClickListener(this);
        startSubscribe_allin();
        startSubscribe_getDetail();

        if ("fwdetsc".equals(BuildConfig.FLAVOR)) {
            text_dancishengoufanwei_label.setVisibility(View.VISIBLE);
            text_dancishengoufanwei.setVisibility(View.VISIBLE);
        }
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
                    EditCountDialog dialog = new EditCountDialog(this);
                    dialog.txt_quaneshengou.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(maxCount)){
                                dialog.etContent.setText(maxCount);
                            }else {
                                dialog.etContent.setText("");
                            }
                        }
                    });
                    dialog.txtSure.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (TextUtils.isEmpty(dialog.etContent.getText().toString().trim())){
                                CommonUtil.showmessage(getResources().getString(R.string.qingshurushengoushuliang),XinbishengouDetailActivity.this);
                                return;
                            }
                            startSubscribe_getApply(dialog.etContent.getText().toString().trim());
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
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
                                        txt_status2.setBackgroundResource(R.drawable.border_status_1);
                                        txt_status2.setText(getResources().getString(R.string.yurezhong));
                                        txt_kaishi_time_tit.setVisibility(View.VISIBLE);
                                        txt_kaishi_time.setVisibility(View.VISIBLE);
                                        txt_jieshu_time_tit.setVisibility(View.GONE);
                                        txt_jieshu_time.setVisibility(View.GONE);
                                        userTime = bean.getDetail().getStartTime()*1000-System.currentTimeMillis();
                                        if (!isApp){
                                            startTime();
                                        }
                                        break;
                                    case 1:
                                        isCan = true;
                                        txt_status.setBackgroundResource(R.drawable.border_status_0);
                                        txt_status.setText(getResources().getString(R.string.shengouzhong));
                                        txt_status2.setBackgroundResource(R.drawable.border_status_0);
                                        txt_status2.setText(getResources().getString(R.string.shengou));
                                        txt_kaishi_time_tit.setVisibility(View.GONE);
                                        txt_kaishi_time.setVisibility(View.GONE);
                                        txt_jieshu_time_tit.setVisibility(View.VISIBLE);
                                        txt_jieshu_time.setVisibility(View.VISIBLE);
                                        userTime = bean.getDetail().getEndTime()*1000-System.currentTimeMillis();
                                        if (!isApp){
                                            startTime();
                                        }
                                        break;
                                    case 2:
                                        isCan = false;
                                        txt_status.setBackgroundResource(R.drawable.border_status_2);
                                        txt_status.setText(getResources().getString(R.string.yijieshu));
                                        txt_status2.setBackgroundResource(R.drawable.border_status_2);
                                        txt_status2.setText(getResources().getString(R.string.yijieshu));
                                        txt_kaishi_time_tit.setVisibility(View.GONE);
                                        txt_kaishi_time.setVisibility(View.GONE);
                                        txt_jieshu_time_tit.setVisibility(View.GONE);
                                        txt_jieshu_time.setVisibility(View.GONE);
                                        break;
                                }
                                if (!TextUtils.isEmpty(bean.getDetail().getContent())){
                                    richEditor_jianjie.loadDataWithBaseURL(null,bean.getDetail().getContent(), "text/html" , "utf-8", null);
                                    richEditor_jianjie.setVisibility(View.VISIBLE);
                                }else{
                                    richEditor_jianjie.setVisibility(View.GONE);
                                }

                                double p = 0.00;
                                if (bean.getDetail().getSum() != 0) {
                                    double a = Double.longBitsToDouble(bean.getDetail().getAlCount());
                                    double b = Double.longBitsToDouble(bean.getDetail().getSum());
                                    p = a/b;
                                } else {
                                    p = 0.00;
                                }
                                int p2 = (int) Math.round(p*100);
                                progress.setProgress(p2);
                                txt_baifenbi.setText(p2+"%");
                                txt_bencikeshengouzongliang.setText(bean.getDetail().getSum()+"");
                                txt_benciyishengou.setText(bean.getDetail().getAlCount()+"");
                                long s = bean.getDetail().getSum() - bean.getDetail().getAlCount();
                                txt_bencishengyushengouliang.setText(s+"");
                                txt_shengouzongliang.setText(bean.getDetail().getAllSum()+"");
                                txt_start_time.setText(getDateToString(bean.getDetail().getStartTime())+"（香港時間）");
                                txt_end_time.setText(getDateToString(bean.getDetail().getEndTime())+"（香港時間）");
                                txt_shangshijianyishijian.setText(getDateToString(bean.getDetail().getTradeTime())+"（香港時間）");
                                txt_shangshijiecangshijian.setText(getDateToString(bean.getDetail().getLiftBanTime())+"（香港時間）");
                                txt_duihuanbili.setText(bean.getDetail().getRate()+"USDT");
                                txt_woyishengou.setText(userCount+"");
                                txt_zuixiaoshengou.setText(bean.getDetail().getMin());
                                text_dancishengoufanwei.setText(bean.getDetail().getMax());

                                if (!TextUtils.isEmpty(bean.getDetail().getAuthorSummary())){
                                    txt_faqichengyuan.loadDataWithBaseURL(null,bean.getDetail().getAuthorSummary(), "text/html" , "utf-8", null);
                                }
                                if (!TextUtils.isEmpty(bean.getDetail().getSummary())){
                                    txt_xiangmujieshao.loadDataWithBaseURL(null,bean.getDetail().getSummary(), "text/html" , "utf-8", null);
                                }
                                if (!TextUtils.isEmpty(bean.getDetail().getContent())){
                                    txt_bizhongjieshao.loadDataWithBaseURL(null,bean.getDetail().getContent(), "text/html" , "utf-8", null);
                                }

                                if (!TextUtils.isEmpty(bean.getDetail().getCondition())){
                                    txt_xiangmucanyutiaojian.loadDataWithBaseURL(null,bean.getDetail().getCondition(), "text/html" , "utf-8", null);
                                }
                                if (!TextUtils.isEmpty(bean.getDetail().getWarning())){
                                    txt_fengxiantishi.loadDataWithBaseURL(null,bean.getDetail().getWarning(), "text/html" , "utf-8", null);
                                }

                                if (!TextUtils.isEmpty(bean.getDetail().getDescription())){
                                    txt_shengoushuoming.loadDataWithBaseURL(null,bean.getDetail().getDescription(), "text/html" , "utf-8", null);
                                }

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
            txt_kaishi_time_tit.setVisibility(View.GONE);
            txt_kaishi_time.setVisibility(View.GONE);
            txt_jieshu_time_tit.setVisibility(View.GONE);
            txt_jieshu_time.setVisibility(View.GONE);
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
        txt_kaishi_time.setText(strTime);
        txt_jieshu_time.setText(strTime);
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
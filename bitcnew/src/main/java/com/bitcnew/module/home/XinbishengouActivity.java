package com.bitcnew.module.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;
import com.bitcnew.common.entity.ResultData;
import com.bitcnew.http.tjrcpt.VHttpServiceManager;
import com.bitcnew.module.home.bean.XinbishengouListBean;
import com.bitcnew.module.richeditor.RichEditor;
import com.bitcnew.util.CommonUtil;
import com.bitcnew.util.MyCallBack;
import com.bitcnew.util.TjrMinuteTaskPool;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class XinbishengouActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvOption1)
    TextView tvOption1;
    @BindView(R.id.tvOption2)
    TextView tvOption2;
    @BindView(R.id.tvOption3)
    TextView tvOption3;
    @BindView(R.id.view_1)
    View view_1;
    @BindView(R.id.view_2)
    View view_2;
    @BindView(R.id.view_3)
    View view_3;


    private XinbishengouAdapter adapter;
    private List<XinbishengouListBean> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvOption1.setOnClickListener(this);
        tvOption2.setOnClickListener(this);
        tvOption3.setOnClickListener(this);
        tvOption1.setSelected(true);
        tvOption2.setSelected(false);
        tvOption3.setSelected(false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new XinbishengouAdapter(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnPlayClickListener(new XinbishengouAdapter.OnPlayClickListener() {
            @Override
            public void onSelClick(int pos) {
                Intent intent = new Intent(XinbishengouActivity.this, XinbishengouDetailActivity.class);
                intent.putExtra("id", "" + list.get(pos).getId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_xinbishengou;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.xinbishengou);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSubscribe_getList();
    }

    @Override
    public void onPause() {
        closeTimer();
        super.onPause();
    }

    private TjrMinuteTaskPool tjrMinuteTaskPool;
    private Runnable task = new Runnable() {
        public void run() {
            try {
                startSubscribe_getList();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("marketData", "Exception is==" + e);
            }
        }
    };

    private void startTimer() {
        if (tjrMinuteTaskPool == null) {
            tjrMinuteTaskPool = new TjrMinuteTaskPool();
        }
        isRun = true;
        tjrMinuteTaskPool.startTime(this, task);

    }

    private void closeTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
    }

    private int type = 0;//0 待开始，1 申购中，2 已结束

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvOption1:
                isRun = false;
                tvOption1.setSelected(true);
                tvOption2.setSelected(false);
                tvOption3.setSelected(false);
                view_1.setVisibility(View.VISIBLE);
                view_2.setVisibility(View.INVISIBLE);
                view_3.setVisibility(View.INVISIBLE);
                type = 0;
                startSubscribe_getList();
                break;
            case R.id.tvOption2:
                isRun = false;
                tvOption1.setSelected(false);
                tvOption2.setSelected(true);
                tvOption3.setSelected(false);
                view_1.setVisibility(View.INVISIBLE);
                view_2.setVisibility(View.VISIBLE);
                view_3.setVisibility(View.INVISIBLE);
                type = 1;
                startSubscribe_getList();
                break;
            case R.id.tvOption3:
                isRun = false;
                tvOption1.setSelected(false);
                tvOption2.setSelected(false);
                tvOption3.setSelected(true);
                view_1.setVisibility(View.INVISIBLE);
                view_2.setVisibility(View.INVISIBLE);
                view_3.setVisibility(View.VISIBLE);
                type = 2;
                startSubscribe_getList();
                break;
        }
    }


    public static boolean isc = false;
    private boolean isRun = false;//定时器是否在跑
    private Call<ResponseBody> getSubscribeGetListCall;

    private void startSubscribe_getList() {
        CommonUtil.cancelCall(getSubscribeGetListCall);
        getSubscribeGetListCall = VHttpServiceManager.getInstance().getVService().subscribe_getList();
        getSubscribeGetListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                try {
                    if (resultData.isSuccess()) {
                        Gson gson = new Gson();
                        List<XinbishengouListBean> myObject = gson.fromJson(resultData.data, new TypeToken<List<XinbishengouListBean>>() {
                        }.getType());
                        if (null != myObject && myObject.size() > 0) {
                            list.clear();
                            int a = myObject.size();
                            for (int i = 0; i < a; i++) {
//                                if (myObject.get(i).getState()==0){//0 待开始
//                                    long b = myObject.get(i).getStartTime()*1000 - System.currentTimeMillis(); //“距离开始还剩：”倒计时时间=开始时间（秒）*1000  - 系统当前时间（毫秒）
//                                    myObject.get(i).setUserTime(b);    //倒计时的时间存到用户数据下的新建一个字段里
//                                }else if (myObject.get(i).getState()==1){//1 申购中
//                                    long c = myObject.get(i).getEndTime()*1000 - System.currentTimeMillis();//“距离结束还剩：”倒计时时间=结束时间（秒）*1000  - 系统当前时间（毫秒）
//                                    myObject.get(i).setUserTime(c);   //倒计时的时间存到用户数据下的新建一个字段里
//                                }else{//2 已结束
//                                    myObject.get(i).setUserTime(0);
//                                }
                                if (myObject.get(i).getState() == type) {
                                    list.add(myObject.get(i));
                                }
                                if (!isc) {
                                    if (isRun) {
                                        adapter.notifyItemChanged(i, R.id.progress_item);
                                        adapter.notifyItemChanged(i,R.id.txt_baifenbi);
                                    }
                                }
                            }

                            if (!isc) {
                                if (!isRun) {
                                    adapter.notifyDataSetChanged();
                                }
                            }
                            if (!isRun && a > 0) startTimer();
                        }

                    }
                } catch (Exception e) {

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    public static class XinbishengouAdapter extends RecyclerView.Adapter<XinbishengouAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private List<XinbishengouListBean> list = new ArrayList<>();
        private Context context;

        public XinbishengouAdapter(Context context, List<XinbishengouListBean> list) {
            this.mInflater = LayoutInflater.from(context);
            this.context = context;
            this.list = list;
            startTime();
        }

        /**
         * 列表倒计时
         */
        private void startTime() {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < list.size(); i++) {
                                long useTime = list.get(i).getUserTime();
                                if (useTime > 1000) {
                                    useTime -= 1000;
                                    list.get(i).setUserTime(useTime);
//                                notifyItemChanged(i);
                                    notifyItemRangeChanged(i, getItemCount(), UPDATE_COUNT_DOWN_TIME);
                                }
                            }
                        }
                    });
                }
            }, 0, 1000);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.item_xinbishengou, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        private final static int UPDATE_COUNT_DOWN_TIME = 1;

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
            if (null == payloads || payloads.size() < 1) {
                onBindViewHolder(holder, position);
            } else {
//            for (Object payload : payloads) {
//                // 刷新倒计时
//                if (payload instanceof Integer && ((int) payload) == UPDATE_COUNT_DOWN_TIME) {
////                    ((ViewHolder) holder).txt_time.setText("刷新了");
//                    try {
//                        setTime(holder, position);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            XinbishengouListBean item = list.get(position);
            if (!TextUtils.isEmpty(item.getImage())) {
                Glide.with(context).load(item.getImage()).into(holder.img_tu);
                holder.img_tu.setVisibility(View.VISIBLE);
            }else {
                holder.img_tu.setVisibility(View.INVISIBLE);
            }
            if (!TextUtils.isEmpty(item.getSummary())) {
                holder.richEditor.loadDataWithBaseURL(null, item.getSummary(), "text/html", "utf-8", null);
                holder.richEditor.setVisibility(View.VISIBLE);
            } else {
                holder.richEditor.setVisibility(View.INVISIBLE);
            }
            switch (item.getState()) {/// 状态：0 待开始，1 申购中，2 已结束
                case 0:
                    holder.txt_status.setBackgroundResource(R.drawable.border_status_1);
                    holder.txt_status.setText(context.getResources().getString(R.string.daikaishi));
                    break;
                case 1:
                    holder.txt_status.setBackgroundResource(R.drawable.border_status_0);
                    holder.txt_status.setText(context.getResources().getString(R.string.shengouzhong));
                    break;
                case 2:
                    holder.txt_status.setBackgroundResource(R.drawable.border_status_2);
                    holder.txt_status.setText(context.getResources().getString(R.string.yijieshu));
                    break;
            }

            double p = 0.00;
            if (item.getAmount() != 0) {
                double a = Double.longBitsToDouble(item.getAlAmount());
                double b = Double.longBitsToDouble(item.getAmount());
                p = a/b;
            } else {
                p = 0.00;
            }
            int p2 = (int) Math.round(p*100);
            holder.progress_item.setProgress(p2);
            holder.txt_baifenbi.setText(p2 + "%");
            holder.txt_shengouzongliang.setText(item.getSumAmount()+"");
            holder.txt_bencikeshengouzongliang.setText(item.getAmount()+"");
            holder.txt_s_time.setText(getDateToString(item.getStartTime())+"（香港時間）");
            holder.txt_e_time.setText(getDateToString(item.getEndTime())+"（香港時間）");


            holder.ll_all.setClickable(true);
            holder.ll_all.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            isc = true;
                            Log.e("marktiao=======", "ACTION_DOWN");
                            break;
                        case MotionEvent.ACTION_UP:
                            isc = false;
                            Log.e("marktiao=======", "ACTION_UP");
                            if (onItemPlayClick != null) {
                                onItemPlayClick.onSelClick(holder.getAdapterPosition());
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            isc = false;
                            Log.e("marktiao=======", "ACTION_CANCEL");
                            break;
                    }
                    return false;
                }
            });
        }

        public String getDateToString(long milSecond) {
            Date date = new Date(milSecond * 1000);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        }

        private void setTime(ViewHolder holder, int position) throws ParseException {
            XinbishengouListBean timeDownBean = list.get(position);
            if (timeDownBean.getUserTime() > 1000) {
                holder.txt_e_time.setVisibility(View.VISIBLE);
                long useTime = timeDownBean.getUserTime();
                useTime = useTime / 1000;
                setTimeShow(useTime, holder);
            } else {
                holder.txt_e_time.setVisibility(View.GONE);
            }

        }

        private void setTimeShow(long useTime, ViewHolder holder) {
            int hour = (int) (useTime / 3600);
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
            holder.txt_e_time.setText(strTime);

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout ll_all;
            public ImageView img_tu;
            public RichEditor richEditor;
            public TextView txt_status;
            public ProgressBar progress_item;
            public TextView txt_baifenbi;

            public TextView txt_shengouzongliang;
            public TextView txt_bencikeshengouzongliang;
            public TextView txt_s_time;
            public TextView txt_e_time;

            public ViewHolder(View view) {
                super(view);
                ll_all = (LinearLayout) view.findViewById(R.id.ll_all);
                img_tu = (ImageView) view.findViewById(R.id.img_tu);
                richEditor = (RichEditor) view.findViewById(R.id.richEditor);
                txt_status = (TextView) view.findViewById(R.id.txt_status);
                progress_item = (ProgressBar) view.findViewById(R.id.progress_item);
                txt_baifenbi = (TextView) view.findViewById(R.id.txt_baifenbi);
                txt_shengouzongliang = (TextView) view.findViewById(R.id.txt_shengouzongliang);
                txt_bencikeshengouzongliang = (TextView) view.findViewById(R.id.txt_bencikeshengouzongliang);
                txt_s_time = (TextView) view.findViewById(R.id.txt_s_time);
                txt_e_time = (TextView) view.findViewById(R.id.txt_e_time);
            }
        }

        public interface OnPlayClickListener {
            void onSelClick(int pos);
        }

        OnPlayClickListener onItemPlayClick;

        public void setOnPlayClickListener(OnPlayClickListener onItemPlayClick) {
            this.onItemPlayClick = onItemPlayClick;
        }
    }

}
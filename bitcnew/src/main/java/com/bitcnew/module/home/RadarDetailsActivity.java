package com.bitcnew.module.home;

import android.os.Bundle;
import android.widget.TextView;

import com.bitcnew.module.home.entity.SubUser;
import com.bitcnew.widgets.RadarView;
import com.bitcnew.R;
import com.bitcnew.common.base.TJRBaseToolBarSwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 雷达图说明
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class RadarDetailsActivity extends TJRBaseToolBarSwipeBackActivity {


    @BindView(R.id.rdv)
    RadarView rdv;
    @BindView(R.id.tvTolIncomeScore)
    TextView tvTolIncomeScore;
    @BindView(R.id.tvCopyRateScore)
    TextView tvCopyRateScore;
    @BindView(R.id.tvProfitShareScore)
    TextView tvProfitShareScore;
    @BindView(R.id.tvCopyNumScore)
    TextView tvCopyNumScore;
    @BindView(R.id.tvCopyBalanceScore)
    TextView tvCopyBalanceScore;

    public volatile SubUser subUser;//雷达图数据

    @Override
    protected int setLayoutId() {
        return R.layout.radar_details;
    }

    @Override
    protected String getActivityTitle() {
        return getResources().getString(R.string.leidatushuoming);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subUser = getApplicationContext().subUser;

//        setContentView(R.layout.my_cropyme);
        ButterKnife.bind(this);

        if (subUser != null) {

            List<Double> data = new ArrayList<>();
            data.add(subUser.radarFollowBalanceWeight);
            data.add(subUser.radarProfitRateWeight);
            data.add(subUser.radarFollowProfitRateWeight);
            data.add(subUser.radarFollowWinRateWeight);
            data.add(subUser.radarFollowNumWeight);
            rdv.setData(data);

            ArrayList<String> titles = new ArrayList<>();

            titles.add(getResources().getString(R.string.gendanyinglie)+"\n");
            titles.add(getResources().getString(R.string.yinglinengli)+"\n");
            titles.add(getResources().getString(R.string.gendanshouyilv)+"\n");
            titles.add(getResources().getString(R.string.gendanshenglv)+"\n");
            titles.add(getResources().getString(R.string.renqizhishu)+"\n");

            ArrayList<String> titleValues = new ArrayList<>();
            titleValues.add(String.valueOf(subUser.radarFollowBalance));
            titleValues.add(subUser.radarProfitRate + "%");
            titleValues.add(subUser.radarFollowProfitRate + "%");
            titleValues.add(subUser.radarFollowWinRate + "%");
            titleValues.add(String.valueOf(subUser.radarFollowNum));

            rdv.setTitles(titles, titleValues);

            tvCopyBalanceScore.setText(getResources().getString(R.string.gendanyinglie)+"(" + subUser.radarFollowBalance + ")");
            tvTolIncomeScore.setText(getResources().getString(R.string.yinglinengli)+"(" + subUser.radarProfitRate + "%)");
            tvCopyRateScore.setText(getResources().getString(R.string.gendanshouyilv)+"(" + subUser.radarFollowProfitRate + "%)");
            tvProfitShareScore.setText(getResources().getString(R.string.gendanshenglv)+"(" + subUser.radarFollowWinRate + "%)");
            tvCopyNumScore.setText(getResources().getString(R.string.renqizhishu)+"(" + subUser.radarFollowNum + ")");


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            subUser = getApplicationContext().subUser = null;
        }
    }
}

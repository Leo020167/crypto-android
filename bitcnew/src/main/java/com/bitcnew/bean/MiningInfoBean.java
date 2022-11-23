package com.bitcnew.bean;

import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MiningInfoBean implements Serializable {
    private String yesProfit;
    private String calRate;
    private String sum;
    private Group<ProfitsBean> profits;


    public String getYesProfit() {
        return yesProfit;
    }

    public void setYesProfit(String yesProfit) {
        this.yesProfit = yesProfit;
    }

    public String getCalRate() {
        return calRate;
    }

    public void setCalRate(String calRate) {
        this.calRate = calRate;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public Group<ProfitsBean> getProfits() {
        return profits;
    }

    public void setProfits(Group<ProfitsBean> profits) {
        this.profits = profits;
    }

    public static class ProfitsBean implements Serializable, TaojinluType {
        private String calDate;
        private String calRate;
        private String createTime;
        private String id;
        private String profit;
        private String userId;

        public String getCalDate() {
            return calDate;
        }

        public void setCalDate(String calDate) {
            this.calDate = calDate;
        }

        public String getCalRate() {
            return calRate;
        }

        public void setCalRate(String calRate) {
            this.calRate = calRate;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}

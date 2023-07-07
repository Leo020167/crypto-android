package com.bitcnew.module.home.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class XinbishengouDetailBean implements Serializable {

    @SerializedName("userCount")
    private long userCount;//我的申购了多少XXXUSDT
    @SerializedName("detail")
    private Detail detail;

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public static class Detail implements Serializable {
        @SerializedName("title")
        private String title;//标题
        @SerializedName("alCount")
        private long alCount;//已申购XX USDT
        @SerializedName("amount")
        private long amount;
        @SerializedName("authorSummary")
        private String authorSummary;//发起成员
        @SerializedName("condition")
        private String condition;//项目参与条件
        @SerializedName("content")
        private String content;//币种介绍
        @SerializedName("warning")
        private String warning;//风险提示
        @SerializedName("summary")
        private String summary;//项目介绍
        @SerializedName("description")
        private String description;//申购说明


        @SerializedName("endTime")
        private long endTime;//结束时间
        @SerializedName("tradeTime")
        private long tradeTime;//上市交易时间
        @SerializedName("liftBanTime")
        private long liftBanTime;//上市解仓时间
        @SerializedName("expCountry")
        private String expCountry;
        @SerializedName("id")
        private String id;//申购id
        @SerializedName("image")
        private String image;//大图
        @SerializedName("min")
        private String min;
        @SerializedName("max")
        private String max;
        @SerializedName("rate")
        private String rate;
        @SerializedName("startTime")
        private long startTime;//开始时间
        @SerializedName("state")
        private int state;
        @SerializedName("sum")
        private long sum;//目标多少USDT
        @SerializedName("symbol")
        private String symbol;//标题
        @SerializedName("allSum")
        private long allSum;

        @SerializedName(value = "alAmount", alternate = "allAmount")
        private long allAmount;
        private long sumAmount;
        private String progress;

        public long getAllAmount() {
            return allAmount;
        }

        public void setAllAmount(long allAmount) {
            this.allAmount = allAmount;
        }

        public long getSumAmount() {
            return sumAmount;
        }

        public void setSumAmount(long sumAmount) {
            this.sumAmount = sumAmount;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public int getProgressInt() {
            if (null == getProgress() || getProgress().length() == 0) {
                return 0;
            }

            return (int) Double.parseDouble(getProgress());
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }


        public long getAllSum() {
            return allSum;
        }

        public void setAllSum(long v) {
            this.allSum = allSum;
        }



        public long getAlCount() {
            return alCount;
        }

        public void setAlCount(long alCount) {
            this.alCount = alCount;
        }

        public String getAuthorSummary() {
            return authorSummary;
        }

        public void setAuthorSummary(String authorSummary) {
            this.authorSummary = authorSummary;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(long tradeTime) {
            this.tradeTime = tradeTime;
        }

        public long getLiftBanTime() {
            return liftBanTime;
        }

        public void setLiftBanTime(long liftBanTime) {
            this.liftBanTime = liftBanTime;
        }


        public String getExpCountry() {
            return expCountry;
        }

        public void setExpCountry(String expCountry) {
            this.expCountry = expCountry;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getMin() {
            return min;
        }

        public void setMin(String min) {
            this.min = min;
        }

        public String getMax() {
            return max;
        }

        public void setMax(String max) {
            this.max = max;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public long getSum() {
            return sum;
        }

        public void setSum(long sum) {
            this.sum = sum;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }


        public String getWarning() {
            return warning;
        }

        public void setWarning(String warning) {
            this.warning = warning;
        }
    }
}

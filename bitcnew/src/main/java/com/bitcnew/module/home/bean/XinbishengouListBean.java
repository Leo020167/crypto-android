package com.bitcnew.module.home.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class XinbishengouListBean implements Serializable {

    @SerializedName("sumAmount")
    private long sumAmount;
    @SerializedName("amount")
    private long amount;
    @SerializedName("alAmount")
    private long alAmount;
    @SerializedName("alCount")
    private long alCount;
    @SerializedName("authorSummary")
    private String authorSummary;
    @SerializedName("condition")
    private String condition;
    @SerializedName("content")
    private String content;
    @SerializedName("endTime")
    private long endTime;//结束时间
    @SerializedName("expCountry")
    private String expCountry;
    @SerializedName("id")
    private String id;//申购id
    @SerializedName("image")
    private String image;//大图
    @SerializedName("min")
    private String min;
    @SerializedName("rate")
    private String rate;
    @SerializedName("startTime")
    private long startTime;//开始时间
    @SerializedName("state")
    private int state;//状态：0 待开始，1 申购中，2 已结束
    @SerializedName("sum")
    private long sum;
    @SerializedName("summary")
    private String summary;//申购的简介
    @SerializedName("symbol")
    private String symbol;
    @SerializedName("tradeTime")
    private String tradeTime;
    @SerializedName("warning")
    private String warning;
    private long userTime;


    @SerializedName("allSum")
    private long allSum;


    public long getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(long sumAmount) {
        this.sumAmount = sumAmount;
    }


    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getAlAmount() {
        return alAmount;
    }

    public void setAlAmount(long alAmount) {
        this.alAmount = alAmount;
    }


    public long getAllSum() {
        return allSum;
    }

    public void setAllSum(long allSum) {
        this.allSum = allSum;
    }


    public long getUserTime() {
        return userTime;
    }

    public void setUserTime(long userTime) {
        this.userTime = userTime;
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

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}

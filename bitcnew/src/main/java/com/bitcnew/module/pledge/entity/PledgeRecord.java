package com.bitcnew.module.pledge.entity;

import com.bitcnew.http.base.TaojinluType;

import java.io.Serializable;
import java.util.Date;

public class PledgeRecord implements TaojinluType, Serializable {
/*
{
    "count": "10",
    "duration": "7",
    "endTime": "1670997365",
    "id": "1600368529192808449",
    "pledgeId": "1",
    "preProfit": "700.0000",
    "profit": "0.0000",
    "profitRate": "10.00",
    "startTime": "1670392565",
    "status": "0",
    "symbol": "ETH",
    "userId": "2009593"
}
 */
    private String count;
    private String duration;
    private Date endTime;
    private String id;
    private String pledgeId;
    private String preProfit;
    private String profit;
    private String profitRate;
    private Date startTime;
    private String status;
    private String symbol;
    private String userId;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPledgeId() {
        return pledgeId;
    }

    public void setPledgeId(String pledgeId) {
        this.pledgeId = pledgeId;
    }

    public String getPreProfit() {
        return preProfit;
    }

    public void setPreProfit(String preProfit) {
        this.preProfit = preProfit;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(String profitRate) {
        this.profitRate = profitRate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

package com.bitcnew.module.home.entity;

import java.io.Serializable;

public class AccountBalance implements Serializable {

//    "frozenAmount": "0",
//    "holdAmount": "0",
//    "sumAmount": "0",
//    "symbol": "BTC",
//    "usdtAmount": "0",
//    "userId": "2009593"
    private String frozenAmount;
    private String holdAmount;
    private String sumAmount;
    private String symbol;
    private String usdtAmount;

    public String getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(String frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(String holdAmount) {
        this.holdAmount = holdAmount;
    }

    public String getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUsdtAmount() {
        return usdtAmount;
    }

    public void setUsdtAmount(String usdtAmount) {
        this.usdtAmount = usdtAmount;
    }
}

package com.bitcnew.module.home.trade.entity;

import java.io.Serializable;

public class RechargeCoinAddress implements Serializable {

    private String address;
    private String symbol;
    private String chainType;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getChainType() {
        return chainType;
    }

    public void setChainType(String chainType) {
        this.chainType = chainType;
    }
}

package com.bitcnew.module.home.trade.entity;

import java.io.Serializable;
import java.util.Objects;

public class CoinConfig implements Serializable {

    private String isTrade;
    private String maxWithdrawDecimals;
    private String symbol;

    public CoinConfig() {
    }

    public CoinConfig(String symbol) {
        this.symbol = symbol;
    }

    public String getIsTrade() {
        return isTrade;
    }

    public void setIsTrade(String isTrade) {
        this.isTrade = isTrade;
    }

    public String getMaxWithdrawDecimals() {
        return maxWithdrawDecimals;
    }

    public void setMaxWithdrawDecimals(String maxWithdrawDecimals) {
        this.maxWithdrawDecimals = maxWithdrawDecimals;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoinConfig that = (CoinConfig) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }
}

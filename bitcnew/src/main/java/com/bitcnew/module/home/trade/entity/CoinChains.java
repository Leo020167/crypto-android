package com.bitcnew.module.home.trade.entity;

import java.util.List;

public class CoinChains {

//    {"chainTypeList": ["Trc20", "Erc20", "Omni"],"coinList": ["BTC", "ETH", "USDT"]}
    private List<String> chainTypeList;
    private List<String> coinList;

    public List<String> getChainTypeList() {
        return chainTypeList;
    }

    public void setChainTypeList(List<String> chainTypeList) {
        this.chainTypeList = chainTypeList;
    }

    public List<String> getCoinList() {
        return coinList;
    }

    public void setCoinList(List<String> coinList) {
        this.coinList = coinList;
    }
}

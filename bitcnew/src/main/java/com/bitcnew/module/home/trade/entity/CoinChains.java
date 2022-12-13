package com.bitcnew.module.home.trade.entity;

import java.util.List;

public class CoinChains {

//    {"chainTypeList": ["Trc20", "Erc20", "Omni"],"coinList": ["BTC", "ETH", "USDT"]}
    private List<String> chainTypeList;
    private List<CoinConfig> coinList;

    public List<String> getChainTypeList() {
        return chainTypeList;
    }

    public void setChainTypeList(List<String> chainTypeList) {
        this.chainTypeList = chainTypeList;
    }

    public List<CoinConfig> getCoinList() {
        return coinList;
    }

    public void setCoinList(List<CoinConfig> coinList) {
        this.coinList = coinList;
    }
}

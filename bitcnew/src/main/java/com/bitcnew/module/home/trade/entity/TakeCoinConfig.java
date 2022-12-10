package com.bitcnew.module.home.trade.entity;

import java.io.Serializable;
import java.util.List;

public class TakeCoinConfig implements Serializable {

    private String availableAmount;
    private String fee;
    private String frozenAmount;
    private List<TakeCoinAddress> addressList;

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(String frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public List<TakeCoinAddress> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<TakeCoinAddress> addressList) {
        this.addressList = addressList;
    }
}

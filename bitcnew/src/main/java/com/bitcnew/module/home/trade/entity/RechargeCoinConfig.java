package com.bitcnew.module.home.trade.entity;

import java.io.Serializable;
import java.util.List;

public class RechargeCoinConfig implements Serializable {

    private String availableAmount;
    private String minChargeAmount;
    private List<RechargeCoinAddress> addressList;

    public String getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    public String getMinChargeAmount() {
        return minChargeAmount;
    }

    public void setMinChargeAmount(String minChargeAmount) {
        this.minChargeAmount = minChargeAmount;
    }

    public List<RechargeCoinAddress> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<RechargeCoinAddress> addressList) {
        this.addressList = addressList;
    }
}

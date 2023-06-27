package com.bitcnew.module.pledge.entity;

import com.bitcnew.http.base.TaojinluType;

import java.io.Serializable;

public class PledgeCoin implements TaojinluType, Serializable {

    //            "duration":"7",
    //            "id":"1",
    //            "minCount":"10",
    //            "profitRate":"10.00",
    //            "symbol":"ETH"

    public String duration;
    public String id;
    public String minCount;
    public String profitRate;
    public String symbol;
    public String pledgeName;
    public String dayProfitRate;
    public String endTime;
    public String returnSymbol;
    public String sort;
    public String startTime;
    public String status;

    public PledgeCoin() {

    }

}

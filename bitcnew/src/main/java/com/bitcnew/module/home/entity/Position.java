package com.bitcnew.module.home.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.bitcnew.http.base.TaojinluType;

import java.io.Serializable;

public class Position implements TaojinluType, Serializable {

    public String buySell;
    public String buySellValue;
    public String closeDone;
    public String closeFee;
    public String closePrice;
    public String closeState;
    public String closeStateDesc;
    public String closeTime;
    public String multiNum;
    public String nowStateDesc;
    public String openBail;
    public String openDone;
    public String openFee;
    public String openHand;
    public String openPrice;
    public String openState;
    public String openStateDesc;
    public String openTime;
    public String price;//持仓均价
    public String priceDecimals;
    public String profit;//持仓收益
    public String profitRate;
    public String stopLossPrice;
    public String stopWinPrice;
    public String symbol;
    public long  userId;
    public long orderId;
    public String hide;

    public String dvUid;
    public String dvHeadUrl;
    public String dvUserName;


    public String originPrice;//成本
    public String availableAmount;   //可用数量
    public double fee;//手续费
    public String amount;//
    public String createTime;//创建时间
    public String updateTime;//成交时间
    public int state;                  //状态：-1 已撤销，0 未成交，1 已成交

    public String usdtAmount;
    public String airDropAmount;
    public String subAmount;
    public String frozenAmount;

    public String orderType;

    public String sum;

}

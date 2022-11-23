package com.bitcnew.module.home.trade.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.bitcnew.http.base.Group;
import com.bitcnew.http.base.TaojinluType;
import com.bitcnew.module.myhome.entity.Receipt;

/**
 *
 *
 */
public class OtcEntity implements TaojinluType {

    public String headUrl;
    public String maxCny;
    public String minCny;
    public String price;
    public String symbol;
    public long userId;
    public String userName;

    public Group<Receipt> receiptList;

}

package com.bitcnew.module.circle.entity;

import com.bitcnew.http.base.TaojinluType;

/**
 * Created by zhengmj on 15-12-24.
 */
public class CircleMemberUser implements TaojinluType {

    public long userId;
    public int role;
    public String userName;
    public String headUrl;
    public String circleId;
    public String createTime;

    public String shiftKey; //A B C等

}

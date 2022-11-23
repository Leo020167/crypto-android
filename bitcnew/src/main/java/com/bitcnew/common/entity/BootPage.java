package com.bitcnew.common.entity;

import com.bitcnew.http.base.TaojinluType;

/**
 * 广告实体类
 * Created by zhengmj on 17-10-26.
 */

public class BootPage implements TaojinluType {

    public String cls;
    public String pkg;
    public String params;
    public String imgUrl;

    @Override
    public String toString() {
        return "cls==" + cls + "  pkg==" + pkg + "  params==" + params + "   imgUrl==" + imgUrl;
    }
}

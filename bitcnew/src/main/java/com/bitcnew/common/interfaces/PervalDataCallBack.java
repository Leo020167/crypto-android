package com.bitcnew.common.interfaces;

import com.bitcnew.common.entity.PervalResponse;
import com.bitcnew.http.base.TaojinluType;

/**
 * Created by zhengmj on 17-5-23.
 */

public interface PervalDataCallBack<T> extends TaojinluType {
    void onResponse(PervalResponse<T> var1);
}

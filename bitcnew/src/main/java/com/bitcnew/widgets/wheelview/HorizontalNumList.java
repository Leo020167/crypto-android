package com.bitcnew.widgets.wheelview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.util.InflaterUtils;

/**
 * Created by zhengmj on 19-7-19.
 */

public class HorizontalNumList extends LinearLayout {

    public HorizontalNumList(@NonNull Context context) {
        super(context);
    }

    public HorizontalNumList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalNumList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(String num) {
        if (!TextUtils.isEmpty(num)) {
            for (int i = 0; i < num.length(); i++) {
                View view = InflaterUtils.inflateView(getContext(), R.layout.lp_num_item);
                TextView textView = view.findViewById(R.id.tvNum);
                textView.setText(String.valueOf(num.charAt(i)));
                addView(view);
            }
        }


    }


}

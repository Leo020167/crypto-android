package com.bitcnew.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bitcnew.R;
import com.bitcnew.http.base.Group;
import com.bitcnew.http.util.TjrImageLoaderUtil;
import com.bitcnew.module.home.entity.SubUser;
import com.bitcnew.util.DensityUtil;

/**
 * Created by zhengmj on 19-7-19.
 */

public class UserHeadHorizontalList extends FrameLayout {
    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private int maxCount = 7;
    private int widthHeight;
    private int space;

    public UserHeadHorizontalList(@NonNull Context context) {
        super(context);
        init();
    }

    public UserHeadHorizontalList(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UserHeadHorizontalList(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        widthHeight = DensityUtil.dip2px(getContext(), 30f);
        space = widthHeight / 3;
    }

    public void setHeadList(Group<SubUser> subUsers, int count) {
        if (subUsers == null) return;

        boolean hasMore = count > maxCount;//是否需要显示99+
        for (int i = 0, m = Math.min(subUsers.size(), maxCount); i < m; i++) {
            if (i == m - 1) {
                if (hasMore) {
                    addMore(count, i);
                } else {
                    addHead(subUsers.get(i).headUrl, i);
                }
            } else {
                addHead(subUsers.get(i).headUrl, i);
            }

        }

    }


    public void setHeadList(String[] headUrls, int count) {
        if (headUrls == null) return;

        boolean hasMore = count > maxCount;//是否需要显示99+
        for (int i = 0, m = Math.min(headUrls.length, maxCount); i < m; i++) {
            if (i == m - 1) {
                if (hasMore) {
                    addMore(count, i);
                } else {
                    addHead(headUrls[i], i);
                }
            } else {
                addHead(headUrls[i], i);
            }

        }

    }

    private void addHead(String headUrl, int index) {
        CircleImageView circleImageView = new CircleImageView(getContext());
        circleImageView.setBorderOverlay(true);
        circleImageView.setBorderColor(Color.WHITE);
        circleImageView.setBorderWidth(3);
        FrameLayout.LayoutParams lp = new LayoutParams(widthHeight, widthHeight);
        lp.leftMargin = widthHeight * index - space * index;
        addView(circleImageView, lp);
        tjrImageLoaderUtil.displayImageForHead(headUrl, circleImageView);
    }

    private void addMore(int count, int index) {

        FrameLayout frameLayout = new FrameLayout(getContext());


        FrameLayout.LayoutParams lp2 = new LayoutParams(widthHeight, widthHeight);
        lp2.leftMargin = widthHeight * index - space * index;
        addView(frameLayout, lp2);

        CircleImageView circleImageView = new CircleImageView(getContext());
        circleImageView.setBorderOverlay(true);
        circleImageView.setBorderColor(Color.WHITE);
        circleImageView.setBorderWidth(3);

        frameLayout.addView(circleImageView);
        circleImageView.setImageDrawable(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.cffe9cc)));

        TextView textView = new TextView(getContext());
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.cff8f01));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        textView.setText(count + "+");
        textView.setGravity(Gravity.CENTER);

        frameLayout.addView(textView);






    }
}

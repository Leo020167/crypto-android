package com.bitcnew.common.base;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bitcnew.R;
import com.bitcnew.common.util.ResourceUtils;
import com.gyf.immersionbar.ImmersionBar;

/**
 * @author yanjun
 * @date 2021/6/8 11:47
 */
public class CommonToolbar extends RelativeLayout implements View.OnClickListener, Observer<Integer> {

    private Toolbar toolbar = null;
    private ViewGroup toolbarContentParent = null;
    private AppCompatTextView titleTv = null;
    private AppCompatImageView backIv = null;
    private AppCompatImageView moreIv = null;
    private AppCompatTextView moreTv = null;

    private OnClickListener onBackListener;
    private OnClickListener onMoreListener;

    public CommonToolbar(Context context) {
        this(context, null);
    }

    public CommonToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        if (null != attrs) {
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.CommonToolbar);
            try {
                boolean backShown = arr.getBoolean(R.styleable.CommonToolbar_backShown, true);
                setBackShown(backShown);

                String title = arr.getString(R.styleable.CommonToolbar_title);
                setTitle(title);

                Drawable moreDrawable = arr.getDrawable(R.styleable.CommonToolbar_moreSrc);
                if (null != moreDrawable) {
                    setMoreDrawable(moreDrawable);
                } else {
                    String moreText = arr.getString(R.styleable.CommonToolbar_moreText);
                    if (null != moreText && "" != moreText) {
                        setMoreText(moreText);
                    }
                }

//                int backgroundColor = arr.getColor(R.styleable.CommonToolbar_titleBackground, -1);
//                if (-1 == backgroundColor) {
//                    backgroundColor = ResourceUtils.getColor(getContext(), R.color.white);
//                }
//                setBackground(backgroundColor);

                int titleColor = arr.getColor(R.styleable.CommonToolbar_titleColor, 0);
                if (0 == titleColor) {
                    titleColor = ResourceUtils.getColorAttr(getContext(), R.attr.toolbar_textColor);
                }
                setTitleColor(titleColor);
            } finally {
                arr.recycle();
            }
        }
    }

    public void setOnBackListener(OnClickListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public void setOnMoreListener(OnClickListener onMoreListener) {
        this.onMoreListener = onMoreListener;
    }

    public void setTitle(int title) {
        setTitle(getContext().getString(title));
    }

    public void setTitle(CharSequence title) {
        titleTv.setText(title);
    }

    public AppCompatTextView getTitleTv() {
        return titleTv;
    }

    public void setBackShown(boolean shown) {
        backIv.setVisibility(shown ? View.VISIBLE : View.GONE);
    }

    public void setMoreResource(int resId) {
        moreIv.setImageResource(resId);
        moreIv.setVisibility(View.VISIBLE);
        moreTv.setVisibility(View.GONE);
    }

    public void setMoreDrawable(Drawable drawable) {
        moreIv.setImageDrawable(drawable);
        moreIv.setVisibility(View.VISIBLE);
        moreTv.setVisibility(View.GONE);
    }

    public void setMoreText(CharSequence more) {
        moreTv.setText(more);
        moreTv.setVisibility(View.VISIBLE);
        moreIv.setVisibility(View.GONE);
    }

    public void setMoreShown(boolean shown) {
        if (!shown) {
            moreTv.setTag(moreTv.getVisibility());
            moreIv.setTag(moreIv.getVisibility());
            moreTv.setVisibility(View.GONE);
            moreIv.setVisibility(View.GONE);
        } else {
            moreTv.setVisibility(moreTv.getTag() == null ? View.GONE : (int) moreTv.getTag());
            moreIv.setVisibility(moreIv.getTag() == null ? View.GONE : (int) moreIv.getTag());
        }
    }

    public void setMoreShow(boolean shown) {
        moreTv.setVisibility(shown?View.VISIBLE:View.GONE);
    }


    public void setBackground(int color) {
        //        toolbar.setBackgroundColor(color);
        setBackgroundColor(color);
    }

    public void setTitleColor(@ColorInt int color) {
        if (0 != color) {
            backIv.setColorFilter(color);
            titleTv.setTextColor(color);
            moreIv.setColorFilter(color);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (null != toolbarContentParent) {
            toolbarContentParent.addView(child, index, params);
        } else {
            super.addView(child, index, params);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == backIv) {
            handleOnBackIvClick();
        } else if (v == moreIv || v == moreTv) {
            handleOnMoreClick();
        }
    }

    @Override
    public void onChanged(Integer y) {
        int maxY = getMeasuredHeight();
        int minY = (int)(maxY * 0.6F);
        if (y > minY) {
            if (y >= maxY) {
                setBackground(Color.rgb(61, 73, 125));
            } else {
                float r = (1 - (float) (maxY - y) / maxY);
                setBackground(Color.argb((int)(255 * r), 61, 73, 125));
            }
        } else {
            setBackground(Color.TRANSPARENT);
        }
    }

    private void handleOnMoreClick() {
        if (null != onMoreListener) {
            onMoreListener.onClick(moreIv);
        }
    }

    private void handleOnBackIvClick() {
        if (null != onBackListener) {
            onBackListener.onClick(backIv);
            return;
        }

        if (getContext() instanceof Activity) {
            ((Activity) getContext()).finish();
        }
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.base_toolbar_commontoolbar, this);
        toolbarContentParent = findViewById(R.id.toolbarContentParent);
        toolbar = findViewById(R.id.toolbar);
        titleTv = findViewById(R.id.titleTvs);
        backIv = findViewById(R.id.backIvs);
        moreIv = findViewById(R.id.moreIvs);
        moreTv = findViewById(R.id.moreTv);

        int statusBarHeight = 0;
        if (getContext() instanceof Activity) {
            statusBarHeight = ImmersionBar.getStatusBarHeight((Activity) getContext());
        }
        toolbar.setPadding(0, statusBarHeight, 0, 0);

        backIv.setOnClickListener(this);
        moreIv.setOnClickListener(this);
        moreTv.setOnClickListener(this);
    }

}

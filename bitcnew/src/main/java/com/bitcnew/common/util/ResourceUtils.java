package com.bitcnew.common.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * 资源帮助
 *
 * @author yanjun
 * @since 0.0.1
 */
public final class ResourceUtils {

    /**
     * 获取尺寸
     *
     * @param resId
     * @return
     */
    public static int getDimen(Context context, int resId) {
        return context.getResources().getDimensionPixelOffset(resId);
    }

    /**
     * 从 theme 中获取尺寸
     *
     * @param context .
     * @param attrId attribute id
     */
    public static int getDimenAttr(Context context, int attrId) {
        TypedValue typedValue = new TypedValue();
        if (context.getTheme().resolveAttribute(attrId, typedValue, true)) {
            if (typedValue.type == TypedValue.TYPE_DIMENSION) {
                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                wm.getDefaultDisplay().getMetrics(displayMetrics);

                return TypedValue.complexToDimensionPixelSize(typedValue.data, displayMetrics);
            }
            if (typedValue.type == TypedValue.TYPE_REFERENCE) {
                try {
                    return context.getResources().getDimensionPixelOffset(typedValue.resourceId);
                } catch (Resources.NotFoundException e) {
                    Log.e("ResourceUtils", e.getMessage(), e);
                }
            }
        }
        return 0;
    }

    /**
     * 获取图片资源
     *
     * @param ctx
     * @param resId
     * @return
     */
    public static Drawable getDrawable(Context ctx, int resId) {
        return ctx.getResources().getDrawable(resId);
    }

    /**
     * 获取字符串数组
     *
     * @param ctx
     * @param resId
     * @return
     */
    public static String[] getStringArray(Context ctx, int resId) {
        return ctx.getResources().getStringArray(resId);
    }

    /**
     * 获取整型数组
     *
     * @param ctx
     * @param resId
     * @return
     */
    public static int[] getIntArray(Context ctx, int resId) {
        return ctx.getResources().getIntArray(resId);
    }

    /**
     * 获取字符串
     *
     * @param context
     * @param msgId
     * @return
     */
    public static String getString(Context context, int msgId) {
        return context.getString(msgId);
    }

    /**
     * 获取字符串
     *
     * @param context
     * @param msgId
     * @param formatArgs
     * @return
     */
    public static String getString(Context context, int msgId, Object... formatArgs) {
        return context.getString(msgId, formatArgs);
    }

    /**
     * 获取颜色
     *
     * @param ctx
     * @param colorId
     * @return
     */
    public static int getColor(Context ctx, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ctx.getColor(colorId);
        }
        return ctx.getResources().getColor(colorId);
    }

    @ColorInt
    public static int getColorAttr(Context ctx, int attrId) {
        TypedValue typedValue = new TypedValue();
        ctx.getTheme().resolveAttribute(attrId, typedValue, true);
        switch (typedValue.type) {
            case TypedValue.TYPE_INT_COLOR_ARGB4:
            case TypedValue.TYPE_INT_COLOR_ARGB8:
            case TypedValue.TYPE_INT_COLOR_RGB4:
            case TypedValue.TYPE_INT_COLOR_RGB8:
                return typedValue.data;
        }
        return 0;
    }

    /**
     * 获取颜色
     *
     * @param ctx
     * @param colorId
     * @return
     */
    public static ColorStateList getColorStateList(Context ctx, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ctx.getColorStateList(colorId);
        }
        return ctx.getResources().getColorStateList(colorId);
    }

    /**
     * 获取主题中的布尔值
     *
     * @param ctx    .
     * @param attrId .
     * @return .
     */
    public static boolean getBoolAttr(Context ctx, int attrId) {
        TypedValue typedValue = new TypedValue();
        if (ctx.getTheme().resolveAttribute(attrId, typedValue, true)) {
            if (typedValue.type == TypedValue.TYPE_INT_BOOLEAN) {
                return typedValue.data != 0;
            }
            if (typedValue.type == TypedValue.TYPE_REFERENCE) {
                try {
                    return ctx.getResources().getBoolean(typedValue.resourceId);
                } catch (Resources.NotFoundException e) {
                    Log.e("ResourceUtils", e.getMessage(), e);
                }
            }
        }
        return false;
    }

    /**
     * 获取字体
     *
     * @param ctx .
     * @param fontId .
     * @return .
     */
    public static Typeface getFont(Context ctx, int fontId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return ctx.getResources().getFont(fontId);
        }
        return ResourcesCompat.getFont(ctx, fontId);
    }
}

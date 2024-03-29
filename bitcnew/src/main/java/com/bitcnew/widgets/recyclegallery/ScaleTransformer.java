package com.bitcnew.widgets.recyclegallery;

import android.view.View;

/**
 * Created by chensuilun on 2016/12/16.
 */
public class ScaleTransformer implements GalleryLayoutManager.ItemTransformer {

    private static final String TAG = "CurveTransformer";


    @Override
    public void transformItem(GalleryLayoutManager layoutManager, View item, float fraction) {
        item.setPivotX(item.getWidth() / 2.f);
        item.setPivotY(item.getHeight()/2.0f);
        float scale = 1 -0.1f* Math.abs(fraction);
        item.setScaleX(scale);
        item.setScaleY(scale);
    }
}

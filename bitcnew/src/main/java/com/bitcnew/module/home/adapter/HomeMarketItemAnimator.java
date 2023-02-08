package com.bitcnew.module.home.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
//import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.bitcnew.util.StockChartUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeMarketItemAnimator extends DefaultItemAnimator {

    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    Map<RecyclerView.ViewHolder, ValueAnimator> updateAnimationsMap = new HashMap<>();

    @Override
    void animateAddImpl(RecyclerView.ViewHolder holder) {
        super.animateAddImpl(holder);

        final double rate;
        if (holder instanceof HomeMarketAdapter.ViewHolder) {
            rate = ((HomeMarketAdapter.ViewHolder) holder).getRate();
        } else {
            rate = 0;
        }

        final int bgColor;
        Drawable bgDrawable = holder.itemView.getBackground();
        if (bgDrawable instanceof ColorDrawable) {
            bgColor = ((ColorDrawable) bgDrawable).getColor();
        } else {
            bgColor = 0;
        }

        final int bgColor1 = StockChartUtil.getRateAnimBgColor(rate);

        ValueAnimator anim = ValueAnimator.ofInt(StockChartUtil.getRateTextColor(rate), 0);

        anim.setDuration(getAddDuration());
        anim.setInterpolator(DECCELERATE_INTERPOLATOR);
        AnimatorListenerAdapter listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                holder.itemView.setBackgroundColor(bgColor1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                holder.itemView.setBackgroundColor(bgColor);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                holder.itemView.setBackgroundColor(bgColor);
                anim.removeListener(this);
            }
        };
        anim.addListener(listener);
        anim.setStartDelay(holder.getAdapterPosition() * 50);
        anim.start();
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags, @NonNull List<Object> payloads) {
        Log.e("Animator", "recordPreLayoutInformation");
        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
//                if (payload instanceof String) {
//                    return new FeedItemHolderInfo((String) payload);
//                }
                if (payload instanceof String[]) {
                    return new FeedItemHolderInfo(((String[])payload)[0], ((String[])payload)[1]);
                }
            }
        }

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder,
                                 @NonNull RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo,
                                 @NonNull ItemHolderInfo postInfo) {
        cancelCurrentAnimationIfExists(newHolder);

        if (preInfo instanceof FeedItemHolderInfo) {
            FeedItemHolderInfo feedItemHolderInfo = (FeedItemHolderInfo) preInfo;
            HomeMarketAdapter.ViewHolder holder = (HomeMarketAdapter.ViewHolder) newHolder;

            animateHeartButton(holder, feedItemHolderInfo.updateAction1, feedItemHolderInfo.updateAction2);
        }

        return false;
    }

    private void cancelCurrentAnimationIfExists(RecyclerView.ViewHolder item) {
        if (updateAnimationsMap.containsKey(item)) {
            updateAnimationsMap.get(item).cancel();
        }
    }

    private void animateHeartButton(final HomeMarketAdapter.ViewHolder holder, String rateString1, String rateString2) {
        if (null == rateString1 || rateString1.length() == 0) {
            return;
        }
        if (null == rateString2 || rateString2.length() == 0) {
            return;
        }

        double rate1 = Double.parseDouble(rateString1);
        double rate2 = Double.parseDouble(rateString2);
        if (rate1 == rate2) {
            return;
        }

        final int bgColor;
        Drawable bgDrawable = holder.itemView.getBackground();
        if (bgDrawable instanceof ColorDrawable) {
            bgColor = ((ColorDrawable) bgDrawable).getColor();
        } else {
            bgColor = 0;
        }

        final int bgColor1 = StockChartUtil.getRateAnimBgColor(rate2 - rate1);
        ValueAnimator anim = ValueAnimator.ofInt(StockChartUtil.getRateTextColor(rate2 - rate1), 0);

        anim.setDuration(300);
        anim.setInterpolator(DECCELERATE_INTERPOLATOR);
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                holder.itemView.setBackgroundColor(bgColor1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                holder.itemView.setBackgroundColor(bgColor);
            }
        });
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                holder.itemView.setBackgroundColor((int)animation.getAnimatedValue());
//            }
//        });
        anim.setStartDelay(holder.getAdapterPosition() * 50);
        anim.start();
        updateAnimationsMap.put(holder, anim);
        Log.e("Animator", "changed " + holder);
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        cancelCurrentAnimationIfExists(item);
    }

    @Override
    public void onAnimationStarted(@NonNull RecyclerView.ViewHolder viewHolder) {
        super.onAnimationStarted(viewHolder);
    }

    public static class FeedItemHolderInfo extends ItemHolderInfo {

        public String updateAction1;
        public String updateAction2;

        public FeedItemHolderInfo(String updateAction1, String updateAction2) {
            this.updateAction1 = updateAction1;
            this.updateAction2 = updateAction2;
        }

    }

}

package com.loomlogic.utils;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by alex on 3/20/17.
 */

public class AnimationUtils {
    public static void expand(final View v) {
        v.setVisibility(View.VISIBLE);
        v.post(new Runnable() {
            @Override
            public void run() {
                v.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

                int targetHeight = v.getMeasuredHeight();
                int duration = (int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density);

                animateViewVisibility(v, 0, targetHeight, duration);
            }
        });
    }

    public static void collapse(View v) {
        int prevHeight = v.getHeight();
        int duration = (int) (prevHeight / v.getContext().getResources().getDisplayMetrics().density);
        animateViewVisibility(v, prevHeight, 0, duration);
    }

    private static void animateViewVisibility(final View v, int prevHeight, int targetHeight, int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });

        valueAnimator.start();
    }
}


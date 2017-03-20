package com.loomlogic.utils;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by alex on 3/20/17.
 */

public class AnimationUtils {
    public static void expand(View v) {
        v.setVisibility(View.VISIBLE);
        int targetHeight = getViewFullHeight(v);
        int duration = (int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density);
        animateViewVisibility(v, 0, targetHeight, duration);
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

    private static int getViewFullHeight(View view) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);
        return view.getMeasuredHeight();
    }
}


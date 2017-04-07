package com.loomlogic.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;

import com.loomlogic.R;

/**
 * Created by alex on 2/15/17.
 */

public class ViewUtils {

    public static void showWhiteMessageInSnackBar(View view, String message) {
        try {
            SpannableString txt = new SpannableString(message);
            txt.setSpan(new ForegroundColorSpan(Color.WHITE), 0, txt.length(), 0);
            Snackbar.make(view, txt, Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {

        }
    }

    public static Snackbar displayNoInternetSnackBar(@NonNull final Activity activity, @NonNull View view) {
        SpannableString txt = new SpannableString(activity.getString(R.string.internet_error));
        txt.setSpan(new ForegroundColorSpan(Color.WHITE), 0, txt.length(), 0);
        Snackbar snackbar = Snackbar.make(view, txt, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.Connect, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS));
                    }
                });

        snackbar.show();
        try {
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
            layout.setPadding(0, 0, 0, 0);
        } catch (Exception e) {

        }
        return snackbar;
    }

    public static void hideSoftKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(View view) {
        InputMethodManager mgr = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    public static void changeRadioBtnTextColor(final RadioButton radio, int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                radio.setTextColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    public static int getColorFromGradient(int[] colors, float[] positions, float position) {

        if (colors.length == 0 || colors.length != positions.length) {
            throw new IllegalArgumentException();
        }

        if (colors.length == 1) {
            return colors[0];
        }

        if (position <= positions[0]) {
            return colors[0];
        }

        if (position >= positions[positions.length - 1]) {
            return colors[positions.length - 1];
        }

        for (int i = 1; i < positions.length; ++i) {
            if (position <= positions[i]) {
                float t = (position - positions[i - 1]) / (positions[i] - positions[i - 1]);
                return lerpColor(colors[i - 1], colors[i], t);
            }
        }
        return Color.WHITE;
    }

    public static int lerpColor(int colorA, int colorB, float t) {
        int alpha = (int) Math.floor(Color.alpha(colorA) * (1 - t) + Color.alpha(colorB) * t);
        int red = (int) Math.floor(Color.red(colorA) * (1 - t) + Color.red(colorB) * t);
        int green = (int) Math.floor(Color.green(colorA) * (1 - t) + Color.green(colorB) * t);
        int blue = (int) Math.floor(Color.blue(colorA) * (1 - t) + Color.blue(colorB) * t);

        return Color.argb(alpha, red, green, blue);
    }

    public static void animViewBgColor(final View view, int colorFrom, int colorTo, int time) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(time);
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
}

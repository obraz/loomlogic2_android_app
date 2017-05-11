package com.loomlogic.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.loomlogic.R;
import com.loomlogic.base.LoomLogicApp;
import com.loomlogic.network.RetryRequestCallback;

/**
 * Created by alex on 2/15/17.
 */

public class ViewUtils {

    public static void showSuccessSnackBar(View view, String message) {
        TSnackbar snackbar = TSnackbar.make(view, message, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.successMessageBgColor));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.successMessageTextColor));
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_alert_success, 0, 0, 0);
        textView.setCompoundDrawablePadding((int) view.getContext().getResources().getDimension(R.dimen.success_alert_drawable_padding));
        snackbar.show();
    }

    public static void showErrorSnackBar(View view, String message) {
        TSnackbar snackbar = TSnackbar.make(view, message, Snackbar.LENGTH_LONG);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.errorMessageBgColor));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        snackbar.show();
    }

    public static void showAlertSnackBar(View view, String message, final RetryRequestCallback callback) {
        SpannableString txt = new SpannableString(message);
        txt.setSpan(new ForegroundColorSpan(ContextCompat.getColor(view.getContext(), R.color.alertMessageTextColor)), 0, txt.length(), 0);
        final TSnackbar snackbar = TSnackbar.make(view, txt, Snackbar.LENGTH_INDEFINITE);

        if (callback != null) {
            snackbar.setAction(R.string.retry, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onRetry();
                }
            });
        }
        snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent));

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.alertMessageBgColor));
        snackbarView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                snackbar.dismiss();
                return false;
            }
        });
        snackbar.show();
    }

    public static TSnackbar displayNoInternetSnackBar(@NonNull final Activity activity, @NonNull View view) {
        SpannableString txt = new SpannableString(activity.getString(R.string.internet_error));
        txt.setSpan(new ForegroundColorSpan(ContextCompat.getColor(activity, R.color.alertMessageTextColor)), 0, txt.length(), 0);
        final TSnackbar snackbar = TSnackbar.make(view, txt, Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction(R.string.Connect, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.openInternetSettingsIntent(activity);
            }
        });

        snackbar.setActionTextColor(ContextCompat.getColor(activity, R.color.colorAccent));

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.alertMessageBgColor));
        snackbarView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                snackbar.dismiss();
                return false;
            }
        });
        snackbar.show();

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

    public static Runnable configTab(final TabLayout mTabLayout, final boolean shouldAnimate) {
        return new Runnable() {
            @Override
            public void run() {
                int widthDiff = Utils.getDisplayWidth(LoomLogicApp.getSharedContext()) - mTabLayout.getWidth();
                if (widthDiff > 0) {
                    int widthAdd = widthDiff / mTabLayout.getTabCount();
                    for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                        if (i == mTabLayout.getTabCount() - 1) {
                            widthAdd = widthAdd + widthDiff % mTabLayout.getTabCount();
                        }
                        View tabView = mTabLayout.getTabAt(i).getCustomView();
                        tabView.setLayoutParams(new LinearLayout.LayoutParams(tabView.getWidth() + widthAdd, LinearLayout.LayoutParams.WRAP_CONTENT));
                    }

                } else if (shouldAnimate) {
                    // todo do it only once
                    mTabLayout.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mTabLayout.fullScroll(HorizontalScrollView.FOCUS_LEFT);
                        }
                    }, 800);

                }
            }
        };
    }

    public static void hideSearchPlate(SearchView searchView) {
        try {
            View searchPlate = (View) searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            searchPlate.getBackground().setColorFilter(ContextCompat.getColor(LoomLogicApp.getSharedContext(), R.color.transparent), PorterDuff.Mode.MULTIPLY);
        } catch (Exception e) {

        }
    }
}

package com.loomlogic.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

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

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int getDisplayWidth(Activity activity) {
        return getDisplayMetrics(activity).widthPixels;
    }
}

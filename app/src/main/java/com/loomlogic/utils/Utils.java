package com.loomlogic.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.WindowManager;

/**
 * Created by alex on 3/10/17.
 */

public class Utils {
    public static boolean isUrlValid(String url) {
        return url != null && Patterns.WEB_URL.matcher(url).matches();
    }

    private static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int getDisplayWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

}

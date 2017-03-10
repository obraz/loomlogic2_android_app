package com.loomlogic.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.Patterns;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by alex on 3/10/17.
 */

public class Utils {
    public static boolean isUrlValid(String url) {
        return url != null && Patterns.WEB_URL.matcher(url).matches();
    }

    public static int getDisplayWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}

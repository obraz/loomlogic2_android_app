package com.loomlogic.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.WindowManager;

import com.loomlogic.base.LoomLogicApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by alex on 3/10/17.
 */

public class Utils {
    public static boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isUrlValid(String url) {
        return url != null && Patterns.WEB_URL.matcher(url).matches();
    }

    public static boolean isPhoneValid(String phone) {
        return phone != null && Patterns.PHONE.matcher(phone).matches();
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

    public static String readFromAssets(Context context, final String fileName) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = null;
            json = context.getAssets().open(fileName);

            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();
            return buf.toString();
        } catch (IOException e) {
            return null;
        }
    }

    public static void copyTextToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) LoomLogicApp.getSharedContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(text, text);
        clipboard.setPrimaryClip(clip);
    }

}

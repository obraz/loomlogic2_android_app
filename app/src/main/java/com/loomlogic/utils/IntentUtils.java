package com.loomlogic.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;

import com.loomlogic.R;
import com.loomlogic.base.BaseActivity;

/**
 * Created by alex on 4/20/17.
 */

public class IntentUtils {
    public static void openDialIntent(BaseActivity activity, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        if (Utils.isPhoneValid(phone)) {
            intent.setData(Uri.parse("tel:" + phone));
        }
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(intent);
        } else {
            activity.showErrorSnackBar(activity.getString(R.string.call_error));
        }
    }

    public static void openMapIntent(BaseActivity activity, String latitude, String longitude, String address) {
        String mapRequest = "geo:" + latitude + "," + longitude;
        if (address != null && !TextUtils.isEmpty(address)) {
            mapRequest = mapRequest + "?q=" + Uri.encode(address);
        }
        Uri gmmIntentUri = Uri.parse(mapRequest);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(mapIntent);
        } else {
            activity.showErrorSnackBar(activity.getString(R.string.map_error));
        }
    }

    public static void openInternetSettingsIntent(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "com.android.settings",
                    "com.android.settings.Settings$DataUsageSummaryActivity"));
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        }
    }
}

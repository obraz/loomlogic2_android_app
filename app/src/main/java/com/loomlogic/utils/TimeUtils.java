package com.loomlogic.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by alex on 5/11/17.
 */

public class TimeUtils {
    private static SimpleDateFormat dateRequestFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.getDefault());

    public static String getFormattedDateToRequest(Calendar cal) {
        //// TODO: 5/11/17  get real time zone from user profile
        dateRequestFormat.setTimeZone(TimeZone.getTimeZone("PST8PDT"));
        return dateRequestFormat.format(cal.getTime());
    }
}

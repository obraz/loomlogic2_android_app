package com.loomlogic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by alex on 5/11/17.
 */

public class TimeUtils {
    private static SimpleDateFormat dateRequestFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.getDefault());

    public static String getFormattedDateToRequest(Calendar cal) {
        //// TODO: 5/11/17  get real time zone from user profile
        SimpleDateFormat dateRequestFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.getDefault());
        dateRequestFormat.setTimeZone(TimeZone.getTimeZone("PST8PDT"));
        return dateRequestFormat.format(cal.getTime());
    }

    public static Date parseDate(String dateString) {
        try {
            return dateRequestFormat.parse(dateString);
        } catch (ParseException e) {
            return new Date();
        }
    }
}

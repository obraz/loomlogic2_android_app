package com.loomlogic.utils;

import android.content.SharedPreferences;

import com.loomlogic.leads.base.LeadType;

/**
 * Created by alex on 3/2/17.
 */
public class LeadPreferencesUtils extends PreferencesUtils {
    private static final String KEY_CURRENT_LEAD_TYPE = "KEY_CURRENT_LEAD_TYPE";


    public static void setCurrentLeadType(LeadType type) {
        SharedPreferences.Editor ed = getSharedPreferences().edit();
        ed.putString(KEY_CURRENT_LEAD_TYPE, type.toString());
        ed.commit();
    }

    public static LeadType getCurrentLeadType() {
        String type = getSharedPreferences().getString(KEY_CURRENT_LEAD_TYPE, LeadType.BUYER.toString());
        return LeadType.toType(type);
    }
}

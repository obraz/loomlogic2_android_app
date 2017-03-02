package com.loomlogic.utils;

import android.content.SharedPreferences;

import com.loomlogic.leads.LeadRole;

/**
 * Created by alex on 3/2/17.
 */
public class LeadPreferencesUtils extends PreferencesUtils {
    private static final String KEY_CURRENT_LEAD_ROLE = "KEY_CURRENT_LEAD_ROLE";


    public static void setCurrentLeadRole(LeadRole role) {
        SharedPreferences.Editor ed = getSharedPreferences().edit();
        ed.putString(KEY_CURRENT_LEAD_ROLE, role.toString());
        ed.commit();
    }

    public static LeadRole getCurrentLeadRole() {
        String role = getSharedPreferences().getString(KEY_CURRENT_LEAD_ROLE, LeadRole.BUYER.toString());
        return LeadRole.toRole(role);
    }
}

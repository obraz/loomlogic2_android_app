package com.loomlogic.utils;

import android.support.v4.content.ContextCompat;

import com.loomlogic.R;
import com.loomlogic.base.LoomLogicApp;

/**
 * Created by alex on 3/2/17.
 */

public class LeadUtils {
    public static int getCurrentLeadRoleColor() {
        switch (LeadPreferencesUtils.getCurrentLeadRole()) {
            case BUYER:
                return ContextCompat.getColor(LoomLogicApp.getSharedContext(), R.color.colorMainBuyer);
            case SELLER:
                return ContextCompat.getColor(LoomLogicApp.getSharedContext(), R.color.colorMainSeller);
            default:
                return ContextCompat.getColor(LoomLogicApp.getSharedContext(), R.color.colorMainBuyer);
        }
    }
}

package com.loomlogic.utils;

import android.support.annotation.ColorRes;

import com.loomlogic.R;

/**
 * Created by alex on 3/2/17.
 */

public class LeadUtils {

    @ColorRes
    public static int getCurrentLeadRoleColor() {
        switch (LeadPreferencesUtils.getCurrentLeadType()) {
            case BUYER:
                return R.color.colorMainBuyer;
            case SELLER:
                return R.color.colorMainSeller;
            default:
                return R.color.colorMainBuyer;
        }
    }
}

package com.loomlogic.leads.menu;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

/**
 * Created by alex on 4/6/17.
 */

public class LeadFilterItem {
    @DrawableRes
    private int iconRes;
    @StringRes
    private int nameRes;

    public LeadFilterItem(int iconRes, int nameRes) {
        this.iconRes = iconRes;
        this.nameRes = nameRes;
    }

    public int getIconRes() {
        return iconRes;
    }

    public int getNameRes() {
        return nameRes;
    }
}

package com.loomlogic.leads.menu;

import android.graphics.drawable.Drawable;

/**
 * Created by alex on 3/1/17.
 */

public class LeadMenuItem {
    private LeadTypes type;
    private int count;
    private LeadRole role;

    public LeadMenuItem(LeadTypes type, int count, LeadRole role) {
        this.type = type;
        this.count = count;
        this.role = role;
    }

    public LeadTypes getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return LeadMenuUtils.getLeadTypeName(type);
    }

    public Drawable getDrawableIcon() {
        return LeadMenuUtils.getLeadTypeIcon(type, role);
    }
}

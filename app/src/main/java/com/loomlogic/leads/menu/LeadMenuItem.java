package com.loomlogic.leads.menu;

/**
 * Created by alex on 3/1/17.
 */

public class LeadMenuItem {
    private LeadTypes type;
    private int count;

    public LeadMenuItem(LeadTypes type, int count) {
        this.type = type;
        this.count = count;
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
}
package com.loomlogic.leads.menu;

import android.graphics.drawable.Drawable;

import com.loomlogic.leads.base.LeadData;
import com.loomlogic.leads.base.LeadStatus;
import com.loomlogic.leads.base.LeadType;
import com.loomlogic.leads.base.LeadUtils;

/**
 * Created by alex on 3/1/17.
 */

public class LeadMenuItem {
    private LeadData data;
    private LeadStatus status;
    private int count;
    private LeadType type;

    public LeadMenuItem(LeadData data, int count) {
        this.data = data;
        this.type = data.getType();
        this.status = data.getStatus();
        this.count = count;
    }

    public LeadData getLead() {
        return data;
    }

    public LeadStatus getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return LeadUtils.getLeadStatusName(status);
    }

    public Drawable getDrawableIcon() {
        return LeadUtils.getLeadStatusIcon(status, type);
    }
}

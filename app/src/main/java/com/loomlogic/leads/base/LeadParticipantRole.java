package com.loomlogic.leads.base;

import android.support.annotation.StringRes;

import com.loomlogic.R;

/**
 * Created by alex on 3/17/17.
 */

public enum LeadParticipantRole {
    AGENT, OWNER, LENDER, CLIENT;

    @StringRes
    public static int toString(LeadParticipantRole role) {
        switch (role) {
            case AGENT:
                return R.string.lead_participant_role_agent;
            case OWNER:
                return R.string.lead_participant_role_owner;
            case LENDER:
                return R.string.lead_participant_role_lender;
            case CLIENT:
                return R.string.lead_participant_role_client;
            default:
                return R.string.lead_participant_role_client;
        }
    }
}

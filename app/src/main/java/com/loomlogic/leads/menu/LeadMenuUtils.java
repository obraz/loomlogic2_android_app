package com.loomlogic.leads.menu;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.loomlogic.R;
import com.loomlogic.base.LoomLogicApp;

/**
 * Created by alex on 3/1/17.
 */

public class LeadMenuUtils {

    public static String getLeadTypeName(LeadTypes type) {
        switch (type) {
            case LEADS:
                return LoomLogicApp.getSharedContext().getString(R.string.leadMenuItem_Leads);
            case LENDER:
                return LoomLogicApp.getSharedContext().getString(R.string.leadMenuItem_Lender);
            case SHOPPING:
                return LoomLogicApp.getSharedContext().getString(R.string.leadMenuItem_Shopping);
            case CONTRACT:
                return LoomLogicApp.getSharedContext().getString(R.string.leadMenuItem_Contract);
            case CLOSED:
                return LoomLogicApp.getSharedContext().getString(R.string.leadMenuItem_Closed);
            case COMING_SOON:
                return LoomLogicApp.getSharedContext().getString(R.string.leadMenuItem_ComingSoon);
            case ACTIVE:
                return LoomLogicApp.getSharedContext().getString(R.string.leadMenuItem_Active);
            case WITHDRAW:
                return LoomLogicApp.getSharedContext().getString(R.string.leadMenuItem_Withdraw);
            case PENDING:
                return LoomLogicApp.getSharedContext().getString(R.string.leadMenuItem_Pending);
            default:
                return "Unspecified";
        }
    }

    public static Drawable getLeadTypeIcon(LeadTypes type) {
        switch (type) {
            case LEADS:
                return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.ic_lead_new);
            case LENDER:
                return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.ic_lead_lender);
            case SHOPPING:
                return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.ic_lead_shopping);
            case CONTRACT:
                return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.ic_lead_contract);
            case CLOSED:
                return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.ic_lead_closed);
            case COMING_SOON:
                return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.ic_lead_comingsoon);
            case ACTIVE:
                return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.ic_lead_active);
            case WITHDRAW:
                return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.ic_lead_withdrawn);
            case PENDING:
                return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.ic_lead_pending);
            default:
                return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.ic_lead_new);
        }
    }
}

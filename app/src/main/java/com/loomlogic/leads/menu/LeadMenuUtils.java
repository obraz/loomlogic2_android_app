package com.loomlogic.leads.menu;

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
}

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

    public static Drawable getLeadTypeIcon(LeadTypes type, LeadRole role) {
        int drawableRes;
        switch (type) {
            case LEADS:
                if (role == LeadRole.SELLER){
                    drawableRes = R.drawable.ic_lead_new_seller;
                    break;
                }
                drawableRes = R.drawable.ic_lead_new_buyer;
                break;
            case LENDER:
                drawableRes = R.drawable.ic_lead_lender;
                break;
            case SHOPPING:
                drawableRes = R.drawable.ic_lead_shopping;
                break;
            case CONTRACT:
                drawableRes = R.drawable.ic_lead_contract;
                break;
            case CLOSED:
                if (role == LeadRole.SELLER){
                    drawableRes = R.drawable.ic_lead_closed_seller;
                    break;
                }
                drawableRes = R.drawable.ic_lead_closed_buyer;
                break;
            case COMING_SOON:
                drawableRes = R.drawable.ic_lead_comingsoon;
                break;
            case ACTIVE:
                drawableRes = R.drawable.ic_lead_active;
                break;
            case WITHDRAW:
                drawableRes = R.drawable.ic_lead_withdrawn;
                break;
            case PENDING:
                drawableRes = R.drawable.ic_lead_pending;
                break;
            default:
                drawableRes = R.drawable.ic_lead_new_buyer;
        }
        return ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), drawableRes);
    }
}

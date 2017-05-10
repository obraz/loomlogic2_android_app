package com.loomlogic.leads.base;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;

import com.loomlogic.R;
import com.loomlogic.base.LoomLogicApp;
import com.loomlogic.utils.LeadPreferencesUtils;

/**
 * Created by alex on 3/1/17.
 */

public class LeadUtils {

    public static String getLeadStatusName(LeadStatus status) {
        switch (status) {
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

    public static String getLeadStatusTitle(LeadStatus status) {
        switch (status) {
            case LEADS:
                return LoomLogicApp.getSharedContext().getString(R.string.lead_leads_title);
            case LENDER:
                return LoomLogicApp.getSharedContext().getString(R.string.lead_leads_lender_title);
            case SHOPPING:
                return LoomLogicApp.getSharedContext().getString(R.string.lead_leads_shopping_title);
            case CONTRACT:
                return LoomLogicApp.getSharedContext().getString(R.string.lead_leads_contract_title);
            case CLOSED:
                return LoomLogicApp.getSharedContext().getString(R.string.lead_leads_closed_title);
            default:
                return "Unspecified";
        }
    }

    public static String getLeadSubStageTitle(LeadSubStage stage) {
        switch (stage) {
            case NEW:
                return LoomLogicApp.getSharedContext().getString(R.string.lead_tab_name_new);
            case ENGAGED:
                return LoomLogicApp.getSharedContext().getString(R.string.lead_tab_name_engaged);
            case FUTURE:
                return LoomLogicApp.getSharedContext().getString(R.string.lead_tab_name_future);
            case APPLICATION:
                return LoomLogicApp.getSharedContext().getString(R.string.lead_tab_name_application);
            case APPOINTMENT:
                return LoomLogicApp.getSharedContext().getString(R.string.lead_tab_name_appointment);
            default:
                return "Unspecified";
        }
    }

    public static Drawable getLeadStatusIcon(LeadStatus status, LeadType type) {
        int drawableRes;
        switch (status) {
            case LEADS:
                if (type == LeadType.SELLER) {
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
                if (type == LeadType.SELLER) {
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

    public static int getLeadSubStagesCount(LeadData leadData) {
        switch (leadData.getStatus()) {
            case LEADS:
                return 4;
            case LENDER:
                return 5;
            default:
                return 1;
        }
    }

    @ColorRes
    public static int getCurrentLeadTypeColor() {
        switch (LeadPreferencesUtils.getCurrentLeadType()) {
            case BUYER:
                return R.color.colorMainBuyer;
            case SELLER:
                return R.color.colorMainSeller;
            default:
                return R.color.colorMainBuyer;
        }
    }


    public static Drawable getLeadQuality(String quality) {
        int color = R.color.transparent;
        if (quality.equals("A")) {
            color = R.color.lead_quality_bg_color_A;
        } else if (quality.equals("B")) {
            color = R.color.lead_quality_bg_color_B;
        } else if (quality.equals("C")) {
            color = R.color.lead_quality_bg_color_C;
        }
        Drawable circleDrawable = ContextCompat.getDrawable(LoomLogicApp.getSharedContext(), R.drawable.circle);
        Drawable bg = circleDrawable.getConstantState().newDrawable();
        bg.setColorFilter(ContextCompat.getColor(LoomLogicApp.getSharedContext(), color), PorterDuff.Mode.SRC_ATOP);
        return bg;
    }
}

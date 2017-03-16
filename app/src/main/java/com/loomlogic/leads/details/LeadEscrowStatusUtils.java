package com.loomlogic.leads.details;

import com.loomlogic.R;
import com.loomlogic.base.LoomLogicApp;
import com.loomlogic.leads.base.LeadItem;

import java.util.ArrayList;

/**
 * Created by alex on 3/16/17.
 */

public class LeadEscrowStatusUtils {

    public static ArrayList<LeadEscrowStatusItem> getEscrowStatusItemList(LeadItem lead) {
        ArrayList<LeadEscrowStatusItem> list = new ArrayList<>();

        String[] abbreviationArray = null;
        if (lead.isFinancing) {
            abbreviationArray = LoomLogicApp.getSharedContext().getResources().getStringArray(R.array.lead_details_escrow_statuses_financing_list_abbreviation);
        } else {
            abbreviationArray = LoomLogicApp.getSharedContext().getResources().getStringArray(R.array.lead_details_escrow_statuses_cash_list_abbreviation);
        }

        for (int i = 0; i < abbreviationArray.length; i++) {
            LeadEscrowStatusState state = LeadEscrowStatusState.FUTURE;
            if (lead.escrowStatusDoneCount > i) {
                state = LeadEscrowStatusState.DONE;
            } else if (lead.escrowStatusDoneCount == i) {
                state = LeadEscrowStatusState.CURRENT;
            }
            LeadEscrowStatusItem item = new LeadEscrowStatusItem(state, abbreviationArray[i]);
            list.add(item);
        }
        return list;
    }

    public static int getMaxEscrowStatusCount(boolean isFinancing) {
        return isFinancing ? 7 : 2;
    }
}

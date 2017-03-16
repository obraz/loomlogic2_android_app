package com.loomlogic.leads.details;

/**
 * Created by alex on 3/16/17.
 */

public class LeadEscrowStatusItem {
    private LeadEscrowStatusState state;
    private String abbreviation;

    public LeadEscrowStatusItem(LeadEscrowStatusState state, String abbreviation) {
        this.state = state;
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public LeadEscrowStatusState getState() {
        return state;
    }
}

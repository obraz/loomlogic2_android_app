package com.loomlogic.leads.base;

/**
 * Created by alex on 4/21/17.
 */

public class LeadData {
    private LeadType type;
    private LeadOwner owner;
    private LeadStatus status;
    private LeadSubStage subStage;

    public LeadData(LeadType type, LeadOwner owner, LeadStatus status) {
        this.type = type;
        this.owner = owner;
        this.status = status;
    }

    public LeadType getType() {
        return type;
    }

    public LeadOwner getOwner() {
        return owner;
    }

    public LeadStatus getStatus() {
        return status;
    }

    public LeadSubStage getSubStage() {
        return subStage;
    }

    public void setSubStage(LeadSubStage subStage) {
        this.subStage = subStage;
    }
}

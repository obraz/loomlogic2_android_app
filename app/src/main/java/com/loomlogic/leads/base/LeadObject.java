package com.loomlogic.leads.base;

/**
 * Created by alex on 4/21/17.
 */

abstract class LeadObject {
    public abstract LeadType getLeadType();

    abstract LeadOwner owner();
}

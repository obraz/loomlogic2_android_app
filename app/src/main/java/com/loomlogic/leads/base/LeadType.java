package com.loomlogic.leads.base;

/**
 * Created by alex on 3/17/17.
 */

public enum LeadType {
    BUYER, SELLER;

    public static LeadType toType(String type) {
        try {
            return valueOf(type);
        } catch (Exception ex) {
            return BUYER;
        }
    }
}

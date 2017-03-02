package com.loomlogic.leads;

/**
 * Created by alex on 3/2/17.
 */

public enum LeadRole {
    BUYER, SELLER;

    public static LeadRole toRole(String role) {
        try {
            return valueOf(role);
        } catch (Exception ex) {
            return BUYER;
        }
    }
}

package com.loomlogic.leads.entity;

/**
 * Created by alex on 3/20/17.
 */

public class LeadContractItem {
    public String purchase;
    public String commissions;
    public String gross;
    public String address;
    public String houseImageUrl;

    public LeadContractItem(String purchase, String commissions, String gross, String address, String houseImageUrl) {
        this.purchase = purchase;
        this.commissions = commissions;
        this.gross = gross;
        this.address = address;
        this.houseImageUrl = houseImageUrl;
    }
}

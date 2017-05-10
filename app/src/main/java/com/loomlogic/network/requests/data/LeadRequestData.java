package com.loomlogic.network.requests.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 5/5/17.
 */

public class LeadRequestData {
    public enum SendToAction {
        CRM("to_crm"), LENDER("to_lender"), AGENT("to_agent"), TEAM("to_team");

        private String value;

        SendToAction(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public enum LeadType {
        BUYER("buyer"), SELLER("seller");

        private String value;

        LeadType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    @SerializedName("name")
    private String name;

    @SerializedName("additional_name")
    private String additionalName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("email")
    private String email;

    @SerializedName("source_id")
    private int sourceId;

    @SerializedName("lead_type")
    private String leadType;

    @SerializedName("note_text")
    private String note;

    @SerializedName("drip_compaigns")
    private boolean dripCompaigns;

    @SerializedName("lender_id")
    private int lenderId;

    @SerializedName("agent_id")
    private int agentId;

    @SerializedName("address")
    private String address;

    @SerializedName("unit")
    private String unit;

    @SerializedName("city")
    private String city;

    @SerializedName("state")
    private String state;

    @SerializedName("zip_code")
    private String zipCode;

    @SerializedName("send_to")
    private String sendTo;

    public void setLeadData(String name, String additionalName, String phone, String email, int sourceId, String note, boolean dripCompaigns) {
        this.name = name;
        this.additionalName = additionalName;
        this.phone = phone;
        this.email = email;
        this.sourceId = sourceId;
        this.note = note;
        this.dripCompaigns = dripCompaigns;
    }

    public void setSendToAction(LeadRequestData.SendToAction sendTo) {
        this.sendTo = sendTo.toString();
    }

    public void setLeadType(LeadType leadType) {
        this.leadType = leadType.toString();
    }

    public void setLenderId(int lenderId) {
        this.lenderId = lenderId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public void setAddress(String address, String unit, String city, String state, String zipCode) {
        this.address = address;
        this.unit = unit;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public void clearAddress() {
        this.address = "";
        this.unit = "";
        this.city = "";
        this.state = "";
        this.zipCode = "";
    }

    public boolean isSeller(){
        return leadType.equals(LeadType.SELLER.toString());
    }

/* parameter name: :name,            in: :formData, required: false, type: :string
        parameter name: :additional_name, in: :formData, required: false, type: :string
        parameter name: :phone,           in: :formData, required: false, type: :string
        parameter name: :email,           in: :formData, required: false, type: :string
        parameter name: :source_id,       in: :formData, required: true,  type: :integer
        parameter name: :lead_type,       in: :formData, required: false, type: :string
        parameter name: :note_text,       in: :formData, required: false, type: :string
        parameter name: :drip_compaigns,  in: :formData, required: true,  type: :boolean
        parameter name: :lender_id,       in: :formData, required: false, type: :integer
        parameter name: :agent_id,        in: :formData, required: false, type: :integer
        parameter name: :address,         in: :formData, required: false, type: :string
        parameter name: :unit,            in: :formData, required: false, type: :string
        parameter name: :city,            in: :formData, required: false, type: :string
        parameter name: :state,           in: :formData, required: false, type: :string
        parameter name: :zip_code,        in: :formData, required: false, type: :string
        parameter name: :send_to,         in: :formData, required: false, type: :string

        send_to: [‘crm’, ‘lender’, ‘agent’, ‘team’]
        lead_type: [‘buyer’, ‘seller’]

   */
}

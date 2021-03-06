package com.loomlogic.network.requests.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.loomlogic.utils.TimeUtils;

import java.util.Calendar;

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
    private Integer sourceId;

    @SerializedName("lead_type")
    private String leadType;

    @SerializedName("note_text")
    private String note;

    @SerializedName("drip_compaigns")
    private boolean dripCompaigns;

    @SerializedName("lender_id")
    private Integer lenderId;

    @SerializedName("agent_id")
    private Integer agentId;

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

    @SerializedName("need_claim")
    private boolean needClaimAgent;

    @SerializedName("sent_to_agent_at_access")
    private String sentToAgentDate;

    @SerializedName("need_claim_lenders")
    private boolean needClaimLenders;

    @SerializedName("sent_to_lender_at_access")
    private String sentToLenderDate;

    @Expose
    private SendToAction sendToAction;

    @Expose
    private String sendToAgentName;

    @Expose
    private String sendToLenderName;

    @Expose
    private Calendar sendToAgentDateCal;

    @Expose
    private Calendar sendToLenderDateCal;

    public void setLeadData(String name, String additionalName, String phone, String email, Integer sourceId, String note, boolean dripCompaigns) {
        this.name = name;
        this.additionalName = additionalName;
        this.phone = phone;
        this.email = email;
        this.sourceId = sourceId;
        this.note = note;
        this.dripCompaigns = dripCompaigns;
    }

    public void setSendToAction(LeadRequestData.SendToAction sendTo) {
        this.sendToAction = sendTo;
        this.sendTo = sendTo.toString();
    }

    public void setLeadType(LeadType leadType) {
        this.leadType = leadType.toString();
    }

    public void setAddress(String address, String unit, String city, String state, String zipCode) {
        this.address = address;
        this.unit = unit;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public void setSendToAgentData(String sendToAgentName, int agentId, boolean needClaimAgent, Calendar sendToAgentDateCal) {
        this.sendToAgentName = sendToAgentName;
        this.agentId = agentId;
        this.needClaimAgent = needClaimAgent;
        this.sendToAgentDateCal = sendToAgentDateCal;
        if (sendToAgentDateCal != null) {
            this.sentToAgentDate = TimeUtils.getFormattedDateToRequest(sendToAgentDateCal);
        }
    }

    public void setSendToLenderData(String sendToLenderName, int lenderId, boolean needClaimLender, Calendar sendToLenderDateCal) {
        this.sendToLenderName = sendToLenderName;
        this.lenderId = lenderId;
        this.needClaimLenders = needClaimLender;
        this.sendToLenderDateCal = sendToLenderDateCal;
        if (sendToLenderDateCal != null) {
            this.sentToLenderDate = TimeUtils.getFormattedDateToRequest(sendToLenderDateCal);
        }
    }

    public void clearAddress() {
        this.address = "";
        this.unit = "";
        this.city = "";
        this.state = "";
        this.zipCode = "";
    }

    public void clearAgentData() {
        this.agentId = null;
        this.needClaimAgent = false;
        this.sendToAgentName = "";
        this.sendToAgentDateCal = null;
        this.sentToAgentDate = null;
    }

    public void clearLenderData() {
        this.lenderId = null;
        this.needClaimLenders = false;
        this.sendToLenderName = "";
        this.sendToLenderDateCal = null;
        this.sentToLenderDate = null;
    }

    public boolean isSeller() {
        return leadType.equals(LeadType.SELLER.toString());
    }

    public SendToAction getSendToAction() {
        return sendToAction;
    }

    public boolean isNeedClaimAgent() {
        return needClaimAgent;
    }

    public boolean isNeedClaimLenders() {
        return needClaimLenders;
    }

    public String getSendToAgentName() {
        return sendToAgentName;
    }

    public String getSendToLenderName() {
        return sendToLenderName;
    }

    public Calendar getSendToAgentDateCal() {
        return sendToAgentDateCal;
    }

    public Calendar getSendToLenderDateCal() {
        return sendToLenderDateCal;
    }

    /*

        send_to: [‘crm’, ‘lender’, ‘agent’, ‘team’]
        lead_type: [‘buyer’, ‘seller’]

parameter name: :name,               in: :formData, required: false, type: :string
parameter name: :additional_name,    in: :formData, required: false, type: :string
parameter name: :phone,              in: :formData, required: false, type: :string
parameter name: :email,              in: :formData, required: false, type: :string
parameter name: :source_id,          in: :formData, required: true,  type: :integer
parameter name: :lead_type,          in: :formData, required: false, type: :string
parameter name: :note_text,          in: :formData, required: false, type: :string
parameter name: :drip_compaigns,     in: :formData, required: true,  type: :boolean
parameter name: :lender_id,          in: :formData, required: false, type: :integer
parameter name: :agent_id,           in: :formData, required: false, type: :integer
parameter name: :address,            in: :formData, required: false, type: :string
parameter name: :unit,               in: :formData, required: false, type: :string
parameter name: :city,               in: :formData, required: false, type: :string
parameter name: :state,              in: :formData, required: false, type: :string
parameter name: :zip_code,           in: :formData, required: false, type: :string
parameter name: :send_to,            in: :formData, required: false, type: :string
parameter name: :need_claim,         in: :formData, required: false, type: :boolean
parameter name: :need_claim_lenders, in: :formData, required: false, type: :boolean
parameter name: :sent_to_agent_at_access, in: :formData, required: false, type: :string
sent_to_lender_at_access
   */
}

package com.loomlogic.leads.base;

/**
 * Created by alex on 3/17/17.
 */

public class LeadParticipantItem {
    public String avatarUrl;
    public String firstName;
    public String lastName;
    public Gender gender;
    public String phone;
    public String email;
    public String address;
    public LeadParticipantRole role;

    public LeadParticipantItem(String avatarUrl, String firstName, String lastName, Gender gender, String phone, String email, String address, LeadParticipantRole role) {
        this.avatarUrl = avatarUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.role = role;
    }

    public String getFullFormattedName() {
        return String.format("%s %s", firstName, lastName);
    }
}

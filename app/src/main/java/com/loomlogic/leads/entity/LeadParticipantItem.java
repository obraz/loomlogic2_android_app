package com.loomlogic.leads.entity;

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
    public String company;
    public LeadParticipantRole role;

    public LeadParticipantItem(String avatarUrl, String firstName, String lastName, Gender gender, String phone, String email, String company, LeadParticipantRole role) {
        this.avatarUrl = avatarUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.company = company;
        this.role = role;
    }

    public String getFullFormattedName() {
        return String.format("%s %s", firstName, lastName);
    }
}

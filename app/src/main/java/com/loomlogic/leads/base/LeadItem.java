package com.loomlogic.leads.base;

import com.loomlogic.base.SwipeData;

/**
 * Created by alex on 3/10/17.
 */

public class LeadItem extends SwipeData {
    public int id;
    public int unreadNotificationCount;
    public int deadlineDate;
    public String avatarUrl;
    public String firstName;
    public String lastName;
    public Gender gender;
    public String address;
    public boolean isFinancing;
    public int escrowStatusDoneCount;

    ///
    public String phone;
    public String email;
    public String source;

    public enum Gender {MALE, FEMALE, NONE}

    public LeadItem(int id, int unreadNotificationCount, int deadlineDate, String avatarUrl, String firstName, String lastName, Gender gender, String address, boolean isFinancing, int escrowStatusDoneCount) {
        this.id = id;
        this.unreadNotificationCount = unreadNotificationCount;
        this.deadlineDate = deadlineDate;
        this.avatarUrl = avatarUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.address = address;
        this.isFinancing = isFinancing;
        this.escrowStatusDoneCount = escrowStatusDoneCount;

        this.phone = "+5 (752) 915 25 20";
        this.email = "consectetud@gmail.com";
        this.source = "Zillow";
    }
}

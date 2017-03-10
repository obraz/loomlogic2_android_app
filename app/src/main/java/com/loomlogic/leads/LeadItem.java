package com.loomlogic.leads;

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
    public String status;
    public boolean isFinancing;
    public int statusCount;

    public enum Gender {MALE, FEMALE, NONE}

    public LeadItem(int id, int unreadNotificationCount, int deadlineDate, String avatarUrl, String firstName, String lastName, Gender gender, String status, boolean isFinancing, int statusCount) {
        this.id = id;
        this.unreadNotificationCount = unreadNotificationCount;
        this.deadlineDate = deadlineDate;
        this.avatarUrl = avatarUrl;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.status = status;
        this.isFinancing = isFinancing;
        this.statusCount = statusCount;
    }
}

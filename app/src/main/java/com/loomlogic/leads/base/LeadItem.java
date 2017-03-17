package com.loomlogic.leads.base;

import com.loomlogic.base.SwipeData;

import java.util.ArrayList;

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
    public ArrayList<LeadParticipantItem> participantList;

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

        //avatarUrl, String firstName, String lastName, Gender gender, String phone, String email, String address, LeadParticipantRole role)
        participantList = new ArrayList<LeadParticipantItem>();
        participantList.add(new LeadParticipantItem("http://loomlogic.ucoz.net/3.jpg", "Carl", "Snyder", Gender.MALE, "+5 (752) 915 25 20", "consectetud0@gmail.com", "Cherry Creek Mortgage0", LeadParticipantRole.LENDER));
        participantList.add(new LeadParticipantItem("http://loomlogic.ucoz.net/1.jpg", "Benjamin", "Carlson", Gender.MALE, "+5 (752) 915 25 21", "consectetud1@gmail.com", "Cherry Creek Mortgage1", LeadParticipantRole.AGENT));
        participantList.add(new LeadParticipantItem("", "William", "Miller", Gender.MALE, "+5 (752) 915 25 22", "consectetud2@gmail.com", "Cherry Creek Mortgage2", LeadParticipantRole.CLIENT));
        participantList.add(new LeadParticipantItem("http://loomlogic.ucoz.net/4.jpg", "Justin", "Keller", Gender.MALE, "+5 (752) 915 25 23", "consectetud3@gmail.com", "Cherry Creek Mortgage3", LeadParticipantRole.OWNER));

    }
}

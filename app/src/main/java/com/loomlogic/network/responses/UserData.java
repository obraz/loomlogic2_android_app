package com.loomlogic.network.responses;

import com.google.gson.annotations.SerializedName;


public class UserData extends TokenData{

    @SerializedName("id")
    private int userId;

    @SerializedName("subscription_expires_at")
    private String subscriptionExpiration;

    public int getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId='" + userId + '\'' +
                "} " + super.toString();
    }
}

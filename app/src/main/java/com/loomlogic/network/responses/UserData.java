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
// {"success":true,"alert":"",
// "data":{"user":{"id":1,"roles":["ROLE_AGENT","ROLE_ADMIN","ROLE_FREE"],"subscription_expires_at":"2017-05-05T23:59:59-07:00"}}}

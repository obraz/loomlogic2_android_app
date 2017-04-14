package com.loomlogic.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 29.09.2016.
 */
public class UserData extends TokenData {

    @SerializedName("id")
    private String userId;

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId='" + userId + '\'' +
                "} " + super.toString();
    }
}

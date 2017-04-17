package com.loomlogic.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 29.09.2016.
 */
public class UserData extends TokenData {

    @SerializedName("id")
    private int userId;

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

package com.loomlogic.network.responses;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordData {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}

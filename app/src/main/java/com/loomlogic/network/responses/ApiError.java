package com.loomlogic.network.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by alex on 16.12.2016.
 */
public class ApiError {
    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }
}

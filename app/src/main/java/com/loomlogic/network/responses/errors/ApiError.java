package com.loomlogic.network.responses.errors;

import com.google.gson.annotations.SerializedName;

public class ApiError<T> {
    @SerializedName("alert")
    private String alert;

    @SerializedName("errors")
    private T errors;

    public String getAlert() {
        return alert;
    }

    public T getErrors() {
        return errors;
    }
}

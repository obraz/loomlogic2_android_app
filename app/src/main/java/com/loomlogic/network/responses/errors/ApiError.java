package com.loomlogic.network.responses.errors;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApiError {
    @SerializedName("alert")
    private List<String> alert;

    @SerializedName("errors")
    private List<String> errors;

    public List<String> getAlert() {
        return alert;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getHumanizedAlert() {
        String humanizedAlert = "";
        for (String txt : alert) {
            humanizedAlert = humanizedAlert + txt + "\n";
        }
        if (!TextUtils.isEmpty(humanizedAlert)) {
            humanizedAlert = humanizedAlert.substring(0, humanizedAlert.length() - 1);
        }
        return humanizedAlert;
    }
}

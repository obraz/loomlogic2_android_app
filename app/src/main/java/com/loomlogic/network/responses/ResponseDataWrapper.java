package com.loomlogic.network.responses;

import android.text.TextUtils;

import java.util.List;

public class ResponseDataWrapper<T> {
    public boolean success;
    public T data;
    private List<String> alert;
    private List<String> errors;

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return success;
    }

    public List<String> getAlert() {
        return alert;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getHumanizedAlert() {
        if (alert == null) {
            return "";
        }
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

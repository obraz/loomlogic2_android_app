package com.loomlogic.network.requests;

import com.google.gson.reflect.TypeToken;
import com.loomlogic.network.responses.ResetPasswordData;
import com.loomlogic.network.responses.ResponseDataWrapper;

import java.lang.reflect.Type;

public class ResetPasswordAuthRequestBuilder extends BaseLLRequestBuilder {
    public ResetPasswordAuthRequestBuilder(String token) {
        Type dataType = new TypeToken<ResponseDataWrapper<ResetPasswordData>>() {
        }.getType();
        this.setResponseClassSpecifier(dataType);
        setLocalPath("auth/password/edit?reset_password_token=" + token);
        setGetMethod();
    }
}
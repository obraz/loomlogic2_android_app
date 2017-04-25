package com.loomlogic.network.requests;

import com.google.gson.reflect.TypeToken;
import com.loomlogic.network.responses.ResetPasswordData;
import com.loomlogic.network.responses.ResponseDataWrapper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ResetPasswordRequestBuilder extends BaseLLRequestBuilder {

    public ResetPasswordRequestBuilder(String email) {
        Type dataType = new TypeToken<ResponseDataWrapper<ResetPasswordData>>() {
        }.getType();
        this.setResponseClassSpecifier(dataType);

        setLocalPath("auth/password");

        Map<String, String> objectToPost = new HashMap<>();
        objectToPost.put("email", email);
        this.setObjectToPost(objectToPost);
    }
}

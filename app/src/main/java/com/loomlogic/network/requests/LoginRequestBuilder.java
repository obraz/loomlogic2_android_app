package com.loomlogic.network.requests;

import com.google.gson.reflect.TypeToken;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.UserData;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LoginRequestBuilder extends BaseLLRequestBuilder {

    public LoginRequestBuilder(String email, String password) {
        Type dataType = new TypeToken<ResponseDataWrapper<UserData>>() {
        }.getType();
        this.setResponseClassSpecifier(dataType);

        setLocalPath("auth/sign_in");

        Map<String, String> objectToPost = new HashMap<>();
        objectToPost.put("email", email);
        objectToPost.put("password", password);
        this.setObjectToPost(objectToPost);
    }
}

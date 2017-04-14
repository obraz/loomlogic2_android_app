package com.loomlogic.network.requests;

import com.google.gson.reflect.TypeToken;
import com.loomlogic.network.ApplicationConfig;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.UserData;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class LoginRequestBuilder extends BaseStylesRequestBuilder {

    public LoginRequestBuilder() {
        Type dataType = new TypeToken<ResponseDataWrapper<UserData>>() {
        }.getType();
        this.setResponseClassSpecifier(dataType);

        this.setLocalPath("customer" + "/login");

        Map<String, String> objectToPost = new HashMap<>();
        objectToPost.put("password", "1762");
        objectToPost.put("phone", "+155");
        objectToPost.put("grant_type", "password");
        objectToPost.put("client_id", ApplicationConfig.CLIENT_ID);
        objectToPost.put("client_secret", ApplicationConfig.CLIENT_SECRET);

        this.setObjectToPost(objectToPost);
    }
}

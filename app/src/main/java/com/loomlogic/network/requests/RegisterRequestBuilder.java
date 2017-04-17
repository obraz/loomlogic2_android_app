package com.loomlogic.network.requests;

import com.google.gson.reflect.TypeToken;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.UserData;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class RegisterRequestBuilder extends BaseLLRequestBuilder {
    public RegisterRequestBuilder() {
        super();

        setLocalPath("auth");
        Type dataType = new TypeToken<ResponseDataWrapper<UserData>>() {
        }.getType();
        this.setResponseClassSpecifier(dataType);
        Map<String, String> objectToPost = new HashMap<>();
        objectToPost.put("email", new Random().nextInt() + "a@gmail.com");
        objectToPost.put("password", "123");
        objectToPost.put("phone", "18000000000");
        objectToPost.put("role", "3");
        objectToPost.put("confirm_success_url", "_");
        this.setObjectToPost(objectToPost);
//curl -X POST --header "Content-Type: application/x-www-form-urlencoded" --header "Accept: application/json" -d
// "email=alexandrobraz%40gmail.com&password=123&phone=1&role=3&confirm_success_url=_" "https://demo.loomlogic.com/api/v1/auth"

    }
}

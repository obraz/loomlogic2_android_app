package com.loomlogic.network.requests;

import com.google.gson.reflect.TypeToken;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.TokenData;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class TokenRequestBuilder extends BaseLLRequestBuilder {

    public TokenRequestBuilder(String refreshToken) {
        Type dataType = new TypeToken<ResponseDataWrapper<TokenData>>() {
        }.getType();
        this.setResponseClassSpecifier(dataType);

        this.setLocalPath("/token");

        Map<String, String> objectToPost = new HashMap<>();
        objectToPost.put("refresh_token", refreshToken);
        objectToPost.put("grant_type", "refresh_token");
        //    objectToPost.put("client_id", ApplicationConfig.CLIENT_ID);
        //     objectToPost.put("client_secret", ApplicationConfig.CLIENT_SECRET);

        this.setObjectToPost(objectToPost);
    }

}

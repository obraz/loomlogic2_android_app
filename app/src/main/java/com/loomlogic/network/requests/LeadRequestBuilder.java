package com.loomlogic.network.requests;

import com.google.gson.reflect.TypeToken;
import com.loomlogic.network.requests.data.LeadRequestData;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.UserData;

import java.lang.reflect.Type;

public class LeadRequestBuilder extends BaseLLRequestBuilder {

    public LeadRequestBuilder(LeadRequestData leadRequestData) {
        Type dataType = new TypeToken<ResponseDataWrapper<UserData>>() {
        }.getType();
        this.setResponseClassSpecifier(dataType);

        setLocalPath("leads");
        this.setObjectToPost(leadRequestData);
    }
}
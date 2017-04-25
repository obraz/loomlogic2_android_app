package com.loomlogic.network.managers;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.kt.http.base.BaseRequest;
import com.kt.http.base.ResponseData;
import com.kt.http.base.client.KTClient;
import com.loomlogic.network.requests.RegisterRequestBuilder;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.UserData;

/**
 * Created by alex on 4/14/17.
 */

public class RegisterManager extends BaseItemManager<UserData, Bundle, String> {
    public RegisterManager(@NonNull KTClient client) {
        super(client);
    }

    @Override
    protected BaseRequest getFetchRequest(KTClient client, Bundle requestParams) {
        return new RegisterRequestBuilder().create();
    }

    @Override
    protected String getEntityRequestTag(Bundle params) {
        return "";
    }

    @Override
    protected UserData readResponseFromRequest(BaseRequest request, ResponseData data, String tag) {
        ResponseDataWrapper<UserData> dataWrapper = (ResponseDataWrapper<UserData>) data.getData();
        return dataWrapper.data;
    }

    @Override
    protected boolean storeResponse(UserData response, String tag) {
        return false;
    }

    @Override
    protected UserData restoreResponse(String tag) {
        return null;
    }
}

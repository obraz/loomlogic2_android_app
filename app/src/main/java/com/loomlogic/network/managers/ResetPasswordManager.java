package com.loomlogic.network.managers;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.kt.http.base.BaseRequest;
import com.kt.http.base.ResponseData;
import com.kt.http.base.client.KTClient;
import com.loomlogic.network.requests.ResetPasswordRequestBuilder;
import com.loomlogic.network.responses.ResetPasswordData;
import com.loomlogic.network.responses.ResponseDataWrapper;

/**
 * Created by alex on 4/14/17.
 */

public class ResetPasswordManager extends BaseItemManager<ResetPasswordData, Bundle, String> {
    private static final String KEY_EMAIL = "KEY_EMAIL";

    public ResetPasswordManager(@NonNull KTClient client) {
        super(client);
    }

    @Override
    protected BaseRequest getFetchRequest(KTClient client, Bundle requestParams) {
        return new ResetPasswordRequestBuilder(getEmailFromBundle(requestParams)).create();
    }

    @Override
    protected String getEntityRequestTag(Bundle params) {
        return "";
    }

    @Override
    protected ResetPasswordData readResponseFromRequest(BaseRequest request, ResponseData data, String tag) {
        ResponseDataWrapper<ResetPasswordData> dataWrapper = (ResponseDataWrapper<ResetPasswordData>) data.getData();
        return dataWrapper.data;
    }

    @Override
    protected boolean storeResponse(ResetPasswordData response, String tag) {
        return false;
    }

    @Override
    protected ResetPasswordData restoreResponse(String tag) {
        return null;
    }

    public void resetPassword(String email) {
        fetchData(getBundle(email));
    }

    private Bundle getBundle(String email) {
        Bundle result = new Bundle();
        result.putString(KEY_EMAIL, email);
        return result;
    }

    private String getEmailFromBundle(Bundle bundle) {
        return bundle.getString(KEY_EMAIL);
    }
}

package com.loomlogic.network.managers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kt.http.base.BaseRequest;
import com.kt.http.base.ResponseData;
import com.kt.http.base.client.KTClient;
import com.loomlogic.network.requests.ResetPasswordAuthRequestBuilder;
import com.loomlogic.network.requests.ResetPasswordCompleteRequestBuilder;
import com.loomlogic.network.requests.ResetPasswordRequestBuilder;
import com.loomlogic.network.responses.ResetPasswordData;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.UserData;
import com.loomlogic.signin.ResetPasswordAction;

import java.util.Map;

import static com.loomlogic.signin.ResetPasswordAction.AUTH;

/**
 * Created by alex on 4/14/17.
 */

public class ResetPasswordManager extends BaseItemManager<ResetPasswordData, Bundle, ResetPasswordAction> {

    private static final String KEY_TAG = "KEY_TAG";
    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_PASSWORD = "KEY_PASSWORD";

    public ResetPasswordManager(@NonNull KTClient client) {
        super(client);
    }

    @Override
    protected BaseRequest getFetchRequest(KTClient client, Bundle requestParams) {
        switch (getTagFromBundle(requestParams)) {
            case RESET:
                return new ResetPasswordRequestBuilder(getEmailFromBundle(requestParams)).create();
            case AUTH:
                return new ResetPasswordAuthRequestBuilder(getTokenFromBundle(requestParams)).create();
            case COMPLETE:
                return new ResetPasswordCompleteRequestBuilder(getPasswordFromBundle(requestParams)).create();
            default:
                return null;
        }
    }

    @Override
    protected ResetPasswordAction getEntityRequestTag(Bundle params) {
        return getTagFromBundle(params);
    }

    @Override
    protected ResetPasswordData readResponseFromRequest(BaseRequest request, ResponseData response, ResetPasswordAction tag) {
        ResponseDataWrapper<ResetPasswordData> dataWrapper = (ResponseDataWrapper<ResetPasswordData>) response.getData();
        switch (tag) {
            case AUTH:
                if (dataWrapper.isSuccess()) {
                    saveUserTokens(response);
                }
                break;
        }
        return dataWrapper.getData();
    }

    @Override
    protected boolean storeResponse(ResetPasswordData response, ResetPasswordAction tag) {
        return false;
    }

    @Override
    protected ResetPasswordData restoreResponse(ResetPasswordAction tag) {
        return null;
    }

    private void saveUserTokens(ResponseData response) {
        UserData userData = new UserData();
        Map<String, String> headers = response.getHeaders();
        userData.setAccessToken(headers.get("access-token"));
        userData.setUid(headers.get("uid"));
        userData.setClient(headers.get("client"));
        ProfileDataStorage.INSTANCE.saveUserData(userData);
        Log.e("saveUserTokens: ", "" + userData.toString());
    }

    public void resetPassword(String email) {
        fetchData(getResetPasswordBundle(email));
    }

    private Bundle getResetPasswordBundle(String email) {
        Bundle result = new Bundle();
        result.putString(KEY_EMAIL, email);
        result.putSerializable(KEY_TAG, ResetPasswordAction.RESET);
        return result;
    }

    public void resetPasswordAuth(String token) {
        fetchData(getResetPasswordAuthBundle(token));
    }

    private Bundle getResetPasswordAuthBundle(String token) {
        Bundle result = new Bundle();
        result.putString(KEY_TOKEN, token);
        result.putSerializable(KEY_TAG, AUTH);
        return result;
    }

    public void resetPasswordComplete(String password) {
        fetchData(getResetPasswordCompleteBundle(password));
    }

    private Bundle getResetPasswordCompleteBundle(String password) {
        Bundle result = new Bundle();
        result.putString(KEY_PASSWORD, password);
        result.putSerializable(KEY_TAG, ResetPasswordAction.COMPLETE);
        return result;
    }

    private ResetPasswordAction getTagFromBundle(Bundle bundle) {
        return (ResetPasswordAction) bundle.getSerializable(KEY_TAG);
    }

    private String getEmailFromBundle(Bundle bundle) {
        return bundle.getString(KEY_EMAIL);
    }

    private String getTokenFromBundle(Bundle bundle) {
        return bundle.getString(KEY_TOKEN);
    }

    private String getPasswordFromBundle(Bundle bundle) {
        return bundle.getString(KEY_PASSWORD);
    }
}

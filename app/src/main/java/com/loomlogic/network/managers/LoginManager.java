package com.loomlogic.network.managers;

import com.android.volley.RequestQueue;
import com.kt.http.base.BaseRequest;
import com.kt.http.base.ResponseData;
import com.kt.http.base.login.ILoginManager;
import com.loomlogic.network.requests.LoginRequestBuilder;
import com.loomlogic.network.responses.ResponseDataWrapper;
import com.loomlogic.network.responses.UserData;

public class LoginManager implements ILoginManager {

    private UserData mUserData;

    public ResponseData login(String email, String password, RequestQueue queue) {

        BaseRequest loginRequest = new LoginRequestBuilder(email, password).create();
        ResponseData loginResponseData = loginRequest.performRequest(true, queue);

        if (loginResponseData != null && loginResponseData.getData() != null) {
            ResponseDataWrapper<UserData> dataWrapper = (ResponseDataWrapper<UserData>) loginResponseData.getData();
            UserData data = dataWrapper.data;
            this.applyLoginResponse(data);
        }

        return loginResponseData;
    }

    private void applyLoginResponse(UserData response) {
        this.mUserData = response;
        ProfileDataStorage.INSTANCE.saveUserData(response);
    }

//    private void applyTokenResponse(TokenData response) {
//        UserData userData = getUserDataStored();
//        userData.applyTokenData(response);
//    }

    public UserData getUserDataStored() {
        if (mUserData == null) {
              mUserData = ProfileDataStorage.INSTANCE.readUserData();
        }
        return mUserData;
    }

    @Override
    public boolean shouldRestoreLogin() {
        //return true if login session restoring with no user interaction is required during runtime (e.g - it can be timed out but you would like to restore it with no user interaction)
        return true;
    }

    @Override
    public boolean canRestoreLogin() {
        //return true if you are able to restore login without user interaction
        return getUserDataStored() != null;
    }

    @Override
    public void applyLoginDataToRequest(BaseRequest request) {
        //apply some session-dependent data to request
        UserData userData = getUserDataStored();
        if (userData != null) {
           // "access-token": "z6oVDt-trgjl1ovXo4srmA",
                  //  "client": "VmTxKXbNz55YbpeSifDfkA",  "uid": "testapimobile@testapimobile.com",
//            Log.e("Authorization  id=" + userData.getUserId(), userData.getAccessToken());
//            request.addRequestHeader("Authorization", "Bearer " + userData.getAccessToken());
        }
    }

    @Override
    public boolean restoreLoginData(RequestQueue queue) {
//        UserData user = getUserDataStored();
//        BaseRequest loginRequest = new TokenRequestBuilder(user.getRefreshToken()).create();
//        ResponseData loginResponseData = loginRequest.performRequest(true, queue);
//        if (loginResponseData != null && loginResponseData.getData() != null) {
//            ResponseDataWrapper<TokenData> dataWrapper = (ResponseDataWrapper<TokenData>) loginResponseData.getData();
//            TokenData data = dataWrapper.data;
//            if (data != null) {
//                this.applyTokenResponse(data);
//                return true;
//            }
//        }

        return false;
    }

    @Override
    public void onLoginRestoreFailed() {
        //handle background login restore failure here
    }

    @Override
    public Object logout(RequestQueue queue) {
        return null;
    }
}

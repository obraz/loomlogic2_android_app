package com.kt.http.base.login;

import com.android.volley.RequestQueue;
import com.kt.http.base.BaseRequest;

public class AnonymousLoginManager implements ILoginManager {


    public Object login(String userName, String password, RequestQueue queue) {
        return null;
    }

    @Override
    public boolean shouldRestoreLogin() {
        return false;
    }

    @Override
    public boolean canRestoreLogin() {
        return false;
    }

    @Override
    public void applyLoginDataToRequest(BaseRequest request) {
    }

    @Override
    public boolean restoreLoginData(RequestQueue queue) {
        return false;
    }


    @Override
    public void onLoginRestoreFailed() {

    }

    @Override
    public Object logout(RequestQueue queue) {
        return null;
    }

}

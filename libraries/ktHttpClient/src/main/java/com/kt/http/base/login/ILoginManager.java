package com.kt.http.base.login;

import com.android.volley.RequestQueue;
import com.kt.http.base.BaseRequest;


public interface ILoginManager {

    /**
     * @return true if manager has to perform login restore attempt in case of 401 error
     */
    boolean shouldRestoreLogin();

    /**
     * @return true if login can be restored (there are credentials or access token cached)
     */
    boolean canRestoreLogin();

    /**
     * Add necessary authentication data to request headers or post/get parameters
     */
    void applyLoginDataToRequest(BaseRequest request);

    /**
     * Restore login data, if possible.
     * Note: this call should be performed synchronously
     *
     * @param queue operation queue you can to perform login within (but it isn't necessary)
     * @return true if restore succeeded (or you can't define a result) false in case of failure
     */
    boolean restoreLoginData(RequestQueue queue);

    /**
     * Method will be called in case if {@link #restoreLoginData restoreLoginData} returned false or we get 401 exception after login was restored
     */
    void onLoginRestoreFailed();

    /**
     * Perform logout operation
     *
     * @return logout request result
     */
    Object logout(RequestQueue queue);

}

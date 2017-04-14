package com.loomlogic.network.requests;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.kt.http.base.BaseRequest;
import com.kt.http.base.BaseRequestBuilder;
import com.loomlogic.network.ApplicationConfig;
import com.loomlogic.network.responses.ApiError;

import java.lang.reflect.Type;

/**
 * Created on 30.09.2016.
 */
public class BaseStylesRequestBuilder extends BaseRequestBuilder {
    public BaseStylesRequestBuilder() {
        this.setRequestMethod(BaseRequest.RequestMethod.POST);
        this.setResponseFormat(BaseRequest.ResponseFormat.JSON);
        this.setRequestFormat(BaseRequest.RequestFormat.JSON);

        Type errorType = new TypeToken<ApiError>() {
        }.getType();
        this.setErrorResponseClassSpecifier(errorType);
    }

    public void setGetMethod() {
        this.setRequestMethod(BaseRequest.RequestMethod.GET);
    }

    public void setPutMethod() {
        this.setRequestMethod(BaseRequest.RequestMethod.PUT);
    }

    public void setLocalPath(String path) {
        this.setRequestUri(concatURL(ApplicationConfig.BASE_URL, path));
    }

    private static String validateBaseURL(String theBaseURL) {
        if (!TextUtils.isEmpty(theBaseURL) && theBaseURL.charAt(theBaseURL.length() - 1) != '/') {
            theBaseURL += '/';
        }
        return theBaseURL;
    }

    public static String concatURL(String baseUrl, String path) {

        baseUrl = validateBaseURL(baseUrl);

        boolean pathAlreadyHasDomain = !TextUtils.isEmpty(path) && (path.startsWith("http://") || path.startsWith("https://"));

        if (TextUtils.isEmpty(baseUrl) || pathAlreadyHasDomain) {
            return path;
        } else {
            if (!TextUtils.isEmpty(path) && path.charAt(0) == '/') {
                path = path.substring(1);
            }
            return baseUrl + path;
        }
    }

}

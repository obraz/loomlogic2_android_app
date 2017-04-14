package com.kt.http.base;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;


public abstract class BaseStringResponseHandler extends ResponseHandler {

    protected abstract Object itemFromResponse(@NonNull String response, @NonNull Class<?> theClass);

    protected abstract Object itemFromResponse(@NonNull String response, @NonNull Type theType);

    protected Object itemFromResponseWithSpecifier(String response, Object theSpecifier) {
        Object result = null;
        if (response != null && theSpecifier != null) {
            if (theSpecifier instanceof Class<?>) {
                result = itemFromResponse(response, (Class<?>) theSpecifier);
            } else if (theSpecifier instanceof Type) {
                result = itemFromResponse(response, (Type) theSpecifier);
            } else {
                throw new IllegalArgumentException("You have to specify Class<?> or Type instance");
            }
        }
        return result;
    }

    protected Response<ResponseData> parseNetworkResponse(NetworkResponse response, Object responseClassSpecifier) {
        String resultStr = parseResponseString(response);
        Log.e("resultStr", resultStr);

        ResponseData responseData = new ResponseData();

        responseData.statusCode = response.statusCode;
        responseData.headers = new HashMap<String, String>(response.headers);

        if (!TextUtils.isEmpty(resultStr)) {
            responseData.data = this.itemFromResponseWithSpecifier(resultStr, responseClassSpecifier);
        }

        Response<ResponseData> result = Response.success(responseData, HttpHeaderParser.parseCacheHeaders(response));

        responseData.error = result.error;

        return result;
    }

    protected String parseResponseString(NetworkResponse response) {
        String parsed = null;
        if (response.data != null) {
            try {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                parsed = new String(response.data);
            }
        }
        return parsed;
    }
}

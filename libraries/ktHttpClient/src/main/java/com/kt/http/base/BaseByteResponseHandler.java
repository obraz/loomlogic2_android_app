package com.kt.http.base;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;


public abstract class BaseByteResponseHandler extends ResponseHandler {

    /**
     * No specifier class supported yet. Just byte[] is returned
     */
    protected Object itemFromResponseWithSpecifier(NetworkResponse response, Object theSpecifier) {
        Object result = null;
        if (response != null && response.data != null && response.data.length > 0) {
            result = response.data;
        }
        return result;
    }

    protected Response<ResponseData> parseNetworkResponse(NetworkResponse response, Object responseClassSpecifier) {
        ResponseData responseData = new ResponseData();

        responseData.data = this.itemFromResponseWithSpecifier(response, responseClassSpecifier);

        responseData.statusCode = response.statusCode;
        responseData.headers = new HashMap<String, String>(response.headers);

        Response<ResponseData> result = Response.success(responseData, HttpHeaderParser.parseCacheHeaders(response));

        responseData.error = result.error;

        return result;
    }

    ;

}

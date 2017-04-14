package com.kt.http.base;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;


public abstract class ResponseHandler {

    protected abstract String getAcceptValueType();

    protected abstract Response<ResponseData> parseNetworkResponse(NetworkResponse response, Object responseClassSpecifier);
}

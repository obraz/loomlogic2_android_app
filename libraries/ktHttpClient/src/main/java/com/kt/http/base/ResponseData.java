package com.kt.http.base;

import com.android.volley.VolleyError;

import java.util.Map;

public class ResponseData {

    protected Object data;
    protected Map<String, String> headers;
    protected int statusCode;
    protected VolleyError error;
    protected Object parsedErrorResponse;


    /**
     * @return Instance of class, specified in response or null if no such class was specified.
     */
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public VolleyError getError() {
        return error;
    }

    /**
     * @return parsed error response is errorResponseSpecifier was provided and error received.
     */
    public Object getParsedErrorResponse() {
        return parsedErrorResponse;
    }

    public void cloneTo(ResponseData target) {
        target.data = data;
        target.headers = headers;
        target.statusCode = statusCode;
        target.error = error;
        target.parsedErrorResponse = parsedErrorResponse;
    }

}

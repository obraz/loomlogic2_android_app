package com.kt.http.base;

/**
 * Created on 17.04.2015.
 */
public class RequestConfig {

    private BaseRequest.RequestFormat requestFormat;
    private BaseRequest.ResponseFormat responseFormat;
    private Object responseClassSpecifier;
    private Object errorResponseClassSpecifier;

    public RequestConfig() {

    }

    public RequestConfig(Object responseClassSpecifier) {
        this(responseClassSpecifier, null, null);
    }

    public RequestConfig(Object responseClassSpecifier, BaseRequest.RequestFormat requestFormat, BaseRequest.ResponseFormat responseFormat) {
        this.responseClassSpecifier = responseClassSpecifier;
        this.requestFormat = requestFormat;
        this.responseFormat = responseFormat;
    }

    public BaseRequest.RequestFormat getRequestFormat() {
        return requestFormat;
    }

    public void setRequestFormat(BaseRequest.RequestFormat requestFormat) {
        this.requestFormat = requestFormat;
    }

    public BaseRequest.ResponseFormat getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(BaseRequest.ResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
    }

    /**
     * @return Class or Type, returned as data field of ResultData object, can be null if you don't need one.
     */
    public Object getResponseClassSpecifier() {
        return responseClassSpecifier;
    }

    /**
     * @param responseClassSpecifier Class or Type, returned as parsedErrorResponse field of ResultData object, can be null if you don't need one.
     */
    public void setResponseClassSpecifier(Object responseClassSpecifier) {
        this.responseClassSpecifier = responseClassSpecifier;
    }

    public Object getErrorResponseClassSpecifier() {
        return errorResponseClassSpecifier;
    }

    public void setErrorResponseClassSpecifier(Object errorResponseClassSpecifier) {
        this.errorResponseClassSpecifier = errorResponseClassSpecifier;
    }
}

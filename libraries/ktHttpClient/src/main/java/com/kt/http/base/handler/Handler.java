package com.kt.http.base.handler;

import com.kt.http.base.BaseRequest;
import com.kt.http.base.RequestHandler;
import com.kt.http.base.ResponseHandler;


/**
 * Created on 15.04.2015.
 */
public class Handler {

    protected static final String PROTOCOL_REQUEST_APP_TYPE_JSON = "application/json";
    protected static final String PROTOCOL_REQUEST_APP_TYPE_XML = "application/xml";
    protected static final String PROTOCOL_REQUEST_APP_TYPE_JSON_HAL = "application/hal+json";
    protected static final String PROTOCOL_REQUEST_APP_TYPE_TEXT = "text/plain";
    protected static final String PROTOCOL_REQUEST_APP_TYPE_URL_ENCODED = "application/x-www-form-urlencoded";

    protected static final String CONTENT_TYPE_CHARSET_PREFIX = "; charset=";

    public static RequestHandler getRequestHandlerForFormat(BaseRequest.RequestFormat requestFormat) {

        if (requestFormat == null) {
            requestFormat = BaseRequest.RequestFormat.TEXT;
        }

        switch (requestFormat) {
            case XML:
                return new XMLRequestHandler();
            case JSON_HAL:
                return new JSONRequestHandler();
            case JSON:
                return new JSONRequestHandler();
            case TEXT:
                return new TextRequestHandler();
            case URL_ENCODED:
                return new UrlEncodedRequestHandler();
            case MULTIPART:
                return new MultipartRequestHandler();
            default:
                throw new IllegalArgumentException("Unrecognised request requestFormat:" + requestFormat.name());
        }
    }

    public static ResponseHandler getResponseHandlerForFormat(BaseRequest.ResponseFormat responseFormat) {
        if (responseFormat == null) {
            responseFormat = BaseRequest.ResponseFormat.TEXT;
        }

        switch (responseFormat) {
            case XML:
                return new XMLResponseHandler();
            case TEXT:
                return new TextResponseHandler();
            case JSON:
                return new JSONResponseHandler();
            case JSON_HAL:
                return new JSONHALResponseHandler();
            case BYTE:
                return new PlainByteReponseHandler();
            case IMAGE:
                return new ImageReponseHandler();
            default: {
                throw new IllegalArgumentException("Unrecognised request responseFormat:" + responseFormat.name());
            }
        }
    }
}

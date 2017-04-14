package com.kt.http.base.handler;

class JSONHALRequestHandler extends JSONRequestHandler {

    @Override
    public String getBodyContentType(String defaultCharset) {
        return Handler.PROTOCOL_REQUEST_APP_TYPE_JSON_HAL + Handler.CONTENT_TYPE_CHARSET_PREFIX + getCharset(defaultCharset);
    }
}

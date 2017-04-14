package com.kt.http.base.handler;

import com.google.gson.Gson;

import com.kt.http.base.IPostableItem;
import com.kt.http.base.RequestHandler;
import com.kt.http.base.SharedGson;

import java.io.UnsupportedEncodingException;


class JSONRequestHandler extends RequestHandler {

    @Override
    public String stringBodyFromItem() {
        if (implementsPostableInterface()) {
            IPostableItem item = (IPostableItem) this.object;
            return item.toJsonString();
        } else {
            Gson gson = SharedGson.getGson();
            return gson.toJson(this.object);
        }
    }

    @Override
    public String getBodyContentType(String defaultCharset) {
        return Handler.PROTOCOL_REQUEST_APP_TYPE_JSON + Handler.CONTENT_TYPE_CHARSET_PREFIX + getCharset(defaultCharset);
    }

    @Override
    public byte[] getBody(String defaultCharset) throws UnsupportedEncodingException {
        String content = this.stringBodyFromItem();
        return content.getBytes(getCharset(defaultCharset));
    }
}

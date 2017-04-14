package com.kt.http.base.handler;

import com.kt.http.base.IPostableItem;
import com.kt.http.base.RequestHandler;

import java.io.UnsupportedEncodingException;

class UrlEncodedRequestHandler extends RequestHandler {

    @Override
    public String stringBodyFromItem() {
        if (implementsPostableInterface()) {
            IPostableItem item = (IPostableItem) this.object;
            return item.toUrlEncodedText();
        } else {
            return this.object.toString();
        }
    }

    @Override
    public String getBodyContentType(String defaultCharset) {
        return Handler.PROTOCOL_REQUEST_APP_TYPE_URL_ENCODED + Handler.CONTENT_TYPE_CHARSET_PREFIX + getCharset(defaultCharset);
    }

    @Override
    public byte[] getBody(String defaultCharset) throws UnsupportedEncodingException {
        String content = this.stringBodyFromItem();
        return content.getBytes(getCharset(defaultCharset));
    }
}

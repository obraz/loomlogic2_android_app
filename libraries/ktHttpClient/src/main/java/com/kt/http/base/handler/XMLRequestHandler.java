package com.kt.http.base.handler;


import com.kt.http.base.IPostableItem;
import com.kt.http.base.RequestHandler;

import java.io.UnsupportedEncodingException;

class XMLRequestHandler extends RequestHandler {

    @Override
    public String stringBodyFromItem() {
        if (implementsPostableInterface()) {
            IPostableItem item = (IPostableItem) this.object;
            return item.toXMLString();
        } else {
            return this.object.toString();
        }
    }

    @Override
    public String getBodyContentType(String defaultCharset) {
        return Handler.PROTOCOL_REQUEST_APP_TYPE_XML + Handler.CONTENT_TYPE_CHARSET_PREFIX + this.getCharset(defaultCharset);
    }

    @Override
    public byte[] getBody(String defaultCharset) throws UnsupportedEncodingException {
        String content = this.stringBodyFromItem();
        return content.getBytes(getCharset(defaultCharset));
    }
}

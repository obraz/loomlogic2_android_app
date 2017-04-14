package com.kt.http.base;

import android.support.annotation.Nullable;

import java.io.UnsupportedEncodingException;

public abstract class RequestHandler {

    protected final String DEFAULT_CHARSET = "utf-8";

    protected Object object;

    public abstract String stringBodyFromItem();

    public abstract String getBodyContentType(String defaultCharset);

    public abstract byte[] getBody(String defaultCharset) throws UnsupportedEncodingException;

    public RequestHandler() {

    }

    protected boolean implementsPostableInterface() {
        return object instanceof IPostableItem;
    }

    protected String getCharset(@Nullable String defaultCharset) {
        String charset = null;
        if (object instanceof ICharsetItem) {
            charset = ((ICharsetItem) object).getCharset();
        }

        if (charset == null) {
            charset = defaultCharset;
            ;
        }

        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        return charset;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}

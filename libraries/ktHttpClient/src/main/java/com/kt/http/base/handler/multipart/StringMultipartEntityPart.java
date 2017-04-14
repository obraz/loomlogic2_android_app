package com.kt.http.base.handler.multipart;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.UnsupportedEncodingException;

/**
 * Created on 17.04.2015.
 */
public class StringMultipartEntityPart implements IMultiPartEntityPart {

    private String value;

    public StringMultipartEntityPart(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public ContentBody getContentBody() throws UnsupportedEncodingException {
        return new StringBody(this.value, ContentType.TEXT_PLAIN);
    }
}

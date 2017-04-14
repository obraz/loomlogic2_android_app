package com.kt.http.base.handler.multipart;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.InputStream;

/**
 * Created on 17.04.2015.
 */
public class StreamMultipartEntityPart implements IMultiPartEntityPart {

    private InputStream value;

    public StreamMultipartEntityPart(InputStream value) {
        this.value = value;
    }

    public InputStream getValue() {
        return value;
    }

    public void setValue(InputStream value) {
        this.value = value;
    }

    @Override
    public ContentBody getContentBody() {
        return new InputStreamBody(this.value, ContentType.DEFAULT_BINARY);
    }
}

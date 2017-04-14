package com.kt.http.base.handler.multipart;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;

/**
 * Created on 17.04.2015.
 */
public class ByteMultipartEntityPart implements IMultiPartEntityPart {

    private byte[] value;
    private String fileName;
    private String mimeType;

    public ByteMultipartEntityPart(byte[] value) {
        this(value, null, null);
    }

    public ByteMultipartEntityPart(byte[] value, String fileName, String mimeType) {
        this.value = value;
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public ContentBody getContentBody() {
        return new ByteArrayBody(value, mimeType, fileName);
    }
}

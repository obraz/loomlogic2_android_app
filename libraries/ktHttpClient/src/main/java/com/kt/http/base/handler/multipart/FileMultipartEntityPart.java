package com.kt.http.base.handler.multipart;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;

/**
 * Created on 17.04.2015.
 */
public class FileMultipartEntityPart implements IMultiPartEntityPart {

    private File value;

    public FileMultipartEntityPart(File value) {
        this.value = value;
    }

    public File getValue() {
        return value;
    }

    public void setValue(File value) {
        this.value = value;
    }

    @Override
    public ContentBody getContentBody() {
        return new FileBody(this.value);
    }
}

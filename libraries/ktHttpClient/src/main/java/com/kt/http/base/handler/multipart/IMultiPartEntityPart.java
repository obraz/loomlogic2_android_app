package com.kt.http.base.handler.multipart;

import org.apache.http.entity.mime.content.ContentBody;

import java.io.UnsupportedEncodingException;

/**
 * Created on 17.04.2015.
 */
public interface IMultiPartEntityPart {

    public ContentBody getContentBody() throws UnsupportedEncodingException;
}

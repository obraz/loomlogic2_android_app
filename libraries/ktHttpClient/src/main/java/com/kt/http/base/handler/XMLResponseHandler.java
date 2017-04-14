package com.kt.http.base.handler;

import android.support.annotation.NonNull;

import com.kt.http.base.BaseStringResponseHandler;
import com.kt.http.base.IResponseItem;
import com.kt.util.internal.ObjectsFactory;

import java.lang.reflect.Type;


class XMLResponseHandler extends BaseStringResponseHandler {

    @Override
    protected Object itemFromResponse(@NonNull String response, @NonNull Class<?> theClass) {
        Object result = createInstanceByInterface(response, theClass);
        if (result == null) {
            //TODO: implement some additional handling for that case
        }
        return result;
    }

    @Override
    protected Object itemFromResponse(@NonNull String json, @NonNull Type theType) {
        Class<?> theClass = theType.getClass();

        Object result = createInstanceByInterface(json, theClass);
        if (result == null) {
            //TODO: implement some additional handling for that case
        }
        return result;
    }

    @Override
    protected String getAcceptValueType() {
        return Handler.PROTOCOL_REQUEST_APP_TYPE_XML;
    }

    private Object createInstanceByInterface(String string, Class<?> theClass) {
        Object result = null;

        if (IResponseItem.class.isAssignableFrom(theClass)) {
            IResponseItem item;
            item = (IResponseItem) ObjectsFactory.newInstance(theClass);
            item.initWithXML(string);
            result = item;
        }
        return result;
    }
}

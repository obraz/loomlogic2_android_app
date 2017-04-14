package com.kt.http.base.handler;

import android.support.annotation.NonNull;

import com.kt.http.base.BaseStringResponseHandler;
import com.kt.http.base.IResponseItem;
import com.kt.util.internal.ObjectsFactory;

import java.lang.reflect.Type;

class TextResponseHandler extends BaseStringResponseHandler {

    protected Object itemFromResponse(@NonNull String data, @NonNull Class<?> theClass) {
        Object result = createInstanceByInterface(data, theClass);
        if (result == null) {
            //TODO: implement some additional handling for that case
        }
        return result;
    }

    protected Object itemFromResponse(@NonNull String data, @NonNull Type theType) {
        Class<?> theClass = theType.getClass();

        Object result = createInstanceByInterface(data, theClass);
        if (result == null) {
            //TODO: implement some additional handling for that case
        }
        return result;
    }

    @Override
    protected String getAcceptValueType() {
        return Handler.PROTOCOL_REQUEST_APP_TYPE_TEXT;
    }

    private Object createInstanceByInterface(String string, Class<?> theClass) {
        Object result = null;

        if (IResponseItem.class.isAssignableFrom(theClass)) {
            IResponseItem item;
            item = (IResponseItem) ObjectsFactory.newInstance(theClass);
            item.initWithText(string);
            result = item;
        }
        return result;
    }

}

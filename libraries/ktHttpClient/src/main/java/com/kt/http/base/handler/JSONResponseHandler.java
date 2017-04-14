package com.kt.http.base.handler;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.kt.http.base.BaseStringResponseHandler;
import com.kt.http.base.IResponseItem;
import com.kt.http.base.SharedGson;
import com.kt.util.internal.ObjectsFactory;

import java.lang.reflect.Type;

class JSONResponseHandler extends BaseStringResponseHandler {

    protected Object itemFromResponse(@NonNull String json, @NonNull Class<?> theClass) {
        Object result = createInstanceByInterface(json, theClass);
        if (result == null) {
            Gson gson = SharedGson.getGson();
            result = gson.fromJson(json, theClass);
        }
        return result;
    }

    protected Object itemFromResponse(@NonNull String json, @NonNull Type theType) {
        Class<?> theClass = theType.getClass();

        Object result = createInstanceByInterface(json, theClass);
        if (result == null) {
            Gson gson = SharedGson.getGson();
            result = gson.fromJson(json, theType);
        }
        return result;
    }

    @Override
    protected String getAcceptValueType() {
        return Handler.PROTOCOL_REQUEST_APP_TYPE_JSON;
    }

    private Object createInstanceByInterface(String json, Class<?> theClass) {
        Object result = null;

        if (IResponseItem.class.isAssignableFrom(theClass)) {
            IResponseItem item;
            item = (IResponseItem) ObjectsFactory.newInstance(theClass);
            item.initWithJSON(json);
            result = item;
        }
        return result;
    }

}

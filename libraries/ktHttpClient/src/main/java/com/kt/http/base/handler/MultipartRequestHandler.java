package com.kt.http.base.handler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.kt.http.base.IPostableItem;
import com.kt.http.base.RequestHandler;
import com.kt.http.base.handler.multipart.IMultiPartEntityPart;
import com.kt.util.L;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

class MultipartRequestHandler extends RequestHandler {

    private MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    private HttpEntity httpentity;


    public MultipartRequestHandler() {
        this.entity = MultipartEntityBuilder.create();
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    }

    @Override
    public void setObject(Object object) {
        super.setObject(object);
        formMultipartEntity(object);
    }

    private void formMultipartEntity(Object source){
        if (source instanceof Map){
            formMultipartEntityMap((Map)source);
        }else{
            formMultipartEntityObject(source);
        }
    }

    private void formMultipartEntityObject(Object source) {
        Class<?> currentClass = source.getClass();
        while (!Object.class.equals(currentClass)) {
            Field[] fields = currentClass.getDeclaredFields();
            for (int counter = 0; counter < fields.length; counter++) {
                Field field = fields[counter];
                Expose expose = field.getAnnotation(Expose.class);
                if (expose != null && !expose.deserialize() || Modifier.isTransient(field.getModifiers())) {
                    continue;// We don't have to copy ignored fields.
                }
                field.setAccessible(true);
                Object value;

                String name;
                SerializedName serializableName = field.getAnnotation(SerializedName.class);
                if (serializableName != null) {
                    name = serializableName.value();
                } else {
                    name = field.getName();
                }
                try {
                    value = field.get(source);
                    addEntity(name, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        httpentity = entity.build();
    }

    private void formMultipartEntityMap(Map<Object,Object> source){
        for (Map.Entry entry : source.entrySet()) {

            Object value = entry.getValue();

            String name = entry.getKey().toString();

            try {
                addEntity(name, value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        httpentity = entity.build();
    }

    private void addEntity(String name, Object value) throws UnsupportedEncodingException {
        if (value != null) {
            if (value instanceof IMultiPartEntityPart) {
                ContentBody body = ((IMultiPartEntityPart) value).getContentBody();
                entity.addPart(name, body);
            } else {
                if (value instanceof List) {
                    for (Object item : (List) value) {
                        addEntity(name + "[]", item);
                    }
                    return;
                }

                if (value.getClass().isArray()) {
                    Object[] array = (Object[]) value;
                    for (int counter = 0; counter < array.length; counter++) {
                        Object item = array[counter];
                        if (item != null) {
                            addEntity(name + "[]", item);
                        }
                    }
                    return;
                }

                entity.addTextBody(name, value.toString());
            }
        }
    }

    @Override
    public String stringBodyFromItem() {
        if (implementsPostableInterface()) {
            IPostableItem item = (IPostableItem) this.object;
            return item.toPlainText();
        } else {
            return this.object.toString();
        }
    }

    @Override
    public String getBodyContentType(String defaultCharset) {
        return httpentity.getContentType().getValue();
    }

    @Override
    public byte[] getBody(String defaultCharset) throws UnsupportedEncodingException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpentity.writeTo(bos);
        } catch (IOException e) {
            L.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }
}

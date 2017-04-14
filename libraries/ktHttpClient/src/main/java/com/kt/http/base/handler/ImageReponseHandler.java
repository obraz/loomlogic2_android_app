package com.kt.http.base.handler;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.android.volley.NetworkResponse;
import com.kt.http.base.BaseByteResponseHandler;

/**
 * Created on 22.04.2015.
 * Returns image Drawable object as a result
 */
public class ImageReponseHandler extends BaseByteResponseHandler {

    @Override
    protected String getAcceptValueType() {
        return "image/*";
    }

    protected Object itemFromResponseWithSpecifier(NetworkResponse response, Object theSpecifier) {
        Object result = null;
        if (response != null && response.data != null && response.data.length > 0) {

            Bitmap imageBitmap = BitmapFactory.decodeByteArray(response.data, 0, response.data.length);
            Drawable imageDrawable = new BitmapDrawable(Resources.getSystem(), imageBitmap);

            result = imageDrawable;
        }
        return result;
    }
}

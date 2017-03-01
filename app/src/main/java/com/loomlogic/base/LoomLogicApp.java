package com.loomlogic.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by alex on 3/1/17.
 */

public class LoomLogicApp extends Application {
    private static Context sharedContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedContext = this;
    }

    @NonNull
    public static Context getSharedContext() {
        if (sharedContext == null) {
            throw new IllegalStateException("getSharedContext() called before Application.onCreate()");
        }
        return sharedContext;
    }
}

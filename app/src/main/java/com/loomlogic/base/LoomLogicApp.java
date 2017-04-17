package com.loomlogic.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.loomlogic.network.Model;

/**
 * Created by alex on 3/1/17.
 */

public class LoomLogicApp extends Application {
    private static Context sharedContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedContext = this;
        Model.instance(sharedContext);
    }

    @NonNull
    public static Context getSharedContext() {
        if (sharedContext == null) {
            throw new IllegalStateException("getSharedContext() called before Application.onCreate()");
        }
        return sharedContext;
    }
}

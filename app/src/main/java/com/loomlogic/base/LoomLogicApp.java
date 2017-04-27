package com.loomlogic.base;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.loomlogic.network.Model;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by alex on 3/1/17.
 */

public class LoomLogicApp extends Application {
    private static Context sharedContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
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

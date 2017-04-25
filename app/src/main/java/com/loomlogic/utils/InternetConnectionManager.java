package com.loomlogic.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.github.pwittchen.networkevents.library.BusWrapper;
import com.github.pwittchen.networkevents.library.NetworkEvents;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by alex on 4/19/17.
 */

public class InternetConnectionManager {
    private final ConnectivityManager connectivityManager;
    private BusWrapper busWrapper;
    private NetworkEvents networkEvents;

    public static InternetConnectionManager from(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return new InternetConnectionManager(context, connectivityManager);
    }

    InternetConnectionManager(Context context, ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
        busWrapper = getGreenRobotBusWrapper(new EventBus());
        networkEvents = new NetworkEvents(context.getApplicationContext(), busWrapper).enableInternetCheck().enableWifiScan();

    }

    @NonNull
    private BusWrapper getGreenRobotBusWrapper(final EventBus bus) {
        return new BusWrapper() {
            @Override
            public void register(Object object) {
                bus.register(object);
            }

            @Override
            public void unregister(Object object) {
                bus.unregister(object);
            }

            @Override
            public void post(Object event) {
                bus.post(event);
            }
        };
    }

    public boolean isConnected() {
        NetworkInfo activeNetworkInfo = getNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private NetworkInfo getNetworkInfo() {
        return connectivityManager.getActiveNetworkInfo();
    }

    public void register(Activity activity) {
        busWrapper.register(activity);
        networkEvents.register();
    }

    public void unregister(Activity activity) {
        busWrapper.unregister(activity);
        networkEvents.unregister();
    }
}

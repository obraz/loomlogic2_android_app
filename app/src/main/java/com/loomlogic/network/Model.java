package com.loomlogic.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.kt.http.base.ResponseData;
import com.kt.http.base.client.KTClient;
import com.kt.util.internal.VolleyHelperFactory;
import com.loomlogic.network.managers.LoginManager;
import com.loomlogic.network.managers.OkHttpStack;
import com.loomlogic.network.managers.RegisterManager;

import java.io.File;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

import okhttp3.OkHttpClient;


public class Model {

    private static Model instance;

    public static Model instance(Context theContext) {
        if (instance == null) {
            instance = new Model(theContext);
        }

        return instance;
    }


    public static Model instance() {
        if (instance == null) {
            throw new IllegalStateException("Called method on uninitialized model");
        }

        return instance;
    }

    private KTClient client;
    private LoginManager loginManager;
    private CookieStore cookieStore;
    private RequestQueue queue;
    //Managers
    private RegisterManager registerManager;

    public KTClient getClient() {
        return client;
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public LoginManager getLoginManager() {
        return loginManager;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public RegisterManager getRegisterManager() {
        return registerManager;
    }

    /**
     * NOTE: login is performed in synchroneus way so you must never call it from UI thread.
     */
    public ResponseData performLogin() {
        return this.loginManager.login(queue);
    }

    public void performLogout() {
        this.loginManager.logout(queue);
    }


    private Model(Context context) {
        loginManager = new LoginManager();
        queue = createNewQueue(context);
        client = new KTClient.Builder(context)
                .setRequestQueue(queue)
                .setLoginManager(loginManager)
                .setRequestTimeout(60000)
                .build();

        registerManager = new RegisterManager(client);
    }

    //Initialization

    private RequestQueue createNewQueue(Context context) {
        cookieStore = new HURLCookieStore(context);
        CookieManager cmrCookieMan = new CookieManager(cookieStore, CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cmrCookieMan);

//        final VolleyHelperFactory.IVolleyHelper helper = VolleyHelperFactory.newHelper();
//        return newRequestQueue(context, helper.createHttpStack(context));

        OkHttpClient client = new OkHttpClient.Builder().build();

        OkHttpStack myOkHttpStack = new OkHttpStack(client);
        return newRequestQueue(context, myOkHttpStack);
    }

    /**
     * volley's default implementation uses internal cache only so we've implemented our, allowing
     * external cache usage.
     */
    private static RequestQueue newRequestQueue(@NonNull final Context context,
                                                @Nullable HttpStack stack) {

        final VolleyHelperFactory.IVolleyHelper helper = VolleyHelperFactory.newHelper();
        final File cacheDir = helper.getBestCacheDir(context);

        if (stack == null) {
            stack = helper.createHttpStack(context);
        }

        final Network network = new BasicNetwork(stack);
        final RequestQueue queue = new RequestQueue(
                new DiskBasedCache(cacheDir, ApplicationConfig.CACHE_DISK_USAGE_BYTES), network, 1);
        queue.start();
        return queue;
    }
}

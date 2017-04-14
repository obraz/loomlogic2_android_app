package com.kt.util.internal;

import android.content.Context;

import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;

import java.io.File;
import java.net.HttpURLConnection;

public class VolleyResponseUtils {

    public static boolean isNetworkingError(VolleyError volleyError)
    {
        if (volleyError.networkResponse == null) {
            if (volleyError instanceof TimeoutError) {
               return true;
            }

            if (volleyError instanceof NoConnectionError) {
                return true;
            }

            if (volleyError instanceof NetworkError) {
                return true;
            }

        }
        return false;
    }

    public static boolean isAuthError(VolleyError volleyError){
        return volleyError != null && volleyError.networkResponse != null
                && volleyError.networkResponse.statusCode == HttpURLConnection.HTTP_UNAUTHORIZED;
    }

    public static RequestQueue defaultRequestQueue(Context context)
    {
        return newRequestQueue(context,null,-1);
    }

    public static RequestQueue newRequestQueue(Context context, HttpStack stack,int maxDiskCacheSizeBytes) {

        final VolleyHelperFactory.IVolleyHelper helper = VolleyHelperFactory.newHelper();
        final File cacheDir = helper.getBestCacheDir(context);

        if (stack == null) {
            stack = helper.createHttpStack(context);
        }

        final Network network = new BasicNetwork(stack);

        final DiskBasedCache diskCache;
        if (maxDiskCacheSizeBytes < 0) {
            diskCache = new DiskBasedCache(cacheDir);
        } else {
            diskCache = new DiskBasedCache(cacheDir, maxDiskCacheSizeBytes);
        }

        final RequestQueue queue = new RequestQueue(diskCache, network,1);
        queue.start();
        return queue;
    }
}

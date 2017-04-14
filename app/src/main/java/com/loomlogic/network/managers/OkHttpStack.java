package com.loomlogic.network.managers;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.OkUrlFactory;


public class OkHttpStack extends HurlStack
{
    private final OkUrlFactory okUrlFactory;
    public OkHttpStack() {
        this(new OkHttpClient());
    }

    public OkHttpStack(OkHttpClient client) {
        this(new OkUrlFactory(client));
    }

    public OkHttpStack(OkUrlFactory okUrlFactory) {
        if (okUrlFactory == null) {
            throw new NullPointerException("Client must not be null.");
        }
        this.okUrlFactory = okUrlFactory;
    }
    @Override
    protected HttpURLConnection createConnection(URL url) throws IOException
    {
        return okUrlFactory.open(url);
    }
}
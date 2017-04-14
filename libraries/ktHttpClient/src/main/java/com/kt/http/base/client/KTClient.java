package com.kt.http.base.client;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.kt.http.base.BaseRequest;
import com.kt.http.base.BaseRequest.OnResponseListener;
import com.kt.http.base.ResponseData;
import com.kt.http.base.login.AnonymousLoginManager;
import com.kt.http.base.login.ILoginManager;
import com.kt.util.internal.ContentResolverRequestQueue;
import com.kt.util.internal.VolleyResponseUtils;

import java.util.List;

public class KTClient implements OnResponseListener {

    public enum DuplicateRequestPolicy {ALLOW, ATTACH, REJECT}

    @NonNull
    private final ResponseListenersSet listeners = new ResponseListenersSet();

    private RequestQueue mDefaultQueue;
    private RequestQueue mContentResolverQueue;

    private String mDefaultCharset;

    private ILoginManager mLoginManager;
    private RequestProgressListener progressListener;

    private int mRequestTimeout = 30000;
    private int mRequestMaxNumRetries = 1;

    private DuplicateRequestPolicy mDuplicateRequestPolicy = DuplicateRequestPolicy.ATTACH;

    public interface OnResponseListener {

        void onResponseReceived(@NonNull BaseRequest request, @NonNull ResponseData data, @Nullable Object tag);

        void onError(@NonNull BaseRequest request, @Nullable ResponseData data, @Nullable Object tag);

        void onCancel(@NonNull BaseRequest request, @Nullable Object tag);
    }

    /**
     * Can be used in order to react on request count changes (start/success/failure or canceling).
     */
    public interface RequestProgressListener {

        /**
         * Called after new request was added to queue
         *
         * @param activeRequests number of requests pending
         */
        void onRequestStarted(KTClient theClient, int activeRequests);

        /**
         * Called after current request was complete
         *
         * @param activeRequests number of requests pending
         */
        void onRequestFinished(KTClient theClient, int activeRequests);
    }

    private KTClient() {
    }


    @NonNull
    protected static RequestQueue getDefaultQueue(@NonNull Context theContext) {
        return Volley.newRequestQueue(theContext.getApplicationContext());
    }


    /**
     * @param request     Request object to be performed
     * @param synchronous if true request result will be returned synchronously
     * @return {@link ResponseData} object, containing request result code and string or error and deserialized object, specified in request.
     */
    public ResponseData performRequest(BaseRequest request, boolean synchronous) {
        return performRequest(request, null, null, synchronous);
    }

    /**
     * @param request     Request object to be performed
     * @param tag         will be applied to the request and returned in listener
     * @param synchronous if true request result will be returned synchronously
     * @return {@link ResponseData} object, containing request result code and string or error and deserialized object, specified in request.
     */
    public ResponseData performRequest(BaseRequest request, Object tag, final OnResponseListener listener, boolean synchronous) {
        request.setRetryPolicy(new DefaultRetryPolicy(mRequestTimeout, mRequestMaxNumRetries, 1));
        if (!mLoginManager.shouldRestoreLogin()) {
            return performRequestNoLoginRestore(request, tag, listener, synchronous);
        } else {
            return performRequestLoginRestore(request, tag, listener, synchronous);
        }
    }

    protected ResponseData performRequestNoLoginRestore(BaseRequest request, Object tag, OnResponseListener listener, boolean synchronous) {
        request.setTag(tag);
        request.setResponseListener(this);
        synchronized (this.mLoginManager){
            this.mLoginManager.applyLoginDataToRequest(request);
        }
        request.setSmartComparisonEnabled(this.mDuplicateRequestPolicy != DuplicateRequestPolicy.ALLOW);

        boolean wasRegisterred;
        boolean skipDuplicateRequestListeners = this.mDuplicateRequestPolicy == KTClient.DuplicateRequestPolicy.REJECT;
        synchronized (listeners) {
            wasRegisterred = this.listeners.registerListenerForRequest(request, listener, tag, skipDuplicateRequestListeners);
        }

        if (wasRegisterred || synchronous) {
            this.onNewRequestStarted();
            return request.performRequest(synchronous, getRequestQueueForRequest(request));
        } else {
            if (skipDuplicateRequestListeners && listener != null) {
                listener.onCancel(request, tag);
            }
            return null;
        }
    }

    @NonNull
    private RequestQueue getRequestQueueForRequest(@NonNull final Request request) {
        if (mDefaultQueue == null) {
            throw new IllegalStateException("mDefaultQueue was not initialized");
        }
        final Uri uri = Uri.parse(request.getUrl());
        switch (uri.getScheme()) {
            case ContentResolver.SCHEME_ANDROID_RESOURCE:
            case ContentResolver.SCHEME_CONTENT:
            case ContentResolver.SCHEME_FILE:
            case ContentResolverRequestQueue.SCHEME_ASSETS:
                if (mContentResolverQueue == null) {
                    throw new IllegalStateException("mContentResolverQueue was not initialized. Make sure you don't use a deprecated constructor");
                }
                return mContentResolverQueue;

            default:
                return mDefaultQueue;
        }
    }

    private ResponseData performRequestLoginRestore(final BaseRequest request, Object tag, final OnResponseListener listener, final boolean synchronous) {
        if (synchronous) {
            return performRequestLoginRestoreSynchrounous(request, tag, listener);
        } else {
            return performRequestLoginRestoreAsynchrounous(request, tag, listener);
        }
    }

    private ResponseData performRequestLoginRestoreAsynchrounous(final BaseRequest request, Object tag, final OnResponseListener listener) {
        final OnResponseListener loginRestoreResponseListener = new OnResponseListener() {
            @Override
            public void onResponseReceived(@NonNull final BaseRequest request,
                                           @NonNull final ResponseData data,
                                           @Nullable final Object tag) {
                if (listener != null) {
                    listener.onResponseReceived(request, data, tag);
                }
            }

            @Override
            public void onError(@NonNull final BaseRequest request,
                                @Nullable final ResponseData data,
                                @Nullable final Object tag) {
                if (data != null && VolleyResponseUtils.isAuthError(data.getError())) {
                    if (mLoginManager.canRestoreLogin()) {
                        new RestoreLoginAttemptTask(request, listener, tag, data).execute();
                    } else {
                        mLoginManager.onLoginRestoreFailed();
                        if (listener != null) {
                            listener.onError(request, data, tag);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onError(request, data, tag);
                    }
                }
            }

            @Override
            public void onCancel(@NonNull final BaseRequest request,
                                 @Nullable final Object tag) {
                if (listener != null) {
                    listener.onCancel(request, tag);
                }
            }
        };

        return performRequestNoLoginRestore(request, tag, loginRestoreResponseListener, false);
    }

    private ResponseData performRequestLoginRestoreSynchrounous(final BaseRequest request, Object tag, final OnResponseListener listener) {
        final OnResponseListener loginRestoreResponseListener = new OnResponseListener() {
            @Override
            public void onResponseReceived(@NonNull final BaseRequest request,
                                           @NonNull final ResponseData data,
                                           @Nullable final Object tag) {
                if (listener != null) {
                    listener.onResponseReceived(request, data, tag);
                }
            }

            @Override
            public void onError(@NonNull final BaseRequest request,
                                @Nullable final ResponseData data,
                                @Nullable final Object tag) {
                if (data != null && VolleyResponseUtils.isAuthError(data.getError())) {
                    if (!mLoginManager.canRestoreLogin()) {
                        if (listener != null) {
                            listener.onError(request, data, tag);
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onError(request, data, tag);
                    }
                }
            }

            @Override
            public void onCancel(@NonNull final BaseRequest request,
                                 @Nullable final Object tag) {
                if (listener != null) {
                    listener.onCancel(request, tag);
                }
            }
        };

        ResponseData result = performRequestNoLoginRestore(request, tag, loginRestoreResponseListener, true);
        if (VolleyResponseUtils.isAuthError(result.getError())) {
            synchronized (this.mLoginManager){
                if (mLoginManager.canRestoreLogin()) {
                    boolean restored = mLoginManager.restoreLoginData(getRequestQueueForRequest(request));
                    if (restored) {
                        result = performRequestNoLoginRestore(request, tag, new OnResponseAuthListenerDecorator(listener), true);
                    } else {
                        if (listener != null) {
                            listener.onError(request, result, tag);
                        }
                    }
                } else {
                    mLoginManager.onLoginRestoreFailed();
                }
            }
        }
        return result;
    }


    /**
     * @return request timeout millis
     */
    public int getRequestTimeout() {
        return mRequestTimeout;
    }

    /**
     * @param requestTimeout request timeout millis
     */
    public void setRequestTimeout(int requestTimeout) {
        this.mRequestTimeout = requestTimeout;
    }

    /**
     * @param mRequestMaxNumRetries request  MaxNumRetries
     */
    public void setRequestMaxNumRetries(int mRequestMaxNumRetries) {
        this.mRequestMaxNumRetries = mRequestMaxNumRetries;
    }

    /**
     * This request is always synchronous
     */
    public final void logout() {
        this.mLoginManager.logout(mDefaultQueue);
    }

    /**
     * @return true all necessary user id data is fetched and login can be restored automatically
     */
    public boolean isLogged() {
        return this.mLoginManager.canRestoreLogin();
    }

    public ILoginManager getLoginManager() {
        return mLoginManager;
    }

    public void setLoginManager(ILoginManager loginManager) {
        this.mLoginManager = loginManager;
    }

    /**
     * Synchronous login restore attempt
     *
     * @return false if login restore failed.
     */
    public boolean restoreLogin() {

        if (this.mLoginManager.canRestoreLogin()) {
            synchronized (this.mLoginManager) {
                return this.mLoginManager.restoreLoginData(mDefaultQueue);
            }
        }
        return false;
    }

    @Override
    public void onResponseReceived(ResponseData data, BaseRequest request) {
        synchronized (listeners) {
            List<ResponseListenersSet.ListenerHolder> listenerList = this.listeners.getListenersForRequest(request);
            this.listeners.removeListenersForRequest(request);
            this.onRequestComplete();
            if (listenerList != null) {
                for (ResponseListenersSet.ListenerHolder holder : listenerList) {
                    holder.getListener().onResponseReceived(request, data, holder.getTag());
                }
            }
        }
    }

    @Override
    public void onError(ResponseData data, BaseRequest request) {
        synchronized (listeners) {
            List<ResponseListenersSet.ListenerHolder> listenerList = this.listeners.getListenersForRequest(request);
            this.listeners.removeListenersForRequest(request);
            this.onRequestComplete();
            if (listenerList != null) {
                for (ResponseListenersSet.ListenerHolder holder : listenerList) {
                    holder.getListener().onError(request, data, holder.getTag());
                }
            }
        }
    }

    /**
     * @return Charset, used to encode/decode server request post body and response.
     */
    public String getDefaultCharset() {
        return mDefaultCharset;
    }

    /**
     * @param defaultCharset Charset, used to encode/decode server request post body and response.
     */
    public void setDefaultCharset(String defaultCharset) {
        this.mDefaultCharset = defaultCharset;
    }

    /**
     * @param tag Cancel all requests, containing given tag. If no tag is specified - all requests are canceled.
     */
    public void cancelByTag(final @NonNull Object tag) {
        this.cancelAllRequestsForListener(null, tag);
    }

    /**
     * Cancel all requests
     */
    public void cancelAll() {
        this.cancelAllRequestsForListener(null, null);
    }

    /**
     * @return current duplicate request policy
     */
    public DuplicateRequestPolicy getDuplicateRequestPolicy() {
        return mDuplicateRequestPolicy;
    }

    /**
     * Sets duplicate request handling policy according to parameter provided. Only simultaneous requests are compared (executing at the same time).
     *
     * @param duplicateRequestPolicy in case if
     *                               "ALLOW" - all requests are performed
     *                               "ATTACH" - only first unique request from queue will be performed all other listeners will be attached to this request and triggered.
     *                               "REJECT" - only first unique request from queue will be performed and it's listener triggered. "onCancel()" listener method will be called for all requests
     *                               skipped.
     *                               Default value is "ALLOW"
     */
    public void setDuplicateRequestPolicy(DuplicateRequestPolicy duplicateRequestPolicy) {
        this.mDuplicateRequestPolicy = duplicateRequestPolicy;
    }

    /**
     * Cancel all requests for given listener with tag
     *
     * @param theListener listener to cancel requests for in case if null passed- all requests for given tag will be canceled
     * @param theTag      to cancel requests for, in case if null passed- all requests for given listener will be canceled
     */
    public void cancelAllRequestsForListener(final @Nullable OnResponseListener theListener, final @Nullable Object theTag) {
        cancelAllRequestsForListener(mDefaultQueue, theListener, theTag);
        if (mContentResolverQueue != null) {
            cancelAllRequestsForListener(mContentResolverQueue, theListener, theTag);
        }
    }

    /**
     * Cancel all requests for given listener with tag
     *
     * @param theListener listener to cancel requests for in case if null passed- all requests for given tag will be canceled
     * @param theTag      to cancel requests for, in case if null passed- all requests for given listener will be canceled
     */
    private void cancelAllRequestsForListener(@NonNull final RequestQueue requestQueue,
                                              final @Nullable OnResponseListener theListener,
                                              final @Nullable Object theTag) {
        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                if (theTag == null || theTag.equals(request.getTag())) {
                    synchronized (listeners) {
                        List<ResponseListenersSet.ListenerHolder> listenerList = listeners
                                .getListenersForRequest(request);

                        if (theListener == null || (listenerList != null
                                && holderListContainsListener(listenerList, theListener))) {
                            if (listenerList != null) {
                                listeners.removeListenersForRequest(request);
                                for (ResponseListenersSet.ListenerHolder holder : listenerList) {
                                    holder.getListener()
                                            .onCancel((BaseRequest) request, holder.getTag());
                                }
                                KTClient.this.onRequestComplete();
                            }
                            return true;
                        }
                    }
                }

                return false;
            }
        });
    }

    protected static boolean holderListContainsListener(List<ResponseListenersSet.ListenerHolder> listenerList, OnResponseListener theListener) {
        if (theListener == null) {
            return false;
        }

        boolean listContainsListener = false;
        for (ResponseListenersSet.ListenerHolder holder : listenerList) {
            if (theListener.equals(holder.getListener())) {
                listContainsListener = true;
            }
        }

        return listContainsListener;
    }

    // Manage request progress

    /**
     * @return number of requests pending
     */
    public int getActiveRequestsCount() {
        synchronized (listeners) {
            return this.listeners.registeredRequestCount();
        }
    }

    public RequestProgressListener getProgressListener() {
        return progressListener;
    }

    public void setProgressListener(RequestProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    private void onNewRequestStarted() {
        if (this.progressListener != null) {

            int requestCount = this.getActiveRequestsCount();
            this.progressListener.onRequestStarted(this, requestCount);

        }
    }

    private void onRequestComplete() {
        if (this.progressListener != null) {
            int requestCount = this.getActiveRequestsCount();
            this.progressListener.onRequestFinished(this, requestCount);
        }
    }

    private class OnResponseAuthListenerDecorator implements OnResponseListener {

        private OnResponseListener listener;

        OnResponseAuthListenerDecorator(OnResponseListener listener) {
            this.listener = listener;
        }

        @Override
        public void onResponseReceived(@NonNull final BaseRequest request,
                                       @NonNull final ResponseData data,
                                       @Nullable final Object tag) {
            if (listener != null) {
                this.listener.onResponseReceived(request, data, tag);
            }
        }

        @Override
        public void onError(@NonNull final BaseRequest request,
                            @Nullable final ResponseData data,
                            @Nullable final Object tag) {
            if (data != null && VolleyResponseUtils.isAuthError(data.getError())) {
                mLoginManager.onLoginRestoreFailed();
            }
            if (listener != null) {
                this.listener.onError(request, data, tag);
            }
        }

        @Override
        public void onCancel(@NonNull final BaseRequest request,
                             @Nullable final Object tag) {
            if (listener != null) {
                this.listener.onCancel(request, tag);
            }
        }
    }

    private class RestoreLoginAttemptTask {

        private final BaseRequest request;
        private final OnResponseListener listener;
        private final Object tag;
        private final ResponseData originData;

        RestoreLoginAttemptTask(BaseRequest request, OnResponseListener listener, Object tag, ResponseData originData) {
            this.request = request;
            this.listener = listener;
            this.tag = tag;
            this.originData = originData;
        }

        public void execute() {
            new Thread() {
                @Override
                public void run() {
                    synchronized (mLoginManager) {
                        boolean restored = mLoginManager.restoreLoginData(mDefaultQueue);
                        if (restored) {
                            performRequestNoLoginRestore(request, tag, new OnResponseAuthListenerDecorator(listener), false);
                        } else {
                            listener.onError(request, originData, tag);
                        }
                    }
                }
            }.start();
        }
    }

    public static final class Builder {

        @NonNull
        private final Context mContext;

        private RequestQueue mDefaultQueue;
        private RequestQueue mContentResolverQueue;
        private String mDefaultCharset;

        private ILoginManager mLoginManager;
        private int mRequestTimeout = 1500;
        private int mRequestMaxNumRetries = 1;
        private DuplicateRequestPolicy mDuplicateRequestPolicy = DuplicateRequestPolicy.ATTACH;

        public Builder(@NonNull final Context context) {
            mContext = context.getApplicationContext();
        }

        public Builder setRequestQueue(@NonNull final RequestQueue defaultQueue) {
            mDefaultQueue = defaultQueue;
            return this;
        }

        public Builder setContentResolverRequestQueue(@NonNull final ContentResolverRequestQueue contentResolverQueue) {
            mContentResolverQueue = contentResolverQueue;
            return this;
        }

        public Builder setDefaultCharset(@NonNull final String defaultCharset) {
            this.mDefaultCharset = defaultCharset;
            return this;
        }

        public Builder setLoginManager(@NonNull final ILoginManager loginManager) {
            this.mLoginManager = loginManager;
            return this;
        }

        public Builder setRequestTimeout(@IntRange(from = 0) final int requestTimeout) {
            this.mRequestTimeout = requestTimeout;
            return this;
        }

        public Builder setRequestMaxNumRetries(@IntRange(from = 0) final int mRequestMaxNumRetries) {
            this.mRequestMaxNumRetries = mRequestMaxNumRetries;
            return this;
        }

        public Builder setDuplicateRequestPolicy(@NonNull final DuplicateRequestPolicy duplicateRequestPolicy) {
            this.mDuplicateRequestPolicy = duplicateRequestPolicy;
            return this;
        }

        @NonNull
        public KTClient build() {
            final KTClient client = new KTClient();
            client.mDefaultQueue = mDefaultQueue != null ? mDefaultQueue : getDefaultQueue(mContext);
            client.mContentResolverQueue = mContentResolverQueue != null ? mContentResolverQueue : new ContentResolverRequestQueue(mContext);
            client.mDefaultCharset = mDefaultCharset;
            client.mLoginManager = mLoginManager != null ? mLoginManager : new AnonymousLoginManager();
            client.mRequestTimeout = mRequestTimeout;
            client.mRequestMaxNumRetries = mRequestMaxNumRetries;
            client.mDuplicateRequestPolicy = mDuplicateRequestPolicy;
            client.mContentResolverQueue.start();
            return client;
        }
    }
}
package com.kt.http.base.client;

import com.android.volley.Request;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created on 27.03.2015.
 */
public class ResponseListenersSet {

    private Map<Request, List<ListenerHolder>> listeners;

    public ResponseListenersSet() {
        listeners = new HashMap<Request, List<ListenerHolder>>();
    }

    /**
     * @param listener listener to register for request
     * @return true if new request was registered, false otherwise
     */
    public boolean registerListenerForRequest(Request request, KTClient.OnResponseListener listener, Object tag, boolean skipDuplicateRequestListeners) {
        boolean result = false;

        if (listener == null) {
            return true;
        }

        List<ListenerHolder> listenersList = listeners.get(request);

        if (listenersList == null) {
            listenersList = new LinkedList<ListenerHolder>();
            listeners.put(request, listenersList);
            result = true;
        } else {
            if (!listenersList.isEmpty() && skipDuplicateRequestListeners) {
                //We don't add duplicate listners in case of reject policy
                return result;
            }
        }

        listenersList.add(new ListenerHolder(listener, tag));
        return result;
    }

    /**
     * @return Listeners, registered for this request
     */
    protected List<ListenerHolder> getListenersForRequest(Request request) {
        return listeners.get(request);
    }

    /**
     * Remove all listeners for request
     */
    public void removeListenersForRequest(Request request) {
        listeners.remove(request);
    }

    public synchronized void removeAllListeners() {
        listeners.clear();
    }

    public int registeredRequestCount() {
        return listeners.size();
    }

    public static class ListenerHolder {

        private KTClient.OnResponseListener listener;
        private Object tag;

        public ListenerHolder(KTClient.OnResponseListener listener, Object tag) {
            this.listener = listener;
            this.tag = tag;
        }

        public KTClient.OnResponseListener getListener() {
            return listener;
        }

        public Object getTag() {
            return tag;
        }
    }
}

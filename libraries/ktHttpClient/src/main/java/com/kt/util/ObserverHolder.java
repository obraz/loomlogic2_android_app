package com.kt.util;

import java.util.LinkedList;
import java.util.List;

public class ObserverHolder<ObserverClass> {

    protected final List<ObserverClass> observers = new LinkedList<>();

    /**
     * @return false if observer was already registered
     */
    public synchronized boolean registerObserver(ObserverClass theObserver) {
        boolean isAlreadyRegistered = false;
        for (ObserverClass observer : observers) {
            if (observer == theObserver) {
                isAlreadyRegistered = true;
                break;
            }
        }

        if (!isAlreadyRegistered) {
            this.observers.add(theObserver);
        }
        return !isAlreadyRegistered;
    }

    public synchronized void unregisterObserver(ObserverClass theObserver) {
        this.observers.remove(theObserver);
    }

    public synchronized void clearAll() {
        this.observers.clear();
    }

    public synchronized void notifyAllObservers(ObserverNotifier<ObserverClass> notifier) {
        final List<ObserverClass> observersCopy = new LinkedList<>(observers);
        for (ObserverClass observer : observersCopy) {
            if (observer != null) {
                notifier.onNotify(observer);
            }
        }
    }

    public interface ObserverNotifier<ObserverClass> {

        void onNotify(ObserverClass observer);
    }
}

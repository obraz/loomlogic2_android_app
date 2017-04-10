package com.loomlogic.base;

/**
 * Created by alex on 3/2/17.
 */

public class MessageEvent {

    public enum MessageEventType {
        LEADS_MENU_SELECT, NAVIGATION_BAR_SHOW, NAVIGATION_BAR_HIDE
    }

    private MessageEventType type;
    private Object object;

    public MessageEvent(MessageEventType type) {
        this.type = type;
    }

    public MessageEvent(MessageEventType type, Object object) {
        this.type = type;
        this.object = object;
    }

    public MessageEventType getType() {
        return type;
    }

    public Object getObject() {
        return object;
    }
}

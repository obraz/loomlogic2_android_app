package com.loomlogic.base;

/**
 * Created by alex on 3/2/17.
 */

public class MessageEvent {

    public enum MessageEventType {
        LEADS_MENU_SELECT
    }

    private MessageEventType type;
    private Object object;

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

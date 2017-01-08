package org.beacon.client.Message;

import org.beacon.client.MessageType;

/**
 * Created by Adiesha on 2017-01-02.
 */
public class Message {
    private MessageType messageType = null;
    private String messageCode = "";
    public Message(MessageType messageType) {
        this.messageType = messageType;
    }
}

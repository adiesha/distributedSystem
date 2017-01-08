package org.beacon.client;

import java.util.StringTokenizer;

/**
 * Created by Adiesha on 2017-01-01.
 */
public class MessageExtractor {
    public MessageExtractor() {

    }

    public MessageType getMessageType(String message) {
        StringTokenizer st = new StringTokenizer(message, " ");

        String length = st.nextToken();
        String messageType = st.nextToken();

        return MessageType.valueOf(messageType);
    }
}

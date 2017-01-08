package org.beacon.client.Message;

import org.beacon.client.MessageType;

import java.net.InetAddress;

/**
 * Created by Adiesha on 2017-01-02.
 */
public class Message {
    private String message;
    private InetAddress address;
    private int port;

    public Message(String message,InetAddress inetAddress,int port) {
        this.message = message;
        this.address = inetAddress;
        this.port  = port;
    }

    public String getMessage() {
        return message;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}

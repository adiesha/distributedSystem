package org.beacon.client;

import java.net.InetAddress;
import java.util.Properties;

/**
 * Created by Adiesha on 2017-01-05.
 */
public class MessageEncoder {
    Properties properties;
    public MessageEncoder(Properties properties) {
        this.properties = properties;
    }

    public String encodeRegisterMessage(String username, InetAddress nodeIp, int port) {
        String regRequest = MessageType.REG.toString()+ " " + nodeIp.getHostAddress() + " " + port + " " + username;
        String request = String.format("%04d " + regRequest, regRequest.getBytes().length + 5);
        return request;
    }

    public String encodeUnregisterMessage(String username,InetAddress nodeIp, int port) {
        String regRequest = MessageType.UNREG.toString()+ " " + nodeIp.getHostAddress() + " " + port + " " + username;
        String request = String.format("%04d " + regRequest, regRequest.getBytes().length + 5);
        return request;
    }

    public String encodeJoinMessage(InetAddress nodeAddress,int port) {
        String regRequest = MessageType.JOIN.toString()+ " " + nodeAddress.getHostAddress() + " " + port;
        String request = String.format("%04d " + regRequest, regRequest.getBytes().length + 5);
        return request;
    }

    public String encodeJoinResponse() {
        return "";
    }

}

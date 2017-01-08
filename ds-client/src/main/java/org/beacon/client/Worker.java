package org.beacon.client;

import org.beacon.client.Message.Message;

import java.io.IOException;

/**
 * Created by Adiesha on 2017-01-08.
 */
public class Worker implements Runnable {
    Node node;
    MessageExtractor messageExtractor = null;
    public Worker(Node node) {
        this.node = node;
        messageExtractor = new MessageExtractor();
    }

    public Worker() {

    }


    public void run() {
        Message message;
        while (true) {
            try {
                message = node.getUdpHandler().receiveMessage(2000);
                MessageType messageType = messageExtractor.getMessageType(message.getMessage());
                this.invokeNodeMethod(messageType,message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void invokeNodeMethod(MessageType messageType,Message message) throws IOException{
        if(messageType.equals(MessageType.REGOK)) {
            node.registerResponse(message.getMessage());
        } else if (messageType.equals(MessageType.JOINOK)) {
            node.joinResponse(message);
        } else if (messageType.equals(MessageType.JOIN)) {
            node.generateJoinRequestResponseForNode(message);
        } else {
            System.out.println("still not implemented");
            return;
        }
    }
}

package org.beacon.client;

import org.beacon.client.Message.Message;

import java.io.IOException;
import java.net.SocketException;

/**
 * Created by Adiesha on 2017-01-08.
 */
public class Worker extends Thread {
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
            System.out.println("Worker Online");
            try {
                if (Thread.interrupted()) {
                    System.out.println("Worker thread was interrupted and exited");
                    return;
                }
                System.out.println("In receiving mode");
                message = node.getUdpHandler().receiveMessage(2000);
                System.out.println("After receiving mode");
                MessageType messageType = messageExtractor.getMessageType(message.getMessage());
                this.invokeNodeMethod(messageType, message);

            } catch (IOException e) {
                System.out.println("wrong");
                e.printStackTrace();
            }
        }
    }


    private void invokeNodeMethod(MessageType messageType, Message message) throws IOException {
        if (messageType.equals(MessageType.REGOK)) {
            node.registerResponse(message.getMessage());
        } else if (messageType.equals(MessageType.JOINOK)) {
            node.joinResponse(message);
        } else if (messageType.equals(MessageType.JOIN)) {
            node.generateJoinRequestResponseForNode(message);
        } else if (messageType.equals(MessageType.UNROK)) {
            node.unregisterResponse(message);
        } else {
            System.out.println("Response still not implemented");
            return;
        }
    }
}

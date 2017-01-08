package org.beacon.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        String username;
        username = args[0];
        int mode = 0;
        System.out.println(username);
        System.out.println("Hello World!");
        final Properties properties = new Properties();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream stream = classLoader.getResourceAsStream("client-config.properties");
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Node node = new Node(username,properties);
            Thread.sleep(10);
            Thread worker = new Thread(new Worker(node));
            worker.start();
            node.register();

            //check whether node is actually registered
            int count =0;
            while (true) {
                Thread.sleep(500);
                if (node.isRegistered()) {
                    System.out.println("Node registered");
                    break;
                } else {
                    if(count>4) {
                        System.out.println("can not register.. quitting");
                        System.exit(1);
                    }
                    count++;
                    continue;
                }
            }

            //Registered
            //Start Joining

            node.join();
            Thread.sleep(2000);




        } catch (SocketException e) {
            e.printStackTrace();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

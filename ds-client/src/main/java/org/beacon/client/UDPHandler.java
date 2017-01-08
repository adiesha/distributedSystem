package org.beacon.client;

import org.beacon.client.Message.Message;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * Created by Adiesha on 2017-01-05.
 */
public class UDPHandler {
    private String bsIp;
    private InetAddress bootstrapServerIp = null;
    private int bsPort;
    private int nodePort;
    Random random = null;
    int portMax;
    int portMin;
    private Properties properties;
    private DatagramSocket datagramSocket = null;
    private NetworkInterface lo = null;
    private NetworkInterface wifi = null;
    private InetAddress localIp;

    //private InetAddress lo = NetworkInterface.getByIndex();
    //private DatagramPacket datagramPacket = null;

    public UDPHandler(Properties properties) throws SocketException, UnknownHostException {
        this.properties = properties;
        lo = NetworkInterface.getByName("lo");
        wifi = NetworkInterface.getByName("wlan0");
        configData();
    }

    private void configData() throws SocketException, UnknownHostException {
        random = new Random(new Date().getTime());
        portMax = Integer.parseInt(properties.getProperty("client-port-max"));
        portMin = Integer.parseInt(properties.getProperty("client-port-min"));
        nodePort = portMin + random.nextInt(portMax - portMin);
        bsIp = properties.getProperty("bootstrap-server-ip");
        this.bootstrapServerIp = InetAddress.getByName(bsIp);
        bsPort = Integer.parseInt(properties.getProperty("bootstrap-server-port"));
        System.out.println(nodePort);
        datagramSocket = new DatagramSocket(nodePort);
        localIp = InetAddress.getLocalHost();

    }

    public boolean sendMessage(String message, InetAddress inetAddress, int port) throws IOException {
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.getBytes().length, inetAddress, port);
        datagramSocket.send(packet);
        System.out.println("message sent");
        return true;
    }

    public Message receiveMessage(int timeout) throws IOException, SocketTimeoutException {
        byte[] buffer = new byte[65536];
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(incoming);
        System.out.println("message received");
        byte[] data = incoming.getData();
        String s = new String(data, 0, incoming.getLength());
        System.out.println(s);
        Message message = new Message(s,incoming.getAddress(),incoming.getPort());

        return message;
    }

    public InetAddress getLoAddressipv4() {
        return lo.getInetAddresses().nextElement();
    }

    public InetAddress getWifiInterfaceAddressipv4() {
        return wifi.getInetAddresses().nextElement();
    }

    public int getNodePort() {
        return nodePort;
    }

    public InetAddress getBootstrapServerIp() {
        return bootstrapServerIp;
    }

    public int getBsPort() {
        return bsPort;
    }

    public InetAddress getLocalIp() {
        return localIp;
    }
}

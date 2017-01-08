package org.beacon.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Created by Adiesha on 2017-01-01.
 */
public class Node {
    private String userName = "";
    private ArrayList<Neighbour> neighbours = null;
    private UDPHandler udpHandler = null;
    private InetAddress nodeAddress = null;
    private MessageEncoder messageEncoder = null;
    private Neighbour[] tempNeighbours;
    private boolean isRegistered = false;

    //remember to set true for the first node
    private boolean isJoined = false;

    public Node(String username, Properties properties) throws SocketException, UnknownHostException {
        this.userName = username;
        udpHandler = new UDPHandler(properties);
        this.nodeAddress = udpHandler.getLoAddressipv4();
        this.messageEncoder = new MessageEncoder(properties);
        this.neighbours = new ArrayList<Neighbour>();
    }

    public Node(String userName) {
        this.userName = userName;
        this.neighbours = new ArrayList<Neighbour>();
    }

    public String getUserName() {
        return userName;
    }

    public ArrayList<Neighbour> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour(Neighbour neighbour) {
        if (getNeighbours().contains(neighbour)) {
            return;
        } else {
            getNeighbours().add(neighbour);
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private void configData() throws SocketException {
    }

    public UDPHandler getUdpHandler() {
        return udpHandler;
    }

    public boolean register() throws IOException {
        this.udpHandler.sendMessage(messageEncoder.encodeRegisterMessage(getUserName(), getUdpHandler().getLoAddressipv4(), getUdpHandler().getNodePort()), getUdpHandler().getBootstrapServerIp(), getUdpHandler().getBsPort());
        return true;
    }



    /***
     * Invoke this method when register response from BS is received
     *
     * @return
     * @throws IOException
     */
    public boolean registerResponse(String message) throws IOException {
        Neighbour[] neighbours = extractRegisterMessage(message);
        this.tempNeighbours = neighbours;
        return true;
    }

    public boolean unregister() throws IOException {
        this.udpHandler.sendMessage(messageEncoder.encodeUnregisterMessage(getUserName(), nodeAddress, this.udpHandler.getNodePort()), this.udpHandler.getBootstrapServerIp(), this.udpHandler.getBsPort());
        this.udpHandler.receiveMessage(2000);
        return true;
    }

    /***
     * Invoke this method to send join messages to neighbours receive from registering
     *
     * @return
     * @throws IOException
     */
    public boolean join() throws IOException {
        getUdpHandler().sendMessage(messageEncoder.encodeJoinMessage(nodeAddress, getUdpHandler().getNodePort()), getUdpHandler().getBootstrapServerIp(), getUdpHandler().getBsPort());
        getUdpHandler().receiveMessage(2000);
        return true;
    }

    /***
     * Invoke this method when there is a response coming from Join message
     *
     * @return
     * @throws IOException
     */
    public boolean joinResponse() throws IOException {
        getUdpHandler().sendMessage(messageEncoder.encodeJoinResponse(), getUdpHandler().getBootstrapServerIp(), getUdpHandler().getBsPort());
        getUdpHandler().receiveMessage(2000);
        return true;
    }

    /***
     * Invoke this method when there is Join response from another node in DS
     *
     * @return
     */
    public boolean generateJoinResponseForNode() {
        return false;
    }

    public boolean search() {
        return false;
    }

    public boolean searchResponse() {
        return false;
    }

    public void addNeighbour(InetAddress neighbourAddress, int neighbourPort) {

    }

    public Neighbour[] extractRegisterMessage(String message) throws UnknownHostException {
        StringTokenizer st = new StringTokenizer(message, " ");

        String length = st.nextToken();
        String command = st.nextToken();
        int numOfNeighbours = Integer.parseInt(st.nextToken());
        if (numOfNeighbours == 0) {
            this.setRegistered(true);
            return null;
        } else if(numOfNeighbours>2) {
            this.setRegistered(false);
            return null;
        }
        Neighbour[] neighbours = new Neighbour[numOfNeighbours];
        for (int i = 0; i < numOfNeighbours; i++) {
            String neighbourIp = st.nextToken();
            int neighbourPort = Integer.parseInt(st.nextToken());
            neighbours[i] = new Neighbour(InetAddress.getByName(neighbourIp), neighbourPort);
        }

        return neighbours;
    }

    public boolean isRegistered() {
        return this.isRegistered;
    }

    public boolean isJoined() {
        return this.isJoined;
    }

    public void setRegistered(boolean check) {
        this.isRegistered = check;
    }

    public void setJoined(boolean check) {
        this.isJoined = check;
    }


}


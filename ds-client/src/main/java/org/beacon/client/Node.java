package org.beacon.client;

import org.beacon.client.Message.Message;

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
        this.nodeAddress = udpHandler.getLocalIp();
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
        this.udpHandler.sendMessage(messageEncoder.encodeRegisterMessage(getUserName(), getUdpHandler().getLocalIp(), getUdpHandler().getNodePort()), getUdpHandler().getBootstrapServerIp(), getUdpHandler().getBsPort());
        return true;
    }


    /***
     * Invoke this method when register response from BS is received
     *
     * @return
     * @throws IOException
     */
    public boolean registerResponse(String message) throws IOException {
        //method will take care
        Neighbour[] neighbours = extractRegisterMessage(message);
        this.tempNeighbours = neighbours;
        return true;
    }

    public boolean unregister() throws IOException {
        this.udpHandler.sendMessage(messageEncoder.encodeUnregisterMessage(getUserName(), getUdpHandler().getLocalIp(), this.udpHandler.getNodePort()), this.udpHandler.getBootstrapServerIp(), this.udpHandler.getBsPort());
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
        int numOfNeighbours = this.getNoOfTempNeighbours();
        this.printStatus();
        for (int i = 0; i < numOfNeighbours; i++) {
            getUdpHandler().sendMessage(messageEncoder.encodeJoinMessage(getUdpHandler().getLocalIp(), getUdpHandler().getNodePort()), getTempNeighbours()[i].getAddress(), getTempNeighbours()[i].getPort());
        }
        return true;
    }

    /***
     * Invoke this method when there is a response coming from Join message
     *
     * @return
     * @throws IOException
     */
    public boolean joinResponse(Message response) throws IOException {
        this.extractJoinResponse(response);
        return true;
    }

    /***
     * Invoke this method when there is Join response from another node in DS
     *
     * @return
     */
    public boolean generateJoinRequestResponseForNode(Message message) throws IOException{
        String response = this.extractJoinRequestResponse(message);
        getUdpHandler().sendMessage(response,message.getAddress(),message.getPort());
        return true;
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
            this.tempNeighbours = null;
            return null;
        } else if (numOfNeighbours > 2) {
            this.setRegistered(false);
            this.tempNeighbours = null;
            return null;
        } else {
            this.setRegistered(true);
        }
        Neighbour[] neighbours = new Neighbour[numOfNeighbours];
        for (int i = 0; i < numOfNeighbours; i++) {
            String neighbourIp = st.nextToken();
            int neighbourPort = Integer.parseInt(st.nextToken());
            neighbours[i] = new Neighbour(InetAddress.getByName(neighbourIp), neighbourPort);
        }
        this.tempNeighbours = neighbours;
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

    public void printStatus() {
        System.out.println(this.userName);
        System.out.println(getUdpHandler().getNodePort());
        System.out.println(getUdpHandler().getLocalIp());
        System.out.println(this.neighbours.size() + " Number of Neighbours got from BS");
    }

    private int getNoOfTempNeighbours() {
        if (this.tempNeighbours == null) return 0;
        else return this.tempNeighbours.length;
    }

    public Neighbour[] getTempNeighbours() {
        return tempNeighbours;
    }

    public void extractJoinResponse(Message response) {
        StringTokenizer st = new StringTokenizer(response.getMessage(), " ");

        String length = st.nextToken();
        String command = st.nextToken();
        int responseCode = Integer.parseInt(st.nextToken());
        if (responseCode==0) {
            Neighbour neighbour = new Neighbour(response.getAddress(),response.getPort());
            this.addNeighbour(neighbour);
            this.setJoined(true);
            System.out.println("One join response was successful");
        } else {
            System.out.println("One Join response was failed");
        }
    }

    public Neighbour extractJoinRequest(String message) throws UnknownHostException {
        StringTokenizer st = new StringTokenizer(message, " ");
        String length = st.nextToken();
        String command = st.nextToken();

        String hostName = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        Neighbour neighbour = new Neighbour(InetAddress.getByName(hostName),port);
        return neighbour;
    }

    public String extractJoinRequestResponse(Message message) throws UnknownHostException {
        StringTokenizer st = new StringTokenizer(message.getMessage()," ");
        String length = st.nextToken();
        String command = st.nextToken();
        String hostName = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        Neighbour neighbour = new Neighbour(InetAddress.getByName(hostName),port);

        Integer lock = new Integer(0);
        String response;
        synchronized(lock){
            if(this.getNeighbours().contains(neighbour)) {
                response = MessageType.JOINOK.toString() + " 9999";
                response = String.format("%04d "+response,response.getBytes().length +5);
            } else {
                this.addNeighbour(neighbour);
                response = MessageType.JOINOK.toString() + " 0";
                response = String.format("%04d "+response,response.getBytes().length +5);
            }

            return response;
        }

    }
}


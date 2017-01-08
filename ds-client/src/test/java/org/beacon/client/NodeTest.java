package org.beacon.client;

import junit.framework.TestCase;

import java.net.InetAddress;
import java.util.Properties;

/**
 * Created by Adiesha on 2017-01-08.
 */
public class NodeTest extends TestCase {
    Node node;
    Node node1;
    public void setUp() throws Exception {
        super.setUp();
        node =new Node("testNode");
        node1 = new Node("testNode2");
    }

    public void testExtractRegisterMessage() throws Exception {
        Neighbour[] neighbours = node.extractRegisterMessage("0028 REGOK 1 127.0.0.1 49306");
        assertEquals(1,neighbours.length);
        Neighbour[] neighbours2 = node1.extractRegisterMessage("0044 REGOK 2 127.0.0.1 47422 127.0.0.1 27558");
        assertEquals(2,neighbours2.length);
        Node node2 = new Node("testNode");
        node2.addNeighbour(new Neighbour(InetAddress.getByName("127.0.0.1"),49306));
        System.out.println(node);
    }
}
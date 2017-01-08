package org.beacon.client;

import junit.framework.TestCase;

import java.net.InetAddress;

/**
 * Created by Adiesha on 2017-01-07.
 */
public class NeighbourTest extends TestCase {
    Neighbour neighbour;
    Neighbour neighbour1;
    Neighbour neighbour2;
    Neighbour neighbour3;
    public void setUp() throws Exception {
        super.setUp();
        neighbour = new Neighbour(InetAddress.getLocalHost(),32322);
        neighbour1 = new Neighbour(InetAddress.getByName("127.0.0.1"),32322);
        neighbour2 = new Neighbour(InetAddress.getByName("127.0.0.1"),32322);
        neighbour3 = new Neighbour(InetAddress.getByName("127.0.0.1"),32323);
    }

    public void testEquals() throws Exception {
        //System.out.println(neighbour);
        System.out.println(neighbour.equals(neighbour1));
        System.out.println(neighbour.getAddress());
        System.out.println(neighbour1.getAddress());
        System.out.println(InetAddress.getLoopbackAddress());
        assertEquals(false,neighbour.equals(neighbour1));
        assertEquals(true,neighbour1.equals(neighbour2));
        assertEquals(false,neighbour1.equals(neighbour3));
    }
}
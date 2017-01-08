package org.beacon.client;

import java.net.InetAddress;

/**
 * Created by Adiesha on 2017-01-07.
 */
public class Neighbour {
    private InetAddress address;
    private int port;

    public Neighbour(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public boolean equals(Object o) {
        if(o == null) return false;
        if(!(o instanceof Neighbour)) return false;

        final Neighbour neighbour = (Neighbour) o;
        if(this.getAddress().equals(neighbour.getAddress()) && this.getPort()==neighbour.getPort()) {
            return true;
        }
        else return false;

    }
}

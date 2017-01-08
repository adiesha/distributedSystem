package org.beacon.client;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.net.InetAddress;
import java.util.Properties;

/**
 * Created by Adiesha on 2017-01-06.
 */
public class MessageEncoderTest extends TestCase {

    public void testEncodeRegisterMessage() throws Exception {
        MessageEncoder messageEncoder = new MessageEncoder(new Properties());
        assertEquals("0032 REG 127.0.0.1 25652 adiesha", messageEncoder.encodeRegisterMessage("adiesha", InetAddress.getByName("127.0.0.1"), 25652));
    }

    public void testEncodeUnregisterMessage() throws Exception {
        MessageEncoder messageEncoder = new MessageEncoder(new Properties());
        assertEquals("0034 UNREG 127.0.0.1 25652 adiesha", messageEncoder.encodeUnregisterMessage("adiesha", InetAddress.getByName("127.0.0.1"), 25652));
    }


    public void testEncodeJoinMessage() throws Exception {
        MessageEncoder messageEncoder = new MessageEncoder(new Properties());
        assertEquals("0025 JOIN 127.0.0.1 32322",messageEncoder.encodeJoinMessage(InetAddress.getByName("127.0.0.1"),32322));
        InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
        InetAddress inetAddress1 = InetAddress.getByName("127.0.0.1");
        assertEquals(true,inetAddress.equals(inetAddress1));
        assertEquals(true,inetAddress1.equals(inetAddress));
    }

    public void testEncodeJoinResponse() throws Exception {

    }
}
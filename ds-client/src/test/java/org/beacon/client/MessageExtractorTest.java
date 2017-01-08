package org.beacon.client;

import junit.framework.TestCase;

/**
 * Created by Adiesha on 2017-01-08.
 */
public class MessageExtractorTest extends TestCase {

    MessageExtractor messageExtractor = null;
    public void setUp() throws Exception {
        super.setUp();
        messageExtractor = new MessageExtractor();
    }

    public void testGetMessageType() throws Exception {
        assertEquals("JOIN","JOIN");
        assertEquals("JOIN",messageExtractor.getMessageType("0032 JOIN").toString());

    }
}
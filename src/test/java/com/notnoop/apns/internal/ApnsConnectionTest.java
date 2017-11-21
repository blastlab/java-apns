package com.notnoop.apns.internal;

import java.io.ByteArrayOutputStream;
import javax.net.SocketFactory;
import com.notnoop.apns.SimpleApnsNotification;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import static com.notnoop.apns.internal.MockingUtils.*;


@SuppressWarnings("deprecation")
public class ApnsConnectionTest {
    private SimpleApnsNotification msg = new SimpleApnsNotification ("a87d8878d878a79", "{\"aps\":{}}");

    @Test
    public void simpleSocket() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SocketFactory factory = mockSocketFactory(baos, null);
        packetSentRegardless(factory, baos);
    }

    @Test
    @Ignore
    public void closedSocket() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SocketFactory factory = mockClosedThenOpenSocket(baos, null, true, 1);
        packetSentRegardless(factory, baos);
    }

    /**
     * Connection fails after first fail
     */
    @Test(expected = Exception.class)
    public void error() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SocketFactory factory = mockClosedThenOpenSocket(baos, null, false, 1);
        packetSentRegardless(factory, baos);
    }

    private void packetSentRegardless(SocketFactory sf, ByteArrayOutputStream baos) {
        ApnsConnectionImpl connection = new ApnsConnectionImpl(sf, "localhost", 80);
        connection.sendMessage(msg);
        Assert.assertArrayEquals(msg.marshall(), baos.toByteArray());
        connection.close();
    }
}

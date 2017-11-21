package com.notnoop.apns.internal;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.net.SocketFactory;

import org.junit.Test;

import static com.notnoop.apns.internal.ApnsFeedbackParsingUtils.*;
import static com.notnoop.apns.internal.MockingUtils.mockClosedThenOpenSocket;

public class ApnsFeedbackConnectionTest {

    InputStream simpleStream = new ByteArrayInputStream(simple);
    InputStream threeStream = new ByteArrayInputStream(three);

    /** Simple Parsing **/
    @Test
    public void rowParseOneDevice() {
        checkRawSimple(Utilities.parseFeedbackStreamRaw(simpleStream));
    }

    @Test
    public void threeParseTwoDevices() {
        checkRawThree(Utilities.parseFeedbackStreamRaw(threeStream));
    }

    @Test
    public void parsedSimple() {
        checkParsedSimple(Utilities.parseFeedbackStream(simpleStream));
    }

    @Test
    public void parsedThree() {
        checkParsedThree(Utilities.parseFeedbackStream(threeStream));
    }

    /** With Connection **/
    @Test
    public void connectionParsedOne() {
        SocketFactory sf = MockingUtils.mockSocketFactory(null, simpleStream);
        ApnsFeedbackConnection connection = new ApnsFeedbackConnection(sf, "localhost", 80);
        checkParsedSimple(connection.getInactiveDevices());
    }

    @Test
    public void connectionParsedThree() {
        SocketFactory sf = MockingUtils.mockSocketFactory(null, threeStream);
        ApnsFeedbackConnection connection = new ApnsFeedbackConnection(sf, "localhost", 80);
        checkParsedThree(connection.getInactiveDevices());
    }

    /**
     * Connection fails after first fail
     */
    @Test(expected = Exception.class)
    public void feedbackWithError() {
        SocketFactory sf = mockClosedThenOpenSocket(null, simpleStream, true, 1);
        ApnsFeedbackConnection connection = new ApnsFeedbackConnection(sf, "localhost", 80);
        checkParsedSimple(connection.getInactiveDevices());
    }

}

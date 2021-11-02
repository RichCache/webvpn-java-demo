package com.richctrl.webvpn;

import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class WebVPNDemoTest {
    private static final Logger LOGGER = LogManager.getLogger();

    @Test
    public void userExists() throws Exception {
        WebVPNConnector connector = new WebVPNConnector("secret-key", "http://webvpn.api:2080");

        ConnectorResponseDTO responseDTO = connector.getApi(
            WebVPNConnector.USER_EXISTS_API,
            ImmutableMap.of(
            "username", "admin"
        ));
        LOGGER.info("result = {}", responseDTO);
    }
}
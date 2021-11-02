package com.richctrl.webvpn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;

public class WebVPNDemoTest {
    private static final Logger LOGGER = LogManager.getLogger();

    @Test
    public void userExists() throws Exception {
        WebVPNConnector connector = new WebVPNConnector("secret-key", "http://webvpn.api:2080");

        ConnectorResponseDTO responseDTO = connector.getApi(
            WebVPNConnector.USER_EXISTS_API,
            new HashMap<String, Object>(){{
                put("username", "admin");
            }});
        LOGGER.info("result = {}", responseDTO);
    }
}
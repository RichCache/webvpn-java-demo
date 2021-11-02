package com.richctrl.webvpn;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class WebVPNDemoTest {
    private static final Logger LOGGER = LogManager.getLogger();

    @Test
    public void userExists() throws Exception {
        WebVPNDemo webVPNDemo = new WebVPNDemo("secret-key", "http://webvpn.api:2080");

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "admin");
        String jwt = webVPNDemo.createJWT(claims);
        LOGGER.info("jwt = `{}`", jwt);

        ConnectorResponseDTO responseDTO = webVPNDemo.getApi(WebVPNDemo.USER_EXISTS_API, jwt);
        LOGGER.info("result = {}", responseDTO);
    }
}
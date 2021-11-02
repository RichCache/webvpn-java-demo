package com.richctrl.webvpn;

import com.google.gson.Gson;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;

public class WebVPNConnector {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private final Key signingKey;
    private final String endpoint;

    public WebVPNConnector(String secretKey, String endpoint) {
         this.signingKey = new SecretKeySpec(secretKey.getBytes(), SIGNATURE_ALGORITHM.getJcaName());
         this.endpoint = endpoint;
    }

    public String createJWT(Map<String, Object> claims) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .signWith(SIGNATURE_ALGORITHM, this.signingKey);

        return builder.compact();
    }

    private ConnectorResponseDTO parseResponse(HttpResponse response) throws ConnectorException, IOException {
        String json = EntityUtils.toString(response.getEntity());

        ConnectorResponseDTO responseDTO = new Gson().fromJson(json, ConnectorResponseDTO.class);
        if (!"success".equals(responseDTO.getStatus())) {
            throw new Gson().fromJson(json, ConnectorException.class);
        }

        return responseDTO;
    }

    public static final String USER_EXISTS_API = "/api/connector/user/exists";

    public ConnectorResponseDTO postApi(String api, String jwt) throws IOException, ConnectorException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(this.endpoint + api);
        HttpEntity entity = new ByteArrayEntity(jwt.getBytes(StandardCharsets.UTF_8));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);

        return parseResponse(response);
    }

    public ConnectorResponseDTO getApi(String api, String jwt) throws IOException, URISyntaxException, ConnectorException {
        HttpClient client = HttpClientBuilder.create().build();
        URIBuilder builder = new URIBuilder(this.endpoint + api);
        builder.setParameter("payload", jwt);
        HttpGet get = new HttpGet(builder.build());
        HttpResponse response = client.execute(get);

        return parseResponse(response);
    }

    public ConnectorResponseDTO getApi(String api, Map<String, Object> claims) throws IOException, URISyntaxException, ConnectorException {
        return getApi(api, createJWT(claims));
    }

    public ConnectorResponseDTO postApi(String api, Map<String, Object> claims) throws IOException, ConnectorException {
        return postApi(api, createJWT(claims));
    }
}

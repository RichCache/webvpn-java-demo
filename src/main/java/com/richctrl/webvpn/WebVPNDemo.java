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

public class WebVPNDemo {
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private final Key signingKey;
    private final String endpoint;

    public WebVPNDemo(String secretKey, String endpoint) {
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

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static final String USER_EXISTS_API = "/api/connector/user/exists";

    public ConnectorResponseDTO postApi(String api, String jwt) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(this.endpoint + api);
        HttpEntity entity = new ByteArrayEntity(jwt.getBytes(StandardCharsets.UTF_8));
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        String json = EntityUtils.toString(response.getEntity());
        return new Gson().fromJson(json, ConnectorResponseDTO.class);
    }

    public ConnectorResponseDTO getApi(String api, String jwt) throws IOException, URISyntaxException {
        HttpClient client = HttpClientBuilder.create().build();
        URIBuilder builder = new URIBuilder(this.endpoint + api);
        builder.setParameter("payload", jwt);
        HttpGet get = new HttpGet(builder.build());
        HttpResponse response = client.execute(get);

        String json = EntityUtils.toString(response.getEntity());
        return new Gson().fromJson(json, ConnectorResponseDTO.class);
    }
}

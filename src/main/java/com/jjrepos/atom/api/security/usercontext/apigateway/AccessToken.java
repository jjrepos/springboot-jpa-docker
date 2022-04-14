package com.jjrepos.atom.api.security.usercontext.apigateway;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * Mashery Access token with user, client information.
 *
 * @author ragarwal
 */
public class AccessToken {
    private static final Logger LOG = LoggerFactory.getLogger(AccessToken.class);

    private static final String JASON_CLIENT_ID = "client_id";
    private static final String JASON_ACCESS_TOKEN = "access_token";
    private static final String JASON_USERNAME = "username";
    private String username;
    private String clientId;

    /**
     * Parses the Api Gateway's Base64 encoded Json and creates an
     * {@link AccessToken} instance.
     *
     * @param encodedJson Mashery Base64 encoded Json
     * @return {@link AccessToken}
     */
    public static AccessToken parse(String encodedJson) {
        AccessToken token = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedJson);
            String json = new String(decodedBytes);
            LOG.trace("Gateway header Json: {}", json);
            try (InputStream is = new ByteArrayInputStream(json.getBytes());
                 JsonParser parser = Json.createParser(is)) {
                token = new AccessToken();
                while (parser.hasNext()) {
                    Event e = parser.next();
                    if (e == Event.KEY_NAME) {
                        switch (StringUtils.lowerCase(parser.getString())) {
                            case JASON_CLIENT_ID:
                                parser.next();
                                token.setClientId(parser.getString());
                                break;
                            case JASON_ACCESS_TOKEN:
                                parser.next();
                                break;
                            case JASON_USERNAME:
                                parser.next();
                                token.setUsername(parser.getString());
                                break;
                        }
                    }
                }
            }
        } catch (Throwable t) {
            throw new AccessTokenParsingException("Error occured parsing API Gateway header", t);
        }
        return token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AccessToken [username=").append(username).append(", clientId=").append(clientId).append("]");
        return builder.toString();
    }
}


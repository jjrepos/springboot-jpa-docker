package com.jjrepos.atom.api.security.usercontext.apigateway;

import com.jjrepos.atom.api.security.usercontext.UserContext;
import com.jjrepos.atom.api.security.usercontext.UserContextProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Looks for the HTTP header 'X-Mashery-Handshake' from Api Gateway if present,
 * decoded string is parsed to retrieve {@link UserContext} information. The
 * Mashery UserContext contains client id and username, if the request is of
 * type resource owner Oauth flow.
 *
 * @author Justin Jesudass
 */

@Component
public class ApiGatewayUserContextProvider implements UserContextProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ApiGatewayUserContextProvider.class);

    private static final String GATEWAY_HANDSHAKE_HDR = "X-AUTHENTICATION-Handshake";
    private static final String AUTHORIZATION_HDR = "Authorization";

    @Override
    public Optional<UserContext> getUserContext(HttpServletRequest request) {

        String encodedJson = request.getHeader(GATEWAY_HANDSHAKE_HDR);

        UserContext userContext = null;
        LOG.debug("Gateway handshake header: {}", encodedJson);
        if (StringUtils.isBlank(encodedJson)) {
            LOG.warn("No Gateway Handshake Header found");
            return Optional.empty();
        }
        AccessToken accessToken = null;
        try {
            accessToken = AccessToken.parse(encodedJson);
        } catch (AccessTokenParsingException e) {
            LOG.error(e.getMessage(), e);
            return Optional.empty();
        }
        LOG.trace("Parsed access token: {}", accessToken);

        String authHeader = request.getHeader(AUTHORIZATION_HDR);
        String token = StringUtils.removeStartIgnoreCase(authHeader, "Bearer ");
        String userAgent = request.getParameter("User-Agent");
        userContext = new UserContext(accessToken.getUsername(), accessToken.getClientId(), token, userAgent);
        return Optional.of(userContext);
    }

}

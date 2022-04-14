package com.jjrepos.atom.api.security.usercontext.header;

import com.jjrepos.atom.api.security.usercontext.UserContext;
import com.jjrepos.atom.api.security.usercontext.UserContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Looks for the HTTP header 'X-USERNAME' if present, decoded string is parsed
 * to retrieve {@link UserContext} information.
 *
 * @author Justin Jesudass
 */

@Component
public class HeaderUserContextProvider implements UserContextProvider {

    private static final Logger LOG = LoggerFactory.getLogger(HeaderUserContextProvider.class);

    private static final String USER_NAME_HEADER = "X-Username";

    @Override
    public Optional<UserContext> getUserContext(HttpServletRequest request) {
        String username = request.getParameter(USER_NAME_HEADER);
        LOG.trace("User name header: {}", username);
        String userAgent = request.getParameter("User-Agent");
        if (username == null || username.length() == 0) {
            return Optional.empty();
        }
        return Optional.of(new UserContext(username).setUserAgent(userAgent));
    }

}
package com.jjrepos.atom.api.security.usercontext;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface UserContextProvider {

    /**
     * Extracts the {@link UserContext } from a resource request and returns the {@link UserConext} associated with the
     * request.
     *
     * @param request {@link HttpServletRequest}
     * @return {@link UserConext} if it could be extracted
     */
    Optional<UserContext> getUserContext(HttpServletRequest request);
}

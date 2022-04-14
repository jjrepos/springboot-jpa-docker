package com.jjrepos.atom.api.security;

import com.jjrepos.atom.api.security.usercontext.UserContextManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Resource Owner Authorization Provider authorizes the request coming in.
 * If the incoming request is not authorized to access the resource, {@link NotAuthorizedException}.
 *
 * @author justinjesudass
 */
public interface ResourceOwnerAuthorizationProvider {
    void authorize(HttpServletRequest request) throws NotAuthorizedException;

    /**
     * validates if the caller {@link UserContextManager#getUserContext()} is the owner of the resource. The default
     * implementation returns false, in case no implementation is provided.
     *
     * @param requestContext {@link HttpServletRequest}
     * @return true if the user contained in the {@link UserContextManager#getUserContext()} is the resource's owner,
     * false otherwise.
     */
    boolean isResourceOwner(HttpServletRequest request);
}

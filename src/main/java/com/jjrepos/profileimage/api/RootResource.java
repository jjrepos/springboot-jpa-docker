package com.jjrepos.profileimage.api;

/**
 * Root resource simply to mark the root of the API.
 *
 * @author Seth Jordan
 */

public interface RootResource extends Resource {

    /**
     * Gets a welcome message.
     *
     * @return welcome message
     */
    public String getWelcomeMessage();
}

package com.jjrepos.profileimage.core.service;

/**
 * Exception that is thrown when the action is not authorized by the user.
 *
 * @author sjordan
 */
public class AuthorizationException extends RuntimeException {
    private static final long serialVersionUID = -1103672671466047834L;

    /**
     * Creates the exception with a message.
     *
     * @param message message
     */
    public AuthorizationException(String message) {
        super(message);
    }

    /**
     * Creates the exception with a message.
     *
     * @param message message
     * @param thr     chained exception
     */
    public AuthorizationException(String message, Throwable thr) {
        super(message, thr);
    }

}

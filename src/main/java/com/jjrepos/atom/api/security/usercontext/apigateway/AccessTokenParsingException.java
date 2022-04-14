package com.jjrepos.atom.api.security.usercontext.apigateway;

/**
 * Indicates that some exception occurred during Access Token {@link AccessToken} parsing.
 *
 * @author Justin Jesudass
 */
public class AccessTokenParsingException extends RuntimeException {

    private static final long serialVersionUID = 1364949798817254061L;

    /**
     * Constructs a new runtime exception with the specified detail message. The cause is not initialized, and may
     * subsequently be initialized by a call to java.lang.Throwable.initCause(java.lang.Throwable).
     *
     * @param message message the detail message. The detail message is saved for later retrieval by the
     *                java.lang.Throwable.getMessage() method.
     */
    public AccessTokenParsingException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and cause.
     * <p>
     * Note that the detail message associated with cause is not automatically incorporated in this runtime exception's
     * detail message.
     *
     * @param message he detail message (which is saved for later retrieval by the java.lang.Throwable.getMessage()
     *                method).
     * @param cause   the cause (which is saved for later retrieval by the java.lang.Throwable.getCause() method). (A null
     *                value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public AccessTokenParsingException(String message, Throwable cause) {
        super(message, cause);
    }

}


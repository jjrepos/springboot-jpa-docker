package com.jjrepos.atom.api.security.usercontext;

/**
 * Manages the{@link UserContext} using a ThreadLocal.
 *
 * @author Justin Jesudass
 */
public enum UserContextManager {

    INSTANCE;

    private static final ThreadLocal<UserContext> CONTEXT = new ThreadLocal<>();

    /**
     * Sets the {@link UserContext}
     *
     * @param ctx {@link UserContext}
     */
    public static void setUserContext(UserContext ctx) {
        CONTEXT.set(ctx);
    }

    /**
     * Gets the user context.
     *
     * @return {@link UserContext}
     */
    public static UserContext get() {
        return CONTEXT.get();
    }

    /**
     * Removes the {@link UserContext}
     */
    public static void removeUserContext() {
        CONTEXT.remove();
    }

}

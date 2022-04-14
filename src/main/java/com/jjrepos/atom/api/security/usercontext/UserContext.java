package com.jjrepos.atom.api.security.usercontext;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * UserContext contains a User's contextual information such as user name and
 * client info
 *
 * @author Justin Jesudass
 */
public class UserContext {

    private String username;
    private String clientId;
    private boolean impersonatedUser;
    private String oauthToken;
    private String userAgent;

    /**
     * Over loaded constructor
     *
     * @param username username
     */
    public UserContext(String username) {
        this.username = username;
    }

    /**
     * Default Constructor.
     */
    public UserContext() {
    }

    /**
     * Initializer with username and client Identifiers.
     *
     * @param username employee Identifier
     * @param clientId client identifier
     */
    public UserContext(String username, String clientId) {
        this.username = username;
        this.clientId = clientId;
    }

    /**
     * Initializer with username and client Identifiers.
     *
     * @param username   employee Identifier
     * @param clientId   client identifier
     * @param oauthToken token
     */
    public UserContext(String username, String clientId, String oauthToken) {
        this.username = username;
        this.clientId = clientId;
        this.oauthToken = oauthToken;
    }

    public UserContext(String username, String clientId, String oauthToken, String userAgent) {
        super();
        this.username = username;
        this.clientId = clientId;
        this.oauthToken = oauthToken;
        this.userAgent = userAgent;
    }

    /**
     * Initializer with username and if the user id is impersonated.
     *
     * @param username         employee Identifier
     * @param impersonatedUser indicator if the user is impersonated by the caller.
     */
    public UserContext(String username, boolean impersonatedUser) {
        this.username = username;
        this.impersonatedUser = impersonatedUser;
    }

    /**
     * Initializer with username and client Id and if the user id is impersonated.
     *
     * @param username         employee Identifier
     * @param clientId         client identifier
     * @param impersonatedUser indicator if the user is impersonated by the caller.
     */
    public UserContext(String username, String clientId, boolean impersonatedUser) {
        this(username, clientId);
        this.impersonatedUser = impersonatedUser;
    }

    public String getUsername() {
        return username;
    }

    public UserContext setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public UserContext setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public UserContext setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
        return this;
    }

    public boolean isUserValid() {
        return StringUtils.isNotEmpty(username);
    }

    public boolean isUserInvalid() {
        return StringUtils.isEmpty(username);
    }

    public boolean isImpersonatedUser() {
        return impersonatedUser;
    }

    /**
     * Sets the impersonated user name.
     *
     * @param username username
     */
    public UserContext withImpersonatedUserName(String username) {
        if (StringUtils.isNotEmpty(username)) {
            this.username = username;
            this.impersonatedUser = true;
        }
        return this;
    }


    public String getUserAgent() {
        return userAgent;
    }

    public UserContext setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, clientId, impersonatedUser);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        UserContext other = (UserContext) obj;
        return Objects.equals(this.username, other.username) && Objects.equals(this.clientId, other.clientId)
                && Objects.equals(this.impersonatedUser, other.impersonatedUser);
    }
}

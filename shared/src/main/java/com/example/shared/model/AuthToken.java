package com.example.shared.model;

import java.util.Objects;

public class AuthToken {
    /**
     * The unique user name
     */
    String userName;
    /**
     * The unique AuthToken for each User
     */
    String authorizationToken;

    /**
     * Creates a AuthToken object
     *
     * @param userName           The unique user name
     * @param authorizationToken The unique AuthToken for each User
     */
    public AuthToken(String userName, String authorizationToken) {
        this.userName = userName;
        this.authorizationToken = authorizationToken;
    }

    /**
     * Checks is an object is equal to this AuthToken
     *
     * @param o Object to check against
     * @return A boolean value of equivalency
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return userName.equals(authToken.userName) &&
                authorizationToken.equals(authToken.authorizationToken);
    }

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public String getUserName() {
        return userName;
    }

    /**
     * Generates a string value for the AuthToken object
     *
     * @return Returns a string value for the AuthToken object
     */
    @Override
    public String toString() {
        return "AuthToken{" +
                "userName='" + userName + '\'' +
                ", authorizationToken='" + authorizationToken + '\'' +
                '}';
    }

    /**
     * Generates a unique hash code for the AuthToken object
     *
     * @return A unique hash code of the AuthToken object
     */
    @Override
    public int hashCode() {
        return Objects.hash(userName, authorizationToken);
    }
}

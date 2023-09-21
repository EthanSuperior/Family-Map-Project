package com.example.shared.request;

public class LoginRequest {

    /**
     * The UserName of the User to Login
     */
    String userName;
    /**
     * The Password of the User to login
     */
    String password;

    /**
     * Creates a LoginRequest Object
     *
     * @param userName The UserName of the User to Login
     * @param password The Password of the User to login
     */
    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.example.shared.result;

public class RegisterResult extends ServiceResult {
    /**
     * The returned AuthTokenID of the new User
     */
    String authToken;
    /**
     * The UserName of the new User
     */
    String userName;
    /**
     * The PersonID of the new User
     */
    String personID;


    /**
     * Creates a successful RegisterResult Object
     *
     * @param newAuthToken A non-empty auth token string
     * @param userName     The userName passed in with the request
     * @param personID     The personID passed in with the request
     */
    public RegisterResult(String newAuthToken, String userName, String personID) {
        super("", true);
        authToken = newAuthToken;
        this.userName = userName;
        this.personID = personID;
    }

    /**
     * Creates a failed RegisterResult Object
     * <p>
     * Fails when request property missing or has invalid value, Username already taken by another user, Internal server error
     *
     * @param errorMessage A Description of the error
     */
    public RegisterResult(String errorMessage) {
        super(errorMessage, false);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}

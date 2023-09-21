package com.example.shared.request;


public class RegisterRequest {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * The new User's UserName
     */
    String userName;
    /**
     * The new User's Password
     */
    String password;
    /**
     * The new User's Email
     */
    String email;
    /**
     * The new User's FirstName
     */
    String firstName;
    /**
     * The new User's LastName
     */
    String lastName;
    /**
     * The new User's Gender
     */
    String gender;

    /**
     * Creates a new RegisterRequest Object
     *
     * @param newUserName  The new User's UserName, non-empty string
     * @param newPassword  The new User's Password, non-empty string
     * @param newEmail     The new User's Email, non-empty string
     * @param newFirstName The new User's First Name, non-empty string
     * @param newLastName  The new User's Last Name, non-empty string
     * @param newGender    The new User's gender, 'f' or 'm'
     */
    public RegisterRequest(String newUserName, String newPassword, String newEmail, String newFirstName, String newLastName, String newGender) {
        userName = newUserName;
        password = newPassword;
        email = newEmail;
        firstName = newFirstName;
        lastName = newLastName;
        gender = newGender;
    }

    public String getFirstName() {
        return firstName;
    }

}

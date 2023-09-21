package com.example.shared.model;

import java.util.Objects;

public class User {

    /**
     * Unique user name
     */
    private String userName;
    /**
     * The User's password
     */
    private String password;
    /**
     * The User's email address
     */
    private String email;
    /**
     * The User's first name
     */
    private String firstName;
    /**
     * The User's last name
     */
    private String lastName;
    /**
     * User's gender ("f" or "m")
     */
    private String gender;
    /**
     * Unique Person ID assigned to this user’s generated Person object
     */
    private String personID;

    /**
     * Creates a User object
     *
     * @param userName  The Unique user name
     * @param password  The User's password
     * @param email     The User's email address
     * @param firstName The User's first name
     * @param lastName  The User's last name
     * @param gender    User's gender ('f' or 'm')
     * @param personID  Unique Person ID assigned to this user’s generated Person object
     */
    public User(String userName, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    /**
     * Generates a string for the User object
     *
     * @return A string holding all the User's information
     */
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", personID='" + personID + '\'' +
                '}';
    }

    /**
     * Checks equivalency of this and another object
     *
     * @param o The object you want to see if this one equals
     * @return Returns true if it is equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getUserName().equals(user.getUserName()) &&
                getPassword().equals(user.getPassword()) &&
                getEmail().equals(user.getEmail()) &&
                getFirstName().equals(user.getFirstName()) &&
                getLastName().equals(user.getLastName()) &&
                gender.equals(user.getGender()) &&
                getPersonID().equals(user.getPersonID());
    }

    /**
     * Generates a unique hashCode for the User Object
     *
     * @return A unique hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getPassword(), getEmail(), getFirstName(), getLastName(), getGender(), getPersonID());
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }

}

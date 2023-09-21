package com.example.shared.model;

import java.util.Objects;

public class Person {
    /**
     * Unique identifier for this person
     */
    private String personID;
    /**
     * User (Username) to which this person belongs
     */
    private String associatedUsername;
    /**
     * Person’s first name
     */
    private String firstName;
    /**
     * Person’s last name
     */
    private String lastName;
    /**
     * Person’s gender ("f" or "m")
     */
    private String gender;
    /**
     * Person ID of person’s father
     */
    private String fatherID = null;
    /**
     * Person ID of person’s mother
     */
    private String motherID = null;
    /**
     * Person ID of person’s spouse
     */
    private String spouseID = null;
    /**
     * Creates a Person object
     *
     * @param personID           Unique identifier for this person
     * @param associatedUsername User (Username) to which this person belongs
     * @param firstName          Person’s first name
     * @param lastName           Person’s last name
     * @param gender             Person’s gender ('f' or 'm')
     * @param fatherID           Person ID of person’s father
     * @param motherID           Person ID of person’s mother
     * @param spouseID           Person ID of person’s spouse
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
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

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    /**
     * Generates a string value for the Person object
     *
     * @return Returns a string value for the Person object
     */
    @Override
    public String toString() {
        return "Person{" +
                "personID='" + personID + '\'' +
                ", associatedUsername='" + associatedUsername + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", fatherID='" + fatherID + '\'' +
                ", motherID='" + motherID + '\'' +
                ", spouseID='" + spouseID + '\'' +
                '}';
    }

    /**
     * Checks is an object is equal to this Person object
     *
     * @param o Object to check against
     * @return A boolean value of equivalency
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return getPersonID().equals(person.getPersonID()) &&
                getAssociatedUsername().equals(person.getAssociatedUsername()) &&
                getFirstName().equals(person.getFirstName()) &&
                getLastName().equals(person.getLastName()) &&
                getGender().equals(person.getGender()) &&
                Objects.equals(getFatherID(), person.getFatherID()) &&
                Objects.equals(getMotherID(), person.getMotherID()) &&
                Objects.equals(getSpouseID(), person.getSpouseID());
    }

    /**
     * Generates a unique hash code for the Person object
     *
     * @return A unique hash code of the Person object
     */
    @Override
    public int hashCode() {
        return Objects.hash(personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

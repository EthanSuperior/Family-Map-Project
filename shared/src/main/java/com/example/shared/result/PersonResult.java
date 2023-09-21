package com.example.shared.result;

public class PersonResult extends ServiceResult {
    String associatedUsername;
    String personID;
    String firstName;
    String lastName;
    String gender;
    String fatherID;
    String motherID;
    String spouseID;

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
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
     * Creates a successful GetPersonResult Object with relative's personIDs
     *
     * @param associatedUsername The name of user account this person belongs to
     * @param personID           The person’s unique ID
     * @param firstName          The person’s first name
     * @param lastName           The person’s last name
     * @param gender             The person’s gender ('m' or 'f')
     * @param fatherID           The person’s father's ID (optional)
     * @param motherID           The person’s mother's ID (optional)
     * @param spouseID           The person’s spouse's ID (optional)
     */
    public PersonResult(String associatedUsername, String personID, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        super("", true);
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    /**
     * Creates a failed GetPersonResult Object
     * <p>
     * Fails when there is an invalid auth token, Invalid personID parameter, the requested person does not belong to this user, or an Internal server error
     *
     * @param errorMessage A Description of the error
     */
    public PersonResult(String errorMessage) {
        super(errorMessage, false);
    }
}

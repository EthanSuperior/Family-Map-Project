package com.example.shared.result;


import com.example.shared.model.Person;

public class PersonsResult extends ServiceResult {

    Person[] data;

    /**
     * Creates a successful PersonResult Object
     *
     * @param personData An array of Person objects
     */
    public PersonsResult(Person[] personData) {
        super("", true);
        data = personData;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }

    /**
     * Creates a failed PersonResult Object
     * <p>
     * Fails with invalid auth token, or Internal server error
     *
     * @param errorMessage A Description of the error
     */
    public PersonsResult(String errorMessage) {
        super(errorMessage, false);
    }
}

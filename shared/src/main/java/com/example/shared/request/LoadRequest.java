package com.example.shared.request;

import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.example.shared.model.User;

import java.util.Arrays;

public class LoadRequest {

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    /**
     * Array of Users to be created
     */
    User[] users;
    /**
     * Array of People for those Users
     */
    Person[] persons;
    /**
     * Array of Events for those People
     */
    Event[] events;

    /**
     * Creates A LoadRequest Object
     * <p>
     * The objects contained in the 'persons' and 'events' arrays should be added to the server’s database.
     * The objects in the 'users' array have the same format as those passed to the /user/register API with the addition of the personID.
     * The objects in the 'persons' array have the same format as those returned by the /person/[personID] API.
     * The objects in the 'events' array have the same format as those returned by the /event/[eventID] API.
     *
     * @param users  Array of users to be created.
     * @param persons Family History People Information for these users
     * @param events Family History Event Information for these users
     */
    LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    @Override
    public String toString() {
        return "LoadRequest{" +
                "users=" + Arrays.toString(users) +
                ", people=" + Arrays.toString(persons) +
                ", events=" + Arrays.toString(events) +
                '}';
    }

}

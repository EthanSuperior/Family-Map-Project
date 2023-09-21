package com.example.shared.model;

import java.util.Objects;

public class Event {
    /**
     * Unique identifier for this event
     */
    private String eventID;
    /**
     * User (Username) to which this person belongs
     */
    private String associatedUsername;
    /**
     * ID of person to which this event belongs
     */
    private String personID;
    /**
     * Latitude of event’s location
     */
    private float latitude;
    /**
     * Longitude of event’s location
     */
    private float longitude;
    /**
     * Country in which event occurred
     */
    private String country;
    /**
     * City in which event occurred
     */
    private String city;
    /**
     * Type of event (birth, baptism, christening, marriage, death, etc.)
     */
    private String eventType;
    /**
     * Year in which event occurred
     */
    private int year;

    /**
     * Creates a Event object
     *
     * @param eventID            Unique identifier for this event
     * @param associatedUsername User (Username) to which this person belongs
     * @param personID           ID of person to which this event belongs
     * @param latitude           Latitude of event’s location
     * @param longitude          Longitude of event’s location
     * @param country            Country in which event occurred
     * @param city               City in which event occurred
     * @param eventType          Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param year               Year in which event occurred
     */
    public Event(String eventID, String associatedUsername, String personID, float latitude, float longitude,
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }

    /**
     * Generates a string value for the Event object
     *
     * @return Returns a string value for the Event object
     */
    @Override
    public String toString() {
        return "Event{" +
                "eventID='" + eventID + '\'' +
                ", associatedUsername='" + associatedUsername + '\'' +
                ", personID='" + personID + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", eventType='" + eventType + '\'' +
                ", year=" + year +
                '}';
    }

    /**
     * Checks is an object is equal to this Event object
     *
     * @param o Object to check against
     * @return A boolean value of equivalency
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Float.compare(event.getLatitude(), getLatitude()) == 0 &&
                Float.compare(event.getLongitude(), getLongitude()) == 0 &&
                getYear() == event.getYear() &&
                getEventID().equals(event.getEventID()) &&
                getAssociatedUsername().equals(event.getAssociatedUsername()) &&
                getPersonID().equals(event.getPersonID()) &&
                getCountry().equals(event.getCountry()) &&
                getCity().equals(event.getCity()) &&
                getEventType().equals(event.getEventType());
    }

    /**
     * Generates a unique hash code for the Event object
     *
     * @return A unique hash code of the Event object
     */
    @Override
    public int hashCode() {
        return Objects.hash(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
    }
}

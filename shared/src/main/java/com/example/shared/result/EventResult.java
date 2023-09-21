package com.example.shared.result;

public class EventResult extends ServiceResult {

    String associatedUsername;
    String eventID;
    String personID;
    float latitude;
    float longitude;
    String country;
    String city;
    String eventType;
    int year;

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Creates a successful GetEventResult Object
     *
     * @param associatedUsername Username of user account this event belongs to
     * @param eventID            Event’s unique ID
     * @param personID           ID of the person this event belongs to
     * @param latitude           Latitude of the event’s location
     * @param longitude          Longitude of the event’s location
     * @param country            Name of country where event occurred
     * @param city               Name of city where event occurred
     * @param eventType          Type of event ('birth', 'baptism', etc.)
     * @param year               Year the event occurred
     */
    public EventResult(String eventID, String associatedUsername, String personID, float latitude, float longitude,
                       String country, String city, String eventType, int year) {
        super(null, true);
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    /**
     * Creates a failed GetEventResult Object
     * <p>
     * Fails when there is an Invalid auth token, Invalid eventID parameter, Requested event does not belong to this user, Internal server error
     *
     * @param errorMessage A Description of the error
     */
    public EventResult(String errorMessage) {
        super(errorMessage, false);
    }
}

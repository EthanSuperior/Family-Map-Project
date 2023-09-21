package com.example.shared.result;


import com.example.shared.model.Event;

public class EventsResult extends ServiceResult {

    Event[] data;
    /**
     * Creates a successful EventResult Object
     *
     * @param eventData An array of Event objects of the current user and their family
     */
    public EventsResult(Event[] eventData) {
        super("", true);
        data = eventData;
    }

    /**
     * Creates a failed EventResult Object
     * <p>
     * Fails when there is Invalid auth token, Internal server error
     *
     * @param errorMessage A Description of the error
     */
    public EventsResult(String errorMessage) {
        super(errorMessage, false);
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }
}

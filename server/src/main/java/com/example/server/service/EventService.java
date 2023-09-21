package com.example.server.service;

import com.example.server.dao.AuthTokenDAO;
import com.example.server.dao.Database;
import com.example.server.dao.EventDAO;
import com.example.shared.model.Event;
import com.example.shared.result.EventResult;

import java.sql.Connection;

public class EventService {

    /**
     * Returns the single Event object with the specified ID.
     *
     * @param eventID   ID of the event to retrieve
     * @param authToken AuthToken of the user
     * @return A GetEventResult Object
     */
    public EventResult getEvent(String eventID, String authToken) {
        try {
            Database db = new Database();
            try {
                Connection conn = db.getConnection();
                EventDAO eDAO = new EventDAO(conn);
                Event result = eDAO.find(eventID);
                //Check if wrong user is requesting
                AuthTokenDAO tokenDAO = new AuthTokenDAO(conn);
                System.out.println("Check for Correct User");
                if(!tokenDAO.findTokenByID(authToken).getUserName().equals(result.getAssociatedUsername())){
                    db.closeConnection(false);
                    return new EventResult("error wrong User Access");
                }
                db.closeConnection(true);
                return new EventResult(result.getEventID(),result.getAssociatedUsername(),result.getPersonID(),result.getLatitude(),
                        result.getLongitude(),result.getCountry(),result.getCity(), result.getEventType(),result.getYear());
            } catch (Exception e) {
                db.closeConnection(false);
                return new EventResult("error" + e.getMessage());
            }
        } catch (Exception e) {
            return new EventResult("error" + e.getMessage());
        }
    }
}

package com.example.server.service;

import com.example.server.dao.AuthTokenDAO;
import com.example.server.dao.Database;
import com.example.server.dao.EventDAO;
import com.example.shared.model.Event;
import com.example.shared.result.EventsResult;


import java.sql.Connection;

public class EventsService {
    /**
     * Returns ALL events for ALL family members of the current user.
     * The current user is determined from the provided auth token.
     *
     * @param token Provided auth token to determined the current user.
     * @return A EventResult Object
     */
    public EventsResult event(String token) {
        try {
            Database db = new Database();
            try {
                Connection conn = db.getConnection();
                AuthTokenDAO tokenDAO = new AuthTokenDAO(conn);
                String userName = tokenDAO.findUser(tokenDAO.findTokenByID(token)).getUserName();

                EventDAO eDAO = new EventDAO(conn);
                Event[] result = eDAO.findAll(userName);

                db.closeConnection(true);
                return new EventsResult(result);
            } catch (Exception e) {
                db.closeConnection(false);
                return new EventsResult("error" + e.getMessage());
            }
        } catch (Exception e) {
            return new EventsResult("error" + e.getMessage());
        }
    }
}

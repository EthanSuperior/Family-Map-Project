package com.example.server.service;

import com.example.server.dao.AuthTokenDAO;
import com.example.server.dao.Database;
import com.example.server.dao.PersonDAO;
import com.example.shared.model.Person;
import com.example.shared.result.PersonsResult;


import java.sql.Connection;

public class PersonsService {
    /**
     * Returns ALL family members of the current user. The current user is determined from the provided auth token.
     *
     * @return Return a PersonResult Object
     */
    public PersonsResult personService(String token) {
        try {
            Database db = new Database();
            try {
                Connection conn = db.getConnection();
                AuthTokenDAO tokenDAO = new AuthTokenDAO(conn);
                String userName = tokenDAO.findUser(tokenDAO.findTokenByID(token)).getUserName();

                PersonDAO pDAO = new PersonDAO(conn);
                Person[] result = pDAO.findAll(userName);
                db.closeConnection(true);
                return new PersonsResult(result);
            } catch (Exception e) {
                db.closeConnection(false);
                return new PersonsResult("error" + e.getMessage());
            }
        } catch (Exception e) {
            return new PersonsResult("error" + e.getMessage());
        }
    }
}

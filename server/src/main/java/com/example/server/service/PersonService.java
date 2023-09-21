package com.example.server.service;

import com.example.server.dao.AuthTokenDAO;
import com.example.server.dao.Database;
import com.example.server.dao.PersonDAO;
import com.example.shared.model.Person;
import com.example.shared.result.PersonResult;

import java.sql.Connection;

public class PersonService {
    /**
     * Returns the single Person object with the specified ID.
     *
     * @param personID  The ID of the Person to Retrieve
     * @param authToken AuthToken of the user
     * @return A GetPersonResultObject
     */
    public PersonResult getPerson(String personID, String authToken) {
        try {
            System.out.println(personID);
            Database db = new Database();
            try {
                Connection conn = db.getConnection();
                PersonDAO pDAO = new PersonDAO(conn);
                Person result = pDAO.find(personID);
                //Check if wrong user is requesting
                AuthTokenDAO tokenDAO = new AuthTokenDAO(conn);
                if(!tokenDAO.findTokenByID(authToken).getUserName().equals(result.getAssociatedUsername())){
                    db.closeConnection(false);
                    return new PersonResult("error wrong User Access");
                }
                db.closeConnection(true);
                return new PersonResult(result.getAssociatedUsername(), result.getPersonID(), result.getFirstName(), result.getLastName(),
                        result.getGender(), result.getFatherID(), result.getMotherID(), result.getSpouseID());
            } catch (Exception e) {
                db.closeConnection(false);
                return new PersonResult("error" + e.getMessage());
            }
        } catch (Exception e) {
            return new PersonResult("error" + e.getMessage());
        }
    }
}

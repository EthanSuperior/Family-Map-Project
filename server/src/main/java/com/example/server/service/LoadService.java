package com.example.server.service;

import com.example.server.dao.Database;
import com.example.server.dao.EventDAO;
import com.example.server.dao.PersonDAO;
import com.example.server.dao.UserDAO;
import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.example.shared.model.User;
import com.example.shared.request.LoadRequest;
import com.example.shared.result.LoadResult;

import java.sql.Connection;

public class LoadService {
    /**
     * Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
     *
     * @param r A LoadRequest Object
     * @return Returns a LoadResult Object
     */
    public LoadResult load(LoadRequest r) {
        try {
            System.out.println("Entering Load Service");
            Database db = new Database();
            try {
                Connection conn = db.getConnection();
                db.clearTables();
                UserDAO uDao = new UserDAO(conn);
                PersonDAO pDao = new PersonDAO(conn);
                EventDAO eDao = new EventDAO(conn);
                for (User user : r.getUsers()) {
                    uDao.insert(user);
                }
                for (Person person : r.getPersons()) {
                    pDao.insert(person);
                }
                for (Event event : r.getEvents()) {
                    eDao.insert(event);
                }
                db.closeConnection(true);
                return new LoadResult("Successfully added " + r.getUsers().length + " users, " + r.getPersons().length + " persons, and " + r.getEvents().length + " events to the database", true);
            }catch (Exception e){
                db.closeConnection(false);
                return new LoadResult("error" + e.getMessage(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new LoadResult("error" + e.getMessage(), false);
        }
    }
}

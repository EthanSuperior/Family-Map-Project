package com.example.server.service;

import com.example.server.dao.Database;
import com.example.shared.result.ClearResult;

public class ClearService {
    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
     *
     * @return A ClearResult Object
     */
    public ClearResult clear() {
        try {
            Database db = new Database();
            try {
                db.openConnection();
                db.clearTables();
                db.closeConnection(true);
                return new ClearResult("Clear succeeded.", true);
            } catch (Exception e) {
            db.closeConnection(false);
            return new ClearResult("error" + e.getMessage(), false);
        }
        } catch (Exception e) {
            return new ClearResult("error" + e.getMessage(), false);
        }
    }
}

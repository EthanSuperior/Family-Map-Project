package com.example.server.service;

import com.example.server.dao.AuthTokenDAO;
import com.example.server.dao.Database;
import com.example.shared.model.AuthToken;
import com.example.shared.model.User;
import com.example.shared.request.LoginRequest;
import com.example.shared.result.LoginResult;

import java.sql.Connection;

public class LoginService {
    /**
     * Logs in the user and returns an auth token.
     *
     * @param r A LoginRequest Object
     * @return A LoginResult Object
     */
    public LoginResult login(LoginRequest r) {
        try {
            Database db = new Database();
            try {
                Connection conn = db.getConnection();
                AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
                AuthToken token = authTokenDAO.create(r.getUserName(), r.getPassword());
                User user = authTokenDAO.findUser(token);
                db.closeConnection(true);
                if (user != null) {
                    return new LoginResult(token.getAuthorizationToken(), user.getUserName(), user.getPersonID());
                } else {
                    return new LoginResult("Could not find related user");
                }
            } catch (Exception e) {
                db.closeConnection(false);
                return new LoginResult("error" + e.getMessage());
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return new LoginResult("error" + e.getMessage());
        }
    }
}

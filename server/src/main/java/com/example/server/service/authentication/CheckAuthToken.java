package com.example.server.service.authentication;

import com.example.server.dao.AuthTokenDAO;
import com.example.server.dao.Database;
import com.example.shared.result.ServiceResult;

import java.sql.Connection;

public class CheckAuthToken {
    public ServiceResult checkToken(String token) {
        try {
            Database db = new Database();
            try {
                Connection conn = db.getConnection();
                AuthTokenDAO tokenDAO = new AuthTokenDAO(conn);
                boolean valid = tokenDAO.findTokenByID(token) != null;
                db.closeConnection(true);
                if (valid) {
                    return new ServiceResult("Valid AuthToken", true);
                } else {
                    return new ServiceResult("error Invalid AuthToken", false);
                }
            } catch (Exception e) {
                db.closeConnection(false);
                return new ServiceResult("error" + e.getMessage(), false);
            }
        } catch (Exception e) {
            return new ServiceResult(e.getMessage(), false);
        }
    }
}

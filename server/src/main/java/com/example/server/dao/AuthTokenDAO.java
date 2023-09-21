package com.example.server.dao;

import com.example.shared.model.AuthToken;
import com.example.shared.model.User;

import java.sql.*;
import java.util.UUID;

public class AuthTokenDAO {
    /**
     * Connection Object to the Database
     */
    private final Connection conn;

    /**
     * Creates an AuthToken object and connects to the database
     *
     * @param conn the Connection to the database
     */
    public AuthTokenDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a AuthToken into the Database
     *
     * @param userName The userName of the AuthToken to create
     * @return Returns true if successful
     */
    public AuthToken create(String userName, String password) throws DataAccessException {
        UserDAO userDAO = new UserDAO(conn);
        User user = userDAO.find(userName, password);
        if (user != null) {
            AuthToken auth = new AuthToken(userName, UUID.randomUUID().toString());
            //We can structure our string to be similar to a sql command, but if we insert question
            //marks we can change them later with help from the statement
            String sql = "INSERT INTO AuthTokens (UserName, AuthToken) VALUES(?,?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                //Using the statements built-in set(type) functions we can pick the question mark we want
                //to fill in and give it a proper value. The first argument corresponds to the first
                //question mark found in our sql String
                stmt.setString(1, auth.getUserName());
                stmt.setString(2, auth.getAuthorizationToken());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error encountered while inserting into the database");
            }
            return auth;
        } else {
            throw new DataAccessException("Error encountered while creating Auth Token");
        }
    }

    /**
     * Gets a User from the Database given an Auth Token
     *
     * @param authToken The AuthToken of the user name to return
     * @return The User name with matching AuthToken
     */
    public User findUser(AuthToken authToken) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE UserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getUserName());
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("UserName"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("PersonID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Checks AuthToken String for validity
     *
     * @param authToken The authToken to check
     * @return Validity of the AuthToken
     */
    public AuthToken findTokenByID(String authToken) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE AuthToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new AuthToken(rs.getString("UserName"), rs.getString("AuthToken"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding AuthToken");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }


    /**
     * Clears the AuthTokens Database
     */
    public boolean clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM AuthTokens";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing AuthTokens table");
        }
        return true;
    }
}

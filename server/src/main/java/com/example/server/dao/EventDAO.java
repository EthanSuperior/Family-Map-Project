package com.example.server.dao;

import com.example.shared.model.Event;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    /**
     * Connection Object to the Database
     */
    private final Connection conn;

    /**
     * Creates an EventDAO object and connects to the database
     *
     * @param conn the Connection to the database
     */
    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a Event into the Database
     *
     * @param newEvent Event Object to insert
     */
    public boolean insert(Event newEvent) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, newEvent.getEventID());
            stmt.setString(2, newEvent.getAssociatedUsername());
            stmt.setString(3, newEvent.getPersonID());
            stmt.setFloat(4, newEvent.getLatitude());
            stmt.setFloat(5, newEvent.getLongitude());
            stmt.setString(6, newEvent.getCountry());
            stmt.setString(7, newEvent.getCity());
            stmt.setString(8, newEvent.getEventType());
            stmt.setInt(9, newEvent.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        return true;
    }

    /**
     * Gets a Event Object from the Database
     *
     * @param eventID The ID of the Event to return
     * @return The Event with matching eventID
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                System.out.println("L:" + event.getEventID());
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
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
     * Clears the Event Database of the given userName
     *
     * @param userName The Associated User Name to Clear
     */
    public boolean clearUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE AssociatedUsername=?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing part of the database");
        }
        return true;
    }

    /**
     * Gets an array of all Event Object related to a user from the Database
     *
     * @param assocUser The ID of the user to return related Events for
     * @return The array of Events that relates to the user name
     */
    public Event[] findAll(String assocUser) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, assocUser);
            rs = stmt.executeQuery();
            List<Event> events = new ArrayList<>();
            while (rs.next()) {
                Event event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                events.add(event);
            }
            return events.toArray(new Event[events.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * Clears the Event Database
     */
    public boolean clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM Events";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing Events table");
        }
        return true;
    }
}

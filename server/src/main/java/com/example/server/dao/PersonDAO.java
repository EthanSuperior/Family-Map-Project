package com.example.server.dao;


import com.example.shared.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    /**
     * Connection Object to the Database
     */
    private final Connection conn;

    /**
     * Creates an PersonDAO object and connects to the database
     *
     * @param conn the Connection to the database
     */
    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a Person into the Database
     *
     * @param newPerson Person Object to insert
     */
    public boolean insert(Person newPerson) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO People (PersonID, AssociatedUsername, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, newPerson.getPersonID());
            stmt.setString(2, newPerson.getAssociatedUsername());
            stmt.setString(3, newPerson.getFirstName());
            stmt.setString(4, newPerson.getLastName());
            stmt.setString(5, newPerson.getGender());
            stmt.setString(6, newPerson.getFatherID());
            stmt.setString(7, newPerson.getMotherID());
            stmt.setString(8, newPerson.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
        return true;
    }

    /**
     * Gets a Person Object from the Database
     *
     * @param personID The ID of the Person to return
     * @return The Person with matching personID
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM People WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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
     * Gets an array of all People Object related to a user from the Database
     *
     * @param assocUser The userName of the user to return related People for
     * @return The array of People that relates to user name
     */
    public Person[] findAll(String assocUser) throws DataAccessException {
        ResultSet rs = null;
        String sql = "SELECT * FROM People WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, assocUser);
            rs = stmt.executeQuery();
            List<Person> people = new ArrayList<>();
            while (rs.next()) {
                Person person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
                people.add(person);
            }
            return people.toArray(new Person[people.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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
     * Clears the Person Database
     *
     * @param userName The Associated User Name to clear
     */
    public boolean clearUser(String userName) throws DataAccessException {
        String sql = "DELETE FROM People WHERE AssociatedUsername=?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while clearing part of the database");
        }
        return true;
    }

    /**
     * Clears the People Database
     */
    public boolean clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM People";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing People table");
        }
        return true;
    }
}

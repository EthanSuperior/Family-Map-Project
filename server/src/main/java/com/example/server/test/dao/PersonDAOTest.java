package com.example.server.test.dao;

import com.example.server.dao.DataAccessException;
import com.example.server.dao.Database;
import com.example.server.dao.PersonDAO;
import com.example.shared.model.Person;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDAOTest {
    private final String personID = "uniquePersonID";
    private Database db;
    private Person bestPerson;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new person with random data
        //personID is uniquePersonID, set as a variable for reference in finding
        bestPerson = new Person(personID, "MyUserName",
                "Jane", "Doe", "m", "myDadID", "myMomID", "myWifeID");
        //Here, we'll open the connection in preparation for the test case to use it
        Connection conn = db.getConnection();
        //Let's clear the database as well so any lingering data doesn't affect our tests
        db.clearTables();
        //Then we pass that connection to the PersonDAO so it can access the database
        pDao = new PersonDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //While insert returns a bool we can't use that to verify that our function actually worked
        //only that it ran without causing an error
        pDao.insert(bestPerson);
        //So lets use a find method to get the person that we just put in back out
        Person compareTest = pDao.find(personID);
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        pDao.insert(bestPerson);
        //but our sql table is set up so that "personID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, () -> pDao.insert(bestPerson));
    }

    @Test
    public void getPass() throws DataAccessException {
        //Make sure it returns null if missing
        Person compareTest = pDao.find(personID);
        assertNull(compareTest);
        pDao.insert(bestPerson);
        compareTest = pDao.find(personID);
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our find got something, and that it didn't change the
        //data in any way
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void getFail() throws DataAccessException {
        //insert make sure you get them, then try to get someone that doesn't exist and see its null
        //lets do this get test again but this time lets try to make it fail
        pDao.insert(bestPerson);
        Person compareTest = pDao.find(personID);
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);

        //pDao.clear();
        compareTest = pDao.find(personID);
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws DataAccessException {
        //Insert into the table
        pDao.insert(bestPerson);
        //Clear it
        //pDao.clear();
        //Make sure we cannot find the person
        Person compareTest = pDao.find(personID);
        assertNull(compareTest);
    }
}

package com.example.server.service;

import com.example.server.dao.DataAccessException;
import com.example.server.dao.Database;
import com.example.server.dao.EventDAO;
import com.example.server.dao.PersonDAO;
import com.example.server.dao.UserDAO;
import com.example.shared.model.Event;
import com.example.shared.model.Location;
import com.example.shared.model.Person;
import com.example.shared.model.User;
import com.example.shared.result.FillResult;

import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

public class FillService {
    private String[] maleNames;
    private String[] femaleNames;
    private String[] sirNames;
    private Location[] locations;

    /**
     * Populates the server's database with generated data for the specified user name.
     * <p>
     * If there is any data in the database already associated with the given user name, it is deleted.
     * (the default is 4, which results in 31 new persons each with associated events)
     *
     * @param userName    The required 'userName' parameter must be a user already registered with the server.
     * @param generations The 'generations' parameter lets the caller specify the number of generations of ancestors to be generated, must be non-negative
     * @return A FillResult Object
     */
    public FillResult fill(String userName, int generations) {
        try {
            System.out.println("Entering Fill Service");
            Database db = new Database();
            try {
                Connection conn = db.openConnection();
                EventDAO eDAO = new EventDAO(conn);
                eDAO.clearUser(userName);
                PersonDAO pDAO = new PersonDAO(conn);
                pDAO.clearUser(userName);
                UserDAO uDAO = new UserDAO(conn);
                User user = uDAO.findUserByUserName(userName);

                Person userPerson = new Person(createID(user.getFirstName(), user.getLastName()), userName,
                        user.getFirstName(), user.getLastName(), user.getGender(), null, null, null);
                uDAO.updatePersonID(user, userPerson.getPersonID());
                int birthYear = 2020 - new Random().nextInt(65);
                Event userBirth = createEvent(userName, userPerson, "birth", birthYear);



                String[] parentIDs = createCouple(userName, generations, birthYear, userPerson.getLastName(), conn);

                userPerson.setFatherID(parentIDs[0]);
                userPerson.setMotherID(parentIDs[1]);

                pDAO.insert(userPerson);
                eDAO.insert(userBirth);

                db.closeConnection(true);

                int numOfPersons = (int) (Math.pow(2, generations + 1) - 2);
                return new FillResult("Successfully added " + (numOfPersons + 1) + " persons and " + ((numOfPersons * 3) + 1) + " events to the database.", true);
            } catch (Exception e) {
                db.closeConnection(false);
                return new FillResult("error" + e.getMessage(),false);
            }
        } catch (Exception e) {
            return new FillResult("error" + e.getMessage(), false);
        }
    }

    /**
     * Creates a Person and their wife recursively
     *
     * @param userName    Associated Username of the objects
     * @param generations number of iterations to run (4 = 31 people)
     * @param birthYear   the birth year of their child
     * @return ID of user to map back to recursive call
     */
    private String[] createCouple(String userName, int generations, int birthYear, String lastName, Connection conn) throws DataAccessException {
        if (generations == 0) {
            return new String[]{null, null};
        }

        PersonDAO pDAO = new PersonDAO(conn);
        Person husband = createHusband(userName, lastName);
        Person wife = createWife(userName);
        Random random = new Random();

        int husBirthYear = birthYear - 14 - random.nextInt(29);
        Event husBirth = createEvent(userName, husband, "birth", husBirthYear);
        int wifeBirthYear = birthYear - 14 - random.nextInt(23);
        Event wifeBirth = createEvent(userName, wife, "birth", wifeBirthYear);
        int marriageYear = birthYear + 14 + random.nextInt(12);
        Event husMarriage = createEvent(userName, husband, "marriage", marriageYear);
        Event wifeMarriage = new Event("marriage_" + createID(wife.getFirstName(), wife.getLastName()), userName, wife.getPersonID(),
                husMarriage.getLatitude(), husMarriage.getLongitude(), husMarriage.getCountry(),
                husMarriage.getCity(), husMarriage.getEventType(), husMarriage.getYear());

        Event husDeath = createEvent(userName, husband, "death", husBirthYear + 35 + random.nextInt(78));
        Event wifeDeath = createEvent(userName, wife, "death", wifeBirthYear + 35 + random.nextInt(80));

        EventDAO eDAO = new EventDAO(conn);
        eDAO.insert(husBirth);
        eDAO.insert(husMarriage);
        eDAO.insert(husDeath);
        eDAO.insert(wifeBirth);
        eDAO.insert(wifeMarriage);
        eDAO.insert(wifeDeath);

        String[] fraternalParentIds = createCouple(userName, generations - 1, husBirthYear, husband.getLastName(), conn);
        String[] maternalParentIds = createCouple(userName, generations - 1, wifeBirthYear, wife.getLastName(), conn);

        husband.setFatherID(fraternalParentIds[0]);
        husband.setMotherID(fraternalParentIds[1]);
        husband.setSpouseID(wife.getPersonID());
        wife.setFatherID(maternalParentIds[0]);
        wife.setMotherID(maternalParentIds[1]);
        wife.setSpouseID(husband.getPersonID());

        pDAO.insert(husband);
        pDAO.insert(wife);

        return new String[]{husband.getPersonID(), wife.getPersonID()};
    }

    private Person createHusband(String accosUser, String lastName) {
        Random random = new Random();
        String husbandName = maleNames[random.nextInt(maleNames.length)];
        String personID = createID(husbandName, lastName);

        return new Person(personID, accosUser, husbandName, lastName, "m", null, null, null);
    }

    private Person createWife(String accosUser) {
        Random random = new Random();
        String wifeName = femaleNames[random.nextInt(femaleNames.length)];
        String sirName = sirNames[random.nextInt(sirNames.length)];
        String personID = createID(wifeName, sirName);

        return new Person(personID, accosUser, wifeName, sirName, "f", null, null, null);
    }

    private Event createEvent(String accosUser, Person person, String type, int year) {
        String eventID = type + "_" + createID(person.getFirstName(), person.getLastName());
        Random random = new Random();
        Location eventLocation = locations[random.nextInt(locations.length)];
        float latitude = eventLocation.latitude;
        float longitude = eventLocation.longitude;
        String country = eventLocation.country;
        String city = eventLocation.city;
        return new Event(eventID, accosUser, person.getPersonID(), latitude, longitude, country, city, type, year);
    }

    private String createID(String firstName, String lastName) {
        return firstName + "_" + lastName + "-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public void setGenerators(String[] mnames, String[] fnames, String[] snames, Location[] locationsOld) {
        maleNames = mnames;
        femaleNames = fnames;
        sirNames = snames;
        this.locations = locationsOld;
    }
}

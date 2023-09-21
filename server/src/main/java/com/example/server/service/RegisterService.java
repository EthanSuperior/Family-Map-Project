package com.example.server.service;

import com.example.server.dao.AuthTokenDAO;
import com.example.server.dao.DataAccessException;
import com.example.server.dao.Database;
import com.example.server.dao.EventDAO;
import com.example.server.dao.PersonDAO;
import com.example.server.dao.UserDAO;
import com.example.shared.model.AuthToken;
import com.example.shared.model.Event;
import com.example.shared.model.Location;
import com.example.shared.model.Person;
import com.example.shared.model.User;
import com.example.shared.request.RegisterRequest;
import com.example.shared.result.RegisterResult;

import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

public class RegisterService {
    private String[] maleNames;
    private String[] femaleNames;
    private String[] sirNames;
    private Location[] locations;

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
     *
     * @param r A RegisterRequest Object
     * @return A RegisterResult Object
     */
    public RegisterResult register(RegisterRequest r) {
        try {
            Database db = new Database();
            try {
                Connection conn = db.getConnection();
                UserDAO uDAO = new UserDAO(conn);
                if (uDAO.findUserByUserName(r.getUserName()) != null) {
                    db.closeConnection(false);
                    return new RegisterResult("error User Exists");
                }
                System.out.println("Creating User");
                User user = new User(r.getUserName(), r.getPassword(), r.getEmail(), r.getFirstName(), r.getLastName(), "" + r.getGender(), createID(r.getFirstName(), r.getLastName()));

                System.out.println("Generating data");
                int birthYear = 2020 - new Random().nextInt(65);
                String[] parentIDs = createCouple(r.getUserName(), 4, birthYear, r.getLastName(), conn);
                Person userPerson = new Person(user.getPersonID(), r.getUserName(), r.getFirstName(), r.getLastName(), r.getGender(), parentIDs[0], parentIDs[1], null);
                Event userBirth = createEvent(r.getUserName(), userPerson, "birth", birthYear);

                System.out.println("Generated");
                new EventDAO(conn).insert(userBirth);
                new PersonDAO(conn).insert(userPerson);

                uDAO.insert(user);
                AuthToken token = new AuthTokenDAO(conn).create(r.getUserName(), r.getPassword());
                db.closeConnection(true);
                return new RegisterResult(token.getAuthorizationToken(), user.getUserName(), user.getPersonID());
            } catch (Exception e) {
                db.closeConnection(false);
                return new RegisterResult("error" + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RegisterResult("error" + e.getMessage());
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

        int marriageYear = birthYear - random.nextInt(12);
        Event husMarriage = createEvent(userName, husband, "marriage", marriageYear);
        Event wifeMarriage = new Event("marriage_" + createID(wife.getFirstName(), wife.getLastName()), userName, wife.getPersonID(),
                husMarriage.getLatitude(), husMarriage.getLongitude(), husMarriage.getCountry(),
                husMarriage.getCity(), husMarriage.getEventType(), husMarriage.getYear());

        Event husDeath = createEvent(userName, husband, "death", husBirthYear + 35 + random.nextInt(78));
        Event wifeDeath = createEvent(userName, wife, "death", wifeBirthYear + 35 + random.nextInt(80));

        EventDAO eDAO = new EventDAO(conn);
        eDAO.insert(husBirth);
        eDAO.insert(wifeBirth);
        eDAO.insert(husMarriage);
        eDAO.insert(wifeMarriage);
        eDAO.insert(husDeath);
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

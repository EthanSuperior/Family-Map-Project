package com.example.familymapclient;

import com.example.shared.model.AuthToken;
import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.example.shared.result.EventsResult;
import com.example.shared.result.PersonsResult;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DataCache {
    private static final Comparator<Event> eventComparator = (o1, o2) -> {
        if (o1.getYear() == o2.getYear()) {
            return o1.getEventType().compareTo(o2.getEventType());
        } else if (o1.getYear() < o2.getYear()) {
            return -1;
        }
        return 1;
    };
    static public Map<String, Person> peopleMap = new HashMap<>();
    static public Map<String, Event> eventMap = new HashMap<>();
    static public Map<String, TreeSet<Event>> fatherMaleEventMap = new HashMap<>();
    static public Map<String, TreeSet<Event>> fatherFemaleEventMap = new HashMap<>();
    static public Map<String, TreeSet<Event>> motherMaleEventMap = new HashMap<>();
    static public Map<String, TreeSet<Event>> motherFemaleEventMap = new HashMap<>();
    static public Map<String, TreeSet<Event>> familyMaleEventMap = new HashMap<>();
    static public Map<String, TreeSet<Event>> familyFemaleEventMap = new HashMap<>();
    static public List<String> eventTypes = new ArrayList<>();
    public static String userID = null;
    public static AuthToken authToken;
    public static boolean maleEvents = true;
    public static boolean femaleEvents = true;
    public static boolean fatherEvents = true;
    public static boolean motherEvents = true;
    public static boolean showLifeLines = true;
    public static boolean showSpouseLines = true;
    public static boolean showFamilyTreeLines = true;

    static public void readPeople(PersonsResult peopleResult) {
        for (Person person : peopleResult.getData()) {
            if (person != null) {
                peopleMap.put(person.getPersonID(), person);
            }
        }
    }

    static public void readEvents(EventsResult eventsResult) {
        for (Event event : eventsResult.getData()) {
            if (event != null) {
                eventMap.put(event.getEventID(), event);
                if (!eventTypes.contains(event.getEventID().toLowerCase()))
                    eventTypes.add(event.getEventType().toLowerCase());
            }
        }
        sortEvents();
    }

    private static void sortEvents() {
        System.out.println("Sorting Events");
        //First Step: Sort the people
        Person user = getPerson(userID);
        if (user.getGender().equals("m")) {
            familyMaleEventMap.put(userID, new TreeSet<>(eventComparator));
            if (user.getSpouseID() != null) {
                familyFemaleEventMap.put(user.getSpouseID(), new TreeSet<>(eventComparator));
            }
        } else {
            familyFemaleEventMap.put(userID, new TreeSet<>(eventComparator));
            if (user.getSpouseID() != null) {
                familyMaleEventMap.put(user.getSpouseID(), new TreeSet<>(eventComparator));
            }
        }
        if (user.getFatherID() != null) {
            sortPeople(true, getPerson(user.getFatherID()));
        }
        if (user.getMotherID() != null) {
            sortPeople(false, getPerson(user.getMotherID()));
        }
        //Sort their events
        for (Event e : eventMap.values()) {
            Person eventPerson = getPerson(e.getPersonID());
            if (eventPerson.getGender().equals("m")) {
                if (familyMaleEventMap.containsKey(e.getPersonID())) {
                    familyMaleEventMap.get(e.getPersonID()).add(e);
                } else if (fatherMaleEventMap.containsKey(e.getPersonID())) {
                    fatherMaleEventMap.get(e.getPersonID()).add(e);
                } else if (motherMaleEventMap.containsKey(e.getPersonID())) {
                    motherMaleEventMap.get(e.getPersonID()).add(e);
                }
            } else {
                if (familyFemaleEventMap.containsKey(e.getPersonID())) {
                    familyFemaleEventMap.get(e.getPersonID()).add(e);
                } else if (fatherFemaleEventMap.containsKey(e.getPersonID())) {
                    fatherFemaleEventMap.get(e.getPersonID()).add(e);
                } else if (motherFemaleEventMap.containsKey(e.getPersonID())) {
                    motherFemaleEventMap.get(e.getPersonID()).add(e);
                }
            }
        }
        System.out.println(familyFemaleEventMap.toString());
    }

    static public TreeSet<Event> getFilteredEvents(String searchPerson) {
        if (maleEvents) {
            if (familyMaleEventMap.containsKey(searchPerson)) {
                return familyMaleEventMap.get(searchPerson);
            }
            if (fatherEvents) {
                if (fatherMaleEventMap.containsKey(searchPerson))
                    return fatherMaleEventMap.get(searchPerson);
            }
            if (motherEvents) {
                if (motherMaleEventMap.containsKey(searchPerson))
                    return motherMaleEventMap.get(searchPerson);
            }
        }
        if (femaleEvents) {
            if (familyFemaleEventMap.containsKey(searchPerson)) {
                return familyFemaleEventMap.get(searchPerson);
            }
            if (fatherEvents) {
                if (fatherFemaleEventMap.containsKey(searchPerson))
                    return fatherFemaleEventMap.get(searchPerson);
            }
            if (motherEvents) {
                if (motherFemaleEventMap.containsKey(searchPerson))
                    return motherFemaleEventMap.get(searchPerson);
            }
        }
        return null;
    }

    static private void sortPeople(boolean isFatherSide, Person basePerson) {
        if (isFatherSide) {
            if (basePerson.getGender().equals("m")) {
                fatherMaleEventMap.put(basePerson.getPersonID(), new TreeSet<>(eventComparator));
            } else {
                fatherFemaleEventMap.put(basePerson.getPersonID(), new TreeSet<>(eventComparator));
            }
        } else {
            if (basePerson.getGender().equals("m")) {
                motherMaleEventMap.put(basePerson.getPersonID(), new TreeSet<>(eventComparator));
            } else {
                motherFemaleEventMap.put(basePerson.getPersonID(), new TreeSet<>(eventComparator));
            }
        }
        if (basePerson.getFatherID() != null) {
            sortPeople(isFatherSide, getPerson(basePerson.getFatherID()));
        }
        if (basePerson.getMotherID() != null) {
            sortPeople(isFatherSide, getPerson(basePerson.getMotherID()));
        }
    }

    static public Person getPerson(String personID) {
        return peopleMap.get(personID);
    }

    public static Person getUser() {
        if (userID == null)
            return null;
        return getPerson(userID);
    }

    public static List<Person> getPersonResults(String searchResult) {
        searchResult = searchResult.toLowerCase();
        List<Person> resultPeople = new ArrayList<>();
        for (Person p : peopleMap.values()) {
            if (p.getFirstName().toLowerCase().contains(searchResult) || p.getLastName().toLowerCase().contains(searchResult)) {
                resultPeople.add(p);
            }
        }
        return resultPeople;
    }

    public static List<Event> getEventResults(String searchResult) {
        searchResult = searchResult.toLowerCase();
        List<Event> resultEvent = new ArrayList<>();
        if (maleEvents) {
            for (Set<Event> tree : familyMaleEventMap.values()) {
                for (Event e : tree) {
                    if (e.getCountry().toLowerCase().contains(searchResult) || e.getCity().toLowerCase().contains(searchResult) ||
                            e.getEventType().toLowerCase().contains(searchResult) || Integer.toString(e.getYear()).contains(searchResult)) {
                        resultEvent.add(e);
                    }
                }
            }
            if (fatherEvents) {
                for (Set<Event> tree : fatherMaleEventMap.values()) {
                    for (Event e : tree) {
                        if (e.getCountry().toLowerCase().contains(searchResult) || e.getCity().toLowerCase().contains(searchResult) ||
                                e.getEventType().toLowerCase().contains(searchResult) || Integer.toString(e.getYear()).contains(searchResult)) {
                            resultEvent.add(e);
                        }
                    }
                }
            }
            if (motherEvents) {
                for (Set<Event> tree : motherMaleEventMap.values()) {
                    for (Event e : tree) {
                        if (e.getCountry().toLowerCase().contains(searchResult) || e.getCity().toLowerCase().contains(searchResult) ||
                                e.getEventType().toLowerCase().contains(searchResult) || Integer.toString(e.getYear()).contains(searchResult)) {
                            resultEvent.add(e);
                        }
                    }
                }
            }
        }
        if (femaleEvents) {
            for (Set<Event> tree : familyFemaleEventMap.values()) {
                for (Event e : tree) {
                    if (e.getCountry().toLowerCase().contains(searchResult) || e.getCity().toLowerCase().contains(searchResult) ||
                            e.getEventType().toLowerCase().contains(searchResult) || Integer.toString(e.getYear()).contains(searchResult)) {
                        resultEvent.add(e);
                    }
                }
            }
        }
        if (fatherEvents) {
            for (Set<Event> tree : fatherFemaleEventMap.values()) {
                for (Event e : tree) {
                    if (e.getCountry().toLowerCase().contains(searchResult) || e.getCity().toLowerCase().contains(searchResult) ||
                            e.getEventType().toLowerCase().contains(searchResult) || Integer.toString(e.getYear()).contains(searchResult)) {
                        resultEvent.add(e);
                    }
                }
            }
        }
        if (motherEvents) {
            for (Set<Event> tree : motherFemaleEventMap.values()) {
                for (Event e : tree) {
                    if (e.getCountry().toLowerCase().contains(searchResult) || e.getCity().toLowerCase().contains(searchResult) ||
                            e.getEventType().toLowerCase().contains(searchResult) || Integer.toString(e.getYear()).contains(searchResult)) {
                        resultEvent.add(e);
                    }
                }
            }
        }
        return resultEvent;
    }

    public static Event getMarkerEvent(String eventID) {
        if (getFilteredEvents(eventMap.get(eventID).getPersonID()) != null)
            return eventMap.get(eventID);
        return null;
    }

    public static List<Person> findFamily(Person basePerson) {
        List<Person> resultFamily = new ArrayList<>();
        if (basePerson.getFatherID() != null)
            resultFamily.add(getPerson(basePerson.getFatherID()));
        if (basePerson.getMotherID() != null)
            resultFamily.add(getPerson(basePerson.getMotherID()));
        if (basePerson.getSpouseID() != null)
            resultFamily.add(getPerson(basePerson.getSpouseID()));
        //Find kids
        for (Person p : peopleMap.values()) {
            if ((p.getFatherID() != null && p.getFatherID().equals(basePerson.getPersonID())) || (p.getMotherID() != null && p.getMotherID().equals(basePerson.getPersonID())))
                resultFamily.add(p);
        }
        return resultFamily;
    }
}

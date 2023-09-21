package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PersonActivity extends AppCompatActivity {

    public static final String PERSON_ID_KEY = "PersonIDKey";
    ExpandableListView expandableListView;
    TextView firstName, lastName, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Person displayPerson = DataCache.getPerson(getIntent().getStringExtra(PERSON_ID_KEY));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        firstName = findViewById(R.id.personFirstName);
        lastName = findViewById(R.id.personLastName);
        gender = findViewById(R.id.personGender);
        LoadValues(displayPerson);
        expandableListView = findViewById(R.id.personExpandableList);
        expandableListView.setAdapter(new ExpandableListAdapter(displayPerson));
    }

    private void LoadValues(Person displayPerson) {
        firstName.setText(displayPerson.getFirstName());
        lastName.setText(displayPerson.getLastName());
        gender.setText((displayPerson.getGender().equals("m")) ? R.string.gender_male : R.string.gender_female);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this.getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int LIFE_EVENT_GROUP_POSITION = 0;
        private static final int FAMILY_GROUP_POSITION = 1;

        private final List<Event> lifeEvents;
        private final List<Person> familyMembers;
        private final Person basePerson;

        ExpandableListAdapter(Person basePerson) {
            this.basePerson = basePerson;
            lifeEvents = new ArrayList<>();
            TreeSet<Event> tree = DataCache.getFilteredEvents(basePerson.getPersonID());
            if (tree != null)
                lifeEvents.addAll(tree);
            familyMembers = DataCache.findFamily(basePerson);
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENT_GROUP_POSITION:
                    return lifeEvents.size();
                case FAMILY_GROUP_POSITION:
                    return familyMembers.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case LIFE_EVENT_GROUP_POSITION:
                    return getString(R.string.lifeEvent);
                case FAMILY_GROUP_POSITION:
                    return getString(R.string.family);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case LIFE_EVENT_GROUP_POSITION:
                    return lifeEvents.get(childPosition);
                case FAMILY_GROUP_POSITION:
                    return familyMembers.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case LIFE_EVENT_GROUP_POSITION:
                    titleView.setText(R.string.lifeEvent);
                    break;
                case FAMILY_GROUP_POSITION:
                    titleView.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch (groupPosition) {
                case LIFE_EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.item_event, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case FAMILY_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.item_person, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View eventItemView, final int childPosition) {
            TextView eventInfo = eventItemView.findViewById(R.id.EventTitle);
            TextView personName = eventItemView.findViewById(R.id.EventPerson);
            ImageView icon = eventItemView.findViewById(R.id.eventIcon);

            Event event = lifeEvents.get(childPosition);
            Person eventPerson = DataCache.getPerson(event.getPersonID());
            personName.setText(String.format("%s %s", eventPerson.getFirstName(), eventPerson.getLastName()));
            eventInfo.setText(String.format("%s:  %s, %s (%s)", event.getEventType(),
                    event.getCity(), event.getCountry(), event.getYear()));
            icon.setImageDrawable(new IconDrawable(eventItemView.getContext(), FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.colorCyan).sizeDp(60));

            eventItemView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), EventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(EventActivity.EVENT_ID_KEY, lifeEvents.get(childPosition).getEventID());
                startActivity(intent);
            });
        }

        private void initializePersonView(View personItemView, final int childPosition) {
            TextView personName = personItemView.findViewById(R.id.PersonName);
            TextView personRelation = personItemView.findViewById(R.id.PersonRelation);
            ImageView icon = personItemView.findViewById(R.id.genderIcon);

            Person person = familyMembers.get(childPosition);
            personName.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));

            //Set Relation
            if (person.getPersonID().equals(basePerson.getFatherID())) {
                personRelation.setText(R.string.relationFather);
            } else if (person.getPersonID().equals(basePerson.getMotherID())) {
                personRelation.setText(R.string.relationMother);
            } else if (person.getPersonID().equals(basePerson.getSpouseID())) {
                personRelation.setText(R.string.relationSpouse);
            } else {
                personRelation.setText(R.string.relationChild);
            }

            if (person.getGender().equals("m")) {
                icon.setImageDrawable(new IconDrawable(personItemView.getContext(), FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(60));
            } else {
                icon.setImageDrawable(new IconDrawable(personItemView.getContext(), FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(60));
            }

            personItemView.setOnClickListener(v -> {
                LoadValues(familyMembers.get(childPosition));
                expandableListView.setAdapter(new ExpandableListAdapter(familyMembers.get(childPosition)));
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
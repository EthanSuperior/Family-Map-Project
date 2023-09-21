package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private static final int PERSON_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;
    SearchView searchView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView = this.findViewById(R.id.searchBar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                SearchResultAdapter adapter = new SearchResultAdapter(searchView.getQuery().toString());
                recyclerView.setAdapter(adapter);
                return false;
            }
        });

        recyclerView = findViewById(R.id.searchRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        Iconify.with(new FontAwesomeModule());

        SearchResultAdapter adapter = new SearchResultAdapter(searchView.getQuery().toString());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this.getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {
        private final List<Person> personResults;
        private final List<Event> eventResults;

        SearchResultAdapter(String searchResult) {
            this.personResults = DataCache.getPersonResults(searchResult);
            this.eventResults = DataCache.getEventResults(searchResult);
        }

        @Override
        public int getItemViewType(int position) {
            return position < personResults.size() ? PERSON_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.item_person, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.item_event, parent, false);
            }

            return new SearchResultViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
            if (position < personResults.size()) {
                holder.bind(personResults.get(position));
            } else {
                holder.bind(eventResults.get(position - personResults.size()));
            }
        }

        @Override
        public int getItemCount() {
            return personResults.size() + eventResults.size();
        }
    }

    private class SearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView personName;
        private final TextView eventInfo;
        private final ImageView icon;

        private final int viewType;
        private Person person;
        private Event event;

        SearchResultViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                personName = itemView.findViewById(R.id.PersonName);
                icon = itemView.findViewById(R.id.genderIcon);
                eventInfo = null;
            } else {
                eventInfo = itemView.findViewById(R.id.EventTitle);
                personName = itemView.findViewById(R.id.EventPerson);
                icon = itemView.findViewById(R.id.eventIcon);
            }
        }

        private void bind(Person person) {
            this.person = person;
            personName.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));
            if (person.getGender().equals("m")) {
                icon.setImageDrawable(new IconDrawable(itemView.getContext(), FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(60));
            } else {
                icon.setImageDrawable(new IconDrawable(itemView.getContext(), FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(60));
            }
        }

        private void bind(Event event) {
            this.event = event;
            Person eventPerson = DataCache.getPerson(event.getPersonID());
            personName.setText(String.format("%s %s", eventPerson.getFirstName(), eventPerson.getLastName()));
            eventInfo.setText(String.format("%s:  %s, %s (%s)", event.getEventType(),
                    event.getCity(), event.getCountry(), event.getYear()));
            icon.setImageDrawable(new IconDrawable(itemView.getContext(), FontAwesomeIcons.fa_map_marker).
                    colorRes(R.color.colorCyan).sizeDp(60));
        }

        @Override
        public void onClick(View view) {
            if (viewType == PERSON_ITEM_VIEW_TYPE) {
                // This is were we could pass the skiResort to a ski resort detail activity
                Intent intent = new Intent(itemView.getContext(), PersonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(PersonActivity.PERSON_ID_KEY, person.getPersonID());
                startActivity(intent);
            } else {
                Intent intent = new Intent(itemView.getContext(), EventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(EventActivity.EVENT_ID_KEY, event.getEventID());
                startActivity(intent);
            }
        }

    }
}
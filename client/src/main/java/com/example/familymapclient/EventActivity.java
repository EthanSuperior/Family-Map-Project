package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class EventActivity extends AppCompatActivity {

    public static final String EVENT_ID_KEY = "EventIDKey";
    private String displayedEventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayedEventID = getIntent().getStringExtra(EVENT_ID_KEY);

        FragmentManager fm = this.getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.mapFrame);
        if (mapFragment == null) {
            mapFragment = createMapFragment();
            fm.beginTransaction()
                    .add(R.id.mapFrame, mapFragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this.getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private MapFragment createMapFragment() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(MapFragment.MARKER_KEY, displayedEventID);
        fragment.setArguments(args);
        return fragment;
    }
}
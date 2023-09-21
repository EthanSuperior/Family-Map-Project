package com.example.familymapclient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.shared.model.Event;
import com.example.shared.model.Person;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import org.jetbrains.annotations.NotNull;

import java.util.TreeSet;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    public static final String MARKER_KEY = "MarkerKeyEventID";
    Event currentSelectedEvent = null;
    private GoogleMap map;
    private ImageView infoBoxIcon;
    private TextView infoBoxName;
    private TextView infoBoxEvent;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        if (getActivity() instanceof MainActivity) {
            setHasOptionsMenu(true);
        }
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Iconify.with(new FontAwesomeModule());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (getArguments() != null) {
            String eventID = getArguments().getString(MARKER_KEY, null);
            if (eventID != null) {
                currentSelectedEvent = DataCache.getMarkerEvent(eventID);
            }
        }
        infoBoxIcon = view.findViewById(R.id.infoIcon);
        infoBoxName = view.findViewById(R.id.infoName);
        infoBoxEvent = view.findViewById(R.id.infoEvent);
        ConstraintLayout infoBox = view.findViewById(R.id.infoBox);
        infoBox.setOnClickListener(v -> {
            if (currentSelectedEvent != null) {
                Intent intent = new Intent(v.getContext(), PersonActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(PersonActivity.PERSON_ID_KEY, currentSelectedEvent.getPersonID());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NotNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.searchMenuItem);
        searchMenuItem.setIcon(new IconDrawable(this.getContext(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.colorWhite)
                .actionBarSize());

        MenuItem settingsMenuItem = menu.findItem(R.id.settingsMenuItem);
        settingsMenuItem.setIcon(new IconDrawable(this.getContext(), FontAwesomeIcons.fa_gear)
                .colorRes(R.color.colorWhite)
                .actionBarSize());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case R.id.searchMenuItem:
                Intent intent = new Intent(this.getContext(), SearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            case R.id.settingsMenuItem:
                intent = new Intent(this.getContext(), SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(marker -> {
            currentSelectedEvent = (Event) marker.getTag();
            loadAllPins();
            return true;
        });
        loadAllPins();
    }

    private void drawLines() {
        if (currentSelectedEvent != null) {
            LatLng eventPos = new LatLng(currentSelectedEvent.getLatitude(), currentSelectedEvent.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(eventPos));
            Person clickedPerson = DataCache.getPerson(currentSelectedEvent.getPersonID());

            if (clickedPerson.getGender().equals("m")) {
                infoBoxIcon.setImageDrawable(new IconDrawable(this.getContext(), FontAwesomeIcons.fa_male).
                        colorRes(R.color.male_icon).sizeDp(60));
            } else {
                infoBoxIcon.setImageDrawable(new IconDrawable(this.getContext(), FontAwesomeIcons.fa_female).
                        colorRes(R.color.female_icon).sizeDp(60));
            }
            infoBoxName.setText(String.format("%s %s", clickedPerson.getFirstName(), clickedPerson.getLastName()));
            infoBoxEvent.setText(String.format("%S: %s, %s (%s)", currentSelectedEvent.getEventType(),
                    currentSelectedEvent.getCity(), currentSelectedEvent.getCountry(), currentSelectedEvent.getYear()));
            if (DataCache.showSpouseLines) {
                if (clickedPerson.getSpouseID() != null) {
                    TreeSet<Event> spouseEvents = DataCache.getFilteredEvents(clickedPerson.getSpouseID());
                    if (spouseEvents != null)
                        map.addPolyline(new PolylineOptions().add(eventPos, new LatLng(spouseEvents.first().getLatitude(), spouseEvents.first().getLongitude())).width(10f).color(Color.BLUE));
                }
            }
            if (DataCache.showLifeLines) {
                TreeSet<Event> personEvents = DataCache.getFilteredEvents(clickedPerson.getPersonID());
                if (personEvents != null) {
                    PolylineOptions polyline = new PolylineOptions().width(10f).color(Color.GREEN);
                    for (Event e : personEvents) {
                        polyline.add(new LatLng(e.getLatitude(), e.getLongitude()));
                    }
                    map.addPolyline(polyline);
                }
            }
            if (DataCache.showFamilyTreeLines) {
                float baseWidth = 20;
                //Draw from current Event to Parents, from there recursively
                if (clickedPerson.getFatherID() != null) {
                    TreeSet<Event> tree = DataCache.getFilteredEvents(clickedPerson.getFatherID());
                    if (tree != null) {
                        Event second = tree.first();
                        map.addPolyline(new PolylineOptions().color(Color.CYAN).width(baseWidth).add(eventPos,
                                new LatLng(second.getLatitude(), second.getLongitude())));
                        addFamilyPolylinesHelper(DataCache.getPerson(clickedPerson.getFatherID()), baseWidth - 4);
                    }
                }
                if (clickedPerson.getMotherID() != null) {
                    TreeSet<Event> tree = DataCache.getFilteredEvents(clickedPerson.getMotherID());
                    if (tree != null) {
                        Event second = tree.first();
                        map.addPolyline(new PolylineOptions().color(Color.CYAN).width(baseWidth).add(eventPos,
                                new LatLng(second.getLatitude(), second.getLongitude())));
                        addFamilyPolylinesHelper(DataCache.getPerson(clickedPerson.getMotherID()), baseWidth - 4);
                    }
                }
            }
        }
    }

    private void addFamilyPolylinesHelper(Person clickedPerson, float currentWidth) {
        Event start = DataCache.getFilteredEvents(clickedPerson.getPersonID()).first();
        if (start == null) {
            return;
        }
        LatLng startLatLng = new LatLng(start.getLatitude(), start.getLongitude());
        if (clickedPerson.getFatherID() != null) {
            TreeSet<Event> tree = DataCache.getFilteredEvents(clickedPerson.getFatherID());
            if (tree != null) {
                Event second = tree.first();
                map.addPolyline(new PolylineOptions().color(Color.CYAN).width(currentWidth).add(startLatLng,
                        new LatLng(second.getLatitude(), second.getLongitude())));
                addFamilyPolylinesHelper(DataCache.getPerson(clickedPerson.getFatherID()), currentWidth - 4);
            }
        }
        if (clickedPerson.getMotherID() != null) {
            TreeSet<Event> tree = DataCache.getFilteredEvents(clickedPerson.getMotherID());
            if (tree != null) {
                Event second = tree.first();
                map.addPolyline(new PolylineOptions().color(Color.CYAN).width(currentWidth).add(startLatLng,
                        new LatLng(second.getLatitude(), second.getLongitude())));
                addFamilyPolylinesHelper(DataCache.getPerson(clickedPerson.getMotherID()), currentWidth - 4);
            }
        }
    }

    private void loadAllPins() {
        map.clear();

        BitmapDescriptor[] mapPinColors = new BitmapDescriptor[]{
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET),
                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW),
        };

        if (DataCache.maleEvents) {
            for (TreeSet<Event> tree : DataCache.familyMaleEventMap.values())
                for (Event e : tree) {
                    MarkerOptions eventMarkerOptions = new MarkerOptions().position(new LatLng(e.getLatitude(), e.getLongitude()));
                    Marker eventMarker = map.addMarker(eventMarkerOptions);
                    eventMarker.setTag(e);
                    eventMarker.setIcon(mapPinColors[(DataCache.eventTypes.indexOf(e.getEventType().toLowerCase())) % mapPinColors.length]);

                }
            if (DataCache.fatherEvents) {
                for (TreeSet<Event> tree : DataCache.fatherMaleEventMap.values())
                    for (Event e : tree) {
                        MarkerOptions eventMarkerOptions = new MarkerOptions().position(new LatLng(e.getLatitude(), e.getLongitude()));
                        Marker eventMarker = map.addMarker(eventMarkerOptions);
                        eventMarker.setTag(e);
                        eventMarker.setIcon(mapPinColors[(DataCache.eventTypes.indexOf(e.getEventType().toLowerCase())) % mapPinColors.length]);
                    }
            }
            if (DataCache.motherEvents) {
                for (TreeSet<Event> tree : DataCache.motherMaleEventMap.values())
                    for (Event e : tree) {
                        MarkerOptions eventMarkerOptions = new MarkerOptions().position(new LatLng(e.getLatitude(), e.getLongitude()));
                        Marker eventMarker = map.addMarker(eventMarkerOptions);
                        eventMarker.setTag(e);
                        eventMarker.setIcon(mapPinColors[(DataCache.eventTypes.indexOf(e.getEventType().toLowerCase())) % mapPinColors.length]);
                    }
            }
        }

        if (DataCache.femaleEvents) {
            for (TreeSet<Event> tree : DataCache.familyFemaleEventMap.values())
                for (Event e : tree) {
                    MarkerOptions eventMarkerOptions = new MarkerOptions().position(new LatLng(e.getLatitude(), e.getLongitude()));
                    Marker eventMarker = map.addMarker(eventMarkerOptions);
                    eventMarker.setTag(e);
                    eventMarker.setIcon(mapPinColors[(DataCache.eventTypes.indexOf(e.getEventType().toLowerCase())) % mapPinColors.length]);
                }
            if (DataCache.fatherEvents) {
                for (TreeSet<Event> tree : DataCache.fatherFemaleEventMap.values())
                    for (Event e : tree) {
                        MarkerOptions eventMarkerOptions = new MarkerOptions().position(new LatLng(e.getLatitude(), e.getLongitude()));
                        Marker eventMarker = map.addMarker(eventMarkerOptions);
                        eventMarker.setTag(e);
                        eventMarker.setIcon(mapPinColors[(DataCache.eventTypes.indexOf(e.getEventType().toLowerCase())) % mapPinColors.length]);
                    }
            }
            if (DataCache.motherEvents) {
                for (TreeSet<Event> tree : DataCache.motherFemaleEventMap.values())
                    for (Event e : tree) {
                        MarkerOptions eventMarkerOptions = new MarkerOptions().position(new LatLng(e.getLatitude(), e.getLongitude()));
                        Marker eventMarker = map.addMarker(eventMarkerOptions);
                        eventMarker.setTag(e);
                        eventMarker.setIcon(mapPinColors[(DataCache.eventTypes.indexOf(e.getEventType().toLowerCase())) % mapPinColors.length]);
                    }
            }
        }

        if (currentSelectedEvent != null) {
            drawLines();
        } else {
            infoBoxName.setText(R.string.infoBoxInstruction);
            infoBoxEvent.setText("");
            infoBoxIcon.setImageDrawable(new IconDrawable(this.getContext(), FontAwesomeIcons.fa_android).
                    colorRes(R.color.androidIcon).sizeDp(60));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            if (currentSelectedEvent != null) {
                if (DataCache.getFilteredEvents(currentSelectedEvent.getPersonID()) == null) {
                    currentSelectedEvent = null;
                }
            }
            loadAllPins();
        }
    }
}
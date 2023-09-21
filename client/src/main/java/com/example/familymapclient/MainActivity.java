package com.example.familymapclient;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = this.getSupportFragmentManager();
        if (DataCache.getUser() != null) {
            MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.frameLayout);
            if (mapFragment == null) {
                mapFragment = createMapFragment();
                fm.beginTransaction()
                        .add(R.id.frameLayout, mapFragment)
                        .commit();
            }
        } else {
            LoginFragment loginFragment = (LoginFragment) fm.findFragmentById(R.id.frameLayout);
            if (loginFragment == null) {
                loginFragment = createLoginFragment();
                fm.beginTransaction()
                        .add(R.id.frameLayout, loginFragment)
                        .commit();
            }
        }
    }

    private LoginFragment createLoginFragment() {
        return new LoginFragment();
    }

    private MapFragment createMapFragment() {
        return new MapFragment();
    }
}
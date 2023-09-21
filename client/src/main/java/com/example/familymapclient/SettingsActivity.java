package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Switch lifeStoryLines, familyTreeLines, spouseLines, fatherSide, motherSide, maleFilter, femaleFilter;
    LinearLayout logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lifeStoryLines = findViewById(R.id.ShowLifeStoryLines);
        lifeStoryLines.setChecked(DataCache.showLifeLines);
        lifeStoryLines.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.showLifeLines = isChecked);

        familyTreeLines = findViewById(R.id.ShowFamilyTree);
        familyTreeLines.setChecked(DataCache.showFamilyTreeLines);
        familyTreeLines.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.showFamilyTreeLines = isChecked);

        spouseLines = findViewById(R.id.ShowSpouseLines);
        spouseLines.setChecked(DataCache.showSpouseLines);
        spouseLines.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.showSpouseLines = isChecked);

        fatherSide = findViewById(R.id.ShowFatherSide);
        fatherSide.setChecked(DataCache.fatherEvents);
        fatherSide.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.fatherEvents = isChecked);

        motherSide = findViewById(R.id.ShowMotherSide);
        motherSide.setChecked(DataCache.motherEvents);
        motherSide.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.motherEvents = isChecked);

        maleFilter = findViewById(R.id.ShowMale);
        maleFilter.setChecked(DataCache.maleEvents);
        maleFilter.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.maleEvents = isChecked);

        femaleFilter = findViewById(R.id.ShowFemale);
        femaleFilter.setChecked(DataCache.femaleEvents);
        femaleFilter.setOnCheckedChangeListener((buttonView, isChecked) -> DataCache.femaleEvents = isChecked);

        logout = findViewById(R.id.logoutLayout);
        logout.setOnClickListener(v -> {
            DataCache.userID = null;
            startActivity(new Intent(v.getContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this.getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
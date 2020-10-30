package com.babbangona.mspalybookupgrade.HarvestSummary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.babbangona.mspalybookupgrade.R;
import com.google.android.material.button.MaterialButton;

public class HarvestHomePage extends AppCompatActivity {

    MaterialButton harvestSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_home_page);

        harvestSummary = findViewById(R.id.btn_harvest_summary);
        harvestSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HarvestHomePage.this, HarvestTrustGroupList.class);
                startActivity(i);
            }
        });
    }
}

package com.babbangona.mspalybookupgrade.HarvestSummary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.babbangona.mspalybookupgrade.R;
import com.google.android.material.button.MaterialButton;

public class TrustGroupDetails extends AppCompatActivity {

    MaterialButton btn_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trust_group_details);

        btn_continue = findViewById(R.id.btn_continue_icon);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrustGroupDetails.this, HarvestSummaryPage.class);
                startActivity(intent);
            }
        });
    }
}

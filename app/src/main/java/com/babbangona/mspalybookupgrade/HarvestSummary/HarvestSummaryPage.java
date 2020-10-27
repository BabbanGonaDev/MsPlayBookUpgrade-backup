package com.babbangona.mspalybookupgrade.HarvestSummary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;

public class HarvestSummaryPage extends AppCompatActivity {

    MaterialButton btn_tgHarvestSummary, btn_memberHarvestSummary;
    ImageView backNav;
    TextView dateContainer, staffIdContainer;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_summary_page);

        btn_tgHarvestSummary = findViewById(R.id.view_tg_harvest_summary_bt);
        btn_memberHarvestSummary = findViewById(R.id.view_member_harvest_summary_bt);
        backNav = findViewById(R.id.back_arrow);
        dateContainer = findViewById(R.id.sync_date_notifier);
        staffIdContainer = findViewById(R.id.staff_id_notifier);
        sharedPrefs = new SharedPrefs(this);

        displayDetails();
        handleAllClicks();
    }

    public void displayDetails(){

        staffIdContainer.setText(sharedPrefs.getStaffID());
        dateContainer.setText(sharedPrefs.getKeyLastSyncTime());
    }

    public void handleAllClicks(){
        btn_tgHarvestSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HarvestSummaryPage.this, TrustGroupDetails.class);
                startActivity(intent);
            }
        });

        btn_memberHarvestSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HarvestSummaryPage.this, TrustGroupMembers.class);
                startActivity(intent);
            }
        });

        backNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HarvestSummaryPage.this, HarvestTrustGroupList.class);
                startActivity(intent);
            }
        });
    }
}

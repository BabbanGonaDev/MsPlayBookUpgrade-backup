package com.babbangona.mspalybookupgrade.HarvestSummary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.HarvestSummary.adapters.HarvestTrustGroupListAdapterView;
import com.babbangona.mspalybookupgrade.HarvestSummary.adapters.TrustGroupMembersAdapterView;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import java.util.List;

public class TrustGroupMembers extends AppCompatActivity {

    AppDatabase appDatabase;
    RecyclerView recyclerView;
    SharedPrefs sharedPrefs;
    List<Members> allTgMembers;
    TrustGroupMembersAdapterView trustGroupMembersAdapterView;
    ImageView backNav;
    TextView dateContainer, staffIdContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trust_group_members);

        backNav = findViewById(R.id.back_arrow);
        recyclerView = findViewById(R.id.trust_group_members_recycler);
        dateContainer = findViewById(R.id.sync_date_notifier);
        staffIdContainer = findViewById(R.id.staff_id_notifier);
        appDatabase = AppDatabase.getInstance(this);
        sharedPrefs = new SharedPrefs(this);
        setBackNav();
        displayDetails();

        allTgMembers = appDatabase.membersDao().getAllTgMembers(sharedPrefs.getKeyHarvestSummaryIkNumber());

        handleRecyclerView();
    }

    public void displayDetails(){

        staffIdContainer.setText(sharedPrefs.getStaffID());
        dateContainer.setText(sharedPrefs.getKeyLastSyncTime());
    }

    public void handleRecyclerView(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        trustGroupMembersAdapterView = new TrustGroupMembersAdapterView(this,allTgMembers);
        recyclerView.setAdapter(trustGroupMembersAdapterView);
    }

    public void setBackNav(){

        backNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TrustGroupMembers.this, HarvestSummaryPage.class);
                startActivity(intent);
            }
        });
    }
}

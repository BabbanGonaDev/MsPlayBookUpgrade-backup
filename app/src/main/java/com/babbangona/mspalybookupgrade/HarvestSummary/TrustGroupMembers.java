package com.babbangona.mspalybookupgrade.HarvestSummary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trust_group_members);

        recyclerView = findViewById(R.id.trust_group_members_recycler);
        appDatabase = AppDatabase.getInstance(this);
        sharedPrefs = new SharedPrefs(this);

        allTgMembers = appDatabase.membersDao().getAllTgMembers(sharedPrefs.getKeyHarvestSummaryIkNumber());

        handleRecyclerView();
    }

    public void handleRecyclerView(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        trustGroupMembersAdapterView = new TrustGroupMembersAdapterView(this,allTgMembers);
        recyclerView.setAdapter(trustGroupMembersAdapterView);
    }
}

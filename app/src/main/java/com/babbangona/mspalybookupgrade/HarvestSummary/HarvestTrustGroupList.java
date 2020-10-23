package com.babbangona.mspalybookupgrade.HarvestSummary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

import com.babbangona.mspalybookupgrade.HarvestSummary.adapters.HarvestTrustGroupListAdapterView;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;

import java.util.ArrayList;
import java.util.List;

public class HarvestTrustGroupList extends AppCompatActivity {

    AppDatabase appDatabase;
    SearchView searchView;
    RecyclerView recyclerView;
    HarvestTrustGroupListAdapterView harvestTrustGroupListAdapterView;
    List<Members> allTgLeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_trust_group_list);

        recyclerView = findViewById(R.id.trust_group_list_recycler);
        searchView = findViewById(R.id.issue_list_search_view);
        appDatabase = AppDatabase.getInstance(this);
        searchIssues(searchView);

        allTgLeaders = appDatabase.membersDao().getAllLeaders();
        handleRecyclerView();
    }

    public void handleRecyclerView(){

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        harvestTrustGroupListAdapterView = new HarvestTrustGroupListAdapterView(this,allTgLeaders);
        recyclerView.setAdapter(harvestTrustGroupListAdapterView);
    }

    private void searchIssues(final SearchView searchView1){

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                harvestTrustGroupListAdapterView.getFilter().filter(s);

                return true;
            }
        });
    }

}

package com.babbangona.mspalybookupgrade.HarvestSummary;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.HarvestSummary.adapters.HarvestTrustGroupListAdapterView;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import java.util.List;

public class HarvestTrustGroupList extends AppCompatActivity {

    SharedPrefs sharedPrefs;
    AppDatabase appDatabase;
    SearchView searchView;
    RecyclerView recyclerView;
    HarvestTrustGroupListAdapterView harvestTrustGroupListAdapterView;
    List<Members> allTgLeaders;
    ImageView backNav;
    TextView dateContainer, staffIdContainer, appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_trust_group_list);

        backNav = findViewById(R.id.back_arrow);
        recyclerView = findViewById(R.id.trust_group_list_recycler);
        searchView = findViewById(R.id.issue_list_search_view);
        dateContainer = findViewById(R.id.sync_date_notifier);
        staffIdContainer = findViewById(R.id.staff_id_notifier);
        appVersion = findViewById(R.id.donotpay_app_version);
        appDatabase = AppDatabase.getInstance(this);
        sharedPrefs = new SharedPrefs(this);
        displayDetails();
        searchIssues(searchView);
        setBackNav();

        allTgLeaders = appDatabase.membersDao().getAllLeaders();
        handleRecyclerView();
    }

    public void displayDetails() {

        staffIdContainer.setText(sharedPrefs.getStaffID());
        dateContainer.setText(sharedPrefs.getKeyLastSyncTime());
    }

    public void handleRecyclerView() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        harvestTrustGroupListAdapterView = new HarvestTrustGroupListAdapterView(this, allTgLeaders);
        recyclerView.setAdapter(harvestTrustGroupListAdapterView);
    }

    private void searchIssues(final SearchView searchView1) {

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

    public void setBackNav() {

        backNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

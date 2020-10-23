package com.babbangona.mspalybookupgrade.HarvestSummary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SearchView;

import com.babbangona.mspalybookupgrade.R;

public class HarvestTrustGroupList extends AppCompatActivity {

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_trust_group_list);

        searchView = findViewById(R.id.issue_list_search_view);
        searchIssues(searchView);
    }

    private void searchIssues(final SearchView searchView1){

        searchView1.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                //issueListAdapterView.getFilter().filter(s);

                return true;
            }
        });
    }

}

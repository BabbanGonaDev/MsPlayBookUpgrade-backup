package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.babbangona.mspalybookupgrade.ComingSoon;
import com.babbangona.mspalybookupgrade.Homepage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ActivityListRecycler.ActivityListAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler.ThreshingFieldListAdapter;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FieldList extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    ThreshingFieldListAdapter threshingFieldListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_list);
        ButterKnife.bind(FieldList.this);
        appDatabase = AppDatabase.getInstance(FieldList.this);
        sharedPrefs = new SharedPrefs(FieldList.this);
        initActivitiesRecycler();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void initActivitiesRecycler(){

        appDatabase
                .fieldsDao()
                .getThreshingMemberFields(sharedPrefs.getKeyThreshingUniqueMemberId())
                .observe(this,activityLists -> {
                    threshingFieldListAdapter = new ThreshingFieldListAdapter(
                            appDatabase.fieldsDao().getThreshingMemberFieldsList(sharedPrefs.getKeyThreshingUniqueMemberId()),
                            FieldList.this);
                    RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(FieldList.this);
                    recycler_view.setLayoutManager(aLayoutManager);
                    recycler_view.setItemAnimator(new DefaultItemAnimator());
                    recycler_view.setAdapter(threshingFieldListAdapter);
                    threshingFieldListAdapter.notifyDataSetChanged();
                });

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(FieldList.this, ThreshingActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.threshing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.schedule) {
            startActivity(new Intent(FieldList.this, ComingSoon.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
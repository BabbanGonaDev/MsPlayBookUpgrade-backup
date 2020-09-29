package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler.ScheduledFieldListAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler.ThreshingFieldListAdapter;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduleDate extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.last_sync_date_tv)
    TextView last_sync_date_tv;

    @BindView(R.id.tv_staff_id)
    TextView tv_staff_id;

    @BindView(R.id.actScheduledDates)
    AutoCompleteTextView actScheduledDates;

    @BindView(R.id.edlScheduledDates)
    TextInputLayout edlScheduledDates;

    String date_selected;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    SetPortfolioMethods setPortfolioMethods;

    ScheduledFieldListAdapter scheduledFieldListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_date_top);
        ButterKnife.bind(ScheduleDate.this);
        appDatabase = AppDatabase.getInstance(ScheduleDate.this);
        sharedPrefs = new SharedPrefs(ScheduleDate.this);
        setPortfolioMethods = new SetPortfolioMethods();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(ScheduleDate.this));


        fillScheduledDatesSpinner(actScheduledDates, ScheduleDate.this);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        actScheduledDates.setOnItemClickListener((parent, view, position, id) -> {
            date_selected = (String)parent.getItemAtPosition(position);
            initActivitiesRecycler(date_selected);
        });
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,ScheduleDate.this);
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
            finish();
            startActivity(new Intent(ScheduleDate.this, CalenderViewActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void fillScheduledDatesSpinner(AutoCompleteTextView autoCompleteTextView, Context context) {
        ArrayAdapter village;
        village = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.scheduleThreshingActivitiesFlagDao().getScheduleDateCount(sharedPrefs.getStaffID()));
        spinnerViewController(autoCompleteTextView,village);
    }

    public void spinnerViewController(AutoCompleteTextView autoCompleteTextView, ArrayAdapter arrayAdapter) {
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    public void initActivitiesRecycler(String schedule_date){

        appDatabase
                .fieldsDao()
                .getScheduleThreshingFields(sharedPrefs.getStaffID(),schedule_date)
                .observe(this,activityLists -> {
                    scheduledFieldListAdapter = new ScheduledFieldListAdapter(
                            appDatabase.fieldsDao().getScheduleThreshingFieldsList(sharedPrefs.getStaffID(),schedule_date),
                            ScheduleDate.this);
                    RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(ScheduleDate.this);
                    recycler_view.setLayoutManager(aLayoutManager);
                    recycler_view.setItemAnimator(new DefaultItemAnimator());
                    recycler_view.setAdapter(scheduledFieldListAdapter);
                    scheduledFieldListAdapter.notifyDataSetChanged();
                });

    }
}
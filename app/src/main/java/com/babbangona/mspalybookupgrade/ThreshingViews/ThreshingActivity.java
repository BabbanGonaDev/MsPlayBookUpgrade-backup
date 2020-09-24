package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.babbangona.mspalybookupgrade.ComingSoon;
import com.babbangona.mspalybookupgrade.Homepage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThreshingActivity extends AppCompatActivity {

    @BindView(R.id.btnScheduleThreshing)
    MaterialButton btnScheduleThreshing;

    @BindView(R.id.btnUpdateScheduleThreshing)
    MaterialButton btnUpdateScheduleThreshing;

    @BindView(R.id.btnConfirmThreshing)
    MaterialButton btnConfirmThreshing;

    @BindView(R.id.btnSummary)
    MaterialButton btnSummary;

    @BindView(R.id.btnMarkHG)
    MaterialButton btnMarkHG;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    SharedPrefs sharedPrefs;

    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshing);
        ButterKnife.bind(ThreshingActivity.this);
        sharedPrefs = new SharedPrefs(ThreshingActivity.this);
        appDatabase = AppDatabase.getInstance(ThreshingActivity.this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @OnClick(R.id.btnScheduleThreshing)
    public void setBtnScheduleThreshing(){
        sharedPrefs.setKeyThreshingActivityRoute(DatabaseStringConstants.SCHEDULE_THRESHING);
        if (sharedPrefs.getStaffID().equalsIgnoreCase("T-10000000000000BB")){
            appDatabase.membersDao().updateCoach();
        }
        startActivity(new Intent(ThreshingActivity.this,MemberList.class));
    }

    @OnClick(R.id.btnUpdateScheduleThreshing)
    public void setBtnUpdateScheduleThreshing(){
        sharedPrefs.setKeyThreshingActivityRoute(DatabaseStringConstants.UPDATE_THRESHING);
        startActivity(new Intent(ThreshingActivity.this,MemberList.class));
    }

    @OnClick(R.id.btnConfirmThreshing)
    public void setBtnConfirmThreshing(){
        sharedPrefs.setKeyThreshingActivityRoute(DatabaseStringConstants.CONFIRM_THRESHING);
        startActivity(new Intent(ThreshingActivity.this,MemberList.class));
    }

    @OnClick(R.id.btnSummary)
    public void setBtnSummary(){
        startActivity(new Intent(ThreshingActivity.this, ComingSoon.class));
    }

    @OnClick(R.id.btnMarkHG)
    public void setBtnMarkHG(){
        sharedPrefs.setKeyThreshingActivityRoute(DatabaseStringConstants.MARK_HG_AT_RISK);
        startActivity(new Intent(ThreshingActivity.this,MemberList.class));
    }

    @Override
    public void onBackPressed(){
        loadPreviousActivity();
    }

    public void loadPreviousActivity() {
        startActivity(new Intent(ThreshingActivity.this, Homepage.class));
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
            startActivity(new Intent(ThreshingActivity.this, ComingSoon.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
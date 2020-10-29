package com.babbangona.mspalybookupgrade.ThreshingViews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.babbangona.mspalybookupgrade.Homepage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

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

    @BindView(R.id.btnThreshingSchedules)
    MaterialButton btnThreshingSchedules;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    SharedPrefs sharedPrefs;

    AppDatabase appDatabase;

    @BindView(R.id.last_sync_date_tv)
    TextView last_sync_date_tv;

    @BindView(R.id.tv_staff_id)
    TextView tv_staff_id;

    SetPortfolioMethods setPortfolioMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshing_top);
        ButterKnife.bind(ThreshingActivity.this);
        setSupportActionBar(toolbar);
        setPortfolioMethods = new SetPortfolioMethods();
        sharedPrefs = new SharedPrefs(ThreshingActivity.this);
        appDatabase = AppDatabase.getInstance(ThreshingActivity.this);
        sharedPrefs.setKeyRescheduleStateFlag("0");


        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(ThreshingActivity.this));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,ThreshingActivity.this);
        removeScheduleAndUpdate();
    }

    @OnClick(R.id.btnScheduleThreshing)
    public void setBtnScheduleThreshing(){
        sharedPrefs.setKeyThreshingActivityRoute(DatabaseStringConstants.SCHEDULE_THRESHING);
        /*if (sharedPrefs.getStaffID().equalsIgnoreCase("T-10000000000000BB")){
            appDatabase.membersDao().updateCoach();
            appDatabase.membersDao().updateMemberBgtID();
        }*/
        startActivity(new Intent(ThreshingActivity.this,MemberList.class));
    }

    @OnClick(R.id.btnUpdateScheduleThreshing)
    public void setBtnUpdateScheduleThreshing() {
        sharedPrefs.setKeyThreshingActivityRoute(DatabaseStringConstants.UPDATE_THRESHING);
        Log.d("CHECK", "Route set: ===========> " + DatabaseStringConstants.UPDATE_THRESHING);
        startActivity(new Intent(ThreshingActivity.this, MemberList.class));
    }

    @OnClick(R.id.btnConfirmThreshing)
    public void setBtnConfirmThreshing(){
        sharedPrefs.setKeyThreshingActivityRoute(DatabaseStringConstants.CONFIRM_THRESHING);
        startActivity(new Intent(ThreshingActivity.this,MemberList.class));
    }

    @OnClick(R.id.btnSummary)
    public void setBtnSummary(){
        startActivity(new Intent(ThreshingActivity.this, ScheduleThreshingSummary.class));
    }

    @OnClick(R.id.btnMarkHG)
    public void setBtnMarkHG(){
        sharedPrefs.setKeyThreshingActivityRoute(DatabaseStringConstants.MARK_HG_AT_RISK);
        startActivity(new Intent(ThreshingActivity.this,MemberList.class));
    }

    @OnClick(R.id.btnThreshingSchedules)
    public void setBtnThreshingSchedules(){
        startActivity(new Intent(ThreshingActivity.this,ScheduledThreshingView.class));
    }

    @Override
    public void onBackPressed() {
        loadPreviousActivity();
    }

    public void loadPreviousActivity() {
        startActivity(new Intent(ThreshingActivity.this, Homepage.class));
    }

    //Remove the calendar icon from homepage.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.threshing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        /*if (item.getItemId() == R.id.schedule) {
            startActivity(new Intent(ThreshingActivity.this, CalenderViewActivity.class));
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    void removeScheduleAndUpdate(){
        if (sharedPrefs.getStaffRole().equalsIgnoreCase("BGT")){
            btnScheduleThreshing.setVisibility(View.VISIBLE);
            btnUpdateScheduleThreshing.setVisibility(View.VISIBLE);
            btnMarkHG.setVisibility(View.VISIBLE);
        }else{
            btnScheduleThreshing.setVisibility(View.GONE);
            btnUpdateScheduleThreshing.setVisibility(View.GONE);
            btnMarkHG.setVisibility(View.GONE);

        }
    }
}
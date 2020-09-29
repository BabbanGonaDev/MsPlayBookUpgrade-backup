package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.CalendarView;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalenderViewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.calendar_view)
    CalendarView calendar_view;

    @BindView(R.id.last_sync_date_tv)
    TextView last_sync_date_tv;

    @BindView(R.id.tv_staff_id)
    TextView tv_staff_id;

    @BindView(R.id.btnUpdateScheduleThreshing)
    MaterialButton btnUpdateScheduleThreshing;

    SetPortfolioMethods setPortfolioMethods;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    List<String> dateList;
    List<Calendar> calenderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_view_top);
        ButterKnife.bind(CalenderViewActivity.this);
        appDatabase = AppDatabase.getInstance(CalenderViewActivity.this);
        sharedPrefs = new SharedPrefs(CalenderViewActivity.this);
        setSupportActionBar(toolbar);
        setPortfolioMethods = new SetPortfolioMethods();
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(CalenderViewActivity.this));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,CalenderViewActivity.this);

        dateList = appDatabase.scheduleThreshingActivitiesFlagDao().getScheduleDateCount(sharedPrefs.getStaffID());

        calendar_view.updateCalendar(ScheduledThreshingActivitiesFlag.convertToCalenderList(dateList));
//        setDate(dateCountList,calendar_view);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @OnClick(R.id.btnUpdateScheduleThreshing)
    void setBtnUpdateScheduleThreshing(){
        finish();
        startActivity(new Intent(CalenderViewActivity.this, ScheduleDate.class));
    }

    void setDate(List<ScheduledThreshingActivitiesFlag.DateCount> dateCountList, CalendarView calender_view){

        for (ScheduledThreshingActivitiesFlag.DateCount dateCount:dateCountList){
//            calender_view.setDate(Long.parseLong(dateCount.getDate()));

            Log.d("damilola_cal", dateCount.getDate());
        }
    }
}
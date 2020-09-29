package com.babbangona.mspalybookupgrade.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CalendarAdapter extends ArrayAdapter<Date> {
    // for view inflation
    private LayoutInflater inflater;
    private List<Calendar> dateCountList;
    AppDatabase appDatabase;
    SharedPrefs sharedPrefs;

    public CalendarAdapter(Context context, ArrayList<Date> days, List<Calendar> dateCountList)
    {
        super(context, R.layout.custom_calendar_day, days);
        this.dateCountList = dateCountList;
        inflater = LayoutInflater.from(context);
        appDatabase = AppDatabase.getInstance(context);
        sharedPrefs = new SharedPrefs(context);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NotNull
    @Override
    public View getView(int position, View view, @NotNull ViewGroup parent)
    {
        // day in question
        Calendar calendar = Calendar.getInstance();
        Date date = getItem(position);
        if (date != null) {
            calendar.setTime(date);
        }
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // today
        Date today = new Date();
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTime(today);

        // inflate item if it does not exist yet
        if (view == null)
            view = inflater.inflate(R.layout.custom_calendar_day, parent, false);
        // clear styling
        TextView tv_count = view.findViewById(R.id.tv_count);
        TextView tv_date_text = view.findViewById(R.id.tv_date_text);

        if (month != calendarToday.get(Calendar.MONTH) || year != calendarToday.get(Calendar.YEAR)) {
            // if this day is outside current month, grey it out
            tv_date_text.setTextColor(view.getResources().getColor(R.color.text_color_black));
        } else if (day == calendarToday.get(Calendar.DATE)) {
            // if it is today, set it to blue/bold
            tv_date_text.setTextColor(view.getResources().getColor(R.color.text_color_white));
            view.setBackgroundResource(R.drawable.date_background);
        }

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formatted = format1.format(calendar.getTime());
        int count = appDatabase.scheduleThreshingActivitiesFlagDao().getDateCount(sharedPrefs.getStaffID(),formatted);

        if (count > 0){
            Log.d("damilola_cal_1",calendar.getTime()+"");
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText(String.valueOf(count));
        }else {
            tv_count.setVisibility(View.GONE);
        }


        // set text
        tv_date_text.setText(String.valueOf(calendar.get(Calendar.DATE)));

        return view;
    }

    Calendar getCalendar(String given_date){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            cal.setTime(Objects.requireNonNull(sdf.parse(given_date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }
}
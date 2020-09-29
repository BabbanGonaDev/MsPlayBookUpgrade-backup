package com.babbangona.mspalybookupgrade.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.babbangona.mspalybookupgrade.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarView extends LinearLayout implements View.OnClickListener, View.OnTouchListener {
    // calendar components
    LinearLayout header;
    TextView date_display_date;
    GridView gridView;
    ImageButton calendar_prev_button;
    ImageButton calendar_next_button;

    private int mYear;
    private int mCurrentDay, mCurrentMonth, mCurrentYear;

    private Calendar mCalender;

    int DAYS_COUNT = 42;
    public static String CURRENT_DATE ;

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initControl(context);
    }

    private void assignUiElements(View view) {
        // layout is inflated, assign local variables to components
        header = view.findViewById(R.id.calendar_header);
        date_display_date = view.findViewById(R.id.date_display_date);
        gridView = view.findViewById(R.id.calendar_grid);
        calendar_prev_button = view.findViewById(R.id.calendar_prev_button);
        calendar_next_button = view.findViewById(R.id.calendar_next_button);
        getDateTime();
    }

    /**
     * Load control xml layout
     */
    private void initControl(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
//            View view = inflater.inflate(R.layout.calender_layout, this,false);
            ViewGroup viewGroup = (ViewGroup) inflate(context , R.layout.calender_layout, null);
            addView(viewGroup);
            assignUiElements(viewGroup);
        }
    }

    private void getDateTime() {
        mCalender = Calendar.getInstance();
        mCurrentDay = mCalender.get(Calendar.DATE);
        mCurrentMonth = mCalender.get(Calendar.MONTH)+1;
        CURRENT_DATE = String.valueOf(mCalender.get(Calendar.DATE));
        Log.i("<<>>Cal<>",CURRENT_DATE);
        mCurrentYear = mCalender.get(Calendar.YEAR);
    }

    /**
     * Display dates correctly in grid
     */
    @SuppressLint("ClickableViewAccessibility")
    public void updateCalendar(List<Calendar> dateCountList)
    {

        calendar_next_button.setOnClickListener(v -> {

            //increment current month by one
            mCurrentMonth++;

            //if current month >= 13 then start from first month
            if (mCurrentMonth >= 13) {
                mCurrentMonth = 1;
                mCurrentYear ++;
            }
            mCalender.set(mCurrentYear,mCurrentMonth,mCurrentDay);
        });

        calendar_prev_button.setOnClickListener(v -> {

            //increment current month by one
            mCurrentMonth--;

            //if current month >= 13 then start from first month
            if (mCurrentMonth < 1) {
                mCurrentMonth = 12;
                mCurrentYear --;
            }
            mCalender.set(mCurrentYear,mCurrentMonth,mCurrentDay);
        });

        gridView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            public void onSwipeTop() {
                Log.i("top","");
            }
            public void onSwipeRight() {
                //increment current month by one
                mCurrentMonth++;

                //if current month >= 13 then start from first month
                if (mCurrentMonth >= 13) {
                    mCurrentMonth = 1;
                    mCurrentYear ++;
                }
                mCalender.set(mCurrentYear,mCurrentMonth,mCurrentDay);

            }
            public void onSwipeLeft() {
                //increment current month by one
                mCurrentMonth--;

                //if current month >= 13 then start from first month
                if (mCurrentMonth < 1) {
                    mCurrentMonth = 12;
                    mCurrentYear --;
                }
                mCalender.set(mCurrentYear,mCurrentMonth,mCurrentDay);
            }
            public void onSwipeBottom() {
                Log.i("bottom","");
            }

        });

        changingValues(dateCountList, mCalender);
    }

    void changingValues(List<Calendar> dateCountList, Calendar mCalender){

        ArrayList<Date> cells = new ArrayList<>();
        Calendar calendar = (Calendar) mCalender.clone();

        // determine the cell for current month's beginning
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int monthBeginningCell = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        // move calendar backwards to the beginning of the week
        calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell);

        // fill cells
        while (cells.size() < DAYS_COUNT)
        {
            cells.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // update grid
        gridView.setAdapter(new CalendarAdapter(getContext(), cells, dateCountList));

        // update title
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, yyyy", Locale.getDefault());
        String dateToday = sdf.format(mCalender.getTime());
        date_display_date.setText(dateToday);
    }

    @Override
    public void onClick(View v) {
        if (v == calendar_next_button){
            //increment current month by one
            mCurrentMonth++;

            //if current month >= 13 then start from first month
            if (mCurrentMonth >= 13) {
                mCurrentMonth = 1;
                mCurrentYear ++;
            }
            mCalender.set(mCurrentYear,mCurrentMonth,mCurrentDay);
        }
        Log.d("<<>>Dam","hello there");
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouchEvent(event);
        return false;
    }
}

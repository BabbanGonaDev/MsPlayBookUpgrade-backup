package com.babbangona.mspalybookupgrade.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.content.Context.WINDOW_SERVICE;

public class SetPortfolioMethods {

    /**
     * This method is used to add a staff ID to an MSS portfolio
     * @param portfolio Set of all the MIKs
     * @param staff_id Staff I to be added
     * @return Set of MSS portfolio with added MIK
     */
    public Set<String> addStaff(Set<String> portfolio, String staff_id) {
        portfolio.add(staff_id);
        return portfolio;
    }

    /**
     * This method is used to delete a staff ID from an MSS portfolio
     * @param portfolio Set of all the MIKs
     * @param staff_id Staff ID to be deleted
     * @return Set of MSS portfolio with removed MIK
     */
    public Set<String> removeStaff(Set<String> portfolio, String staff_id) {
        portfolio.remove(staff_id);
        return portfolio;
    }

    /**
     * This method is used to convert Set to ArrayLIst
     * @param portfolio Set of MSS portfolio
     * @return ArrayList of portfolio
     */
    public List<String> convertSetToList(Set<String> portfolio){
        return new ArrayList<>(portfolio);
    }

    public int getScreenWidthInDPs(Context context){
        /*
            DisplayMetrics
                A structure describing general information about a display,
                such as its size, density, and font scaling.
        */
        DisplayMetrics dm = new DisplayMetrics();

        /*
            WindowManager
                The interface that apps use to talk to the window manager.
                Use Context.getSystemService(Context.WINDOW_SERVICE) to get one of these.
        */

        /*
            public abstract Object getSystemService (String name)
                Return the handle to a system-level service by name. The class of the returned
                object varies by the requested name. Currently available names are:

                WINDOW_SERVICE ("window")
                    The top-level window manager in which you can place custom windows.
                    The returned object is a WindowManager.
        */

        /*
            public abstract Display getDefaultDisplay ()

                Returns the Display upon which this WindowManager instance will create new windows.

                Returns
                The display that this window manager is managing.
        */

        /*
            public void getMetrics (DisplayMetrics outMetrics)
                Gets display metrics that describe the size and density of this display.
                The size is adjusted based on the current rotation of the display.

                Parameters
                outMetrics A DisplayMetrics object to receive the metrics.
        */
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getMetrics(dm);
        }
        int widthInDP = Math.round(dm.widthPixels / dm.density);
        return getPixelsFromDPs( context, widthInDP);
    }

    // Method for converting DP/DIP value to pixels
    private int getPixelsFromDPs(Context activity, int dps){
        /*
            public abstract Resources getResources ()

                Return a Resources instance for your application's package.
        */
        Resources r = activity.getResources();

        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
    }

    public String getCategory(Context context){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        String category;
        try {
            category = appDatabase.categoryDao().getRoleCategory(sharedPrefs.getStaffRole());
        } catch (Exception e) {
            e.printStackTrace();
            category = "subd";
        }
        if (category == null || category.equalsIgnoreCase("")){
            category = "subd";
        }
        return category;
    }

    private double returnRightDoubleValue(String inputValue){
        if (inputValue == null || inputValue.equalsIgnoreCase("")){
            return 0;
        }else{
            return Double.parseDouble(inputValue);
        }
    }

    public double getLocationAverage(String value1, String value2){
        return (returnRightDoubleValue(value1)+returnRightDoubleValue(value2))/2;
    }

    public void setFooter(TextView tv_last_sync_time, TextView tv_staff_id, Context context){
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        String last_sync_time = parseDate(getLastSyncTimeStaffList(context));
        tv_last_sync_time.setText(last_sync_time);
        tv_staff_id.setText(sharedPrefs.getStaffID());
    }

    String getLastSyncTimeStaffList(Context context){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        SharedPrefs sharedPrefs = new SharedPrefs(context);
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncStaff(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2020-05-20 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2020-05-20 00:00:00";
        }
        return last_sync_time;
    }

    public String parseDate(String time) {
        String inputPattern = "yyyy-MM-dd hh:mm:ss";
        String outputPattern = "dd/MMM/yy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(time);
            if (date != null) {
                str = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}

package com.babbangona.mspalybookupgrade.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
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
        /**
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
        windowManager.getDefaultDisplay().getMetrics(dm);
        int widthInDP = Math.round(dm.widthPixels / dm.density);
        return getPixelsFromDPs( context, widthInDP);
    }


    // Method for converting DP/DIP value to pixels
    public int getPixelsFromDPs(Context activity, int dps){
        /*
            public abstract Resources getResources ()

                Return a Resources instance for your application's package.
        */
        Resources r = activity.getResources();

        return (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
    }
}

package com.babbangona.mspalybookupgrade.donotpay.data;

import android.content.Context;
import android.content.SharedPreferences;

public class DSessionManager {

    /**
     * App-specific keys. (Keys needed for proper operation of the application)
     */
    public static final String KEY_LOG_IN_STAFF_ID = "log_in_staff_id";
    public static final String KEY_LOG_IN_STAFF_NAME = "log_in_staff_name";


    private static final String PREF_NAME = "DNP Preferences";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context mCtx;
    int PRIVATE_MODE = 0;

    public DSessionManager(Context mCtx) {
        this.mCtx = mCtx;
        prefs = mCtx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    /**
     * =====================================================================
     * KEEP ALL SESSION SETTERS BELOW THIS LINE
     * =====================================================================
     */

    public void SET_LOG_IN_STAFF_ID(String value) {
        editor.putString(KEY_LOG_IN_STAFF_ID, value).commit();
    }

    public void SET_LOG_IN_STAFF_NAME(String value) {
        editor.putString(KEY_LOG_IN_STAFF_NAME, value).commit();
    }


    /**
     * =====================================================================
     * KEEP ALL SESSION GETTERS BELOW THIS LINE
     * =====================================================================
     */

    public String GET_LOG_IN_STAFF_ID() {
        return prefs.getString(KEY_LOG_IN_STAFF_ID, "");
    }

}

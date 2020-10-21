package com.babbangona.mspalybookupgrade.donotpay.data;

import android.content.Context;
import android.content.SharedPreferences;

public class DSessionManager {

    public static final String KEY_SELECTED_TG = "selected_tg";

    /**
     * App-specific keys. (Keys needed for proper operation of the application)
     */
    public static final String KEY_LOG_IN_STAFF_ID = "log_in_staff_id";
    public static final String KEY_LOG_IN_STAFF_NAME = "log_in_staff_name";

    /**
     * Regular needed keys for smooth operation of the app
     */
    public static final String KEY_LAST_SYNC_DNP_TABLE = "last_sync_dnp_table";
    public static final String KEY_LAST_SYNC_DNP_REASONS = "last_sync_dnp_reasons";


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

    public void SET_SELECTED_TG(String value) {
        editor.putString(KEY_SELECTED_TG, value).commit();
    }

    public void SET_LAST_SYNC_DNP_TABLE(String value) {
        editor.putString(KEY_LAST_SYNC_DNP_TABLE, value).commit();
    }

    public void SET_LAST_SYNC_DNP_REASONS(String value) {
        editor.putString(KEY_LAST_SYNC_DNP_REASONS, value).commit();
    }

    /**
     * =====================================================================
     * KEEP ALL SESSION GETTERS BELOW THIS LINE
     * =====================================================================
     */

    public String GET_LOG_IN_STAFF_ID() {
        return prefs.getString(KEY_LOG_IN_STAFF_ID, "");
    }

    public String GET_SELECTED_TG() {
        return prefs.getString(KEY_SELECTED_TG, "");
    }

    public String GET_LAST_SYNC_DNP() {
        return prefs.getString(KEY_LAST_SYNC_DNP_TABLE, "2020-10-20 00:00:00");
    }

    public String GET_LAST_SYNC_DNP_REASONS() {
        return prefs.getString(KEY_LAST_SYNC_DNP_REASONS, "2020-10-20 00:00:00");
    }
}

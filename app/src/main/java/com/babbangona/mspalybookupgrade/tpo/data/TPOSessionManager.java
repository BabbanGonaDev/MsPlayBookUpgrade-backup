package com.babbangona.mspalybookupgrade.tpo.data;

import android.content.Context;
import android.content.SharedPreferences;

public class TPOSessionManager {

    /**
     * These keys are used to store the details for marking attendance.
     */
    public static final String KEY_COLLECTION_CENTER_ID = "collection_center_id";
    public static final String KEY_SELECTED_MEMBER = "selected_member";

    /**
     * App-specific keys. (Keys needed for proper operation of the application)
     */
    public static final String KEY_LOG_IN_STAFF_ID = "log_in_staff_id";
    public static final String KEY_LOG_IN_STAFF_NAME = "log_in_staff_name";
    public static final String KEY_LAST_SYNC_TIME = "last_sync_time";


    private static final String PREF_NAME = "TPO Preferences";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context mCtx;
    int PRIVATE_MODE = 0;

    public TPOSessionManager(Context mCtx) {
        this.mCtx = mCtx;
        prefs = mCtx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    /**
     * =========================================================================
     * KEEP ALL SESSION SETTERS BELOW THIS LINE
     * =========================================================================
     */

    public void SET_COLLECTION_CENTER_ID(String value) {
        editor.putString(KEY_COLLECTION_CENTER_ID, value).commit();
    }

    public void SET_LOG_IN_STAFF_ID(String value) {
        editor.putString(KEY_LOG_IN_STAFF_ID, value).commit();
    }

    public void SET_LOG_IN_STAFF_NAME(String value) {
        editor.putString(KEY_LOG_IN_STAFF_NAME, value).commit();
    }

    public void SET_SELECTED_MEMBER(String value) {
        editor.putString(KEY_SELECTED_MEMBER, value).commit();
    }

    public void SET_LAST_SYNC_TIME(String value) {
        editor.putString(KEY_LAST_SYNC_TIME, value).commit();
    }

    /**
     * =========================================================================
     * KEEP ALL SESSION GETTERS BELOW THIS LINE
     * =========================================================================
     */

    public String GET_COLLECTION_CENTER_ID() {
        return prefs.getString(KEY_COLLECTION_CENTER_ID, "");
    }

    public String GET_LOG_IN_STAFF_ID() {
        return prefs.getString(KEY_LOG_IN_STAFF_ID, "");
    }

    public String GET_SELECTED_MEMBER() {
        return prefs.getString(KEY_SELECTED_MEMBER, "");
    }

    public String GET_LAST_SYNC_TIME() {
        return prefs.getString(KEY_LAST_SYNC_TIME, "2020-10-27 00:00:00");
    }
}

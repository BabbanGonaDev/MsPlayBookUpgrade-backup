package com.babbangona.mspalybookupgrade.transporter.data;

import android.content.Context;
import android.content.SharedPreferences;

public class TSessionManager {

    /**
     * These keys are used to store the details of a currently registering transporter.
     */
    public static final String KEY_REG_PHONE_NUMBER = "reg_phone_number";
    public static final String KEY_REG_FIRST_NAME = "reg_first_name";
    public static final String KEY_REG_LAST_NAME = "reg_last_name";
    public static final String KEY_REG_FACE_TEMPLATE = "reg_face_template";
    public static final String KEY_REG_VEHICLE_TYPE = "reg_vehicle_type";
    public static final String KEY_REG_COLLECTION_CENTERS = "reg_collection_centers";

    /**
     * App-specific keys. (Keys needed for proper operation of the application)
     */
    public static final String KEY_LOG_IN_STAFF_ID = "log_in_staff_id";
    public static final String KEY_LOG_IN_STAFF_NAME = "log_in_staff_name";
    public static final String KEY_LAST_SYNC_TRANSPORTER = "last_sync_transporter_table";
    public static final String KEY_LAST_SYNC_CC = "last_sync_cc_table";
    public static final String KEY_LAST_SYNC_OPERATING_AREAS = "last_sync_operating_areas_table";


    private static final String PREF_NAME = "Transporter Preferences";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context mCtx;
    int PRIVATE_MODE = 0;


    public TSessionManager(Context mCtx) {
        this.mCtx = mCtx;
        prefs = mCtx.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    /**
     * =========================================================================
     * KEEP ALL SESSION SETTERS BELOW THIS LINE
     * =========================================================================
     */

    public void SET_REG_PHONE_NUMBER(String value) {
        editor.putString(KEY_REG_PHONE_NUMBER, value).commit();
    }

    public void SET_REG_FIRST_NAME(String value) {
        editor.putString(KEY_REG_FIRST_NAME, value).commit();
    }

    public void SET_REG_LAST_NAME(String value) {
        editor.putString(KEY_REG_LAST_NAME, value).commit();
    }

    public void SET_REG_FACE_TEMPLATE(String value) {
        editor.putString(KEY_REG_FACE_TEMPLATE, value).commit();
    }

    public void SET_REG_VEHICLE_TYPE(String value) {
        editor.putString(KEY_REG_VEHICLE_TYPE, value).commit();
    }

    public void SET_REG_COLLECTION_CENTERS(String value) {
        editor.putString(KEY_REG_COLLECTION_CENTERS, value).commit();
    }

    public void SET_LOG_IN_STAFF_ID(String value) {
        editor.putString(KEY_LOG_IN_STAFF_ID, value).commit();
    }

    public void SET_LOG_IN_STAFF_NAME(String value) {
        editor.putString(KEY_LOG_IN_STAFF_NAME, value).commit();
    }

    public void SET_LAST_SYNC_TRANSPORTER(String value) {
        editor.putString(KEY_LAST_SYNC_TRANSPORTER, value).commit();
    }

    public void SET_LAST_SYNC_CC(String value) {
        editor.putString(KEY_LAST_SYNC_CC, value).commit();
    }

    public void SET_LAST_SYNC_OPERATING_AREAS(String value) {
        editor.putString(KEY_LAST_SYNC_OPERATING_AREAS, value).commit();
    }


    /**
     * =========================================================================
     * KEEP ALL SESSION GETTERS BELOW THIS LINE
     * =========================================================================
     */

    public String GET_REG_PHONE_NUMBER() {
        return prefs.getString(KEY_REG_PHONE_NUMBER, "");
    }

    public String GET_REG_FIRST_NAME() {
        return prefs.getString(KEY_REG_FIRST_NAME, "");
    }

    public String GET_REG_LAST_NAME() {
        return prefs.getString(KEY_REG_LAST_NAME, "");
    }

    public String GET_REG_FACE_TEMPLATE() {
        return prefs.getString(KEY_REG_FACE_TEMPLATE, "");
    }

    public String GET_REG_VEHICLE_TYPE() {
        return prefs.getString(KEY_REG_VEHICLE_TYPE, "");
    }

    public String GET_REG_COLLECTION_CENTERS() {
        return prefs.getString(KEY_REG_COLLECTION_CENTERS, "");
    }

    public String GET_LOG_IN_STAFF_ID() {
        return prefs.getString(KEY_LOG_IN_STAFF_ID, "");
    }

    public String GET_LOG_IN_STAFF_NAME() {
        return prefs.getString(KEY_LOG_IN_STAFF_NAME, "");
    }

    public String GET_LAST_SYNC_TRANSPORTER() {
        return prefs.getString(KEY_LAST_SYNC_TRANSPORTER, "2020-08-26 00:00:00");
    }

    public String GET_LAST_SYNC_CC() {
        return prefs.getString(KEY_LAST_SYNC_CC, "2020-08-26 00:00:00");
    }

    public String GET_LAST_SYNC_OPERATING_AREAS() {
        return prefs.getString(KEY_LAST_SYNC_OPERATING_AREAS, "2020-08-26 00:00:00");
    }

    /**
     * =========================================================================
     * KEEP ALL SESSION CLEARING BELOW THIS LINE
     * =========================================================================
     */

    public void CLEAR_REG_SESSION() {
        editor.remove(KEY_REG_PHONE_NUMBER)
                .remove(KEY_REG_FIRST_NAME)
                .remove(KEY_REG_LAST_NAME)
                .remove(KEY_REG_FACE_TEMPLATE)
                .remove(KEY_REG_VEHICLE_TYPE)
                .remove(KEY_REG_COLLECTION_CENTERS)
                .commit();
    }
}

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
                .commit();
    }
}

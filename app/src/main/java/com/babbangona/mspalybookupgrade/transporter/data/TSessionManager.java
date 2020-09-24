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
    public static final String KEY_REG_FACE_TEMPLATE_FLAG = "reg_face_template_flag";
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
    public static final String KEY_LAST_SYNC_CARDS = "last_sync_cards_table";

    /**
     * These keys are used to store details for Transporter booking and assignment
     */
    public static final String KEY_TRANSPORTED_BY = "transported_by";
    public static final String KEY_UNIQUE_MEMBER_ID = "unique_member_id";
    public static final String KEY_SELECTED_TRANSPORTER = "selected_transporter";
    public static final String KEY_SELECTED_CC_ID = "selected_cc_id";
    public static final String KEY_QTY_TRANSPORTED = "quantity_transported";

    /**
     * List of image names to be used for syncing.
     */
    public static final String KEY_TRANSPORTER_CARDS = "TRANSPORTER_CARDS";
    public static final String KEY_TRANSPORTER_FACES = "TRANSPORTER_FACES";

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

    public void SET_REG_FACE_TEMPLATE_FLAG(Integer value) {
        editor.putInt(KEY_REG_FACE_TEMPLATE_FLAG, value).commit();
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

    public void SET_LAST_SYNC_CARDS(String value) {
        editor.putString(KEY_LAST_SYNC_CARDS, value).commit();
    }

    public void SET_TRANSPORTER_CARDS(String value) {
        String cards_list = prefs.getString(KEY_TRANSPORTER_CARDS, "");
        if (cards_list.isEmpty()) {
            editor.putString(KEY_TRANSPORTER_CARDS, value).commit();
        } else {
            editor.putString(KEY_TRANSPORTER_CARDS, cards_list + "," + value).commit();
        }
    }

    public void SET_TRANSPORTER_FACES(String value) {
        String faces_list = prefs.getString(KEY_TRANSPORTER_FACES, "");
        if (faces_list.isEmpty()) {
            editor.putString(KEY_TRANSPORTER_FACES, value).commit();
        } else {
            editor.putString(KEY_TRANSPORTER_FACES, faces_list + "," + value).commit();
        }
    }

    public void SET_TRANSPORTED_BY(String value) {
        editor.putString(KEY_TRANSPORTED_BY, value).commit();
    }

    public void SET_UNIQUE_MEMBER_ID(String value) {
        editor.putString(KEY_UNIQUE_MEMBER_ID, value).commit();
    }

    public void SET_SELECTED_TRANSPORTER(String value) {
        editor.putString(KEY_SELECTED_TRANSPORTER, value).commit();
    }

    public void SET_SELECTED_CC_ID(String value) {
        editor.putString(KEY_SELECTED_CC_ID, value).commit();
    }

    public void SET_QTY_TRANSPORTED(Integer value) {
        editor.putInt(KEY_QTY_TRANSPORTED, value).commit();
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

    public Integer GET_REG_FACE_TEMPLATE_FLAG() {
        return prefs.getInt(KEY_REG_FACE_TEMPLATE_FLAG, 99);
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

    public String GET_LAST_SYNC_CARDS() {
        return prefs.getString(KEY_LAST_SYNC_CARDS, "2020-08-26 00:00:00");
    }

    public String[] GET_TRANSPORTER_CARDS() {
        String cards_list = prefs.getString(KEY_TRANSPORTER_CARDS, "");
        if (cards_list.isEmpty()) {
            return new String[1];
        } else {
            return cards_list.split(",");
        }
    }

    public String[] GET_TRANSPORTER_FACES() {
        String faces_list = prefs.getString(KEY_TRANSPORTER_FACES, "");
        if (faces_list.isEmpty()) {
            return new String[1];
        } else {
            return faces_list.split(",");
        }
    }

    public String GET_TRANSPORTED_BY() {
        return prefs.getString(KEY_TRANSPORTED_BY, "");
    }

    public String GET_UNIQUE_MEMBER_ID() {
        return prefs.getString(KEY_UNIQUE_MEMBER_ID, "");
    }

    public String GET_SELECTED_TRANSPORTER() {
        return prefs.getString(KEY_SELECTED_TRANSPORTER, "");
    }

    public String GET_SELECTED_CC_ID() {
        return prefs.getString(KEY_SELECTED_CC_ID, "");
    }

    public Integer GET_QTY_TRANSPORTED() {
        return prefs.getInt(KEY_QTY_TRANSPORTED, 0);
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
                .remove(KEY_REG_FACE_TEMPLATE_FLAG)
                .remove(KEY_REG_VEHICLE_TYPE)
                .remove(KEY_REG_COLLECTION_CENTERS)
                .commit();
    }

    public void REMOVE_TRANSPORTER_FACE_FROM_LIST(String img_name) {
        String faces_list = prefs.getString(KEY_TRANSPORTER_FACES, "");
        if (faces_list.equals("")) {
            return;
        } else {
            faces_list = faces_list.replaceAll(img_name, "");
            faces_list = faces_list.replaceAll(",,", ",");
            editor.putString(KEY_TRANSPORTER_FACES, faces_list).commit();
        }
    }

    public void REMOVE_TRANSPORTER_CARDS_FROM_LIST(String img_name) {
        String cards_list = prefs.getString(KEY_TRANSPORTER_CARDS, "");
        if (cards_list.equals("")) {
            return;
        } else {
            cards_list = cards_list.replaceAll(img_name, "");
            cards_list = cards_list.replaceAll(",,", ",");
            editor.putString(KEY_TRANSPORTER_CARDS, cards_list).commit();
        }
    }

    public void CLEAR_BOOKING_SESSION() {
        editor.remove(KEY_TRANSPORTED_BY)
                .remove(KEY_UNIQUE_MEMBER_ID)
                .commit();
    }
}

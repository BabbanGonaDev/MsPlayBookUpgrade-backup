package com.babbangona.mspalybookupgrade.data.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SharedPrefs {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    //TO-DO: CHANGE THIS TO YOUR SHARED PREFS NEED
    private static final String PREF_NAME = "AndroidHivePref";


    //TODO: CHANGE THESE TO THE SPECIFIED STRINGS YOU NEED
    public static final String KEY_STAFF_NAME  = "staff_name";
    public static final String KEY_STAFF_ID = "staff_id";
    public static final String KEY_STAFF_ROLE = "staff_role";
    public static final String KEY_STAFF_PROGRAM = "staff_program";

    //add additional string below this comment
    public static final String KEY_APP_LANGUAGE = "app_language";
    public static final String KEY_FIRST_TIME_DATA_FLAG = "first_time_data_flag";
    public static final String KEY_PORTFOLIO_LIST = "portfolio_list";
    public static final String KEY_ADDED_PORTFOLIO_LIST = "added_portfolio_list";
    public static final String KEY_ADAPTER_OFFSET = "adapter_offset";
    public static final String KEY_ADAPTER_OFFSET_1 = "adapter_offset_1";
    public static final String KEY_ACTIVITY_TYPE = "activity_type";

    public static final String KEY_ROUTE_TYPE = "route_type";

    public static final String KEY_GRID_MIN_LAT = "min_lat";
    public static final String KEY_GRID_MAX_LAT = "max_lat";
    public static final String KEY_GRID_MIN_LNG = "min_lng";
    public static final String KEY_GRID_MAX_LNG = "max_lng";


    public static final String KEY_SEARCH_UNIQUE_FIELD_ID = "unique_field_id";
    public static final String KEY_SEARCH_IK_NUMBER = "ik_number";
    public static final String KEY_SEARCH_MEMBER_ID = "member_id";
    public static final String KEY_SEARCH_MEMBER_NAME = "member_name";
    public static final String KEY_SEARCH_VILLAGE = "village";
    public static final String KEY_ADAPTER_OFFSET_FIELD_LIST = "adapter_offset_field_list";

    // Constructor
    public SharedPrefs(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setKeyAdapterOffsetFieldList(int offset){
        // Storing role in pref
        editor.putInt(KEY_ADAPTER_OFFSET_FIELD_LIST, offset);


        // commit changes
        editor.commit();
    }

    public void setKeySearchParameters(String unique_field_id, String ik_number, String member_id,
                                       String member_name, String village){
        editor.putString(KEY_SEARCH_UNIQUE_FIELD_ID, unique_field_id);
        editor.putString(KEY_SEARCH_IK_NUMBER, ik_number);
        editor.putString(KEY_SEARCH_MEMBER_ID, member_id);
        editor.putString(KEY_SEARCH_MEMBER_NAME, member_name);
        editor.putString(KEY_SEARCH_VILLAGE, village);
        editor.commit();
    }

    public void setKeyGridParameters(String min_lat, String max_lat, String min_lng, String max_lng){
        editor.putString(KEY_GRID_MIN_LAT, min_lat);
        editor.putString(KEY_GRID_MAX_LAT, max_lat);
        editor.putString(KEY_GRID_MIN_LNG, min_lng);
        editor.putString(KEY_GRID_MAX_LNG, max_lng);
        editor.commit();
    }

    public void setKeyRouteType(String route_type){
        editor.putString(KEY_ROUTE_TYPE, route_type);
        editor.commit();
    }

    public void setKeyActivityType(String activity_type){
        editor.putString(KEY_ACTIVITY_TYPE, activity_type);
        editor.commit();

    }

    public void setKeyAdapterOffset1(int offset){
        // Storing role in pref
        editor.putInt(KEY_ADAPTER_OFFSET_1, offset);


        // commit changes
        editor.commit();
    }

    public void setKeyAdapterOffset(int offset){
        // Storing role in pref
        editor.putInt(KEY_ADAPTER_OFFSET, offset);


        // commit changes
        editor.commit();
    }

    public void setKeyAddedPortfolioList(Set<String> added_portfolio_list){
        // Storing username in pref

        // Storing role in pref
        editor.putStringSet(KEY_ADDED_PORTFOLIO_LIST, added_portfolio_list);


        // commit changes
        editor.commit();
    }

    public void setKeyPortfolioList(Set<String> portfolio_list){
        // Storing username in pref

        // Storing role in pref
        editor.putStringSet(KEY_PORTFOLIO_LIST, portfolio_list);


        // commit changes
        editor.commit();
    }

    public void setKeyFirstTimeDataFlag(String first_time_data_flag){
        editor.putString(KEY_FIRST_TIME_DATA_FLAG, first_time_data_flag);
        editor.commit();
    }

    public void setKeyAppLanguage(String app_language){
        editor.putString(KEY_APP_LANGUAGE, app_language);
        editor.commit();
    }

    /**
     *      Do not edit anything below
     * ____________________________________________________________________________________________
     */

    //THIS IMPLEMETATION SAVES STAFF DETAILS TO SHARED PREFERENCE
    //THIS DATA COMES FROM ACCESS CONTROL AND IT IS SAVED HERE

    public void CreateLoginSession(String staffname,
                                   String staffid,
                                   String staff_role,
                                   String staff_program){

        editor.putString(KEY_STAFF_NAME, staffname);
        editor.putString(KEY_STAFF_ID, staffid);
        editor.putString(KEY_STAFF_ROLE, staff_role);
        editor.putString(KEY_STAFF_PROGRAM, staff_program);

        editor.commit();
    }


    //This method returns all the data partaining to the staff

    public HashMap<String, String> getAllStaffData(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_STAFF_NAME, pref.getString(KEY_STAFF_NAME,     "cannot_find"));
        user.put(KEY_STAFF_ID, pref.getString(KEY_STAFF_ID,         "cannot_find"));
        user.put(KEY_STAFF_ROLE,pref.getString(KEY_STAFF_ROLE,      "cannot_find"));
        user.put(KEY_STAFF_PROGRAM,pref.getString(KEY_STAFF_PROGRAM,"cannot_find"));

        return user;
    }

    public String getStaffID(){      return pref.getString(KEY_STAFF_ID,"cannot_find"); }
    public String getStaffName(){    return pref.getString(KEY_STAFF_NAME,"cannot_find"); }
    public String getStaffRole(){    return pref.getString(KEY_STAFF_ROLE ,"cannot_find"); }
    public String getStaffProgram(){ return pref.getString(KEY_STAFF_PROGRAM ,"cannot_find");}

    /**
     *      Do not edit anything above
     * __________________________________________________________________________________
     *  *
     */

    //TODO: Add additional parameters below this line
    public String getKeyAppLanguage(){
        return pref.getString(KEY_APP_LANGUAGE ,"en");
    }

    public String getKeyFirstTimeDataFlag(){
        return pref.getString(KEY_FIRST_TIME_DATA_FLAG ,"0");
    }

    public Set<String > getKeyPortfolioList(){
        return pref.getStringSet(KEY_PORTFOLIO_LIST, new HashSet<>());
    }

    public Set<String > getKeyAddedPortfolioList(){
        return pref.getStringSet(KEY_ADDED_PORTFOLIO_LIST, new HashSet<>());
    }

    public int getKeyAdapterOffset(){
        return pref.getInt(KEY_ADAPTER_OFFSET ,0);
    }

    public int getKeyAdapterOffset1(){
        return pref.getInt(KEY_ADAPTER_OFFSET_1 ,0);
    }

    public String getKeyActivityType(){
        return pref.getString(KEY_ACTIVITY_TYPE,"8");
    }

    public String getKeyRouteType(){
        return pref.getString(KEY_ROUTE_TYPE,"0");
    }

    public String getKeyGridMinLat(){
        return pref.getString(KEY_GRID_MIN_LAT,"");
    }

    public String getKeyGridMaxLat(){
        return pref.getString(KEY_GRID_MAX_LAT,"");
    }

    public String getKeyGridMinLng(){
        return pref.getString(KEY_GRID_MIN_LNG,"");
    }

    public String getKeyGridMaxLng(){
        return pref.getString(KEY_GRID_MAX_LNG,"");
    }

    public String getKeySearchUniqueFieldId(){
        return pref.getString(KEY_SEARCH_UNIQUE_FIELD_ID,"");
    }

    public String getKeySearchIkNumber(){
        return pref.getString(KEY_SEARCH_IK_NUMBER,"");
    }

    public String getKeySearchMemberId(){
        return pref.getString(KEY_SEARCH_MEMBER_ID,"");
    }

    public String getKeySearchMemberName(){
        return pref.getString(KEY_SEARCH_MEMBER_NAME,"");
    }

    public String getKeySearchVillage(){
        return pref.getString(KEY_SEARCH_VILLAGE,"");
    }

    public int getKeyAdapterOffsetFieldList(){
        return pref.getInt(KEY_ADAPTER_OFFSET_FIELD_LIST ,0);
    }



}

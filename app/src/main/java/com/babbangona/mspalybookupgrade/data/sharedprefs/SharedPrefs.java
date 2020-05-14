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





    // Constructor
    public SharedPrefs(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
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



}

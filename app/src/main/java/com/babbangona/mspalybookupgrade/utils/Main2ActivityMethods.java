package com.babbangona.mspalybookupgrade.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.AutoCompleteTextView;

import com.babbangona.mspalybookupgrade.Homepage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ActivityListRecycler.ActivityListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Main2ActivityMethods {

    private Context context;
    private AppDatabase appDatabase;

    private SharedPrefs sharedPrefs;

    public Main2ActivityMethods(Context context) {
        this.context = context;
        appDatabase = AppDatabase.getInstance(this.context);
        sharedPrefs = new SharedPrefs(this.context);
    }

    public List<ActivityListRecyclerModel> composingRecyclerList(List<ActivityList> initialActivityResultList){
        return recyclerController(appListManipulator(initialActivityResultList));
    }

    private List<ActivityListRecyclerModel> recyclerController(ArrayList<Map<String,String>> wordList){
        List<ActivityListRecyclerModel> activityListRecyclerModels =new ArrayList<>();
        JSONArray jsonArray = new JSONArray(wordList);
        JSONObject jsonObject;
        for(int i = 0; i<jsonArray.length();i++) {
            try {
                jsonObject = jsonArray.getJSONObject(i);
                /*Log.d("DAMILOLA", "loadRecyclerView: "
                        +jsonObject.getString("activity_id")
                        +"-"+jsonObject.getString("activity_name"));*/

                activityListRecyclerModels.add (new ActivityListRecyclerModel(
                        jsonObject.getString("activity_id"),
                        jsonObject.getString("activity_name"),
                        jsonObject.getString("total_field_count"),
                        jsonObject.getString("logged_field_count"),
                        jsonObject.getString("activity_statistics"),
                        jsonObject.getString("activity_destination"),
                        jsonObject.getString("activity_priority")
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return activityListRecyclerModels;
    }

    private ArrayList<Map<String, String>> appListManipulator(List<ActivityList> initialActivityResultList){
        int total_fields = appDatabase.fieldsDao().getTotalFieldsCount("%"+sharedPrefs.getStaffID()+"%");
        Map<String,String > map;
        ArrayList<Map<String, String>> medianAppList = new ArrayList<>();
        try {
            for (ActivityList x: initialActivityResultList){
                map = new HashMap<>();
                map.put("activity_id",x.getActivity_id());
                map.put("activity_name",x.getActivity_name());
                map.put("total_field_count", String.valueOf(total_fields));
                int logged_field_count = getLoggedFieldCount(x.getActivity_id());
                map.put("logged_field_count", String.valueOf(logged_field_count));
                map.put("activity_statistics",getActivityStatistics(total_fields,logged_field_count,x.getActivity_id()));
                map.put("activity_destination",x.getActivity_destination());
                map.put("activity_priority",x.getActivity_priority());
                medianAppList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return medianAppList;
    }

    private double normalisedTotalFieldsCount(int field_count){
        if (field_count <= 0){
            return roundToDouble(1);
        }else{
            return roundToDouble(field_count);
        }
    }

    private double roundToDouble(double result){
        return Math.round(result * 10) / 10.0;
    }

    private double roundToDoubleDecimal(double result){
        return Math.round(result * 100) / 100.0;
    }

    private int getLoggedFieldCount(String activity_id){
        int count_returned;
        if (activity_id.equalsIgnoreCase("1")){
            count_returned = appDatabase.normalActivitiesFlagDao().getFertilizer1Count("%"+sharedPrefs.getStaffID()+"%");
        }else if (activity_id.equalsIgnoreCase("2")){
            count_returned = appDatabase.normalActivitiesFlagDao().getFertilizer2Count("%"+sharedPrefs.getStaffID()+"%");
        }else if (activity_id.equalsIgnoreCase("3")){
            count_returned = appDatabase.hgActivitiesFlagDao().getHGCount("%"+sharedPrefs.getStaffID()+"%");
        }else if (activity_id.equalsIgnoreCase("4")){
            count_returned = 0;
        }else{
            count_returned = 0;
        }
        return count_returned;
    }

    private String getActivityStatistics(int total_field_count, int logged_field_count, String activity_id){
        String composed_sentence;
        double percent = logged_field_count/normalisedTotalFieldsCount(total_field_count);
        percent = roundToDoubleDecimal(percent);
        percent = percent * 100;
        if (activity_id.equalsIgnoreCase("1")){
            composed_sentence = (int)percent+
                    " "+context.getResources().getString(R.string.fertilizer_1_pt_1)+
                    " ("+logged_field_count+
                    " " +context.getResources().getString(R.string.out_of)+
                    " "+total_field_count+
                    " "+context.getResources().getString(R.string.sentence_close);
        }else if (activity_id.equalsIgnoreCase("2")){
            composed_sentence = (int)percent+
                    " "+context.getResources().getString(R.string.fertilizer_2_pt_1)+
                    " ("+logged_field_count+
                    " " +context.getResources().getString(R.string.out_of)+
                    " "+total_field_count+
                    " "+context.getResources().getString(R.string.sentence_close);
        }else if (activity_id.equalsIgnoreCase("3")){
            composed_sentence = (int)percent+
                    " "+context.getResources().getString(R.string.hg_pt_1)+
                    " ("+logged_field_count+
                    " " +context.getResources().getString(R.string.out_of)+
                    " "+total_field_count+
                    " "+context.getResources().getString(R.string.sentence_close_hg);
        }else if (activity_id.equalsIgnoreCase("4")){
            composed_sentence = context.getResources().getString(R.string.set_portfolio);
        }else{
            composed_sentence = context.getResources().getString(R.string.strange_activity);
        }
        return composed_sentence;
    }

    public void confirmPhoneDate(){
        //TODO --- Get the apps list last sync date for now, get a better one later.
        String str_default_date = getLastSyncTimeStaffList();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date def_date = null;
        try {
            def_date = sdf.parse(str_default_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (new Date().before(def_date)) {
            //Current Date is behind default date or last sync date, redirect
            new MaterialAlertDialogBuilder(context)
                    .setIcon(context.getResources().getDrawable(R.drawable.ic_wrong_calendar))
                    .setTitle(context.getResources().getString(R.string.wrong_date_title))
                    .setMessage(context.getResources().getString(R.string.wrong_date_msg))
                    .setCancelable(false)
                    .setPositiveButton(context.getResources().getString(R.string.change_date), (dialogInterface, i) -> {
                        context.startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                    }).show();
        }
    }



    public void confirmLocationOpen(){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            if (lm != null) {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        if(!gps_enabled) {
            // notify user
            new MaterialAlertDialogBuilder(context)
                    .setIcon(context.getResources().getDrawable(R.drawable.ic_location_signs))
                    .setTitle(context.getResources().getString(R.string.location_off_title))
                    .setMessage(context.getResources().getString(R.string.location_off_msg))
                    .setCancelable(false)
                    .setPositiveButton(context.getResources().getString(R.string.turn_location_on), (dialogInterface, i) -> {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }).show();
        }
    }

    private String getLastSyncTimeStaffList(){
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

    public int validateMemberInfo(AutoCompleteTextView state, AutoCompleteTextView lga, AutoCompleteTextView ward, AutoCompleteTextView village){

        // Checks if the state field is empty
        if(Objects.requireNonNull(state.getText()).toString().matches("")) {
            return 0;
        }

        // Checks if the lga field is empty
        else if(Objects.requireNonNull(lga.getText()).toString().matches("")) {
            return 0;
        }

        // Checks if the ward field is empty
        else if(Objects.requireNonNull(ward.getText()).toString().matches("")) {
            return 0;
        }

        // Checks if the age village is empty
        else if(Objects.requireNonNull(village.getText()).toString().matches("")) {
            return 0;
        }

        //all checks are passed
        else{
            return 1;
        }
    }
}

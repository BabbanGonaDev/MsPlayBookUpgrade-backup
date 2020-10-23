package com.babbangona.mspalybookupgrade.LocationTraker.worker;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;


import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.LocationTraker.database.entity.StaffDetails;
import com.babbangona.mspalybookupgrade.LocationTraker.service.GPSTracker;
import com.babbangona.mspalybookupgrade.LocationTraker.util.SharedPreference;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LocationTrackerWorker extends Worker {
    private Context context;
    SharedPrefs sharedPreference;
    AppDatabase appDatabase;
    GPSTracker gps;
    SimpleDateFormat simpleDateFormat;
    public LocationTrackerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
        this.sharedPreference=new SharedPrefs(context);
        this.appDatabase=AppDatabase.getInstance(context);
        this.gps=new GPSTracker(context);
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.d("Worker", "Worker Tracking Location");
            String current_staff = sharedPreference.getStaffID();
            StaffDetails staff = new StaffDetails();
            staff.setBgtId(current_staff);
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                Log.d("Worker", "Latitude,Longitude " + latitude + " " + longitude);

                //Field Location Tracking
                List<StaffDetails.StaffWithLocation> staffWithLocations = appDatabase.staffDao().getAllLocation(current_staff);
                if(staffWithLocations.size()==0){
                    staff.setOutside_field_portfolio_flag(1);
                }else {
                    for (StaffDetails.StaffWithLocation staffWithLocation : staffWithLocations) {
                        String[] middle = staffWithLocation.getLat_long().split("_");
                        double field_lat = Double.parseDouble(middle[0]);
                        double field_long = Double.parseDouble(middle[1]);
                        float[] results = new float[1];
                        android.location.Location.distanceBetween(latitude, longitude, field_lat, field_long, results);
                        Log.i("Worker", "Distance Between Two " + latitude + " " + longitude + " " + field_lat + " " + field_long + "results " + results[0]);
                        float distanceInMeters = results[0];
                        boolean isWithin10km = distanceInMeters < 200;
                        if (isWithin10km) {
                            Log.d("Worker", "With in Field");
                            staff.setOutside_field_portfolio_flag(0);
                            break;
                        } else {
                            staff.setOutside_field_portfolio_flag(1);
                            Log.d("Worker", "Not in Field");
                        }
                    }
                }

                //Village Location Tracking
                List<StaffDetails.StaffWithVillageLocation> staffWithVillageLocations = appDatabase.staffDao().getAllVillageLocation(current_staff);
                if(staffWithVillageLocations.size()==0){
                    staff.setOutside_village_portfolio_flag(1);
                }else {
                    for (StaffDetails.StaffWithVillageLocation staffWithVillageLocation : staffWithVillageLocations) {
                        double village_lat = Double.parseDouble(staffWithVillageLocation.getLatitude());
                        double village_long = Double.parseDouble(staffWithVillageLocation.getLongitude());
                        float[] results = new float[1];
                        android.location.Location.distanceBetween(latitude, longitude, village_lat, village_long, results);
                        Log.i("Worker", "Distance Between Two " + latitude + " " + longitude + " " + village_lat + " " + village_long + "results " + results[0]);
                        float distanceInMeters = results[0];
                        boolean isWithin10km = distanceInMeters < 200;
                        if (isWithin10km) {
                            Log.d("Worker", "With in Village");
                            staff.setOutside_village_portfolio_flag(0);
                            break;
                        } else {
                            staff.setOutside_village_portfolio_flag(1);
                            Log.d("Worker", "Not in Village");
                        }
                    }
                }

                staff.setImei(sharedPreference.getIMEI());
                staff.setAppVersion(BuildConfig.VERSION_NAME);
                staff.setLongitude(String.valueOf(longitude));
                staff.setLatitude(String.valueOf(latitude));
                staff.setDateLogged(simpleDateFormat.format(new Date()));
                staff.setTimestamp(simpleDateFormat.format(new Date()));
                staff.setCoach_id(current_staff);
                staff.setSync_flag(0);
                appDatabase.staffDao().update(staff);
                Data.Builder builder = new Data.Builder();
                Gson gson = new Gson();
                builder.putString("bgId", gson.toJson(staff));
                Data data = builder.build();

                OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(StaffWorker.class).setInputData(data).build();
                WorkManager.getInstance(context).enqueue(request);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return Result.success();
    }
}

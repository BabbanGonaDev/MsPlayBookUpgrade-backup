package com.babbangona.mspalybookupgrade.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GPSController {

    private static String time="";
    private static double longitude;
    private static double latitude;

    public static LocationGetter initialiseLocationListener(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new android.location.LocationListener() {

            public void onLocationChanged(Location location) {

                time = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(location.getTime());
                latitude = location.getLatitude();
                longitude = location.getLongitude();



                if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
                    //Toast.makeText(context, "GPS Longitude" + location.getLongitude() +" Latitude"+location.getLatitude(), Toast.LENGTH_LONG).show();
                    //Log.d("Location", "Time GPS: " + time);
                }// This is what we want!
                else {

                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras){

            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

//        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

        try {
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
            }
            // Note: To Stop listening use: locationManager.removeUpdates(locationListener)
        } catch (Exception e) {
            e.printStackTrace();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            time = dateFormat.format(date);
        }
        if (time.matches(""))
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date();
            time = dateFormat.format(date);
        }

        return new LocationGetter(latitude,longitude,time);
    }

    public static class LocationGetter{
        private double latitude;
        private double longitude;
        private String time;

        LocationGetter(double latitude, double longitude, String time) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.time = time;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public static void checkPermission(Context context){

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA}, 50);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.RECORD_AUDIO}, 15);
        }

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);
        }
        try {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 2);
        }

    }

    public void enableLocations(final Context mCtx){
        LocationManager lm = (LocationManager)mCtx.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            if (lm != null) {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (lm != null) {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }


        if(!gps_enabled && !network_enabled){
            final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
            builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        dialog.dismiss();
                        mCtx.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
            final AlertDialog alert = builder.create();
            alert.show();

        }
    }


}

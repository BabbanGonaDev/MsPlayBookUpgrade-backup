package com.babbangona.mspalybookupgrade.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListRecyclerModel;

import java.util.ArrayList;
import java.util.Collections;


//AHA, MAPPING IS DONE HERE
public class MappingActivity extends AppCompatActivity implements LocationListener {
    TextView txtNumPoints;
    LocationManager locationManager;
    SharedPreferences.Editor memEdit;
    String provider;
    ArrayList<Double> lats;
    ArrayList<Double> longs;
    ArrayList<Double> time;
    ArrayList<Double> latlongs;
    ArrayList<Double> latLongsPlot;
    Double minlat;
    Double maxlat;
    Double minlng;
    Double maxlng;
    Double midlats;
    Double midlng;

    //this is constant
    final long MIN_LOC_UPDATE_TIME = 500;
    String walkOrBike;
    //these will be varied
    static final float MIN_LOC_UPDATE_DISTANCE = 3;
    static final float MAX_WALKING_SPEED = 10;
    static final float MAX_BIKE_SPEED = 20;
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapping);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        txtNumPoints = findViewById(R.id.txtNumPoints);
        lats = new ArrayList<>();
        longs = new ArrayList<>();
        time = new ArrayList<>();
        latlongs = new ArrayList<>();
        latLongsPlot =new ArrayList<>();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        walkOrBike = "W";
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        provider = locationManager.getBestProvider(criteria, false);
        Toast.makeText(this, "Start Mapping.", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionGranted()) {
            try {
                Location location = locationManager.getLastKnownLocation(provider);
                locationManager.requestLocationUpdates(provider, MIN_LOC_UPDATE_TIME, 0, this);

                if (location != null) {
                    onLocationChanged(location);
                }

            } catch (SecurityException e) {

            }
        } else {
            ActivityCompat.requestPermissions(MappingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private double distanceCheck(double lat1, double lon1, double lat2, double lon2){  // generally used geo measurement function
        int R = 6371; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        double x=d * 1000;
        return (int)Math.round(x); // meters

    }

    public String getBoundary(ArrayList<Double> latme,ArrayList<Double>lngme)
    {
        String boundary="";
        minlat=Collections.max(latme);
        maxlat=Collections.min(latme);
        minlng=Collections.min(lngme);
        maxlng=Collections.max(lngme);
        midlats=(minlat+maxlat)/2;
        midlng=(minlng+maxlng)/2;
        boundary +=minlat+"-"+maxlat+"-"+minlng+"-"+maxlng+"-"+midlats+"-"+midlng;
        return boundary;
    }

    public void end(View v) {
        int length = lats.size();

        if (length >= 5 && calculateArea(lats, longs) >= 0.01) {
//        if (length >= 5 ) {
            final String x = getBoundary(lats, longs);
            if (maxlat < 12) {
                if (distanceCheck(lats.get(2), longs.get(2), lats.get(lats.size() - 1), longs.get(longs.size() - 1)) < 50) {

                    final String field_area = String.valueOf(alertArea());

                    new AlertDialog.Builder(this)
                            .setTitle(R.string.endMapSession)
                            .setMessage(this.getResources().getString(R.string.theFieldA) + " " + field_area + " " + this.getResources().getString(R.string.theFieldA2))
                            .setPositiveButton(R.string.saveDet, (DialogInterface.OnClickListener) (dialog, id) -> {

                                String[] a = x.split("-");
                                /*memEdit.putString("fieldsize", String.valueOf(alertArea()));
                                memEdit.putString("latlongs", getLatLongs());
                                memEdit.putString("minlat", a[0]);
                                memEdit.putString("maxlat", a[1]);
                                memEdit.putString("minlng", a[2]);
                                memEdit.putString("maxlng", a[3]);
                                memEdit.putString("middle", a[4] + "_" + a[5]);
                                memEdit.commit();
                                startActivity(new Intent(MappingActivity.this, MappingForm.class));*/
                                finishMapping(
                                        new PWSFieldListRecyclerModel.PWSMapModel(
                                                field_area,
                                        getLatLongs(),
                                        a[0],a[1],a[2],a[3],a[4],a[5])
                                );
                            })
                            .setNegativeButton("Cancel", (dialog, id) -> {
                                dialog.dismiss();
                            })
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.farFromStart), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.farNorth), Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.notEnoughPoints), Toast.LENGTH_LONG).show();
        }
    }


    public String getLatLongs() {
        StringBuilder lat_and_longs = new StringBuilder();
        int size = latlongs.size();
        for (int i = 0; i < size - 1; i++) {
            lat_and_longs.append(latlongs.get(i)).append(",");
        }
        lat_and_longs.append(latlongs.get(size - 1));

        return lat_and_longs.toString();
    }


    public String getLat() {
        StringBuilder lat_and_longs = new StringBuilder();
        int size = latlongs.size();
        for (int i = 0; i < size - 1; i++) {
            lat_and_longs.append(latlongs.get(i)).append(",");
        }
        lat_and_longs.append(latlongs.get(size - 1));

        return lat_and_longs.toString();
    }

    public String getMiddle() {
        return midlng+"_"+midlats;
    }

    public double pointsDist(double lat1, double lon1, double lat2, double lon2) {

        return Math.sqrt((lat2 - lat1) * (lat2 - lat1) + (lon2 - lon1) * (lon2 - lon1)) * 110000;
    }

    public double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public static double calculateArea(final ArrayList<Double> lats, final ArrayList<Double>longs) {
        return calculateAreaOfGPSPolygonOnSphereInSquareMeters(lats,longs,6378137);
    }

    public double alertArea(){
        double area = calculateArea(lats, longs);
        Log.d("arrreaa",String.valueOf(area));
        return roundFieldSizeDouble(area);
    }

    private double roundFieldSizeDouble(double result){
        return Math.round(result * 1000) / 1000.0;
    }

    private static double calculateAreaOfGPSPolygonOnSphereInSquareMeters(final ArrayList<Double>lats ,final ArrayList<Double>longs, final double radius) {
        if (lats.size() < 3) {
            return 0;
        }

        final double diameter = radius * 2;
        final double circumference = diameter * Math.PI;
        final ArrayList<Double> listY = new ArrayList<Double>();
        final ArrayList<Double> listX = new ArrayList<Double>();
        final ArrayList<Double> listArea = new ArrayList<Double>();
        // calculate segment x and y in degrees for each point
        final double latitudeRef = lats.get(3);
        final double longitudeRef = longs.get(3);
        for (int i = 4; i < lats.size(); i++) {
            final double latitude = lats.get(i);
            final double longitude = longs.get(i);
            listY.add(calculateYSegment(latitudeRef, latitude, circumference));
            listX.add(calculateXSegment(longitudeRef, longitude, latitude, circumference));
        }

        // calculate areas for each triangle segment
        for (int i = 2; i < listX.size(); i++) {
            final double x1 = listX.get(i - 1);
            final double y1 = listY.get(i - 1);
            final double x2 = listX.get(i);
            final double y2 = listY.get(i);
            listArea.add(calculateAreaInSquareMeters(x1, x2, y1, y2));
            //Log.d(LOG_TAG, String.format("area %s: %s", listArea.size() - 1, listArea.get(listArea.size() - 1)));
        }

        // sum areas of all triangle segments
        double areasSum = 0;
        for (final Double area : listArea) {
            areasSum = areasSum + area;
        }

        // get abolute value of area, it can't be negative
        return Math.abs(areasSum/10000);// Math.sqrt(areasSum * areasSum);
        //return Math.round(result*10)/10;//rounding to 1 decimal places
    }

    private static Double calculateAreaInSquareMeters(final double x1, final double x2, final double y1, final double y2) {
        double v = (y1 * x2 - x1 * y2) / 2;
        Log.d("memme",String.valueOf(v));
        return v;
    }

    private static double calculateYSegment(final double latitudeRef, final double latitude, final double circumference) {
        Log.d("memme",String.valueOf((latitude - latitudeRef) * circumference / 360.0));
        return (latitude - latitudeRef) * circumference / 360.0;
    }

    private static double calculateXSegment(final double longitudeRef, final double longitude, final double latitude,
                                            final double circumference) {
        return (longitude - longitudeRef) * circumference * Math.cos(Math.toRadians(latitude)) / 360.0;
    }

    private double area(ArrayList<Double> lats, ArrayList<Double> lons) {
        double sum = 0;
        double prevcolat = 0;
        double prevaz = 0;
        double colat0 = 0;
        double az0 = 0;
        for (int i = 0; i < lats.size(); i++) {
            double colat = 2 * Math.atan2(Math.sqrt(Math.pow(Math.sin(lats.get(i) * Math.PI / 180 / 2), 2) + Math.cos(lats.get(i) * Math.PI / 180) * Math.pow(Math.sin(lons.get(i) * Math.PI / 180 / 2), 2)), Math.sqrt(1 - Math.pow(Math.sin(lats.get(i) * Math.PI / 180 / 2), 2) - Math.cos(lats.get(i) * Math.PI / 180) * Math.pow(Math.sin(lons.get(i) * Math.PI / 180 / 2), 2)));
            double az = 0;
            if (lats.get(i) >= 90) {
                az = 0;
            } else if (lats.get(i) <= -90) {
                az = Math.PI;
            } else {
                az = Math.atan2(Math.cos(lats.get(i) * Math.PI / 180) * Math.sin(lons.get(i) * Math.PI / 180), Math.sin(lats.get(i) * Math.PI / 180)) % (2 * Math.PI);
            }
            if (i == 0) {
                colat0 = colat;
                az0 = az;
            }
            if (i > 0 && i < lats.size()) {
                sum = sum + (1 - Math.cos(prevcolat + (colat - prevcolat) / 2)) * Math.PI * ((Math.abs(az - prevaz) / Math.PI) - 2 * Math.ceil(((Math.abs(az - prevaz) / Math.PI) - 1) / 2)) * Math.signum(az - prevaz);
            }
            prevcolat = colat;
            prevaz = az;
        }
        sum = sum + (1 - Math.cos(prevcolat + (colat0 - prevcolat) / 2)) * (az0 - prevaz);
        return 5.10072E14 * Math.min(Math.abs(sum) / 4 / Math.PI, 1 - Math.abs(sum) / 4 / Math.PI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public boolean permissionGranted() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();
//        txtLat.setText(String.valueOf(Math.round(lat * 100.0) / 100.0));
  //      txtLong.setText(String.valueOf(Math.round(lng * 100.0) / 100.0));
         size = lats.size();
            Log.d("got here", "yes");
        //if it is the first point, add it
        if (size == 0) {
            lats.add(lat);
            longs.add(lng);
            time.add((double) (System.currentTimeMillis() / 1000));
            latlongs.add(lat);
            latlongs.add(lng);
            latLongsPlot.add(lat);

            txtNumPoints.setText(String.valueOf(size + 1));

            return;
        }

        double dist = pointsDist(lat, lng, lats.get(size - 1), longs.get(size - 1));

        if (walkOrBike.equals("W") && dist >= MIN_LOC_UPDATE_DISTANCE && dist/((double) (System.currentTimeMillis() / 1000) - time.get(size - 1)) <= MAX_WALKING_SPEED) {
            lats.add(lat);
            longs.add(lng);
            time.add((double) (System.currentTimeMillis() / 1000));
            latlongs.add(lat);
            latlongs.add(lng);
            latLongsPlot.add(lng);
            latLongsPlot.add(lat);
            txtNumPoints.setText(String.valueOf(size + 1));

            Log.i("TEST", String.valueOf(Double.valueOf(System.currentTimeMillis() / 1000f)));

            return;
        }

        if (walkOrBike.equals("B") && dist >= MIN_LOC_UPDATE_DISTANCE && dist/((double) (System.currentTimeMillis() / 1000) - time.get(size - 1)) <= MAX_BIKE_SPEED) {
            lats.add(lat);
            longs.add(lng);
            time.add((double) (System.currentTimeMillis() / 1000));
            latlongs.add(lat);
            latLongsPlot.add(lat);
            latlongs.add(lng);
            latLongsPlot.add(lng);

            txtNumPoints.setText(String.valueOf(size + 1));

            return;
        }

        //lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, latlongs));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onBackPressed() {
        finishMapping(new PWSFieldListRecyclerModel.PWSMapModel());
    }

    void finishMapping(PWSFieldListRecyclerModel.PWSMapModel result){
        Intent intentMessage = new Intent();
        intentMessage.putExtra("RESULT", result);
        setResult(Activity.RESULT_OK,intentMessage);
        finish();
    }
}

package com.babbangona.mspalybookupgrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.network.ActivityListDownloadService;

public class PoorWeatherSupportLauncher extends AppCompatActivity {

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poor_weather_support_launcher);
        appDatabase = AppDatabase.getInstance(PoorWeatherSupportLauncher.this);
        sharedPrefs = new SharedPrefs(PoorWeatherSupportLauncher.this);
        selectRouteActivity();
    }

    void selectRouteActivity(){
        if (!getCategory().contains("1")){
            startPCPoorWeatherSupport();
        }else{
            startGridDetailsClass();
        }
    }

    void startGridDetailsClass(){
        finish();
//        startActivity(new Intent(this, PCStaffPSWPage.class));
        startActivity(new Intent(this, GridDetails.class));
    }

    void startPCPoorWeatherSupport(){
        finish();
        //this goes to select mik.
        startActivity(new Intent(this, PCStaffPSWPage.class));

    }

    String getCategory(){
        String category;
        try {
            category = appDatabase.pwsActivityControllerDao().getRoleCategory(sharedPrefs.getStaffRole());
        } catch (Exception e) {
            e.printStackTrace();
            category = "1";
        }
        if (category == null || category.equalsIgnoreCase("")){
            category = "1";
        }
        return category;
    }
}
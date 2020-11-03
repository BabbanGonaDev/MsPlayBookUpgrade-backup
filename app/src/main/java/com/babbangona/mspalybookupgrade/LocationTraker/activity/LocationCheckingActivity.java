package com.babbangona.mspalybookupgrade.LocationTraker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.babbangona.mspalybookupgrade.LocationTraker.util.LocationUtil;
import com.babbangona.mspalybookupgrade.LocationTraker.worker.LocationTrackerWorker;
import com.babbangona.mspalybookupgrade.R;

public class LocationCheckingActivity extends AppCompatActivity {

    Button btn_desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_checking_acitivity);
        btn_desc = findViewById(R.id.btn_location);
        btn_desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationUtil.displayLocationSettingsRequest(LocationCheckingActivity.this, getApplicationContext());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case 100:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        // All required changes were successfully made
                        //Toast.makeText(MainActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                        try {
                            Thread.sleep(3000);
                            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(LocationTrackerWorker.class).build();
                            WorkManager.getInstance(this).enqueue(request);
                            finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(LocationCheckingActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }
}

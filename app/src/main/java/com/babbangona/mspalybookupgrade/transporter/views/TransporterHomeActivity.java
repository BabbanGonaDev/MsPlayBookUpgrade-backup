package com.babbangona.mspalybookupgrade.transporter.views;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterHomeBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.babbangona.mspalybookupgrade.transporter.services.SyncDownWorker;
import com.babbangona.mspalybookupgrade.transporter.services.SyncUpWorker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TransporterHomeActivity extends AppCompatActivity {
    ActivityTransporterHomeBinding binding;
    TSessionManager session;
    private static String WORK_MANAGER_SINGLE_REQ_UP = "transporter_single_sync_up";
    private static String WORK_MANAGER_SINGLE_REQ_DOWN = "transporter_single_sync_down";
    private static String WORK_MANAGER_PERIODIC_REQ_UP = "transporter_periodic_sync_up";
    private static String WORK_MANAGER_PERIODIC_REQ_DOWN = "transporter_periodic_sync_down";
    OneTimeWorkRequest sync_down_request, sync_up_request;
    Constraints constraints;

    //Shared prefs of the general app to get logged-in staff id.
    SharedPrefs shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_home);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new TSessionManager(this);
        shared = new SharedPrefs(this);

        //Set constraints for work manager periodic syncing.
        constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        session.SET_LOG_IN_STAFF_ID(shared.getStaffID());
        session.SET_LOG_IN_STAFF_NAME(shared.getStaffName());

        sync_down_request = new OneTimeWorkRequest.Builder(SyncDownWorker.class).build();
        sync_up_request = new OneTimeWorkRequest.Builder(SyncUpWorker.class).build();

        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        confirmTransporterPhoneDate();

        binding.btnNextActivity.setOnClickListener(v -> {
            session.CLEAR_REG_SESSION();
            startActivity(new Intent(this, TransporterPhoneNumberActivity.class));
        });

        binding.btnViewRegTransporters.setOnClickListener(v -> {
            startActivity(new Intent(TransporterHomeActivity.this, ViewRegisteredTransporters.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transporter_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            case R.id.item_sync:
                if (AppUtils.isConnectedToNetwork(TransporterHomeActivity.this)) {
                    syncUp();
                    syncDown();
                } else {
                    Snackbar.make(binding.constraintLayout2, "No Network Connection", Snackbar.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        confirmTransporterPhoneDate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        syncUp();
        setUpRecurringSyncUp();
        setUpRecurringSyncDown();
    }

    public void syncDown() {
        WorkManager
                .getInstance(TransporterHomeActivity.this)
                .enqueueUniqueWork(WORK_MANAGER_SINGLE_REQ_DOWN, ExistingWorkPolicy.REPLACE, sync_down_request);
    }

    public void syncUp() {
        WorkManager
                .getInstance(TransporterHomeActivity.this)
                .enqueueUniqueWork(WORK_MANAGER_SINGLE_REQ_UP, ExistingWorkPolicy.REPLACE, sync_up_request);
    }

    public void setUpRecurringSyncUp() {
        PeriodicWorkRequest periodic_up = new PeriodicWorkRequest.Builder(SyncUpWorker.class, 1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager
                .getInstance(TransporterHomeActivity.this)
                .enqueueUniquePeriodicWork(WORK_MANAGER_PERIODIC_REQ_UP, ExistingPeriodicWorkPolicy.REPLACE, periodic_up);
    }

    public void setUpRecurringSyncDown() {
        PeriodicWorkRequest periodic_down = new PeriodicWorkRequest.Builder(SyncDownWorker.class, 1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager
                .getInstance(TransporterHomeActivity.this)
                .enqueueUniquePeriodicWork(WORK_MANAGER_PERIODIC_REQ_DOWN, ExistingPeriodicWorkPolicy.REPLACE, periodic_down);
    }

    public void confirmTransporterPhoneDate() {
        String last_sync_transporter = session.GET_LAST_SYNC_TRANSPORTER();
        String default_date = "2020-08-30 00:00:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date last_sync = null;
        Date def_date = null;
        try {
            last_sync = sdf.parse(last_sync_transporter);
            def_date = sdf.parse(default_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (new Date().before(def_date) || new Date().before(last_sync)) {
            //Current Date is behind default date or last sync date, redirect
            new MaterialAlertDialogBuilder(TransporterHomeActivity.this)
                    .setIcon(getResources().getDrawable(R.drawable.ic_wrong_calendar))
                    .setTitle("Incorrect Date")
                    .setMessage("Kindly correct phone date/time to use the Transporter Module")
                    .setCancelable(false)
                    .setPositiveButton("Okay", (dialogInterface, i) -> {
                        startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                    }).setCancelable(false).show();
        }
    }
}
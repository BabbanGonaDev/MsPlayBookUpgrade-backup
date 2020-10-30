package com.babbangona.mspalybookupgrade.donotpay.views;

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
import com.babbangona.mspalybookupgrade.HarvestSummary.HarvestTrustGroupList;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.databinding.ActivityDonotpayHomeBinding;
import com.babbangona.mspalybookupgrade.donotpay.data.DSessionManager;
import com.babbangona.mspalybookupgrade.donotpay.data.room.DNPDatabase;
import com.babbangona.mspalybookupgrade.donotpay.services.RefreshWorker;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DNPHomeActivity extends AppCompatActivity {
    ActivityDonotpayHomeBinding binding;
    DSessionManager session;
    DNPDatabase db;
    private static String WORK_MANAGER_SINGLE_REFRESH = "dnp_single_refresh";
    private static String WORK_MANAGER_PERIODIC_REFRESH = "dnp_periodic_refresh";
    OneTimeWorkRequest refresh_request;
    Constraints constraints;

    //Shared prefs of the general app to get logged-in staff id.
    SharedPrefs shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_donotpay_home);

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setTitle("Do Not Pay v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DNPDatabase.getInstance(this);
        session = new DSessionManager(this);
        shared = new SharedPrefs(this);

        session.SET_LOG_IN_STAFF_ID(shared.getStaffID());
        session.SET_LOG_IN_STAFF_NAME(shared.getStaffName());

        binding.footer.tvLastSyncTime.setText(session.GET_LAST_SYNC_DNP());
        binding.footer.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());

        confirmDNPPhoneDate();

        constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        refresh_request = new OneTimeWorkRequest.Builder(RefreshWorker.class).build();

        binding.btnDoNotPay.setOnClickListener(v -> {
            startActivity(new Intent(this, DNPTrustGroupActivity.class));
        });

        binding.btnHarvestHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, HarvestTrustGroupList.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.donotpay_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.dnp_refresh) {
            if (AppUtils.isConnectedToNetwork(DNPHomeActivity.this)) {
                refreshData();
            } else {
                Snackbar.make(binding.cl, "No Network Connection", Snackbar.LENGTH_LONG).show();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setupRecurringRefresh();
    }

    public Boolean confirmDNPPhoneDate() {
        String default_date = "2020-10-14 00:00:00";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date last_sync = null;
        Date def_date = null;
        try {
            //last_sync = sdf.parse(last_sync_transporter);
            def_date = sdf.parse(default_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (new Date().before(def_date)) {
            //Current Date is behind default date or last sync date, redirect
            new MaterialAlertDialogBuilder(DNPHomeActivity.this)
                    .setIcon(getResources().getDrawable(R.drawable.ic_wrong_calendar))
                    .setTitle("Incorrect Date")
                    .setMessage("Kindly correct phone date/time to use the Transporter Module")
                    .setCancelable(false)
                    .setPositiveButton("Okay", (dialogInterface, i) -> {
                        startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                    }).setCancelable(false).show();
        } else {
            return true;
        }

        return false;
    }

    public void refreshData() {
        WorkManager
                .getInstance(DNPHomeActivity.this)
                .enqueueUniqueWork(WORK_MANAGER_SINGLE_REFRESH, ExistingWorkPolicy.REPLACE, refresh_request);
    }

    public void setupRecurringRefresh() {
        PeriodicWorkRequest periodic_refresh = new PeriodicWorkRequest.Builder(RefreshWorker.class, 1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build();

        WorkManager
                .getInstance(DNPHomeActivity.this)
                .enqueueUniquePeriodicWork(WORK_MANAGER_PERIODIC_REFRESH, ExistingPeriodicWorkPolicy.REPLACE, periodic_refresh);
    }
}
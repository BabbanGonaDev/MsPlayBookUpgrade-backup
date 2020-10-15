package com.babbangona.mspalybookupgrade.donotpay.views;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.databinding.ActivityDonotpayHomeBinding;
import com.babbangona.mspalybookupgrade.donotpay.data.DSessionManager;
import com.babbangona.mspalybookupgrade.donotpay.data.room.DNPDatabase;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DNPHomeActivity extends AppCompatActivity {
    ActivityDonotpayHomeBinding binding;
    DSessionManager session;
    DNPDatabase db;

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

        binding.footer.tvLastSyncTime.setText("Coming Soon");
        binding.footer.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());

        confirmDNPPhoneDate();

        binding.btnDoNotPay.setOnClickListener(v -> {
            startActivity(new Intent(this, DNPTrustGroupActivity.class));
        });

        binding.btnHarvestHistory.setOnClickListener(v -> {
            //Do Something
        });
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
}
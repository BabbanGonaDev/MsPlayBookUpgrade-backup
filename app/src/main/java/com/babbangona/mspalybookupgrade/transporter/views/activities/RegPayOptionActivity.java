package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterPayOptionBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.babbangona.mspalybookupgrade.transporter.views.TransporterHomeActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RegPayOptionActivity extends AppCompatActivity {
    ActivityTransporterPayOptionBinding binding;
    TransporterDatabase db;
    TSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_pay_option);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);

        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        binding.btnBank.setOnClickListener(v -> {
            //Go to bank activity
            startActivity(new Intent(this, RegBankOptionActivity.class));
        });

        binding.btnCard.setOnClickListener(v -> {
            //Go to card activity
            startActivity(new Intent(this, RegCardOptionActivity.class));
        });

        binding.btnCash.setOnClickListener(v -> {
            //Confirm from user if they truly want to do this.
            AlertDialog incentives = new MaterialAlertDialogBuilder(this)
                    .setTitle("Are you sure ?")
                    .setMessage("There are incentives for online payment options...")
                    .setIcon(R.drawable.ic_sad_face)
                    .setPositiveButton("Continue", (dialog, which) -> {
                        //Save details.
                        displayConfirmationDialog();
                    })
                    .setNegativeButton("Go Back", (dialog, which) -> {
                        dialog.dismiss();
                    }).setCancelable(false).show();
            incentives.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            incentives.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dummy_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayConfirmationDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirm details")
                .setMessage("Are you sure you want to register this transporter ?")
                .setPositiveButton("Yes", (dialogW, whichW) -> {
                    saveRegDetailsCashOption();
                })
                .setNegativeButton("Cancel", (dialog1, which1) -> {
                    dialog1.dismiss();
                }).setCancelable(false).show();
    }

    public void saveRegDetailsCashOption() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            db.getTransporterDao().insertSingleTransporter(getTransporterInfo());
            db.getOpAreaDao().insertOpAreasList(getOperatingAreasInfo());

            runOnUiThread(() -> {
                AlertDialog congrats = new MaterialAlertDialogBuilder(RegPayOptionActivity.this)
                        .setIcon(R.drawable.ic_smiley_face)
                        .setTitle("Congratulations")
                        .setMessage("Transporter successfully registered")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            //Return Home.
                            session.CLEAR_REG_SESSION();
                            startActivity(new Intent(this, TransporterHomeActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }).setCancelable(false).show();
                congrats.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            });
        });
    }

    public TransporterTable getTransporterInfo() {

        return new TransporterTable(session.GET_REG_PHONE_NUMBER(),
                session.GET_REG_FIRST_NAME(),
                session.GET_REG_LAST_NAME(),
                session.GET_REG_VEHICLE_TYPE(),
                "Cash",
                "N/A",
                0,
                "N/A",
                "N/A",
                0,
                "N/A",
                session.GET_REG_FACE_TEMPLATE(),
                session.GET_REG_FACE_TEMPLATE_FLAG(),
                session.GET_LOG_IN_STAFF_ID(),
                getDeviceID(),
                BuildConfig.VERSION_NAME,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                0);
    }

    public List<OperatingAreasTable> getOperatingAreasInfo() {
        List<String> ops_areas = new Gson().fromJson(session.GET_REG_COLLECTION_CENTERS(), AppUtils.stringType);
        List<OperatingAreasTable> areas_list = new ArrayList<>();

        for (String x : ops_areas) {
            areas_list.add(new OperatingAreasTable(session.GET_REG_PHONE_NUMBER(),
                    x,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                    0));
        }

        return areas_list;
    }

    private String getDeviceID() {
        String device_id;
        TelephonyManager tm = (TelephonyManager) Objects.requireNonNull(this).getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            try {
                device_id = tm.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
                device_id = "";
            }
            if (device_id == null) {
                device_id = "";
            }
        } else {
            device_id = "";
        }
        return device_id;
    }
}
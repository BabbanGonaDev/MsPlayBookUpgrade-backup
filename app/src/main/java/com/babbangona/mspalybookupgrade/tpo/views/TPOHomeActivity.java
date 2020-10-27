package com.babbangona.mspalybookupgrade.tpo.views;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTpoHomeBinding;
import com.babbangona.mspalybookupgrade.tpo.data.TPOSessionManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TPOHomeActivity extends AppCompatActivity {
    private static final int QR_SCAN_CODE = 560;
    /**
     * Starting of this activity, will first trigger a QRCode scan for the warehouse.
     * Then the mark attendance button will now show.
     */

    ActivityTpoHomeBinding binding;
    TPOSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tpo_home);

        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new TPOSessionManager(this);

        confirmTPOPhoneDate();

        binding.btnScanQr.setOnClickListener(v -> {
            Intent intent = new Intent(this, TPOScannerActivity.class);
            startActivityForResult(intent, QR_SCAN_CODE);
        });

        /*binding.btnMarkAttendance.setOnClickListener(v -> {
            //Proceed to activity select member.
            startActivity(new Intent(TPOHomeActivity.this, MarkAttendanceActivity.class));
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_SCAN_CODE) {

            if (resultCode == RESULT_OK) {
                //Save to shared prefs and then proceed.
                if (data.getStringExtra("STATUS").equals("SUCCESS")) {

                    String cc_id = data.getStringExtra("CC_ID");
                    session.SET_COLLECTION_CENTER_ID(cc_id);
                    Toast.makeText(this, "CC ID: " + cc_id + " captured.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(TPOHomeActivity.this, MarkAttendanceActivity.class));

                } else {

                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Alert !!!")
                            .setMessage("Unable to get Collection Center ID")
                            .setPositiveButton("Okay", (dialog, which) -> {
                                dialog.dismiss();
                            }).setCancelable(false).show();
                }

            }
        }

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

    public Boolean confirmTPOPhoneDate() {
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
            new MaterialAlertDialogBuilder(TPOHomeActivity.this)
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
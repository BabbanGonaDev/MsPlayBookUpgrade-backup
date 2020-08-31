package com.babbangona.mspalybookupgrade.transporter.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterPhoneNumberBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Arrays;

public class TransporterPhoneNumberActivity extends AppCompatActivity {
    ActivityTransporterPhoneNumberBinding binding;
    TransporterDatabase db;
    TSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_phone_number);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);

        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        setPhoneNoFilter();


        binding.btnSubmit.setOnClickListener(v -> {
            String phone_no = binding.editPhoneNumber.getText().toString().trim();
            if (!isPhoneNumberValid(phone_no)) {
                //Phone number not valid
                AlertDialog invalid_no_check = new MaterialAlertDialogBuilder(this)
                        .setTitle("Invalid Entry")
                        .setIcon(R.drawable.ic_sad_face)
                        .setMessage("Kindly enter a valid phone number")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        }).setCancelable(false).show();
                invalid_no_check.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);

            } else if (!isPhoneNumberVerified(phone_no)) {
                //Phone number already exists.
                AlertDialog duplicate_no = new MaterialAlertDialogBuilder(this)
                        .setTitle("Duplicate Entry")
                        .setIcon(R.drawable.ic_sad_face)
                        .setMessage("This transporter phone number already exists")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        }).setCancelable(false).show();
                duplicate_no.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);

            } else {
                //--- GO to next page.
                session.SET_REG_PHONE_NUMBER(phone_no);
                startActivity(new Intent(this, TransporterNamesActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    public void setPhoneNoFilter() {
        InputFilter filter = (source, start, end, dest, dStart, dEnd) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i)) || dest.length() >= 11) {
                    return "";
                }
            }
            return null;
        };
        binding.editPhoneNumber.setFilters(new InputFilter[]{filter});
    }

    public boolean isPhoneNumberValid(String number) {
        String first_four;
        if (number.length() > 4) {
            first_four = number.substring(0, 4);
            return (number.length() == 11) && Arrays.asList(AppUtils.phone_prefix).contains(first_four);
        } else {
            return false;
        }
    }

    public boolean isPhoneNumberVerified(String number) {
        TransporterTable trans = db.getTransporterDao().getTransporterDetails(number);
        if (trans != null) {
            return false;
        }
        return true;
    }
}
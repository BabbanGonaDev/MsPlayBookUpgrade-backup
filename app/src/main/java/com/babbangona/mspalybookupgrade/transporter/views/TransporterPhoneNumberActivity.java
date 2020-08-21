package com.babbangona.mspalybookupgrade.transporter.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterPhoneNumberBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

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

        setPhoneNoFilter();


        binding.btnSubmit.setOnClickListener(v -> {
            String phone_no = binding.editPhoneNumber.getText().toString().trim();
            if (!isPhoneNumberValid(phone_no)) {
                //Phone number not valid
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Invalid Entry")
                        .setIcon(R.drawable.ic_sad_face)
                        .setMessage("Kindly enter a valid phone number")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        }).setCancelable(false).show();

            } else if (!isPhoneNumberVerified(phone_no)) {
                //Phone number already exists.
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Duplicate Entry")
                        .setIcon(R.drawable.ic_sad_face)
                        .setMessage("This transporter phone number already exists")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        }).setCancelable(false).show();

            } else {
                //--- GO to next page.
                session.SET_REG_PHONE_NUMBER(phone_no);
                startActivity(new Intent(this, TransporterPhoneNumberActivity.class));
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
        return (number.length() == 11) && number.charAt(0) == '0';
    }

    public boolean isPhoneNumberVerified(String number) {
        TransporterTable trans = db.getTransporterDao().getTransporterDetails(number);
        //if trans is null, return true. Means, number doesn't exist.
        return trans == null;
    }
}
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
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterNamesBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class TransporterNamesActivity extends AppCompatActivity {
    ActivityTransporterNamesBinding binding;
    TransporterDatabase db;
    TSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_names);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);

        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        setNamesFilter();

        binding.btnSubmit.setOnClickListener(v -> {
            String f_name = binding.editFirstName.getText().toString().trim();
            String l_name = binding.editLastName.getText().toString().trim();

            if (isNamesEmpty(f_name) || isNamesEmpty(l_name)) {
                AlertDialog empty_entry_check = new MaterialAlertDialogBuilder(TransporterNamesActivity.this)
                        .setIcon(R.drawable.ic_sad_face)
                        .setTitle("Invalid Entry")
                        .setMessage("Kindly fill in the required details")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        }).setCancelable(false).show();
                empty_entry_check.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
            } else {
                session.SET_REG_FIRST_NAME(f_name);
                session.SET_REG_LAST_NAME(l_name);

                AlertDialog facial_check = new MaterialAlertDialogBuilder(TransporterNamesActivity.this)
                        .setIcon(R.drawable.ic_smiley_face)
                        .setTitle("Facial Capture ?")
                        .setMessage("Do you want to perform facial capture ?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            //Navigate to facial capture page
                            startActivity(new Intent(this, TransporterTemplateCaptureActivity.class));
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            //Move to next activity
                            session.SET_REG_FACE_TEMPLATE("N/A");
                            startActivity(new Intent(this, TransporterVehicleActivity.class));
                        }).setCancelable(true).show();
                facial_check.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                facial_check.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }


    public boolean isNamesEmpty(String name) {
        return name.isEmpty();
    }

    //TODO: Note this filter isn't allowing space-bar. Is that a problem ???
    public void setNamesFilter() {
        InputFilter filter = (source, start, end, dest, dStart, dEnd) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isLetter(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        binding.editFirstName.setFilters(new InputFilter[]{filter});
        binding.editLastName.setFilters(new InputFilter[]{filter});
    }


}
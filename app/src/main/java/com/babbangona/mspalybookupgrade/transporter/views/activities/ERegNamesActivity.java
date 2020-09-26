package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterNamesBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomOneButtonBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;

public class ERegNamesActivity extends AppCompatActivity {
    /**
     * Note: This activity makes use of the same layout as RegNamesActivity
     */

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
                promptEmptyNameFields();

            } else {
                session.SET_E_REG_FIRST_NAME(f_name);
                session.SET_E_REG_LAST_NAME(l_name);

                //Next activity
                startActivity(new Intent(this, ERegDestinationActivity.class));
            }
        });

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

    public boolean isNamesEmpty(String name) {
        return name.isEmpty();
    }

    public void setNamesFilter() {
        InputFilter filter = (source, start, end, dest, dStart, dEnd) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isWhitespace(source.charAt(i)) && !Character.isLetter(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        binding.editFirstName.setFilters(new InputFilter[]{filter});
        binding.editLastName.setFilters(new InputFilter[]{filter});
    }

    public void promptEmptyNameFields() {
        DialogCustomOneButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_sad_face);
        binding.btnPrimary.setText("Okay");
        binding.mtvDialogText.setText("Kindly fill in the required details");
        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());

        binding.btnPrimary.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }
}
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
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterPhoneNumberBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomOneButtonBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;

import java.util.Arrays;

public class ERegPhoneNumberActivity extends AppCompatActivity {
    /**
     * Note, this activity makes use of the same layout as RegPhoneNumberActivity.
     */

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
                promptPhoneNumberNotValid();

            } else if (!isPhoneNumberVerified(phone_no)) {
                //Phone number already exists.
                promptPhoneNumberAlreadyExists();

            } else {
                //--- GO to next page.
                session.SET_E_REG_PHONE_NUMBER(phone_no);
                startActivity(new Intent(this, ERegNamesActivity.class));
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
        TransporterTable trans = db.getTransporterDao().getTransporterDetailsByPhoneNo(number);
        if (trans != null) {
            return false;
        }
        return true;
    }

    public void promptPhoneNumberNotValid() {
        DialogCustomOneButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_sad_face);
        binding.btnPrimary.setText("Okay");
        binding.mtvDialogText.setText("Kindly enter a valid phone number");
        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());

        binding.btnPrimary.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }

    public void promptPhoneNumberAlreadyExists() {
        DialogCustomOneButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_sad_face);
        binding.btnPrimary.setText("Okay");
        binding.mtvDialogText.setText("This transporter phone number already exists");
        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());

        binding.btnPrimary.setOnClickListener(v -> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }
}
package com.babbangona.mspalybookupgrade.tpo.views;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTpoSaveDetailsBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomOneButtonBinding;
import com.babbangona.mspalybookupgrade.tpo.data.TPOSessionManager;
import com.babbangona.mspalybookupgrade.tpo.data.models.MemberModel;
import com.babbangona.mspalybookupgrade.tpo.data.room.TPODatabase;
import com.babbangona.mspalybookupgrade.tpo.data.room.tables.DeliveryAttendance;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class SaveTPODetailsActivity extends AppCompatActivity {
    ActivityTpoSaveDetailsBinding binding;
    TPOSessionManager session;
    MemberModel selectedMember;
    TPODatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tpo_save_details);
        db = TPODatabase.getInstance(this);
        session = new TPOSessionManager(this);

        getSupportActionBar().setTitle("Do Not Pay v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.footer.tvLastSyncTime.setText(session.GET_LAST_SYNC_TIME());
        binding.footer.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());

        try {
            selectedMember = new Gson().fromJson(session.GET_SELECTED_MEMBER(), MemberModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.btnSubmitAttendance.setOnClickListener(v -> saveDetails());
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

    public void saveDetails() {
        if (binding.editEnterAmount.getText().toString().trim().equals(binding.editConfirmAmount.getText().toString().trim())
                && !binding.editEnterAmount.getText().toString().isEmpty()
                && !binding.editEnterAmount.getText().toString().trim().equals("0")) {

            //Save details
            db.getDeliveryAttendanceDao().insert(new DeliveryAttendance(selectedMember.getUnique_member_id(),
                    selectedMember.getIk_number(),
                    session.GET_COLLECTION_CENTER_ID(),
                    binding.editEnterAmount.getText().toString().trim(),
                    getDeviceID(),
                    BuildConfig.VERSION_NAME,
                    new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(new Date()),
                    0));

            confirmDeliveryMarked();
        } else {

            Toast.makeText(this, "Kindly check amount and try again", Toast.LENGTH_LONG).show();
        }
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

    private void confirmDeliveryMarked() {
        DialogCustomOneButtonBinding dBinding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(dBinding.getRoot());
        dBinding.btnPrimary.setText("Okay");
        dBinding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        dBinding.mtvDialogText.setText("This delivery has been successfully marked");
        dBinding.imgBtnCancel.setVisibility(View.GONE);

        dBinding.btnPrimary.setOnClickListener(v -> {
            startActivity(new Intent(this, MarkAttendanceActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            dialog.dismiss();
        });
        dialog.setCancelable(false);
        dialog.show();

    }
}
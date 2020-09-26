package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterPayOptionBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomOneButtonBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomTwoButtonsBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.babbangona.mspalybookupgrade.transporter.views.TransporterHomeActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ERegPayOptionActivity extends AppCompatActivity {
    /**
     * NOTE: This activity is using the same layout as RegPayOptionActivity
     */
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
            startActivity(new Intent(this, ERegBankOptionActivity.class));
        });

        binding.btnCard.setOnClickListener(v -> {
            //Go to card activity
            startActivity(new Intent(this, ERegCardOptionActivity.class));
        });

        binding.btnCash.setOnClickListener(v -> {
            //Confirm from user if they truly want to do this.
            promptIncentivesForOnlinePayment();
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

    public void promptConfirmationDialog() {
        DialogCustomTwoButtonsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_two_buttons, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.btnRight.setText("Yes");
        binding.btnLeft.setText("Cancel");
        binding.mtvDialogText.setText("Are you sure you want to register this transporter and save booking details ?");
        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());

        binding.btnRight.setOnClickListener(v -> {
            dialog.dismiss();
            saveRegAndBookingDetailsCashOption();
        });

        binding.btnLeft.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    public void promptIncentivesForOnlinePayment() {
        DialogCustomTwoButtonsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_two_buttons, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.btnRight.setText("Continue");
        binding.btnLeft.setText("Go Back");
        binding.mtvDialogText.setText("Are you sure ? \n There are incentives for online payment options...");
        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());

        binding.btnRight.setOnClickListener(v -> {
            dialog.dismiss();
            promptConfirmationDialog();
        });

        binding.btnLeft.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    public void promptSaveSuccessful() {
        DialogCustomOneButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.btnPrimary.setText("Okay");
        binding.mtvDialogText.setText("Recruitment & Booking successfully saved.");

        binding.btnPrimary.setOnClickListener(v -> {
            dialog.dismiss();
            session.CLEAR_REG_SESSION();
            startActivity(new Intent(this, TransporterHomeActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });

        binding.imgBtnCancel.setVisibility(View.GONE);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void saveRegAndBookingDetailsCashOption() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            db.getTransporterDao().insertSingleTransporter(getTransporterInfo());
            db.getCoachLogsDao().insertSingleCoachLog(getBookingInfo());

            runOnUiThread(this::promptSaveSuccessful);
        });
    }

    public TransporterTable getTransporterInfo() {

        return new TransporterTable(session.GET_E_REG_PHONE_NUMBER(),
                session.GET_E_REG_FIRST_NAME(),
                session.GET_E_REG_LAST_NAME(),
                "N/A",
                "Cash",
                "N/A",
                0,
                "N/A",
                "N/A",
                0,
                "N/A",
                "N/A",
                0,
                session.GET_LOG_IN_STAFF_ID(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                0);
    }

    public CoachLogsTable getBookingInfo() {

        return new CoachLogsTable(session.GET_UNIQUE_MEMBER_ID(),
                session.GET_QTY_TRANSPORTED(),
                session.GET_TRANSPORTED_BY(),
                session.GET_E_REG_PHONE_NUMBER(),
                session.GET_VOUCHER_ID(),
                session.GET_VOUCHER_ID_FLAG(),
                session.GET_SELECTED_CC_ID(),
                session.GET_INSTANT_PAYMENT_FLAG(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                0);
    }
}
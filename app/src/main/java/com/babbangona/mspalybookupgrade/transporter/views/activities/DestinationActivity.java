package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterDestinationBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomOneButtonBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomTwoButtonsBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogTransporterNoOfBagsBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CollectionCenterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DestinationActivity extends AppCompatActivity {
    ActivityTransporterDestinationBinding binding;
    TSessionManager session;
    TransporterDatabase db;
    private static Integer VOUCHER_SCAN_REQUEST_CODE = 110;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_destination);

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new TSessionManager(this);
        db = TransporterDatabase.getInstance(this);

        binding.footer.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.footer.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        initDestinationAdapter();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == VOUCHER_SCAN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    switch (data.getStringExtra("STATUS")) {
                        case "SUCCESS":
                            String voucher_id = data.getStringExtra("VOUCHER_ID");
                            saveDetailsWithVoucher(voucher_id);
                            break;
                        case "FAILED":
                            Toast.makeText(this, "Kindly scan a valid Transporter Voucher", Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void initDestinationAdapter() {
        db.getCcDao().getAllCollectionCenters().observe(this, cc_list -> {
            ArrayAdapter<CollectionCenterTable> destinationAdapter =
                    new ArrayAdapter<>(DestinationActivity.this,
                            R.layout.dropdown_menu_transporter_popup_item,
                            cc_list);

            binding.autocompleteDestination.setAdapter(destinationAdapter);
            binding.autocompleteDestination.setOnItemClickListener((parent, view, position, id) -> {
                CollectionCenterTable select = (CollectionCenterTable) parent.getItemAtPosition(position);

                Log.d("=====================> ", select.getCc_id() + " " + select.getCc_name());
                Toast.makeText(this, "Selected: " + select.getCc_name(), Toast.LENGTH_LONG).show();

                session.SET_SELECTED_CC_ID(select.getCc_id());
                requestNoOfBagsTransported();
                //Request amount transported.
            });
        });
    }

    public void requestNoOfBagsTransported() {
        DialogTransporterNoOfBagsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_transporter_no_of_bags, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);

        binding.btnContinue.setOnClickListener(v -> {
            String numberOfBags = binding.editEnterNoBags.getText().toString().trim();
            String confirmNumberOfBags = binding.editConfirmNoBags.getText().toString().trim();

            if (numberOfBags.equals(confirmNumberOfBags) && !numberOfBags.isEmpty()) {
                session.SET_QTY_TRANSPORTED(Integer.parseInt(numberOfBags));
                dialog.dismiss();
                requestScanTransporterVoucher();
            } else {
                Toast.makeText(this, "Kindly fill in the inputs correctly", Toast.LENGTH_LONG).show();
            }
        });

        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());
        dialog.setCancelable(false);
        dialog.show();
    }

    public void requestScanTransporterVoucher() {
        DialogCustomTwoButtonsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_two_buttons, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.btnRight.setText("Yes");
        binding.btnLeft.setText("No");
        binding.mtvDialogText.setText("Do you want to scan the Transporter's voucher ?");

        binding.btnRight.setOnClickListener(v -> {
            //Navigate to scan of voucher.
            dialog.dismiss();
            startActivityForResult(new Intent(DestinationActivity.this, ScannerActivity.class), VOUCHER_SCAN_REQUEST_CODE);
        });
        binding.btnLeft.setOnClickListener(v -> {
            //Prompt about no instant payment.
            dialog.dismiss();
            promptNoInstantPayment();
        });

        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());
        dialog.setCancelable(false);
        dialog.show();
    }

    public void promptNoInstantPayment() {
        DialogCustomOneButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_sad_face);
        binding.btnPrimary.setText("Continue");
        binding.mtvDialogText.setText("You will not be eligible for instant payments until Coach confirms your details");

        binding.btnPrimary.setOnClickListener(v -> {
            //Save details without voucher.
            dialog.dismiss();
            saveDetailsWithoutVoucher();
        });

        binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());
        dialog.setCancelable(false);
        dialog.show();
    }

    public void saveDetailsWithoutVoucher() {
        CoachLogsTable coach = new CoachLogsTable(session.GET_UNIQUE_MEMBER_ID(),
                session.GET_QTY_TRANSPORTED(),
                session.GET_TRANSPORTED_BY(),
                session.GET_SELECTED_TRANSPORTER(),
                "N/A",
                0,
                session.GET_SELECTED_CC_ID(),
                0,
                session.GET_LOG_IN_STAFF_ID(),
                AppUtils.getDeviceID(DestinationActivity.this),
                BuildConfig.VERSION_NAME,
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date()),
                0);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.getCoachLogsDao().insertSingleCoachLog(coach);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                promptMarkedAsTransported();
            }
        }.execute();
    }

    public void saveDetailsWithVoucher(String voucherId) {
        CoachLogsTable coach = new CoachLogsTable(session.GET_UNIQUE_MEMBER_ID(),
                session.GET_QTY_TRANSPORTED(),
                session.GET_TRANSPORTED_BY(),
                session.GET_SELECTED_TRANSPORTER(),
                voucherId,
                1,
                session.GET_SELECTED_CC_ID(),
                1,
                session.GET_LOG_IN_STAFF_ID(),
                AppUtils.getDeviceID(DestinationActivity.this),
                BuildConfig.VERSION_NAME,
                new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date()),
                0);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.getCoachLogsDao().insertSingleCoachLog(coach);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                promptMarkedAsTransported();
            }
        }.execute();
    }

    public void promptMarkedAsTransported() {
        DialogCustomOneButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.btnPrimary.setText("Okay");
        binding.mtvDialogText.setText("Member has been successfully marked as transported by Transporter");
        binding.imgBtnCancel.setVisibility(View.GONE);

        binding.btnPrimary.setOnClickListener(v -> {
            //Go Home
            dialog.dismiss();
            session.CLEAR_BOOKING_SESSION();
            startActivity(new Intent(DestinationActivity.this, BookingActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
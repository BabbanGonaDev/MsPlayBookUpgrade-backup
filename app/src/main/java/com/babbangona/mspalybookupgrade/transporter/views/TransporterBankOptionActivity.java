package com.babbangona.mspalybookupgrade.transporter.views;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterBankOptionBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TransporterBankOptionActivity extends AppCompatActivity {
    ActivityTransporterBankOptionBinding binding;
    TransporterDatabase db;
    TSessionManager session;
    int name_mismatch_flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_bank_option);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);

        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        setAccNumberFilter();

        //setAccNameTxtWatcher();

        initBankNameAdapter();

        binding.btnSubmit.setOnClickListener(v -> {
            if (isInputsEmpty()) {

                AlertDialog empty_inputs_check = new MaterialAlertDialogBuilder(this)
                        .setTitle("Invalid Entry")
                        .setMessage("Kindly fill in all inputs")
                        .setIcon(R.drawable.ic_sad_face)
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        }).setCancelable(false).show();
                empty_inputs_check.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);

            } else if (!isAccNumberValid()) {

                AlertDialog valid_no_check = new MaterialAlertDialogBuilder(this)
                        .setTitle("Invalid Entry")
                        .setMessage("Kindly check the account number again")
                        .setIcon(R.drawable.ic_sad_face)
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                            //binding.editAccountNumber.requestFocus();
                        }).setCancelable(false).show();
                valid_no_check.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);

            } else if (isBankDetailsDuplicate()) {

                AlertDialog duplicate_acc_check = new MaterialAlertDialogBuilder(TransporterBankOptionActivity.this)
                        .setTitle("Account Error")
                        .setMessage("This bank details already exist")
                        .setIcon(R.drawable.ic_sad_face)
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                            binding.editAccountNumber.requestFocus();
                        }).setCancelable(false).show();
                duplicate_acc_check.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);

            } else if (!isAccNameMatching()) {

                AlertDialog bank_mismatch_check = new MaterialAlertDialogBuilder(TransporterBankOptionActivity.this)
                        .setTitle("Are you sure ?")
                        .setMessage("Account name doesn't match Transporter's name")
                        .setIcon(R.drawable.ic_sad_face)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            //Set the name mismatch flag.
                            name_mismatch_flag = 1;
                            displayConfirmationDialog();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                            name_mismatch_flag = 0;
                            binding.editAccountName.requestFocus();
                        }).setCancelable(false).show();
                bank_mismatch_check.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
                bank_mismatch_check.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);

            } else {
                //Save details.
                displayConfirmationDialog();
            }
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

    public void initBankNameAdapter() {
        ArrayAdapter<String> bank_adapter =
                new ArrayAdapter<>(getApplicationContext(),
                        R.layout.dropdown_menu_transporter_popup_item,
                        AppUtils.bank_names);
        binding.atvBank.setAdapter(bank_adapter);
    }

    public boolean isInputsEmpty() {
        if (binding.editAccountName.getText().toString().trim().isEmpty()) {
            return true;
        } else if (binding.editAccountNumber.getText().toString().trim().isEmpty()) {
            return true;
        } else if (binding.editConfirmAccountNumber.getText().toString().trim().isEmpty()) {
            return true;
        } else {
            return binding.atvBank.getText().toString().trim().isEmpty();
        }
    }

    public boolean isAccNumberValid() {
        if (binding.editAccountNumber.getText().toString().trim().equals(binding.editConfirmAccountNumber.getText().toString().trim())) {
            return binding.editAccountNumber.getText().toString().length() == 10;
        } else {
            return false;
        }
    }

    public boolean isAccNameMatching() {
        String entered_name = binding.editAccountName.getText().toString().trim().toLowerCase();
        return entered_name.contains(session.GET_REG_FIRST_NAME().toLowerCase())
                && entered_name.contains(session.GET_REG_LAST_NAME().toLowerCase());
    }

    public boolean isBankDetailsDuplicate() {
        String account_no = binding.editAccountNumber.getText().toString().trim();
        String bank_name = binding.atvBank.getText().toString().trim();

        TransporterTable trans = db.getTransporterDao().getTransporterDetailsByBank(account_no, bank_name);
        if (trans == null) {
            return false;
        }
        return true;
    }

    public void displayConfirmationDialog() {
        AlertDialog confirmation_check = new MaterialAlertDialogBuilder(this)
                .setTitle("Confirm details")
                .setMessage("Are you sure you want to register this transporter ?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    saveRegDetailsBankOption();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    name_mismatch_flag = 0;
                    dialog.dismiss();
                }).setCancelable(false).show();
        confirmation_check.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        confirmation_check.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
    }

    public void saveRegDetailsBankOption() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            db.getTransporterDao().insertSingleTransporter(getTransporterInfo());
            db.getOpAreaDao().insertOpAreasList(getOperatingAreasInfo());

            runOnUiThread(() -> {

                AlertDialog congrats = new MaterialAlertDialogBuilder(TransporterBankOptionActivity.this)
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
                "Bank Account",
                "N/A",
                0,
                binding.editAccountNumber.getText().toString(),
                binding.editAccountName.getText().toString(),
                name_mismatch_flag,
                binding.atvBank.getText().toString(),
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
                    session.GET_LOG_IN_STAFF_ID(),
                    getDeviceID(),
                    BuildConfig.VERSION_NAME,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                    0));
        }

        return areas_list;
    }

    public void setAccNumberFilter() {
        InputFilter filter_1 = (source, start, end, dest, dStart, dEnd) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isDigit(source.charAt(i)) || dest.length() >= 10) {
                    return "";
                }
            }
            return null;
        };
        binding.editAccountNumber.setFilters(new InputFilter[]{filter_1});
        binding.editConfirmAccountNumber.setFilters(new InputFilter[]{filter_1});
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
package com.babbangona.mspalybookupgrade.transporter.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
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

public class TransporterBankOptionActivity extends AppCompatActivity {
    ActivityTransporterBankOptionBinding binding;
    TransporterDatabase db;
    TSessionManager session;

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

        setAccNumberFilter();

        //setAccNameTxtWatcher();

        initBankNameAdapter();

        binding.btnSubmit.setOnClickListener(v -> {
            if (isInputsEmpty()) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Invalid Entry")
                        .setMessage("Kindly fill in all inputs")
                        .setIcon(R.drawable.ic_sad_face)
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        }).setCancelable(false).show();
            } else if (!isAccNumberValid()) {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Invalid Entry")
                        .setMessage("Kindly enter a valid account number of 10 digits")
                        .setIcon(R.drawable.ic_sad_face)
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                            binding.editAccountNumber.requestFocus();
                        }).setCancelable(false).show();
            } else if (!isAccNameMatching()) {
                new MaterialAlertDialogBuilder(TransporterBankOptionActivity.this)
                        .setTitle("Are you sure ?")
                        .setMessage("Account name doesn't match Transporter's name")
                        .setIcon(R.drawable.ic_sad_face)
                        .setPositiveButton("Yes", (dialog, which) -> {
                            displayConfirmationDialog();
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                            binding.editAccountName.requestFocus();
                        }).setCancelable(false).show();
            } else {
                //Save details.
                displayConfirmationDialog();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    public void initBankNameAdapter() {
        ArrayAdapter<String> bank_adapter =
                new ArrayAdapter<>(getApplicationContext(),
                        R.layout.dropdown_menu_transporter_popup_item,
                        AppUtils.bank_names);
        binding.atvBank.setAdapter(bank_adapter);
    }

    public boolean isInputsEmpty() {
        if (binding.editAccountName.getText().toString().isEmpty()) {
            return true;
        } else if (binding.editAccountNumber.getText().toString().isEmpty()) {
            return true;
        } else return binding.atvBank.getText().toString().isEmpty();
    }

    public boolean isAccNumberValid() {
        return binding.editAccountNumber.getText().toString().length() == 10;
    }

    public boolean isAccNameMatching() {
        String entered_name = binding.editAccountName.getText().toString().toLowerCase();
        return entered_name.contains(session.GET_REG_FIRST_NAME().toLowerCase())
                && entered_name.contains(session.GET_REG_LAST_NAME().toLowerCase());
    }

    public void displayConfirmationDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirm details")
                .setMessage("Are you sure you want to register this transporter ?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    saveRegDetailsBankOption();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                }).setCancelable(false).show();
    }

    public void saveRegDetailsBankOption() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            db.getTransporterDao().insertSingleTransporter(getTransporterInfo());
            db.getOpAreaDao().insertOpAreasList(getOperatingAreasInfo());

            runOnUiThread(() -> {
                new MaterialAlertDialogBuilder(TransporterBankOptionActivity.this)
                        .setIcon(R.drawable.ic_smiley_face)
                        .setTitle("Congratulations")
                        .setMessage("Transporter successfully registered")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            //Return Home.
                            session.CLEAR_REG_SESSION();
                            startActivity(new Intent(this, TransporterHomeActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }).setCancelable(false).show();
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
                binding.editAccountNumber.getText().toString(),
                binding.editAccountName.getText().toString(),
                binding.atvBank.getText().toString(),
                session.GET_REG_FACE_TEMPLATE(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                "0");
    }

    public List<OperatingAreasTable> getOperatingAreasInfo() {
        List<String> ops_areas = new Gson().fromJson(session.GET_REG_COLLECTION_CENTERS(), AppUtils.stringType);
        List<OperatingAreasTable> areas_list = new ArrayList<>();

        for (String x : ops_areas) {
            areas_list.add(new OperatingAreasTable(session.GET_REG_PHONE_NUMBER(),
                    x,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime())));
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
    }
}
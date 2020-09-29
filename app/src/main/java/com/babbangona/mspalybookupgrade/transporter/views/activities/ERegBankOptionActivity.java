package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterBankOptionBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomOneButtonBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomTwoButtonsBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.babbangona.mspalybookupgrade.transporter.views.TransporterHomeActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ERegBankOptionActivity extends AppCompatActivity {
    /**
     * NOTE: This activity makes use of the same layout as RegBankOptionActivity.
     */
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
                promptEmptyInputs();
            } else if (!isAccNumberValid()) {
                promptInvalidAccNumber();
            } else if (isBankDetailsDuplicate()) {
                promptDuplicateAccDetails();
            } else if (!isAccNameMatching()) {
                promptBankNameMismatch();
            } else {
                //Save details.
                promptConfirmationDialog();
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

    public void promptConfirmationDialog() {
        DialogCustomTwoButtonsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_two_buttons, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        binding.btnRight.setText("Yes");
        binding.btnLeft.setText("Cancel");
        binding.mtvDialogText.setText("Are you sure you want to register this transporter and save booking details ?");
        binding.imgBtnCancel.setVisibility(View.GONE);

        binding.btnRight.setOnClickListener(v -> {
            dialog.dismiss();
            saveRegAndBookingDetailsBankOption();
        });

        binding.btnLeft.setOnClickListener(v -> {
            name_mismatch_flag = 0;
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    public void promptEmptyInputs() {
        DialogCustomOneButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_sad_face);
        binding.btnPrimary.setText("Okay");
        binding.mtvDialogText.setText("Kindly fill in all inputs");

        binding.btnPrimary.setOnClickListener(v -> {
            dialog.dismiss();
        });

        binding.imgBtnCancel.setVisibility(View.GONE);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void promptInvalidAccNumber() {
        DialogCustomOneButtonBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding.getRoot());
        binding.imgViewAvatar.setImageResource(R.drawable.ic_sad_face);
        binding.btnPrimary.setText("Okay");
        binding.mtvDialogText.setText("Kindly check the account number again");

        binding.btnPrimary.setOnClickListener(v -> {
            dialog.dismiss();
        });

        binding.imgBtnCancel.setVisibility(View.GONE);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void promptDuplicateAccDetails() {
        DialogCustomOneButtonBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_one_button, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.imgViewAvatar.setImageResource(R.drawable.ic_sad_face);
        dialogBinding.btnPrimary.setText("Okay");
        dialogBinding.mtvDialogText.setText("This bank details already exist");

        dialogBinding.btnPrimary.setOnClickListener(v -> {
            dialog.dismiss();
            binding.editAccountNumber.requestFocus();
        });

        dialogBinding.imgBtnCancel.setVisibility(View.GONE);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void promptBankNameMismatch() {
        DialogCustomTwoButtonsBinding dialog_binding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_two_buttons, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(dialog_binding.getRoot());
        dialog_binding.imgViewAvatar.setImageResource(R.drawable.ic_sad_face);
        dialog_binding.btnRight.setText("Yes");
        dialog_binding.btnLeft.setText("Cancel");
        dialog_binding.mtvDialogText.setText("Are you sure ? \n Account name doesn't match Transporter's name");
        dialog_binding.imgBtnCancel.setOnClickListener(v -> dialog.cancel());

        dialog_binding.btnRight.setOnClickListener(v -> {
            dialog.dismiss();
            name_mismatch_flag = 1;
            promptConfirmationDialog();
        });

        dialog_binding.btnLeft.setOnClickListener(v -> {
            dialog.dismiss();
            name_mismatch_flag = 0;
            binding.editAccountName.requestFocus();
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

    public void saveRegAndBookingDetailsBankOption() {
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
                "Bank Account",
                "N/A",
                0,
                binding.editAccountNumber.getText().toString(),
                binding.editAccountName.getText().toString(),
                name_mismatch_flag,
                binding.atvBank.getText().toString(),
                "N/A",
                0,
                session.GET_LOG_IN_STAFF_ID(),
                AppUtils.getDeviceID(ERegBankOptionActivity.this),
                BuildConfig.VERSION_NAME,
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
                session.GET_LOG_IN_STAFF_ID(),
                AppUtils.getDeviceID(ERegBankOptionActivity.this),
                BuildConfig.VERSION_NAME,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                0);
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
}
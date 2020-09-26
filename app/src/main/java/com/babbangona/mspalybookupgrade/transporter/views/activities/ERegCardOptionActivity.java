package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterCardOptionBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomOneButtonBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomTwoButtonsBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogTransporterCardNumberBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CardsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.babbangona.mspalybookupgrade.transporter.views.TransporterHomeActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ERegCardOptionActivity extends AppCompatActivity {
    /**
     * Note: This activity uses the same layout as RegCardOptionActivity
     */
    ActivityTransporterCardOptionBinding binding;
    TransporterDatabase db;
    TSessionManager session;
    StringBuilder card_number;
    ProgressDialog d;
    Boolean allowWrongCard;
    Integer invalid_card_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_card_option);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        d = new ProgressDialog(ERegCardOptionActivity.this);
        d.setTitle("Please wait...");
        d.setMessage("Verifying card information");
        d.setCancelable(false);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);
        allowWrongCard = false;
        invalid_card_flag = 0;


        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        binding.pinViewLeftTop.setPinViewEventListener((pinview, fromUser) -> binding.pinViewRightTop.requestPinEntryFocus());

        binding.pinViewRightTop.setPinViewEventListener((pinview, fromUser) -> binding.pinViewBottom.requestPinEntryFocus());

        binding.btnCaptureCard.setOnClickListener(v -> {
            //String card_number = extractCardNumber();

            if (!isCardNumberEntered()) {
                Toast.makeText(this, "Kindly enter complete card number", Toast.LENGTH_LONG).show();
            } else {
                captureCardImage();
            }
        });

        binding.btnContinue.setOnClickListener(v -> {
            String card_no = extractCardNumber();

            if (!isCardNumberEntered()) {
                Toast.makeText(this, "Kindly enter complete card number", Toast.LENGTH_LONG).show();
            } else if (!isCardNumberValid(card_no) && !allowWrongCard) {
                Toast.makeText(this, "Invalid card number", Toast.LENGTH_LONG).show();
                cardNumberDialog(card_no);
            } else if (!picExists()) {
                Toast.makeText(this, "Kindly capture picture of transporter with card", Toast.LENGTH_LONG).show();
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

    public boolean isCardNumberEntered() {
        if (binding.pinViewLeftTop.getValue().length() != 2) {
            return false;
        } else if (binding.pinViewRightTop.getValue().length() != 2) {
            return false;
        } else return binding.pinViewBottom.getValue().length() == 5;

    }

    public String extractCardNumber() {
        if (isCardNumberEntered()) {
            card_number = new StringBuilder();
            card_number.append(binding.pinViewLeftTop.getValue())
                    .append("/")
                    .append(binding.pinViewRightTop.getValue())
                    .append(binding.pinViewBottom.getValue());

            return card_number.toString();
        }

        return "Empty";
    }

    public boolean isCardNumberValid(String card_no) {
        d.show();

        CardsTable card = db.getCardsDao().getSingleCard(card_no);
        d.dismiss();
        return card != null;
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
            saveRegDetailsCardOption();
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

    public void saveRegDetailsCardOption() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            db.getTransporterDao().insertSingleTransporter(getTransporterInfo());
            db.getCoachLogsDao().insertSingleCoachLog(getBookingInfo());

            runOnUiThread(this::promptSaveSuccessful);
        });
    }

    public TransporterTable getTransporterInfo() {
        String card_no = extractCardNumber();

        return new TransporterTable(session.GET_E_REG_PHONE_NUMBER(),
                session.GET_E_REG_FIRST_NAME(),
                session.GET_E_REG_LAST_NAME(),
                "N/A",
                "BG Card",
                card_no,
                invalid_card_flag,
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

    public void captureCardImage() {

        Intent capture_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (capture_intent.resolveActivity(getPackageManager()) != null) {
            String img_name = session.GET_REG_PHONE_NUMBER() + "_bg_card.jpg";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File storage_dir = new File(Environment.getExternalStorageDirectory().getPath(), AppUtils.bg_cards_location);
                if (!storage_dir.exists() && !storage_dir.mkdirs()) {
                    //Unable to create.
                } else {
                    Uri photo_uri = FileProvider.getUriForFile(ERegCardOptionActivity.this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            new File(storage_dir.getPath() + File.separator + img_name));
                    capture_intent.putExtra(MediaStore.EXTRA_OUTPUT, photo_uri);
                    startActivity(capture_intent);
                }
            }
        }
    }

    public boolean picExists() {
        String img_name = session.GET_REG_PHONE_NUMBER() + "_bg_card.jpg";

        File dir = new File(Environment.getExternalStorageDirectory().getPath(), AppUtils.bg_cards_location);
        try {
            dir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            File[] list = dir.listFiles();
            for (File dir_file : Objects.requireNonNull(list)) {
                if (dir_file.isFile()) {
                    String file_name = dir_file.getName();
                    if (file_name.equals(img_name)) {
                        //Compress image and add to shared prefs before returning.
                        if (AppUtils.compressImage(img_name, AppUtils.bg_cards_location)) {
                            session.SET_TRANSPORTER_CARDS(img_name);
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void cardNumberDialog(String prev_card_no) {
        DialogTransporterCardNumberBinding binding_dialog =
                DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_transporter_card_number, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(binding_dialog.getRoot());
        binding_dialog.imgBtnClose.setOnClickListener(v -> dialog.dismiss());
        binding_dialog.btnSubmit.setOnClickListener(v -> {
            String card_no_1 = binding_dialog.editEnterCardNumber.getText().toString().trim();
            String card_no_2 = binding_dialog.editConfirmCardNumber.getText().toString().trim();

            if (card_no_1.equals(card_no_2) && !card_no_1.isEmpty()) {
                if (card_no_1.equals(prev_card_no)) {
                    Toast.makeText(this, "Card Number accepted. Kindly submit details...", Toast.LENGTH_LONG).show();
                    allowWrongCard = true;
                    invalid_card_flag = 1;
                } else {
                    //Reject and clear fields.
                    Toast.makeText(this, "Card Number rejected", Toast.LENGTH_LONG).show();
                    binding.pinViewLeftTop.clearValue();
                    binding.pinViewRightTop.clearValue();
                    binding.pinViewBottom.clearValue();
                }
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Check Card numbers again", Toast.LENGTH_LONG).show();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }
}
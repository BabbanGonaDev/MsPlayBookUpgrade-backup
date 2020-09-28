package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterCardOptionBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogTransporterCardNumberBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CardsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.babbangona.mspalybookupgrade.transporter.views.TransporterHomeActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RegCardOptionActivity extends AppCompatActivity {
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

        d = new ProgressDialog(RegCardOptionActivity.this);
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
                Toast.makeText(RegCardOptionActivity.this, "Kindly enter complete card number", Toast.LENGTH_LONG).show();
            } else {
                captureCardImage();
            }
        });

        binding.btnContinue.setOnClickListener(v -> {
            String card_no = extractCardNumber();

            if (!isCardNumberEntered()) {
                Toast.makeText(RegCardOptionActivity.this, "Kindly enter complete card number", Toast.LENGTH_LONG).show();
            } else if (!isCardNumberValid(card_no) && !allowWrongCard) {
                Toast.makeText(RegCardOptionActivity.this, "Invalid card number", Toast.LENGTH_LONG).show();
                cardNumberDialog(card_no);
            } else if (!picExists()) {
                Toast.makeText(RegCardOptionActivity.this, "Kindly capture picture of transporter with card", Toast.LENGTH_LONG).show();
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

    public void displayConfirmationDialog() {
        AlertDialog confirm_check = new MaterialAlertDialogBuilder(this)
                .setTitle("Confirm details")
                .setMessage("Are you sure you want to register this transporter ?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    saveRegDetailsCardOption();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                }).setCancelable(false).show();
        confirm_check.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        confirm_check.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
    }

    public void saveRegDetailsCardOption() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            db.getTransporterDao().insertSingleTransporter(getTransporterInfo());
            db.getOpAreaDao().insertOpAreasList(getOperatingAreasInfo());

            runOnUiThread(() -> {
                AlertDialog congrats = new MaterialAlertDialogBuilder(RegCardOptionActivity.this)
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
                congrats.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
            });
        });
    }

    public TransporterTable getTransporterInfo() {
        String card_no = extractCardNumber();

        return new TransporterTable(session.GET_REG_PHONE_NUMBER(),
                session.GET_REG_FIRST_NAME(),
                session.GET_REG_LAST_NAME(),
                session.GET_REG_VEHICLE_TYPE(),
                "BG Card",
                card_no,
                invalid_card_flag,
                "N/A",
                "N/A",
                0,
                "N/A",
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
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                    0));
        }

        return areas_list;
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
                    Uri photo_uri = FileProvider.getUriForFile(RegCardOptionActivity.this,
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
                DataBindingUtil.inflate(LayoutInflater.from(RegCardOptionActivity.this), R.layout.dialog_transporter_card_number, null, false);

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
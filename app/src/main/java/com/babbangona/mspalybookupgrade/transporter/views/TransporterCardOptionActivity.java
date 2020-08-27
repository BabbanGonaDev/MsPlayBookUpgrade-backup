package com.babbangona.mspalybookupgrade.transporter.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterCardOptionBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TransporterCardOptionActivity extends AppCompatActivity {
    ActivityTransporterCardOptionBinding binding;
    TransporterDatabase db;
    TSessionManager session;
    StringBuilder card_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_card_option);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);

        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        binding.pinViewLeftTop.setPinViewEventListener((pinview, fromUser) -> binding.pinViewRightTop.requestPinEntryFocus());

        binding.pinViewRightTop.setPinViewEventListener((pinview, fromUser) -> binding.pinViewBottom.requestPinEntryFocus());

        binding.btnCaptureCard.setOnClickListener(v -> {
            if (!isCardNumberEntered()) {
                Toast.makeText(TransporterCardOptionActivity.this, "Kindly enter complete card number", Toast.LENGTH_LONG).show();
            } else {
                captureCardImage();
            }
        });

        binding.btnContinue.setOnClickListener(v -> {
            if (!isCardNumberEntered()) {
                Toast.makeText(TransporterCardOptionActivity.this, "Kindly enter complete card number", Toast.LENGTH_LONG).show();
            } else if (!picExists()) {
                Toast.makeText(TransporterCardOptionActivity.this, "Kindly capture picture of transporter with card", Toast.LENGTH_LONG).show();
            } else {
                card_number = new StringBuilder();
                card_number.append(binding.pinViewLeftTop.getValue())
                        .append("/")
                        .append(binding.pinViewRightTop.getValue())
                        .append(binding.pinViewBottom.getValue());

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

    public boolean isCardNumberEntered() {
        if (binding.pinViewLeftTop.getValue().length() != 2) {
            return false;
        } else if (binding.pinViewRightTop.getValue().length() != 2) {
            return false;
        } else return binding.pinViewBottom.getValue().length() == 5;

    }

    public void displayConfirmationDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirm details")
                .setMessage("Are you sure you want to register this transporter ?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    saveRegDetailsCardOption();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                }).setCancelable(false).show();
    }

    public void saveRegDetailsCardOption() {
        AppExecutors.getInstance().diskIO().execute(() -> {
            db.getTransporterDao().insertSingleTransporter(getTransporterInfo());
            db.getOpAreaDao().insertOpAreasList(getOperatingAreasInfo());

            runOnUiThread(() -> {
                new MaterialAlertDialogBuilder(TransporterCardOptionActivity.this)
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
                "BG Card",
                card_number.toString(),
                "N/A",
                "N/A",
                "N/A",
                session.GET_REG_FACE_TEMPLATE(),
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
                    Uri photo_uri = FileProvider.getUriForFile(TransporterCardOptionActivity.this,
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
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
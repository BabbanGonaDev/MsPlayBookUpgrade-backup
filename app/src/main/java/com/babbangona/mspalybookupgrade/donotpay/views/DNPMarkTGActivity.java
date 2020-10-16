package com.babbangona.mspalybookupgrade.donotpay.views;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityDonotpayMarkTgBinding;
import com.babbangona.mspalybookupgrade.donotpay.data.DSessionManager;
import com.babbangona.mspalybookupgrade.donotpay.data.room.DNPDatabase;
import com.babbangona.mspalybookupgrade.donotpay.data.room.tables.DoNotPayTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class DNPMarkTGActivity extends AppCompatActivity {
    ActivityDonotpayMarkTgBinding binding;
    DNPDatabase db;
    DSessionManager session;

    //TODO: => Change this to database call.
    String[] reasons = new String[]{"This is the first reason",
            "This is the No. 2 reason",
            "Way to the Tree of Life",
            "Faith, Hope & Charity", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_donotpay_mark_tg);

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setTitle("Do Not Pay v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DNPDatabase.getInstance(this);
        session = new DSessionManager(this);

        binding.footer.tvLastSyncTime.setText("Coming Soon");
        binding.footer.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());

        initReasonAdapter();

        binding.autocompleteReason.setOnItemClickListener((parent, view, position, id) -> {
            String selected = binding.autocompleteReason.getText().toString().trim();

            if (selected.equalsIgnoreCase("others")) {
                binding.etCustomReason.setText("");
                binding.etCustomReason.setVisibility(View.VISIBLE);
            } else {
                binding.etCustomReason.setText("");
                binding.etCustomReason.setVisibility(View.GONE);
            }
        });

        binding.btnSubmit.setOnClickListener(v -> {
            String selected = binding.autocompleteReason.getText().toString().trim();

            if (selected.equalsIgnoreCase("others") && !binding.etCustomReason.getText().toString().trim().equals("")) {
                String custom = binding.etCustomReason.getText().toString().trim();
                submit(custom);
            } else if (!selected.equalsIgnoreCase("others") && !selected.equals("")) {
                submit(selected);
            } else {
                Toast.makeText(this, "Kindly enter your reason for marking the TG", Toast.LENGTH_LONG).show();
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

    public void initReasonAdapter() {
        ArrayAdapter<String> reasons_adapter =
                new ArrayAdapter<>(getApplicationContext(),
                        R.layout.dropdown_menu_transporter_popup_item,
                        reasons);
        binding.autocompleteReason.setAdapter(reasons_adapter);
    }

    public void submit(String reason) {
        AppExecutors.getInstance().diskIO().execute(() -> {
            //Save details here
            db.getDoNotPayDao().insert(new DoNotPayTable(session.GET_SELECTED_TG(),
                    reason,
                    session.GET_LOG_IN_STAFF_ID(),
                    getDeviceID(),
                    BuildConfig.VERSION_NAME,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime()),
                    0));

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Inform user of success.
                }
            });
        });

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
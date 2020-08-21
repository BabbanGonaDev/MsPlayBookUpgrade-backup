package com.babbangona.mspalybookupgrade.transporter.views;

import android.os.Bundle;
import android.text.InputFilter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterNamesBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;

public class TransporterNamesActivity extends AppCompatActivity {
    ActivityTransporterNamesBinding binding;
    TransporterDatabase db;
    TSessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_names);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);

        setNamesFilter();

    }

    public void setNamesFilter() {
        InputFilter filter = (source, start, end, dest, dStart, dEnd) -> {
            for (int i = start; i < end; i++) {
                if (!Character.isLetter(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        };
        //
    }


}
package com.babbangona.mspalybookupgrade.tpo.views;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTpoHomeBinding;

public class TPOHomeActivity extends AppCompatActivity {
    /**
     * Starting of this activity, will first trigger a QRcode scan for the warehouse.
     * Then the mark attendance button will now show.
     */
    ActivityTpoHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tpo_home);

        binding.btnMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Proceed to activity select member.
            }
        });
    }
}
package com.babbangona.mspalybookupgrade.transporter.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterLocationBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;

public class TransporterLocationActivity extends AppCompatActivity {
    ActivityTransporterLocationBinding binding;
    TransporterDatabase db;
    TSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_location);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);

        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());

        initStateAdapter();

        binding.autocompleteLocationState.setOnItemClickListener((parent, view, position, id) -> {
            binding.autocompleteLocationLga.setText("");
            TransporterLocationActivity.this.initLgaAdapter(binding.autocompleteLocationState.getText().toString().trim());
        });

        binding.btnContinue.setOnClickListener(v -> nextActivity());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    public void initStateAdapter() {
        db.getCcDao().getAllStates().observe(this, strings -> {
            ArrayAdapter<String> states_adapter =
                    new ArrayAdapter<>(getApplicationContext(),
                            R.layout.dropdown_menu_transporter_popup_item,
                            strings);
            binding.autocompleteLocationState.setAdapter(states_adapter);
        });
    }

    public void initLgaAdapter(String selected_state) {
        db.getCcDao().getAllLga(selected_state).observe(this, strings -> {
            ArrayAdapter<String> lga_adapter =
                    new ArrayAdapter<>(getApplicationContext(),
                            R.layout.dropdown_menu_transporter_popup_item,
                            strings);
            binding.autocompleteLocationLga.setAdapter(lga_adapter);
        });
    }

    public void nextActivity() {
        if (!isEmptyDropdown()) {
            //Save details to shared-prefs and go to next activity.
            String state = binding.autocompleteLocationState.getText().toString().trim();
            String lga = binding.autocompleteLocationLga.getText().toString().trim();

            startActivity(new Intent(this, TransporterCcActivity.class)
                    .putExtra("state", state)
                    .putExtra("lga", lga));
        }
    }

    public boolean isEmptyDropdown() {
        String state = binding.autocompleteLocationState.getText().toString().trim();
        String lga = binding.autocompleteLocationLga.getText().toString().trim();

        return state.isEmpty() || lga.isEmpty();
    }
}
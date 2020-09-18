package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterCcBinding;
import com.babbangona.mspalybookupgrade.transporter.adapters.SelectCcAdapter;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.models.CcModel;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CollectionCenterTable;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RegCcActivity extends AppCompatActivity {
    ActivityTransporterCcBinding binding;
    TransporterDatabase db;
    TSessionManager session;
    SelectCcAdapter adapter;
    String passed_state, passed_lga;
    List<String> selected_ccId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_cc);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);

        try {
            passed_state = getIntent().getStringExtra("state");
            passed_lga = getIntent().getStringExtra("lga");
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, "Select State and LGA first", Toast.LENGTH_LONG).show();
            finish();
        }

        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        initCollectionCentersList();

        binding.btnContinue.setOnClickListener(v -> {
            if (selected_ccId.isEmpty()) {
                Toast.makeText(RegCcActivity.this, "Kindly select at least 1 collection center", Toast.LENGTH_LONG).show();
            } else {
                session.SET_REG_COLLECTION_CENTERS(new Gson().toJson(selected_ccId));
                //Go to next activity.
                startActivity(new Intent(this, RegPayOptionActivity.class));
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

    public void initCollectionCentersList() {
        selected_ccId = new ArrayList<>();

        db.getCcDao().getCollectionCenters(passed_state, passed_lga).observe(this, ccTables -> {
            //Copy the collection center list into ccModel. So i can add an extra attribute (isSelected).
            List<CcModel> ccList = new ArrayList<>();
            for (CollectionCenterTable c : ccTables) {
                ccList.add(new CcModel(c.getCc_id(), c.getState(), c.getLga(), c.getCc_name(), c.getDate_updated()));
            }

            adapter = new SelectCcAdapter(RegCcActivity.this, ccList, new SelectCcAdapter.OnItemClickListener() {
                @Override
                public void selectCc(CcModel current_cc) {
                    binding.chipGroup.addView(createChip(current_cc.getCc_name(), current_cc.getCc_id()));
                    selected_ccId.add(current_cc.getCc_id());
                    //Toast.makeText(RegCcActivity.this, selected_ccId.size() + " selected.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void deSelectCc(CcModel current_cc) {
                    Chip remove_chip = binding.chipGroup.findViewWithTag(current_cc.getCc_id());
                    binding.chipGroup.removeView(remove_chip);
                    selected_ccId.remove(current_cc.getCc_id());
                    //Toast.makeText(RegCcActivity.this, selected_ccId.size() + " selected.", Toast.LENGTH_LONG).show();
                }
            });

            binding.rcvCollectionCenter.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.rcvCollectionCenter.setAdapter(adapter);
        });
    }

    private Chip createChip(String text, String id) {
        ChipDrawable drawable = ChipDrawable.createFromAttributes(this, null, 0, R.style.Widget_MaterialComponents_Chip_Entry);
        int paddingDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

        final Chip chip = new Chip(this);
        chip.setChipDrawable(drawable);
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(text);
        chip.setCheckable(false);
        chip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        chip.setTag(id);
        chip.setCloseIconVisible(false);
        chip.setChipIconResource(R.drawable.ic_transporter_warehouse);

        return chip;
    }
}
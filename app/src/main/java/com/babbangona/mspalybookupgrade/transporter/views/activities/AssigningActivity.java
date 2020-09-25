package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterAssigningBinding;
import com.babbangona.mspalybookupgrade.transporter.adapters.SelectTransporterAdapter;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.models.CustomTransporter;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.FavouritesTable;

public class AssigningActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ActivityTransporterAssigningBinding binding;
    TSessionManager session;
    TransporterDatabase db;
    SelectTransporterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_assigning);

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new TSessionManager(this);
        db = TransporterDatabase.getInstance(this);

        binding.footer.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.footer.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        binding.searchViewTransporters.setOnQueryTextListener(this);
        binding.searchViewTransporters.setOnClickListener(v -> binding.searchViewTransporters.setIconified(false));

        initRecycler();
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

    public void initRecycler() {
        db.getTransporterDao().getTransporterForBooking().observe(this, customTransporters -> {

            adapter = new SelectTransporterAdapter(AssigningActivity.this, customTransporters, new SelectTransporterAdapter.OnItemClickListener() {
                @Override
                public void onClick(CustomTransporter transList) {
                    //Select the transporter and move to destination activity
                    session.SET_SELECTED_TRANSPORTER(transList.getPhone_number());
                    startActivity(new Intent(AssigningActivity.this, DestinationActivity.class));
                }

                @Override
                public void makeFavourite(CustomTransporter trans) {
                    //Mark or Un-mark the transporter
                    if (trans.isFavourite()) {
                        //Un-mark as favourite
                        db.getFavouritesDao().unMarkFavourite(trans.getPhone_number(), session.GET_LOG_IN_STAFF_ID());
                        adapter.notifyDataSetChanged();
                    } else {
                        //Mark as favourite
                        db.getFavouritesDao().markFavourite(new FavouritesTable(session.GET_LOG_IN_STAFF_ID(), trans.getPhone_number(), 1, 0));
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            binding.rcvTransporters.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.rcvTransporters.setItemAnimator(new DefaultItemAnimator());
            binding.rcvTransporters.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            binding.rcvTransporters.setAdapter(adapter);
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            adapter.getFilter().filter(newText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
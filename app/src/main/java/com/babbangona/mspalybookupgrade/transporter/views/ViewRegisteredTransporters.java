package com.babbangona.mspalybookupgrade.transporter.views;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityViewRegisteredTransportersBinding;
import com.babbangona.mspalybookupgrade.transporter.adapters.ViewTransportersAdapter;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;

public class ViewRegisteredTransporters extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ActivityViewRegisteredTransportersBinding binding;
    TSessionManager session;
    ViewTransportersAdapter adapter;
    TransporterDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_registered_transporters);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = TransporterDatabase.getInstance(this);
        session = new TSessionManager(this);

        binding.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        binding.searchView.setOnQueryTextListener(this);

        binding.searchView.setOnClickListener(v -> binding.searchView.setIconified(false));

        initTransportersRecycler();
    }

    public void initTransportersRecycler() {
        db.getTransporterDao().getAllTransporters().observe(this, transporter_list -> {

            adapter = new ViewTransportersAdapter(ViewRegisteredTransporters.this, transporter_list, trans_list -> {
                //Do nothing.
            });

            binding.recyclerViewTransporters.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.recyclerViewTransporters.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
            binding.recyclerViewTransporters.setAdapter(adapter);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.empty_menu, menu);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            adapter.getFilter().filter(newText.toLowerCase());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
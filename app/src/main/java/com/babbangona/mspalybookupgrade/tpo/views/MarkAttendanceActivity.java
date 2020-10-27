package com.babbangona.mspalybookupgrade.tpo.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.databinding.ActivityTpoMarkAttendanceBinding;
import com.babbangona.mspalybookupgrade.tpo.adapters.ViewMembersAdapter;
import com.babbangona.mspalybookupgrade.tpo.data.TPOSessionManager;
import com.babbangona.mspalybookupgrade.tpo.viewmodels.MembersViewModel;
import com.google.gson.Gson;

public class MarkAttendanceActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    ActivityTpoMarkAttendanceBinding binding;
    TPOSessionManager session;
    private MembersViewModel model;
    private ViewMembersAdapter adapter;
    private AppDatabase appDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tpo_mark_attendance);
        model = new ViewModelProvider(this).get(MembersViewModel.class);
        appDb = AppDatabase.getInstance(this);

        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new TPOSessionManager(this);

        binding.footer.tvLastSyncTime.setText(session.GET_LAST_SYNC_TIME());
        binding.footer.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());

        initRecycler();

        model.setSearchText("");

        model.getPagedMembersList(appDb.membersDao()).observe(this, memberModels -> adapter.submitList(memberModels));

        binding.svMembers.setOnQueryTextListener(this);

        binding.svMembers.setOnClickListener(v -> binding.svMembers.setIconified(false));
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
        adapter = new ViewMembersAdapter(mem -> {
            //Save to shared prefs and move to last activity.
            session.SET_SELECTED_MEMBER(new Gson().toJson(mem));
            startActivity(new Intent(this, SaveTPODetailsActivity.class));
        });

        binding.rcvMembers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.rcvMembers.setItemAnimator(new DefaultItemAnimator());
        binding.rcvMembers.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        binding.rcvMembers.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        model.setSearchText("%" + s.toLowerCase() + "%");
        return false;
    }
}
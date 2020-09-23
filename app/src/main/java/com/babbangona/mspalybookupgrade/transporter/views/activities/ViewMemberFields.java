package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.databinding.ActivityViewMemberFieldsBinding;
import com.babbangona.mspalybookupgrade.transporter.adapters.ViewFieldsAdapter;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.models.Members;

public class ViewMemberFields extends AppCompatActivity {
    ActivityViewMemberFieldsBinding binding;
    ViewFieldsAdapter adapter;
    AppDatabase appDb;
    TSessionManager session;
    Members passed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_member_fields);
        appDb = AppDatabase.getInstance(this);

        session = new TSessionManager(this);

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.footer.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.footer.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        try {
            passed = getIntent().getParcelableExtra("member_details");
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        appDb.fieldsDao().getMemberFields(passed.getUniqueMemberId()).observe(this, fields -> {
            adapter = new ViewFieldsAdapter(ViewMemberFields.this, fields, field -> {
                //Do Nothing
            });

            binding.rcvFields.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.rcvFields.setItemAnimator(new DefaultItemAnimator());
            binding.rcvFields.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
            binding.rcvFields.setAdapter(adapter);
        });
    }
}
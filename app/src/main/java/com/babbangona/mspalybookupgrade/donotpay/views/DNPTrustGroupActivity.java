package com.babbangona.mspalybookupgrade.donotpay.views;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.databinding.ActivityDonotpayTrustGroupBinding;
import com.babbangona.mspalybookupgrade.donotpay.adapter.TrustGroupAdapter;
import com.babbangona.mspalybookupgrade.donotpay.data.DSessionManager;
import com.babbangona.mspalybookupgrade.donotpay.data.models.TGList;
import com.babbangona.mspalybookupgrade.donotpay.data.room.DNPDatabase;

public class DNPTrustGroupActivity extends AppCompatActivity {
    ActivityDonotpayTrustGroupBinding binding;
    DNPDatabase db;
    AppDatabase appDb;
    DSessionManager session;
    TrustGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_donotpay_trust_group);

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setTitle("Do Not Pay v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = DNPDatabase.getInstance(this);
        appDb = AppDatabase.getInstance(this);
        session = new DSessionManager(this);

        binding.footer.tvLastSyncTime.setText("Coming Soon");
        binding.footer.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());

        initTrustGroupRecycler();
    }

    private void initTrustGroupRecycler() {
        appDb.membersDao().getTrustGroupsList().observe(this, tgLists -> {
            Toast.makeText(this, "Size: " + tgLists.size(), Toast.LENGTH_LONG).show();
            //put adapter here
            adapter = new TrustGroupAdapter(this, tgLists, new TrustGroupAdapter.OnItemClickListener() {
                @Override
                public void onClick(TGList list) {
                    //
                }
            });

            binding.rcvTrustGroups.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.rcvTrustGroups.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
            binding.rcvTrustGroups.setAdapter(adapter);
        });
    }
}
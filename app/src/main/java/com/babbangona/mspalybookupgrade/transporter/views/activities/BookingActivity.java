package com.babbangona.mspalybookupgrade.transporter.views.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ActivityTransporterBookingBinding;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.views.fragments.IssueLogFragment;
import com.babbangona.mspalybookupgrade.transporter.views.fragments.SelectMemberFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class BookingActivity extends AppCompatActivity {
    ActivityTransporterBookingBinding binding;
    TSessionManager session;

    private String[] titles = new String[]{"Select Member", "Issue Log"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transporter_booking);

        setSupportActionBar(binding.header.toolbar);
        getSupportActionBar().setTitle("MS Playbook v" + BuildConfig.VERSION_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new TSessionManager(this);

        binding.footer.tvStaffId.setText(session.GET_LOG_IN_STAFF_ID());
        binding.footer.tvLastSyncTime.setText(session.GET_LAST_SYNC_TRANSPORTER());

        initTabs();
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

    public void initTabs() {
        binding.viewPager.setAdapter(new ViewPagerFragmentAdapter(this));

        //Attach Tab Mediator
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            tab.setText(titles[position]);
        }).attach();
    }


    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new SelectMemberFragment();
                case 1:
                    return new IssueLogFragment();
            }
            return new SelectMemberFragment();
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }
    }
}
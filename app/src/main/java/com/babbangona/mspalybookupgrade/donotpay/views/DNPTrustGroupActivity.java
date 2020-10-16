package com.babbangona.mspalybookupgrade.donotpay.views;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.databinding.ActivityDonotpayTrustGroupBinding;
import com.babbangona.mspalybookupgrade.databinding.DialogCustomTwoButtonsBinding;
import com.babbangona.mspalybookupgrade.donotpay.adapter.TrustGroupAdapter;
import com.babbangona.mspalybookupgrade.donotpay.data.DSessionManager;
import com.babbangona.mspalybookupgrade.donotpay.data.models.TGList;
import com.babbangona.mspalybookupgrade.donotpay.data.room.DNPDatabase;

public class DNPTrustGroupActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
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

        binding.searchView.setOnQueryTextListener(this);

        initTrustGroupRecycler();
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

    private void initTrustGroupRecycler() {
        appDb.membersDao().getTrustGroupsList().observe(this, tgLists -> {
            //put adapter here
            adapter = new TrustGroupAdapter(this, tgLists, list -> requestMarkTg(list));

            binding.rcvTrustGroups.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.rcvTrustGroups.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
            binding.rcvTrustGroups.setAdapter(adapter);
        });
    }

    public void requestMarkTg(TGList tg) {
        DialogCustomTwoButtonsBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(this), R.layout.dialog_custom_two_buttons, null, false);

        Dialog dialog = new Dialog(this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        dialog.setContentView(dialogBinding.getRoot());
        dialogBinding.btnLeft.setText("No");
        dialogBinding.btnRight.setText("Yes");
        dialogBinding.imgViewAvatar.setImageResource(R.drawable.ic_smiley_face);
        dialogBinding.mtvDialogText.setText("Are you going to mark the TG as Do Not Pay ?");
        dialogBinding.imgBtnCancel.setVisibility(View.GONE);

        dialogBinding.btnLeft.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialogBinding.btnRight.setOnClickListener(v -> {
            //Save tg and move to next activity.
            session.SET_SELECTED_TG(tg.getIk_number());
            startActivity(new Intent(this, DNPMarkTGActivity.class));
            dialog.dismiss();
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.getFilter().filter(s);
        return false;
    }
}
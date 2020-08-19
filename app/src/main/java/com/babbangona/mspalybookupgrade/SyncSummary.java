package com.babbangona.mspalybookupgrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.SyncSummaryAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SyncSummary extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.emptyView)
    ImageView emptyView;

    @BindView(R.id.toolbar_sync_summary)
    Toolbar toolbar_sync_summary;

    @BindView(R.id.staff_id)
    MaterialTextView staff_id;

    SyncSummaryAdapter syncSummaryAdapter;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_summary);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_sync_summary);
        sharedPrefs = new SharedPrefs(SyncSummary.this);
        appDatabase = AppDatabase.getInstance(SyncSummary.this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.sync_summary_title));
        getSupportActionBar().setSubtitle(sharedPrefs.getStaffID());
        staff_id.setText(sharedPrefs.getStaffID());
        toolbar_sync_summary.setNavigationOnClickListener(view -> onBackPressed());

        runOnUiThread(this::initActivitiesRecycler);
    }



    public void initActivitiesRecycler(){

        appDatabase
                .syncSummaryDao()
                .getAllSyncSummary(sharedPrefs.getStaffID())
                .observe(this,syncSummaryList -> {
                    syncSummaryAdapter = new SyncSummaryAdapter(syncSummaryList, SyncSummary.this);
                    RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(SyncSummary.this);
                    int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
                    VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
                    recycler_view.addItemDecoration(verticalSpaceItemDecoration);
                    recycler_view.setLayoutManager(aLayoutManager);
                    recycler_view.setItemAnimator(new DefaultItemAnimator());
                    updateView(syncSummaryList.size());
                    recycler_view.setAdapter(syncSummaryAdapter);
                    syncSummaryAdapter.notifyDataSetChanged();
                });

    }

    private void updateView(int itemCount) {
        if (itemCount > 0) {
            // The list is not empty. Show the recycler view.
            showView(recycler_view);
            hideView(emptyView);
        } else {
            // The list is empty. Show the empty list view
            hideView(recycler_view);
            showView(emptyView);
        }

    }

    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

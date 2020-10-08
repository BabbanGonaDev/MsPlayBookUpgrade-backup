package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.ComingSoon;
import com.babbangona.mspalybookupgrade.Homepage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ActivityListRecycler.ActivityListAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler.ThreshingFieldListAdapter;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FieldList extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.last_sync_date_tv)
    TextView last_sync_date_tv;

    @BindView(R.id.tv_staff_id)
    TextView tv_staff_id;

    SetPortfolioMethods setPortfolioMethods;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    ThreshingFieldListAdapter threshingFieldListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_list_top);
        ButterKnife.bind(FieldList.this);
        appDatabase = AppDatabase.getInstance(FieldList.this);
        sharedPrefs = new SharedPrefs(FieldList.this);
        setPortfolioMethods = new SetPortfolioMethods();
        initActivitiesRecycler();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(FieldList.this));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,FieldList.this);
    }

    public void initActivitiesRecycler(){

        appDatabase
                .fieldsDao()
                .getThreshingMemberFields(sharedPrefs.getKeyThreshingUniqueMemberId())
                .observe(this,activityLists -> {
                    threshingFieldListAdapter = new ThreshingFieldListAdapter(
                            appDatabase.fieldsDao().getThreshingMemberFieldsList(sharedPrefs.getKeyThreshingUniqueMemberId()),
                            FieldList.this);

                    RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(FieldList.this);
                    recycler_view.setLayoutManager(aLayoutManager);
                    recycler_view.setItemAnimator(new DefaultItemAnimator());
                    recycler_view.setAdapter(threshingFieldListAdapter);
                    threshingFieldListAdapter.notifyDataSetChanged();
                });

    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(FieldList.this, MemberList.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.threshing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.schedule) {
            startActivity(new Intent(FieldList.this, CalenderViewActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
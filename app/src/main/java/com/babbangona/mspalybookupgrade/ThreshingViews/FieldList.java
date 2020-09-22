package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.babbangona.mspalybookupgrade.Homepage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ActivityListRecycler.ActivityListAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler.ThreshingFieldListAdapter;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FieldList extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    ThreshingFieldListAdapter threshingFieldListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_list);
        ButterKnife.bind(FieldList.this);
        appDatabase = AppDatabase.getInstance(FieldList.this);
        sharedPrefs = new SharedPrefs(FieldList.this);
        initActivitiesRecycler();
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
}
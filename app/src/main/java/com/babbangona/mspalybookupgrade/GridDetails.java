package com.babbangona.mspalybookupgrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;


import com.babbangona.mspalybookupgrade.RecyclerAdapters.GridDetailsRecycler.GridDetailsRecyclerAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.GridDetailsRecycler.GridDetailsRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridDetails extends AppCompatActivity {



    @BindView(R.id.toolbar_grid_details)
    Toolbar toolbar_grid_details;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.btn_filter)
    Button btn_filter;

    AppDatabase appDatabase;
    GridDetailsRecyclerAdapter gridDetailsRecyclerAdapter;
    SharedPrefs sharedPrefs;
    GridDetailsRecyclerModel gridDetailsRecyclerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_details);
        ButterKnife.bind(this);
        appDatabase = AppDatabase.getInstance(GridDetails.this);
        sharedPrefs = new SharedPrefs(GridDetails.this);
        gridDetailsRecyclerModel = new GridDetailsRecyclerModel();

        initActivitiesRecycler();

    }

    public void initActivitiesRecycler(){
        appDatabase
                .fieldsDao()
                .viewLongLatRange("%"+sharedPrefs.getStaffID()+"%")
                .observe(this,initialGridDetailsModel -> {
                    gridDetailsRecyclerAdapter = new GridDetailsRecyclerAdapter(GridDetails.this,gridDetailsRecyclerModel.getAlmostVerticalGridModel(initialGridDetailsModel));
                    RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(GridDetails.this);
                    recycler_view.setLayoutManager(aLayoutManager);
                    recycler_view.setItemAnimator(new DefaultItemAnimator());
                    int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
                    VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
                    recycler_view.addItemDecoration(verticalSpaceItemDecoration);
                    recycler_view.setAdapter(gridDetailsRecyclerAdapter);
                    gridDetailsRecyclerAdapter.notifyDataSetChanged();
                });

    }
}

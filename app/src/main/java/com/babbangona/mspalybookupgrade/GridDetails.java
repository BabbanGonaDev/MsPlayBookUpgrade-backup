package com.babbangona.mspalybookupgrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import com.babbangona.mspalybookupgrade.RecyclerAdapters.GridDetailsRecycler.GridDetailsRecyclerAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.GridDetailsRecycler.GridDetailsRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.babbangona.mspalybookupgrade.utils.Main2ActivityMethods;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        GPSController.initialiseLocationListener(GridDetails.this);
        gridDetailsRecyclerModel = new GridDetailsRecyclerModel();
        setSupportActionBar(toolbar_grid_details);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getActivityTitle());

        initActivitiesRecycler();

        toolbar_grid_details.setNavigationOnClickListener(v -> loadPreviousActivity());
        Main2ActivityMethods main2ActivityMethods = new Main2ActivityMethods(GridDetails.this);
        main2ActivityMethods.confirmPhoneDate();
        main2ActivityMethods.confirmLocationOpen();

    }

    String getActivityTitle(){
        String title;
        if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){
            title = getResources().getString(R.string.fert_1_title);
        }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("2")){
            title = getResources().getString(R.string.fert_2_title);
        }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("3")){
            title = getResources().getString(R.string.HG_title);
        }else{
            title = "";
        }
        return title;
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

    @Override
    public void onBackPressed() {
        finish();
        loadPreviousActivity();
    }

    @OnClick(R.id.btn_filter)
    public void loadAllFields(){
        Intent intent;
        if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("3")) {
            intent = new Intent(GridDetails.this, HGFieldListPage.class);
        } else {
            intent = new Intent(GridDetails.this, FieldListPage.class);
        }
        sharedPrefs.setKeyRouteType("3");
        startActivity(intent);
    }

    void loadPreviousActivity(){
        startActivity(new Intent(GridDetails.this,Homepage.class));
    }
}

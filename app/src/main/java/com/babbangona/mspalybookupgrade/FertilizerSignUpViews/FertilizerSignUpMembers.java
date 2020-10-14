package com.babbangona.mspalybookupgrade.FertilizerSignUpViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.ThreshingViews.FieldList;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FertilizerSignUpMembers extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_sign_up_members);
        ButterKnife.bind(FertilizerSignUpMembers.this);
        appDatabase = AppDatabase.getInstance(FertilizerSignUpMembers.this);
        sharedPrefs = new SharedPrefs(FertilizerSignUpMembers.this);
        setPortfolioMethods = new SetPortfolioMethods();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(FertilizerSignUpMembers.this));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,FertilizerSignUpMembers.this);
    }
}
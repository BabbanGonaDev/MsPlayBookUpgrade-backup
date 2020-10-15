package com.babbangona.mspalybookupgrade.FertilizerSignUpViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpMembersRecycler.FertilizerMembersPageListModelClass;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpMembersRecycler.FertilizerMembersRecyclerViewAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.MembersDao;
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

    @BindView(R.id.emptyView)
    ImageView emptyView;

    SetPortfolioMethods setPortfolioMethods;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    FertilizerMembersRecyclerViewAdapter fertilizerMembersRecyclerViewAdapter;

    FertilizerMembersPageListModelClass fertilizerMembersPageListModelClass;

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



        fertilizerMembersPageListModelClass = new ViewModelProvider(this, new MyViewModelFactory(appDatabase.membersDao(), this)).get(FertilizerMembersPageListModelClass.class);
        fertilizerMembersPageListModelClass.filterTextAll.setValue("");

        setAdapter();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,FertilizerSignUpMembers.this);
    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(FertilizerSignUpMembers.this, FertilizerSignUpHome.class);
        startActivity(intent);
    }

    public static class MyViewModelFactory implements ViewModelProvider.Factory {
        private MembersDao application;
        private Context context;

        MyViewModelFactory(MembersDao application, Context context) {
            this.application = application;
            this.context = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new FertilizerMembersPageListModelClass(application, context);
        }
    }

    private void setAdapter() {
        fertilizerMembersRecyclerViewAdapter = new FertilizerMembersRecyclerViewAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
        recycler_view.addItemDecoration(verticalSpaceItemDecoration);
        recycler_view.setLayoutManager(layoutManager);

        fertilizerMembersPageListModelClass.memberRecyclerModelList.observe(this,memberRecyclerModelList -> {


            updateView(memberRecyclerModelList.size());
            fertilizerMembersRecyclerViewAdapter.submitList(memberRecyclerModelList);

            memberRecyclerModelList.addWeakCallback(null, new PagedList.Callback() {
                @Override
                public void onChanged(int position, int count) {
                    updateView(memberRecyclerModelList.size());
                }

                @Override
                public void onInserted(int position, int count) {

                }

                @Override
                public void onRemoved(int position, int count) {

                }
            });
        });
        recycler_view.setAdapter(fertilizerMembersRecyclerViewAdapter);

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

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }
}
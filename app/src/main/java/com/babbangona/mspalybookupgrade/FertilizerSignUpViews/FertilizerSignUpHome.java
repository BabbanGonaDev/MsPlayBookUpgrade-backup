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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpHomeRecycler.FertilizerHomePageListModelClass;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpHomeRecycler.FertilizerHomeRecyclerViewAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.ThreshingViews.ThreshingActivity;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.MembersDao;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FertilizerSignUpHome extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_search)
    TextView et_search;

    @BindView(R.id.search_edit_text)
    EditText search_edit_text;

    @BindView(R.id.back_to_toolbar)
    ImageView back_to_toolbar;

    @BindView(R.id.search_linear_layout)
    LinearLayout search_linear_layout;

    @BindView(R.id.toolbar_linear_layout)
    LinearLayout toolbar_linear_layout;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.emptyView)
    ImageView emptyView;

    @BindView(R.id.last_sync_date_tv)
    TextView last_sync_date_tv;

    @BindView(R.id.tv_staff_id)
    TextView tv_staff_id;

    SetPortfolioMethods setPortfolioMethods;

    AppDatabase appDatabase;

    FertilizerHomeRecyclerViewAdapter fertilizerHomeRecyclerViewAdapter;

    FertilizerHomePageListModelClass fertilizerHomePageListModelClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_sign_up_home);
        ButterKnife.bind(FertilizerSignUpHome.this);
        setSupportActionBar(toolbar);
        appDatabase = AppDatabase.getInstance(FertilizerSignUpHome.this);
        setPortfolioMethods = new SetPortfolioMethods();
        showView(toolbar_linear_layout);
        hideView(search_linear_layout);
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(FertilizerSignUpHome.this));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        fertilizerHomePageListModelClass = new ViewModelProvider(this, new MyViewModelFactory(appDatabase.membersDao(), this)).get(FertilizerHomePageListModelClass.class);
        fertilizerHomePageListModelClass.filterTextAll.setValue("");

        setAdapter();
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,FertilizerSignUpHome.this);
    }

    @OnClick(R.id.et_search)
    public void move_to_search(View view){
        hideView(toolbar_linear_layout);
        showView(search_linear_layout);
        search_edit_text.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).showSoftInput(search_edit_text, InputMethodManager.SHOW_IMPLICIT);
    }

    @OnClick(R.id.back_to_toolbar)
    public void move_away_from_search(View view){
        removeSearchTray();
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
            return (T) new FertilizerHomePageListModelClass(application, context);
        }
    }

    void removeSearchTray(){
        showView(toolbar_linear_layout);
        hideView(search_linear_layout);
        search_edit_text.setText(null);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(et_search.getWindowToken(), 0);
    }

    private void setAdapter() {
        fertilizerHomeRecyclerViewAdapter = new FertilizerHomeRecyclerViewAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
        recycler_view.addItemDecoration(verticalSpaceItemDecoration);
        recycler_view.setLayoutManager(layoutManager);

        fertilizerHomePageListModelClass.memberRecyclerModelList.observe(this,memberRecyclerModelList -> {


            updateView(memberRecyclerModelList.size());
            fertilizerHomeRecyclerViewAdapter.submitList(memberRecyclerModelList);

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
        recycler_view.setAdapter(fertilizerHomeRecyclerViewAdapter);

        textWatcher(search_edit_text,fertilizerHomePageListModelClass);

    }

    public void textWatcher(EditText editText, FertilizerHomePageListModelClass fertilizerHomePageListModelClass) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).isEmpty()){
                    fertilizerHomePageListModelClass.filterTextAll.setValue("%%");
                }else{
                    fertilizerHomePageListModelClass.filterTextAll.setValue("%" + s + "%");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void loadPreviousActivity() {
        startActivity(new Intent(FertilizerSignUpHome.this, ThreshingActivity.class));
    }

    @Override
    public void onBackPressed(){
        if (search_linear_layout.getVisibility() == View.VISIBLE){
            removeSearchTray();
        }else{
            loadPreviousActivity();
        }
    }
}
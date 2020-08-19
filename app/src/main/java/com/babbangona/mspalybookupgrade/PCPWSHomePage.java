package com.babbangona.mspalybookupgrade;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.PCPWSHomePageRecycler.PCPWSPageListModelClass;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PCPWSHomePageRecycler.PCPWSPendingAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PCPWSHomePageRecycler.PCPWSReviewedAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.PCPWSActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.Main2ActivityMethods;
import com.google.android.material.textview.MaterialTextView;


import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PCPWSHomePage extends AppCompatActivity {

    @BindView(R.id.toolbar_portfolio)
    Toolbar toolbar_portfolio;

    @BindView(R.id.et_search)
    TextView et_search;

    @BindView(R.id.pending_container)
    LinearLayout pending_container;

    @BindView(R.id.reviewed_container)
    LinearLayout reviewed_container;

    @BindView(R.id.tv_pending)
    MaterialTextView tv_pending;

    @BindView(R.id.tv_pending_count)
    MaterialTextView tv_pending_count;

    @BindView(R.id.tv_reviewed)
    MaterialTextView tv_reviewed;

    @BindView(R.id.tv_reviewed_count)
    MaterialTextView tv_reviewed_count;

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

    PCPWSPageListModelClass pcpwsPageListModelClass;

    PCPWSPendingAdapter pcpwsPendingAdapter;

    PCPWSReviewedAdapter pcpwsReviewedAdapter;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    String staff_id;

    int pendingCount, reviewedCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pc_pws_homepage);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_portfolio);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.pws_title));
        appDatabase = AppDatabase.getInstance(PCPWSHomePage.this);
        sharedPrefs = new SharedPrefs(PCPWSHomePage.this);
        showView(toolbar_linear_layout);
        hideView(search_linear_layout);

        toolbar_portfolio.setNavigationOnClickListener(view -> loadPreviousActivity());

        pcpwsPageListModelClass = new ViewModelProvider(this, new MyViewModelFactory(appDatabase.pcpwsActivitiesFlagDao(), this)).get(PCPWSPageListModelClass.class);
        pcpwsPageListModelClass.filterTextAll.setValue("");

        pending_container.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv_pending.setTextColor(getResources().getColor(R.color.text_color_white));
        int offset;
        if (sharedPrefs.getKeyAdapterOffset1() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffset1(1);
        }else{
            offset = 0;
        }
        setAdapter(offset);

        staff_id = "%"+sharedPrefs.getKeyPcPwsHomeStaffId()+"%";

        pendingCount = appDatabase.pcpwsActivitiesFlagDao().getPendingClaimsCount(staff_id);
        reviewedCount = appDatabase.pcpwsActivitiesFlagDao().getReviewedClaimsCount(staff_id);

        tv_pending_count.setText(String.valueOf(pendingCount));
        tv_reviewed_count.setText(String.valueOf(reviewedCount));

        Main2ActivityMethods main2ActivityMethods = new Main2ActivityMethods(PCPWSHomePage.this);
        main2ActivityMethods.confirmPhoneDate();
        main2ActivityMethods.confirmLocationOpen();
    }

    void setAllStaffAdapter(){
        pcpwsPageListModelClass = new ViewModelProvider(this, new MyViewModelFactory(appDatabase.pcpwsActivitiesFlagDao(), this)).get(PCPWSPageListModelClass.class);
        pcpwsPageListModelClass.filterTextAll.setValue("");
        int offset;
        if (sharedPrefs.getKeyAdapterOffset1() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffset1(1);
        }else{
            offset = 0;
        }
        setAdapter(offset);
    }

    void setAddedAdapter(){
        pcpwsPageListModelClass = new ViewModelProvider(this, new MyViewModelFactory(appDatabase.pcpwsActivitiesFlagDao(), this)).get(PCPWSPageListModelClass.class);
        pcpwsPageListModelClass.filterTextAllAnother.setValue("");
        int offset;
        if (sharedPrefs.getKeyAdapterOffset() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffset(1);
        }else{
            offset = 0;
        }
        setReviewedAdapterRecycler(offset);
    }

    @OnClick(R.id.reviewed_container)
    public void setReviewed_container(View view){
        reviewed_container.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv_reviewed.setTextColor(getResources().getColor(R.color.text_color_white));

        pending_container.setBackgroundColor(getResources().getColor(R.color.text_color_white));
        tv_pending.setTextColor(getResources().getColor(R.color.text_color_black));
        try{
            setAddedAdapter();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.pending_container)
    public void setPending_container(View view){

        pending_container.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv_pending.setTextColor(getResources().getColor(R.color.text_color_white));

        reviewed_container.setBackgroundColor(getResources().getColor(R.color.text_color_white));
        tv_reviewed.setTextColor(getResources().getColor(R.color.text_color_black));
        try{
            setAllStaffAdapter();
        }catch (Exception e){
            e.printStackTrace();
        }
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

    void removeSearchTray(){
        showView(toolbar_linear_layout);
        hideView(search_linear_layout);
        search_edit_text.setText(null);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(et_search.getWindowToken(), 0);
    }

    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public void loadPreviousActivity() {
        finish();
        startActivity(new Intent(PCPWSHomePage.this, PCStaffPSWPage.class));
    }

    public void textWatcher(EditText editText, PCPWSPageListModelClass pcpwsPageListModelClass) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).isEmpty()){
                    pcpwsPageListModelClass.filterTextAll.setValue("%%");
                }else{
                    pcpwsPageListModelClass.filterTextAll.setValue("%" + s + "%");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void textWatcherAdded(EditText editText, PCPWSPageListModelClass pcpwsPageListModelClass) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).isEmpty()){
                    pcpwsPageListModelClass.filterTextAllAnother.setValue("%%");
                }else{
                    pcpwsPageListModelClass.filterTextAllAnother.setValue("%" + s + "%");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setAdapter(int offset) {
        pcpwsPendingAdapter = new PCPWSPendingAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (offset == 1){
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
            VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
            recycler_view.addItemDecoration(verticalSpaceItemDecoration);
        }
        recycler_view.setLayoutManager(layoutManager);

        pcpwsPageListModelClass.pcpwsRecyclerModelList.observe(this,pcpwsRecyclerModelList -> {


            updateView(pcpwsRecyclerModelList.size());
            pcpwsPendingAdapter.submitList(pcpwsRecyclerModelList);

            pcpwsRecyclerModelList.addWeakCallback(null, new PagedList.Callback() {
                @Override
                public void onChanged(int position, int count) {
                    updateView(pcpwsRecyclerModelList.size());
                }

                @Override
                public void onInserted(int position, int count) {

                }

                @Override
                public void onRemoved(int position, int count) {

                }
            });
        });
        recycler_view.setAdapter(pcpwsPendingAdapter);

        textWatcher(search_edit_text,pcpwsPageListModelClass);

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

    private void setReviewedAdapterRecycler(int offset) {
        pcpwsReviewedAdapter = new PCPWSReviewedAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (offset == 1){
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
            VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
            recycler_view.addItemDecoration(verticalSpaceItemDecoration);
        }
        recycler_view.setLayoutManager(layoutManager);
        //portfolioPageListModelClass.setPortfolioRecyclerModelList1.observe(this,setAddedPortfolioAdapter::submitList);
        pcpwsPageListModelClass.pcpwsRecyclerModelList1.observe(this,pcpwsRecyclerModelList1 -> {


            updateView(pcpwsRecyclerModelList1.size());
            pcpwsReviewedAdapter.submitList(pcpwsRecyclerModelList1);

            pcpwsRecyclerModelList1.addWeakCallback(null, new PagedList.Callback() {
                @Override
                public void onChanged(int position, int count) {
                    updateView(pcpwsRecyclerModelList1.size());
                }

                @Override
                public void onInserted(int position, int count) {

                }

                @Override
                public void onRemoved(int position, int count) {

                }
            });
        });
        recycler_view.setAdapter(pcpwsReviewedAdapter);

        textWatcherAdded(search_edit_text,pcpwsPageListModelClass);

    }

    public static class MyViewModelFactory implements ViewModelProvider.Factory {
        private PCPWSActivitiesFlagDao application;
        private  Context context;


        MyViewModelFactory(PCPWSActivitiesFlagDao application, Context context) {
            this.application = application;
            this.context = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PCPWSPageListModelClass(application, context);
        }
    }

    @Override
    public void onBackPressed() {
        if (search_linear_layout.getVisibility() == View.VISIBLE){
            removeSearchTray();
        }else{
            loadPreviousActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        pendingCount = appDatabase.pcpwsActivitiesFlagDao().getPendingClaimsCount(staff_id);
        reviewedCount = appDatabase.pcpwsActivitiesFlagDao().getReviewedClaimsCount(staff_id);

        tv_pending_count.setText(String.valueOf(pendingCount));
        tv_reviewed_count.setText(String.valueOf(reviewedCount));
    }

    public void myMethod(){
        runOnUiThread(() -> pcpwsPendingAdapter.notifyDataSetChanged());
        pendingCount = appDatabase.pcpwsActivitiesFlagDao().getPendingClaimsCount(staff_id);
        reviewedCount = appDatabase.pcpwsActivitiesFlagDao().getReviewedClaimsCount(staff_id);

        tv_pending_count.setText(String.valueOf(pendingCount));
        tv_reviewed_count.setText(String.valueOf(reviewedCount));
    }
}

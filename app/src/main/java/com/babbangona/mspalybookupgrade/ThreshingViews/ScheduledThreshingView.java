package com.babbangona.mspalybookupgrade.ThreshingViews;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PCPWSHomePageRecycler.PCPWSPageListModelClass;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler.ViewScheduleRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler.ViewSchedulesAdapter;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.Main2ActivityMethods;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduledThreshingView extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_search)
    TextView et_search;

    /*@BindView(R.id.pending_container)
    LinearLayout pending_container;
*/
  /*  @BindView(R.id.reviewed_container)
    LinearLayout reviewed_container;*/

/*    @BindView(R.id.tv_pending)
    MaterialTextView tv_pending;

    @BindView(R.id.tv_pending_count)
    MaterialTextView tv_pending_count;

    @BindView(R.id.tv_reviewed)
    MaterialTextView tv_reviewed;

    @BindView(R.id.tv_reviewed_count)
    MaterialTextView tv_reviewed_count;*/

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

    @BindView(R.id.rbAllSchedules)
    RadioButton rbAllSchedules;

    @BindView(R.id.rbUrgentSchedules)
    RadioButton rbUrgentSchedules;


    @BindView(R.id.radioGroup1)
    RadioGroup RadioGroup;

/*  PCPWSPageListModelClass pcpwsPageListModelClass;

    PCPWSPendingAdapter pcpwsPendingAdapter;

    PCPWSReviewedAdapter pcpwsReviewedAdapter;*/

    List<ViewScheduleRecyclerModel> viewScheduleRecyclerModel;

    ViewSchedulesAdapter viewSchedulesAdapter;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    SetPortfolioMethods setPortfolioMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Bayo Implement urgent schedule and all schedule here

        setContentView(R.layout.activity_scheduled_list_top);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setPortfolioMethods = new SetPortfolioMethods();
        appDatabase = AppDatabase.getInstance(ScheduledThreshingView.this);
        sharedPrefs = new SharedPrefs(ScheduledThreshingView.this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(ScheduledThreshingView.this));
        showView(toolbar_linear_layout);
        hideView(search_linear_layout);

        recycler_view.setNestedScrollingEnabled(false);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);

        toolbar.setNavigationOnClickListener(view -> loadPreviousActivity());

        setPortfolioMethods.setFooter(last_sync_date_tv, tv_staff_id, ScheduledThreshingView.this);

        /*pcpwsPageListModelClass = new ViewModelProvider(this, new MyViewModelFactory(appDatabase.pcpwsActivitiesFlagDao(), this)).get(PCPWSPageListModelClass.class);
        pcpwsPageListModelClass.filterTextAll.setValue("");*/

        // pending_container.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // tv_pending.setTextColor(getResources().getColor(R.color.text_color_white));
        /*int offset;
        if (sharedPrefs.getKeyAdapterOffset1() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffset1(1);
        }else{
            offset = 0;
        }*/

        //rbAllSchedules.isChecked();
        //setAdapter(offset);

        setAllSchedules();
        FilterRecycler(search_edit_text, viewSchedulesAdapter);
        rbAllSchedules.setChecked(true);
        rbUrgentSchedules.setChecked(false);

        Main2ActivityMethods main2ActivityMethods = new Main2ActivityMethods(ScheduledThreshingView.this);
        main2ActivityMethods.confirmPhoneDate();
        main2ActivityMethods.confirmLocationOpen();

        RadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbAllSchedules) {
                //do your stuff.
                setAllSchedules();
                FilterRecycler(search_edit_text, viewSchedulesAdapter);
            } else if (checkedId == R.id.rbUrgentSchedules) {
                //do your stuff.
                setRbUrgentSchedules();
                FilterRecycler(search_edit_text, viewSchedulesAdapter);
            } else {
                //Do nothing
            }
        });
    }

    void FilterRecycler(EditText edt, ViewSchedulesAdapter adapter){
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null){
                    adapter.getFilter().filter(s);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /*    @OnCheckedChanged(R.id.rbAllSchedules)
    void allSchedules(CompoundButton button, boolean checked) {

    }


    @OnCheckedChanged(R.id.rbUrgentSchedules)
    void UrgentSchedules(CompoundButton button, boolean checked) {

    }*/

    void setAllSchedules() {

        viewScheduleRecyclerModel = new ArrayList<>();
        viewScheduleRecyclerModel = AppDatabase.getInstance(this).scheduleThreshingActivitiesFlagDao().viewAllScheduledFields(sharedPrefs.getStaffID());

        if (viewScheduleRecyclerModel.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);

            viewSchedulesAdapter = new ViewSchedulesAdapter(viewScheduleRecyclerModel, getApplicationContext(), emptyView, recycler_view);
            recycler_view.setAdapter(viewSchedulesAdapter);
        }

        /*    int offset;
        if (sharedPrefs.getKeyAdapterOffset1() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffset1(1);
        }else{
            offset = 0;
        }*/
        //setAdapter(offset);
    }

    void setRbUrgentSchedules() {
        viewScheduleRecyclerModel = new ArrayList<>();
        viewScheduleRecyclerModel = appDatabase.scheduleThreshingActivitiesFlagDao().viewAllUrgentScheduledFields(sharedPrefs.getStaffID());

        if (viewScheduleRecyclerModel.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recycler_view.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);

            viewSchedulesAdapter = new ViewSchedulesAdapter(viewScheduleRecyclerModel, getApplicationContext(), emptyView, recycler_view);
            recycler_view.setAdapter(viewSchedulesAdapter);
        }

        /*        int offset;
        if (sharedPrefs.getKeyAdapterOffset() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffset(1);
        }else{
            offset = 0;
        }*/
        //setReviewedAdapterRecycler(offset);
    }

    /*
    @OnClick(R.id.reviewed_container)
    public void setReviewed_container(View view){
        reviewed_container.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv_reviewed.setTextColor(getResources().getColor(R.color.text_color_white));

       // pending_container.setBackgroundColor(getResources().getColor(R.color.text_color_white));
        tv_pending.setTextColor(getResources().getColor(R.color.text_color_black));
        try{
            setAddedAdapter();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
*/

    /*
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
*/

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
        startActivity(new Intent(ScheduledThreshingView.this, ThreshingActivity.class));
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
    /*

    private void setAdapter(int offset) {
        viewSchedulesAdapter = new ViewSchedulesAdapter(this);
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

*/

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

    /*   private void setReviewedAdapterRecycler(int offset) {
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
    */
    /*  public static class MyViewModelFactory implements ViewModelProvider.Factory {
            private ScheduleThreshingActivitiesFlagDao application;
            private  Context context;


            MyViewModelFactory(ScheduleThreshingActivitiesFlagDao application, Context context) {
                this.application = application;
                this.context = context;
            }

            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                return (T) new PCPWSPageListModelClass(application, context);
            }
        }
    */

    @Override
    public void onBackPressed() {
        if (search_linear_layout.getVisibility() == View.VISIBLE) {
            removeSearchTray();
        } else {
            loadPreviousActivity();
        }
    }
}

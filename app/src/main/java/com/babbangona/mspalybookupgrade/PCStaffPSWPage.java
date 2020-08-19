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

import com.babbangona.mspalybookupgrade.RecyclerAdapters.PCStaffPSWPageRecycler.PCStaffPSWPageListModelClass;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PCStaffPSWPageRecycler.PCStaffPWSAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.PWSActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.babbangona.mspalybookupgrade.utils.Main2ActivityMethods;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PCStaffPSWPage extends AppCompatActivity {

    @BindView(R.id.et_search)
    TextView et_search;

    @BindView(R.id.search_edit_text)
    EditText search_edit_text;

    @BindView(R.id.toolbar_field_list)
    Toolbar toolbar_field_list;

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

    PCStaffPSWPageListModelClass pcStaffPSWPageListModelClass;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    PCStaffPWSAdapter pcStaffPWSAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc_staff_psw_page);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_field_list);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.pws_title));
        appDatabase = AppDatabase.getInstance(PCStaffPSWPage.this);
        sharedPrefs = new SharedPrefs(PCStaffPSWPage.this);
        GPSController.initialiseLocationListener(PCStaffPSWPage.this);
        showView(toolbar_linear_layout);
        hideView(search_linear_layout);
        loadAdapter();
        setAdapter();
        toolbar_field_list.setNavigationOnClickListener(v -> startPreviousActivity());
        Main2ActivityMethods main2ActivityMethods = new Main2ActivityMethods(PCStaffPSWPage.this);
        main2ActivityMethods.confirmPhoneDate();
        main2ActivityMethods.confirmLocationOpen();
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

    void loadAdapter(){
        pcStaffPSWPageListModelClass = new ViewModelProvider(this, new MyViewModelFactory(appDatabase.pwsActivitiesFlagDao(), this))
                        .get(PCStaffPSWPageListModelClass.class);
        pcStaffPSWPageListModelClass.filterTextAll.setValue("");//from all

    }

    public static class MyViewModelFactory implements ViewModelProvider.Factory {
        private PWSActivitiesFlagDao application;
        private Context context;

        MyViewModelFactory(PWSActivitiesFlagDao application, Context context) {
            this.application = application;
            this.context = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PCStaffPSWPageListModelClass(application, context);
        }
    }

    private void setAdapter() {
        pcStaffPWSAdapter = new PCStaffPWSAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
        recycler_view.addItemDecoration(verticalSpaceItemDecoration);
        recycler_view.setLayoutManager(layoutManager);
        //fieldListPageListModelClass.fieldListRecyclerModel.observe(this,fieldListRecyclerAdapter::submitList);

        pcStaffPSWPageListModelClass.pcStaffPWSRecyclerModel.observe(this,pcStaffPWSRecyclerModel -> {


            updateView(pcStaffPWSRecyclerModel.size());
            pcStaffPWSAdapter.submitList(pcStaffPWSRecyclerModel);

            pcStaffPWSRecyclerModel.addWeakCallback(null, new PagedList.Callback() {
                @Override
                public void onChanged(int position, int count) {
                    updateView(pcStaffPWSRecyclerModel.size());
                }

                @Override
                public void onInserted(int position, int count) {

                }

                @Override
                public void onRemoved(int position, int count) {

                }
            });
        });
        recycler_view.setAdapter(pcStaffPWSAdapter);

        textWatcher(search_edit_text,pcStaffPSWPageListModelClass);

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

    public void textWatcher(EditText editText, PCStaffPSWPageListModelClass pcStaffPSWPageListModelClass) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).isEmpty()){
                    pcStaffPSWPageListModelClass.filterTextAll.setValue("%%");
                }else{
                    pcStaffPSWPageListModelClass.filterTextAll.setValue("%" + s + "%");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (search_linear_layout.getVisibility() == View.VISIBLE){
            removeSearchTray();
        }else{
            startPreviousActivity();
        }
    }

    void startPreviousActivity(){
        finish();
        startActivity(new Intent(PCStaffPSWPage.this,Homepage.class));
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

    @Override
    protected void onResume() {
        pcStaffPWSAdapter.notifyDataSetChanged();
        super.onResume();
    }

    public void myMethod(){
        runOnUiThread(() -> pcStaffPWSAdapter.notifyDataSetChanged());
    }
}

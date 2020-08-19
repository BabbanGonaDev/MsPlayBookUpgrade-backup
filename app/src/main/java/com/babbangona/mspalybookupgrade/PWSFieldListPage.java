package com.babbangona.mspalybookupgrade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler.HGFieldListPageListModelClass;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler.HGFieldListRecyclerAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListPageListModelClass;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListRecyclerAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.FieldsDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.babbangona.mspalybookupgrade.utils.Main2ActivityMethods;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PWSFieldListPage extends AppCompatActivity {

    @BindView(R.id.toolbar_field_list)
    Toolbar toolbar_field_list;

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

    PWSFieldListPageListModelClass pwsFieldListPageListModelClass;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    PWSFieldListRecyclerAdapter pwsFieldListRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_list_page);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_field_list);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.pws_field_list_title));
        appDatabase = AppDatabase.getInstance(PWSFieldListPage.this);
        sharedPrefs = new SharedPrefs(PWSFieldListPage.this);
        GPSController.initialiseLocationListener(PWSFieldListPage.this);
        showView(toolbar_linear_layout);
        hideView(search_linear_layout);
        loadAdapter(sharedPrefs.getKeyRouteType());
        int offset;
        if (sharedPrefs.getKeyAdapterOffsetHgFieldList() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffsetHgFieldList(1);
        }else{
            offset = 0;
        }
        setAdapter(offset);
        toolbar_field_list.setNavigationOnClickListener(v -> startPreviousActivity());
        Main2ActivityMethods main2ActivityMethods = new Main2ActivityMethods(PWSFieldListPage.this);
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

    @Override
    protected void onResume() {
        pwsFieldListRecyclerAdapter.notifyDataSetChanged();
        super.onResume();
    }

    void loadAdapter(String route){
        Log.d("hello_there:-",route);
        String min_lat = sharedPrefs.getKeyGridMinLat();
        String max_lat = sharedPrefs.getKeyGridMaxLat();
        String min_lng = sharedPrefs.getKeyGridMinLng();
        String max_lng = sharedPrefs.getKeyGridMaxLng();
        String unique_field_id = "%"+sharedPrefs.getKeySearchUniqueFieldId()+"%";
        String ik_number = "%"+sharedPrefs.getKeySearchIkNumber()+"%";
        String member_id = "%"+sharedPrefs.getKeySearchMemberId()+"%";
        String member_name = "%"+sharedPrefs.getKeySearchMemberName()+"%";
        String village = "%"+sharedPrefs.getKeySearchVillage()+"%";
        switch (route){
            case "1":
                pwsFieldListPageListModelClass = new ViewModelProvider(this,
                        new MyViewModelFactory2(appDatabase.fieldsDao(), this,
                                Double.parseDouble(min_lat),Double.parseDouble(max_lat),
                                Double.parseDouble(min_lng),Double.parseDouble(max_lng)))
                        .get(PWSFieldListPageListModelClass.class);
                pwsFieldListPageListModelClass.filterTextAll.setValue("");//from grid
                break;
            case "2":
                pwsFieldListPageListModelClass = new ViewModelProvider(this,
                        new MyViewModelFactory1(appDatabase.fieldsDao(), this,unique_field_id,
                                ik_number,member_id,member_name,village))
                        .get(PWSFieldListPageListModelClass.class);
                pwsFieldListPageListModelClass.filterTextAll.setValue("");//from search
                break;
            case "3":
                pwsFieldListPageListModelClass = new ViewModelProvider(this,
                        new MyViewModelFactory(appDatabase.fieldsDao(), this))
                        .get(PWSFieldListPageListModelClass.class);
                pwsFieldListPageListModelClass.filterTextAll.setValue("");//from all
                break;
        }
    }

    public static class MyViewModelFactory implements ViewModelProvider.Factory {
        private FieldsDao application;
        private Context context;

        MyViewModelFactory(FieldsDao application, Context context) {
            this.application = application;
            this.context = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PWSFieldListPageListModelClass(application, context);
        }
    }

    public static class MyViewModelFactory1 implements ViewModelProvider.Factory {
        private FieldsDao application;
        private Context context;
        private String unique_field_id;
        private String ik_number;
        private String member_id;
        private String member_name;
        private String village;


        MyViewModelFactory1(FieldsDao application, Context context, String unique_field_id,
                            String ik_number, String member_id, String member_name, String village) {
            this.application = application;
            this.context = context;
            this.unique_field_id = unique_field_id;
            this.ik_number = ik_number;
            this.member_id = member_id;
            this.member_name = member_name;
            this.village = village;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PWSFieldListPageListModelClass(application, context,unique_field_id,ik_number,member_id,member_name,village);
        }
    }

    public static class MyViewModelFactory2 implements ViewModelProvider.Factory {
        private FieldsDao application;
        private Context context;
        double min_lat;
        double max_lat;
        double min_lng;
        double max_lng;

        MyViewModelFactory2(FieldsDao application, Context context, double min_lat,
                            double max_lat, double min_lng, double max_lng) {
            this.application = application;
            this.context = context;
            this.min_lat = min_lat;
            this.max_lat = max_lat;
            this.min_lng = min_lng;
            this.max_lng = max_lng;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PWSFieldListPageListModelClass(application, context,min_lat,max_lat,min_lng,max_lng);
        }
    }

    private void setAdapter(int offset) {
        pwsFieldListRecyclerAdapter = new PWSFieldListRecyclerAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
        recycler_view.addItemDecoration(verticalSpaceItemDecoration);
        recycler_view.setLayoutManager(layoutManager);
//        hgFieldListPageListModelClass.hgFieldListRecyclerModel.observe(this,hgFieldListRecyclerAdapter::submitList);
        pwsFieldListPageListModelClass.pwsFieldListRecyclerModel.observe(this,pwsFieldListRecyclerModel -> {


            updateView(pwsFieldListRecyclerModel.size());
            pwsFieldListRecyclerAdapter.submitList(pwsFieldListRecyclerModel);

            pwsFieldListRecyclerModel.addWeakCallback(null, new PagedList.Callback() {
                @Override
                public void onChanged(int position, int count) {
                    updateView(pwsFieldListRecyclerModel.size());
                }

                @Override
                public void onInserted(int position, int count) {

                }

                @Override
                public void onRemoved(int position, int count) {

                }
            });
        });
        recycler_view.setAdapter(pwsFieldListRecyclerAdapter);

        textWatcher(search_edit_text,pwsFieldListPageListModelClass);

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

    public void textWatcher(EditText editText, PWSFieldListPageListModelClass pwsFieldListPageListModelClass) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).isEmpty()){
                    pwsFieldListPageListModelClass.filterTextAll.setValue("%%");
                }else{
                    pwsFieldListPageListModelClass.filterTextAll.setValue("%" + s + "%");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            pwsFieldListRecyclerAdapter.notifyDataSetChanged();
        }else {
            if (search_linear_layout.getVisibility() == View.VISIBLE){
                removeSearchTray();
            }else{
                startPreviousActivity();
            }
        }

    }

    void startPreviousActivity(){
        finish();
        startActivity(new Intent(PWSFieldListPage.this,GridDetails.class));
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

    public void myMethod(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pwsFieldListRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
}

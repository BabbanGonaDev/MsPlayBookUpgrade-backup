package com.babbangona.mspalybookupgrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListPageListModelClass;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.FieldsDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FieldListPage extends AppCompatActivity {

    @BindView(R.id.toolbar_field_list)
    Toolbar toolbar_field_list;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    FieldListPageListModelClass fieldListPageListModelClass;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    FieldListRecyclerAdapter fieldListRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_list_page);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_field_list);
        appDatabase = AppDatabase.getInstance(FieldListPage.this);
        sharedPrefs = new SharedPrefs(FieldListPage.this);
        loadAdapter(sharedPrefs.getKeyRouteType());
        int offset;
        if (sharedPrefs.getKeyAdapterOffsetFieldList() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffsetFieldList(1);
        }else{
            offset = 0;
        }
        setAdapter(offset);
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
                fieldListPageListModelClass = new ViewModelProvider(this,
                        new MyViewModelFactory2(appDatabase.fieldsDao(), this,
                                Double.parseDouble(min_lat),Double.parseDouble(max_lat),
                                Double.parseDouble(min_lng),Double.parseDouble(max_lng)))
                        .get(FieldListPageListModelClass.class);
                fieldListPageListModelClass.filterTextAll.setValue("");
                break;
            case "2":
                fieldListPageListModelClass = new ViewModelProvider(this,
                        new MyViewModelFactory1(appDatabase.fieldsDao(), this,unique_field_id,
                                ik_number,member_id,member_name,village))
                        .get(FieldListPageListModelClass.class);
                fieldListPageListModelClass.filterTextAll.setValue("");
                break;
            case "3":
                fieldListPageListModelClass = new ViewModelProvider(this,
                        new MyViewModelFactory(appDatabase.fieldsDao(), this))
                        .get(FieldListPageListModelClass.class);
                fieldListPageListModelClass.filterTextAll.setValue("");
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
            return (T) new FieldListPageListModelClass(application, context);
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
            return (T) new FieldListPageListModelClass(application, context,unique_field_id,ik_number,member_id,member_name,village);
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
            return (T) new FieldListPageListModelClass(application, context,min_lat,max_lat,min_lng,max_lng);
        }
    }

    private void setAdapter(int offset) {
        fieldListRecyclerAdapter = new FieldListRecyclerAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
        recycler_view.addItemDecoration(verticalSpaceItemDecoration);
        recycler_view.setLayoutManager(layoutManager);
        fieldListPageListModelClass.fieldListRecyclerModel.observe(this,fieldListRecyclerAdapter::submitList);
        recycler_view.setAdapter(fieldListRecyclerAdapter);

//        textWatcher(search_edit_text,fieldListPageListModelClass);

    }

    public void textWatcher(EditText editText, FieldListPageListModelClass fieldListPageListModelClass) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).isEmpty()){
                    fieldListPageListModelClass.filterTextAll.setValue("%%");
                }else{
                    fieldListPageListModelClass.filterTextAll.setValue("%" + s + "%");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}

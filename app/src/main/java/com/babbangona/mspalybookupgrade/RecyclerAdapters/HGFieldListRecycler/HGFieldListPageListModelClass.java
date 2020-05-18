package com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.daos.FieldsDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

public class HGFieldListPageListModelClass extends ViewModel {

    private FieldsDao fieldsDao;
    private Context context;
    private String unique_field_id;
    private String ik_number;
    private String member_id;
    private String member_name;
    private String village;
    private double min_lat;
    private double max_lat;
    private double min_lng;
    private double max_lng;

    public final LiveData<PagedList<HGFieldListRecyclerModel>> hgFieldListRecyclerModel;
    public MutableLiveData<String> filterTextAll       = new MutableLiveData<>();

    public HGFieldListPageListModelClass(FieldsDao fieldsDao, Context context) {
        this.fieldsDao = fieldsDao;
        this.context = context;
        SharedPrefs sharedPrefs = new SharedPrefs(this.context);
        String staff_id = "%"+sharedPrefs.getStaffID()+"%";

        hgFieldListRecyclerModel = Transformations.switchMap(filterTextAll, input -> {
            if (input == null || input.equals("") || input.equals("%%") ||input.equals("% %")) {
                //check if the current value is empty load all data else search
                Log.i("PagedList", "ReportedModelClass: inside not blank just staff id");
                return new LivePagedListBuilder<>(
                        this.fieldsDao.getFieldListByStaffIDHG(staff_id),5)
                        .build();
            } else {
                Log.i("PagedList", "ReportedModelClass: inside blank just staff ID");
                return new LivePagedListBuilder<>(
                        this.fieldsDao.getFieldListBySearchHG(staff_id,filterTextAll.getValue()),5)
                        .build();
            }

        });
    }

    public HGFieldListPageListModelClass(FieldsDao fieldsDao, Context context, String unique_field_id,
                                         String ik_number, String member_id, String member_name,
                                         String village) {
        this.fieldsDao = fieldsDao;
        this.context = context;
        this.unique_field_id = unique_field_id;
        this.ik_number = ik_number;
        this.member_id = member_id;
        this.member_name = member_name;
        this.village = village;

        SharedPrefs sharedPrefs = new SharedPrefs(this.context);
        String staff_id = "%"+sharedPrefs.getStaffID()+"%";

        hgFieldListRecyclerModel = Transformations.switchMap(filterTextAll, input -> {
            if (input == null || input.equals("") || input.equals("%%") ||input.equals("% %")) {
                //check if the current value is empty load all data else search
                Log.i("PagedList", "ReportedModelClass: inside not blank search");
                return new LivePagedListBuilder<>(
                        this.fieldsDao.getFieldListByActivitySearchHG(staff_id,
                                this.unique_field_id,this.ik_number,this.member_id,this.member_name,this.village),5)
                        .build();
            } else {
                Log.i("PagedList", "ReportedModelClass: inside blank search ");
                return new LivePagedListBuilder<>(
                        this.fieldsDao.getFieldListByActivitySearchHG(staff_id,
                                this.unique_field_id,this.ik_number,this.member_id,this.member_name,this.village,filterTextAll.getValue()),5)
                        .build();
            }

        });
    }

    public HGFieldListPageListModelClass(FieldsDao fieldsDao, Context context, double min_lat,
                                         double max_lat, double min_lng, double max_lng) {
        this.fieldsDao = fieldsDao;
        this.context = context;
        this.min_lat = min_lat;
        this.max_lat = max_lat;
        this.min_lng = min_lng;
        this.max_lng = max_lng;


        SharedPrefs sharedPrefs = new SharedPrefs(this.context);
        String staff_id = "%"+sharedPrefs.getStaffID()+"%";

        hgFieldListRecyclerModel = Transformations.switchMap(filterTextAll, input -> {
            if (input == null || input.equals("") || input.equals("%%") ||input.equals("% %")) {
                //check if the current value is empty load all data else search
                Log.i("PagedList", "ReportedModelClass: not inside grid ");

                Log.d("Locations_search",
                        min_lat+"|"+
                                max_lat+"|"+
                                min_lng+"|"+
                                max_lng+"|"
                );
                return new LivePagedListBuilder<>(
                        this.fieldsDao.getFieldListByGridHG(staff_id,this.min_lat,this.max_lat,this.min_lng,this.max_lng),5)
                        .build();
            } else {
                Log.i("PagedList", "ReportedModelClass: inside grid ");
                return new LivePagedListBuilder<>(
                        this.fieldsDao.getFieldListByGridHG(staff_id,this.min_lat,this.max_lat,this.min_lng,this.max_lng,filterTextAll.getValue()),5)
                        .build();
            }

        });
    }
}

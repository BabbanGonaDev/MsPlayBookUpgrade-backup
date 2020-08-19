package com.babbangona.mspalybookupgrade.RecyclerAdapters.PCStaffPSWPageRecycler;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.babbangona.mspalybookupgrade.data.db.daos.PWSActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

public class PCStaffPSWPageListModelClass extends ViewModel {
    private PWSActivitiesFlagDao pwsActivitiesFlagDao;
    private  Context context;

    public final LiveData<PagedList<PCStaffPWSRecyclerModel>> pcStaffPWSRecyclerModel;
    public MutableLiveData<String> filterTextAll       = new MutableLiveData<>();

    public PCStaffPSWPageListModelClass(PWSActivitiesFlagDao pwsActivitiesFlagDao, Context context) {
        this.pwsActivitiesFlagDao = pwsActivitiesFlagDao;
        this.context = context;
        SharedPrefs sharedPrefs = new SharedPrefs(this.context);
        String staff_id = "%"+sharedPrefs.getStaffID()+"%";

        pcStaffPWSRecyclerModel = Transformations.switchMap(filterTextAll, input -> {
            if (input == null || input.equals("") || input.equals("%%") ||input.equals("% %")) {
                //check if the current value is empty load all data else search
                Log.i("PagedList", "ReportedModelClass: inside not blank ");
                return new LivePagedListBuilder<>(
                        this.pwsActivitiesFlagDao.getPWSStaffList(staff_id),5)
                        .build();
            } else {
                Log.i("PagedList", "ReportedModelClass: inside blank ");
                return new LivePagedListBuilder<>(
                        this.pwsActivitiesFlagDao.getPWSStaffList(staff_id,filterTextAll.getValue()),5)
                        .build();
            }

        });
    }
}

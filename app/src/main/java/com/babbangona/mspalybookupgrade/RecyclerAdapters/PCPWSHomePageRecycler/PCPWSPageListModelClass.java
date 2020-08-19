package com.babbangona.mspalybookupgrade.RecyclerAdapters.PCPWSHomePageRecycler;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.babbangona.mspalybookupgrade.data.db.daos.PCPWSActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.daos.StaffListDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

public class PCPWSPageListModelClass extends ViewModel {
    private PCPWSActivitiesFlagDao pcpwsActivitiesFlagDao;
    private  Context context;

    public final LiveData<PagedList<PCPWSRecyclerModel>> pcpwsRecyclerModelList;
    public final LiveData<PagedList<PCPWSRecyclerModel>> pcpwsRecyclerModelList1;
    public MutableLiveData<String> filterTextAll       = new MutableLiveData<>();
    public MutableLiveData<String> filterTextAllAnother       = new MutableLiveData<>();

    public PCPWSPageListModelClass(PCPWSActivitiesFlagDao pcpwsActivitiesFlagDao, Context context) {
        this.pcpwsActivitiesFlagDao = pcpwsActivitiesFlagDao;
        this.context = context;
        SharedPrefs sharedPrefs = new SharedPrefs(this.context);
        String staff_id = "%"+sharedPrefs.getKeyPcPwsHomeStaffId()+"%";

        pcpwsRecyclerModelList = Transformations.switchMap(filterTextAll, input -> {
            if (input == null || input.equals("") || input.equals("%%") ||input.equals("% %")) {
                //check if the current value is empty load all data else search
                Log.i("PagedList", "ReportedModelClass: inside not blank ");
                return new LivePagedListBuilder<>(
                        this.pcpwsActivitiesFlagDao.getPendingClaims(staff_id),5)
                        .build();
            } else {
                Log.i("PagedList", "ReportedModelClass: inside blank ");
                return new LivePagedListBuilder<>(
                        this.pcpwsActivitiesFlagDao.getPendingClaims(staff_id,filterTextAll.getValue()),5)
                        .build();
            }

        });

        pcpwsRecyclerModelList1 = Transformations.switchMap(filterTextAllAnother, input -> {
            if (input == null || input.equals("") || input.equals("%%") ||input.equals("% %")) {
                //check if the current value is empty load all data else search
                Log.i("PagedListAdded", "ReportedModelClass: inside not blank ");
                return new LivePagedListBuilder<>(
                        this.pcpwsActivitiesFlagDao.getReviewedClaims(staff_id),5)
                        .build();
            } else {
                Log.i("PagedListAdded", "ReportedModelClass: inside blank ");
                return new LivePagedListBuilder<>(
                        this.pcpwsActivitiesFlagDao.getReviewedClaims(staff_id,filterTextAllAnother.getValue()),5)
                        .build();
            }

        });
    }
}

package com.babbangona.mspalybookupgrade.RecyclerAdapters.SetPortfolioRecycler;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.babbangona.mspalybookupgrade.data.db.daos.StaffListDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

public class PortfolioPageListModelClass extends ViewModel {
    private StaffListDao staffListDao;
    private  Context context;

    public final LiveData<PagedList<SetPortfolioRecyclerModel>> setPortfolioRecyclerModelList;
    public final LiveData<PagedList<SetPortfolioRecyclerModel>> setPortfolioRecyclerModelList1;
    public MutableLiveData<String> filterTextAll       = new MutableLiveData<>();
    public MutableLiveData<String> filterTextAllAnother       = new MutableLiveData<>();

    public PortfolioPageListModelClass(StaffListDao staffListDao, Context context) {
        this.staffListDao = staffListDao;
        this.context = context;
        SharedPrefs sharedPrefs = new SharedPrefs(this.context);

        String staff_id = "%"+sharedPrefs.getStaffID()+"%";

        setPortfolioRecyclerModelList = Transformations.switchMap(filterTextAll, input -> {
            if (input == null || input.equals("") || input.equals("%%") ||input.equals("% %")) {
                //check if the current value is empty load all data else search
                Log.i("PagedList", "ReportedModelClass: inside not blank ");
                return new LivePagedListBuilder<>(
                        this.staffListDao.getAllStaffNotAdded(),5)
                        .build();
            } else {
                Log.i("PagedList", "ReportedModelClass: inside blank ");
                return new LivePagedListBuilder<>(
                        this.staffListDao.getAllStaffNotAdded(filterTextAll.getValue()),5)
                        .build();
            }

        });

        setPortfolioRecyclerModelList1 = Transformations.switchMap(filterTextAllAnother, input -> {
            if (input == null || input.equals("") || input.equals("%%") ||input.equals("% %")) {
                //check if the current value is empty load all data else search
                Log.i("PagedListAdded", "ReportedModelClass: inside not blank ");
                return new LivePagedListBuilder<>(
                        this.staffListDao.getAllStaffAdded(staff_id),5)
                        .build();
            } else {
                Log.i("PagedListAdded", "ReportedModelClass: inside blank ");
                return new LivePagedListBuilder<>(
                        this.staffListDao.getAllStaffAdded(staff_id,filterTextAllAnother.getValue()),5)
                        .build();
            }

        });
    }
}

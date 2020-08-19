package com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFragmentRecycler;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.daos.PWSActivitiesFlagDao;

public class PWSClaimPageListModelClass extends ViewModel {
    private PWSActivitiesFlagDao pwsActivitiesFlagDao;
    private String unique_field_id;

    public final LiveData<PagedList<PWSFieldListRecyclerModel.PWSListModel>> pwsListModel;
    public MutableLiveData<String> filterTextAll       = new MutableLiveData<>();

    public PWSClaimPageListModelClass(PWSActivitiesFlagDao pwsActivitiesFlagDao, String unique_field_id) {
        this.pwsActivitiesFlagDao = pwsActivitiesFlagDao;
        this.unique_field_id = unique_field_id;

        pwsListModel = Transformations.switchMap(filterTextAll, input -> new LivePagedListBuilder<>(
                this.pwsActivitiesFlagDao.getAllActivePWS(unique_field_id),5)
                .build());
    }
}

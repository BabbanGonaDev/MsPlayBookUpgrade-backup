package com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpMembersRecycler;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.MembersDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

public class FertilizerMembersPageListModelClass extends ViewModel {
    private MembersDao membersDao;
    private  Context context;

    public final LiveData<PagedList<FertilizerMembersRecyclerModel>> memberRecyclerModelList;
    public MutableLiveData<String> filterTextAll       = new MutableLiveData<>();

    public FertilizerMembersPageListModelClass(MembersDao membersDao, Context context) {
        this.membersDao = membersDao;
        this.context = context;
        SharedPrefs sharedPrefs = new SharedPrefs(this.context);
        String ik_number = sharedPrefs.getKeyFertilizerSignUpIkNumber();

        memberRecyclerModelList = Transformations.switchMap(filterTextAll, input -> {
                //check if the current value is empty load all data else search
                Log.i("PagedList", "ReportedModelClass: inside not blank ");
                return new LivePagedListBuilder<>(
                        this.membersDao.getTrustGroupMemberList(ik_number),5)
                        .build();

        });
    }
}

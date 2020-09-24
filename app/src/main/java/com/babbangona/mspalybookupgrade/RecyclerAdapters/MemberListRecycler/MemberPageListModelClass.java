package com.babbangona.mspalybookupgrade.RecyclerAdapters.MemberListRecycler;

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

public class MemberPageListModelClass extends ViewModel {
    private MembersDao membersDao;
    private  Context context;

    public final LiveData<PagedList<MemberListRecyclerModel>> memberRecyclerModelList;
    public MutableLiveData<String> filterTextAll       = new MutableLiveData<>();

    public MemberPageListModelClass(MembersDao membersDao, Context context) {
        this.membersDao = membersDao;
        this.context = context;
        SharedPrefs sharedPrefs = new SharedPrefs(this.context);
        String coach_id = getCoach(sharedPrefs.getStaffID(),this.context);

        memberRecyclerModelList = Transformations.switchMap(filterTextAll, input -> {
            if (input == null || input.equals("") || input.equals("%%") ||input.equals("% %")) {
                //check if the current value is empty load all data else search
                Log.i("PagedList", "ReportedModelClass: inside not blank ");
                return new LivePagedListBuilder<>(
                        this.membersDao.getMemberListByCoach(coach_id),5)
                        .build();
            } else {
                Log.i("PagedList", "ReportedModelClass: inside blank ");
                return new LivePagedListBuilder<>(
                        this.membersDao.getMemberListBySearch(coach_id,filterTextAll.getValue()),5)
                        .build();
            }

        });
    }

    String getCoach(String staff_id, Context context){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        String coach_id;
        try {
            coach_id = appDatabase.bgtCoachesDao().getCoach(staff_id);
        } catch (Exception e) {
            e.printStackTrace();
            coach_id = "T-10000000000000AA";
        }
        if (coach_id == null || coach_id.equalsIgnoreCase("") ){
            coach_id = "T-10000000000000AA";
        }
        return coach_id;
    }
}

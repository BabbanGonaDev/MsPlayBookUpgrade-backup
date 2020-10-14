package com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpHomeRecycler;

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

public class FertilizerHomePageListModelClass extends ViewModel {
    private MembersDao membersDao;
    private  Context context;

    public final LiveData<PagedList<FertilizerHomeRecyclerModel>> memberRecyclerModelList;
    public MutableLiveData<String> filterTextAll       = new MutableLiveData<>();

    public FertilizerHomePageListModelClass(MembersDao membersDao, Context context) {
        this.membersDao = membersDao;
        this.context = context;
        SharedPrefs sharedPrefs = new SharedPrefs(this.context);
        String coach_id = sharedPrefs.getStaffID();

        memberRecyclerModelList = Transformations.switchMap(filterTextAll, input -> {
            if (input == null || input.equals("") || input.equals("%%") ||input.equals("% %")) {
                //check if the current value is empty load all data else search
                Log.i("PagedList", "ReportedModelClass: inside not blank ");
                return new LivePagedListBuilder<>(
                        this.membersDao.getLeaderMemberList(coach_id),5)
                        .build();
            } else {
                Log.i("PagedList", "ReportedModelClass: inside blank ");
                return new LivePagedListBuilder<>(
                        this.membersDao.getLeaderMemberListBySearch(coach_id,filterTextAll.getValue()),5)
                        .build();
            }

        });
    }

    String getCoach(String staff_id, Context context){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        SharedPrefs sharedPrefs = new SharedPrefs(context);
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
        if (sharedPrefs.getStaffRole().equalsIgnoreCase("BGT")){
            return coach_id;
        }else{
            return sharedPrefs.getStaffID();
        }
    }
}

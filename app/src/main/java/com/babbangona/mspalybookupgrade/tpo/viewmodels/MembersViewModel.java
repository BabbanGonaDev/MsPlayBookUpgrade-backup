package com.babbangona.mspalybookupgrade.tpo.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.babbangona.mspalybookupgrade.data.db.daos.MembersDao;
import com.babbangona.mspalybookupgrade.tpo.data.models.MemberModel;

public class MembersViewModel extends ViewModel {

    public MutableLiveData<String> searchText = new MutableLiveData<>();
    private LiveData<PagedList<MemberModel>> pagedMembersList;

    public MembersViewModel() {

    }

    public LiveData<PagedList<MemberModel>> getPagedMembersList(MembersDao dao) {
        createPagedMembers(dao);
        return pagedMembersList;
    }

    private void createPagedMembers(MembersDao dao) {

        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(100)
                .setPageSize(30)
                .build();

        pagedMembersList = Transformations.switchMap(searchText, input -> {
            if (input == null || input.equals("") || input.equals("%%")) {
                return new LivePagedListBuilder<>(dao.getAllTpoMembers(), config)
                        .build();
            } else {
                //Filter function happens here
                return new LivePagedListBuilder<>(dao.getAllTpoMembersFilter(input), config)
                        .build();
            }
        });
    }

    public void setSearchText(String value) {
        searchText.postValue(value);
    }
}

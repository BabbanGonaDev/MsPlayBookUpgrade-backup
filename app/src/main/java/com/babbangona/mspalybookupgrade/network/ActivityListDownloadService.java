package com.babbangona.mspalybookupgrade.network;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;
import com.babbangona.mspalybookupgrade.data.db.entities.Fields;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.HGList;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.network.object.ActivityListDownload;
import com.babbangona.mspalybookupgrade.network.object.HGActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.HGListDownload;
import com.babbangona.mspalybookupgrade.network.object.LogsUpload;
import com.babbangona.mspalybookupgrade.network.object.MsPlaybookInputDownload;
import com.babbangona.mspalybookupgrade.network.object.NormalActivitiesUpload;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class ActivityListDownloadService extends IntentService {

    RetrofitInterface retrofitInterface;
    AppDatabase appDatabase;
    SetPortfolioMethods setPortfolioMethods;
    SharedPrefs sharedPrefs;

    public ActivityListDownloadService() {
        // Used to name the worker thread, important only for debugging.
        super("test-service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPrefs = new SharedPrefs(getApplicationContext());
        setPortfolioMethods = new SetPortfolioMethods();

        // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getActivityList();
        getHGList();
        getInputData();
        if (appDatabase.logsDao().getUnSyncedLogsCount() > 0){
            syncUpLogs();
        }
        if (appDatabase.hgActivitiesFlagDao().getUnSyncedHGActivitiesCount() > 0){
            syncUpHGActivities();
        }
        if (appDatabase.normalActivitiesFlagDao().getUnSyncedNormalActivitiesCount() > 0){
            syncUpNormalActivities();
        }
    }

    public void getActivityList() {

        String last_synced = "2019-01-01 00:00:00";

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<ActivityListDownload> call = retrofitInterface.getActivityListDownload(last_synced);

        call.enqueue(new Callback<ActivityListDownload>() {
            @Override
            public void onResponse(@NonNull Call<ActivityListDownload> call,
                                   @NonNull Response<ActivityListDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    ActivityListDownload activityListDownload = response.body();

                    if (activityListDownload != null) {
                        List<ActivityList> activityLists = activityListDownload.getDownload_list();
                        if (activityLists.size() > 0){
                            saveToActivityListTable(activityLists);
                        }
                    }

                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            Toast.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            Toast.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            Toast.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(@NotNull Call<ActivityListDownload> call, @NotNull Throwable t) {
                Log.d("tobi_check_list", t.toString());

            }
        });

    }

    void saveToActivityListTable(List<ActivityList> activityLists){
        SaveActivityListTable saveActivityListTable = new SaveActivityListTable();
        saveActivityListTable.execute(activityLists);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveActivityListTable extends AsyncTask<List<ActivityList>, Void, Void> {


        private final String TAG = SaveActivityListTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<ActivityList>... params) {

            List<ActivityList> activityLists = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.activityListDao().insert(activityLists);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getHGList() {

        String last_synced = "2019-01-01 00:00:00";

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<HGListDownload> call = retrofitInterface.getHGListDownload(last_synced);

        call.enqueue(new Callback<HGListDownload>() {
            @Override
            public void onResponse(@NonNull Call<HGListDownload> call,
                                   @NonNull Response<HGListDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    HGListDownload hgListDownload = response.body();

                    if (hgListDownload != null) {
                        List<HGList> hgLists = hgListDownload.getDownload_list();
                        if (hgLists.size() > 0){
                            saveToHGListTable(hgLists);
                        }
                    }

                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            Toast.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            Toast.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            Toast.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(@NotNull Call<HGListDownload> call, @NotNull Throwable t) {
                Log.d("tobi_check_list", t.toString());

            }
        });

    }

    void saveToHGListTable(List<HGList> hgLists){
        SaveHGListTable saveHGListTable = new SaveHGListTable();
        saveHGListTable.execute(hgLists);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveHGListTable extends AsyncTask<List<HGList>, Void, Void> {


        private final String TAG = SaveHGListTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<HGList>... params) {

            List<HGList> hgLists = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.hgListDao().insert(hgLists);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getInputData() {

        String last_synced = "2019-01-01 00:00:00";

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<MsPlaybookInputDownload> call = retrofitInterface.getMsPlaybookInputDataDownload(sharedPrefs.getStaffID(),portfolioToGson(sharedPrefs.getKeyPortfolioList()),last_synced);

        call.enqueue(new Callback<MsPlaybookInputDownload>() {
            @Override
            public void onResponse(@NonNull Call<MsPlaybookInputDownload> call,
                                   @NonNull Response<MsPlaybookInputDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    MsPlaybookInputDownload msPlaybookInputDownload = response.body();

                    if (msPlaybookInputDownload != null) {
                        List<Fields> fieldsList = msPlaybookInputDownload.getFields();
                        List<Members> membersList = msPlaybookInputDownload.getMembers();
                        if (fieldsList.size() > 0){
                            saveToFieldsTable(fieldsList);
                        }
                        if (membersList.size() > 0){
                            saveToMembersTable(membersList);
                        }
                    }
                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            Toast.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            Toast.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            Toast.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(@NotNull Call<MsPlaybookInputDownload> call, @NotNull Throwable t) {
                Log.d("tobi_check_list", t.toString());

            }
        });

    }

    void saveToFieldsTable(List<Fields> fieldsList){
        SaveFieldsTable saveFieldsTable = new SaveFieldsTable();
        saveFieldsTable.execute(fieldsList);
    }

    void saveToMembersTable(List<Members> membersList){
        SaveMembersTable saveMembersTable = new SaveMembersTable();
        saveMembersTable.execute(membersList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveFieldsTable extends AsyncTask<List<Fields>, Void, Void> {


        private final String TAG = SaveFieldsTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Fields>... params) {

            List<Fields> fieldsList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.fieldsDao().insert(fieldsList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class SaveMembersTable extends AsyncTask<List<Members>, Void, Void> {


        private final String TAG = SaveMembersTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Members>... params) {

            List<Members> membersList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.membersDao().insert(membersList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    String portfolioToGson(Set<String> portfolio){
        List<String> portfolioList = setPortfolioMethods.convertSetToList(portfolio);
        List<PortfolioListConverter> portfolioListConverters =  new ArrayList<>(0);
        for (String TempPortFolio : portfolioList){
            portfolioListConverters.add(new PortfolioListConverter(TempPortFolio));
        }
        return new Gson().toJson(portfolioListConverters);
    }

    private  class PortfolioListConverter{

        @SerializedName("ID")
        public String ID;

        public PortfolioListConverter(String ID) {
            this.ID = ID;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }
    }

    private void syncUpLogs() {
        List<Logs> logsList = appDatabase.logsDao().getUnSyncedLogs();
        String composed_json = new Gson().toJson(logsList);
        Log.d("composed_json",composed_json);
        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<List<LogsUpload>> call = retrofitInterface.uploadLogsRecord(composed_json, sharedPrefs.getStaffID());
        call.enqueue(new Callback<List<LogsUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<LogsUpload>> call, @NonNull Response<List<LogsUpload>> response) {
                Log.d("RETROFIT_TFM_DATA", "onResponse: " + response.body());
                if (response.isSuccessful()) {

                    List<LogsUpload> logsUploadList = response.body();

                    Log.d("syncingResponseTFM", Objects.requireNonNull(logsUploadList).toString());
                    if (logsUploadList == null){
                        Log.d("result", "null");
                    }else if(logsUploadList.size() == 0){
                        Log.d("result", "0");
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < logsUploadList.size(); i++) {
                                LogsUpload logsUpload = logsUploadList.get(i);
                                appDatabase.logsDao().updateLogs(logsUpload.getUnique_field_id(),logsUpload.getStaff_id(),logsUpload.getActivity_type(),logsUpload.getDate_logged());
                            }
                        });

                    }

                } else {
                    Log.i("tobi_tfm", "onResponse ERROR: " + response.body());
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 401:
                            Log.e("Error 401", "Bad Request");
                            break;
                        case 403:
                            Log.e("Error 403", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        case 500:
                            Log.e("Error 500", "Bad Request");
                            break;
                        case 501:
                            Log.e("Error 501", "Bad Request");
                            break;
                        default:
                            Log.e("Error", "Generic Error " + response.code());
                            break;
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<LogsUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewLogsUpload", t.toString());
            }
        });
    }

    private void syncUpHGActivities() {
        List<HGActivitiesFlag> hgActivitiesFlagList = appDatabase.hgActivitiesFlagDao().getUnSyncedHGActivities();
        String composed_json = new Gson().toJson(hgActivitiesFlagList);
        Log.d("composed_json",composed_json);
        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<List<HGActivitiesUpload>> call = retrofitInterface.uploadHGActivitiesRecord(composed_json, sharedPrefs.getStaffID());
        call.enqueue(new Callback<List<HGActivitiesUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<HGActivitiesUpload>> call, @NonNull Response<List<HGActivitiesUpload>> response) {
                Log.d("RETROFIT_TFM_DATA", "onResponse: " + response.body());
                if (response.isSuccessful()) {

                    List<HGActivitiesUpload> hgActivitiesUploadList = response.body();

                    Log.d("syncingResponseTFM", Objects.requireNonNull(hgActivitiesUploadList).toString());
                    if (hgActivitiesUploadList == null){
                        Log.d("result", "null");
                    }else if(hgActivitiesUploadList.size() == 0){
                        Log.d("result", "0");
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < hgActivitiesUploadList.size(); i++) {
                                HGActivitiesUpload hgActivitiesUpload = hgActivitiesUploadList.get(i);
                                appDatabase.hgActivitiesFlagDao().updateHGActivitiesSyncFlags(hgActivitiesUpload.getUnique_field_id(),hgActivitiesUpload.getHg_type());
                            }
                        });

                    }

                } else {
                    Log.i("tobi_tfm", "onResponse ERROR: " + response.body());
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 401:
                            Log.e("Error 401", "Bad Request");
                            break;
                        case 403:
                            Log.e("Error 403", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        case 500:
                            Log.e("Error 500", "Bad Request");
                            break;
                        case 501:
                            Log.e("Error 501", "Bad Request");
                            break;
                        default:
                            Log.e("Error", "Generic Error " + response.code());
                            break;
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<HGActivitiesUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewHGUpload", t.toString());
            }
        });
    }

    private void syncUpNormalActivities() {
        List<NormalActivitiesFlag> normalActivitiesFlagList = appDatabase.normalActivitiesFlagDao().getUnSyncedNormalActivities();
        String composed_json = new Gson().toJson(normalActivitiesFlagList);
        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<List<NormalActivitiesUpload>> call = retrofitInterface.uploadNormalActivitiesRecord(composed_json, sharedPrefs.getStaffID());
        call.enqueue(new Callback<List<NormalActivitiesUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<NormalActivitiesUpload>> call, @NonNull Response<List<NormalActivitiesUpload>> response) {
                if (response.isSuccessful()) {

                    List<NormalActivitiesUpload> normalActivitiesUploadList = response.body();
                    if (normalActivitiesUploadList == null){
                        Log.d("result", "null");
                    }else if(normalActivitiesUploadList.size() == 0){
                        Log.d("result", "0");
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < normalActivitiesUploadList.size(); i++) {
                                NormalActivitiesUpload normalActivitiesUpload = normalActivitiesUploadList.get(i);
                                appDatabase.normalActivitiesFlagDao().updateNormalActivitiesSyncFlag(normalActivitiesUpload.getUnique_field_id());
                            }
                        });

                    }

                } else {
                    Log.i("tobi_tfm", "onResponse ERROR: " + response.body());
                    int sc = response.code();
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            break;
                        case 401:
                            Log.e("Error 401", "Bad Request");
                            break;
                        case 403:
                            Log.e("Error 403", "Bad Request");
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            break;
                        case 500:
                            Log.e("Error 500", "Bad Request");
                            break;
                        case 501:
                            Log.e("Error 501", "Bad Request");
                            break;
                        default:
                            Log.e("Error", "Generic Error " + response.code());
                            break;
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<NormalActivitiesUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewNormalActivities", t.toString());
            }
        });
    }

}
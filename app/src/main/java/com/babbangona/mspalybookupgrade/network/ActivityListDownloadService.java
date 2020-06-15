package com.babbangona.mspalybookupgrade.network;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;
import com.babbangona.mspalybookupgrade.data.db.entities.AppVariables;
import com.babbangona.mspalybookupgrade.data.db.entities.Category;
import com.babbangona.mspalybookupgrade.data.db.entities.Fields;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.HGList;
import com.babbangona.mspalybookupgrade.data.db.entities.HarvestLocationsTable;
import com.babbangona.mspalybookupgrade.data.db.entities.LastSyncTable;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.RFActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.RFList;
import com.babbangona.mspalybookupgrade.data.db.entities.StaffList;
import com.babbangona.mspalybookupgrade.data.db.entities.SyncSummary;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.network.object.ActivityListDownload;
import com.babbangona.mspalybookupgrade.network.object.AppVariablesDownload;
import com.babbangona.mspalybookupgrade.network.object.CategoryDownload;
import com.babbangona.mspalybookupgrade.network.object.HGActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.HGActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.HGListDownload;
import com.babbangona.mspalybookupgrade.network.object.HarvestLocationDownload;
import com.babbangona.mspalybookupgrade.network.object.LogsDownload;
import com.babbangona.mspalybookupgrade.network.object.LogsUpload;
import com.babbangona.mspalybookupgrade.network.object.MsPlaybookInputDownload;
import com.babbangona.mspalybookupgrade.network.object.NormalActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.NormalActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.RFActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.RFActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.RFListDownload;
import com.babbangona.mspalybookupgrade.network.object.StaffListDownload;
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
        setPortfolioMethods = new SetPortfolioMethods();/**/
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getInputData();
        getHGList();
        getRFList();
        getHGACtivitiesFlagDownload();
        getRFActivitiesFlagDownload();
        getNormalActivitiesFlagDownload();
        getCategoryDownload();
        getHarvestLocationDownload();
        getAppVariablesDownload();

        if (appDatabase.logsDao().getUnSyncedLogsCount() > 0){
            syncUpLogs();
        }
        if (appDatabase.hgActivitiesFlagDao().getUnSyncedHGActivitiesCount() > 0){
            syncUpHGActivities();
        }
        if (appDatabase.normalActivitiesFlagDao().getUnSyncedNormalActivitiesCount() > 0){
            syncUpNormalActivities();
        }
        if (appDatabase.rfActivitiesFlagDao().getUnSyncedRFActivitiesCount() > 0){
            syncUpRFActivities();
        }
        getStaffList();
        getLogsDownload();
    }

    /*
    public static final String LAST_SYNC_TABLE                          = "last_sync";
    public static final String CATEGORY_TABLE                           = "category";
    public static final String SYNC_SUMMARY_TABLE                       = "sync_summary"; */

    public void getActivityList() {

        String last_synced = getLastSyncActivityList();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<ActivityListDownload> call = retrofitInterface.getActivityListDownload(last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
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

                        if (getStaffCountLastSync() > 0){
                            appDatabase.lastSyncTableDao().updateLastSyncActivityList(sharedPrefs.getStaffID(),activityListDownload.getLast_sync_time());
                        }else{
                            LastSyncTable lastSyncTable = new LastSyncTable();
                            lastSyncTable.setLast_sync_activity_list(activityListDownload.getLast_sync_time());
                            lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                            appDatabase.lastSyncTableDao().insert(lastSyncTable);
                        }
                        insetToSyncSummary(DatabaseStringConstants.ACTIVITY_LIST_TABLE,
                                "Activity List Download",
                                "1",
                                returnRemark(activityLists.size()),
                                activityListDownload.getLast_sync_time()

                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.ACTIVITY_LIST_TABLE,
                                "Activity List Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }
                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.ACTIVITY_LIST_TABLE,
                            "Activity List Download",
                            "0",
                            "Download Error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<ActivityListDownload> call, @NotNull Throwable t) {
                Log.d("tobi_check_list", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);

                saveToSyncSummary(DatabaseStringConstants.ACTIVITY_LIST_TABLE,
                        "Activity List Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
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

        String last_synced = getLastSyncTimeHGList();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<HGListDownload> call = retrofitInterface.getHGListDownload(last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
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
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncHGList(sharedPrefs.getStaffID(),hgListDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_hg_list(hgListDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.HG_LIST_TABLE,
                                "HG List Download",
                                "1",
                                returnRemark(hgLists.size()),
                                hgListDownload.getLast_sync_time()

                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.HG_LIST_TABLE,
                                "HG List Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }
                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.HG_LIST_TABLE,
                            "HG List Download",
                            "0",
                            "Download Error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<HGListDownload> call, @NotNull Throwable t) {
                Log.d("tobi_check_list", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.HG_LIST_TABLE,
                        "HG List Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
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

        String last_synced = getLastSyncTimeInputRecords();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<MsPlaybookInputDownload> call = retrofitInterface.getMsPlaybookInputDataDownload(sharedPrefs.getStaffID(),portfolioToGson(sharedPrefs.getKeyPortfolioList()),last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
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
                            saveToFieldsTable(fieldsList);if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncFields(sharedPrefs.getStaffID(),msPlaybookInputDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_fields(msPlaybookInputDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        if (membersList.size() > 0){
                            saveToMembersTable(membersList);
                            saveToFieldsTable(fieldsList);if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncMembers(sharedPrefs.getStaffID(),msPlaybookInputDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_members(msPlaybookInputDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.FIELDS_TABLE,
                                "Fields Record Download",
                                "1",
                                returnRemark(fieldsList.size()),
                                msPlaybookInputDownload.getLast_sync_time()

                        );
                        insetToSyncSummary(DatabaseStringConstants.MEMBERS_TABLE,
                                "Members Record Download",
                                "1",
                                returnRemark(membersList.size()),
                                msPlaybookInputDownload.getLast_sync_time()

                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.FIELDS_TABLE,
                                "Fields Record Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                        saveToSyncSummary(DatabaseStringConstants.MEMBERS_TABLE,
                                "Members Record Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }
                    getActivityList();
                    Toast.makeText(ActivityListDownloadService.this, "Download done", Toast.LENGTH_LONG).show();
                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    sharedPrefs.setKeyProgressDialogStatus(1);
                    getActivityList();
                    Toast.makeText(ActivityListDownloadService.this, "Download failed, network error", Toast.LENGTH_LONG).show();

                    saveToSyncSummary(DatabaseStringConstants.FIELDS_TABLE,
                            "Fields Record Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                    saveToSyncSummary(DatabaseStringConstants.MEMBERS_TABLE,
                            "Members Record Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<MsPlaybookInputDownload> call, @NotNull Throwable t) {
                Log.d("tobi_check_list", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                getActivityList();
                Toast.makeText(ActivityListDownloadService.this, "Download failed", Toast.LENGTH_LONG).show();
                saveToSyncSummary(DatabaseStringConstants.FIELDS_TABLE,
                        "Fields Record Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
                saveToSyncSummary(DatabaseStringConstants.MEMBERS_TABLE,
                        "Members Record Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
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
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<List<LogsUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<LogsUpload>> call, @NonNull Response<List<LogsUpload>> response) {
                Log.d("RETROFIT_TFM_DATA", "onResponse: " + response.body());
                if (response.isSuccessful()) {

                    List<LogsUpload> logsUploadList = response.body();

                    Log.d("syncingResponseTFM", Objects.requireNonNull(logsUploadList).toString());
                    if (logsUploadList == null){
                        Log.d("result", "null");
                        saveToSyncSummary(DatabaseStringConstants.LOGS_TABLE+"_upload",
                                "Logs Upload",
                                "0",
                                "Upload null",
                                "0000-00-00 00:00:00"
                        );
                    }else if(logsUploadList.size() == 0){
                        Log.d("result", "0");
                        saveToSyncSummary(DatabaseStringConstants.LOGS_TABLE+"_upload",
                                "Logs Upload",
                                "0",
                                "Upload empty",
                                "0000-00-00 00:00:00"
                        );
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < logsUploadList.size(); i++) {
                                LogsUpload logsUpload = logsUploadList.get(i);
                                appDatabase.logsDao().updateLogs(logsUpload.getUnique_field_id(),logsUpload.getStaff_id(),logsUpload.getActivity_type(),logsUpload.getDate_logged());
                            }

                            insetToSyncSummary(DatabaseStringConstants.LOGS_TABLE+"_upload",
                                    "Logs Upload",
                                    "1",
                                    returnRemark(logsUploadList.size()),
                                    logsUploadList.get(0).getLast_synced()

                            );
                        });
                        if (getStaffCountLastSync() > 0){
                            appDatabase.lastSyncTableDao().updateLastSyncUpLogs(sharedPrefs.getStaffID(),logsUploadList.get(0).getLast_synced());
                        }else{
                            LastSyncTable lastSyncTable = new LastSyncTable();
                            lastSyncTable.setLast_sync_up_logs(logsUploadList.get(0).getLast_synced());
                            lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                            appDatabase.lastSyncTableDao().insert(lastSyncTable);
                        }
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
                    saveToSyncSummary(DatabaseStringConstants.LOGS_TABLE+"_upload",
                            "Logs Upload",
                            "0",
                            "Upload error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NonNull Call<List<LogsUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewLogsUpload", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.LOGS_TABLE+"_upload",
                        "Logs Upload",
                        "0",
                        "Upload failed",
                        "0000-00-00 00:00:00"
                );
            }
        });
    }

    private void syncUpHGActivities() {
        List<HGActivitiesFlag> hgActivitiesFlagList = appDatabase.hgActivitiesFlagDao().getUnSyncedHGActivities();
        String composed_json = new Gson().toJson(hgActivitiesFlagList);
        Log.d("composed_json",composed_json);
        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<List<HGActivitiesUpload>> call = retrofitInterface.uploadHGActivitiesRecord(composed_json, sharedPrefs.getStaffID());
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<List<HGActivitiesUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<HGActivitiesUpload>> call, @NonNull Response<List<HGActivitiesUpload>> response) {
                Log.d("RETROFIT_TFM_DATA", "onResponse: " + response.body());
                if (response.isSuccessful()) {

                    List<HGActivitiesUpload> hgActivitiesUploadList = response.body();

                    Log.d("syncingResponseTFM", Objects.requireNonNull(hgActivitiesUploadList).toString());
                    if (hgActivitiesUploadList == null){
                        Log.d("result", "null");
                        saveToSyncSummary(DatabaseStringConstants.HG_ACTIVITY_FLAGS_TABLE+"_upload",
                                "HG Activities Upload",
                                "0",
                                "Upload null",
                                "0000-00-00 00:00:00"
                        );
                    }else if(hgActivitiesUploadList.size() == 0){
                        Log.d("result", "0");
                        saveToSyncSummary(DatabaseStringConstants.HG_ACTIVITY_FLAGS_TABLE+"_upload",
                                "HG Activities Upload",
                                "0",
                                "Upload empty",
                                "0000-00-00 00:00:00"
                        );
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < hgActivitiesUploadList.size(); i++) {
                                HGActivitiesUpload hgActivitiesUpload = hgActivitiesUploadList.get(i);
                                appDatabase.hgActivitiesFlagDao().updateHGActivitiesSyncFlags(hgActivitiesUpload.getUnique_field_id(),hgActivitiesUpload.getHg_type());
                            }
                            insetToSyncSummary(DatabaseStringConstants.HG_ACTIVITY_FLAGS_TABLE+"_upload",
                                    "HG Activities Upload",
                                    "1",
                                    returnRemark(hgActivitiesUploadList.size()),
                                    hgActivitiesUploadList.get(0).getLast_synced()

                            );
                        });
                        if (getStaffCountLastSync() > 0){
                            appDatabase.lastSyncTableDao().updateLastSyncUpHGActivitiesFlag(sharedPrefs.getStaffID(),hgActivitiesUploadList.get(0).getLast_synced());
                        }else{
                            LastSyncTable lastSyncTable = new LastSyncTable();
                            lastSyncTable.setLast_sync_up_hg_activities_flag(hgActivitiesUploadList.get(0).getLast_synced());
                            lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                            appDatabase.lastSyncTableDao().insert(lastSyncTable);
                        }

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
                    saveToSyncSummary(DatabaseStringConstants.HG_ACTIVITY_FLAGS_TABLE+"_upload",
                            "HG Activities Upload",
                            "0",
                            "Upload error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NonNull Call<List<HGActivitiesUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewHGUpload", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.HG_ACTIVITY_FLAGS_TABLE+"_upload",
                        "HG Activities Upload",
                        "0",
                        "Upload failed",
                        "0000-00-00 00:00:00"
                );
            }
        });
    }

    private void syncUpNormalActivities() {
        List<NormalActivitiesFlag> normalActivitiesFlagList = appDatabase.normalActivitiesFlagDao().getUnSyncedNormalActivities();
        String composed_json = new Gson().toJson(normalActivitiesFlagList);
        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<List<NormalActivitiesUpload>> call = retrofitInterface.uploadNormalActivitiesRecord(composed_json, sharedPrefs.getStaffID());
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<List<NormalActivitiesUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<NormalActivitiesUpload>> call, @NonNull Response<List<NormalActivitiesUpload>> response) {
                if (response.isSuccessful()) {

                    List<NormalActivitiesUpload> normalActivitiesUploadList = response.body();
                    if (normalActivitiesUploadList == null){
                        Log.d("result", "null");
                        saveToSyncSummary(DatabaseStringConstants.NORMAL_ACTIVITY_FLAGS_TABLE+"_upload",
                                "Normal Activities Upload",
                                "0",
                                "Upload null",
                                "0000-00-00 00:00:00"
                        );
                    }else if(normalActivitiesUploadList.size() == 0){
                        Log.d("result", "0");
                        saveToSyncSummary(DatabaseStringConstants.NORMAL_ACTIVITY_FLAGS_TABLE+"_upload",
                                "Normal Activities Upload",
                                "0",
                                "Upload empty",
                                "0000-00-00 00:00:00"
                        );
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < normalActivitiesUploadList.size(); i++) {
                                NormalActivitiesUpload normalActivitiesUpload = normalActivitiesUploadList.get(i);
                                appDatabase.normalActivitiesFlagDao().updateNormalActivitiesSyncFlag(normalActivitiesUpload.getUnique_field_id());
                                insetToSyncSummary(DatabaseStringConstants.NORMAL_ACTIVITY_FLAGS_TABLE+"_upload",
                                        "Normal Activities Upload",
                                        "1",
                                        returnRemark(normalActivitiesUploadList.size()),
                                        normalActivitiesUploadList.get(0).getLast_synced()

                                );
                            }
                        });
                        if (getStaffCountLastSync() > 0){
                            appDatabase.lastSyncTableDao().updateLastSyncUpNormalActivitiesFlag(sharedPrefs.getStaffID(),normalActivitiesUploadList.get(0).getLast_synced());
                        }else{
                            LastSyncTable lastSyncTable = new LastSyncTable();
                            lastSyncTable.setLast_sync_up_normal_activities_flag(normalActivitiesUploadList.get(0).getLast_synced());
                            lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                            appDatabase.lastSyncTableDao().insert(lastSyncTable);
                        }

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
                    saveToSyncSummary(DatabaseStringConstants.NORMAL_ACTIVITY_FLAGS_TABLE+"_upload",
                            "Normal Activities Upload",
                            "0",
                            "Upload error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NonNull Call<List<NormalActivitiesUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewNormalActivities", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.NORMAL_ACTIVITY_FLAGS_TABLE+"_upload",
                        "Normal Activities Upload",
                        "0",
                        "Upload failed",
                        "0000-00-00 00:00:00"
                );
            }
        });
    }

    public void getStaffList() {

        String last_synced = getLastSyncTimeStaffList();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<StaffListDownload> call = retrofitInterface.getStaffListDownload(last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<StaffListDownload>() {
            @Override
            public void onResponse(@NonNull Call<StaffListDownload> call,
                                   @NonNull Response<StaffListDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    StaffListDownload staffListDownload = response.body();

                    if (staffListDownload != null) {
                        List<StaffList> staffLists = staffListDownload.getDownload_list();
                        if (staffLists.size() > 0){
                            saveToStaffListTable(staffLists);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncStaff(sharedPrefs.getStaffID(),staffListDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_staff(staffListDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.STAFF_TABLE,
                                "Staff List Download",
                                "1",
                                returnRemark(staffLists.size()),
                                staffListDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.STAFF_TABLE,
                                "Staff List Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }
                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.STAFF_TABLE,
                            "Staff List Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<StaffListDownload> call, @NotNull Throwable t) {
                Log.d("tobi_staff_list", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.STAFF_TABLE,
                        "Staff List Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToStaffListTable(List<StaffList> staffLists){
        SaveStaffListTable saveStaffListTable = new SaveStaffListTable();
        saveStaffListTable.execute(staffLists);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveStaffListTable extends AsyncTask<List<StaffList>, Void, Void> {


        private final String TAG = SaveStaffListTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<StaffList>... params) {

            List<StaffList> staffLists = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.staffListDao().insert(staffLists);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getLogsDownload() {

        String last_synced = getLastSyncLogs();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<LogsDownload> call = retrofitInterface.downloadLogs(sharedPrefs.getStaffID(),portfolioToGson(sharedPrefs.getKeyPortfolioList()),last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<LogsDownload>() {
            @Override
            public void onResponse(@NonNull Call<LogsDownload> call,
                                   @NonNull Response<LogsDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    LogsDownload logsDownload = response.body();

                    if (logsDownload != null) {
                        List<Logs> logsList = logsDownload.getDownload_list();
                        if (logsList.size() > 0){
                            saveToLogsTable(logsList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncDownLogs(sharedPrefs.getStaffID(),logsDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_down_logs(logsDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.LOGS_TABLE+"_download",
                                "Logs Download",
                                "1",
                                returnRemark(logsList.size()),
                                logsDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.LOGS_TABLE+"_download",
                                "Logs Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }

                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.LOGS_TABLE+"_download",
                            "Logs Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<LogsDownload> call, @NotNull Throwable t) {
                Log.d("tobi_logs_download", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.LOGS_TABLE+"_download",
                        "Logs Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToLogsTable(List<Logs> staffLists){
        SaveLogsTable saveLogsTable = new SaveLogsTable();
        saveLogsTable.execute(staffLists);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveLogsTable extends AsyncTask<List<Logs>, Void, Void> {


        private final String TAG = SaveStaffListTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Logs>... params) {

            List<Logs> logsList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.logsDao().insert(logsList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getHGACtivitiesFlagDownload() {

        String last_synced = getLastSyncHGActivities();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<HGActivitiesFlagDownload> call = retrofitInterface.downloadHGActivityFlag(sharedPrefs.getStaffID(),portfolioToGson(sharedPrefs.getKeyPortfolioList()),last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<HGActivitiesFlagDownload>() {
            @Override
            public void onResponse(@NonNull Call<HGActivitiesFlagDownload> call,
                                   @NonNull Response<HGActivitiesFlagDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    HGActivitiesFlagDownload hgActivitiesFlagDownload = response.body();

                    if (hgActivitiesFlagDownload != null) {
                        List<HGActivitiesFlag> hgActivitiesFlagList = hgActivitiesFlagDownload.getDownload_list();
                        if (hgActivitiesFlagList.size() > 0){
                            saveToHGActivitiesFlagTable(hgActivitiesFlagList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncDownHGActivitiesFlag(sharedPrefs.getStaffID(),hgActivitiesFlagDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_down_hg_activities_flag(hgActivitiesFlagDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.HG_ACTIVITY_FLAGS_TABLE+"_download",
                                "HG Activities Download",
                                "1",
                                returnRemark(hgActivitiesFlagList.size()),
                                hgActivitiesFlagDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.HG_ACTIVITY_FLAGS_TABLE+"_download",
                                "HG Activities Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }

                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.HG_ACTIVITY_FLAGS_TABLE+"_download",
                            "HG Activities Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<HGActivitiesFlagDownload> call, @NotNull Throwable t) {
                Log.d("tobi_hg_flag_download", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.HG_ACTIVITY_FLAGS_TABLE+"_download",
                        "HG Activities Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToHGActivitiesFlagTable(List<HGActivitiesFlag> hgActivitiesFlagList){
        SaveHGActivitiesFlagTable saveHGActivitiesFlagTable = new SaveHGActivitiesFlagTable();
        saveHGActivitiesFlagTable.execute(hgActivitiesFlagList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveHGActivitiesFlagTable extends AsyncTask<List<HGActivitiesFlag>, Void, Void> {


        private final String TAG = SaveHGActivitiesFlagTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<HGActivitiesFlag>... params) {

            List<HGActivitiesFlag> hgActivitiesFlagList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.hgActivitiesFlagDao().insert(hgActivitiesFlagList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getNormalActivitiesFlagDownload() {

        String last_synced = getLastSyncNormalActivities();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<NormalActivitiesFlagDownload> call = retrofitInterface.downloadNormalActivityFlag(sharedPrefs.getStaffID(),portfolioToGson(sharedPrefs.getKeyPortfolioList()),last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<NormalActivitiesFlagDownload>() {
            @Override
            public void onResponse(@NonNull Call<NormalActivitiesFlagDownload> call,
                                   @NonNull Response<NormalActivitiesFlagDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    NormalActivitiesFlagDownload normalActivitiesFlagDownload = response.body();

                    if (normalActivitiesFlagDownload != null) {
                        List<NormalActivitiesFlag> normalActivitiesFlagList = normalActivitiesFlagDownload.getDownload_list();
                        if (normalActivitiesFlagList.size() > 0){
                            saveToNormalActivitiesFlagTable(normalActivitiesFlagList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncDownNormalActivitiesFlag(sharedPrefs.getStaffID(),normalActivitiesFlagDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_down_normal_activities_flag(normalActivitiesFlagDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.NORMAL_ACTIVITY_FLAGS_TABLE+"_download",
                                "Normal Activities Download",
                                "1",
                                returnRemark(normalActivitiesFlagList.size()),
                                normalActivitiesFlagDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.NORMAL_ACTIVITY_FLAGS_TABLE+"_download",
                                "Normal Activities Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }

                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.NORMAL_ACTIVITY_FLAGS_TABLE+"_download",
                            "Normal Activities Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<NormalActivitiesFlagDownload> call, @NotNull Throwable t) {
                Log.d("tobi_normal_download", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.NORMAL_ACTIVITY_FLAGS_TABLE+"_download",
                        "Normal Activities Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToNormalActivitiesFlagTable(List<NormalActivitiesFlag> normalActivitiesFlagList){
        SaveNormalActivitiesFlagTable saveNormalActivitiesFlagTable = new SaveNormalActivitiesFlagTable();
        saveNormalActivitiesFlagTable.execute(normalActivitiesFlagList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveNormalActivitiesFlagTable extends AsyncTask<List<NormalActivitiesFlag>, Void, Void> {


        private final String TAG = SaveNormalActivitiesFlagTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<NormalActivitiesFlag>... params) {

            List<NormalActivitiesFlag> normalActivitiesFlagList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.normalActivitiesFlagDao().insert(normalActivitiesFlagList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getCategoryDownload() {

        String last_synced = getLastSyncCategory();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<CategoryDownload> call = retrofitInterface.downloadCategory(last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<CategoryDownload>() {
            @Override
            public void onResponse(@NonNull Call<CategoryDownload> call,
                                   @NonNull Response<CategoryDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    CategoryDownload categoryDownload = response.body();

                    if (categoryDownload != null) {
                        List<Category> categoryList = categoryDownload.getDownload_list();
                        if (categoryList.size() > 0){
                            saveToCategoryTable(categoryList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncCategory(sharedPrefs.getStaffID(),categoryDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_category(categoryDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.CATEGORY_TABLE,
                                "Category List Download",
                                "1",
                                returnRemark(categoryList.size()),
                                categoryDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.CATEGORY_TABLE,
                                "Category List Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }

                }else {
                    int sc = response.code();
                    Log.d("scCode Category:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.CATEGORY_TABLE,
                            "Category List Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<CategoryDownload> call, @NotNull Throwable t) {
                Log.d("tobi_staff_list", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.CATEGORY_TABLE,
                        "Category List Download",
                        "0",
                        "Download empty",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToCategoryTable(List<Category> categoryList){
        SaveCategoryTable saveCategoryTable = new SaveCategoryTable();
        saveCategoryTable.execute(categoryList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveCategoryTable extends AsyncTask<List<Category>, Void, Void> {


        private final String TAG = SaveCategoryTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Category>... params) {

            List<Category> categoryList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.categoryDao().insert(categoryList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getHarvestLocationDownload() {

        String last_synced = getLastSyncHarvestLocation();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<HarvestLocationDownload> call = retrofitInterface.downloadHarvestLocation(last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<HarvestLocationDownload>() {
            @Override
            public void onResponse(@NonNull Call<HarvestLocationDownload> call,
                                   @NonNull Response<HarvestLocationDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    HarvestLocationDownload harvestLocationDownload = response.body();

                    if (harvestLocationDownload != null) {
                        List<HarvestLocationsTable> harvestLocationsTableList = harvestLocationDownload.getDownload_list();
                        if (harvestLocationsTableList.size() > 0){
                            saveToHarvestLocationsTable(harvestLocationsTableList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncHarvestLocation(sharedPrefs.getStaffID(),harvestLocationDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_harvest_location(harvestLocationDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.HARVEST_LOCATION_TABLE,
                                "Harvest Collection Centres Download",
                                "1",
                                returnRemark(harvestLocationsTableList.size()),
                                harvestLocationDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.HARVEST_LOCATION_TABLE,
                                "Harvest Collection Centres Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }

                }else {
                    int sc = response.code();
                    Log.d("scCode Category:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.HARVEST_LOCATION_TABLE,
                            "Harvest Collection Centres Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<HarvestLocationDownload> call, @NotNull Throwable t) {
                Log.d("tobi_harvest_locations_table", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.HARVEST_LOCATION_TABLE,
                        "Harvest Collection Centres Download",
                        "0",
                        "Download empty",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToHarvestLocationsTable(List<HarvestLocationsTable> harvestLocationsTableList){
        SaveToHarvestLocationsTable saveToHarvestLocationsTable = new SaveToHarvestLocationsTable();
        saveToHarvestLocationsTable.execute(harvestLocationsTableList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveToHarvestLocationsTable extends AsyncTask<List<HarvestLocationsTable>, Void, Void> {


        private final String TAG = SaveToHarvestLocationsTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<HarvestLocationsTable>... params) {

            List<HarvestLocationsTable> harvestLocationsTableList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.harvestLocationsDao().insert(harvestLocationsTableList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getAppVariablesDownload() {

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<AppVariablesDownload> call = retrofitInterface.downloadAppVariables(sharedPrefs.getStaffID());
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<AppVariablesDownload>() {
            @Override
            public void onResponse(@NonNull Call<AppVariablesDownload> call,
                                   @NonNull Response<AppVariablesDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    AppVariablesDownload appVariablesDownload = response.body();

                    if (appVariablesDownload != null) {
                        List<AppVariables> appVariablesList = appVariablesDownload.getDownload_list();
                        if (appVariablesList.size() > 0){
                            saveToAppVariablesTable(appVariablesList);
                        }
                        insetToSyncSummary(DatabaseStringConstants.APP_VARIABLES,
                                "App Variables Download",
                                "1",
                                returnRemark(appVariablesList.size()),
                                appVariablesDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.APP_VARIABLES,
                                "App Variables Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }

                }else {
                    int sc = response.code();
                    Log.d("scCode Category:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.APP_VARIABLES,
                            "App Variables Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<AppVariablesDownload> call, @NotNull Throwable t) {
                Log.d("tobi_apps_variable", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.APP_VARIABLES,
                        "App Variables Download",
                        "0",
                        "Download empty",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToAppVariablesTable(List<AppVariables> appVariablesList){
        SaveToAppVariablesTable saveToAppVariablesTable = new SaveToAppVariablesTable();
        saveToAppVariablesTable.execute(appVariablesList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveToAppVariablesTable extends AsyncTask<List<AppVariables>, Void, Void> {


        private final String TAG = SaveToAppVariablesTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<AppVariables>... params) {

            List<AppVariables> appVariablesList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.appVariablesDao().insert(appVariablesList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getRFList() {

        String last_synced = getLastSyncTimeRFList();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<RFListDownload> call = retrofitInterface.getRFListDownload(last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<RFListDownload>() {
            @Override
            public void onResponse(@NonNull Call<RFListDownload> call,
                                   @NonNull Response<RFListDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    RFListDownload rfListDownload = response.body();

                    if (rfListDownload != null) {
                        List<RFList> rfLists = rfListDownload.getDownload_list();
                        if (rfLists.size() > 0){
                            saveToRFListTable(rfLists);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncRFList(sharedPrefs.getStaffID(),rfListDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_rf_list(rfListDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.RF_LIST_TABLE,
                                "RF List Download",
                                "1",
                                returnRemark(rfLists.size()),
                                rfListDownload.getLast_sync_time()

                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.RF_LIST_TABLE,
                                "RF List Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }
                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.RF_LIST_TABLE,
                            "RF List Download",
                            "0",
                            "Download Error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<RFListDownload> call, @NotNull Throwable t) {
                Log.d("tobi_rf_list", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.RF_LIST_TABLE,
                        "RF List Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToRFListTable(List<RFList> rfLists){
        SaveRFListTable saveRFListTable = new SaveRFListTable();
        saveRFListTable.execute(rfLists);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveRFListTable extends AsyncTask<List<RFList>, Void, Void> {


        private final String TAG = SaveRFListTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<RFList>... params) {

            List<RFList> rfLists = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.rfListDao().insert(rfLists);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getRFActivitiesFlagDownload() {

        String last_synced = getLastSyncRFActivities();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<RFActivitiesFlagDownload> call = retrofitInterface.downloadRFActivityFlag(sharedPrefs.getStaffID(),portfolioToGson(sharedPrefs.getKeyPortfolioList()),last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<RFActivitiesFlagDownload>() {
            @Override
            public void onResponse(@NonNull Call<RFActivitiesFlagDownload> call,
                                   @NonNull Response<RFActivitiesFlagDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    RFActivitiesFlagDownload rfActivitiesFlagDownload = response.body();

                    if (rfActivitiesFlagDownload != null) {
                        List<RFActivitiesFlag> rfActivitiesFlagList = rfActivitiesFlagDownload.getDownload_list();
                        if (rfActivitiesFlagList.size() > 0){
                            saveToRFActivitiesFlagTable(rfActivitiesFlagList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncDownRFActivitiesFlag(sharedPrefs.getStaffID(),rfActivitiesFlagDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_down_rf_activities_flag(rfActivitiesFlagDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.RF_ACTIVITY_FLAGS_TABLE+"_download",
                                "RF Activities Download",
                                "1",
                                returnRemark(rfActivitiesFlagList.size()),
                                rfActivitiesFlagDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.RF_ACTIVITY_FLAGS_TABLE+"_download",
                                "RF Activities Download",
                                "0",
                                "Download null",
                                "0000-00-00 00:00:00"
                        );
                    }

                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(ActivityListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                    saveToSyncSummary(DatabaseStringConstants.RF_ACTIVITY_FLAGS_TABLE+"_download",
                            "RF Activities Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<RFActivitiesFlagDownload> call, @NotNull Throwable t) {
                Log.d("tobi_rf_flag_download", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.RF_ACTIVITY_FLAGS_TABLE+"_download",
                        "RF Activities Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToRFActivitiesFlagTable(List<RFActivitiesFlag> rfActivitiesFlagList){
        SaveRFActivitiesFlagTable saveRFActivitiesFlagTable = new SaveRFActivitiesFlagTable();
        saveRFActivitiesFlagTable.execute(rfActivitiesFlagList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveRFActivitiesFlagTable extends AsyncTask<List<RFActivitiesFlag>, Void, Void> {


        private final String TAG = SaveRFActivitiesFlagTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<RFActivitiesFlag>... params) {

            List<RFActivitiesFlag> rfActivitiesFlagList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.rfActivitiesFlagDao().insert(rfActivitiesFlagList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    private void syncUpRFActivities() {
        List<RFActivitiesFlag> rfActivitiesFlagList = appDatabase.rfActivitiesFlagDao().getUnSyncedRFActivities();
        String composed_json = new Gson().toJson(rfActivitiesFlagList);
        Log.d("composed_json",composed_json);
        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<List<RFActivitiesUpload>> call = retrofitInterface.uploadRFActivitiesRecord(composed_json, sharedPrefs.getStaffID());
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<List<RFActivitiesUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<RFActivitiesUpload>> call, @NonNull Response<List<RFActivitiesUpload>> response) {
                Log.d("RETROFIT_TFM_DATA", "onResponse: " + response.body());
                if (response.isSuccessful()) {

                    List<RFActivitiesUpload> rfActivitiesUploadList = response.body();

                    //Log.d("syncingResponseTFM", Objects.requireNonNull(rfActivitiesUploadList).toString());
                    if (rfActivitiesUploadList == null){
                        Log.d("result", "null");
                        saveToSyncSummary(DatabaseStringConstants.RF_ACTIVITY_FLAGS_TABLE+"_upload",
                                "RF Activities Upload",
                                "0",
                                "Upload null",
                                "0000-00-00 00:00:00"
                        );
                    }else if(rfActivitiesUploadList.size() == 0){
                        Log.d("result", "0");
                        saveToSyncSummary(DatabaseStringConstants.RF_ACTIVITY_FLAGS_TABLE+"_upload",
                                "RF Activities Upload",
                                "0",
                                "Upload empty",
                                "0000-00-00 00:00:00"
                        );
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < rfActivitiesUploadList.size(); i++) {
                                RFActivitiesUpload rfActivitiesUpload = rfActivitiesUploadList.get(i);
                                appDatabase.rfActivitiesFlagDao().updateRFActivitiesSyncFlags(rfActivitiesUpload.getUnique_field_id(),rfActivitiesUpload.getHg_type());
                            }
                            insetToSyncSummary(DatabaseStringConstants.RF_ACTIVITY_FLAGS_TABLE+"_upload",
                                    "RF Activities Upload",
                                    "1",
                                    returnRemark(rfActivitiesUploadList.size()),
                                    rfActivitiesUploadList.get(0).getLast_synced()

                            );
                        });
                        if (getStaffCountLastSync() > 0){
                            appDatabase.lastSyncTableDao().updateLastSyncUpRFActivitiesFlag(sharedPrefs.getStaffID(),rfActivitiesUploadList.get(0).getLast_synced());
                        }else{
                            LastSyncTable lastSyncTable = new LastSyncTable();
                            lastSyncTable.setLast_sync_up_rf_activities_flag(rfActivitiesUploadList.get(0).getLast_synced());
                            lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                            appDatabase.lastSyncTableDao().insert(lastSyncTable);
                        }

                    }
                } else {
                    Log.i("tobi_rf_upload ", "onResponse ERROR: " + response.body());
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
                    saveToSyncSummary(DatabaseStringConstants.RF_ACTIVITY_FLAGS_TABLE+"_upload",
                            "RF Activities Upload",
                            "0",
                            "Upload error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NonNull Call<List<RFActivitiesUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewRFUpload", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.RF_ACTIVITY_FLAGS_TABLE+"_upload",
                        "RF Activities Upload",
                        "0",
                        "Upload failed",
                        "0000-00-00 00:00:00"
                );
            }
        });
    }

    String getLastSyncTimeStaffList(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncStaff(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncTimeInputRecords(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncFields(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncTimeHGList(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncHGList(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncActivityList(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncActivityList(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncLogs(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncDownLogs(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncHGActivities(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncDownHGActivitiesFlag(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncNormalActivities(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncDownNormalActivitiesFlag(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    int getStaffCountLastSync(){
        int count = 0;
        try {
            count = appDatabase.lastSyncTableDao().getStaffCount(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            count = 0;
        }
        return count;
    }

    String getLastSyncCategory(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncCategory(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncHarvestLocation(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncHarvestLocation(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncTimeRFList(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncRFList(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncRFActivities(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncDownRFActivitiesFlag(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    int getTableIDCount(String table_id, String staff_id){
        int result = 0;
        try {
            result = appDatabase.syncSummaryDao().countTableID(table_id, staff_id);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    void saveToSyncSummary(String table_id, String table_name, String status, String remarks, String sync_time){

        if (getTableIDCount(table_id, sharedPrefs.getStaffID()) > 0){
            appDatabase.syncSummaryDao().updateStatus(
                    table_id,
                    sharedPrefs.getStaffID(),
                    remarks,
                    status
            );
        }else{
            appDatabase.syncSummaryDao().insert(new SyncSummary(
                    table_id,
                    sharedPrefs.getStaffID(),
                    table_name,
                    status,
                    remarks,
                    sync_time
                    )
            );
        }
    }

    String returnRemark(int size){
        String statement;
        if (size > 0){
            statement = "Download successful";
        }else{
            statement = "Download Empty";
        }
        return statement;
    }

    void insetToSyncSummary(String table_id, String table_name, String status, String remarks, String sync_time){

        appDatabase.syncSummaryDao().insert(new SyncSummary(
                table_id,
                sharedPrefs.getStaffID(),
                table_name,
                status,
                remarks,
                sync_time
                )
        );
    }

}
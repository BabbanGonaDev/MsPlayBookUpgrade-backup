package com.babbangona.mspalybookupgrade.network;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;
import com.babbangona.mspalybookupgrade.data.db.entities.AppVariables;
import com.babbangona.mspalybookupgrade.data.db.entities.BGTCoaches;
import com.babbangona.mspalybookupgrade.data.db.entities.Category;
import com.babbangona.mspalybookupgrade.data.db.entities.ConfirmThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.Fields;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.HGList;
import com.babbangona.mspalybookupgrade.data.db.entities.HarvestLocationsTable;
import com.babbangona.mspalybookupgrade.data.db.entities.LastSyncTable;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.PCPWSActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.PWSActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.PWSActivityController;
import com.babbangona.mspalybookupgrade.data.db.entities.PWSCategoryList;
import com.babbangona.mspalybookupgrade.data.db.entities.PictureSync;
import com.babbangona.mspalybookupgrade.data.db.entities.RFActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.RFList;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.StaffList;
import com.babbangona.mspalybookupgrade.data.db.entities.SyncSummary;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.network.object.ActivityListDownload;
import com.babbangona.mspalybookupgrade.network.object.AppVariablesDownload;
import com.babbangona.mspalybookupgrade.network.object.BGTCoachesDownload;
import com.babbangona.mspalybookupgrade.network.object.CategoryDownload;
import com.babbangona.mspalybookupgrade.network.object.ConfirmThreshingActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.ConfirmThreshingActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.HGActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.HGActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.HGListDownload;
import com.babbangona.mspalybookupgrade.network.object.HarvestLocationDownload;
import com.babbangona.mspalybookupgrade.network.object.LogsDownload;
import com.babbangona.mspalybookupgrade.network.object.LogsUpload;
import com.babbangona.mspalybookupgrade.network.object.MsPlaybookInputDownload;
import com.babbangona.mspalybookupgrade.network.object.NormalActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.NormalActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.PCPWSActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.PCPWSActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.PWSActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.PWSActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.PWSActivityControllerDownload;
import com.babbangona.mspalybookupgrade.network.object.PWSCategoryListDownload;
import com.babbangona.mspalybookupgrade.network.object.RFActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.RFActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.RFListDownload;
import com.babbangona.mspalybookupgrade.network.object.ScheduledThreshingActivitiesFlagDownload;
import com.babbangona.mspalybookupgrade.network.object.ScheduledThreshingActivitiesUpload;
import com.babbangona.mspalybookupgrade.network.object.ServerResponse;
import com.babbangona.mspalybookupgrade.network.object.StaffListDownload;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
        getPWSCategoryList();
        getHGACtivitiesFlagDownload();
        getRFActivitiesFlagDownload();
        getNormalActivitiesFlagDownload();
        getPWSActivitiesFlagDownload();
        getPCPWSActivitiesFlagDownload();
        getCategoryDownload();
        getPWSActivitiesControllerDownload();
        getHarvestLocationDownload();
        getScheduledThreshingActivitiesFlagDownload();
        getConfirmThreshingActivitiesFlagDownload();
        getBGTCoachesDownload();
        getAppVariablesDownload();

        File ImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.MS_PLAYBOOK_PICTURE_LOCATION);

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
        if (appDatabase.pwsActivitiesFlagDao().getUnSyncedPWSActivitiesCount() > 0){
            syncUpPWSActivities();
        }
        if (appDatabase.pcpwsActivitiesFlagDao().getUnSyncedPCPWSActivitiesCount() > 0){
            syncUpPCPWSActivities();
        }
        if (appDatabase.scheduleThreshingActivitiesFlagDao().getUnSyncedScheduleThreshingActivitiesCount() > 0){
            syncUpScheduledThreshingActivities();
        }
        if (appDatabase.confirmThreshingActivitiesFlagDao().getUnSyncedConfirmThreshingActivitiesCount() > 0){
            syncUpConfirmThreshingActivities();
        }

        getStaffList();
        getLogsDownload();
        pictureLoop(ImgDirectory);
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
                            saveToFieldsTable(fieldsList);
                            List<String> member_list = new ArrayList<>();
                            for (Members members: membersList){
                                downloadPictures(members.getUnique_member_id());
                                sharedPrefs.setKeyProgressDialogStatus(0);
                            }


                            if (getStaffCountLastSync() > 0){
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
                    Toast.makeText(ActivityListDownloadService.this, "Syncing done", Toast.LENGTH_LONG).show();
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

    void downloadPictures(String member){
        if(ImageStorage.checkIfImageExists(member, DatabaseStringConstants.MS_PLAYBOOK_INPUT_PICTURE_LOCATION,ActivityListDownloadService.this)) {
            GetImages getImages = new GetImages(RetrofitClient.BASE_URL_FOR_PICTURES + "images/small_tfm_face/" + member + "_thumb.jpg",
                    member, DatabaseStringConstants.MS_PLAYBOOK_INPUT_PICTURE_LOCATION, ActivityListDownloadService.this);
            getImages.execute();
        }
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



    /**
     * This class handles the download of pictures of Trust group Leaders.
     */
    @SuppressLint("StaticFieldLeak")
    private static class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imageName, folder_name;
        //private ImageView view;
        private Bitmap bitmap ;
        private FileOutputStream fos;
        Context mCtx;

        private GetImages(String requestUrl, String image_name, String folder_name, Context context) {
            this.requestUrl = requestUrl;
            //this.view = view;
            this.imageName = image_name ;
            this.folder_name = folder_name ;
            this.mCtx = context;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {

                Log.d("logger","getting_here|");

                HttpURLConnection connection = null;
                InputStream is = null;
                ByteArrayOutputStream out = null;
                try {
                    connection = (HttpURLConnection) new URL(requestUrl).openConnection();
                    is = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (Throwable e) {
                    if (!this.isCancelled()) {
                        //error = new ImageError(e).setErrorCode(ImageError.ERROR_GENERAL_EXCEPTION);
                        this.cancel(true);
                    }
                } finally {
                    try {
                        if (connection != null)
                            connection.disconnect();
                        if (out != null) {
                            out.flush();
                            out.close();
                        }
                        if (is != null)
                            is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(ImageStorage.checkIfImageExists(imageName,folder_name,mCtx)) {
                //view.setImageBitmap(bitmap);

                String result = ImageStorage.saveToSdCard(bitmap, imageName,folder_name,mCtx);
                Log.d("storeResult",result+"");
                if (result == null){
                    Log.d("image_store","Null result");
                }else if (result.equalsIgnoreCase("fileExists")){
                    Log.d("image_store","Image did not save");
                }else if (result.equalsIgnoreCase("success")){
                    Log.d("image_store","Image saved");
                }
            }
        }
    }

    public static class ImageStorage {

        @SuppressWarnings("ResultOfMethodCallIgnored")
        static String saveToSdCard(Bitmap bitmap, String filename, String folder_name, Context context) {

            String stored = null;

            File ChkImgDirectory;
            String storageState = Environment.getExternalStorageState();
            if (storageState.equals(Environment.MEDIA_MOUNTED)) {
                ChkImgDirectory = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(), folder_name);

                File file, file3;
                File file1 = new File(ChkImgDirectory.getAbsoluteFile(), filename + "_thumb.jpg");
                File file2 = new File(ChkImgDirectory.getAbsoluteFile(), ".nomedia");
                if (!ChkImgDirectory.exists() && !ChkImgDirectory.mkdirs()) {
                    ChkImgDirectory.mkdir();
                    file = file1;
                    file3 = file2;
                    if (!file3.exists()){
                        try {
                            FileOutputStream outNoMedia = new FileOutputStream(file3);
                            outNoMedia.flush();
                            outNoMedia.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (file.exists()){
                        stored = "fileExists";
                    }else{
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                            stored = "success";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    file = file1;
                    file3 = file2;
                    if (!file3.exists()){
                        try {
                            FileOutputStream outNoMedia = new FileOutputStream(file3);
                            outNoMedia.flush();
                            outNoMedia.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (file.exists()){
                        stored = "fileExists";
                    }else{
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                            stored = "success";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            return stored;
        }

        static File getImage(String imageName, String folder_name, Context context) {

            File mediaImage = null;
            try {
                String root;
                root = Environment.getExternalStorageDirectory().getAbsoluteFile().toString();
                File myDir = new File(root);
                if (!myDir.exists())
                    return null;

                mediaImage = new File(myDir.getPath() + folder_name + imageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mediaImage;
        }

        static boolean checkIfImageExists(String imageName, String folder_name, Context context) {
            Bitmap b = null;
            File file = null;
            try {
                file = ImageStorage.getImage("/"+imageName+"_thumb.jpg","/"+folder_name,context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String path = null;
                if (file != null) {
                    path = file.getAbsolutePath();
                }
                if (path != null){
                    b = BitmapFactory.decodeFile(path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return b == null;
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

    //function to loop through files in folder
    private void pictureLoop(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        pictureLoop(file);
                    } else {

                        //Log.d("DIRect:",file.toString());


                        File file2 = new File(file.toString());

                        //Log.d("damiFile",file2.getName());
                        int check_if_picture_synced = appDatabase.pictureSyncDao().getPictureNameCount(file.getName());
                        if (check_if_picture_synced > 0) {
                            Log.d("pictureSynced", file.getName());
                        } else {
                            uploadFile(file2, file.getName());
                        }
                    }
                }
            }
        }
    }

    //upload file function
    private void uploadFile(File file, String name) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        // LmrApiInterface getResponse = LmrApiClient.getClient(LmrApiClient.BASE_URL).create(LmrApiClient.class);

        RetrofitInterface getResponse = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call call = getResponse.uploadMsPlaybookPicture(fileToUpload, filename);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                ServerResponse serverResponse = (ServerResponse) response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        //Log.d("ServerResponse","Successful "+ name);
                        //boolean deleted = file.delete();
                        //Log.d("result",String.valueOf(deleted));
                        SaveIntoDatabasePictures task = new SaveIntoDatabasePictures();
                        task.execute(name);
                    } else {
                        //Log.d("ServerResponse","Not_Successful "+ name);
                    }
                } else {
                    //Log.d("ServerResponse","Null Response");
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.d("ServerResponse","Error Uploading Files" + t.toString());
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class SaveIntoDatabasePictures extends AsyncTask<String, Void, Void> {


        private final String TAG = SaveIntoDatabasePictures.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            PictureSync pictureSync = new PictureSync(params[0]);

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.pictureSyncDao().insert(pictureSync);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getPWSCategoryList() {

        String last_synced = getLastSyncTimePWSCategoryList();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<PWSCategoryListDownload> call = retrofitInterface.getPWSCategoryList(last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<PWSCategoryListDownload>() {
            @Override
            public void onResponse(@NonNull Call<PWSCategoryListDownload> call,
                                   @NonNull Response<PWSCategoryListDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    PWSCategoryListDownload pwsCategoryListDownload = response.body();

                    if (pwsCategoryListDownload != null) {
                        List<PWSCategoryList> pwsCategoryLists = pwsCategoryListDownload.getDownload_list();
                        if (pwsCategoryLists.size() > 0){
                            saveToPWSCategoryListTable(pwsCategoryLists);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncPWSCategoryList(sharedPrefs.getStaffID(),pwsCategoryListDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_rf_list(pwsCategoryListDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.PWS_CATEGORY_LIST_TABLE,
                                "PWS Category List Download",
                                "1",
                                returnRemark(pwsCategoryLists.size()),
                                pwsCategoryListDownload.getLast_sync_time()

                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.PWS_CATEGORY_LIST_TABLE,
                                "PWS Category List Download",
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
                    saveToSyncSummary(DatabaseStringConstants.PWS_CATEGORY_LIST_TABLE,
                            "PWS Category List Download",
                            "0",
                            "Download Error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<PWSCategoryListDownload> call, @NotNull Throwable t) {
                Log.d("tobi_pws_category_list", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.PWS_CATEGORY_LIST_TABLE,
                        "PWS Category List Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToPWSCategoryListTable(List<PWSCategoryList> pwsCategoryLists){
        SavePWSCategoryListTable savePWSCategoryListTable = new SavePWSCategoryListTable();
        savePWSCategoryListTable.execute(pwsCategoryLists);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SavePWSCategoryListTable extends AsyncTask<List<PWSCategoryList>, Void, Void> {


        private final String TAG = SavePWSCategoryListTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<PWSCategoryList>... params) {

            List<PWSCategoryList> pwsCategoryLists = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.pwsCategoryListDao().insert(pwsCategoryLists);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    private void syncUpPWSActivities() {
        List<PWSActivitiesFlag> pwsActivitiesFlags = appDatabase.pwsActivitiesFlagDao().getUnSyncedPWSActivities();
        String composed_json_pws = new Gson().toJson(pwsActivitiesFlags);
        //Log.d("composed_json_pws",composed_json_pws);
        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<List<PWSActivitiesUpload>> call = retrofitInterface.uploadPWSActivitiesRecord(composed_json_pws, sharedPrefs.getStaffID());
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<List<PWSActivitiesUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<PWSActivitiesUpload>> call, @NonNull Response<List<PWSActivitiesUpload>> response) {
                //Log.d("RETROFIT_PWS_DATA", "onResponse: " + response.body());
                if (response.isSuccessful()) {

                    List<PWSActivitiesUpload> pwsActivitiesUploadList = response.body();

                    //Log.d("syncingResponseTFM", Objects.requireNonNull(rfActivitiesUploadList).toString());
                    if (pwsActivitiesUploadList == null){
                        Log.d("result", "null");
                        saveToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_FLAGS_TABLE+"_upload",
                                "PWS Activities Upload",
                                "0",
                                "Upload null",
                                "0000-00-00 00:00:00"
                        );
                    }else if(pwsActivitiesUploadList.size() == 0){
                        Log.d("result", "0");
                        saveToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_FLAGS_TABLE+"_upload",
                                "PWS Activities Upload",
                                "0",
                                "Upload empty",
                                "0000-00-00 00:00:00"
                        );
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < pwsActivitiesUploadList.size(); i++) {
                                PWSActivitiesUpload pwsActivitiesUpload = pwsActivitiesUploadList.get(i);
                                appDatabase.pwsActivitiesFlagDao().updatePWSActivitiesSyncFlags(pwsActivitiesUpload.getPws_id());
                            }
                            insetToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_FLAGS_TABLE+"_upload",
                                    "PWS Activities Upload",
                                    "1",
                                    returnRemark(pwsActivitiesUploadList.size()),
                                    pwsActivitiesUploadList.get(0).getLast_synced()

                            );
                        });
                        if (getStaffCountLastSync() > 0){
                            appDatabase.lastSyncTableDao().updateLastSyncUpPWSActivitiesFlag(sharedPrefs.getStaffID(),pwsActivitiesUploadList.get(0).getLast_synced());
                        }else{
                            LastSyncTable lastSyncTable = new LastSyncTable();
                            lastSyncTable.setLast_sync_up_pws_activities_flag(pwsActivitiesUploadList.get(0).getLast_synced());
                            lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                            appDatabase.lastSyncTableDao().insert(lastSyncTable);
                        }

                    }
                } else {
                    Log.i("tobi_pws_upload ", "onResponse ERROR: " + response.body());
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
                    saveToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_FLAGS_TABLE+"_upload",
                            "PWS Activities Upload",
                            "0",
                            "Upload error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NonNull Call<List<PWSActivitiesUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewPWSUpload", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_FLAGS_TABLE+"_upload",
                        "PWS Activities Upload",
                        "0",
                        "Upload failed",
                        "0000-00-00 00:00:00"
                );
            }
        });
    }

    private void syncUpPCPWSActivities() {
        List<PCPWSActivitiesFlag> pcpwsActivitiesFlags = appDatabase.pcpwsActivitiesFlagDao().getUnSyncedPCPWSActivities();
        String composed_json = new Gson().toJson(pcpwsActivitiesFlags);
        Log.d("composed_json",composed_json);
        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<List<PCPWSActivitiesUpload>> call = retrofitInterface.uploadPCPWSActivitiesRecord(composed_json, sharedPrefs.getStaffID());
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<List<PCPWSActivitiesUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<PCPWSActivitiesUpload>> call, @NonNull Response<List<PCPWSActivitiesUpload>> response) {
                //Log.d("RETROFIT_TFM_DATA", "onResponse: " + response.body());
                if (response.isSuccessful()) {

                    List<PCPWSActivitiesUpload> pcpwsActivitiesUploadList = response.body();

                    //Log.d("syncingResponseTFM", Objects.requireNonNull(rfActivitiesUploadList).toString());
                    if (pcpwsActivitiesUploadList == null){
                        Log.d("result", "null");
                        saveToSyncSummary(DatabaseStringConstants.PC_PWS_ACTIVITY_FLAGS_TABLE+"_upload",
                                "PC PWS Activities Upload",
                                "0",
                                "Upload null",
                                "0000-00-00 00:00:00"
                        );
                    }else if(pcpwsActivitiesUploadList.size() == 0){
                        Log.d("result", "0");
                        saveToSyncSummary(DatabaseStringConstants.PC_PWS_ACTIVITY_FLAGS_TABLE+"_upload",
                                "PC PWS Activities Upload",
                                "0",
                                "Upload empty",
                                "0000-00-00 00:00:00"
                        );
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < pcpwsActivitiesUploadList.size(); i++) {
                                PCPWSActivitiesUpload pcpwsActivitiesUpload = pcpwsActivitiesUploadList.get(i);
                                appDatabase.pcpwsActivitiesFlagDao().updatePCPWSActivitiesSyncFlags(pcpwsActivitiesUpload.getPws_id());
                            }
                            insetToSyncSummary(DatabaseStringConstants.PC_PWS_ACTIVITY_FLAGS_TABLE+"_upload",
                                    "PC PWS Activities Upload",
                                    "1",
                                    returnRemark(pcpwsActivitiesUploadList.size()),
                                    pcpwsActivitiesUploadList.get(0).getLast_synced()

                            );
                        });
                        if (getStaffCountLastSync() > 0){
                            appDatabase.lastSyncTableDao().updateLastSyncUpPCPWSActivitiesFlag(sharedPrefs.getStaffID(),pcpwsActivitiesUploadList.get(0).getLast_synced());
                        }else{
                            LastSyncTable lastSyncTable = new LastSyncTable();
                            lastSyncTable.setLast_sync_up_pc_pws_activities_flag(pcpwsActivitiesUploadList.get(0).getLast_synced());
                            lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                            appDatabase.lastSyncTableDao().insert(lastSyncTable);
                        }

                    }
                } else {
                    Log.i("tobi_pc_pws_upload ", "onResponse ERROR: " + response.body());
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
                    saveToSyncSummary(DatabaseStringConstants.PC_PWS_ACTIVITY_FLAGS_TABLE+"_upload",
                            "PC PWS Activities Upload",
                            "0",
                            "Upload error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NonNull Call<List<PCPWSActivitiesUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewPCPWSUpload", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.PC_PWS_ACTIVITY_FLAGS_TABLE+"_upload",
                        "PC PWS Activities Upload",
                        "0",
                        "Upload failed",
                        "0000-00-00 00:00:00"
                );
            }
        });
    }

    public void getPWSActivitiesFlagDownload() {

        String last_synced = getLastSyncTimePWSActivities();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<PWSActivitiesFlagDownload> call = retrofitInterface.downloadPWSActivityFlag(sharedPrefs.getStaffID(),portfolioToGson(sharedPrefs.getKeyPortfolioList()),last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<PWSActivitiesFlagDownload>() {
            @Override
            public void onResponse(@NonNull Call<PWSActivitiesFlagDownload> call,
                                   @NonNull Response<PWSActivitiesFlagDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    PWSActivitiesFlagDownload pwsActivitiesFlagDownload = response.body();

                    if (pwsActivitiesFlagDownload != null) {
                        List<PWSActivitiesFlag> pwsActivitiesFlagList = pwsActivitiesFlagDownload.getDownload_list();
                        if (pwsActivitiesFlagList.size() > 0){
                            saveToPWSActivitiesFlagTable(pwsActivitiesFlagList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncDownNormalActivitiesFlag(sharedPrefs.getStaffID(),pwsActivitiesFlagDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_down_normal_activities_flag(pwsActivitiesFlagDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_FLAGS_TABLE+"_download",
                                "PWS Activities Download",
                                "1",
                                returnRemark(pwsActivitiesFlagList.size()),
                                pwsActivitiesFlagDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_FLAGS_TABLE+"_download",
                                "PWS Activities Download",
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
                    saveToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_FLAGS_TABLE+"_download",
                            "PWS Activities Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<PWSActivitiesFlagDownload> call, @NotNull Throwable t) {
                Log.d("tobi_pws_download", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_FLAGS_TABLE+"_download",
                        "PWS Activities Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToPWSActivitiesFlagTable(List<PWSActivitiesFlag> pwsActivitiesFlags){
        SavePWSActivitiesFlagTable savePWSActivitiesFlagTable = new SavePWSActivitiesFlagTable();
        savePWSActivitiesFlagTable.execute(pwsActivitiesFlags);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SavePWSActivitiesFlagTable extends AsyncTask<List<PWSActivitiesFlag>, Void, Void> {


        private final String TAG = SavePWSActivitiesFlagTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<PWSActivitiesFlag>... params) {

            List<PWSActivitiesFlag> pwsActivitiesFlags = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.pwsActivitiesFlagDao().insert(pwsActivitiesFlags);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getPCPWSActivitiesFlagDownload() {

        String last_synced = getLastSyncTimePCPWSActivities();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<PCPWSActivitiesFlagDownload> call = retrofitInterface.downloadPCPWSActivityFlag(sharedPrefs.getStaffID(),portfolioToGson(sharedPrefs.getKeyPortfolioList()),last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<PCPWSActivitiesFlagDownload>() {
            @Override
            public void onResponse(@NonNull Call<PCPWSActivitiesFlagDownload> call,
                                   @NonNull Response<PCPWSActivitiesFlagDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    PCPWSActivitiesFlagDownload pcpwsActivitiesFlagDownload = response.body();

                    if (pcpwsActivitiesFlagDownload != null) {
                        List<PCPWSActivitiesFlag> pcpwsActivitiesFlagList = pcpwsActivitiesFlagDownload.getDownload_list();
                        if (pcpwsActivitiesFlagList.size() > 0){
                            saveToPCPWSActivitiesFlagTable(pcpwsActivitiesFlagList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncDownPCPWSActivitiesFlag(sharedPrefs.getStaffID(),pcpwsActivitiesFlagDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_down_pc_pws_activities_flag(pcpwsActivitiesFlagDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.PC_PWS_ACTIVITY_FLAGS_TABLE+"_download",
                                "PC PWS Activities Download",
                                "1",
                                returnRemark(pcpwsActivitiesFlagList.size()),
                                pcpwsActivitiesFlagDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.PC_PWS_ACTIVITY_FLAGS_TABLE+"_download",
                                "PC PWS Activities Download",
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
                    saveToSyncSummary(DatabaseStringConstants.PC_PWS_ACTIVITY_FLAGS_TABLE+"_download",
                            "PC PWS Activities Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<PCPWSActivitiesFlagDownload> call, @NotNull Throwable t) {
                Log.d("tobi_pc_pws_download", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.PC_PWS_ACTIVITY_FLAGS_TABLE+"_download",
                        "PC PWS Activities Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToPCPWSActivitiesFlagTable(List<PCPWSActivitiesFlag> pcpwsActivitiesFlags){
        SavePCPWSActivitiesFlagTable savePCPWSActivitiesFlagTable = new SavePCPWSActivitiesFlagTable();
        savePCPWSActivitiesFlagTable.execute(pcpwsActivitiesFlags);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SavePCPWSActivitiesFlagTable extends AsyncTask<List<PCPWSActivitiesFlag>, Void, Void> {


        private final String TAG = SavePCPWSActivitiesFlagTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<PCPWSActivitiesFlag>... params) {

            List<PCPWSActivitiesFlag> pcpwsActivitiesFlags = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.pcpwsActivitiesFlagDao().insert(pcpwsActivitiesFlags);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getPWSActivitiesControllerDownload() {

        String last_synced = getLastSyncPWSActivitiesController();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<PWSActivityControllerDownload> call = retrofitInterface.downloadPWSActivityController(last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<PWSActivityControllerDownload>() {
            @Override
            public void onResponse(@NonNull Call<PWSActivityControllerDownload> call,
                                   @NonNull Response<PWSActivityControllerDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    PWSActivityControllerDownload pwsActivityControllerDownload = response.body();

                    if (pwsActivityControllerDownload != null) {
                        List<PWSActivityController> pwsActivityControllerList = pwsActivityControllerDownload.getDownload_list();
                        if (pwsActivityControllerList.size() > 0){
                            saveToPWSActivityControllerTable(pwsActivityControllerList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncPWSActivitiesController(sharedPrefs.getStaffID(),pwsActivityControllerDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_pws_activities_controller(pwsActivityControllerDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_CONTROLLER_TABLE,
                                "PWS Activity Controller List Download",
                                "1",
                                returnRemark(pwsActivityControllerList.size()),
                                pwsActivityControllerDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_CONTROLLER_TABLE,
                                "PWS Activity Controller List Download",
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
                    saveToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_CONTROLLER_TABLE,
                            "PWS Activity Controller List Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<PWSActivityControllerDownload> call, @NotNull Throwable t) {
                Log.d("tobi_staff_list", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.PWS_ACTIVITY_CONTROLLER_TABLE,
                        "PWS Activity Controller List Download",
                        "0",
                        "Download empty",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToPWSActivityControllerTable(List<PWSActivityController> pwsActivityControllerList){
        SavePWSActivityControllerTable savePWSActivityControllerTable = new SavePWSActivityControllerTable();
        savePWSActivityControllerTable.execute(pwsActivityControllerList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SavePWSActivityControllerTable extends AsyncTask<List<PWSActivityController>, Void, Void> {


        private final String TAG = SaveCategoryTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<PWSActivityController>... params) {

            List<PWSActivityController> pwsActivityControllerList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.pwsActivityControllerDao().insert(pwsActivityControllerList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getScheduledThreshingActivitiesFlagDownload() {

        String last_synced = getLastSyncScheduledThreshingActivities();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<ScheduledThreshingActivitiesFlagDownload> call = retrofitInterface.downloadScheduledThreshingActivityFlag(sharedPrefs.getStaffID(),portfolioToGson(sharedPrefs.getKeyPortfolioList()),last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<ScheduledThreshingActivitiesFlagDownload>() {
            @Override
            public void onResponse(@NonNull Call<ScheduledThreshingActivitiesFlagDownload> call,
                                   @NonNull Response<ScheduledThreshingActivitiesFlagDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    ScheduledThreshingActivitiesFlagDownload scheduledThreshingActivitiesFlagDownload = response.body();

                    if (scheduledThreshingActivitiesFlagDownload != null) {
                        List<ScheduledThreshingActivitiesFlag> scheduledThreshingActivitiesFlagList = scheduledThreshingActivitiesFlagDownload.getDownload_list();
                        if (scheduledThreshingActivitiesFlagList.size() > 0){
                            saveToScheduledThreshingActivitiesTable(scheduledThreshingActivitiesFlagList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncDownScheduledThreshingActivitiesFlag(sharedPrefs.getStaffID(),scheduledThreshingActivitiesFlagDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_down_scheduled_activities_flag(scheduledThreshingActivitiesFlagDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE+"_download",
                                "Scheduled Activities Download",
                                "1",
                                returnRemark(scheduledThreshingActivitiesFlagList.size()),
                                scheduledThreshingActivitiesFlagDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE+"_download",
                                "Scheduled Activities Download",
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
                    saveToSyncSummary(DatabaseStringConstants.SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE+"_download",
                            "Scheduled Activities Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<ScheduledThreshingActivitiesFlagDownload> call, @NotNull Throwable t) {
                Log.d("tobi_scheduled_download", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE+"_download",
                        "Scheduled Activities Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToScheduledThreshingActivitiesTable(List<ScheduledThreshingActivitiesFlag> scheduledThreshingActivitiesFlagList){
        SaveScheduledThreshingActivitiesFlagTable saveScheduledThreshingActivitiesFlagTable = new SaveScheduledThreshingActivitiesFlagTable();
        saveScheduledThreshingActivitiesFlagTable.execute(scheduledThreshingActivitiesFlagList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveScheduledThreshingActivitiesFlagTable extends AsyncTask<List<ScheduledThreshingActivitiesFlag>, Void, Void> {


        private final String TAG = SaveScheduledThreshingActivitiesFlagTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<ScheduledThreshingActivitiesFlag>... params) {

            List<ScheduledThreshingActivitiesFlag> scheduledThreshingActivitiesFlagList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.scheduleThreshingActivitiesFlagDao().insert(scheduledThreshingActivitiesFlagList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getConfirmThreshingActivitiesFlagDownload() {

        String last_synced = getLastSyncConfirmThreshingActivities();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<ConfirmThreshingActivitiesFlagDownload> call = retrofitInterface.downloadConfirmThreshingActivityFlag(sharedPrefs.getStaffID(),portfolioToGson(sharedPrefs.getKeyPortfolioList()),last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        Log.d("CHECK", "download confirm threshing: " + sharedPrefs.getStaffID() + " == " + portfolioToGson(sharedPrefs.getKeyPortfolioList()) + " == " + last_synced);
        call.enqueue(new Callback<ConfirmThreshingActivitiesFlagDownload>() {
            @Override
            public void onResponse(@NonNull Call<ConfirmThreshingActivitiesFlagDownload> call,
                                   @NonNull Response<ConfirmThreshingActivitiesFlagDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    ConfirmThreshingActivitiesFlagDownload confirmThreshingActivitiesFlagDownload = response.body();

                    if (confirmThreshingActivitiesFlagDownload != null) {
                        List<ConfirmThreshingActivitiesFlag> confirmThreshingActivitiesFlagList = confirmThreshingActivitiesFlagDownload.getDownload_list();
                        if (confirmThreshingActivitiesFlagList.size() > 0){
                            saveToConfirmThreshingActivitiesTable(confirmThreshingActivitiesFlagList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncDownConfirmThreshingActivitiesFlag(sharedPrefs.getStaffID(),confirmThreshingActivitiesFlagDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_down_confirm_activities_flag(confirmThreshingActivitiesFlagDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE+"_download",
                                "Confirm Activities Download",
                                "1",
                                returnRemark(confirmThreshingActivitiesFlagList.size()),
                                confirmThreshingActivitiesFlagDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE+"_download",
                                "Confirm Activities Download",
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
                    saveToSyncSummary(DatabaseStringConstants.CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE+"_download",
                            "Confirm Activities Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<ConfirmThreshingActivitiesFlagDownload> call, @NotNull Throwable t) {
                Log.d("tobi_confirm_download", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE+"_download",
                        "Confirm Activities Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToConfirmThreshingActivitiesTable(List<ConfirmThreshingActivitiesFlag> confirmThreshingActivitiesFlagList){
        SaveConfirmThreshingActivitiesFlagTable saveConfirmThreshingActivitiesFlagTable = new SaveConfirmThreshingActivitiesFlagTable();
        saveConfirmThreshingActivitiesFlagTable.execute(confirmThreshingActivitiesFlagList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveConfirmThreshingActivitiesFlagTable extends AsyncTask<List<ConfirmThreshingActivitiesFlag>, Void, Void> {


        private final String TAG = SaveConfirmThreshingActivitiesFlagTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<ConfirmThreshingActivitiesFlag>... params) {

            List<ConfirmThreshingActivitiesFlag> confirmThreshingActivitiesFlagList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.confirmThreshingActivitiesFlagDao().insert(confirmThreshingActivitiesFlagList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    public void getBGTCoachesDownload() {

        String last_synced = getLastSyncBGTCoaches();

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<BGTCoachesDownload> call = retrofitInterface.downloadBGTCoaches(last_synced);
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<BGTCoachesDownload>() {
            @Override
            public void onResponse(@NonNull Call<BGTCoachesDownload> call,
                                   @NonNull Response<BGTCoachesDownload> response) {
                //Log.d("tobiRes", ""+ Objects.requireNonNull(response.body()).toString());
                if (response.isSuccessful()) {
                    BGTCoachesDownload bgtCoachesDownload = response.body();

                    if (bgtCoachesDownload != null) {
                        List<BGTCoaches> bgtCoachesList = bgtCoachesDownload.getDownload_list();
                        if (bgtCoachesList.size() > 0){
                            saveToBGTCoachesTable(bgtCoachesList);
                            if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncBGTCoaches(sharedPrefs.getStaffID(),bgtCoachesDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_bgt_coaches(bgtCoachesDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                        insetToSyncSummary(DatabaseStringConstants.BGT_COACHES_TABLE+"_download",
                                "BGT Coaches Download",
                                "1",
                                returnRemark(bgtCoachesList.size()),
                                bgtCoachesDownload.getLast_sync_time()
                        );
                    }else{
                        saveToSyncSummary(DatabaseStringConstants.BGT_COACHES_TABLE+"_download",
                                "BGT Coaches Download",
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
                    saveToSyncSummary(DatabaseStringConstants.BGT_COACHES_TABLE+"_download",
                            "BGT Coaches Download",
                            "0",
                            "Download error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<BGTCoachesDownload> call, @NotNull Throwable t) {
                Log.d("tobi_bgt_coaches_download", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.BGT_COACHES_TABLE+"_download",
                        "BGT Coaches Download",
                        "0",
                        "Download failed",
                        "0000-00-00 00:00:00"
                );
            }
        });

    }

    void saveToBGTCoachesTable(List<BGTCoaches> bgtCoachesList){
        SaveBGTCoachesTable saveBGTCoachesTable = new SaveBGTCoachesTable();
        saveBGTCoachesTable.execute(bgtCoachesList);
    }

    /**
     * This AsyncTask carries out saving to database the downloaded data by calling a database helper method
     * This background thread is required as the volume of data is pretty much high
     */
    @SuppressLint("StaticFieldLeak")
    public class SaveBGTCoachesTable extends AsyncTask<List<BGTCoaches>, Void, Void> {


        private final String TAG = SaveBGTCoachesTable.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<BGTCoaches>... params) {

            List<BGTCoaches> bgtCoachesList = params[0];

            try {
                appDatabase = AppDatabase.getInstance(ActivityListDownloadService.this);
                appDatabase.bgtCoachesDao().insert(bgtCoachesList);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

    private void syncUpScheduledThreshingActivities() {
        List<ScheduledThreshingActivitiesFlag> scheduledThreshingActivitiesFlagList = appDatabase.scheduleThreshingActivitiesFlagDao().getUnSyncedScheduleThreshingActivities();
        String composed_json = new Gson().toJson(scheduledThreshingActivitiesFlagList);
        Log.d("CHECK", "ScheduledThreshingActivities: " + composed_json);
        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<List<ScheduledThreshingActivitiesUpload>> call = retrofitInterface.uploadScheduledThreshingActivitiesRecord(composed_json, sharedPrefs.getStaffID());
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<List<ScheduledThreshingActivitiesUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<ScheduledThreshingActivitiesUpload>> call, @NonNull Response<List<ScheduledThreshingActivitiesUpload>> response) {
                if (response.isSuccessful()) {

                    List<ScheduledThreshingActivitiesUpload> scheduledThreshingActivitiesUploadList = response.body();
                    if (scheduledThreshingActivitiesUploadList == null){
                        Log.d("result", "null");
                        saveToSyncSummary(DatabaseStringConstants.SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE+"_upload",
                                "Scheduled Activities Upload",
                                "0",
                                "Upload null",
                                "0000-00-00 00:00:00"
                        );
                    }else if(scheduledThreshingActivitiesUploadList.size() == 0){
                        Log.d("result", "0");
                        saveToSyncSummary(DatabaseStringConstants.SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE+"_upload",
                                "Scheduled Activities Upload",
                                "0",
                                "Upload empty",
                                "0000-00-00 00:00:00"
                        );
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < scheduledThreshingActivitiesUploadList.size(); i++) {
                                ScheduledThreshingActivitiesUpload scheduledThreshingActivitiesUpload = scheduledThreshingActivitiesUploadList.get(i);
                                appDatabase.scheduleThreshingActivitiesFlagDao().updateScheduledThreshingActivitiesSyncFlag(scheduledThreshingActivitiesUpload.getUnique_field_id());
                                insetToSyncSummary(DatabaseStringConstants.SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE+"_upload",
                                        "Scheduled Activities Upload",
                                        "1",
                                        returnRemark(scheduledThreshingActivitiesUploadList.size()),
                                        scheduledThreshingActivitiesUploadList.get(0).getLast_synced()

                                );
                            }
                        });
                        if (getStaffCountLastSync() > 0){
                            appDatabase.lastSyncTableDao().updateLastSyncUpScheduleActivitiesFlag(sharedPrefs.getStaffID(),scheduledThreshingActivitiesUploadList.get(0).getLast_synced());
                        }else{
                            LastSyncTable lastSyncTable = new LastSyncTable();
                            lastSyncTable.setLast_sync_up_scheduled_activities_flag(scheduledThreshingActivitiesUploadList.get(0).getLast_synced());
                            lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                            appDatabase.lastSyncTableDao().insert(lastSyncTable);
                        }

                    }
                } else {
                    Log.i("tobi_schedule", "onResponse ERROR: " + response.body());
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
                    saveToSyncSummary(DatabaseStringConstants.SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE+"_upload",
                            "Scheduled Activities Upload",
                            "0",
                            "Upload error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NonNull Call<List<ScheduledThreshingActivitiesUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewScheduleActivities", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE+"_upload",
                        "Scheduled Activities Upload",
                        "0",
                        "Upload failed",
                        "0000-00-00 00:00:00"
                );
            }
        });
    }

    private void syncUpConfirmThreshingActivities() {
        List<ConfirmThreshingActivitiesFlag> confirmThreshingActivitiesFlagList = appDatabase.confirmThreshingActivitiesFlagDao().getUnSyncedConfirmThreshingActivities();
        String composed_json = new Gson().toJson(confirmThreshingActivitiesFlagList);
        Log.d("CHECK", "ConfirmedThreshingActivities: " + composed_json);
        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<List<ConfirmThreshingActivitiesUpload>> call = retrofitInterface.uploadConfirmThreshingActivitiesRecord(composed_json, sharedPrefs.getStaffID());
        sharedPrefs.setKeyProgressDialogStatus(0);
        call.enqueue(new Callback<List<ConfirmThreshingActivitiesUpload>>() {
            @Override
            public void onResponse(@NonNull Call<List<ConfirmThreshingActivitiesUpload>> call, @NonNull Response<List<ConfirmThreshingActivitiesUpload>> response) {
                if (response.isSuccessful()) {

                    List<ConfirmThreshingActivitiesUpload> confirmThreshingActivitiesUploadList = response.body();
                    if (confirmThreshingActivitiesUploadList == null){
                        Log.d("result", "null");
                        saveToSyncSummary(DatabaseStringConstants.CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE+"_upload",
                                "Confirm Activities Upload",
                                "0",
                                "Upload null",
                                "0000-00-00 00:00:00"
                        );
                    }else if(confirmThreshingActivitiesUploadList.size() == 0){
                        Log.d("result", "0");
                        saveToSyncSummary(DatabaseStringConstants.CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE+"_upload",
                                "Confirm Activities Upload",
                                "0",
                                "Upload empty",
                                "0000-00-00 00:00:00"
                        );
                    }else {
                        AsyncTask.execute(()->{
                            for (int i = 0; i < confirmThreshingActivitiesUploadList.size(); i++) {
                                ConfirmThreshingActivitiesUpload confirmThreshingActivitiesUpload = confirmThreshingActivitiesUploadList.get(i);
                                appDatabase.confirmThreshingActivitiesFlagDao().updateConfirmThreshingActivitiesSyncFlag(confirmThreshingActivitiesUpload.getUnique_field_id());
                                insetToSyncSummary(DatabaseStringConstants.CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE+"_upload",
                                        "Confirm Activities Upload",
                                        "1",
                                        returnRemark(confirmThreshingActivitiesUploadList.size()),
                                        confirmThreshingActivitiesUploadList.get(0).getLast_synced()

                                );
                            }
                        });
                        if (getStaffCountLastSync() > 0){
                            appDatabase.lastSyncTableDao().updateLastSyncUpScheduleActivitiesFlag(sharedPrefs.getStaffID(),confirmThreshingActivitiesUploadList.get(0).getLast_synced());
                        }else{
                            LastSyncTable lastSyncTable = new LastSyncTable();
                            lastSyncTable.setLast_sync_up_confirm_activities_flag(confirmThreshingActivitiesUploadList.get(0).getLast_synced());
                            lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                            appDatabase.lastSyncTableDao().insert(lastSyncTable);
                        }

                    }
                } else {
                    Log.i("tobi_confirm", "onResponse ERROR: " + response.body());
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
                    saveToSyncSummary(DatabaseStringConstants.CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE+"_upload",
                            "Confirm Activities Upload",
                            "0",
                            "Upload error",
                            "0000-00-00 00:00:00"
                    );
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NonNull Call<List<ConfirmThreshingActivitiesUpload>> call, @NonNull Throwable t) {
                Log.d("TobiNewScheduleActivities", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
                saveToSyncSummary(DatabaseStringConstants.CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE+"_upload",
                        "Confirm Activities Upload",
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

    String getLastSyncTimePWSCategoryList(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncPWSCategoryList(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncTimePWSActivities(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncDownPWSActivitiesFlag(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncTimePCPWSActivities(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncDownPCPWSActivitiesFlag(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncPWSActivitiesController(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncPWSActivitiesController(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncScheduledThreshingActivities(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncDownScheduledThreshingActivitiesFlag(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncConfirmThreshingActivities(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncDownConfirmThreshingActivitiesFlag(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2019-01-01 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2019-01-01 00:00:00";
        }
        return last_sync_time;
    }

    String getLastSyncBGTCoaches(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncBGTCoaches(sharedPrefs.getStaffID());
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
package com.babbangona.mspalybookupgrade.network;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.Fields;
import com.babbangona.mspalybookupgrade.data.db.entities.LastSyncTable;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.network.object.MsPlaybookInputDownload;
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
public class MsPlaybookInputDataDownloadService extends IntentService {

    RetrofitInterface retrofitInterface;
    SharedPrefs sharedPrefs;
    AppDatabase appDatabase;
    SetPortfolioMethods setPortfolioMethods;

    public MsPlaybookInputDataDownloadService() {
        // Used to name the worker thread, important only for debugging.
        super("test-service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPrefs = new SharedPrefs(getApplicationContext());
        setPortfolioMethods = new SetPortfolioMethods();
        appDatabase = AppDatabase.getInstance(getApplicationContext());

        // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getInputData();
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
                            saveToMembersTable(membersList);if (getStaffCountLastSync() > 0){
                                appDatabase.lastSyncTableDao().updateLastSyncMembers(sharedPrefs.getStaffID(),msPlaybookInputDownload.getLast_sync_time());
                            }else{
                                LastSyncTable lastSyncTable = new LastSyncTable();
                                lastSyncTable.setLast_sync_members(msPlaybookInputDownload.getLast_sync_time());
                                lastSyncTable.setStaff_id(sharedPrefs.getStaffID());
                                appDatabase.lastSyncTableDao().insert(lastSyncTable);
                            }
                        }
                    }
                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            //Toasst.makeText(MsPlaybookInputDataDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            //Toasst.makeText(MsPlaybookInputDataDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            //Toasst.makeText(MsPlaybookInputDataDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                }
                sharedPrefs.setKeyProgressDialogStatus(1);
            }

            @Override
            public void onFailure(@NotNull Call<MsPlaybookInputDownload> call, @NotNull Throwable t) {
                Log.d("tobi_check_list", t.toString());
                sharedPrefs.setKeyProgressDialogStatus(1);
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
                appDatabase = AppDatabase.getInstance(MsPlaybookInputDataDownloadService.this);
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
                appDatabase = AppDatabase.getInstance(MsPlaybookInputDataDownloadService.this);
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

}
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
import com.babbangona.mspalybookupgrade.network.object.ActivityListDownload;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class MsPlayBookDownloadService extends IntentService {

    RetrofitInterface retrofitInterface;
    AppDatabase appDatabase;

    public MsPlayBookDownloadService() {
        // Used to name the worker thread, important only for debugging.
        super("test-service");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // if you override onCreate(), make sure to call super().
        // If a Context object is needed, call getApplicationContext() here.
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getActivityList();
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
                            Toast.makeText(MsPlayBookDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            Toast.makeText(MsPlayBookDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            Toast.makeText(MsPlayBookDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
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
                appDatabase = AppDatabase.getInstance(MsPlayBookDownloadService.this);
                appDatabase.activityListDao().insert(activityLists);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

}
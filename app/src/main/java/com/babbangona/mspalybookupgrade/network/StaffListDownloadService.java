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
import com.babbangona.mspalybookupgrade.data.db.entities.StaffList;
import com.babbangona.mspalybookupgrade.network.object.ActivityListDownload;
import com.babbangona.mspalybookupgrade.network.object.StaffListDownload;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("ALL")
public class StaffListDownloadService extends IntentService {

    RetrofitInterface retrofitInterface;
    AppDatabase appDatabase;

    public StaffListDownloadService() {
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
        getStaffList();
    }

    public void getStaffList() {

        String last_synced = "2019-01-01 00:00:00";

        retrofitInterface = RetrofitClient.getApiClient().create(RetrofitInterface.class);
        Call<StaffListDownload> call = retrofitInterface.getStaffListDownload(last_synced);

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
                        }
                    }

                }else {
                    int sc = response.code();
                    Log.d("scCode:- ",""+sc);
                    switch (sc) {
                        case 400:
                            Log.e("Error 400", "Bad Request");
                            Toast.makeText(StaffListDownloadService.this, "Error 400: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        case 404:
                            Log.e("Error 404", "Not Found");
                            Toast.makeText(StaffListDownloadService.this, "Error 404: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Log.e("Error", "Generic Error");
                            Toast.makeText(StaffListDownloadService.this, "Error: Network Error Please Reconnect", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void onFailure(@NotNull Call<StaffListDownload> call, @NotNull Throwable t) {
                Log.d("tobi_staff_list", t.toString());

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
                appDatabase = AppDatabase.getInstance(StaffListDownloadService.this);
                appDatabase.staffListDao().insert(staffLists);
            } catch (Exception e) {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            }

            return null;
        }
    }

}
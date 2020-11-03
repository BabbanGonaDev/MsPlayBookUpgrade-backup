package com.babbangona.mspalybookupgrade.LocationTraker.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.babbangona.mspalybookupgrade.LocationTraker.api.ApiClient;
import com.babbangona.mspalybookupgrade.LocationTraker.api.ApiInterface;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.google.gson.Gson;

public abstract class SyncWorker extends Worker {


    public String ErrorMessage = "";
    public Gson gson;
    public Context context;
    public ApiInterface apiInterface;
    public AppDatabase dataBase;

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        gson = new Gson();
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        dataBase = AppDatabase.getInstance(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        return doSyncWork();
    }

    public abstract Result doSyncWork();

    public abstract String getErrorString(String Message);
}

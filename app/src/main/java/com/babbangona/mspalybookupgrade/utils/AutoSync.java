package com.babbangona.mspalybookupgrade.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.network.ActivityListDownloadService;

public class AutoSync extends Worker {

    SharedPrefs sharedPrefs;

    public AutoSync(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        sharedPrefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("got here", "auto_sync");
        Intent i = new Intent(getApplicationContext(), ActivityListDownloadService.class);
        sharedPrefs.setKeyAutoSyncFlag(DatabaseStringConstants.SYNC_TYPE_AUTO);
        getApplicationContext().startService(i);
        return Result.success();
    }
}

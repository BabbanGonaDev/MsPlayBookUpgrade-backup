package com.babbangona.mspalybookupgrade.transporter.services;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;

public class SyncDownWorker extends Worker {
    private TSessionManager session;
    private TransporterDatabase db;

    /**
     * This would be used to sync down the following tables:
     * - Transporter table
     * - Collection center table &
     * - Operating areas table.
     */


    public SyncDownWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        session = new TSessionManager(context);
        db = TransporterDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        return null;
    }
}

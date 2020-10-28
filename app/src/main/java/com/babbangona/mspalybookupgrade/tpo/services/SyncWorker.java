package com.babbangona.mspalybookupgrade.tpo.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.tpo.data.TPOSessionManager;
import com.babbangona.mspalybookupgrade.tpo.data.models.DeliveryDownload;
import com.babbangona.mspalybookupgrade.tpo.data.retrofit.RetrofitApiCalls;
import com.babbangona.mspalybookupgrade.tpo.data.retrofit.RetrofitClient;
import com.babbangona.mspalybookupgrade.tpo.data.room.TPODatabase;
import com.babbangona.mspalybookupgrade.tpo.data.room.tables.DeliveryAttendance;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncWorker extends Worker {
    private static final String CHANNEL_ID = "TPO_REFRESH";
    private static final int NOTIFICATION_ID = 278;
    private NotificationCompat.Builder builder;
    private NotificationManager mNotifyManager;
    private int CURRENT;
    private int MAX = 2;

    private TPOSessionManager session;
    private TPODatabase db;

    /**
     * This worker would be used to handle the following:
     * Sync up of delivery attendance table
     * Sync down of delivery attendance table.
     */

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        session = new TPOSessionManager(context);
        db = TPODatabase.getInstance(context);
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override
    public Result doWork() {
        CURRENT = 0;
        setNotification();

        syncUpDeliveryAttendanceTable();
        syncDownDeliveryAttendanceTable();

        return Result.success();
    }

    public void syncDownDeliveryAttendanceTable() {
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<DeliveryDownload>> call = service.syncDownDeliveryAttendance(session.GET_LAST_SYNC_TIME());
        call.enqueue(new Callback<List<DeliveryDownload>>() {
            @Override
            public void onResponse(Call<List<DeliveryDownload>> call, Response<List<DeliveryDownload>> response) {
                if (response.isSuccessful()) {
                    List<DeliveryDownload> res = response.body();

                    if (res != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (DeliveryDownload d : res) {
                                db.getDeliveryAttendanceDao().insert(new DeliveryAttendance(d.getUnique_member_id(),
                                        d.getIk_number(),
                                        d.getCc_id(),
                                        d.getQty_delivered(),
                                        d.getImei(),
                                        d.getApp_version(),
                                        d.getDate_delivered(),
                                        1));
                                session.SET_LAST_SYNC_TIME(d.getLast_sync_time());
                            }
                        });
                    }
                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<DeliveryDownload>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync down Delivery table: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncUpDeliveryAttendanceTable() {
        String unSynced = new Gson().toJson(db.getDeliveryAttendanceDao().getUnSyncedDelivery());
        Log.d("CHECK", "TPO Records to sync: " + unSynced);
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<DeliveryAttendance.Response>> call = service.syncUpDeliveryAttendance(unSynced);
        call.enqueue(new Callback<List<DeliveryAttendance.Response>>() {
            @Override
            public void onResponse(Call<List<DeliveryAttendance.Response>> call, Response<List<DeliveryAttendance.Response>> response) {
                if (response.isSuccessful()) {
                    List<DeliveryAttendance.Response> res = response.body();

                    AppExecutors.getInstance().diskIO().execute(() -> {
                        for (DeliveryAttendance.Response r : res) {
                            db.getDeliveryAttendanceDao().updateSyncFlag(r.getUnique_member_id(),
                                    r.getCc_id(),
                                    r.getQty_delivered(),
                                    r.getDate_delivered(),
                                    r.getSync_flag());
                        }
                    });
                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<DeliveryAttendance.Response>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync Up Delivery Attendance: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel upload_channel = new NotificationChannel(CHANNEL_ID,
                    "Sync Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            if (mNotifyManager != null) {
                mNotifyManager.createNotificationChannel(upload_channel);
            }
        }
    }

    private void setNotification() {
        createNotificationChannel();
        builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_outline_cloud_upload_24)
                .setProgress(0, 0, false)
                .setContentTitle("Syncing Data")
                .setContentText("Syncing TPO Data")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void sendNotification(int value) {
        CURRENT += value;

        if (CURRENT == MAX) {
            builder.setProgress(0, 0, false)
                    .setSmallIcon(R.drawable.ic_outline_cloud_done_24)
                    .setContentText("TPO Sync Complete");
        } else {
            builder.setProgress(MAX, CURRENT, false);
        }

        mNotifyManager.notify(NOTIFICATION_ID, builder.build());
    }
}

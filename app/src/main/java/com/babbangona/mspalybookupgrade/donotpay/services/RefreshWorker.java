package com.babbangona.mspalybookupgrade.donotpay.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.donotpay.data.DSessionManager;
import com.babbangona.mspalybookupgrade.donotpay.data.retrofit.RetrofitApiCalls;
import com.babbangona.mspalybookupgrade.donotpay.data.retrofit.RetrofitClient;
import com.babbangona.mspalybookupgrade.donotpay.data.room.DNPDatabase;
import com.babbangona.mspalybookupgrade.donotpay.data.room.tables.DoNotPayReasonsTable;
import com.babbangona.mspalybookupgrade.donotpay.data.room.tables.DoNotPayTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RefreshWorker extends Worker {
    private static final String CHANNEL_ID = "DONOTPAY_REFRESH";
    private static final int NOTIFICATION_ID = 231;
    private static String TAG = "DNP CHECK";
    private NotificationCompat.Builder builder;
    private NotificationManager mNotifyManager;
    private int CURRENT;
    private int MAX = 3;

    private DSessionManager session;
    private DNPDatabase db;

    /**
     * This worker would be used to handle the following:
     * Sync up of DoNotPay Table
     * Sync down of DoNotPay Table &
     * Sync down of DoNotPay Reasons Table.
     */

    public RefreshWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        session = new DSessionManager(context);
        db = DNPDatabase.getInstance(context);
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override
    public Result doWork() {
        CURRENT = 0;
        setNotification();

        syncUpDoNotPayTable();
        syncDownDoNotPayReasons();
        syncDownDoNotPayTable();
        return Result.success();
    }

    public void syncDownDoNotPayTable() {
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<DoNotPayTable>> call = service.syncDownDoNotPay(session.GET_LAST_SYNC_DNP());
        call.enqueue(new Callback<List<DoNotPayTable>>() {
            @Override
            public void onResponse(Call<List<DoNotPayTable>> call, Response<List<DoNotPayTable>> response) {
                if (response.isSuccessful()) {
                    List<DoNotPayTable> res = response.body();

                    if (res != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (DoNotPayTable d : res) {
                                db.getDoNotPayDao().insert(d);
                                session.SET_LAST_SYNC_DNP_TABLE(d.getDate_updated());
                            }
                        });
                    }
                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<DoNotPayTable>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync down DNP table: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownDoNotPayReasons() {
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<DoNotPayReasonsTable>> call = service.syncDownDoNotPayReasons(session.GET_LAST_SYNC_DNP_REASONS());
        call.enqueue(new Callback<List<DoNotPayReasonsTable>>() {
            @Override
            public void onResponse(Call<List<DoNotPayReasonsTable>> call, Response<List<DoNotPayReasonsTable>> response) {
                if (response.isSuccessful()) {
                    List<DoNotPayReasonsTable> res = response.body();

                    if (res != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (DoNotPayReasonsTable r : res) {
                                db.getDoNotPayReasonsDao().insertDNPReasons(r);
                                session.SET_LAST_SYNC_DNP_REASONS(r.getDate_updated());
                            }
                        });
                    }
                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<DoNotPayReasonsTable>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync Down DNP Reasons " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncUpDoNotPayTable() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                String unsynced = new Gson().toJson(db.getDoNotPayDao().getUnsyncedDNP());
                RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
                Call<List<DoNotPayTable.Response>> call = service.syncUpDoNotPay(unsynced);
                call.enqueue(new Callback<List<DoNotPayTable.Response>>() {
                    @Override
                    public void onResponse(Call<List<DoNotPayTable.Response>> call, Response<List<DoNotPayTable.Response>> response) {
                        if (response.isSuccessful()) {
                            List<DoNotPayTable.Response> res = response.body();

                            AppExecutors.getInstance().diskIO().execute(() -> {
                                for (DoNotPayTable.Response sync_res : res) {
                                    //Update
                                    db.getDoNotPayDao().updateSyncFlag(sync_res.getSync_flag(), sync_res.getIk_number());
                                }
                            });
                            sendNotification(1);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<DoNotPayTable.Response>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Sync Up DNP: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel upload_channel = new NotificationChannel(CHANNEL_ID,
                    "Refresh Channel",
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
                .setContentTitle("Refreshing Data")
                .setContentText("Refreshing DoNotPay Data")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void sendNotification(int value) {
        CURRENT += value;

        if (CURRENT == MAX) {
            builder.setProgress(0, 0, false)
                    .setSmallIcon(R.drawable.ic_outline_cloud_done_24)
                    .setContentText("DoNotPay refresh complete");
        } else {
            builder.setProgress(MAX, CURRENT, false);
        }

        mNotifyManager.notify(NOTIFICATION_ID, builder.build());
    }
}

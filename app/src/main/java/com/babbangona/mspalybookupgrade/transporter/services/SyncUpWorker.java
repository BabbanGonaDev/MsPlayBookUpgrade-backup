package com.babbangona.mspalybookupgrade.transporter.services;

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
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.retrofit.RetrofitApiCalls;
import com.babbangona.mspalybookupgrade.transporter.data.retrofit.RetrofitClient;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TempTransporterTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TpoLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.google.gson.Gson;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncUpWorker extends Worker {
    private static final String CHANNEL_ID = "TRANSPORTER_UPLOAD";
    private static final int NOTIFICATION_ID = 190;
    private static String TAG = "Transporter CHECK";
    private NotificationCompat.Builder builder;
    private NotificationManager mNotifyManager;
    private int CURRENT;

    private TSessionManager session;
    private TransporterDatabase db;

    /**
     * This would be used to sync up the following tables:
     * - Transporter table
     * - Operating areas table.
     * <p>
     * - Coach logs table.
     * - TPO logs table.
     * - Favourites table.
     * - Temp Transporters table.
     */

    public SyncUpWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        session = new TSessionManager(context);
        db = TransporterDatabase.getInstance(context);
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @NonNull
    @Override
    public Result doWork() {
        CURRENT = 0;
        setNotification();

        syncUpTransporterTable();
        syncUpOperatingAreas();
        syncUpCoachLogs();
        syncUpTpoLogs();
        syncUpTempTransporter();
        syncUpFavourites();

        return Result.success();
    }

    public void syncUpTransporterTable() {

        AppExecutors.getInstance().diskIO().execute(() -> {
            String unsynced = new Gson().toJson(db.getTransporterDao().getUnSyncedTransporterTable());
            RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
            Call<List<TransporterTable.Response>> call = service.syncUpTransporterTable(unsynced);
            Log.d("CHECK", "For sync up transporter" + unsynced);
            call.enqueue(new Callback<List<TransporterTable.Response>>() {
                @Override
                public void onResponse(Call<List<TransporterTable.Response>> call, Response<List<TransporterTable.Response>> response) {
                    if (response.isSuccessful()) {
                        List<TransporterTable.Response> res = response.body();

                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (TransporterTable.Response sync_res : res) {
                                db.getTransporterDao().updateSyncResponse(sync_res.phone_number, sync_res.sync_flag);
                            }
                        });

                        sendNotification(1);
                    }
                }

                @Override
                public void onFailure(Call<List<TransporterTable.Response>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Sync Up Transporter Table " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        });

    }

    public void syncUpOperatingAreas() {

        AppExecutors.getInstance().diskIO().execute(() -> {
            String un_synced = new Gson().toJson(db.getOpAreaDao().getUnSyncedOperatingAreas());
            RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
            Call<List<OperatingAreasTable.Response>> call = service.syncUpOperatingAreasTable(un_synced);
            Log.d("CHECK", "For sync up areas" + un_synced);
            call.enqueue(new Callback<List<OperatingAreasTable.Response>>() {
                @Override
                public void onResponse(Call<List<OperatingAreasTable.Response>> call, Response<List<OperatingAreasTable.Response>> response) {
                    if (response.isSuccessful()) {
                        List<OperatingAreasTable.Response> res = response.body();

                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (OperatingAreasTable.Response sync_res : res) {
                                db.getOpAreaDao().updateSyncResponse(sync_res.phone_number, sync_res.cc_id, sync_res.sync_flag);
                            }
                        });

                        sendNotification(1);
                    }
                }

                @Override
                public void onFailure(Call<List<OperatingAreasTable.Response>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Sync Up Ops Area Table " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    public void syncUpCoachLogs() {

        AppExecutors.getInstance().diskIO().execute(() -> {
            String unsynced = new Gson().toJson(db.getCoachLogsDao().getUnSyncedCoachLogs());
            RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
            Call<List<CoachLogsTable.Response>> call = service.syncUpCoachLogsTable(unsynced);
            Log.d("CHECK", "For sync up coach logs" + unsynced);
            call.enqueue(new Callback<List<CoachLogsTable.Response>>() {
                @Override
                public void onResponse(Call<List<CoachLogsTable.Response>> call, Response<List<CoachLogsTable.Response>> response) {
                    if (response.isSuccessful()) {
                        List<CoachLogsTable.Response> res = response.body();

                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (CoachLogsTable.Response r : res) {
                                db.getCoachLogsDao().updateSyncFlags(r.getMember_id(),
                                        r.getQuantity(),
                                        r.getTransporter_id(),
                                        r.getDate_logged(),
                                        r.getSync_flag());
                            }
                        });

                        sendNotification(1);
                    }
                }

                @Override
                public void onFailure(Call<List<CoachLogsTable.Response>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Sync Up Coach Logs " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        });
    }

    public void syncUpTpoLogs() {

        AppExecutors.getInstance().diskIO().execute(() -> {
            String unsynced = new Gson().toJson(db.getTpoLogsDao().getUnSyncedTpoLogs());
            RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
            Call<List<TpoLogsTable.Response>> call = service.syncUpTpoLogsTable(unsynced);
            Log.d("CHECK", "For sync up tpo logs" + unsynced);
            call.enqueue(new Callback<List<TpoLogsTable.Response>>() {
                @Override
                public void onResponse(Call<List<TpoLogsTable.Response>> call, Response<List<TpoLogsTable.Response>> response) {
                    if (response.isSuccessful()) {
                        List<TpoLogsTable.Response> res = response.body();

                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (TpoLogsTable.Response r : res) {
                                db.getTpoLogsDao().updateSyncFlags(r.getMember_id(),
                                        r.getQuantity(),
                                        r.getTransporter_id(),
                                        r.getDate_logged(),
                                        r.getSync_flag());
                            }
                        });

                        sendNotification(1);
                    }
                }

                @Override
                public void onFailure(Call<List<TpoLogsTable.Response>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Sync Up Coach Logs " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        });
    }

    public void syncUpTempTransporter() {
        String unsynced = new Gson().toJson(db.getTempTransporterDao().getAllUnSynced());
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<TempTransporterTable.Response>> call = service.syncUpTempTransporters(unsynced);
        Log.d("CHECK", "For sync up temp logs " + unsynced);
        call.enqueue(new Callback<List<TempTransporterTable.Response>>() {
            @Override
            public void onResponse(Call<List<TempTransporterTable.Response>> call, Response<List<TempTransporterTable.Response>> response) {
                if (response.isSuccessful()) {
                    List<TempTransporterTable.Response> res = response.body();

                    AppExecutors.getInstance().diskIO().execute(() -> {
                        for (TempTransporterTable.Response r : res) {
                            db.getTempTransporterDao().updateSyncFlag(r.getTemp_transporter_id(), r.getSync_flag());
                            ;
                        }
                    });

                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<TempTransporterTable.Response>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync Up Temp Transporters " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncUpFavourites() {

        AppExecutors.getInstance().diskIO().execute(() -> {
            String favourites = new Gson().toJson(db.getFavouritesDao().getAllUsersFavourites(session.GET_LOG_IN_STAFF_ID()));
            RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
            Call<ResponseBody> call = service.syncUpFavourites(favourites);
            Log.d("CHECK", "For favourites: " + favourites);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        sendNotification(1);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Sync Up Favourites " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel upload_channel = new NotificationChannel(CHANNEL_ID,
                    "Upload Channel",
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
                .setContentTitle("Uploading Data")
                .setContentText("Uploading Transporter Data")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void sendNotification(int value) {
        CURRENT += value;

        int MAX = 6;
        if (CURRENT == MAX) {
            builder.setProgress(0, 0, false)
                    .setSmallIcon(R.drawable.ic_outline_cloud_done_24)
                    .setContentText("Transporter Upload Complete");
        } else {
            builder.setProgress(MAX, CURRENT, false);
        }

        mNotifyManager.notify(NOTIFICATION_ID, builder.build());
    }
}

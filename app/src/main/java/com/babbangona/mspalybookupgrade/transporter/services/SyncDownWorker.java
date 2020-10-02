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
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CardsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CollectionCenterTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.FavouritesTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TpoLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncDownWorker extends Worker {
    private static final String CHANNEL_ID = "TRANSPORTER_DOWNLOAD";
    private static final int NOTIFICATION_ID = 191;
    private static String TAG = "Transporter CHECK";
    private NotificationCompat.Builder builder;
    private NotificationManager mNotifyManager;
    private int CURRENT;

    private TSessionManager session;
    private TransporterDatabase db;

    /**
     * This would be used to sync down the following tables:
     * - Transporter table
     * - Collection center table
     * - Operating areas table &
     * - Cards table.
     * <p>
     * - Coach logs table.
     * - TPO logs table.
     * - Favourites table.
     */


    public SyncDownWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
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

        syncDownTransporterTable();
        syncDownOperatingAreas();
        syncDownCCTable();
        syncDownCards();
        syncDownCoachLogs();
        syncDownTpoLogs();
        syncDownFavourites();
        return Result.success();
    }

    public void syncDownTransporterTable() {
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<TransporterTable>> call = service.syncDownTransporterTable(session.GET_LAST_SYNC_TRANSPORTER());
        call.enqueue(new Callback<List<TransporterTable>>() {
            @Override
            public void onResponse(Call<List<TransporterTable>> call, Response<List<TransporterTable>> response) {
                if (response.isSuccessful()) {
                    List<TransporterTable> res = response.body();

                    if (res != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (TransporterTable t : res) {
                                Log.d("CHECK", "Phone No: " + t.getPhone_number());
                                db.getTransporterDao().insertSingleTransporter(t);
                                session.SET_LAST_SYNC_TRANSPORTER(t.getDate_updated());
                            }
                        });
                    }

                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<TransporterTable>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync down Transporter Table " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownOperatingAreas() {
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<OperatingAreasTable>> call = service.syncDownOperatingAreas(session.GET_LAST_SYNC_OPERATING_AREAS());
        call.enqueue(new Callback<List<OperatingAreasTable>>() {
            @Override
            public void onResponse(Call<List<OperatingAreasTable>> call, Response<List<OperatingAreasTable>> response) {
                if (response.isSuccessful()) {
                    List<OperatingAreasTable> res = response.body();

                    if (res != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (OperatingAreasTable x : res) {
                                db.getOpAreaDao().insertSingleOpArea(x);
                                session.SET_LAST_SYNC_OPERATING_AREAS(x.getDate_updated());
                            }
                        });
                    }

                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<OperatingAreasTable>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync down Operating Areas Table " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownCCTable() {
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<CollectionCenterTable>> call = service.syncDownCC(session.GET_LAST_SYNC_CC());
        call.enqueue(new Callback<List<CollectionCenterTable>>() {
            @Override
            public void onResponse(Call<List<CollectionCenterTable>> call, Response<List<CollectionCenterTable>> response) {
                if (response.isSuccessful()) {
                    List<CollectionCenterTable> res = response.body();

                    if (res != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (CollectionCenterTable c : res) {
                                db.getCcDao().insertSingleCC(c);
                                session.SET_LAST_SYNC_CC(c.getDate_updated());
                            }
                        });
                    }

                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<CollectionCenterTable>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync down CC Table " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownCards() {
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<CardsTable.Download>> call = service.syncDownCards(session.GET_LAST_SYNC_CARDS());
        call.enqueue(new Callback<List<CardsTable.Download>>() {
            @Override
            public void onResponse(Call<List<CardsTable.Download>> call, Response<List<CardsTable.Download>> response) {
                if (response.isSuccessful()) {
                    List<CardsTable.Download> res = response.body();

                    if (res != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (CardsTable.Download d : res) {
                                db.getCardsDao().insertSingleCard(new CardsTable(d.id,
                                        d.account_number,
                                        d.card_name,
                                        d.product_code,
                                        d.branch_number,
                                        d.card_number));
                                session.SET_LAST_SYNC_CARDS(d.last_sync);
                            }
                        });
                    }

                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<CardsTable.Download>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync down Cards Table " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownCoachLogs() {
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<CoachLogsTable.Download>> call = service.syncDownCoachLogs(session.GET_LAST_SYNC_COACH_LOGS());
        call.enqueue(new Callback<List<CoachLogsTable.Download>>() {
            @Override
            public void onResponse(Call<List<CoachLogsTable.Download>> call, Response<List<CoachLogsTable.Download>> response) {
                if (response.isSuccessful()) {
                    List<CoachLogsTable.Download> res = response.body();

                    if (res != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (CoachLogsTable.Download d : res) {
                                db.getCoachLogsDao().insertSingleCoachLog(new CoachLogsTable(d.getMember_id(),
                                        d.getQuantity(),
                                        d.getTransported_by(),
                                        d.getTransporter_id(),
                                        d.getVoucher_id(),
                                        d.getVoucher_id_flag(),
                                        d.getCc_id(),
                                        d.getInstant_payment_flag(),
                                        d.getStaff_id(),
                                        d.getImei(),
                                        d.getApp_version(),
                                        d.getDate_logged(),
                                        d.getSync_flag()));
                                session.SET_LAST_SYNC_COACH_LOGS(d.getLast_sync());
                            }
                        });
                    }
                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<CoachLogsTable.Download>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync down Coach logs " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownTpoLogs() {
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<TpoLogsTable.Download>> call = service.syncDownTpoLogs(session.GET_LAST_SYNC_TPO_LOGS());
        call.enqueue(new Callback<List<TpoLogsTable.Download>>() {
            @Override
            public void onResponse(Call<List<TpoLogsTable.Download>> call, Response<List<TpoLogsTable.Download>> response) {
                if (response.isSuccessful()) {
                    List<TpoLogsTable.Download> res = response.body();

                    if (res != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            for (TpoLogsTable.Download d : res) {
                                db.getTpoLogsDao().insertSingleTpoLog(new TpoLogsTable(d.getMember_id(),
                                        d.getQuantity(),
                                        d.getTransported_by(),
                                        d.getTransporter_id(),
                                        d.getVoucher_id(),
                                        d.getVoucher_id_flag(),
                                        d.getCc_id(),
                                        d.getInstant_payment_flag(),
                                        d.getStaff_id(),
                                        d.getImei(),
                                        d.getApp_version(),
                                        d.getDate_logged(),
                                        d.getSync_flag()));
                                session.SET_LAST_SYNC_TPO_LOGS(d.getLast_sync());
                            }
                        });
                    }
                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<TpoLogsTable.Download>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync down TPO logs " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void syncDownFavourites() {
        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<List<FavouritesTable.Download>> call = service.syncDownFavourites(session.GET_LAST_SYNC_FAVOURITES(), session.GET_LOG_IN_STAFF_ID());
        call.enqueue(new Callback<List<FavouritesTable.Download>>() {
            @Override
            public void onResponse(Call<List<FavouritesTable.Download>> call, Response<List<FavouritesTable.Download>> response) {
                if (response.isSuccessful()) {
                    List<FavouritesTable.Download> res = response.body();

                    if (res != null) {
                        AppExecutors.getInstance().diskIO().execute(() -> {
                            db.getFavouritesDao().emptyFavouritesTable();

                            for (FavouritesTable.Download d : res) {
                                db.getFavouritesDao().markFavourite(new FavouritesTable(d.getStaff_id(),
                                        d.getPhone_number(),
                                        d.getActive_flag(),
                                        d.getSync_flag()));
                                session.SET_LAST_SYNC_FAVOURITES(d.getLast_sync());
                            }
                        });
                    }
                    sendNotification(1);
                }
            }

            @Override
            public void onFailure(Call<List<FavouritesTable.Download>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Sync down Favourites: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel upload_channel = new NotificationChannel(CHANNEL_ID,
                    "Download Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);

            if (mNotifyManager != null) {
                mNotifyManager.createNotificationChannel(upload_channel);
            }
        }
    }

    private void setNotification() {
        createNotificationChannel();
        builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_outline_cloud_download_24)
                .setProgress(0, 0, false)
                .setContentTitle("Downloading Data")
                .setContentText("Downloading Transporter Data")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void sendNotification(int value) {
        CURRENT += value;
        //Log.d("CHECK", "Current: " + CURRENT);

        int MAX = 7;
        if (CURRENT == MAX) {
            builder.setProgress(0, 0, false)
                    .setSmallIcon(R.drawable.ic_outline_cloud_done_24)
                    .setContentText("Transporter Download Complete");
        } else {
            builder.setProgress(MAX, CURRENT, false);
        }

        mNotifyManager.notify(NOTIFICATION_ID, builder.build());
    }
}

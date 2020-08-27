package com.babbangona.mspalybookupgrade.transporter.services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.retrofit.RetrofitApiCalls;
import com.babbangona.mspalybookupgrade.transporter.data.retrofit.RetrofitClient;
import com.babbangona.mspalybookupgrade.transporter.data.room.TransporterDatabase;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppExecutors;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncUpWorker extends Worker {
    private TSessionManager session;
    private TransporterDatabase db;

    /**
     * This would be used to sync up the following tables:
     * - Transporter table
     * - Operating areas table.
     */

    public SyncUpWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        session = new TSessionManager(context);
        db = TransporterDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        syncUpTransporterTable();
        syncUpOperatingAreas();
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
                    }
                }

                @Override
                public void onFailure(Call<List<OperatingAreasTable.Response>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Sync Up Ops Area Table " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}

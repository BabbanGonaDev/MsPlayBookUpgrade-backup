package com.babbangona.mspalybookupgrade.LocationTraker.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.WorkerParameters;

import com.babbangona.mspalybookupgrade.LocationTraker.database.entity.StaffDetails;
import com.babbangona.mspalybookupgrade.LocationTraker.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Response;

public class StaffWorker extends SyncWorker {

    public StaffWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        Log.i("SyncWorker", "Staff");
        //dataBase.getSyncUnitDao().insert(new SyncUnit("Syncing Audit Details",Constants.SyncId.AUDIT));
    }

    public Result doSyncWork() {
        Data.Builder outPut = new Data.Builder();
        String staffModel = getInputData().getString("bgId");
        //StaffDetails staff = dataBase.staffDao().getStaffByID(bgId);
        Gson gson = new Gson();
        StaffDetails staffDetails = gson.fromJson(staffModel, StaffDetails.class);
        String model = gson.toJson(staffDetails);
        String request = "[" + model + "]";
        try {
            Response<ArrayList<StaffDetails>> response = this.apiInterface.updateStaff(request).execute();
            if (response == null) {
                outPut.putInt(Constants.SYNC_ERROR_CODE, response.code());
                outPut.putString(Constants.SYNC_ERROR_MESSAGE, response.errorBody().string());
                //dataBase.getSyncUnitDao().updateID(Constants.SyncId.AUDIT, getErrorString(response.errorBody().string()),Constants.SYNC_ERROR);
                return Result.failure(outPut.build());
            } else {
                Log.e("response-->", response.body().toString());
                StaffDetails[] staff1 = response.body().toArray(new StaffDetails[response.body().size()]);
                //dataBase.getSyncUnitDao().updateID(Constants.SyncId.AUDIT, "Synced Audit Details",Constants.SYNC_SUCCESS);
                StaffDetails staff2 = staff1[0];
                staff2.setSync_flag(1);
                this.dataBase.staffDao().insert(staff2);

                if (staff1.length == 0)
                    return Result.success();
                return Result.success();
                /*for (int i  = 0; i < response.body().size(); i++)
                    response.body().get(i).setSync_flag(1);
                Staff[] staff1 =  response.body().toArray(new Staff[response.body().size()]);
                //dataBase.getSyncUnitDao().updateID(Constants.SyncId.AUDIT, "Synced Audit Details",Constants.SYNC_SUCCESS);
                Staff staff2 = staff1[0];
                staff2.setSync_flag(0);
                this.dataBase.staffDao().insert(staff2);

                if(staff1.length == 0)
                    return Result.success();
                return Result.success();*/
            }
        } catch (Exception e) {
            outPut.putString(Constants.SYNC_ERROR_MESSAGE, e.getMessage());
            //dataBase.getSyncUnitDao().updateID(Constants.SyncId.AUDIT, getErrorString(e.getMessage()),Constants.SYNC_ERROR);
            return Result.failure(outPut.build());
            // return Result.retry();
        }
    }

    @Override
    public String getErrorString(String Message) {
        return "Error Syncing Down Staff Details : " + Message;
    }
}


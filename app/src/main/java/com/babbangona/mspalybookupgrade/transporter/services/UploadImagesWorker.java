package com.babbangona.mspalybookupgrade.transporter.services;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.data.models.ImageResponse;
import com.babbangona.mspalybookupgrade.transporter.data.retrofit.RetrofitApiCalls;
import com.babbangona.mspalybookupgrade.transporter.data.retrofit.RetrofitClient;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImagesWorker extends Worker {
    TSessionManager session;

    public UploadImagesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        session = new TSessionManager(getApplicationContext());
    }

    @NonNull
    @Override
    public Result doWork() {
        getAndQueueTransporterCardsImages();
        getAndQueueTransporterFacesImages();

        return Result.success();
    }

    private void getAndQueueTransporterCardsImages() {
        String[] transporter_cards_list = session.GET_TRANSPORTER_CARDS();
        if (transporter_cards_list == null) {
            Log.d("CHECK", "Empty Cards List");
            return;
        }

        for (String c : transporter_cards_list) {
            File card = new File(Environment.getExternalStorageDirectory().getPath(), AppUtils.bg_cards_location + File.separator + c);
            if (!card.exists()) {
                Log.d("CHECK", "Card doesn't exists in location " + card.getPath());
            }
            uploadTransporterCard(card, c);
        }
    }


    private void uploadTransporterCard(File file, String imageName) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<ImageResponse> call = service.uploadTransporterImages(fileToUpload, filename);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                ImageResponse res = response.body();
                if (res == null) {
                    Log.d("CHECK", "onResponse: NO RESPONSE FROM THE SERVER");
                } else {
                    if (res.getStatus().contains("Success")) {
                        session.REMOVE_TRANSPORTER_CARDS_FROM_LIST(imageName);
                        Log.d("CHECK", "Successful upload: " + imageName);
                    } else {
                        Log.d("CHECK", "Failed to upload: " + imageName);
                    }
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.d("CHECK", "Transporter Cards Failed image upload: " + t.getMessage());
            }
        });
    }

    private void getAndQueueTransporterFacesImages() {
        String[] transporter_faces_list = session.GET_TRANSPORTER_FACES();
        if (transporter_faces_list == null) {
            Log.d("CHECK", "Empty Faces List");
            return;
        }

        for (String f : transporter_faces_list) {
            File face = new File(Environment.getExternalStorageDirectory().getPath(), AppUtils.facial_captures_location + File.separator + f);
            if (!face.exists()) {
                Log.d("CHECK", "Face doesn't exists in location " + face.getPath());
            }
            uploadTransporterFace(face, f);
        }
    }

    private void uploadTransporterFace(File file, String imageName) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        RetrofitApiCalls service = RetrofitClient.getRetrofitInstance().create(RetrofitApiCalls.class);
        Call<ImageResponse> call = service.uploadTransporterImages(fileToUpload, filename);
        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                ImageResponse res = response.body();
                if (res == null) {
                    Log.d("CHECK", "onResponse: NO RESPONSE FROM THE SERVER");
                } else {
                    if (res.getStatus().contains("Success")) {
                        session.REMOVE_TRANSPORTER_FACE_FROM_LIST(imageName);
                        Log.d("CHECK", "Successful upload: " + imageName);
                    } else {
                        Log.d("CHECK", "Failed to upload: " + imageName);
                    }
                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Log.d("CHECK", "Assigned Assets Failed image upload: " + t.getMessage());
            }
        });
    }
}

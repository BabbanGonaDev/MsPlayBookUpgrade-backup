package com.babbangona.mspalybookupgrade.transporter.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.babbangona.bgfr.CustomLuxandActivity;
import com.babbangona.bgfr.Database.BGFRInfo;
import com.babbangona.bgfr.ErrorCodes;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.transporter.data.TSessionManager;
import com.babbangona.mspalybookupgrade.transporter.helpers.AppUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class TransporterTemplateCaptureActivity extends CustomLuxandActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Boolean setDetectFakeFaces() {
        //SET TO TRUE WHEN READY TO DETECT FAKE FACES
        return true;
    }

    @Override
    public Boolean setKeepFaces() {
        //SET TO TRUE  FOR THE INCREASED LUXAND TRACKER SIZE
        return false;
    }

    @Override
    public Long setTimer() {
        //SET TO WHATEVER COUNT DOWN TIME YOU WANT
        return 15000L;
    }

    @Override
    public void TimerFinished() {
        switch (getERROR_CODE()) {
            case ErrorCodes.KEY_GENERIC_ERROR:
                showErrorAndClose("Generic Error");
                break;
            case ErrorCodes.KEY_NO_FACE_FOUND:
                showErrorAndClose("Unable to detect a human face");
                break;
            case ErrorCodes.KEY_BLINK_NOT_FOUND:
                showErrorAndClose("Blink not found");
                break;
            case ErrorCodes.KEY_FACE_NOT_MATCHED:
                //Not sure why this was called though. But am hiding it for now.
                //showErrorAndClose("Face not matched");
                break;
        }
    }

    @Override
    public void MyFlow() {
        TSessionManager session = new TSessionManager(getApplicationContext());
        loadBlankTracker(session.GET_REG_PHONE_NUMBER());
    }

    @NonNull
    @Override
    public String buttonText() {
        return "Begin";
    }

    @NonNull
    @Override
    public String headerText() {
        return "Capture Template";
    }

    @Override
    public void Authenticated() {

    }

    @Override
    public void TrackerSaved() {
        StopTimer();

        BGFRInfo info = new BGFRInfo(this);
        String template = info.getSingleTemplate();
        Bitmap image = info.getImage();

        saveImageAndTemplate(template, image);
    }

    public void saveImageAndTemplate(String passed_template, Bitmap passed_image) {
        TSessionManager session = new TSessionManager(getApplicationContext());
        session.SET_REG_FACE_TEMPLATE(passed_template);

        new SaveBitmapImage().execute(passed_image);
    }

    @SuppressLint("StaticFieldLeak")
    public class SaveBitmapImage extends AsyncTask<Bitmap, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            TSessionManager session = new TSessionManager(getApplicationContext());
            String file_name = session.GET_REG_PHONE_NUMBER() + "_template_face.jpg";

            File dir = new File(Environment.getExternalStorageDirectory().getPath(), AppUtils.facial_captures_location);
            if (!dir.exists() && !dir.mkdirs()) {
                //Unable to create folder.
            } else {
                File img_file = new File(Environment.getExternalStorageDirectory().getPath(), AppUtils.facial_captures_location + File.separator + file_name);
                try {
                    FileOutputStream out_stream = new FileOutputStream(img_file);
                    bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 60, out_stream);
                    session.SET_TRANSPORTER_FACES(file_name);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(TransporterTemplateCaptureActivity.this)
                    .setIcon(R.drawable.ic_smiley_face)
                    .setTitle("Success")
                    .setMessage("Template successfully captured")
                    .setPositiveButton("Okay", (dialog, which) -> {
                        finish();
                        startActivity(new Intent(getApplicationContext(), TransporterVehicleActivity.class));
                    }).setCancelable(false);

            if (!isFinishing()) builder.show();
        }
    }

}
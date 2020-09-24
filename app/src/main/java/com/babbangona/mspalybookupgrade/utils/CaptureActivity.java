package com.babbangona.mspalybookupgrade.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.babbangona.bgfr.BGFRMode;
import com.babbangona.bgfr.CustomLuxandActivity;
import com.babbangona.bgfr.Database.BGFRInfo;
import com.babbangona.bgfr.ErrorCodes;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.MemberListRecycler.MemberListRecyclerModel;
import com.babbangona.mspalybookupgrade.ThreshingViews.FieldList;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import java.io.ByteArrayOutputStream;

public class CaptureActivity extends CustomLuxandActivity {

    //startActivity(new Intent(this, CaptureActivity.class));
    @Override
    public Boolean setDetectFakeFaces() {
        //SET TO TRUE WHEN WE ARE READY TO DETECT FAKE FACES
        return true;
    }

    @Override
    public Long setTimer() {
        //SET TO WHATEVER COUNT DOWN TIME YOU WANT
        return 15000L;
    }

    @Override
    public Boolean setKeepFaces() {
        //SET TO TRUE FOR THE INCREASED LUXAND TRACKER SIZE
        return false;
    }

    @Override
    public void TimerFinished() {

        switch(getERROR_CODE())
        {
            case ErrorCodes.KEY_GENERIC_ERROR:
                //GENERIC ERROR: NOT LIKELY TO HAPPEN
                break;
            case ErrorCodes.KEY_NO_FACE_FOUND:
                //NO FACE FOUND DECIDE WHAT TO DO
                showErrorAndClose(this.getResources().getString(R.string.no_face_found));
                break;
            case ErrorCodes.KEY_BLINK_NOT_FOUND:
                //NO BLINK FOUND DECIDE WHAT TO DO
                showErrorAndClose(this.getResources().getString(R.string.blink_not_found));
                break;
            case ErrorCodes.KEY_FACE_NOT_MATCHED:
                //NO FACE MATCHED DECIDE WHAT TO DO
                setBGFR_MODE(BGFRMode.CAPTURE_NEW);
                StartTimer();
                break;

        }
    }

    @Override
    public void MyFlow() {

        loadBlankTracker("BABBANGONA-MEMBER-" + Math.random());
    }

    @NonNull
    @Override
    public String buttonText() {
        return this.getResources().getString(R.string.start_capture);
    }

    @NonNull
    @Override
    public String headerText() {
        return this.getResources().getString(R.string.capture_);
    }

    @Override
    public void Authenticated() {
        StopTimer();

        //dialog that shows template exist
        /*Intent intentMessage = new Intent();
        intentMessage.putExtra("RESULT",new MemberListRecyclerModel.TemplateModel());
        this.setResult(Activity.RESULT_OK,intentMessage);
        this.finish();*/

    }

    @Override
    public void TrackerSaved() {
        StopTimer();
        BGFRInfo info                                       = new BGFRInfo(this);
        String individualTemplate                           = info.getSingleTemplate();
        String image_person_small                           = info.getImageString();
        String image_person_large                           = info.getBiggerImageString();
        saveAndReturn(new MemberListRecyclerModel.TemplateModel(individualTemplate,image_person_small,image_person_large,"1"));
    }

    public void saveAndReturn(MemberListRecyclerModel.TemplateModel result){
        Intent intentMessage = new Intent();
        intentMessage.putExtra("RESULT",result);
        this.setResult(Activity.RESULT_OK,intentMessage);
        Log.i(TAG, "TrackerSaved: TRACKER SAVED IN CAPTURE ACTIVITY");
        this.finish();
        //startActivity(new Intent(CaptureActivity.this, FieldList.class));
    }
}

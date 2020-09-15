package com.babbangona.mspalybookupgrade.utils;

import android.content.Intent;

import com.babbangona.bgfr.BGFRMode;
import com.babbangona.bgfr.CustomLuxandActivity;
import com.babbangona.bgfr.ErrorCodes;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.ThreshingViews.FieldList;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

public class VerifyActivity extends CustomLuxandActivity {


    private AppDatabase appDatabase;
    private SharedPrefs sharedPrefs;


    @Override
    public Boolean setDetectFakeFaces() {
        //SET TO TRUE WHEN WE ARE READY TO DETECT FAKE FACES
        return false;
    }


    @Override
    public Long setTimer() {
        //SET TO WHATEVER COUNT DOWN TIME YOU WANT
        return new Long(15000);
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
                showErrorAndClose(this.getResources().getString(R.string.no_face_found));
//                myDialog(this.getResources().getString(R.string.no_face_found));
                break;
            case ErrorCodes.KEY_BLINK_NOT_FOUND:
                showErrorAndClose(this.getResources().getString(R.string.blink_not_found));
//                myDialog(this.getResources().getString(R.string.blink_not_found));
                break;
            case ErrorCodes.KEY_FACE_NOT_MATCHED:
                /*Intent i = new Intent(getApplicationContext(), ReverifyActivity.class);
                startActivity(i);*/
                finish();
                break;
        }

    }

    @Override
    public void MyFlow() {

        sharedPrefs = new SharedPrefs(getApplicationContext());
        appDatabase = AppDatabase.getInstance(getApplicationContext());


        LoadTracker(appDatabase.membersDao().getMemberTemplate(sharedPrefs.getKeyThreshingUniqueMemberId()),
                BGFRMode.AUTHENTICATE);

    }

    @Override
    public void Authenticated() {
        StopTimer();

        startActivity(new Intent(getApplicationContext(), FieldList.class));
        finish();
    }

    @Override
    public void TrackerSaved() {

    }

//    public void save(String template, String pile) {
//
//    }

    @Override
    public String buttonText() {
        return getString(R.string.start_verify);
    }

    @Override
    public String headerText() {
        return getString(R.string.verify);
    }


}

package com.babbangona.standardtemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.babbangona.standardtemplate.data.sharedprefs.SharedPrefs;

public class Main2Activity extends AppCompatActivity {

    String mStaffName,mStaffRole, mStaffId, mStaffProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        saveAccessControlDetails();

    }

    void saveAccessControlDetails(){
        try {

            //This gets the intents sent from access control and saves it to sharedprefs
            Intent intent  = getIntent();
            Bundle b       = intent.getExtras();
            mStaffName     = (String) b.get("staff_name");
            mStaffRole     = (String) b.get("staff_role");
            mStaffId       = (String) b.get("staff_id");
            mStaffProgram  = (String) b.get("staff_program");

            new SharedPrefs(this).CreateLoginSession(mStaffName,mStaffId,mStaffRole,mStaffProgram);


        }catch(Exception e){

        }

    }


}

package com.babbangona.mspalybookupgrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

public class Main2Activity extends AppCompatActivity {

    String mStaffName,mStaffRole, mStaffId, mStaffProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        saveAccessControlDetails();
        startHomepageActivity();
    }

    void saveAccessControlDetails(){
        try {

            //This gets the intents sent from access control and saves it to shared prefs
            Intent intent  = getIntent();
            Bundle b       = intent.getExtras();
            if (b != null) {
                mStaffName     = (String) b.get("staff_name");
                mStaffRole     = (String) b.get("staff_role");
                mStaffId       = (String) b.get("staff_id");
                mStaffProgram  = (String) b.get("staff_program");
            }

            new SharedPrefs(this).CreateLoginSession(mStaffName,mStaffId,LeadMIKAdjustment(mStaffRole),mStaffProgram);


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    void startHomepageActivity(){
        finish();
        startActivity(new Intent(Main2Activity.this,Homepage.class));
    }

    String LeadMIKAdjustment(String staff_role){
        if (staff_role.equalsIgnoreCase("LMIK")){
            return "LMIk";
        }else{
            return staff_role;
        }
    }
}

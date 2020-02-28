package com.babbangona.standardtemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    void OpenAccessControl(){


        try{

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(
                    new ComponentName("com.babbangona.accesscontrol",
                            "com.babbangona.accesscontrol.MainActivity"));
            startActivity(intent);



        }
        catch(Exception e){
            //This is the message that shows up when the user clicks the open access control button and the application is not on the phone
            Toast.makeText(this, getResources().getString(R.string.main_access_control), Toast.LENGTH_SHORT).show();
        }
    }

    void OpenMain2(){

        Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
        intent.putExtra("staff_name","test_user");
        intent.putExtra("staff_id","T-1ZZZZZZZZ");
        intent.putExtra("staff_role","ES");
        intent.putExtra("staff_program","BGD");
        startActivity(intent);

    }

    public void Launch(View view) {

        //TODO: change the call below to OpenAccessControl before deploying the application
        OpenMain2();
    }
}

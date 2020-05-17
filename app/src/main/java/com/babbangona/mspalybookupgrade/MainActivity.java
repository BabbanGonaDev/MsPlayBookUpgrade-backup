package com.babbangona.mspalybookupgrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;
import com.babbangona.mspalybookupgrade.data.db.entities.StaffList;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.btn_open_access_control)
    MaterialButton btn_open_access_control;

    SharedPrefs sharedPrefs;

    AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPrefs = new SharedPrefs(MainActivity.this);
        appDatabase  = AppDatabase.getInstance(MainActivity.this);
        addDataToDatabase();
    }

    @OnClick(R.id.btn_open_access_control)
    public void navigateToAccessControl(){
        Launch();
    }


    void OpenAccessControl(){
        try{
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName("com.babbangona.accesscontrol",
                    "com.babbangona.accesscontrol.MainActivity"));
            startActivity(intent);
        }
        catch(Exception e){
            //This is the message that shows up when the user clicks the open access control button and the application is not on the phone
            Toast.makeText(this, getResources().getString(R.string.main_access_control), Toast.LENGTH_SHORT).show();
        }
    }

    public void Launch() {
        OpenMain2();
    }

    void OpenMain2(){
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        intent.putExtra("staff_name","test_user");
        intent.putExtra("staff_id","T-1ZZZZZZZZ");
        intent.putExtra("staff_role","MSS");
        intent.putExtra("staff_program","BGD");
        startActivity(intent);
    }

    void addDataToDatabase(){
        AsyncTask.execute(()->{
            if (sharedPrefs.getKeyFirstTimeDataFlag().equalsIgnoreCase("0")){
                appDatabase.activityListDao().insert(new ActivityList("4","en",
                        "Set Portfolio", "com.babbangona.mspalybookupgrade.SetPortfolio",
                        "1","MSS,LMIk","0"));
                sharedPrefs.setKeyFirstTimeDataFlag("1");
            }

        });
    }
}

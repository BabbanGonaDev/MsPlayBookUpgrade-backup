package com.babbangona.mspalybookupgrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.babbangona.mspalybookupgrade.data.db.entities.PWSActivityController;
import com.babbangona.mspalybookupgrade.data.db.entities.PWSCategoryList;
import com.uxcam.UXCam;

import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;
import com.babbangona.mspalybookupgrade.data.db.entities.Category;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.Main2ActivityMethods;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

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
        Main2ActivityMethods main2ActivityMethods = new Main2ActivityMethods(MainActivity.this);
        main2ActivityMethods.confirmPhoneDate();
        main2ActivityMethods.confirmLocationOpen();
        sharedPrefs.setKeyProgressDialogStatus(1);
        UXCam.startWithKey("l5h2x6r7c5j34t5");
        UXCam.setUserIdentity(new SharedPrefs(getApplicationContext()).getStaffID());
    }

    @OnClick(R.id.btn_open_access_control)
    public void navigateToAccessControl(){
        //Launch();
        OpenAccessControl();
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
            if (sharedPrefs.getKeyFirstUpdateFlag().equalsIgnoreCase("0")){
                List<Category> categoryList = new ArrayList<>();
                categoryList.add(new Category("MIK","subd"));
                categoryList.add(new Category("LA","subd"));
                categoryList.add(new Category("MSB","subd"));
                categoryList.add(new Category("MSS","supr"));
                categoryList.add(new Category("LMIK","supr"));
                categoryList.add(new Category("PC","supr"));
                appDatabase.categoryDao().insert(categoryList);
                sharedPrefs.setKeyFirstUpdateFlag("1");
            }

            if (sharedPrefs.getKeySecondUpdateFlag().equalsIgnoreCase("0")){
                List<PWSActivityController> pwsActivityControllerList = new ArrayList<>();
                pwsActivityControllerList.add(new PWSActivityController("MIK","1"));
                pwsActivityControllerList.add(new PWSActivityController("LA","1"));
                pwsActivityControllerList.add(new PWSActivityController("MSB","1"));
                pwsActivityControllerList.add(new PWSActivityController("MSS","2"));
                pwsActivityControllerList.add(new PWSActivityController("LMIK","1"));
                pwsActivityControllerList.add(new PWSActivityController("BGT","2"));
                pwsActivityControllerList.add(new PWSActivityController("PC","2"));
                appDatabase.pwsActivityControllerDao().insert(pwsActivityControllerList);
                sharedPrefs.setKeySecondUpdateFlag("1");
            }

            if (sharedPrefs.getKeyFirstTimeDataFlag().equalsIgnoreCase("0")){
                List<ActivityList> activityLists = new ArrayList<>();
                activityLists.add(new ActivityList("4","en",
                        "Set Portfolio", "com.babbangona.mspalybookupgrade.SetPortfolio",
                        "1","MSS","0"));
                activityLists.add(new ActivityList("4","en",
                        "Set Portfolio", "com.babbangona.mspalybookupgrade.SetPortfolio",
                        "1","LMIK","0"));
                activityLists.add(new ActivityList("4","en",
                        "Set Portfolio", "com.babbangona.mspalybookupgrade.SetPortfolio",
                        "1","PC","0"));
                activityLists.add(new ActivityList("7","en",
                        "Threshing Activity", "com.babbangona.mspalybookupgrade.ThreshingViews.ThreshingActivity",
                        "1","BGT","0"));
                appDatabase.activityListDao().insert(activityLists);

                List<PWSCategoryList> pwsCategoryLists = new ArrayList<>();
                pwsCategoryLists.add(new PWSCategoryList("Flood","MSB","0"));
                pwsCategoryLists.add(new PWSCategoryList("Drought","MSB","0"));
                appDatabase.pwsCategoryListDao().insert(pwsCategoryLists);
                sharedPrefs.setKeyFirstTimeDataFlag("1");
            }

        });
    }
}

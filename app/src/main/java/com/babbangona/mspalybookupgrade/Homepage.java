package com.babbangona.mspalybookupgrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.ActivityListRecycler.ActivityListAdapter;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.network.ActivityListDownloadService;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.babbangona.mspalybookupgrade.utils.Main2ActivityMethods;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Homepage extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.toolbar_landing)
    MaterialToolbar toolbar_landing;

    @BindView(R.id.mtv_staff_id)
    MaterialTextView mtv_staff_id;

    @BindView(R.id.mtv_staff_name)
    MaterialTextView mtv_staff_name;

    @BindView(R.id.mtv_app_version)
    MaterialTextView mtv_app_version;

    @BindView(R.id.mtv_user_role)
    MaterialTextView mtv_user_role;

    @BindView(R.id.appbar)
    AppBarLayout appbar;

    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbar;

    SharedPrefs sharedPrefs;

    private static final int PERMISSIONS_REQUEST_CODE = 4043;

    String[] appPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
    };

    Main2ActivityMethods main2ActivityMethods;

    ActivityListAdapter activityListAdapter;

    AppDatabase appDatabase;

    boolean doubleBackToExitPressedOnce = false;

    ProgressDialog pd;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        ButterKnife.bind(this);
        sharedPrefs = new SharedPrefs(Homepage.this);
        main2ActivityMethods = new Main2ActivityMethods(Homepage.this);
        appDatabase = AppDatabase.getInstance(Homepage.this);
        GPSController.initialiseLocationListener(Homepage.this);
        setSupportActionBar(toolbar_landing);
        collapsingToolbarTitle();
        displayUserDetails();
        checkActivityListForSyncDialog();
        confirmPhoneDate();
        mHandler = new Handler();
        runOnUiThread(this::initActivitiesRecycler);
        pd = new ProgressDialog(Homepage.this);
        startRepeatingTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.homepage_menu_sync:
                if(checkAndRequestPermissions()){
                    if(isNetworkConnectionAvailable()){
                        dialogWithSync();
                    }else{
                        checkNetworkConnection();
                    }
                }
                return true;
            case R.id.homepage_menu_sync_summary:
                openSyncSummary();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void dialogWithSync(){
        syncRecords();
        runOnUiThread(this::initActivitiesRecycler);
        activityListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSIONS_REQUEST_CODE){
            HashMap<String, Integer> permissionResults = new HashMap<>();
            int deniedCount = 0;

            //Gather permission grant results
            for(int i = 0; i < grantResults.length; i++){
                //Add only permissions which are denied
                if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionResults.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }

            //Check if all permissions are granted
            if(deniedCount == 0){
                //Proceed ahead with the app.
                //Toast.makeText(this, "We got to true", Toast.LENGTH_LONG).show();
                dialogWithSync();
            }else{
                //At least one or all permissions are denied
                for(Map.Entry<String, Integer> entry : permissionResults.entrySet()){
                    String perName = entry.getKey();
                    int permResult = entry.getValue();

                    //Permission is denied (this is the first time, when "never ask again" is not checked)
                    //so ask again explaining the usage of permission
                    //shouldShowRequestPermissionRationale will return true.
                    if(ActivityCompat.shouldShowRequestPermissionRationale(this, perName)){
                        //Show a dialog here, explaining why and a button to take them back to request again.
                    }else{
                        //Permission is denied (and "never ask again" is checked)
                        //shouldShowRequestPermissionRationale will return false.
                        //Hence ask or redirect the user to the settings page to manually allow permissions.
                    }
                    break;
                }
            }
        }
    }

    public boolean checkAndRequestPermissions(){
        //Check which permissions are granted
        List<String> listPermissionsNeeded = new ArrayList<>();
        for(String perm : appPermissions){
            if(ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED){
                listPermissionsNeeded.add(perm);
            }
        }
        //Ask for non-granted permissions
        if(!listPermissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(Homepage.this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
    }

    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            Log.d("Network", "Connected");
            return true;
        }
        else{
//            checkNetworkConnection();
            Log.d("Network","Not Connected");
            return false;
        }
    }

    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setIcon(R.drawable.ic_no_data_connection);
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void collapsingToolbarTitle() {
        //Use this function to display the title in the collapsing toolbar, ONLY when the toolbar has been collapsed.
        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getResources().getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    //Remove the menu here too.
                    isShow = false;
                }
            }
        });
    }

    public void displayUserDetails(){
        mtv_staff_name.setText(sharedPrefs.getStaffName());
        mtv_staff_id.setText(sharedPrefs.getStaffID());
        mtv_user_role.setText(sharedPrefs.getStaffRole().toUpperCase());
        String app_version_text = "\u00a9" + " " + getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME;
        mtv_app_version.setText(app_version_text);
    }

    void syncRecords() {
        if (getCategory().equalsIgnoreCase("supr")){
            if (sharedPrefs.getKeyPortfolioList().isEmpty()){
                Toast.makeText(this, getResources().getString(R.string.set_portfolio_before_sync), Toast.LENGTH_SHORT).show();
            }else{
                Intent i = new Intent(this, ActivityListDownloadService.class);
                this.startService(i);
            }
        }else{
            Intent i = new Intent(this, ActivityListDownloadService.class);
            this.startService(i);
        }
    }

    public void initActivitiesRecycler(){

        appDatabase
                .activityListDao()
                .getAllActivityList(sharedPrefs.getKeyAppLanguage(),"%"+getCategory()+"%")
                .observe(this,activityLists -> {
                    activityListAdapter = new ActivityListAdapter(main2ActivityMethods.composingRecyclerList(activityLists), Homepage.this);
                    RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(Homepage.this);
                    recycler_view.setLayoutManager(aLayoutManager);
                    recycler_view.setItemAnimator(new DefaultItemAnimator());
                    recycler_view.setAdapter(activityListAdapter);
                    activityListAdapter.notifyDataSetChanged();
                });

    }

    void checkActivityListForSyncDialog(){
        if (appDatabase.activityListDao().countActivities() <= 1){
            showDialogForSync(getResources().getString(R.string.click_sync_message),Homepage.this);
        }
    }



    private void showDialogForSync(String s, Context context) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForSync(builder,s,context);
    }

    private void showDialogForSync(MaterialAlertDialogBuilder builder, String s, Context context) {
        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_warning))
                .setTitle(context.getResources().getString(R.string.attention))
                .setMessage(s)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    public void confirmPhoneDate(){
        //TODO --- Get the apps list last sync date for now, get a better one later.
        String str_default_date = getLastSyncTimeStaffList();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        Date def_date = null;
        try {
            def_date = sdf.parse(str_default_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (new Date().before(def_date)) {
            //Current Date is behind default date or last sync date, redirect
            new MaterialAlertDialogBuilder(Homepage.this)
                    .setIcon(getResources().getDrawable(R.drawable.ic_wrong_calendar))
                    .setTitle(getResources().getString(R.string.wrong_date_title))
                    .setMessage(getResources().getString(R.string.wrong_date_msg))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.change_date), (dialogInterface, i) -> {
                        startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                    }).show();
        }
    }

    String getLastSyncTimeStaffList(){
        String last_sync_time;
        try {
            last_sync_time = appDatabase.lastSyncTableDao().getLastSyncStaff(sharedPrefs.getStaffID());
        } catch (Exception e) {
            e.printStackTrace();
            last_sync_time = "2020-05-20 00:00:00";
        }
        if (last_sync_time == null || last_sync_time.equalsIgnoreCase("") ){
            last_sync_time = "2020-05-20 00:00:00";
        }
        return last_sync_time;
    }

    @Override
    public void onBackPressed() {
        //TODO
        if (doubleBackToExitPressedOnce) {
            finish();
            Intent homeScreenIntent = new Intent(Intent.ACTION_MAIN);
            homeScreenIntent.addCategory(Intent.CATEGORY_HOME);
            homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeScreenIntent);
        }else{
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getResources().getString(R.string.exit_app), Toast.LENGTH_SHORT).show();
        }


        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    void startRepeatingTask() {
        mSyncTask.run();
    }

    Runnable mSyncTask = new Runnable() {
        @Override
        public void run() {
            try {
                checkProgressDialogStatus(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                int mInterval = 500;
                mHandler.postDelayed(mSyncTask, mInterval);
            }
        }
    };

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mSyncTask);
    }

    public void checkProgressDialogStatus(){
        int progress_dialog_status = sharedPrefs.getKeyProgressDialogStatus();
        if (progress_dialog_status == 0) {
            pd.setTitle(getResources().getString(R.string.downloading_data));
            pd.setMessage(getResources().getString(R.string.please_wait));
            pd.setCancelable(false);
            pd.show();
        } else if (progress_dialog_status == 1) {
            //pd.show();
            pd.dismiss();
        }
    }

    String getCategory(){
        String category;
        try {
            category = appDatabase.categoryDao().getRoleCategory(sharedPrefs.getStaffRole());
        } catch (Exception e) {
            e.printStackTrace();
            category = "subd";
        }
        if (category == null || category.equalsIgnoreCase("")){
            category = "subd";
        }
        return category;
    }

    void openSyncSummary(){
        startActivity(new Intent(this, SyncSummary.class));
    }
}

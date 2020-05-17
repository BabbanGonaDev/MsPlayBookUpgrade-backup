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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.ActivityListRecycler.ActivityListAdapter;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.network.ActivityListDownloadService;
import com.babbangona.mspalybookupgrade.utils.Main2ActivityMethods;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        ButterKnife.bind(this);
        sharedPrefs = new SharedPrefs(Homepage.this);
        main2ActivityMethods = new Main2ActivityMethods(Homepage.this);
        appDatabase = AppDatabase.getInstance(Homepage.this);
        setSupportActionBar(toolbar_landing);
        collapsingToolbarTitle();
        displayUserDetails();
        runOnUiThread(this::initActivitiesRecycler);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void dialogWithSync(){
        ProgressDialog pd;
        pd = new ProgressDialog(Homepage.this);
        pd.setTitle(getResources().getString(R.string.downloading_data));
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();
        syncRecords();
        runOnUiThread(this::initActivitiesRecycler);
        pd.dismiss();
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
        Intent i = new Intent(this, ActivityListDownloadService.class);
        this.startService(i);
    }

    public void initActivitiesRecycler(){
        appDatabase
                .activityListDao()
                .getAllActivityList(sharedPrefs.getKeyAppLanguage(),"%"+sharedPrefs.getStaffRole()+"%")
                .observe(this,activityLists -> {
                    activityListAdapter = new ActivityListAdapter(main2ActivityMethods.composingRecyclerList(activityLists), Homepage.this);
                    RecyclerView.LayoutManager aLayoutManager = new LinearLayoutManager(Homepage.this);
                    recycler_view.setLayoutManager(aLayoutManager);
                    recycler_view.setItemAnimator(new DefaultItemAnimator());
                    recycler_view.setAdapter(activityListAdapter);
                    activityListAdapter.notifyDataSetChanged();
                });

    }

}

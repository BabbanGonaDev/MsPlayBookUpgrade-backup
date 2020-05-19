package com.babbangona.mspalybookupgrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.SetPortfolioRecycler.PortfolioPageListModelClass;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.SetPortfolioRecycler.SetAddedPortfolioAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.SetPortfolioRecycler.SetPortfolioAdapter;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.VerticalSpaceItemDecoration;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.StaffListDao;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.network.ActivityListDownloadService;
import com.babbangona.mspalybookupgrade.network.MsPlaybookInputDataDownloadService;
import com.babbangona.mspalybookupgrade.network.StaffListDownloadService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetPortfolio extends AppCompatActivity {

    @BindView(R.id.toolbar_portfolio)
    Toolbar toolbar_portfolio;

    @BindView(R.id.et_search)
    TextView et_search;

    @BindView(R.id.tv_all_staff)
    TextView tv_all_staff;

    @BindView(R.id.tv_added_staff)
    TextView tv_added_staff;

    @BindView(R.id.search_edit_text)
    EditText search_edit_text;

    @BindView(R.id.back_to_toolbar)
    ImageView back_to_toolbar;

    @BindView(R.id.search_linear_layout)
    LinearLayout search_linear_layout;

    @BindView(R.id.toolbar_linear_layout)
    LinearLayout toolbar_linear_layout;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.fab_download_flags)
    FloatingActionButton fab_download_flags;

    @BindView(R.id.fab_delete_flags)
    FloatingActionButton fab_delete_flags;

    PortfolioPageListModelClass portfolioPageListModelClass;

    SetPortfolioAdapter setPortfolioAdapter;

    SetAddedPortfolioAdapter setAddedPortfolioAdapter;

    AppDatabase appDatabase;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_portfolio);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_portfolio);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getResources().getString(R.string.set_portfolio_title));
        appDatabase = AppDatabase.getInstance(SetPortfolio.this);
        sharedPrefs = new SharedPrefs(SetPortfolio.this);
        showView(toolbar_linear_layout);
        hideView(search_linear_layout);
        hideView(fab_delete_flags);
        showView(fab_download_flags);

        toolbar_portfolio.setNavigationOnClickListener(view -> loadPreviousActivity());

        portfolioPageListModelClass = new ViewModelProvider(this, new MyViewModelFactory(appDatabase.staffListDao(), this)).get(PortfolioPageListModelClass.class);
        portfolioPageListModelClass.filterTextAll.setValue("");

        tv_all_staff.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv_all_staff.setTextColor(getResources().getColor(R.color.text_color_white));
        int offset;
        if (sharedPrefs.getKeyAdapterOffset1() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffset1(1);
        }else{
            offset = 0;
        }
        setAdapter(offset);
    }

    @OnClick(R.id.fab_download_flags)
    public void setFab_download_flags(){
        Set<String> portfolioSet = sharedPrefs.getKeyPortfolioList();
        if (portfolioSet.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.select_portfolio_before_downloading), Toast.LENGTH_SHORT).show();
        }else{
            showDialogSetPortfolioConfirmation(getResources().getString(R.string.confirm_set_portfolio),SetPortfolio.this);
        }
    }

    @OnClick(R.id.fab_delete_flags)
    public void setFab_delete_flags(){
        Set<String> addedPortfolioSet = sharedPrefs.getKeyAddedPortfolioList();
        if (addedPortfolioSet.isEmpty()){
            Toast.makeText(this, getResources().getString(R.string.select_portfolio_before_deleting), Toast.LENGTH_SHORT).show();
        }else{
            showDialogDeletePortfolioConfirmation(getResources().getString(R.string.confirm_deleting_portfolio),SetPortfolio.this);
        }
    }

    void setAllStaffAdapter(){
        portfolioPageListModelClass = new ViewModelProvider(this, new MyViewModelFactory(appDatabase.staffListDao(), this)).get(PortfolioPageListModelClass.class);
        portfolioPageListModelClass.filterTextAll.setValue("");
        int offset;
        if (sharedPrefs.getKeyAdapterOffset1() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffset1(1);
        }else{
            offset = 0;
        }
        setAdapter(offset);
    }

    void setAddedAdapter(){
        portfolioPageListModelClass = new ViewModelProvider(this, new MyViewModelFactory(appDatabase.staffListDao(), this)).get(PortfolioPageListModelClass.class);
        portfolioPageListModelClass.filterTextAllAnother.setValue("");
        int offset;
        if (sharedPrefs.getKeyAdapterOffset() == 0){
            offset = 1;
            sharedPrefs.setKeyAdapterOffset(1);
        }else{
            offset = 0;
        }
        setAddedPortfolioAdapterRecycler(offset);
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
            ActivityCompat.requestPermissions(SetPortfolio.this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
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

    void dialogWithSync(){
        ProgressDialog pd;
        pd = new ProgressDialog(SetPortfolio.this);
        pd.setTitle(getResources().getString(R.string.downloading_data));
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();
        syncRecords();
        runOnUiThread(this::setAllStaffAdapter);
        pd.dismiss();
    }

    void dialogInputRecordWithSync(){
        ProgressDialog pd;
        pd = new ProgressDialog(SetPortfolio.this);
        pd.setTitle(getResources().getString(R.string.downloading_data));
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();
        syncInputRecords();
        pd.dismiss();
    }

    void syncRecords() {
        Intent i = new Intent(this, StaffListDownloadService.class);
        this.startService(i);
    }

    void syncInputRecords(){
        Intent i = new Intent(this, ActivityListDownloadService.class);
        this.startService(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.portfolio_menu, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if(checkAndRequestPermissions()){
                if(isNetworkConnectionAvailable()){
                    dialogWithSync();
                }else{
                    checkNetworkConnection();
                }
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    @OnClick(R.id.tv_added_staff)
    public void added_staff(View view){
        tv_added_staff.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv_added_staff.setTextColor(getResources().getColor(R.color.text_color_white));

        tv_all_staff.setBackgroundColor(getResources().getColor(R.color.text_color_white));
        tv_all_staff.setTextColor(getResources().getColor(R.color.text_color_black));
        fab_download_flags.hide();
        fab_delete_flags.show();
        try{
            setAddedAdapter();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClick(R.id.tv_all_staff)
    public void all_staff(View view){

        tv_all_staff.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv_all_staff.setTextColor(getResources().getColor(R.color.text_color_white));

        tv_added_staff.setBackgroundColor(getResources().getColor(R.color.text_color_white));
        tv_added_staff.setTextColor(getResources().getColor(R.color.text_color_black));
        try{
            setAllStaffAdapter();
        }catch (Exception e){
            e.printStackTrace();
        }
        fab_delete_flags.hide();
        fab_download_flags.show();
    }

    @OnClick(R.id.et_search)
    public void move_to_search(View view){
        hideView(toolbar_linear_layout);
        showView(search_linear_layout);
        search_edit_text.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).showSoftInput(search_edit_text, InputMethodManager.SHOW_IMPLICIT);
    }

    @OnClick(R.id.back_to_toolbar)
    public void move_away_from_search(View view){
        removeSearchTray();
    }

    void removeSearchTray(){
        showView(toolbar_linear_layout);
        hideView(search_linear_layout);
        search_edit_text.setText(null);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(et_search.getWindowToken(), 0);
    }

    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public void loadPreviousActivity() {
        finish();
        startActivity(new Intent(SetPortfolio.this, Homepage.class));
    }

    public void textWatcher(EditText editText, PortfolioPageListModelClass portfolioPageListModelClass) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).isEmpty()){
                    portfolioPageListModelClass.filterTextAll.setValue("%%");
                }else{
                    portfolioPageListModelClass.filterTextAll.setValue("%" + s + "%");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void textWatcherAdded(EditText editText, PortfolioPageListModelClass portfolioPageListModelClass) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (String.valueOf(s).isEmpty()){
                    portfolioPageListModelClass.filterTextAllAnother.setValue("%%");
                }else{
                    portfolioPageListModelClass.filterTextAllAnother.setValue("%" + s + "%");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setAdapter(int offset) {
        setPortfolioAdapter = new SetPortfolioAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (offset == 1){
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
            VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
            recycler_view.addItemDecoration(verticalSpaceItemDecoration);
        }
        recycler_view.setLayoutManager(layoutManager);
        portfolioPageListModelClass.setPortfolioRecyclerModelList.observe(this,setPortfolioAdapter::submitList);
        recycler_view.setAdapter(setPortfolioAdapter);

        textWatcher(search_edit_text,portfolioPageListModelClass);

    }

    private void setAddedPortfolioAdapterRecycler(int offset) {
        setAddedPortfolioAdapter = new SetAddedPortfolioAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        if (offset == 1){
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
            VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(smallPadding);
            recycler_view.addItemDecoration(verticalSpaceItemDecoration);
        }
        recycler_view.setLayoutManager(layoutManager);
        portfolioPageListModelClass.setPortfolioRecyclerModelList1.observe(this,setAddedPortfolioAdapter::submitList);
        recycler_view.setAdapter(setAddedPortfolioAdapter);

        textWatcherAdded(search_edit_text,portfolioPageListModelClass);

    }

    public static class MyViewModelFactory implements ViewModelProvider.Factory {
        private StaffListDao application;
        private  Context context;


        MyViewModelFactory(StaffListDao application, Context context) {
            this.application = application;
            this.context = context;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new PortfolioPageListModelClass(application, context);
        }
    }

    private void showDialogSetPortfolioConfirmation(String s, Context context) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogSetPortfolioConfirmation(builder,s,context);
    }

    private void showDialogSetPortfolioConfirmation(MaterialAlertDialogBuilder builder, String s, Context context) {
        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_warning))
                .setTitle(context.getResources().getString(R.string.attention))
                .setMessage(s)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    dialogInputRecordWithSync();
                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private void showDialogDeletePortfolioConfirmation(String s, Context context) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogDeletePortfolioConfirmation(builder,s,context);
    }

    private void showDialogDeletePortfolioConfirmation(MaterialAlertDialogBuilder builder, String s, Context context) {
        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_warning))
                .setTitle(context.getResources().getString(R.string.attention))
                .setMessage(s)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    deletePortfolio(sharedPrefs.getKeyAddedPortfolioList());
                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    void deletePortfolio(Set<String> set_portfolio_list){
        for (String x: set_portfolio_list) {
            appDatabase.fieldsDao().deleteRecords(x);
        }
        Set<String> get_set_portfolio = sharedPrefs.getKeyPortfolioList();
        Set<String> get_added_portfolio = sharedPrefs.getKeyAddedPortfolioList();
        if (!get_set_portfolio.isEmpty()){
            get_set_portfolio.removeAll(get_added_portfolio);
            sharedPrefs.setKeyPortfolioList(get_set_portfolio);
        }
        if (!get_added_portfolio.isEmpty()){
            get_added_portfolio.clear();
            sharedPrefs.setKeyAddedPortfolioList(get_added_portfolio);
        }
    }

    @Override
    public void onBackPressed() {
        if (search_linear_layout.getVisibility() == View.VISIBLE){
            removeSearchTray();
        }else{
            loadPreviousActivity();
        }
    }
}

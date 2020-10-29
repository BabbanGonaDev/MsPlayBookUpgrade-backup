package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler.ThreshingFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogThreshingHG extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.last_sync_date_tv)
    TextView last_sync_date_tv;

    @BindView(R.id.tv_staff_id)
    TextView tv_staff_id;

    @BindView(R.id.actHGReason)
    AutoCompleteTextView actHGReason;

    @BindView(R.id.edlHGReason)
    TextInputLayout edlHGReason;

    @BindView(R.id.edtOtherReason)
    TextInputEditText edtOtherReason;

    @BindView(R.id.edlOtherReason)
    TextInputLayout edlOtherReason;

    @BindView(R.id.btnSubmit)
    MaterialButton btnSubmit;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    SetPortfolioMethods setPortfolioMethods;

    String reason_selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_threshing_h_g);
        ButterKnife.bind(LogThreshingHG.this);
        appDatabase = AppDatabase.getInstance(LogThreshingHG.this);
        sharedPrefs = new SharedPrefs(LogThreshingHG.this);
        setPortfolioMethods = new SetPortfolioMethods();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(LogThreshingHG.this));

        edlOtherReason.setVisibility(View.GONE);

        fillCollectionCenterSpinner(actHGReason, LogThreshingHG.this);

        toolbar.setNavigationOnClickListener(v -> leaveScreen());

        actHGReason.setOnItemClickListener((parent, view, position, id) -> {
            reason_selected = (String)parent.getItemAtPosition(position);
            showOtherReason(reason_selected);
        });
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,LogThreshingHG.this);
    }

    void showOtherReason(String reason_selected){
        if (reason_selected.equalsIgnoreCase("others")){
            edlOtherReason.setVisibility(View.VISIBLE);
        }else{
            edlOtherReason.setVisibility(View.GONE);
            edtOtherReason.setText("");
        }
    }

    @OnClick(R.id.btnSubmit)
    public  void setBtnSubmit(){
        if (actHGReason.getText().toString().equalsIgnoreCase("")){
            showProblemStart(getResources().getString(R.string.enter_reason), this);
            checkForEmptyReason();
        }else if(actHGReason.getText().toString().equalsIgnoreCase("others") &&
                edtOtherReason.getText().toString().equalsIgnoreCase("")){
            showProblemStart(getResources().getString(R.string.enter_reason), this);
            checkForEmptyReasonOther();
        }else{
            checkForEmptyReason();
            checkForEmptyReasonOther();
            showLogHGFinish(this, getReason_selected());
        }
    }

    String getReason_selected(){
        if (actHGReason.getText().toString().equalsIgnoreCase("others")){
            return edtOtherReason.getText().toString();
        }else{
            return actHGReason.getText().toString();
        }
    }

    private void showConfirmSuccess(String message, Context context){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showConfirmSuccessBody(builder, message, context);
    }

    private void showConfirmSuccessBody(MaterialAlertDialogBuilder builder, String message,
                                        Context context) {

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_check_green_24dp))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    leaveScreen();
                })
                .setCancelable(false)
                .show();

    }

    private void showProblemStart(String message, Context context){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showProblemBody(builder, message, context);
    }

    private void showProblemBody(MaterialAlertDialogBuilder builder, String message,
                                     Context context) {

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setTitle(context.getResources().getString(R.string.oops))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.go_back), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    void saveHG(String reason){

        GPSController.LocationGetter locationGetter;
        locationGetter = GPSController.initialiseLocationListener(this);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();

        FieldListRecyclerModel fieldListRecyclerModel = appDatabase.fieldsDao().getFieldCompleteDetails(sharedPrefs.getKeyThreshingUniqueFieldId());

        appDatabase.hgActivitiesFlagDao().insert(new HGActivitiesFlag(
                sharedPrefs.getKeyThreshingUniqueFieldId(), "HG_At_Risk",getDate(""),
                "1", sharedPrefs.getStaffID(),"0",fieldListRecyclerModel.getIk_number(),
                fieldListRecyclerModel.getCrop_type(),getDate("spread"),sharedPrefs.getKeyThreshingThreshValue()+"_"+reason));

        appDatabase.logsDao().insert(new Logs(sharedPrefs.getKeyThreshingUniqueFieldId(),sharedPrefs.getStaffID(),
                "HG_At_Risk",getDate("normal"),sharedPrefs.getStaffRole(),
                String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0",
                fieldListRecyclerModel.getIk_number(),fieldListRecyclerModel.getCrop_type()));

        showConfirmSuccess(getResources().getString(R.string.hg_log_completed), this);

    }

    private String getDeviceID(){
        String device_id;
        TelephonyManager tm = (TelephonyManager) Objects.requireNonNull(this).getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission. READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED){
            try {
                device_id = tm.getDeviceId();
            } catch (Exception e) {
                device_id = "";
            }
            if (device_id == null){
                device_id = "";
            }
        } else{
            device_id = "";
        }
        return device_id;
    }

    private String getDate(String module){

        SimpleDateFormat dateFormat1;
        if (module.matches("concat")) {
            dateFormat1 = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault());
        }else if (module.matches("spread")) {
            dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }else{
            dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }

        Date date1 = new Date();
        return dateFormat1.format(date1);
    }

    String getIssuesList(){
        String issues_list;
        try {
            issues_list = appDatabase.appVariablesDao().getIssuesList("1");
        } catch (Exception e) {
            e.printStackTrace();
            issues_list = "Find the maize to thresh,Rumors of member planning not to market his/her minimum commitment,Others";
        }
        if (issues_list == null || issues_list.equalsIgnoreCase("") ){
            issues_list = "Find the maize to thresh,Rumors of member planning not to market his/her minimum commitment,Others";
        }
        return issues_list;
    }

    public void fillCollectionCenterSpinner(AutoCompleteTextView autoCompleteTextView, Context context) {
        ArrayAdapter village;
        village = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, getIssuesList().split(","));
        spinnerViewController(autoCompleteTextView,village);
    }

    public void spinnerViewController(AutoCompleteTextView autoCompleteTextView, ArrayAdapter arrayAdapter) {
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    void checkForEmptyReason(){
        if (actHGReason.getText().toString().equalsIgnoreCase("")) {
            setErrorOfTextView(edlHGReason,getResources().getString(R.string.enter_reason));
        }else{
            removeErrorFromText(edlHGReason);
        }
    }

    void checkForEmptyReasonOther(){
        if (actHGReason.getText().toString().equalsIgnoreCase("others") &&
                edtOtherReason.getText().toString().equalsIgnoreCase("")){
            setErrorOfTextView(edlOtherReason,getResources().getString(R.string.enter_reason));
        }else{
            removeErrorFromText(edlOtherReason);
        }
    }

    public void setErrorOfTextView(TextInputLayout textInputLayout, String error_message) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(error_message);
    }

    public void removeErrorFromText(TextInputLayout textInputLayout) {
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }

    private void showLogHGFinish(Context context, String reason){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showLogHGFinishBody(builder,
                context.getResources().getString(R.string.confirm_reason_title) +" "+reason,
                context, reason);
    }

    private void showLogHGFinishBody(MaterialAlertDialogBuilder builder, String message,
                                     Context context, String reason) {

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    //save hg
                    saveHG(reason);

                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    void leaveScreen(){
        finish();
    }
}
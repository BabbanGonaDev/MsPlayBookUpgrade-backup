package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UrgentThreshingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.last_sync_date_tv)
    TextView last_sync_date_tv;

    @BindView(R.id.tv_staff_id)
    TextView tv_staff_id;

    @BindView(R.id.edtOtherReason)
    TextInputEditText edtOtherReason;

    @BindView(R.id.edlOtherReason)
    TextInputLayout edlOtherReason;

    @BindView(R.id.btnSubmit)
    MaterialButton btnSubmit;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    SetPortfolioMethods setPortfolioMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_threshing);
        ButterKnife.bind(UrgentThreshingActivity.this);
        appDatabase = AppDatabase.getInstance(UrgentThreshingActivity.this);
        sharedPrefs = new SharedPrefs(UrgentThreshingActivity.this);
        setPortfolioMethods = new SetPortfolioMethods();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(UrgentThreshingActivity.this));

        textWatcher(edtOtherReason, edlOtherReason, getResources().getString(R.string.enter_reason));


        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,UrgentThreshingActivity.this);
    }

    @OnClick(R.id.btnSubmit)
    public  void setBtnSubmit(){
        if (edtOtherReason.getText().toString().equalsIgnoreCase("")){
            checkForEmptyReasonOther();
        }else{
            checkForEmptyReasonOther();
            showLogUrgentFinish(this, edtOtherReason.getText().toString());
        }
    }

    void checkForEmptyReasonOther(){
        if (edtOtherReason.getText().toString().equalsIgnoreCase("")){
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

    private void showLogUrgentFinish(Context context, String reason){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showLogUrgentFinishBody(builder,
                context.getResources().getString(R.string.confirm_urgent_threshing_reason_title) +" "+reason,
                context, reason);
    }

    private void showLogUrgentFinishBody(MaterialAlertDialogBuilder builder, String message,
                                     Context context, String reason) {

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    //save hg
                    saveUrgentFlag(reason);

                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    void saveUrgentFlag(String reason){

        GPSController.LocationGetter locationGetter;
        locationGetter = GPSController.initialiseLocationListener(this);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();


        updateUrgentActivity(sharedPrefs.getKeyThreshingUniqueFieldId(),
                String.valueOf(latitude),
                String.valueOf(longitude),
                sharedPrefs.getKeyThreshingIkNumber(),
                sharedPrefs.getKeyThreshingCropType(),reason);

    }

    private void updateUrgentActivity(String unique_field_id, String latitude, String longitude,
                                      String ik_number, String crop_type, String reason){

        //String unique_field_id, String schedule_date, String reschedule_reason, String staff_id, String date_logged

        appDatabase.scheduleThreshingActivitiesFlagDao().updateUrgentScheduleDate(
                unique_field_id,
                "0000-00-00",
                sharedPrefs.getStaffID(),
                getDate("spread"),
                reason,
                BuildConfig.VERSION_NAME
        );

        appDatabase.logsDao().insert(new Logs(unique_field_id,sharedPrefs.getStaffID(),
                "Re Schedule Urgent threshing "+ reason,getDate("normal"),sharedPrefs.getStaffRole(),
                latitude, longitude, getDeviceID(),"0",
                ik_number, crop_type));


        showSuccessStart(getResources().getString(R.string.re_threshing_success),this);
    }

    private void showSuccessStart(String message, Context context){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showSuccessBody(builder, message, context);
    }

    private void showSuccessBody(MaterialAlertDialogBuilder builder, String message,
                                 Context context) {

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_smiley_face))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.done), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    goToHomePage();
                })
                .setCancelable(false)
                .show();
    }

    void goToHomePage(){
        finish();
        Intent intent = new Intent(UrgentThreshingActivity.this, ThreshingActivity.class);
        startActivity(intent);
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

    private String getDeviceID(){
        String device_id;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission. READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED){
            try {
                device_id = tm.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
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

    void textWatcher(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String error_message) {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Objects.requireNonNull(textInputEditText.getText()).toString().length() > 0){
                    if (!validateFields(textInputEditText)){
                        setErrorOfTextView(textInputLayout,error_message);
                    }else {
                        removeErrorFromText(textInputLayout);
                    }
                }else{
                    removeErrorFromText(textInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean validateFields(TextInputEditText textInputEditText){
        String watchWord = Objects.requireNonNull(textInputEditText.getText()).toString();
        return watchWord.length() >= 2;
    }
}
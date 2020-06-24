package com.babbangona.mspalybookupgrade.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.babbangona.mspalybookupgrade.FieldListPage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CustomDialogFragmentFertilizer extends DialogFragment {

    private Unbinder unbinder;

    @BindView(R.id.toolbar_hg_fragment)
    Toolbar toolbar_hg_fragment;

    @BindView(R.id.tv_field_r_id)
    TextView tv_field_r_id;

    @BindView(R.id.tv_member_r_id)
    TextView tv_member_r_id;

    @BindView(R.id.tv_ik_number)
    TextView tv_ik_number;

    @BindView(R.id.tv_member_name)
    TextView tv_member_name;

    @BindView(R.id.tv_phone_number)
    TextView tv_phone_number;

    @BindView(R.id.tv_field_size)
    TextView tv_field_size;

    @BindView(R.id.tv_village_name)
    TextView tv_village_name;

    @BindView(R.id.tv_crop_type)
    TextView tv_crop_type;

    @BindView(R.id.tv_enter_date)
    TextView tv_enter_date;

    @BindView(R.id.tv_confirm_date)
    TextView tv_confirm_date;

    @BindView(R.id.btnAddHGActivity)
    MaterialButton btn_log_activity;

    @BindView(R.id.activityImage)
    ImageView activityImage;

    private SharedPrefs sharedPrefs;

    private SetPortfolioMethods setPortfolioMethods;

    private AppDatabase appDatabase;

    FieldListRecyclerModel fieldListRecyclerModel;

    private static final int CAMERA_REQUEST = 1888;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    Bitmap photo = null;

    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View fragmentLayout = inflater.inflate(R.layout.log_fragment_fertilizer, container, false);
        ButterKnife.bind(this, fragmentLayout);
        unbinder = ButterKnife.bind(this, fragmentLayout);
        setPortfolioMethods = new SetPortfolioMethods();
        appDatabase = AppDatabase.getInstance(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());
        fieldListRecyclerModel = new FieldListRecyclerModel();
        fieldListRecyclerModel = sharedPrefs.getKeyFieldModel();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar_hg_fragment);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle(getActivityTitleName());
        GPSController.initialiseLocationListener(getActivity());
        sharedPrefs.setKeyImageCaptureFlag("0");

        toolbar_hg_fragment.setNavigationOnClickListener(v -> dismissAndRefresh());

        String field_r_id = fieldListRecyclerModel.getUnique_field_id();
        String ik_number = Objects.requireNonNull(getActivity()).getResources().getString(R.string.ik_number) +" "+ fieldListRecyclerModel.getIk_number();
        String member_name = getActivity().getResources().getString(R.string.member_name) +" "+ fieldListRecyclerModel.getMember_name();
        String phone_number = getActivity().getResources().getString(R.string.member_phone_number) +" "+ fieldListRecyclerModel.getPhone_number();
        String field_size = getActivity().getResources().getString(R.string.field_size) +" "+ fieldListRecyclerModel.getField_size();
        String village = getActivity().getResources().getString(R.string.member_village) +" "+ fieldListRecyclerModel.getVillage_name();
        String crop_type = getActivity().getResources().getString(R.string.member_crop_type) +" "+ fieldListRecyclerModel.getCrop_type();
        String member_r_id = getActivity().getResources().getString(R.string.member_r_id) +" "+ fieldListRecyclerModel.getField_r_id();
        tv_member_r_id.setText(member_r_id);

        tv_field_r_id.setText(field_r_id);
        tv_ik_number.setText(ik_number);
        tv_member_name.setText(member_name);
        tv_phone_number.setText(phone_number);
        tv_field_size.setText(field_size);
        tv_village_name.setText(village);
        tv_crop_type.setText(crop_type);
        tv_confirm_date.setEnabled(false);
        activityImage.setVisibility(View.GONE);

        allowButton();
        btn_log_activity.setText(getActivityText());

        return fragmentLayout;
    }

    @OnClick(R.id.tv_enter_date)
    void enterDate(){
        getCalenderDate(tv_enter_date, "Select "+getActivityText(), getActivity());
        tv_confirm_date.setEnabled(true);
        clearTextViewText(tv_confirm_date);
        activityImage.setVisibility(View.GONE);
        sharedPrefs.setKeyImageCaptureFlag("0");
    }

    @OnClick(R.id.tv_confirm_date)
    void confirmDate(){
        getCalenderDate(tv_confirm_date, "Confirm "+getActivityText(), getActivity());
        activityImage.setVisibility(View.GONE);
        sharedPrefs.setKeyImageCaptureFlag("0");
    }

    void allowButton(){
        if (validateFieldsInfo(tv_enter_date,tv_confirm_date) == 0){
            btn_log_activity.setEnabled(false);
        }else{
            btn_log_activity.setEnabled(true);
        }
    }

    /**
     * Very important to unbind when using butterKnife with fragments
     */
    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     *  The system calls this only when creating the layout in a dialog.
     */
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @OnClick(R.id.btnAddHGActivity)
    public  void next(View view){
        if (validateFieldsInfo(tv_enter_date,tv_confirm_date) == 0){
            checkForEmptyFields();
        }else if (getDateCorrelationFlag(
                Objects.requireNonNull(tv_enter_date.getText()).toString().trim(),
                getMinimumLogDate(),getMaximumLogDate()) == 0){
            setErrorOfTextView(tv_enter_date,Objects.requireNonNull(getActivity()).getResources().getString(R.string.error_date_abnormal));
            setErrorOfTextView(tv_confirm_date,Objects.requireNonNull(getActivity()).getResources().getString(R.string.error_date_abnormal));
            Toast.makeText(getActivity(), Objects.requireNonNull(getActivity()).getResources().getString(R.string.error_date_abnormal), Toast.LENGTH_LONG).show();
        }else{
            checkForEmptyFields();
            GPSController.LocationGetter locationGetter = GPSController.initialiseLocationListener(Objects.requireNonNull(getActivity()));
            double latitude = locationGetter.getLatitude();
            double longitude = locationGetter.getLongitude();
            fieldListRecyclerModel = sharedPrefs.getKeyFieldModel();
            if (sharedPrefs.getKeyImageCaptureFlag().equalsIgnoreCase("1")){
                savePictureAndClose(fieldListRecyclerModel, getActivityName(), latitude, longitude);
            }else{
                updateActivity(fieldListRecyclerModel,tv_enter_date.getText().toString(),latitude,longitude);
            }
        }
    }

    void checkForEmptyFields(){
        checkIfTextViewEmpty(tv_enter_date, Objects.requireNonNull(getActivity()).getResources().getString(R.string.error_message_enter_date));
        checkIfTextViewEmpty(tv_confirm_date, Objects.requireNonNull(getActivity()).getResources().getString(R.string.error_message_confirm_date));
    }

    public void clearTextViewText(TextView textView) {
        textView.setText("");
    }

    public int validateFieldsInfo(TextView tv_enter_date, TextView tv_confirm_date){

        // Checks if the state field is empty
        if(Objects.requireNonNull(tv_enter_date.getText()).toString().matches("")) {
            return 0;
        }else if(Objects.requireNonNull(tv_confirm_date.getText()).toString().matches("")) {
            return 0;
        }else if(tv_confirm_date.getText().toString().matches("")) {
            return 0;
        }else if(!tv_enter_date.getText().toString().matches(tv_confirm_date.getText().toString())) {
            return 0;
        }

        //all checks are passed
        else{
            return 1;
        }
    }

    public void checkIfTextViewEmpty(TextView TextView, String error_message) {
        String textEntered = Objects.requireNonNull(TextView.getText()).toString();
        if (textEntered.equalsIgnoreCase("")){
            setErrorOfTextView(TextView, error_message);
            Toast.makeText(getActivity(), error_message, Toast.LENGTH_SHORT).show();
        }else{
            removeErrorFromText(TextView);
        }
    }

    public void setErrorOfTextView(TextView TextView, String error_message) {
        TextView.setError(error_message);
    }

    public void removeErrorFromText(TextView TextView) {
        TextView.setError(null);
    }

    private void updateActivity(FieldListRecyclerModel fieldListRecyclerModel, String activity_date,
                                double latitude, double longitude){
        int field_existence = appDatabase.normalActivitiesFlagDao().countFieldInNormalActivity(fieldListRecyclerModel.getUnique_field_id());
        if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){
            if (field_existence > 0){
                appDatabase.normalActivitiesFlagDao().updateFert1Flag(fieldListRecyclerModel.getUnique_field_id(),"1",
                        activity_date,sharedPrefs.getStaffID(),getDate("spread"));
            }else{
                appDatabase.normalActivitiesFlagDao().insert(new NormalActivitiesFlag(fieldListRecyclerModel.getUnique_field_id(),
                        "1",activity_date,"0","0000-00-00",
                        sharedPrefs.getStaffID(),"0",fieldListRecyclerModel.getIk_number(),
                        fieldListRecyclerModel.getCrop_type(),"0",getDate("spread")));
            }
            appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                    "Log Fertilizer 1",getDate("normal"),sharedPrefs.getStaffRole(),
                    String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0",
                    fieldListRecyclerModel.getIk_number(),fieldListRecyclerModel.getCrop_type()));
            fieldListRecyclerModel.setFertilizer_1_status("1");
            dismissAndRefresh();
        }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("2")){
            if (field_existence > 0){
                appDatabase.normalActivitiesFlagDao().updateFert2Flag(fieldListRecyclerModel.getUnique_field_id(),"1",
                        activity_date,sharedPrefs.getStaffID(),getDate("spread"));
            }else{
                appDatabase.normalActivitiesFlagDao().insert(new NormalActivitiesFlag(fieldListRecyclerModel.getUnique_field_id(),
                        "0","0000-00-00","1",activity_date,
                        sharedPrefs.getStaffID(),"0",fieldListRecyclerModel.getIk_number(),
                        fieldListRecyclerModel.getCrop_type(),"0",getDate("spread")));
            }
            appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                    "Log Fertilizer 2",getDate("normal"),sharedPrefs.getStaffRole(),
                    String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0",
                    fieldListRecyclerModel.getIk_number(),fieldListRecyclerModel.getCrop_type()));
            fieldListRecyclerModel.setFertilizer_2_status("1");
            dismissAndRefresh();
        }
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

        java.util.Date date1 = new java.util.Date();
        return dateFormat1.format(date1);
    }

    private String getDeviceID(){
        String device_id;
        TelephonyManager tm = (TelephonyManager) Objects.requireNonNull(getActivity()).getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission. READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED){
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

    void dismissAndRefresh(){
        dismiss();
        ((FieldListPage) Objects.requireNonNull(getActivity())).myMethod();
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    String getActivityText(){
        String button_text;
        if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){
            button_text = Objects.requireNonNull(getActivity()).getResources().getString(R.string.log_fertilizer_1);
        }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("2")){
            button_text = Objects.requireNonNull(getActivity()).getResources().getString(R.string.log_fertilizer_2);
        }else{
            button_text = Objects.requireNonNull(getActivity()).getResources().getString(R.string.log_activity);
        }
       return button_text;
    }

    void getCalenderDate(TextView textView, String activity_string, Context context){

        //To show current date in the datePicker
        Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(context, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
            String text = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay ;
            textView.setText(text);
            allowButton();
        }, mYear, mMonth, mDay);
        activity_string = activity_string+" Date";
        mDatePicker.setTitle(activity_string);
        mDatePicker.show();
    }

    String getMinimumLogDate(){
        String minimum_log_date;
        try {
            minimum_log_date = appDatabase.appVariablesDao().getMinimumLogDate("1");
        } catch (Exception e) {
            e.printStackTrace();
            minimum_log_date = "2020-05-15";
        }
        if (minimum_log_date == null || minimum_log_date.equalsIgnoreCase("") ){
            minimum_log_date = "2020-05-15";
        }
        return minimum_log_date;
    }

    String getMaximumLogDate(){
        String maximum_log_date;
        try {
            maximum_log_date = appDatabase.appVariablesDao().getMaximumLogDate("1");
        } catch (Exception e) {
            e.printStackTrace();
            maximum_log_date = "2020-08-15";
        }
        if (maximum_log_date == null || maximum_log_date.equalsIgnoreCase("") ){
            maximum_log_date = "2020-08-15";
        }
        return maximum_log_date;
    }

    public int getDateCorrelationFlag(String selected_date, String reference_minimum_date,
                                      String reference_maximum_date){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Date selectedDate = sdf.parse(selected_date);
            Date referenceMinimumDate = sdf.parse(reference_minimum_date);
            Date referenceMaximumDate = sdf.parse(reference_maximum_date);
            Date todayDate = sdf.parse(today);

            Log.d("corr_min_date",reference_minimum_date+"|"+referenceMinimumDate);
            Log.d("corr_max_date",reference_maximum_date+"|"+referenceMaximumDate);
            Log.d("corr_selected_date",selected_date+"|"+selectedDate);
            // before() will return true if and only if date1 is before date2
            if (selectedDate != null) {
                if(selectedDate.before(referenceMinimumDate)){
                    return 0;
                }else if (selectedDate.after(referenceMaximumDate)){
                    return 0;
                }else if(selectedDate.after(todayDate)){
                    return 0;
                }else{
                    return 1;
                }
            }else{
                return 0;
            }
        } catch(ParseException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    @OnClick(R.id.btnTakePicture)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void capturePicture(View view){
        if (ContextCompat.checkSelfPermission(view.getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            Bitmap bitmap = Bitmap.createScaledBitmap(Objects.requireNonNull(photo), 400, 400, true);
            sharedPrefs.setKeyImageCaptureFlag("1");
            activityImage.setVisibility(View.VISIBLE);
            activityImage.setImageBitmap(bitmap);
        }
    }

    void savePictureAndClose(FieldListRecyclerModel fieldListRecyclerModel, String activity_type,
                             double latitude, double longitude){

        String image_name = activity_type + "_" + fieldListRecyclerModel.getUnique_field_id() + "_" + getDate("concat");

        String result = saveToSdCard(photo, image_name);
        if (result.equalsIgnoreCase("success")){
            updateActivity(fieldListRecyclerModel, Objects.requireNonNull(tv_enter_date.getText()).toString(),latitude,longitude);
            Toast.makeText(getActivity(), "Picture Saved", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Picture Not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    String saveToSdCard(Bitmap bitmap, String filename) {
        String stored = null;
        File ChkImgDirectory;
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            ChkImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.NORMAL_ACTIVITY_PICTURE_LOCATION);

            File file, file3;
            File file1 = new File(ChkImgDirectory.getAbsoluteFile(), filename + ".png");
            File file2 = new File(ChkImgDirectory.getAbsoluteFile(), ".nomedia");
            if (!ChkImgDirectory.exists() && !ChkImgDirectory.mkdirs()) {
                boolean x = ChkImgDirectory.mkdir();
                Log.d("is_file_created", String.valueOf(x));
                file = file1;
                file3 = file2;
                if (!file3.exists()) {
                    try {
                        FileOutputStream outNoMedia = new FileOutputStream(file3);
                        outNoMedia.flush();
                        outNoMedia.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (file.exists()) {
                    stored = "fileExists";
                } else {
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        stored = "success";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                file = file1;
                file3 = file2;
                if (!file3.exists()) {
                    try {
                        FileOutputStream outNoMedia = new FileOutputStream(file3);
                        outNoMedia.flush();
                        outNoMedia.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (file.exists()) {
                    stored = "fileExists";
                } else {
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        stored = "success";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return stored;
    }

    String getActivityName(){
        if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){
            return "Fert_1";
        }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){
            return "Fert_2";
        }else{
            return "Fert";
        }
    }

    String getActivityTitleName(){
        if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){
            return "Fertilizer 1";
        }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){
            return "Fertilizer 2";
        }else{
            return "Fertilizer";
        }
    }

}

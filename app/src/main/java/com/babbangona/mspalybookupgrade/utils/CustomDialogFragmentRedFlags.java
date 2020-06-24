package com.babbangona.mspalybookupgrade.utils;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.babbangona.mspalybookupgrade.HGFieldListPage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler.HGFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.RFActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class CustomDialogFragmentRedFlags extends DialogFragment {

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

    @BindView(R.id.tv_hg_list)
    TextView tv_hg_list;

    @BindView(R.id.actHGType)
    AutoCompleteTextView actHGType;

    @BindView(R.id.edlHGType)
    TextInputLayout edlHGType;

    @BindView(R.id.btnAddHGActivity)
    MaterialButton btnAddHGActivity;

    @BindView(R.id.activityImage)
    ImageView activityImage;

    @BindView(R.id.tv_enter_date)
    MaterialTextView tv_enter_date;

    @BindView(R.id.tv_confirm_date)
    MaterialTextView tv_confirm_date;

    private SharedPrefs sharedPrefs;

    private SetPortfolioMethods setPortfolioMethods;

    private AppDatabase appDatabase;

    String hg_type_selection;

    HGFieldListRecyclerModel hgFieldListRecyclerModel;

    private GPSController.LocationGetter locationGetter;

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
        View fragmentLayout = inflater.inflate(R.layout.hg_log_fragment_rf, container, false);
        ButterKnife.bind(this, fragmentLayout);
        unbinder = ButterKnife.bind(this, fragmentLayout);
        setPortfolioMethods = new SetPortfolioMethods();
        appDatabase = AppDatabase.getInstance(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());
        hgFieldListRecyclerModel = new HGFieldListRecyclerModel();
        hgFieldListRecyclerModel = sharedPrefs.getKeyHgFieldModel();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar_hg_fragment);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Red Flag Activity");
        GPSController.initialiseLocationListener(getActivity());
        sharedPrefs.setKeyImageCaptureFlag("0");

        toolbar_hg_fragment.setNavigationOnClickListener(v -> dismissAndRefresh());

        String field_r_id = hgFieldListRecyclerModel.getUnique_field_id();
        String ik_number = Objects.requireNonNull(getActivity()).getResources().getString(R.string.ik_number) +" "+ hgFieldListRecyclerModel.getIk_number();
        String member_name = getActivity().getResources().getString(R.string.member_name) +" "+ hgFieldListRecyclerModel.getMember_name();
        String phone_number = getActivity().getResources().getString(R.string.member_phone_number) +" "+ hgFieldListRecyclerModel.getPhone_number();
        String field_size = getActivity().getResources().getString(R.string.field_size) +" "+ hgFieldListRecyclerModel.getField_size();
        String village = getActivity().getResources().getString(R.string.member_village) +" "+ hgFieldListRecyclerModel.getVillage_name();
        String crop_type = getActivity().getResources().getString(R.string.member_crop_type) +" "+ hgFieldListRecyclerModel.getCrop_type();
        String member_r_id = getActivity().getResources().getString(R.string.member_r_id) +" "+ hgFieldListRecyclerModel.getField_r_id();
        tv_member_r_id.setText(member_r_id);

        tv_field_r_id.setText(field_r_id);
        tv_ik_number.setText(ik_number);
        tv_member_name.setText(member_name);
        tv_phone_number.setText(phone_number);
        tv_field_size.setText(field_size);
        tv_village_name.setText(village);
        tv_crop_type.setText(crop_type);

        fillRFListSpinner(actHGType,getActivity());
        allowButton();

        actHGType.setOnItemClickListener((parent, view, position , rowId) -> {
            hg_type_selection = (String)parent.getItemAtPosition(position);
            allowButton();
            tv_enter_date.setEnabled(true);
            tv_confirm_date.setEnabled(false);
            activityImage.setVisibility(View.GONE);
            sharedPrefs.setKeyImageCaptureFlag("0");
        });

        int status = appDatabase.rfActivitiesFlagDao().countFieldInRFActivity(hgFieldListRecyclerModel.getUnique_field_id());
        if (status > 0){
            tv_hg_list.setVisibility(View.VISIBLE);
            List<String> allFieldHGs = appDatabase.rfActivitiesFlagDao().getAllFieldRFs(hgFieldListRecyclerModel.getUnique_field_id());
            StringBuilder text = new StringBuilder(Objects.requireNonNull(getActivity()).getResources().getString(R.string.rf_list_header) +
                    "\n-----------------------\n");
            for (String x: allFieldHGs){
                text.append(x).append("\n");
            }
            tv_hg_list.setText(text);
        }else{
            tv_hg_list.setVisibility(View.GONE);
        }

        tv_enter_date.setEnabled(false);
        tv_confirm_date.setEnabled(false);
        activityImage.setVisibility(View.GONE);


        return fragmentLayout;
    }

    @OnClick(R.id.tv_enter_date)
    void enterDate(){
        getCalenderDate(tv_enter_date, "Select Activity Date", getActivity());
        tv_confirm_date.setEnabled(true);
        activityImage.setVisibility(View.GONE);
        clearTextViewText(tv_confirm_date);
        sharedPrefs.setKeyImageCaptureFlag("0");
    }

    @OnClick(R.id.tv_confirm_date)
    void confirmDate(){
        getCalenderDate(tv_confirm_date, "Confirm Activity Date", getActivity());
        activityImage.setVisibility(View.GONE);
        sharedPrefs.setKeyImageCaptureFlag("0");
    }

    void allowButton(){
        if (validateFieldsInfo(actHGType,tv_enter_date,tv_confirm_date) == 0){
            btnAddHGActivity.setEnabled(false);
        }else{
            btnAddHGActivity.setEnabled(true);
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
        if (validateFieldsInfo(actHGType, tv_enter_date, tv_confirm_date) == 0){
            checkForEmptyAutocompleteFields();
            checkForEmptyTextViewFields();
        }else if (getDateCorrelationFlag(
                Objects.requireNonNull(tv_enter_date.getText()).toString().trim(),
                getMinimumLogDate()) == 0){
            checkForEmptyAutocompleteFields();
            checkForEmptyTextViewFields();
            setErrorOfTextView(tv_enter_date,Objects.requireNonNull(getActivity()).getResources().getString(R.string.error_date_abnormal));
            setErrorOfTextView(tv_confirm_date,Objects.requireNonNull(getActivity()).getResources().getString(R.string.error_date_abnormal));
            Toast.makeText(getActivity(), Objects.requireNonNull(getActivity()).getResources().getString(R.string.error_date_abnormal), Toast.LENGTH_LONG).show();
        }else{
            checkForEmptyAutocompleteFields();
            checkForEmptyTextViewFields();
            GPSController.LocationGetter locationGetter = GPSController.initialiseLocationListener(Objects.requireNonNull(getActivity()));
            double latitude = locationGetter.getLatitude();
            double longitude = locationGetter.getLongitude();
            hgFieldListRecyclerModel = sharedPrefs.getKeyHgFieldModel();double min_lat = Double.parseDouble(hgFieldListRecyclerModel.getMin_lat());
            double max_lat = Double.parseDouble(hgFieldListRecyclerModel.getMax_lat());
            double min_lng = Double.parseDouble(hgFieldListRecyclerModel.getMin_lng());
            double max_lng = Double.parseDouble(hgFieldListRecyclerModel.getMax_lng());
            double mid_lat = (max_lat+min_lat)/2.0;
            double mid_lng = (max_lng+min_lng)/2.0;

            double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
            double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

            if (locationDistance <= allowedDistance){
                if (sharedPrefs.getKeyImageCaptureFlag().equalsIgnoreCase("1")){
                    savePictureAndClose(hgFieldListRecyclerModel,actHGType.getText().toString(),latitude,longitude);
                }else{
                    showCapturePictureDialogStart(getActivity().getResources().getString(R.string.take_location_picture),getActivity());
                }
            }else{
                updateActivity(hgFieldListRecyclerModel,actHGType.getText().toString(),
                        latitude,longitude,tv_enter_date.getText().toString());
            }
        }
    }

    void checkForEmptyAutocompleteFields(){
        checkIfAutocompleteEmpty(actHGType,edlHGType,this.getResources().getString(R.string.error_message_rf_type));
    }

    public void fillRFListSpinner(AutoCompleteTextView autoCompleteTextView, Context context) {
        List<String> whole_rf_list = appDatabase.rfListDao().getAllRFs("%"+setPortfolioMethods.getCategory(context)+"%");
        ArrayAdapter<String> rf_adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, whole_rf_list);
        spinnerViewController(autoCompleteTextView,rf_adapter);
    }

    public void spinnerViewController(AutoCompleteTextView autoCompleteTextView, ArrayAdapter<String> arrayAdapter) {
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    public void clearSpinnerText(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setText("");
    }

    public int validateFieldsInfo(AutoCompleteTextView hg_type, MaterialTextView tv_enter_date,
                                  MaterialTextView tv_confirm_date){

        // Checks if the state field is empty
        if(Objects.requireNonNull(hg_type.getText()).toString().matches("")) {
            return 0;
        }

        else if(Objects.requireNonNull(tv_enter_date.getText()).toString().matches("")) {
            return 0;
        }

        else if(Objects.requireNonNull(tv_confirm_date.getText()).toString().matches("")) {
            return 0;
        }

        else if(tv_confirm_date.getText().toString().matches("")) {
            return 0;
        }

        else if(!tv_enter_date.getText().toString().matches(tv_confirm_date.getText().toString())) {
            return 0;
        }

        //all checks are passed
        else{
            return 1;
        }
    }

    public void checkIfAutocompleteEmpty(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout, String error_message) {
        String textEntered = Objects.requireNonNull(autoCompleteTextView.getText()).toString();
        if (textEntered.equalsIgnoreCase("")){
            setErrorOfTextInputLayout(textInputLayout, error_message);
            Toast.makeText(getActivity(), error_message, Toast.LENGTH_SHORT).show();
        }else{
            removeErrorFromTextInputLayout(textInputLayout);
        }
    }

    public void setErrorOfTextInputLayout(TextInputLayout textInputLayout, String error_message) {
        textInputLayout.setErrorEnabled(true);
        textInputLayout.setError(error_message);
    }

    public void removeErrorFromTextInputLayout(TextInputLayout textInputLayout) {
        textInputLayout.setError(null);
        textInputLayout.setErrorEnabled(false);
    }

    public void checkIfTextViewEmpty(MaterialTextView materialTextView, String error_message) {
        String textEntered = Objects.requireNonNull(materialTextView.getText()).toString();
        if (textEntered.equalsIgnoreCase("")){
            setErrorOfTextView(materialTextView, error_message);
            Toast.makeText(getActivity(), error_message, Toast.LENGTH_SHORT).show();
        }else{
            removeErrorFromText(materialTextView);
        }
    }

    public void setErrorOfTextView(MaterialTextView materialTextView, String error_message) {
        materialTextView.setError(error_message);
    }

    public void removeErrorFromText(MaterialTextView materialTextView) {
        materialTextView.setError(null);
    }

    private void updateActivity(HGFieldListRecyclerModel hgFieldListRecyclerModel, String hg_selected,
                                double latitude, double longitude, String activity_date){
        String flag;
        String initial_activity = hg_selected;
        if (hg_selected.startsWith("Solve_")){
            flag = "0";
            hg_selected = hg_selected.replace("Solve_","");
        }else{
            flag = "1";
        }
        int field_rf_activity_existence = appDatabase.rfActivitiesFlagDao()
                .countFieldSpecificRFActivity(hgFieldListRecyclerModel.getUnique_field_id(),hg_selected);

        if (field_rf_activity_existence > 0){
            if (initial_activity.startsWith("Solve_")){

                double min_lat = Double.parseDouble(hgFieldListRecyclerModel.getMin_lat());
                double max_lat = Double.parseDouble(hgFieldListRecyclerModel.getMax_lat());
                double min_lng = Double.parseDouble(hgFieldListRecyclerModel.getMin_lng());
                double max_lng = Double.parseDouble(hgFieldListRecyclerModel.getMax_lng());
                double mid_lat = (max_lat+min_lat)/2.0;
                double mid_lng = (max_lng+min_lng)/2.0;

                double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
                double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

                if (locationDistance <= allowedDistance){
                    appDatabase.rfActivitiesFlagDao().updateRFFlag(hgFieldListRecyclerModel.getUnique_field_id(),hg_selected,flag,
                            activity_date,sharedPrefs.getStaffID(),getDate("spread"));
                    appDatabase.logsDao().insert(new Logs(hgFieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                            initial_activity,getDate("normal"),sharedPrefs.getStaffRole(),
                            String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0",
                            hgFieldListRecyclerModel.getIk_number(),hgFieldListRecyclerModel.getCrop_type()));
                    dismissAndRefresh();
                }else{
                    locationMismatchedDialog(latitude,longitude,min_lat,max_lat,min_lng,max_lng,
                            getActivity(),hgFieldListRecyclerModel.getUnique_field_id(),
                            Objects.requireNonNull(getActivity()).getResources().getString(R.string.wrong_location));
                }
            }else{
                appDatabase.rfActivitiesFlagDao().updateRFFlag(hgFieldListRecyclerModel.getUnique_field_id(),hg_selected,flag,
                        activity_date,sharedPrefs.getStaffID(),getDate("spread"));
                appDatabase.logsDao().insert(new Logs(hgFieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                        initial_activity,getDate("normal"),sharedPrefs.getStaffRole(),
                        String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0",
                        hgFieldListRecyclerModel.getIk_number(),hgFieldListRecyclerModel.getCrop_type()));
                dismissAndRefresh();
            }
        }else{
            if (!initial_activity.startsWith("Solve_")){
                appDatabase.rfActivitiesFlagDao().insert(new RFActivitiesFlag(hgFieldListRecyclerModel.getUnique_field_id(),
                        hg_selected,activity_date,flag, sharedPrefs.getStaffID(),"0",
                        hgFieldListRecyclerModel.getIk_number(), hgFieldListRecyclerModel.getCrop_type(),getDate("spread")));

                appDatabase.logsDao().insert(new Logs(hgFieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                        initial_activity,getDate("normal"),sharedPrefs.getStaffRole(),
                        String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0",
                        hgFieldListRecyclerModel.getIk_number(),hgFieldListRecyclerModel.getCrop_type()));
                dismissAndRefresh();
            }else{
                Toast.makeText(getActivity(), "There is no RF to solve", Toast.LENGTH_SHORT).show();
            }
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
        ((HGFieldListPage) Objects.requireNonNull(getActivity())).myMethod();
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }private void locationMismatchedDialog(double latitude,
                                           double longitude,
                                           double min_lat,
                                           double max_lat,
                                           double min_lng,
                                           double max_lng,
                                           Context context,
                                           String unique_field_id,
                                           String message) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        locationMismatchedDialog(latitude, longitude, min_lat, max_lat,
                min_lng, max_lng, context, unique_field_id, builder, message);
    }

    private void locationMismatchedDialog(final double latitude,
                                          final double longitude,
                                          final double min_lat,
                                          final double max_lat,
                                          final double min_lng,
                                          final double max_lng,
                                          Context context,
                                          String unique_field_id,
                                          MaterialAlertDialogBuilder builder,
                                          String message) {

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 20;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        final TextView fieldId = new TextView(context);
        fieldId.setText("Field ID: " + unique_field_id);
        fieldId.setPadding(paddingPixel, 0, 0, 0);
        fieldId.setTypeface(null, Typeface.ITALIC);
        layout.addView(fieldId);

        final TextView minlat = new TextView(context);
        minlat.setText("\nMin/Max Lat= " + min_lat + "/"  + max_lat );
        minlat.setPadding(paddingPixel, 0, 0, 0);
        minlat.setTypeface(null, Typeface.ITALIC);
        layout.addView(minlat);

        final TextView minlng = new TextView(context);
        minlng.setText("Min/Max Long= " + min_lng + "/"  + max_lng );
        minlng.setPadding(paddingPixel, 0, 0, 0);
        minlng.setTypeface(null, Typeface.ITALIC);
        layout.addView(minlng);

        final TextView tvId = new TextView(context);
        tvId.setText("\nYou are on Latitude: " + latitude);
        tvId.setPadding(paddingPixel, 0, 0, 0);
        tvId.setTypeface(null, Typeface.ITALIC);
        layout.addView(tvId);

        final TextView tvIK = new TextView(context);
        tvIK.setText("You are on Longitude: " + longitude);
        tvIK.setPadding(paddingPixel, 0, 0, 0);
        tvIK.setTypeface(null, Typeface.ITALIC);
        layout.addView(tvIK);

        builder.setCancelable(false)
                .setView(layout)
                .setTitle("Not on Field.")
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, id) -> dialog.cancel())
                .show();
    }

    private double locationDistanceToFieldCentre(double lat1, double lon1, double lat2, double lon2) {
        double R = 6.371; // Radius of the earth in m
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private double allowedDistanceToFieldBoundary(double lat1, double lon1, double lat2, double lon2) {
        double R = 6.371; // Radius of the earth in m
        double dLat = deg2rad(Math.abs(lat2 - lat1));  // deg2rad below
        double dLon = deg2rad(Math.abs(lon2 - lon1));
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in m
        Log.d("distanceDami",String.valueOf(d));
        return d+0.0002;
        // return Math.sqrt((lat2 - lat1) * (lat2 - lat1) + (lon2 - lon1) * (lon2 - lon1)) * 110000;
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public void clearTextViewText(MaterialTextView textView) {
        textView.setText("");
    }

    void checkForEmptyTextViewFields(){
        checkIfTextViewEmpty(tv_enter_date, Objects.requireNonNull(getActivity()).getResources().getString(R.string.error_message_enter_date));
        checkIfTextViewEmpty(tv_confirm_date, Objects.requireNonNull(getActivity()).getResources().getString(R.string.error_message_confirm_date));
    }

    void getCalenderDate(MaterialTextView textView, String activity_string, Context context){

        //To show current date in the datePicker
        Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(context, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
            String text = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            textView.setText(text);
            allowButton();
        }, mYear, mMonth, mDay);
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

    public int getDateCorrelationFlag(String selected_date, String reference_minimum_date){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Date selectedDate = sdf.parse(selected_date);
            Date referenceMinimumDate = sdf.parse(reference_minimum_date);
            Date todayDate = sdf.parse(today);
            // before() will return true if and only if date1 is before date2
            if (selectedDate != null) {
                if(selectedDate.before(referenceMinimumDate)){
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
        double min_lat = Double.parseDouble(hgFieldListRecyclerModel.getMin_lat());
        double max_lat = Double.parseDouble(hgFieldListRecyclerModel.getMax_lat());
        double min_lng = Double.parseDouble(hgFieldListRecyclerModel.getMin_lng());
        double max_lng = Double.parseDouble(hgFieldListRecyclerModel.getMax_lng());
        double mid_lat = (max_lat+min_lat)/2.0;
        double mid_lng = (max_lng+min_lng)/2.0;
        locationGetter = GPSController.initialiseLocationListener(Objects.requireNonNull(getActivity()));
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();

        double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
        double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

        if (locationDistance <= allowedDistance){
            if (ContextCompat.checkSelfPermission(view.getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            } else {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }else{
            locationMismatchedDialog(latitude,longitude,min_lat,max_lat,min_lng,max_lng,
                    getActivity(),hgFieldListRecyclerModel.getUnique_field_id(),
                    Objects.requireNonNull(getActivity()).getResources().getString(R.string.wrong_location));
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

    void savePictureAndClose(HGFieldListRecyclerModel hgFieldListRecyclerModel, String activity_type,
                             double latitude, double longitude){

        String image_name = activity_type + "_" + hgFieldListRecyclerModel.getUnique_field_id() + "_" + getDate("concat");

        String result = saveToSdCard(photo, image_name);
        if (result.equalsIgnoreCase("success")){
            updateActivity(hgFieldListRecyclerModel, actHGType.getText().toString(),
                    latitude,longitude,Objects.requireNonNull(tv_enter_date.getText()).toString());
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
            ChkImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.RF_ACTIVITY_PICTURE_LOCATION);

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

    private void showCapturePictureDialogStart(String message, Context context){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showCapturePictureDialogStartBody(builder, message, context);
    }

    private void showCapturePictureDialogStartBody(MaterialAlertDialogBuilder builder, String message,
                                                   Context context) {

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setTitle(context.getResources().getString(R.string.log_activity))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

}

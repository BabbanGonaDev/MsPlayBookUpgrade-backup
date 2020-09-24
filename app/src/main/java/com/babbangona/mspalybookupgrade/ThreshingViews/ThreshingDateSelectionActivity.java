package com.babbangona.mspalybookupgrade.ThreshingViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.ComingSoon;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThreshingDateSelectionActivity extends AppCompatActivity  implements LocationListener {

    @BindView(R.id.tv_enter_date)
    MaterialTextView tv_enter_date;

    @BindView(R.id.tv_confirm_date)
    MaterialTextView tv_confirm_date;

    @BindView(R.id.btnSubmit)
    MaterialButton btnSubmit;

    @BindView(R.id.actCollectionCenter)
    AutoCompleteTextView actCollectionCenter;

    @BindView(R.id.edlCollectionCenter)
    TextInputLayout edlCollectionCenter;

    @BindView(R.id.actConfirmCollectionCenter)
    AutoCompleteTextView actConfirmCollectionCenter;

    @BindView(R.id.edlConfirmCollectionCenter)
    TextInputLayout edlConfirmCollectionCenter;

    @BindView(R.id.edtPhoneNumber)
    TextInputEditText edtPhoneNumber;

    @BindView(R.id.edlPhoneNumber)
    TextInputLayout edlPhoneNumber;

    @BindView(R.id.edtConfirmPhoneNumber)
    TextInputEditText edtConfirmPhoneNumber;

    @BindView(R.id.edlConfirmPhoneNumber)
    TextInputLayout edlConfirmPhoneNumber;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    String cc_selection;

    @BindView(R.id.threshing_date_confirmation_dialog)
    LinearLayout threshing_date_confirmation_dialog;

    @BindView(R.id.bottom_sheet_schedule_date)
    TextView bottom_sheet_schedule_date;

    @BindView(R.id.bottom_sheet_collection_center)
    TextView bottom_sheet_collection_center;

    @BindView(R.id.bottom_sheet_phone_number)
    TextView bottom_sheet_phone_number;

    @BindView(R.id.btnCancel)
    MaterialButton btnCancel;

    @BindView(R.id.btnConfirm)
    MaterialButton btnConfirm;

    private BottomSheetBehavior sheetBehavior;

    int size;
    int count=0;
    int locationFlag;

    String provider;
    String user_lat = "";
    String user_long = "";

    ArrayList<Double> lats;
    ArrayList<Double> time;
    ArrayList<Double> longs;
    ArrayList<Double> latLongs;

    CountDownTimer locationTimer;
    ProgressDialog locationDialog;
    LocationManager locationManager;
    final long MIN_LOC_UPDATE_TIME = 500;

    private GPSController.LocationGetter locationGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threshing_date_selection);
        ButterKnife.bind(ThreshingDateSelectionActivity.this);
        appDatabase = AppDatabase.getInstance(ThreshingDateSelectionActivity.this);
        sharedPrefs = new SharedPrefs(ThreshingDateSelectionActivity.this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> goToHomePage());


        fillCollectionCenterSpinner(actCollectionCenter, ThreshingDateSelectionActivity.this);

        actCollectionCenter.setOnItemClickListener((parent, view, position, id) -> {
            clearSpinnerText(actConfirmCollectionCenter);
            cc_selection = (String)parent.getItemAtPosition(position);
            fillCollectionCenterSpinner(actConfirmCollectionCenter, ThreshingDateSelectionActivity.this);
        });

        textWatcher(edtPhoneNumber, edlPhoneNumber, getResources().getString(R.string.wrong_phone_number_format));
        textWatcher(edtConfirmPhoneNumber, edlConfirmPhoneNumber, getResources().getString(R.string.wrong_phone_number_format));

        sheetBehavior = BottomSheetBehavior.from(threshing_date_confirmation_dialog);
        addBehaviourToBottomSheet(sheetBehavior);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lats = new ArrayList<>();
        longs = new ArrayList<>();
        time = new ArrayList<>();
        latLongs = new ArrayList<>();
        locationDialog= new ProgressDialog(ThreshingDateSelectionActivity.this);
        locationFlag=0;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setBearingAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        provider = locationManager.getBestProvider(criteria, false);
//
        if (permissionGranted()) {
            try {
                Location location = locationManager.getLastKnownLocation(provider);
                locationManager.requestLocationUpdates(provider, MIN_LOC_UPDATE_TIME, 0, this);
                if (location != null) {
                    onLocationChanged(location);
                }

            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(ThreshingDateSelectionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        locationTimer =new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                locationManager.removeUpdates(ThreshingDateSelectionActivity.this);
                goToHomeLocationCapture();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.threshing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.schedule) {
            startActivity(new Intent(ThreshingDateSelectionActivity.this, ComingSoon.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void addBehaviourToBottomSheet(BottomSheetBehavior sheetBehavior){
        sheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }

    @OnClick(R.id.tv_enter_date)
    void enterDate(){
        getCalenderDate(tv_enter_date, getResources().getString(R.string.enter_thresh_date), ThreshingDateSelectionActivity.this);
        tv_confirm_date.setEnabled(true);
        clearTextViewText(tv_confirm_date);
    }

    @OnClick(R.id.tv_confirm_date)
    void confirmDate(){
        getCalenderDate(tv_confirm_date, getResources().getString(R.string.confirm_thresh_date), ThreshingDateSelectionActivity.this);
    }

    @OnClick(R.id.btnCancel)
    public  void setBtnCancel(){
        showBottomSheet();
    }

    @OnClick(R.id.btnConfirm)
    public  void setBtnConfirm(){
        showDialogForExit(ThreshingDateSelectionActivity.this);
    }

    void getCalenderDate(MaterialTextView textView, String activity_string, Context context){

        //To show current date in the datePicker
        Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(context, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
            String text = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            textView.setText(parseDate(text));
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle(activity_string);
        mDatePicker.show();
    }

    public void clearTextViewText(MaterialTextView textView) {
        textView.setText("");
    }

    public String parseDate(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(time);
            if (date != null) {
                str = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String reverseParseDate(String time) {
        String inputPattern = "dd-MMM-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.getDefault());

        Date date;
        String str = null;

        try {
            date = inputFormat.parse(time);
            if (date != null) {
                str = outputFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @OnClick(R.id.btnSubmit)
    public  void setBtnSubmit(){
        if (validateFieldsInfo(tv_enter_date,tv_confirm_date,actCollectionCenter,
                actConfirmCollectionCenter,edtPhoneNumber,edtConfirmPhoneNumber) == 0){
            checkForEmptyFields();
            checkForEmptyTextViewFields();
            checkForEmptyTextInputFields();
        }else if (getDateCorrelationFlag(tv_enter_date.getText().toString().trim(),getMaximumScheduleDate()) == 0 ||
                getDateCorrelationFlag(tv_confirm_date.getText().toString().trim(),getMaximumScheduleDate()) == 0){
            checkForEmptyFields();
            checkForEmptyTextViewFields();
            checkForEmptyTextInputFields();
            checkForWrongSelectedDate();
            //Toast.makeText(this, getResources().getString(R.string.error_thresh_date), Toast.LENGTH_LONG).show();
            showDateProblemStart(getResources().getString(R.string.error_thresh_date),this);
        }else if (!tv_enter_date.getText().toString().trim().matches(tv_confirm_date.getText().toString().trim())){
            checkForWrongSelectedDate();
            checkForEmptyFields();
            checkForEmptyTextInputFields();
            checkForMismatchedCollectionCenter();
            checkForMismatchedDate();
            //Toast.makeText(this, getResources().getString(R.string.error_thresh_date_mismatch), Toast.LENGTH_LONG).show();
            showDateProblemStart(getResources().getString(R.string.error_thresh_date_mismatch),this);
        }else if (!checkThreshHours(tv_enter_date.getText().toString().trim(), sharedPrefs.getStaffID())){
//            Toast.makeText(this, getResources().getString(R.string.thresh_date_error), Toast.LENGTH_SHORT).show();
            showDateProblemStart(getResources().getString(R.string.thresh_date_error),this);
        }else if (!actCollectionCenter.getText().toString().trim().matches(actConfirmCollectionCenter.getText().toString().trim())){
            checkForEmptyFields();
            checkForEmptyTextViewFields();
            checkForEmptyTextInputFields();
            checkForMismatchedCollectionCenter();
            checkForMismatchedDate();
            checkForWrongSelectedDate();
        }else if (!Objects.requireNonNull(edtPhoneNumber.getText()).toString().trim().matches(Objects.requireNonNull(edtConfirmPhoneNumber.getText()).toString().trim())){
            checkForEmptyFields();
            checkForEmptyTextViewFields();
            checkForEmptyTextInputFields();
            checkForMismatchedCollectionCenter();
            checkForMismatchedDate();
            checkForWrongSelectedDate();
        }else{
            checkForEmptyFields();
            checkForEmptyTextInputFields();
            checkForEmptyTextViewFields();
            checkForMismatchedCollectionCenter();
            checkForMismatchedDate();
            checkForWrongSelectedDate();
            //save to shared preference and move on
            setBottomSheerTexts();
            showBottomSheet();
        }
    }

    private void showDateProblemStart(String message, Context context){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDateProblemBody(builder, message, context);
    }

    private void showDateProblemBody(MaterialAlertDialogBuilder builder, String message,
                                                   Context context) {

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setTitle(context.getResources().getString(R.string.attention))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.go_back), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    public int validateFieldsInfo(MaterialTextView tv_enter_date, MaterialTextView tv_confirm_date,
                                  AutoCompleteTextView actCollectionCenter, AutoCompleteTextView actConfirmCollectionCenter,
                                  TextInputEditText edtPhoneNumber, TextInputEditText edtConfirmPhoneNumber){

        // Checks if the state field is empty
        if(Objects.requireNonNull(tv_enter_date.getText()).toString().matches("")) {
            return 0;
        }

        else if(Objects.requireNonNull(tv_confirm_date.getText()).toString().matches("")) {
            return 0;
        }

        else if(Objects.requireNonNull(actCollectionCenter.getText()).toString().matches("")) {
            return 0;
        }

        else if(Objects.requireNonNull(actConfirmCollectionCenter.getText()).toString().matches("")) {
            return 0;
        }

        else if(Objects.requireNonNull(edtPhoneNumber.getText()).toString().matches("")) {
            return 0;
        }

        else if(Objects.requireNonNull(edtConfirmPhoneNumber.getText()).toString().matches("")) {
            return 0;
        }

        //all checks are passed
        else{
            return 1;
        }
    }

    void checkForEmptyFields(){
        checkIfAutocompleteEmpty(actCollectionCenter,edlCollectionCenter,this.getResources().getString(R.string.error_collection_point_location));
        checkIfAutocompleteEmpty(actConfirmCollectionCenter,edlConfirmCollectionCenter,this.getResources().getString(R.string.error_confirm_collection_point_location));
    }

    void checkForMismatchedCollectionCenter(){
        if (!actCollectionCenter.getText().toString().trim().matches(actConfirmCollectionCenter.getText().toString().trim())){
            setErrorOfTextView(edlCollectionCenter, getResources().getString(R.string.error_collection_center_mismatch));
            setErrorOfTextView(edlConfirmCollectionCenter, getResources().getString(R.string.error_collection_center_mismatch));
        }else{
            removeErrorFromText(edlCollectionCenter);
            removeErrorFromText(edlConfirmCollectionCenter);
        }
    }

    void checkForMismatchedDate(){
        if (!tv_enter_date.getText().toString().trim().matches(tv_confirm_date.getText().toString().trim())){
            setErrorOfTextView(tv_enter_date,getResources().getString(R.string.error_thresh_date_mismatch));
            setErrorOfTextView(tv_confirm_date,getResources().getString(R.string.error_thresh_date_mismatch));
        }else{
            removeErrorFromText(tv_enter_date);
            removeErrorFromText(tv_confirm_date);
        }
    }

    void checkForWrongSelectedDate(){
        if (getDateCorrelationFlag(tv_enter_date.getText().toString().trim(),getMaximumScheduleDate()) == 0 ||
                getDateCorrelationFlag(tv_confirm_date.getText().toString().trim(),getMaximumScheduleDate()) == 0){
            setErrorOfTextView(tv_enter_date,getResources().getString(R.string.error_thresh_date));
            setErrorOfTextView(tv_confirm_date,getResources().getString(R.string.error_thresh_date));
        }else{
            removeErrorFromText(tv_enter_date);
            removeErrorFromText(tv_confirm_date);
        }
    }

    void checkForEmptyTextViewFields(){
        checkIfTextViewEmpty(tv_enter_date, getResources().getString(R.string.please_thresh_date));
        checkIfTextViewEmpty(tv_confirm_date, getResources().getString(R.string.please_confirm_thresh_date));
    }

    void checkForEmptyTextInputFields(){
        checkIfTextInputEditTextEmpty(edtPhoneNumber, edlPhoneNumber, getResources().getString(R.string.please_phone_number));
        checkIfTextInputEditTextEmpty(edtConfirmPhoneNumber, edlConfirmPhoneNumber, getResources().getString(R.string.please_confirm_phone_number));
    }

    public void checkIfTextViewEmpty(MaterialTextView materialTextView, String error_message) {
        String textEntered = Objects.requireNonNull(materialTextView.getText()).toString();
        if (textEntered.equalsIgnoreCase("")){
            setErrorOfTextView(materialTextView, error_message);
            Toast.makeText(ThreshingDateSelectionActivity.this, error_message, Toast.LENGTH_SHORT).show();
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

    public int getDateCorrelationFlag(String selected_date, String reference_maximum_date){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
            String today = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
            Date selectedDate = sdf.parse(selected_date);
            Date todayDate = sdf.parse(today);
            Date referenceMaximumDate = sdf.parse(parseDate(reference_maximum_date));
            // before() will return true if and only if date1 is before date2
            if (selectedDate != null) {
                if(selectedDate.before(todayDate)){
                    return 0;
                }else if (selectedDate.after(referenceMaximumDate)){
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

    private boolean checkThreshHours(String selected_date, String staff_id){
        String converted_date = reverseParseDate(selected_date);
        List<ScheduledThreshingActivitiesFlag.ScheduleCalculationModel> scheduleCalculationModelList;
        scheduleCalculationModelList = appDatabase.scheduleThreshingActivitiesFlagDao().getAllScheduledFields(staff_id,converted_date);
        int count = 0;
        double cumulativeFieldSize = 0.00d;
        if (scheduleCalculationModelList.size() > 0){
            for (ScheduledThreshingActivitiesFlag.ScheduleCalculationModel scheduleCalculationModel : scheduleCalculationModelList){
                cumulativeFieldSize += returnRightDoubleValue(scheduleCalculationModel.getField_size());
                count += 1;
            }
        }
        double result = getFieldsTravelTime() + (count * getAverageTransitionTime()) + (cumulativeFieldSize * getTimePerHa());
        return result < 8;
    }

    private double returnRightDoubleValue(String inputValue){
        if (inputValue == null || inputValue.equalsIgnoreCase("")){
            return 0;
        }else{
            return roundFieldSizeDouble(Double.parseDouble(inputValue));
        }
    }

    private double roundFieldSizeDouble(double result){
        return Math.round(result * 1000) / 1000.0;
    }

    double getFieldsTravelTime(){
        String fields_travel_time;
        try {
            fields_travel_time = appDatabase.appVariablesDao().getFieldsTravelTime("1");
        } catch (Exception e) {
            e.printStackTrace();
            fields_travel_time = "1.00";
        }
        if (fields_travel_time == null || fields_travel_time.equalsIgnoreCase("") ){
            fields_travel_time = "1.00";
        }
        return returnRightDoubleValue(fields_travel_time);
    }

    double getAverageTransitionTime(){
        String average_transition_time;
        try {
            average_transition_time = appDatabase.appVariablesDao().getAverageTransitionTime("1");
        } catch (Exception e) {
            e.printStackTrace();
            average_transition_time = "1.00";
        }
        if (average_transition_time == null || average_transition_time.equalsIgnoreCase("") ){
            average_transition_time = "1.00";
        }
        return returnRightDoubleValue(average_transition_time);
    }

    double getTimePerHa(){
        String time_per_ha;
        try {
            time_per_ha = appDatabase.appVariablesDao().getTimePerHa("1");
        } catch (Exception e) {
            e.printStackTrace();
            time_per_ha = "1.00";
        }
        if (time_per_ha == null || time_per_ha.equalsIgnoreCase("") ){
            time_per_ha = "1.00";
        }
        return returnRightDoubleValue(time_per_ha);
    }

    String getMaximumScheduleDate(){
        String maximum_schedule_date;
        try {
            maximum_schedule_date = appDatabase.appVariablesDao().getMaximumScheduleDate("1");
        } catch (Exception e) {
            e.printStackTrace();
            maximum_schedule_date = "2020-12-31";
        }
        if (maximum_schedule_date == null || maximum_schedule_date.equalsIgnoreCase("") ){
            maximum_schedule_date = "2020-12-31";
        }
        return maximum_schedule_date;
    }

    public void fillCollectionCenterSpinner(AutoCompleteTextView autoCompleteTextView, Context context) {
        ArrayAdapter village;
        village = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.harvestLocationsDao().getAllCollectionPointsNoConstraint());
        spinnerViewController(autoCompleteTextView,village);
    }

    public void spinnerViewController(AutoCompleteTextView autoCompleteTextView, ArrayAdapter arrayAdapter) {
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    public void checkIfAutocompleteEmpty(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout, String error_message) {
        String textEntered = Objects.requireNonNull(autoCompleteTextView.getText()).toString();
        if (textEntered.equalsIgnoreCase("")){
            setErrorOfTextView(textInputLayout, error_message);
        }else{
            removeErrorFromText(textInputLayout);
        }
    }

    public void checkIfTextInputEditTextEmpty(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String error_message) {
        String textEntered = Objects.requireNonNull(textInputEditText.getText()).toString();
        if (textEntered.equalsIgnoreCase("")){
            setErrorOfTextView(textInputLayout, error_message);
        }else{
            removeErrorFromText(textInputLayout);
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

    public void clearSpinnerText(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setText("");
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
        if( watchWord.length() < 10 ||
                watchWord.length() > 14 ||
                watchWord.length()==12 ) {
            return false;
        }else{
            return watchWord.matches("^0(70|90|80|81)\\d{8}");
        }
    }

    void setBottomSheerTexts(){
        bottom_sheet_schedule_date.setText(Objects.requireNonNull(tv_enter_date.getText()).toString());
        bottom_sheet_collection_center.setText(Objects.requireNonNull(actCollectionCenter.getText()).toString());
        bottom_sheet_phone_number.setText(Objects.requireNonNull(edtPhoneNumber.getText()).toString());
    }

    void showBottomSheet(){
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onLocationChanged(Location location) {


        double lat = location.getLatitude();
        double lng = location.getLongitude();
        count = lats.size();

        Log.d("goThere",user_lat+" "+user_long);
        Log.d("size_e", size + "|"+ locationFlag);



        if (count>=4) {

            if(locationFlag==1){
                locationDialog.dismiss();
                locationTimer.cancel();
                locationManager.removeUpdates(this);

                user_lat=String.valueOf(lats.get(3));
                user_long=String.valueOf(longs.get(3));
                Log.d("goThere1",user_lat+" "+user_long);

                sharedPrefs.setKeyVillageLocation(user_lat,user_long);

                saveDetails(1,
                        reverseParseDate(tv_enter_date.getText().toString().trim()),
                        actCollectionCenter.getText().toString().trim(),
                        Objects.requireNonNull(edtPhoneNumber.getText()).toString().trim());
            } else {
                lats.add(lat);
                longs.add(lng);
                time.add((double) (System.currentTimeMillis() / 1000));
                latLongs.add(lat);
                latLongs.add(lng);
                //count.setText(String.valueOf(size));

                Log.d("goThere2",latLongs.toString());

            }

        } else{
            lats.add(lat);
            longs.add(lng);
            time.add((double) (System.currentTimeMillis() / 1000));
            latLongs.add(lat);
            latLongs.add(lng);

            Log.d("goThere3",latLongs.toString());

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    void goToHomeLocationCapture(){
        finish();
        Intent intent = new Intent(ThreshingDateSelectionActivity.this, ThreshingActivity.class);
        startActivity(intent);
    }

    public boolean permissionGranted() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void showDialogForExit(Context context) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForExitBody(builder, context);
    }

    private void showDialogForExitBody(MaterialAlertDialogBuilder builder, Context context) {


        locationDialog.setMessage("Getting present location");
        locationDialog.setTitle("Location Dialog");
        locationDialog.setCancelable(false);

        builder.setTitle(context.getResources().getString(R.string.location_question_title))
                .setMessage(context.getResources().getString(R.string.location_question))
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    //this is to dismiss the dialog
                    locationFlag=1;
                    dialog.dismiss();
                    locationDialog.show();
                    locationTimer.start();
                })
                .setNeutralButton(context.getResources().getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();
                    saveDetails(0,
                            reverseParseDate(tv_enter_date.getText().toString().trim()),
                            actCollectionCenter.getText().toString().trim(),
                            Objects.requireNonNull(edtPhoneNumber.getText()).toString().trim());
                })
                .setCancelable(false)
                .show();
    }

    void goToHomePage(){
        finish();
        Intent intent = new Intent(ThreshingDateSelectionActivity.this, ThreshingActivity.class);
        startActivity(intent);
    }

    void saveDetails(int location_flag, String schedule_date, String collection_center, String phone_number){

        locationGetter = GPSController.initialiseLocationListener(this);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();

        if (sharedPrefs.getKeyThreshingRecaptureFlag().equalsIgnoreCase("1")){
            savePictureAndClose(
                    getBitmap(sharedPrefs.getKeyThreshingPicture()),
                    sharedPrefs.getKeyThreshingUniqueMemberId(),
                    location_flag,
                    schedule_date,
                    collection_center,
                    phone_number,
                    String.valueOf(latitude),
                    String.valueOf(longitude)
            );
        }else{
            updateActivity(
                    sharedPrefs.getKeyThreshingUniqueFieldId(),
                    location_flag,
                    schedule_date,
                    collection_center,
                    phone_number,
                    String.valueOf(latitude),
                    String.valueOf(longitude),
                    sharedPrefs.getKeyThreshingIkNumber(),
                    sharedPrefs.getKeyThreshingCropType()
            );
        }
        showSuccessStart(getResources().getString(R.string.threshing_success),this);
    }

    void savePictureAndClose(Bitmap threshing_picture, String unique_member_id, int location_flag,
                             String schedule_date, String collection_center, String phone_number,
                             String latitude, String longitude){

        final String picture_name = unique_member_id +"_threshing";

        String result1 = saveToSdCard(threshing_picture, picture_name);
        if (result1 != null ) {
            if (result1.equalsIgnoreCase("success") ){
                updateActivity(
                        sharedPrefs.getKeyThreshingUniqueFieldId(),
                        location_flag,
                        schedule_date,
                        collection_center,
                        phone_number,
                        latitude,
                        longitude,
                        sharedPrefs.getKeyThreshingIkNumber(),
                        sharedPrefs.getKeyThreshingCropType()
                );
                Toast.makeText(this, "Picture Saved", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Picture Not Saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Picture Not Saved", Toast.LENGTH_SHORT).show();
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

        Date date1 = new Date();
        return dateFormat1.format(date1);
    }

    private String getDeviceID(){
        String device_id;
        TelephonyManager tm = (TelephonyManager) Objects.requireNonNull(this).getSystemService(Context.TELEPHONY_SERVICE);
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

    String saveToSdCard(Bitmap bitmap, String filename) {
        String stored = null;
        File ChkImgDirectory;
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            ChkImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.MEMBER_PICTURE_LOCATION);

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

    private void updateActivity(String unique_field_id,int locationFlag, String schedule_date,
                                String collection_center, String phone_number, String latitude,
                                String longitude, String ik_number, String crop_type){

        String new_latitude, new_longitude;
        if (locationFlag <= 0){
            new_latitude = "0.00";
            new_longitude = "0.00";
        }else{
            new_latitude = latitude;
            new_longitude = longitude;
        }

        appDatabase.scheduleThreshingActivitiesFlagDao().insert(new ScheduledThreshingActivitiesFlag(
                unique_field_id,
                sharedPrefs.getKeyThresher(),
                sharedPrefs.getKeyThreshingRecaptureFlag(),
                sharedPrefs.getKeyThreshingTemplate(),
                schedule_date,
                collection_center,
                phone_number,
                getDeviceID(),
                BuildConfig.VERSION_NAME,
                new_latitude,
                new_longitude,
                sharedPrefs.getStaffID(),
                getDate("spread"),
                "0",
                "XXX",
                sharedPrefs.getKeyThreshingIkNumber()
                )
        );

        appDatabase.logsDao().insert(new Logs(unique_field_id,sharedPrefs.getStaffID(),
                "Schedule threshing",getDate("normal"),sharedPrefs.getStaffRole(),
                latitude, longitude, getDeviceID(),"0",
                ik_number, crop_type));
    }

    private Bitmap getBitmap(String member_picture_byte_array){
        byte[] imageAsBytes = new byte[0];
        if (member_picture_byte_array != null) {
            imageAsBytes = Base64.decode(member_picture_byte_array.getBytes(), Base64.DEFAULT);
        }
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
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
}
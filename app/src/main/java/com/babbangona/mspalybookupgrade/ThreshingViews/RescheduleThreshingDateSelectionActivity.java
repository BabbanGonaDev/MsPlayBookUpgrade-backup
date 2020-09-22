package com.babbangona.mspalybookupgrade.ThreshingViews;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

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

public class RescheduleThreshingDateSelectionActivity extends AppCompatActivity{

    @BindView(R.id.tv_enter_date)
    MaterialTextView tv_enter_date;

    @BindView(R.id.tv_confirm_date)
    MaterialTextView tv_confirm_date;

    @BindView(R.id.btnSubmit)
    MaterialButton btnSubmit;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reschedule_threshing_date_selection_content);
        ButterKnife.bind(RescheduleThreshingDateSelectionActivity.this);
        appDatabase = AppDatabase.getInstance(RescheduleThreshingDateSelectionActivity.this);
        sharedPrefs = new SharedPrefs(RescheduleThreshingDateSelectionActivity.this);

    }

    @OnClick(R.id.tv_enter_date)
    void enterDate(){
        getCalenderDate(tv_enter_date, getResources().getString(R.string.enter_thresh_date), RescheduleThreshingDateSelectionActivity.this);
        tv_confirm_date.setEnabled(true);
        clearTextViewText(tv_confirm_date);
    }

    @OnClick(R.id.tv_confirm_date)
    void confirmDate(){
        getCalenderDate(tv_confirm_date, getResources().getString(R.string.confirm_thresh_date), RescheduleThreshingDateSelectionActivity.this);
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
        if (validateFieldsInfo(tv_enter_date,tv_confirm_date) == 0){
            checkForEmptyTextViewFields();
        }else if (getDateCorrelationFlag(tv_enter_date.getText().toString().trim()) == 0 ||
                getDateCorrelationFlag(tv_confirm_date.getText().toString().trim()) == 0){
            checkForEmptyTextViewFields();
            checkForWrongSelectedDate();
            Toast.makeText(this, getResources().getString(R.string.error_thresh_date), Toast.LENGTH_LONG).show();
        }else if (!tv_enter_date.getText().toString().trim().matches(tv_confirm_date.getText().toString().trim())){
            checkForWrongSelectedDate();
            checkForMismatchedDate();
            Toast.makeText(this, getResources().getString(R.string.error_thresh_date_mismatch), Toast.LENGTH_LONG).show();
        }else if (!checkThreshHours(tv_enter_date.getText().toString().trim(), sharedPrefs.getStaffID())){
            Toast.makeText(this, getResources().getString(R.string.thresh_date_error), Toast.LENGTH_SHORT).show();
        }else{
            checkForEmptyTextViewFields();
            checkForMismatchedDate();
            checkForWrongSelectedDate();
            //save to shared preference and move on
        }
    }

    public int validateFieldsInfo(MaterialTextView tv_enter_date, MaterialTextView tv_confirm_date){

        // Checks if the state field is empty
        if(Objects.requireNonNull(tv_enter_date.getText()).toString().matches("")) {
            return 0;
        }

        else if(Objects.requireNonNull(tv_confirm_date.getText()).toString().matches("")) {
            return 0;
        }

        //all checks are passed
        else{
            return 1;
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
        if (getDateCorrelationFlag(tv_enter_date.getText().toString().trim()) == 0 ||
                getDateCorrelationFlag(tv_confirm_date.getText().toString().trim()) == 0){
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

    public void checkIfTextViewEmpty(MaterialTextView materialTextView, String error_message) {
        String textEntered = Objects.requireNonNull(materialTextView.getText()).toString();
        if (textEntered.equalsIgnoreCase("")){
            setErrorOfTextView(materialTextView, error_message);
            Toast.makeText(RescheduleThreshingDateSelectionActivity.this, error_message, Toast.LENGTH_SHORT).show();
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

    public int getDateCorrelationFlag(String selected_date){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
            String today = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
            Date selectedDate = sdf.parse(selected_date);
            Date todayDate = sdf.parse(today);
            // before() will return true if and only if date1 is before date2
            if (selectedDate != null) {
                if(selectedDate.before(todayDate)){
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

    void goToHomeLocationCapture(){
        finish();
        Intent intent = new Intent(RescheduleThreshingDateSelectionActivity.this, MemberLocation.class);
        startActivity(intent);
    }
}
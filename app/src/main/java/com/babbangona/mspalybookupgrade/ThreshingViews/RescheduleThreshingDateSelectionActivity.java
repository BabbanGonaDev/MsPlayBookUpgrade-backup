package com.babbangona.mspalybookupgrade.ThreshingViews;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

    @BindView(R.id.tv_old_thresh_date)
    TextView tv_old_thresh_date;

    @BindView(R.id.tv_confirm_date)
    MaterialTextView tv_confirm_date;

    @BindView(R.id.btnSubmit)
    MaterialButton btnSubmit;

    @BindView(R.id.edlRescheduleReason)
    TextInputLayout edlRescheduleReason;

    @BindView(R.id.edtRescheduleReason)
    TextInputEditText edtRescheduleReason;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.last_sync_date_tv)
    TextView last_sync_date_tv;

    @BindView(R.id.tv_staff_id)
    TextView tv_staff_id;

    @BindView(R.id.actSwap)
    AutoCompleteTextView actSwap;

    @BindView(R.id.edlSwap)
    TextInputLayout edlSwap;

    @BindView(R.id.layout_reason)
    LinearLayout layout_reason;

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    SetPortfolioMethods setPortfolioMethods;

    private GPSController.LocationGetter locationGetter;

    String old_thresh_date;
    String raw_old_thresh_date;

    String swap_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reschedule_threshing_date_selection_content_top);
        ButterKnife.bind(RescheduleThreshingDateSelectionActivity.this);
        setSupportActionBar(toolbar);
        appDatabase = AppDatabase.getInstance(RescheduleThreshingDateSelectionActivity.this);
        sharedPrefs = new SharedPrefs(RescheduleThreshingDateSelectionActivity.this);
        setPortfolioMethods = new SetPortfolioMethods();
        raw_old_thresh_date = appDatabase.scheduleThreshingActivitiesFlagDao().getFieldSchedule(sharedPrefs.getKeyThreshingUniqueFieldId());
        old_thresh_date = parseDate(raw_old_thresh_date);

        if (sharedPrefs.getKeyRescheduleStateFlag().equalsIgnoreCase("0")){
            resetLayout();
            fillSwapSpinner(actSwap,RescheduleThreshingDateSelectionActivity.this);
            showUrgentStart(getResources().getString(R.string.select_threshing_type),RescheduleThreshingDateSelectionActivity.this);
        }else{
            String answer = "Yes";
            actSwap.setText(answer);
            actSwap.setEnabled(false);
            tv_old_thresh_date.setText(getRescheduleParameters(answer));
            checkIfDateChangeable(sharedPrefs.getKeyThreshingUniqueFieldId(), sharedPrefs.getKeySwapFieldId());
        }
        actSwap.setOnItemClickListener((parent, view, position, id) -> {
            swap_answer = (String)parent.getItemAtPosition(position);
            moveToSelectSwap(swap_answer);
        });
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(RescheduleThreshingDateSelectionActivity.this));
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,RescheduleThreshingDateSelectionActivity.this);
    }

    void moveToSelectSwap(String swap_answer){
        if (swap_answer.equalsIgnoreCase("no")){
            layout_reason.setVisibility(View.VISIBLE);
            tv_old_thresh_date.setText(getRescheduleParameters(swap_answer));
        }else{
            if (getDateError(old_thresh_date) == 0){
                showDateProblemStart(getResources().getString(R.string.swap_dialog_error), RescheduleThreshingDateSelectionActivity.this);
                resetLayout();
            }else{
                showSelectSwapField(getResources().getString(R.string.swap_dialog_body),RescheduleThreshingDateSelectionActivity.this);
            }
        }
    }

    String getRescheduleParameters(String swap_answer){
        String text;
        if (swap_answer == null){
            swap_answer = "No";
        }
        if (swap_answer.equalsIgnoreCase("no")){
            text = "Field ID: " + sharedPrefs.getKeyThreshingUniqueFieldId() + "\n" +
                    "IK Number: " + sharedPrefs.getKeyThreshingIkNumber() + "\n" +
                    "Old Scheduled Date: " + old_thresh_date;
        }else{
            text = "Field ID: " + sharedPrefs.getKeyThreshingUniqueFieldId() + "\n" +
                    "IK Number: " + sharedPrefs.getKeyThreshingIkNumber() + "\n" +
                    "Old Scheduled Date: " + old_thresh_date + "\n" +
                    "Swap Field ID: " + sharedPrefs.getKeySwapFieldId();

        }
        return text;
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
        if (validateFieldsInfo(tv_enter_date,tv_confirm_date,edtRescheduleReason) == 0){
            checkForEmptyTextViewFields();
            checkForEmptyTextInputFields();
        }else if (getDateCorrelationFlag(tv_enter_date.getText().toString().trim(), getMaximumScheduleDate()) == 0 ||
                getDateCorrelationFlag(tv_confirm_date.getText().toString().trim(), getMaximumScheduleDate()) == 0){
            checkForEmptyTextViewFields();
            checkForWrongSelectedDate();
            checkForEmptyTextInputFields();
//            Toast.makeText(this, getResources().getString(R.string.error_thresh_date), Toast.LENGTH_LONG).show();
            showDateProblemStart(getResources().getString(R.string.error_re_thresh_date),this);
        }else if (getDateCorrelationFlagSameDate(tv_enter_date.getText().toString().trim(), old_thresh_date) == 0 ){
            checkForEmptyTextViewFields();
            checkForWrongSelectedDate();
            checkForEmptyTextInputFields();
//            Toast.makeText(this, getResources().getString(R.string.error_thresh_date), Toast.LENGTH_LONG).show();
            showDateProblemStart(getResources().getString(R.string.error_re_thresh_date_same),this);
        }else if (!tv_enter_date.getText().toString().trim().matches(tv_confirm_date.getText().toString().trim())){
            checkForWrongSelectedDate();
            checkForMismatchedDate();
            checkForEmptyTextInputFields();
//            Toast.makeText(this, getResources().getString(R.string.error_thresh_date_mismatch), Toast.LENGTH_LONG).show();
            showDateProblemStart(getResources().getString(R.string.error_thresh_date_mismatch),this);
        }else if (checkThreshHours(tv_enter_date.getText().toString().trim(), sharedPrefs.getStaffID())){
//            Toast.makeText(this, getResources().getString(R.string.thresh_date_error), Toast.LENGTH_SHORT).show();
            showDateProblemStart(getResources().getString(R.string.thresh_date_error),this);
        }else if (actSwap.getText().toString().equalsIgnoreCase("yes") && checkThreshHours(raw_old_thresh_date,sharedPrefs.getStaffID())){
//            Toast.makeText(this, getResources().getString(R.string.thresh_date_error), Toast.LENGTH_SHORT).show();
            showDateProblemStart(getResources().getString(R.string.swap_thresh_date_error),this);
        }else{
            checkForEmptyTextViewFields();
            checkForMismatchedDate();
            checkForWrongSelectedDate();
            checkForEmptyTextInputFields();
            //save to shared preference and move on
            showDialogForExit(this,
                    "Please confirm rescheduling information" ,
                    sharedPrefs.getKeyThreshingUniqueFieldId(),
                    parseDate(appDatabase.scheduleThreshingActivitiesFlagDao().getFieldSchedule(sharedPrefs.getKeyThreshingUniqueFieldId())),
                    tv_enter_date.getText().toString().trim(),
                    Objects.requireNonNull(edtRescheduleReason.getText()).toString().trim(), actSwap.getText().toString());
        }
    }

    public int validateFieldsInfo(MaterialTextView tv_enter_date, MaterialTextView tv_confirm_date,
                                  TextInputEditText edtRescheduleReason){

        // Checks if the state field is empty
        if(Objects.requireNonNull(tv_enter_date.getText()).toString().matches("")) {
            return 0;
        }

        else if(Objects.requireNonNull(tv_confirm_date.getText()).toString().matches("")) {
            return 0;
        }

        else if(Objects.requireNonNull(edtRescheduleReason.getText()).toString().matches("")) {
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
        if (getDateCorrelationFlag(tv_enter_date.getText().toString().trim(),getMaximumScheduleDate()) == 0 ||
                getDateCorrelationFlag(tv_confirm_date.getText().toString().trim(),getMaximumScheduleDate()) == 0){
            setErrorOfTextView(tv_enter_date,getResources().getString(R.string.error_re_thresh_date));
            setErrorOfTextView(tv_confirm_date,getResources().getString(R.string.error_re_thresh_date));
        }if (getDateCorrelationFlagSameDate(tv_enter_date.getText().toString().trim(),old_thresh_date) == 0 ){
            setErrorOfTextView(tv_enter_date,getResources().getString(R.string.error_re_thresh_date_same));
            setErrorOfTextView(tv_confirm_date,getResources().getString(R.string.error_re_thresh_date_same));
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
        checkIfTextInputEditTextEmpty(edtRescheduleReason, edlRescheduleReason, getResources().getString(R.string.please_reason));
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

    public int getDateError(String selected_date){
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

    public int getDateCorrelationFlagSameDate(String selected_date, String former_date){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
            String today = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
            Date selectedDate = sdf.parse(selected_date);
            Date todayDate = sdf.parse(today);
            Date existingDate = sdf.parse(former_date);
            // before() will return true if and only if date1 is before date2
            if (selectedDate != null) {
                if(selectedDate.before(todayDate)){
                    return 0;
                }else if (selectedDate.equals(existingDate)){
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
        return result >= 8;
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

    private void showDateProblemStart(String message, Context context){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDateProblemBody(builder, message, context);
    }

    private void showDateProblemBody(MaterialAlertDialogBuilder builder, String message,
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

    private void showDateProblemStartSwap(String message, Context context){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDateProblemStartSwapBody(builder, message, context);
    }

    private void showDateProblemStartSwapBody(MaterialAlertDialogBuilder builder, String message,
                                     Context context) {

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setTitle(context.getResources().getString(R.string.oops))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.go_back), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    goToHomePage();
                })
                .setCancelable(false)
                .show();
    }

    private void showDialogForExit(Context context, String message,
                                   String unique_field_id, String former_schedule_date,
                                   String new_schedule_date, String reason, String swap_flag) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForExitBody(builder, context, message, unique_field_id, former_schedule_date, new_schedule_date, reason, swap_flag);
    }

    private void showDialogForExitBody(MaterialAlertDialogBuilder builder, Context context, String message,
                                       String unique_field_id, String former_schedule_date,
                                       String new_schedule_date, String reason, String swap_flag) {

        locationGetter = GPSController.initialiseLocationListener(this);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 20;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        String field_id_text = "Field ID: " + unique_field_id;
        String old_schedule_date_text = "Old Schedule Date: " + former_schedule_date;
        String new_schedule_date_text = "New Schedule Date: " + new_schedule_date;
        String swap_field_id = "Swap Field ID: " + sharedPrefs.getKeySwapFieldId();
        String reason_text = "Reason: " + reason;

        final TextView fieldId = new TextView(context);
        fieldId.setText(field_id_text);
        fieldId.setPadding(paddingPixel, 0, 0, 0);
        fieldId.setTypeface(null, Typeface.ITALIC);
        layout.addView(fieldId);

        final TextView old_schedule_date = new TextView(context);
        old_schedule_date.setText(old_schedule_date_text);
        old_schedule_date.setPadding(paddingPixel, 0, 0, 0);
        old_schedule_date.setTypeface(null, Typeface.ITALIC);
        layout.addView(old_schedule_date);

        final TextView tv_new_schedule_date = new TextView(context);
        tv_new_schedule_date.setText(new_schedule_date_text);
        tv_new_schedule_date.setPadding(paddingPixel, 0, 0, 0);
        tv_new_schedule_date.setTypeface(null, Typeface.ITALIC);
        layout.addView(tv_new_schedule_date);

        if (swap_flag.equalsIgnoreCase("yes")){
            final TextView tv_swap_field_id = new TextView(context);
            tv_swap_field_id.setText(swap_field_id);
            tv_swap_field_id.setPadding(paddingPixel, 0, 0, 0);
            tv_swap_field_id.setTypeface(null, Typeface.ITALIC);
            layout.addView(tv_swap_field_id);
        }

        final TextView tv_reason = new TextView(context);
        tv_reason.setText(reason_text);
        tv_reason.setPadding(paddingPixel, 0, 0, 0);
        tv_reason.setTypeface(null, Typeface.ITALIC);
        layout.addView(tv_reason);

        builder.setTitle(context.getResources().getString(R.string.reschedule_title))
                .setMessage(message)
                .setView(layout)
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    updateActivity(sharedPrefs.getKeyThreshingUniqueFieldId(),
                            reverseParseDate(tv_enter_date.getText().toString()),
                            Objects.requireNonNull(edtRescheduleReason.getText()).toString(),
                            String.valueOf(latitude),
                            String.valueOf(longitude),
                            sharedPrefs.getKeyThreshingIkNumber(),
                            sharedPrefs.getKeyThreshingCropType(),swap_flag);
                })
                .setNeutralButton(context.getResources().getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private void showDialogForExitSwap(Context context, String message,
                                   String unique_field_id, String former_schedule_date,
                                   String new_schedule_date, String reason) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForExitSwapBody(builder, context, message, unique_field_id, former_schedule_date, new_schedule_date, reason);
    }

    private void showDialogForExitSwapBody(MaterialAlertDialogBuilder builder, Context context, String message,
                                       String unique_field_id, String former_schedule_date,
                                       String new_schedule_date, String reason) {

        locationGetter = GPSController.initialiseLocationListener(this);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 20;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        String field_id_text = "Selected Field ID: " + unique_field_id;
        String swap_field_id = "Swap Field ID: " + sharedPrefs.getKeySwapFieldId();
        String old_schedule_date_text = "Selected Field ID Old Schedule Date: " + former_schedule_date;
        String new_schedule_date_text = "Selected Field ID New Schedule Date: " + new_schedule_date;
        String old_schedule_date_text_1 = "Swap Field ID Old Schedule Date: " + new_schedule_date;
        String new_schedule_date_text_1 = "Swap Field ID New Schedule Date: " + former_schedule_date;
        String reason_text = "Reason: " + reason;

        final TextView fieldId = new TextView(context);
        fieldId.setText(field_id_text);
        fieldId.setPadding(paddingPixel, 0, 0, 0);
        fieldId.setTypeface(null, Typeface.ITALIC);
        layout.addView(fieldId);

        final TextView old_schedule_date = new TextView(context);
        old_schedule_date.setText(old_schedule_date_text);
        old_schedule_date.setPadding(paddingPixel, 0, 0, 0);
        old_schedule_date.setTypeface(null, Typeface.ITALIC);
        layout.addView(old_schedule_date);

        final TextView tv_new_schedule_date = new TextView(context);
        tv_new_schedule_date.setText(new_schedule_date_text);
        tv_new_schedule_date.setPadding(paddingPixel, 0, 0, 0);
        tv_new_schedule_date.setTypeface(null, Typeface.ITALIC);
        layout.addView(tv_new_schedule_date);

        final TextView tv_swap_field_id = new TextView(context);
        tv_swap_field_id.setText(swap_field_id);
        tv_swap_field_id.setPadding(paddingPixel, 0, 0, 0);
        tv_swap_field_id.setTypeface(null, Typeface.ITALIC);
        layout.addView(tv_swap_field_id);

        final TextView old_schedule_date_swap = new TextView(context);
        old_schedule_date_swap.setText(old_schedule_date_text_1);
        old_schedule_date_swap.setPadding(paddingPixel, 0, 0, 0);
        old_schedule_date_swap.setTypeface(null, Typeface.ITALIC);
        layout.addView(old_schedule_date_swap);

        final TextView tv_new_schedule_date_swap = new TextView(context);
        tv_new_schedule_date_swap.setText(new_schedule_date_text_1);
        tv_new_schedule_date_swap.setPadding(paddingPixel, 0, 0, 0);
        tv_new_schedule_date_swap.setTypeface(null, Typeface.ITALIC);
        layout.addView(tv_new_schedule_date_swap);


        final TextView tv_reason = new TextView(context);
        tv_reason.setText(reason_text);
        tv_reason.setPadding(paddingPixel, 0, 0, 0);
        tv_reason.setTypeface(null, Typeface.ITALIC);
        layout.addView(tv_reason);


        builder.setTitle(context.getResources().getString(R.string.reschedule_title))
                .setMessage(message)
                .setView(layout)
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    updateActivity(sharedPrefs.getKeyThreshingUniqueFieldId(),
                            reverseParseDate(new_schedule_date),
                            reason,
                            String.valueOf(latitude),
                            String.valueOf(longitude),
                            sharedPrefs.getKeyThreshingIkNumber(),
                            sharedPrefs.getKeyThreshingCropType(),"Yes");
                })
                .setNeutralButton(context.getResources().getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();
                    goToHomePage();
                })
                .setCancelable(false)
                .show();
    }

    private void updateActivity(String unique_field_id, String schedule_date,
                                 String reschedule_reason, String latitude,
                                 String longitude, String ik_number, String crop_type, String swap_flag){

        //String unique_field_id, String schedule_date, String reschedule_reason, String staff_id, String date_logged

        //TODO: HTA-180
        appDatabase.scheduleThreshingActivitiesFlagDao().updateScheduleDate(
                unique_field_id,
                schedule_date,
                reschedule_reason,
                sharedPrefs.getStaffID(),
                getDate("spread")

        );

        appDatabase.logsDao().insert(new Logs(unique_field_id,sharedPrefs.getStaffID(),
                "Re Schedule threshing",getDate("normal"),sharedPrefs.getStaffRole(),
                latitude, longitude, getDeviceID(),"0",
                ik_number, crop_type));

        if (swap_flag.equalsIgnoreCase("Yes")){
            int count = appDatabase.scheduleThreshingActivitiesFlagDao().getFieldScheduleStatus(sharedPrefs.getKeySwapFieldId());
            FieldListRecyclerModel fieldListRecyclerModel = appDatabase.fieldsDao().getFieldCompleteDetails(sharedPrefs.getKeySwapFieldId());
            if (count > 0){
                appDatabase.scheduleThreshingActivitiesFlagDao().updateScheduleDate(
                        sharedPrefs.getKeySwapFieldId(),
                        raw_old_thresh_date,
                        "swapped with "+ unique_field_id,
                        sharedPrefs.getStaffID(),
                        getDate("spread")
                );
                appDatabase.logsDao().insert(new Logs(sharedPrefs.getKeySwapFieldId(),sharedPrefs.getStaffID(),
                        "Re Schedule threshing",getDate("normal"),sharedPrefs.getStaffRole(),
                        latitude, longitude, getDeviceID(),"0",
                        fieldListRecyclerModel.getIk_number(), fieldListRecyclerModel.getCrop_type()));
            }
        }


        showSuccessStart(getResources().getString(R.string.re_threshing_success),this);
    }

    private void showUrgentStart(String message, Context context){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showUrgentStartBody(builder, message, context);
    }

    private void showUrgentStartBody(MaterialAlertDialogBuilder builder, String message,
                                     Context context) {



        locationGetter = GPSController.initialiseLocationListener(this);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();

        builder.setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.scheduled), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                })
                .setNegativeButton(context.getResources().getString(R.string.urgent), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    updateUrgentActivity(sharedPrefs.getKeyThreshingUniqueFieldId(),
                            String.valueOf(latitude),
                            String.valueOf(longitude),
                            sharedPrefs.getKeyThreshingIkNumber(),
                            sharedPrefs.getKeyThreshingCropType());
                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    goToHomePage();
                })
                .setCancelable(false)
                .show();
    }

    private void updateUrgentActivity(String unique_field_id,
                                      String latitude,
                                      String longitude, String ik_number, String crop_type){

        //String unique_field_id, String schedule_date, String reschedule_reason, String staff_id, String date_logged

        appDatabase.scheduleThreshingActivitiesFlagDao().updateUrgentScheduleDate(
                unique_field_id,
                "0000-00-00",
                "urgent reschedule",
                sharedPrefs.getStaffID(),
                getDate("spread")

        );

        appDatabase.logsDao().insert(new Logs(unique_field_id,sharedPrefs.getStaffID(),
                "Re Schedule Urgent threshing",getDate("normal"),sharedPrefs.getStaffRole(),
                latitude, longitude, getDeviceID(),"0",
                ik_number, crop_type));


        showSuccessStart(getResources().getString(R.string.re_threshing_success),this);
    }

    //updateUrgentScheduleDate

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
        Intent intent = new Intent(RescheduleThreshingDateSelectionActivity.this, ThreshingActivity.class);
        startActivity(intent);
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
            startActivity(new Intent(RescheduleThreshingDateSelectionActivity.this, CalenderViewActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fillSwapSpinner(AutoCompleteTextView autoCompleteTextView, Context context) {
        String[] swapList = new String[]{"Yes", "No"};
        ArrayAdapter arrayAdapter;
        arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, swapList);
        spinnerViewController(autoCompleteTextView,arrayAdapter);
    }

    public void spinnerViewController(AutoCompleteTextView autoCompleteTextView, ArrayAdapter arrayAdapter) {
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    private void showSelectSwapField(String message, Context context){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showSelectSwapFieldBody(builder, message, context);
    }

    private void showSelectSwapFieldBody(MaterialAlertDialogBuilder builder, String message,
                                     Context context) {

        builder.setTitle(context.getResources().getString(R.string.swap_dialog_title))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    //move to select field
                    dialog.dismiss();
                    sharedPrefs.setKeyThreshingActivityRoute(DatabaseStringConstants.SWAP_SCHEDULE_DATE);
                    startActivity(new Intent(RescheduleThreshingDateSelectionActivity.this,MemberList.class));
                })
                .setNeutralButton(context.getResources().getString(R.string.no), (dialog, which) -> {
                    //move to select field
                    dialog.dismiss();
                    resetLayout();
                })
                .setCancelable(false)
                .show();
    }

    void resetLayout(){
        layout_reason.setVisibility(View.GONE);
        actSwap.setText("");
    }

    void checkIfDateChangeable(String first_selected_field, String second_selected_field){

        String first_selected_field_date = appDatabase.scheduleThreshingActivitiesFlagDao().getFieldSchedule(first_selected_field);
        String second_selected_field_date = appDatabase.scheduleThreshingActivitiesFlagDao().getFieldSchedule(second_selected_field);

        if (getDateCorrelationFlag(parseDate(first_selected_field_date), getMaximumScheduleDate()) == 0){
            showDateProblemStartSwap(first_selected_field + " "+getResources().getString(R.string.error_re_thresh_date_split_1),this);
        }else if (checkThreshHours(first_selected_field_date,sharedPrefs.getStaffID())){
            showDateProblemStartSwap(first_selected_field + " "+getResources().getString(R.string.thresh_date_error_field_split_1),this);
        }else if (getDateCorrelationFlag(parseDate(second_selected_field_date), getMaximumScheduleDate()) == 0){
            showDateProblemStartSwap(first_selected_field + " "+getResources().getString(R.string.error_re_thresh_date_split_1),this);
        }else if (checkThreshHours(second_selected_field_date,sharedPrefs.getStaffID())){
            showDateProblemStartSwap(second_selected_field + " "+getResources().getString(R.string.thresh_date_error_field_split_1),this);
        }else {
            showDialogForExitSwap(this,
                    "Please confirm rescheduling information" ,
                    sharedPrefs.getKeyThreshingUniqueFieldId(),
                    parseDate(first_selected_field_date),
                    parseDate(second_selected_field_date),
                    "Swap Field Schedule");
        }
    }
}
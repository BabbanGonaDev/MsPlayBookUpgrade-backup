package com.babbangona.mspalybookupgrade.FertilizerSignUpViews;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.ThreshingViews.ThreshingDateSelectionActivity;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.FertilizerMembers;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.Main2ActivityMethods;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FertilizerCollectionCenter extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.last_sync_date_tv)
    TextView last_sync_date_tv;

    @BindView(R.id.tv_staff_id)
    TextView tv_staff_id;

    @BindView(R.id.actVillage)
    AutoCompleteTextView actVillage;

    @BindView(R.id.edlVillage)
    TextInputLayout edlVillage;

    @BindView(R.id.actVillageCopy)
    AutoCompleteTextView actVillageCopy;

    @BindView(R.id.edlVillageCopy)
    TextInputLayout edlVillageCopy;

    @BindView(R.id.btnSubmit)
    MaterialButton btnSubmit;

    @BindView(R.id.fertilizer_cc_confirmation_dialog)
    LinearLayout fertilizer_cc_confirmation_dialog;

    @BindView(R.id.bottom_sheet_village)
    TextView bottom_sheet_village;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    String lga_selection;
    String ward_selection;
    String state_selection;
    SharedPrefs sharedPrefs;
    AppDatabase appDatabase;
    String harvest_collection_center;
    SetPortfolioMethods setPortfolioMethods;
    Main2ActivityMethods main2ActivityMethods;
    private BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_cc_selection);
        ButterKnife.bind(FertilizerCollectionCenter.this);
        setSupportActionBar(toolbar);
        appDatabase = AppDatabase.getInstance(FertilizerCollectionCenter.this);
        setPortfolioMethods = new SetPortfolioMethods();
        main2ActivityMethods = new Main2ActivityMethods(FertilizerCollectionCenter.this);
        sharedPrefs = new SharedPrefs(FertilizerCollectionCenter.this);
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(FertilizerCollectionCenter.this));

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        fillVillageSpinner(actVillage, FertilizerCollectionCenter.this);

        actVillage.setOnItemClickListener((parent, view, position, id) -> {
            clearSpinnerText(actVillageCopy);
            harvest_collection_center = (String)parent.getItemAtPosition(position);
            fillVillageSpinner(actVillageCopy, FertilizerCollectionCenter.this);
        });

        sheetBehavior = BottomSheetBehavior.from(fertilizer_cc_confirmation_dialog);
        addBehaviourToBottomSheet(sheetBehavior);
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,FertilizerCollectionCenter.this);
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

    @OnClick(R.id.btnSubmit)
    public  void next(View view){
        if (validateMemberInfo(actVillage, actVillageCopy) == 0){
            checkForEmptyFields();
        } else if(!actVillage.getText().toString().trim().matches(actVillageCopy.getText().toString().trim())){
            checkForMismatchedCollectionCenter();
        }else{
            checkForMismatchedCollectionCenter();
            checkForEmptyFields();
            setBottomSheerTexts();
            showBottomSheet();
        }
    }

    void checkForMismatchedCollectionCenter(){
        if (!actVillage.getText().toString().trim().matches(actVillageCopy.getText().toString().trim())){
            setErrorOfTextView(edlVillage, getResources().getString(R.string.error_collection_center_mismatch));
            setErrorOfTextView(edlVillageCopy, getResources().getString(R.string.error_collection_center_mismatch));
        }else{
            removeErrorFromText(edlVillage);
            removeErrorFromText(edlVillageCopy);
        }
    }

    public int validateMemberInfo(AutoCompleteTextView village, AutoCompleteTextView confirm_village){

        // Checks if the village is empty
        if(Objects.requireNonNull(village.getText()).toString().matches("")) {
            return 0;
        }

        // Checks if the confirm village field is empty
        else if(Objects.requireNonNull(confirm_village.getText()).toString().matches("")) {
            return 0;
        }

        //all checks are passed
        else{
            return 1;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (sheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                fertilizer_cc_confirmation_dialog.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        return super.dispatchTouchEvent(event);
    }

    @OnClick(R.id.btnCancel)
    public  void setBtnCancel(){
        showBottomSheet();
    }

    @OnClick(R.id.btnConfirm)
    public  void setBtnConfirm(View view){
        if (updateFertilizerMembers(harvest_collection_center, sharedPrefs.getKeyFertilizerSignUpMemberId()
        ).equalsIgnoreCase("1")){
            showBottomSheet();
            showDialogForPassedVerification(this.getResources().getString(R.string.fertilizer_collection_point_success), FertilizerCollectionCenter.this);
        }
    }

    void checkForEmptyFields(){
        checkIfAutocompleteEmpty(actVillage,edlVillage,this.getResources().getString(R.string.error_fertilizer_collection_point_location));
        checkIfAutocompleteEmpty(actVillageCopy,edlVillageCopy,this.getResources().getString(R.string.error_fertilizer_collection_point_location));
    }

    public void clearSpinnerText(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setText("");
    }

    public void spinnerViewController(AutoCompleteTextView autoCompleteTextView, ArrayAdapter arrayAdapter) {
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    public void fillVillageSpinner(AutoCompleteTextView autoCompleteTextView, Context context) {
        ArrayAdapter village = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.harvestLocationsDao().getAllCollectionPointsNoConstraint());
        spinnerViewController(autoCompleteTextView,village);
    }

    public void checkIfAutocompleteEmpty(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout, String error_message) {
        String textEntered = Objects.requireNonNull(autoCompleteTextView.getText()).toString();
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

    void setBottomSheerTexts(){
        bottom_sheet_village.setText(Objects.requireNonNull(actVillage.getText()).toString());
    }

    void showBottomSheet(){
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    public void showDialogForPassedVerification(String s, Context context) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForPassedVerification(builder,s,context);
    }

    public void showDialogForPassedVerification(MaterialAlertDialogBuilder builder, String s, Context context) {
        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_check_green_24dp))
                .setTitle(context.getResources().getString(R.string.dialog_congrats))
                .setMessage(s)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    backToHomePage();
                }).setCancelable(false)
                .show();
    }

    String updateFertilizerMembers(String location_id, String unique_member_id){
        String flag;
        int member_existence = appDatabase.fertilizerMembersDao().getFertilizerMemberCount(unique_member_id);
        Members.MemberDetails memberDetails = appDatabase.membersDao().getMemberDetails(unique_member_id);
        try {
            if (member_existence > 0){
                appDatabase.fertilizerMembersDao().updateCollectionCenter(location_id, unique_member_id);
            }else{
                appDatabase.fertilizerMembersDao().insert(new FertilizerMembers(
                        unique_member_id,
                        memberDetails.getFirst_name(),
                        memberDetails.getLast_name(),
                        memberDetails.getIk_number(),
                        memberDetails.getVillage_name(),
                        sharedPrefs.getKeyFertilizerRecaptureFlag(),
                        sharedPrefs.getKeyFertilizerTemplate(),
                        "0",
                        sharedPrefs.getKeyFertilizerMemberPresence(),
                        location_id,
                        sharedPrefs.getStaffID(),
                        BuildConfig.VERSION_NAME,
                        getDeviceID(),
                        "0"));
            }
            flag = "1";
        } catch (Exception e) {
            e.printStackTrace();
            flag = "0";
        }
        return flag;
    }

    void backToHomePage(){
        finish();
        startActivity(new Intent(FertilizerCollectionCenter.this, FertilizerSignUpHome.class));
    }

    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            finish();
            startActivity(new Intent(FertilizerCollectionCenter.this, FertilizerSignUpMembers.class));
        }
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
}

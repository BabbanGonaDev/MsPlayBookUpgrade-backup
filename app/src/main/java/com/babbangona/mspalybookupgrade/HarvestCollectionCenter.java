package com.babbangona.mspalybookupgrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.Main2ActivityMethods;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HarvestCollectionCenter extends AppCompatActivity {

    @BindView(R.id.actState)
    AutoCompleteTextView actState;

    @BindView(R.id.edlState)
    TextInputLayout edlState;

    @BindView(R.id.actLga)
    AutoCompleteTextView actLga;

    @BindView(R.id.edlLga)
    TextInputLayout edlLga;

    @BindView(R.id.actWard)
    AutoCompleteTextView actWard;

    @BindView(R.id.edlWard)
    TextInputLayout edlWard;

    @BindView(R.id.actVillage)
    AutoCompleteTextView actVillage;

    @BindView(R.id.edlVillage)
    TextInputLayout edlVillage;

    @BindView(R.id.toolbar_harvest_location)
    Toolbar toolbar_harvest_location;

    @BindView(R.id.btnAddCollectionPoint)
    MaterialButton btnAddCollectionPoint;

    @BindView(R.id.harvest_cc_confirmation_dialog)
    LinearLayout harvest_cc_confirmation_dialog;

    @BindView(R.id.bottom_sheet_state)
    TextView bottom_sheet_state;

    @BindView(R.id.bottom_sheet_lga)
    TextView bottom_sheet_lga;

    @BindView(R.id.bottom_sheet_ward)
    TextView bottom_sheet_ward;

    @BindView(R.id.bottom_sheet_village)
    TextView bottom_sheet_village;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    private BottomSheetBehavior sheetBehavior;
    AppDatabase appDatabase;
    Main2ActivityMethods main2ActivityMethods;
    SharedPrefs sharedPrefs;
    String state_selection;
    String lga_selection;
    String ward_selection;
    String harvest_collection_center;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harvest_collection_center);
        ButterKnife.bind(HarvestCollectionCenter.this);
        setSupportActionBar(toolbar_harvest_location);
        appDatabase = AppDatabase.getInstance(HarvestCollectionCenter.this);
        main2ActivityMethods = new Main2ActivityMethods(HarvestCollectionCenter.this);
        sharedPrefs = new SharedPrefs(HarvestCollectionCenter.this);

        fillStateSpinner(actState,HarvestCollectionCenter.this);

        toolbar_harvest_location.setNavigationOnClickListener(v -> goBackFromToolBar());

        actState.setOnItemClickListener((parent, view, position , rowId) -> {
            clearSpinnerText(actLga);
            clearSpinnerText(actWard);
            clearSpinnerText(actVillage);
            state_selection = (String)parent.getItemAtPosition(position);
            fillLgaSpinner(actLga,HarvestCollectionCenter.this,state_selection);
        });

        actLga.setOnItemClickListener((parent, view, position, id) -> {
            clearSpinnerText(actWard);
            clearSpinnerText(actVillage);
            lga_selection = (String)parent.getItemAtPosition(position);
            fillWardSpinner(actWard,HarvestCollectionCenter.this,state_selection,lga_selection);
        });

        actWard.setOnItemClickListener((parent, view, position, id) -> {
            clearSpinnerText(actVillage);
            ward_selection = (String)parent.getItemAtPosition(position);
            fillVillageSpinner(actVillage,HarvestCollectionCenter.this,state_selection,lga_selection,ward_selection);
        });

        actVillage.setOnItemClickListener((parent, view, position, id) -> harvest_collection_center = (String)parent.getItemAtPosition(position));

        sheetBehavior = BottomSheetBehavior.from(harvest_cc_confirmation_dialog);
        addBehaviourToBottomSheet(sheetBehavior);
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

    @OnClick(R.id.btnAddCollectionPoint)
    public  void next(View view){
        if (main2ActivityMethods.validateMemberInfo(actState,actLga,actWard,actVillage) == 0){
            checkForEmptyFields();
        }else{
            checkForEmptyFields();
            setBottomSheerTexts();
            showBottomSheet();
        }
    }



    @OnClick(R.id.btnCancel)
    public  void setBtnCancel(){
        showBottomSheet();
    }

    @OnClick(R.id.btnConfirm)
    public  void setBtnConfirm(View view){
        if (updateNormalActivitiesFlag(harvest_collection_center, sharedPrefs.getKeyHarvestCcUniqueFieldId()
        ).equalsIgnoreCase("1")){
            showDialogForPassedVerification(this.getResources().getString(R.string.harvest_collection_point_success),HarvestCollectionCenter.this);
        }
    }

    void checkForEmptyFields(){
        checkIfAutocompleteEmpty(actState,edlState,this.getResources().getString(R.string.error_message_state));
        checkIfAutocompleteEmpty(actLga,edlLga,this.getResources().getString(R.string.error_message_lga));
        checkIfAutocompleteEmpty(actWard,edlWard,this.getResources().getString(R.string.error_message_ward));
        checkIfAutocompleteEmpty(actVillage,edlVillage,this.getResources().getString(R.string.error_harvest_collection_point_location));
    }

    public void clearSpinnerText(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setText("");
    }

    public void spinnerViewController(AutoCompleteTextView autoCompleteTextView, ArrayAdapter arrayAdapter) {
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    public void fillStateSpinner(AutoCompleteTextView autoCompleteTextView, Context context) {
        ArrayAdapter states = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.harvestLocationsDao().getAllStates());
        spinnerViewController(autoCompleteTextView,states);
    }

    public void fillLgaSpinner(AutoCompleteTextView autoCompleteTextView, Context context, String state) {
        ArrayAdapter lga = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.harvestLocationsDao().getAllLgs(state));
        spinnerViewController(autoCompleteTextView,lga);
    }

    public void fillWardSpinner(AutoCompleteTextView autoCompleteTextView, Context context, String state, String lga) {
        ArrayAdapter ward = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.harvestLocationsDao().getAllWards(state,lga));
        spinnerViewController(autoCompleteTextView,ward);
    }

    public void fillVillageSpinner(AutoCompleteTextView autoCompleteTextView, Context context, String state, String lga, String ward) {
        ArrayAdapter village;
        if ((state == null || state.equalsIgnoreCase("")) &&
                (lga == null || lga.equalsIgnoreCase("")) &&
                (ward == null || ward.equalsIgnoreCase(""))){
            village = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.harvestLocationsDao().getAllCollectionPointsNoConstraint());
        }else{
            village = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.harvestLocationsDao().getAllCollectionPoints(state,lga,ward));
        }
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
        bottom_sheet_state.setText(Objects.requireNonNull(actState.getText()).toString());
        bottom_sheet_lga.setText(Objects.requireNonNull(actLga.getText()).toString());
        bottom_sheet_ward.setText(Objects.requireNonNull(actWard.getText()).toString());
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
                    onBackPressed();
                }).setCancelable(false)
                .show();
    }

    String updateNormalActivitiesFlag(String location_id, String unique_field_id){
        String flag;
        int field_existence = appDatabase.normalActivitiesFlagDao().countFieldInNormalActivity(unique_field_id);
        try {
            if (field_existence > 0){
                appDatabase.normalActivitiesFlagDao().updateLocationID(location_id, unique_field_id);
            }else{
                appDatabase.normalActivitiesFlagDao().insert(new NormalActivitiesFlag(unique_field_id,
                        "0","0000-00-00","0","0000-00-00",
                        sharedPrefs.getStaffID(),"0",sharedPrefs.getKeyHarvestCcIkNumber(),
                        sharedPrefs.getKeyHarvestCcCropType(),location_id,"0000-00-00"));
            }
            flag = "1";
        } catch (Exception e) {
            e.printStackTrace();
            flag = "0";
        }
        return flag;
    }

    void goBackFromToolBar(){
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

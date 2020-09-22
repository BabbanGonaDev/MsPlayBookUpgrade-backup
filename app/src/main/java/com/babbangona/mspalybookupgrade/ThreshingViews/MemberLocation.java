package com.babbangona.mspalybookupgrade.ThreshingViews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.daos.ThreshingLocation;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MemberLocation extends AppCompatActivity {

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
    TextInputEditText actVillage;

    @BindView(R.id.actVillageCopy)
    TextInputEditText actVillageCopy;

    @BindView(R.id.edlVillage)
    TextInputLayout edlVillage;

    @BindView(R.id.edlVillageCopy)
    TextInputLayout edlVillageCopy;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.btnAddCollectionPoint)
    MaterialButton btnAddCollectionPoint;

    @BindView(R.id.member_location_confirmation_dialog)
    LinearLayout member_location_confirmation_dialog;

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
    SharedPrefs sharedPrefs;
    String state_selection;
    String lga_selection;
    String ward_selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_location);
        ButterKnife.bind(MemberLocation.this);
        setSupportActionBar(toolbar);
        appDatabase = AppDatabase.getInstance(MemberLocation.this);
        sharedPrefs = new SharedPrefs(MemberLocation.this);
        toolbar.setNavigationOnClickListener(v -> showDialogForExit(MemberLocation.this));

        fillStateSpinner(actState,MemberLocation.this);

        actState.setOnItemClickListener((parent, view, position , rowId) -> {
            clearSpinnerText(actLga);
            clearSpinnerText(actWard);
            clearText(actVillage);
            clearText(actVillageCopy);
            state_selection = (String)parent.getItemAtPosition(position);
            fillLgaSpinner(actLga,MemberLocation.this,state_selection);
        });

        actLga.setOnItemClickListener((parent, view, position, id) -> {
            clearSpinnerText(actWard);
            clearText(actVillage);
            clearText(actVillageCopy);
            lga_selection = (String)parent.getItemAtPosition(position);
            fillWardSpinner(actWard,MemberLocation.this,state_selection,lga_selection);
        });

        actWard.setOnItemClickListener((parent, view, position, id) -> {
            clearText(actVillage);
            clearText(actVillageCopy);
            ward_selection = (String)parent.getItemAtPosition(position);
        });

        sheetBehavior = BottomSheetBehavior.from(member_location_confirmation_dialog);
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
        if (validateMemberInfo(actState,actLga,actWard,actVillage,actVillageCopy) == 0){
            checkForEmptyFields();
        }else{
            if(!Objects.requireNonNull(actVillage.getText()).toString().equalsIgnoreCase(Objects.requireNonNull(actVillageCopy.getText()).toString())) {
                checkForEmptyFields();
            }else{
                checkForEmptyFields();
                setBottomSheerTexts();
                showBottomSheet();
            }
        }
    }



    @OnClick(R.id.btnCancel)
    public  void setBtnCancel(){
        showBottomSheet();
    }

    @OnClick(R.id.btnConfirm)
    public  void setBtnConfirm(View view){
        if (updateMembersLocation(
                getLocationID(state_selection, lga_selection, ward_selection),
                sharedPrefs.getKeyThreshingUniqueMemberId(),
                sharedPrefs.getKeyMemberVillageLongitude(),
                sharedPrefs.getKeyMemberVillageLatitude(),
                Objects.requireNonNull(actVillage.getText()).toString().trim(),
                state_selection,
                lga_selection,
                ward_selection
        ).equalsIgnoreCase("1")){
            showDialogForPassedVerification(this.getResources().getString(R.string.location_saved),MemberLocation.this);
        }
    }

    void checkForEmptyFields(){
        checkIfAutocompleteEmpty(actState,edlState,this.getResources().getString(R.string.member_loc_toast_state_error));
        checkIfAutocompleteEmpty(actLga,edlLga,this.getResources().getString(R.string.member_loc_toast_lga_error));
        checkIfAutocompleteEmpty(actWard,edlWard,this.getResources().getString(R.string.member_loc_toast_ward_error));
        checkIfTextInputEmpty(actVillage,edlVillage,this.getResources().getString(R.string.village_error));
        checkIfTextInputEmpty(actVillageCopy,edlVillageCopy,this.getResources().getString(R.string.village_error));
        if (!Objects.requireNonNull(actVillage.getText()).toString().equalsIgnoreCase("") &&
                !Objects.requireNonNull(actVillageCopy.getText()).toString().equalsIgnoreCase("")){
            checkIfTextVillageMismatch(actVillage,edlVillage,actVillageCopy,edlVillageCopy,this.getResources().getString(R.string.village_copy_error));
        }
    }

    public void clearSpinnerText(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setText("");
    }

    public void clearText(TextInputEditText textInputEditText) {
        textInputEditText.setText("");
    }

    public void spinnerViewController(AutoCompleteTextView autoCompleteTextView, ArrayAdapter<String> arrayAdapter) {
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    public void fillStateSpinner(AutoCompleteTextView autoCompleteTextView, Context context) {
        ArrayAdapter<String> states = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.locationDao().getAllStates());
        spinnerViewController(autoCompleteTextView,states);
    }

    public void fillLgaSpinner(AutoCompleteTextView autoCompleteTextView, Context context, String state) {
        ArrayAdapter<String> lga = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.locationDao().getAllLgs(state));
        spinnerViewController(autoCompleteTextView,lga);
    }

    public void fillWardSpinner(AutoCompleteTextView autoCompleteTextView, Context context, String state, String lga) {
        ArrayAdapter<String> ward = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, appDatabase.locationDao().getAllWards(state,lga));
        spinnerViewController(autoCompleteTextView,ward);
    }

    public void checkIfAutocompleteEmpty(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout, String error_message) {
        String textEntered = Objects.requireNonNull(autoCompleteTextView.getText()).toString();
        if (textEntered.equalsIgnoreCase("")){
            setErrorOfTextView(textInputLayout, error_message);
        }else{
            removeErrorFromText(textInputLayout);
        }
    }

    public void checkIfTextInputEmpty(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String error_message) {
        String textEntered = Objects.requireNonNull(textInputEditText.getText()).toString();
        if (textEntered.equalsIgnoreCase("")){
            setErrorOfTextView(textInputLayout, error_message);
        }else{
            removeErrorFromText(textInputLayout);
        }
    }

    public void checkIfTextVillageMismatch(TextInputEditText textInputEditText, TextInputLayout textInputLayout,
                                           TextInputEditText textInputEditText1, TextInputLayout textInputLayout1, String error_message) {
        String textEntered = Objects.requireNonNull(textInputEditText.getText()).toString();
        String textEnteredCopy = Objects.requireNonNull(textInputEditText1.getText()).toString();
        if (!textEntered.equalsIgnoreCase(textEnteredCopy)){
            setErrorOfTextView(textInputLayout, error_message);
            setErrorOfTextView(textInputLayout1, error_message);
        }else{
            removeErrorFromText(textInputLayout);
            removeErrorFromText(textInputLayout1);
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
                .setTitle(context.getResources().getString(R.string.congratulations))
                .setMessage(s)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    loadThreshingHome();
                }).setCancelable(false)
                .show();
    }

    String updateMembersLocation(String location_id, String unique_member_id, String latitude,
                                 String longitude, String village_name, String state, String lga, String ward){
        String flag;
        try {
            appDatabase.threshingLocationDao().insert(new ThreshingLocation(unique_member_id,location_id,
                    village_name, sharedPrefs.getStaffID(),latitude,longitude,"0",state, lga, ward, BuildConfig.VERSION_NAME));
            flag = "1";
        } catch (Exception e) {
            e.printStackTrace();
            flag = "0";
        }
        return flag;
    }

    void loadThreshingHome(){
        finish();
        Intent intent = new Intent(MemberLocation.this, ThreshingActivity.class);
        startActivity(intent);
    }

    String getLocationID(String state, String lga, String ward){
        String result = appDatabase.locationDao().getId(state, lga, ward);
        if (result == null){
            result = "0";
        }
        return result;
    }

    public int validateMemberInfo(AutoCompleteTextView state, AutoCompleteTextView lga, AutoCompleteTextView ward, TextInputEditText village, TextInputEditText villageCopy){

        // Checks if the state field is empty
        if(Objects.requireNonNull(state.getText()).toString().matches("")) {
            return 0;
        }

        // Checks if the lga field is empty
        else if(Objects.requireNonNull(lga.getText()).toString().matches("")) {
            return 0;
        }

        // Checks if the ward field is empty
        else if(Objects.requireNonNull(ward.getText()).toString().matches("")) {
            return 0;
        }

        // Checks if the village is empty
        else if(Objects.requireNonNull(village.getText()).toString().matches("")) {
            return 0;
        }

        // Checks if the village copy is empty
        else if(Objects.requireNonNull(villageCopy.getText()).toString().matches("")) {
            return 0;
        }

        //all checks are passed
        else{
            return 1;
        }
    }

    @Override
    public void onBackPressed() {
        showDialogForExit(MemberLocation.this);
    }

    private void showDialogForExit(Context context) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForExitBody(builder, context);
    }

    private void showDialogForExitBody(MaterialAlertDialogBuilder builder, Context context) {
        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_warning))
                .setTitle(context.getResources().getString(R.string.warning))
                .setMessage(context.getResources().getString(R.string.error_going_back))
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    goToHomePage();
                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }

    void goToHomePage(){
        finish();
        Intent intent = new Intent(MemberLocation.this, ThreshingActivity.class);
        startActivity(intent);
    }

}

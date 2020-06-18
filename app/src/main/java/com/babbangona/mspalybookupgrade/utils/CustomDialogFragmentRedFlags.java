package com.babbangona.mspalybookupgrade.utils;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.babbangona.mspalybookupgrade.HGFieldListPage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler.HGFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.RFActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
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

    private SharedPrefs sharedPrefs;

    private SetPortfolioMethods setPortfolioMethods;

    private AppDatabase appDatabase;

    String hg_type_selection;

    HGFieldListRecyclerModel hgFieldListRecyclerModel;

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
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("");
        GPSController.initialiseLocationListener(getActivity());

        toolbar_hg_fragment.setNavigationOnClickListener(v -> dismissAndRefresh());

        String field_r_id = hgFieldListRecyclerModel.getUnique_field_id();
        String ik_number = Objects.requireNonNull(getActivity()).getResources().getString(R.string.ik_number) +" "+ hgFieldListRecyclerModel.getIk_number();
        String member_name = getActivity().getResources().getString(R.string.member_name) +" "+ hgFieldListRecyclerModel.getMember_name();
        String phone_number = getActivity().getResources().getString(R.string.member_phone_number) +" "+ hgFieldListRecyclerModel.getPhone_number();
        String field_size = getActivity().getResources().getString(R.string.field_size) +" "+ hgFieldListRecyclerModel.getField_size();
        String village = getActivity().getResources().getString(R.string.member_village) +" "+ hgFieldListRecyclerModel.getVillage_name();
        String crop_type = getActivity().getResources().getString(R.string.member_crop_type) +" "+ hgFieldListRecyclerModel.getCrop_type();

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


        return fragmentLayout;
    }

    void allowButton(){
        if (validateFieldsInfo(actHGType) == 0){
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
        if (validateFieldsInfo(actHGType) == 0){
            checkForEmptyFields();
        }else{
            checkForEmptyFields();
            GPSController.LocationGetter locationGetter = GPSController.initialiseLocationListener(Objects.requireNonNull(getActivity()));
            double latitude = locationGetter.getLatitude();
            double longitude = locationGetter.getLongitude();
            hgFieldListRecyclerModel = sharedPrefs.getKeyHgFieldModel();
            updateActivity(hgFieldListRecyclerModel,actHGType.getText().toString(),latitude,longitude);
        }
    }

    void checkForEmptyFields(){
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

    public int validateFieldsInfo(AutoCompleteTextView hg_type){

        // Checks if the state field is empty
        if(Objects.requireNonNull(hg_type.getText()).toString().matches("")) {
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

    private void updateActivity(HGFieldListRecyclerModel hgFieldListRecyclerModel,
                                String hg_selected, double latitude, double longitude){
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
            appDatabase.rfActivitiesFlagDao().updateRFFlag(hgFieldListRecyclerModel.getUnique_field_id(),hg_selected,flag,
                    getDate("spread"),sharedPrefs.getStaffID());
            appDatabase.logsDao().insert(new Logs(hgFieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                    initial_activity,getDate("normal"),sharedPrefs.getStaffRole(),
                    String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0",
                    hgFieldListRecyclerModel.getIk_number(),hgFieldListRecyclerModel.getCrop_type()));
            dismissAndRefresh();
        }else{
            if (!initial_activity.startsWith("Solve_")){
                appDatabase.rfActivitiesFlagDao().insert(new RFActivitiesFlag(hgFieldListRecyclerModel.getUnique_field_id(),
                        hg_selected,getDate("spread"),flag, sharedPrefs.getStaffID(),"0",hgFieldListRecyclerModel.getIk_number(),
                        hgFieldListRecyclerModel.getCrop_type()));

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
    }

}

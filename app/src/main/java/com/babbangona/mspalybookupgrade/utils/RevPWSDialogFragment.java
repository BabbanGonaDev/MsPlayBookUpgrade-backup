package com.babbangona.mspalybookupgrade.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.babbangona.mspalybookupgrade.PCPWSHomePage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.PCPWSActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RevPWSDialogFragment extends DialogFragment {

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

    @BindView(R.id.actCategory)
    AutoCompleteTextView actCategory;

    @BindView(R.id.edlCategory)
    TextInputLayout edlCategory;

    @BindView(R.id.tv_picture_name_1)
    MaterialTextView tv_picture_name_1;

    @BindView(R.id.btn_capture_picture_1)
    MaterialButton btn_capture_picture_1;

    @BindView(R.id.btn_cancel_pic_1)
    MaterialButton btn_cancel_pic_1;

    @BindView(R.id.tv_picture_name_2)
    MaterialTextView tv_picture_name_2;

    @BindView(R.id.btn_capture_picture_2)
    MaterialButton btn_capture_picture_2;

    @BindView(R.id.btn_cancel_pic_2)
    MaterialButton btn_cancel_pic_2;

    @BindView(R.id.tv_picture_name_3)
    MaterialTextView tv_picture_name_3;

    @BindView(R.id.btn_capture_picture_3)
    MaterialButton btn_capture_picture_3;

    @BindView(R.id.btn_cancel_pic_3)
    MaterialButton btn_cancel_pic_3;

    @BindView(R.id.tv_picture_name_4)
    MaterialTextView tv_picture_name_4;

    @BindView(R.id.btn_capture_picture_4)
    MaterialButton btn_capture_picture_4;

    @BindView(R.id.btn_cancel_pic_4)
    MaterialButton btn_cancel_pic_4;

    @BindView(R.id.tv_pws_map)
    MaterialTextView tv_pws_map;

    @BindView(R.id.btn_map_pws)
    MaterialButton btn_map_pws;

    @BindView(R.id.edlMIKDescription)
    TextInputLayout edlMIKDescription;

    @BindView(R.id.edtMIKDescription)
    TextInputEditText edtMIKDescription;

    @BindView(R.id.btnSubmitPWS)
    MaterialButton btnSubmitPWS;

    private SharedPrefs sharedPrefs;

    private SetPortfolioMethods setPortfolioMethods;

    private AppDatabase appDatabase;

    String pws_category_selection;

    PWSFieldListRecyclerModel pwsFieldListRecyclerModel;

    private GPSController.LocationGetter locationGetter;

    private static final int CAMERA_REQUEST_1           = 101;
    private static final int CAMERA_REQUEST_2           = 102;
    private static final int CAMERA_REQUEST_3           = 103;
    private static final int CAMERA_REQUEST_4           = 104;
    private static final int MAPPING_ACTIVITY_REQUEST   = 105;
    private int CAMERA_REQUEST                          = 0;

    private static final String IMAGE_CAPTURE_1         = "1";
    private static final String IMAGE_CAPTURE_2         = "2";
    private static final String IMAGE_CAPTURE_3         = "3";
    private static final String IMAGE_CAPTURE_4         = "4";

    private static final int MY_CAMERA_PERMISSION_CODE  = 100;

    Bitmap photo1 = null;
    Bitmap photo2 = null;
    Bitmap photo3 = null;
    Bitmap photo4 = null;

    PWSFieldListRecyclerModel.PWSMapModel pwsMapModel;



    @BindView(R.id.confirmation_dialog)
    LinearLayout confirmation_dialog;

    @BindView(R.id.btnNo)
    MaterialButton btnNo;

    @BindView(R.id.btnYes)
    MaterialButton btnYes;

    private BottomSheetBehavior sheetBehavior;

    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View fragmentLayout = inflater.inflate(R.layout.log_pws_holder, container, false);
        ButterKnife.bind(this, fragmentLayout);
        unbinder = ButterKnife.bind(this, fragmentLayout);
        setPortfolioMethods = new SetPortfolioMethods();
        appDatabase = AppDatabase.getInstance(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());
        pwsFieldListRecyclerModel = new PWSFieldListRecyclerModel();
        pwsMapModel = new PWSFieldListRecyclerModel.PWSMapModel();
        pwsFieldListRecyclerModel = sharedPrefs.getKeyPWSFieldModel();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar_hg_fragment);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Log PWS Activity");
        GPSController.initialiseLocationListener(getActivity());
        sharedPrefs.setKeyImageCaptureFlag("0");
        actCategory.setEnabled(false);

        toolbar_hg_fragment.setNavigationOnClickListener(v -> dismissAndRefresh());

        String field_r_id = pwsFieldListRecyclerModel.getUnique_field_id();
        String ik_number = Objects.requireNonNull(getActivity()).getResources().getString(R.string.ik_number) +" "+ pwsFieldListRecyclerModel.getIk_number();
        String member_name = getActivity().getResources().getString(R.string.member_name) +" "+ pwsFieldListRecyclerModel.getMember_name();
        String phone_number = getActivity().getResources().getString(R.string.member_phone_number) +" "+ pwsFieldListRecyclerModel.getPhone_number();
        String field_size = getActivity().getResources().getString(R.string.field_size) +" "+ pwsFieldListRecyclerModel.getField_size();
        String village = getActivity().getResources().getString(R.string.member_village) +" "+ pwsFieldListRecyclerModel.getVillage_name();
        String crop_type = getActivity().getResources().getString(R.string.member_crop_type) +" "+ pwsFieldListRecyclerModel.getCrop_type();
        String member_r_id = getActivity().getResources().getString(R.string.member_r_id) +" "+ pwsFieldListRecyclerModel.getField_r_id();
        tv_member_r_id.setText(member_r_id);

        tv_field_r_id.setText(field_r_id);
        tv_ik_number.setText(ik_number);
        tv_member_name.setText(member_name);
        tv_phone_number.setText(phone_number);
        tv_field_size.setText(field_size);
        tv_village_name.setText(village);
        tv_crop_type.setText(crop_type);

        String category = appDatabase.pwsActivitiesFlagDao().getPWSCategory(sharedPrefs.getKeyPwsId());
        actCategory.setText(category);

        //fillCategoryListSpinner(actCategory,getActivity());

        /*actCategory.setOnItemClickListener((parent, view, position , rowId) -> {
            pws_category_selection = (String)parent.getItemAtPosition(position);
            clearPictureCaptured();
        });*/

        showAllCapture();
        clearPictureCaptured1();

        sheetBehavior = BottomSheetBehavior.from(confirmation_dialog);

        addBehaviourToBottomSheet(sheetBehavior);

        return fragmentLayout;
    }

    void showAllCapture(){
        showCapture(btn_capture_picture_1,btn_cancel_pic_1);
        showCapture(btn_capture_picture_2,btn_cancel_pic_2);
        showCapture(btn_capture_picture_3,btn_cancel_pic_3);
        showCapture(btn_capture_picture_4,btn_cancel_pic_4);
    }

    @OnClick(R.id.btn_capture_picture_1)
    @RequiresApi(api = Build.VERSION_CODES.M)
    void setBtn_capture_picture_1(View view){
        if (locationCheck()){
            if (actCategory.getText().toString().equalsIgnoreCase("")){
                checkForEmptyAutocompleteFields();
            }else{
                checkForEmptyAutocompleteFields();
                if (ContextCompat.checkSelfPermission(view.getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    CAMERA_REQUEST = CAMERA_REQUEST_1;
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_1);
                }
            }
        }else{
            showLocationError();
        }

    }

    @OnClick(R.id.btn_cancel_pic_1)
    void setBtn_cancel_pic_1(){
        cancelCapturedPicture(IMAGE_CAPTURE_1,btn_capture_picture_1,btn_cancel_pic_1);
        resetTextViewText(tv_picture_name_1, Objects.requireNonNull(getActivity()).getResources().getString(R.string.pws_log_picture1));
    }

    @OnClick(R.id.btn_capture_picture_2)
    @RequiresApi(api = Build.VERSION_CODES.M)
    void setBtn_capture_picture_2(View view){
        Set<String> get_added_capture = sharedPrefs.getKeyPwsCaptureList();
        if (locationCheck()){
            if (actCategory.getText().toString().equalsIgnoreCase("")){
                checkForEmptyAutocompleteFields();
            }else{
                checkForEmptyAutocompleteFields();
                if (!get_added_capture.contains(IMAGE_CAPTURE_1)){
                    Toast.makeText(getActivity(), "Please capture Picture 1 before taking this picture", Toast.LENGTH_SHORT).show();
                }else{
                    if (ContextCompat.checkSelfPermission(view.getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        CAMERA_REQUEST = CAMERA_REQUEST_2;
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_2);
                    }
                }
            }
        }else{
            showLocationError();
        }
    }

    @OnClick(R.id.btn_cancel_pic_2)
    void setBtn_cancel_pic_2(){
        cancelCapturedPicture(IMAGE_CAPTURE_2,btn_capture_picture_2,btn_cancel_pic_2);
        resetTextViewText(tv_picture_name_2, Objects.requireNonNull(getActivity()).getResources().getString(R.string.pws_log_picture2));
    }

    @OnClick(R.id.btn_capture_picture_3)
    @RequiresApi(api = Build.VERSION_CODES.M)
    void setBtn_capture_picture_3(View view){
        Set<String> get_added_capture = sharedPrefs.getKeyPwsCaptureList();
        if (locationCheck()){
            if (actCategory.getText().toString().equalsIgnoreCase("")){
                checkForEmptyAutocompleteFields();
            }else{
                checkForEmptyAutocompleteFields();
                if (get_added_capture.contains(IMAGE_CAPTURE_1) && get_added_capture.contains(IMAGE_CAPTURE_2)){
                    if (ContextCompat.checkSelfPermission(view.getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        CAMERA_REQUEST = CAMERA_REQUEST_3;
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_3);
                    }
                }else{
                    Toast.makeText(getActivity(), "Please capture Pictures 1 and 2 before taking this picture", Toast.LENGTH_SHORT).show();

                }
            }
        }else{
            showLocationError();
        }


    }

    @OnClick(R.id.btn_cancel_pic_3)
    void setBtn_cancel_pic_3(){
        cancelCapturedPicture(IMAGE_CAPTURE_3,btn_capture_picture_3,btn_cancel_pic_3);
        resetTextViewText(tv_picture_name_3, Objects.requireNonNull(getActivity()).getResources().getString(R.string.pws_log_picture3));
    }

    @OnClick(R.id.btn_capture_picture_4)
    @RequiresApi(api = Build.VERSION_CODES.M)
    void setBtn_capture_picture_4(View view){
        Set<String> get_added_capture = sharedPrefs.getKeyPwsCaptureList();
        if (locationCheck()){
            if (actCategory.getText().toString().equalsIgnoreCase("")){
                checkForEmptyAutocompleteFields();
            }else{
                checkForEmptyAutocompleteFields();
                if (get_added_capture.contains(IMAGE_CAPTURE_1) && get_added_capture.contains(IMAGE_CAPTURE_2) &&
                        get_added_capture.contains(IMAGE_CAPTURE_3)){
                    if (ContextCompat.checkSelfPermission(view.getContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                        CAMERA_REQUEST = CAMERA_REQUEST_4;
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_4);
                    }
                }else{
                    Toast.makeText(getActivity(), "Please capture Picture 1, 2, and 3 before taking this picture", Toast.LENGTH_SHORT).show();

                }
            }
        }else{
            showLocationError();
        }
    }

    @OnClick(R.id.btn_cancel_pic_4)
    void setBtn_cancel_pic_4(){
        cancelCapturedPicture(IMAGE_CAPTURE_4,btn_capture_picture_4,btn_cancel_pic_4);
        resetTextViewText(tv_picture_name_4, Objects.requireNonNull(getActivity()).getResources().getString(R.string.pws_log_picture4));
    }

    @OnClick(R.id.btn_map_pws)
    void setBtn_map_pws(){
        ProgressDialog pd1 = new ProgressDialog(getActivity());
        pd1.setTitle("Setting up your phone for mapping");
        pd1.setMessage("Please wait...");
        pd1.show();
        pd1.setCancelable(false);
        new CountDownTimer(10000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                pd1.dismiss();
                startActivityForResult(new Intent(getActivity(), MappingActivity.class),MAPPING_ACTIVITY_REQUEST);
            }
        }.start();
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
        if (requestCode == CAMERA_REQUEST_1 && resultCode == Activity.RESULT_OK) {
            photo1 = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            /*Bitmap bitmap = Bitmap.createScaledBitmap(Objects.requireNonNull(photo1), 400, 400, true);
            activityImage.setImageBitmap(bitmap);*/
            addCapturedPicture(IMAGE_CAPTURE_1, tv_picture_name_1, btn_capture_picture_1, btn_cancel_pic_1);
        }else if (requestCode == CAMERA_REQUEST_2 && resultCode == Activity.RESULT_OK) {
            photo2 = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            addCapturedPicture(IMAGE_CAPTURE_2, tv_picture_name_2, btn_capture_picture_2, btn_cancel_pic_2);
        }else if (requestCode == CAMERA_REQUEST_3 && resultCode == Activity.RESULT_OK) {
            photo3 = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            addCapturedPicture(IMAGE_CAPTURE_3, tv_picture_name_3, btn_capture_picture_3, btn_cancel_pic_3);
        }else if (requestCode == CAMERA_REQUEST_4 && resultCode == Activity.RESULT_OK) {
            photo4 = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            addCapturedPicture(IMAGE_CAPTURE_4, tv_picture_name_4, btn_capture_picture_4, btn_cancel_pic_4);
        }else if (requestCode == MAPPING_ACTIVITY_REQUEST && resultCode == Activity.RESULT_OK) {
            pwsMapModel = Objects.requireNonNull(data.getExtras()).getParcelable("RESULT");
            if (pwsMapModel != null) {
                resetTextViewText(tv_pws_map,pwsMapModel.getFieldSize()+" Ha");
            }
        }else{
            Toast.makeText(getActivity(), "What are you trying to do", Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.btnSubmitPWS)
    public  void next(View view){
        if (validateFieldsInfo(actCategory,edtMIKDescription, photo1, photo2,photo3,photo4,pwsMapModel) == 0){
            checkForEmptyAutocompleteFields();
            //checkForEmptyEditText();
            photoCheck();
        }
        else{
            checkForEmptyAutocompleteFields();
            //checkForEmptyEditText();
            locationGetter = GPSController.initialiseLocationListener(Objects.requireNonNull(getActivity()));
            double latitude = locationGetter.getLatitude();
            double longitude = locationGetter.getLongitude();
            pwsFieldListRecyclerModel = sharedPrefs.getKeyPWSFieldModel();
            double min_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lat());
            double max_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lat());
            double min_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lng());
            double max_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lng());
            double mid_lat = (max_lat+min_lat)/2.0;
            double mid_lng = (max_lng+min_lng)/2.0;

            double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
            double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

            if (locationDistance <= allowedDistance){
                showBottomSheet();
            }else{
                locationMismatchedDialog(latitude,longitude,min_lat,max_lat,min_lng,max_lng,
                        getActivity(),pwsFieldListRecyclerModel.getUnique_field_id(),
                        Objects.requireNonNull(getActivity()).getResources().getString(R.string.wrong_location));
            }
        }
    }

    public void fillCategoryListSpinner(AutoCompleteTextView autoCompleteTextView, Context context) {
        List<String> whole_category_list = appDatabase.pwsCategoryListDao().getAllPWSCategories(sharedPrefs.getStaffRole().toLowerCase());
        ArrayAdapter<String> hg_adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, whole_category_list);
        spinnerViewController(autoCompleteTextView,hg_adapter);
    }

    public void spinnerViewController(AutoCompleteTextView autoCompleteTextView, ArrayAdapter<String> arrayAdapter) {
        autoCompleteTextView.setAdapter(arrayAdapter);
    }

    public int validateFieldsInfo(AutoCompleteTextView actCategory, TextInputEditText edtMIKDescription,
                                  Bitmap photo1, Bitmap photo2, Bitmap photo3, Bitmap photo4,
                                  PWSFieldListRecyclerModel.PWSMapModel pwsMapModel){

        // Checks if the category field is empty
        if(Objects.requireNonNull(actCategory.getText()).toString().matches("")) {
            return 0;
        }

        // Checks if the photos are empty field is empty
        else if(photo1 == null) {
            return 0;
        }

        else if(photo2 == null) {
            return 0;
        }

        else if(photo3 == null) {
            return 0;
        }

        else if(photo4 == null) {
            return 0;
        }

        else if(pwsMapModel.getFieldSize() == null || pwsMapModel.getFieldSize().equalsIgnoreCase("")) {
            return 0;
        }

        /*else if(Objects.requireNonNull(edtMIKDescription.getText()).toString().matches("")) {
            return 0;
        }*/

        //all checks are passed
        else{
            return 1;
        }
    }

    void checkForEmptyAutocompleteFields(){
        checkIfAutocompleteEmpty(actCategory,edlCategory,this.getResources().getString(R.string.error_message_pws_category));
    }

    public void checkIfAutocompleteEmpty(AutoCompleteTextView autoCompleteTextView, TextInputLayout textInputLayout, String error_message) {
        String textEntered = Objects.requireNonNull(autoCompleteTextView.getText()).toString();
        if (textEntered.equalsIgnoreCase("")){
            setErrorOfTextInputLayout(textInputLayout, error_message);
            Toast.makeText(getActivity(), error_message, Toast.LENGTH_SHORT).show();
            showDialogForLocked(error_message,getActivity());
        }else{
            removeErrorFromTextInputLayout(textInputLayout);
        }
    }

    void checkForEmptyEditText(){
        checkIfEditTextEmpty(edtMIKDescription,edlMIKDescription,this.getResources().getString(R.string.error_message_pws_description));
    }

    public void checkIfEditTextEmpty(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String error_message) {
        String textEntered = Objects.requireNonNull(textInputEditText.getText()).toString();
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

    void dismissAndRefresh(){
        clearPictureCaptured1();
        dismiss();
        ((PCPWSHomePage) Objects.requireNonNull(getActivity())).myMethod();
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void locationMismatchedDialog(double latitude,
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

    void savePictureAndClose(PWSFieldListRecyclerModel pwsFieldListRecyclerModel, String category,
                             Bitmap photo1, Bitmap photo2, Bitmap photo3, Bitmap photo4,
                             PWSFieldListRecyclerModel.PWSMapModel pwsMapModel, String description){

        final String pws_id = sharedPrefs.getKeyPwsId();

        String result1 = saveToSdCard(photo1, pws_id + "_pc_" + IMAGE_CAPTURE_1);
        String result2 = saveToSdCard(photo2, pws_id + "_pc_" + IMAGE_CAPTURE_2);
        String result3 = saveToSdCard(photo3, pws_id + "_pc_" + IMAGE_CAPTURE_3);
        String result4 = saveToSdCard(photo4, pws_id + "_pc_" + IMAGE_CAPTURE_4);
        if (result1.equalsIgnoreCase("success") &&
                result2.equalsIgnoreCase("success") &&
                result3.equalsIgnoreCase("success") &&
                result4.equalsIgnoreCase("success")){
            updateActivity(pwsFieldListRecyclerModel, pws_id, pwsMapModel, category, description);
            Toast.makeText(getActivity(), "Picture Saved For Review", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Picture Not Saved", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateActivity(PWSFieldListRecyclerModel pwsFieldListRecyclerModel, String pws_id,
                                PWSFieldListRecyclerModel.PWSMapModel pwsMapModel, String category,
                                String description){

        String unique_member_id = appDatabase.fieldsDao().getUniqueMemberID(pwsFieldListRecyclerModel.getUnique_field_id());

        appDatabase.pcpwsActivitiesFlagDao().insert(new PCPWSActivitiesFlag(
                pws_id,
                pwsFieldListRecyclerModel.getUnique_field_id(),
                pwsFieldListRecyclerModel.getIk_number(),
                category,
                pwsMapModel.getFieldSize(),
                pwsMapModel.getLat_longs(),
                pwsMapModel.getMin_lat(),
                pwsMapModel.getMax_lat(),
                pwsMapModel.getMin_long(),
                pwsMapModel.getMax_long(),
                pwsMapModel.getLatitude(),
                pwsMapModel.getLongitude(),
                sharedPrefs.getStaffID(),
                "0",
                "0",
                getDate("spread"),
                description,
                "0",
                pwsFieldListRecyclerModel.getMember_name(),
                getDeviceID(),
                unique_member_id)
        );

        appDatabase.logsDao().insert(new Logs(pws_id,sharedPrefs.getStaffID(),
                "review_pws_" + category,getDate("normal"),sharedPrefs.getStaffRole(),
                pwsMapModel.getLatitude(), pwsMapModel.getLongitude(),getDeviceID(),"0",
                pwsFieldListRecyclerModel.getIk_number(),pwsFieldListRecyclerModel.getCrop_type()));

        dismissAndRefresh();
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

    String saveToSdCard(Bitmap bitmap, String filename) {
        String stored = null;
        File ChkImgDirectory;
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            ChkImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.PWS_ACTIVITY_PICTURE_LOCATION);

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

    void addToPictureCaptured(String picture){
        Set<String> oldPicList, newPicList;
        oldPicList = sharedPrefs.getKeyPwsCaptureList();
        newPicList = setPortfolioMethods.addStaff(oldPicList,picture);
        sharedPrefs.setKeyPWSCaptureList(newPicList);
    }

    void removeFromPictureCaptured(String picture){
        Set<String> oldPicList, newPicList;
        oldPicList = sharedPrefs.getKeyPwsCaptureList();
        newPicList = setPortfolioMethods.removeStaff(oldPicList,picture);
        sharedPrefs.setKeyPWSCaptureList(newPicList);
        System.out.println(newPicList);
    }

    void clearPictureCaptured(){
        Set<String> get_added_capture = sharedPrefs.getKeyPwsCaptureList();
        if (!get_added_capture.isEmpty()){
            Toast.makeText(getActivity(), "Parameter change requires image recapture.", Toast.LENGTH_SHORT).show();
            get_added_capture.clear();
            sharedPrefs.setKeyPWSCaptureList(get_added_capture);
            resetAllImageName();
        }
        photo1 = null;
        photo2 = null;
        photo3 = null;
        photo4 = null;
        pwsMapModel = new PWSFieldListRecyclerModel.PWSMapModel();
        showAllCapture();
    }

    void clearPictureCaptured1(){
        Set<String> get_added_capture = sharedPrefs.getKeyPwsCaptureList();
        if (!get_added_capture.isEmpty()){
            get_added_capture.clear();
            sharedPrefs.setKeyPWSCaptureList(get_added_capture);
            resetAllImageName();
        }
        photo1 = null;
        photo2 = null;
        photo3 = null;
        photo4 = null;
        pwsMapModel = new PWSFieldListRecyclerModel.PWSMapModel();
        showAllCapture();
    }

    public void resetTextViewText(MaterialTextView textView, String text) {
        textView.setText(text);
    }

    void showCapture(MaterialButton capture, MaterialButton cancel){
        capture.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
    }

    void hideCapture(MaterialButton capture, MaterialButton cancel){
        capture.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
    }

    void cancelCapturedPicture(String photo, MaterialButton capture, MaterialButton cancel){
        removeFromPictureCaptured(photo);
        showCapture(capture,cancel);
    }

    void addCapturedPicture(String photo, MaterialTextView tv_picture_name, MaterialButton capture, MaterialButton cancel){
        addToPictureCaptured(photo);
        resetTextViewText(tv_picture_name,actCategory.getText().toString()+"_"+photo);
        hideCapture(capture,cancel);
    }

    void resetAllImageName(){
        resetTextViewText(tv_picture_name_1, Objects.requireNonNull(getActivity()).getResources().getString(R.string.pws_log_picture1));
        resetTextViewText(tv_picture_name_2, Objects.requireNonNull(getActivity()).getResources().getString(R.string.pws_log_picture2));
        resetTextViewText(tv_picture_name_3, Objects.requireNonNull(getActivity()).getResources().getString(R.string.pws_log_picture3));
        resetTextViewText(tv_picture_name_4, Objects.requireNonNull(getActivity()).getResources().getString(R.string.pws_log_picture4));
        resetTextViewText(tv_pws_map, Objects.requireNonNull(getActivity()).getResources().getString(R.string.pws_mapped_area));
    }

    void photoCheck(){
        if (photo1 == null){
            Toast.makeText(getActivity(), "Please capture Picture 1", Toast.LENGTH_SHORT).show();
        }
        if (photo2 == null){
            Toast.makeText(getActivity(), "Please capture Picture 2", Toast.LENGTH_SHORT).show();
        }
        if (photo3 == null){
            Toast.makeText(getActivity(), "Please capture Picture 3", Toast.LENGTH_SHORT).show();
        }
        if (photo4 == null){
            Toast.makeText(getActivity(), "Please capture Picture 4", Toast.LENGTH_SHORT).show();
        }
        if (pwsMapModel == null){
            Toast.makeText(getActivity(), "Please map area affected with poor weather", Toast.LENGTH_SHORT).show();
        }

        if (photo1 == null || photo2 == null || photo3 == null || photo4 == null){
            if (pwsMapModel.getFieldSize() == null || pwsMapModel.getFieldSize().equalsIgnoreCase("")){
                showDialogForLocked("Please map affected area and take required pictures", getActivity());
            }else{
                showDialogForLocked("Please take required pictures", getActivity());
            }
        }else{
            if (pwsMapModel.getFieldSize() == null || pwsMapModel.getFieldSize().equalsIgnoreCase("")){
                showDialogForLocked("Please map affected area", getActivity());
            }
        }
    }

    void showBottomSheet(){
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
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

    @OnClick(R.id.btnNo)
    public  void setBtnNo(){
        showBottomSheet();
    }

    @OnClick(R.id.btnYes)
    public  void setBtnYes(){
        savePictureAndClose(pwsFieldListRecyclerModel, actCategory.getText().toString(),
                photo1, photo2, photo3, photo4, pwsMapModel, Objects.requireNonNull(edtMIKDescription.getText()).toString());
    }

    private boolean locationCheck(){
        locationGetter = GPSController.initialiseLocationListener(Objects.requireNonNull(getActivity()));
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();
        pwsFieldListRecyclerModel = sharedPrefs.getKeyPWSFieldModel();
        double min_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lat());
        double max_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lat());
        double min_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lng());
        double max_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lng());
        double mid_lat = (max_lat+min_lat)/2.0;
        double mid_lng = (max_lng+min_lng)/2.0;

        double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
        double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

        return locationDistance <= allowedDistance;
    }

    void showLocationError(){
        locationGetter = GPSController.initialiseLocationListener(Objects.requireNonNull(getActivity()));
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();
        pwsFieldListRecyclerModel = sharedPrefs.getKeyPWSFieldModel();
        double min_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lat());
        double max_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lat());
        double min_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lng());
        double max_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lng());

        locationMismatchedDialog(latitude,longitude,min_lat,max_lat,min_lng,max_lng,
                getActivity(),pwsFieldListRecyclerModel.getUnique_field_id(),
                Objects.requireNonNull(getActivity()).getResources().getString(R.string.wrong_location));
    }
    private void showDialogForLocked(String s, Context context) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForLockedLogging(builder,s,context);
    }

    private void showDialogForLockedLogging(MaterialAlertDialogBuilder builder, String s, Context context) {
        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setTitle(context.getResources().getString(R.string.oops))
                .setMessage(s)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                }).setCancelable(false)
                .show();
    }

}

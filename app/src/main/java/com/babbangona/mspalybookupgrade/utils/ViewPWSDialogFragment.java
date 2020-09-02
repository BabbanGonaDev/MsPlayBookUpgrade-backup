package com.babbangona.mspalybookupgrade.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ViewPWSDialogFragment extends DialogFragment {

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
    TextInputEditText actCategory;

    @BindView(R.id.edlCategory)
    TextInputLayout edlCategory;

    @BindView(R.id.tv_picture_name_1)
    MaterialTextView tv_picture_name_1;

    @BindView(R.id.tv_picture_name_2)
    MaterialTextView tv_picture_name_2;

    @BindView(R.id.tv_picture_name_3)
    MaterialTextView tv_picture_name_3;

    @BindView(R.id.tv_picture_name_4)
    MaterialTextView tv_picture_name_4;

    @BindView(R.id.tv_pws_map)
    MaterialTextView tv_pws_map;

    @BindView(R.id.edlMIKDescription)
    TextInputLayout edlMIKDescription;

    @BindView(R.id.edtMIKDescription)
    TextInputEditText edtMIKDescription;

    @BindView(R.id.edlPCDescription)
    TextInputLayout edlPCDescription;

    @BindView(R.id.edtPCDescription)
    TextInputEditText edtPCDescription;

    private SharedPrefs sharedPrefs;

    private SetPortfolioMethods setPortfolioMethods;

    private AppDatabase appDatabase;

    PWSFieldListRecyclerModel pwsFieldListRecyclerModel;

    PWSFieldListRecyclerModel.PWSMapModel pwsMapModel;

    String role, pws_id;
    PWSFieldListRecyclerModel.PWSDetails pwsDetails;

    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View fragmentLayout = inflater.inflate(R.layout.view_pws_fragment, container, false);
        ButterKnife.bind(this, fragmentLayout);
        unbinder = ButterKnife.bind(this, fragmentLayout);
        setPortfolioMethods = new SetPortfolioMethods();
        appDatabase = AppDatabase.getInstance(getActivity());
        sharedPrefs = new SharedPrefs(getActivity());
        pwsFieldListRecyclerModel = new PWSFieldListRecyclerModel();
        pwsMapModel = new PWSFieldListRecyclerModel.PWSMapModel();
        pwsDetails = new PWSFieldListRecyclerModel.PWSDetails();
        pwsFieldListRecyclerModel = sharedPrefs.getKeyPWSFieldModel();
        ((AppCompatActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar_hg_fragment);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("PWS Claim Details");
        GPSController.initialiseLocationListener(getActivity());
        sharedPrefs.setKeyImageCaptureFlag("0");

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

        role = sharedPrefs.getKeyPwsViewRole();
        pws_id = sharedPrefs.getKeyPwsId();

        resetTextViewText(tv_picture_name_1,pws_id+"_"+role+"_1.png");
        resetTextViewText(tv_picture_name_2,pws_id+"_"+role+"_2.png");
        resetTextViewText(tv_picture_name_3,pws_id+"_"+role+"_3.png");
        resetTextViewText(tv_picture_name_4,pws_id+"_"+role+"_4.png");

        pwsDetails = appDatabase.pwsActivitiesFlagDao().getPWSDetails(pws_id);

        actCategory.setText(pwsDetails.getCategory());

        if (role.equalsIgnoreCase("mik")){
            resetTextViewText(tv_pws_map,"Claim mapped area: "+pwsDetails.getMik_field_size());
        }else{
            resetTextViewText(tv_pws_map,"Claim mapped area: "+pwsDetails.getPc_field_size());
        }

        edtMIKDescription.setText(pwsDetails.getMik_description());
        edtPCDescription.setText(pwsDetails.getPc_description());

        return fragmentLayout;
    }

    @OnClick(R.id.tv_picture_name_1)
    void setTv_picture_name_1(){
        showCapturePictureDialogStart(getActivity(),pws_id+"_"+role+"_1.png");
    }

    @OnClick(R.id.tv_picture_name_2)
    void setTv_picture_name_2(){
        showCapturePictureDialogStart(getActivity(),pws_id+"_"+role+"_2.png");
    }

    @OnClick(R.id.tv_picture_name_3)
    void setTv_picture_name_3(){
        showCapturePictureDialogStart(getActivity(),pws_id+"_"+role+"_3.png");
    }

    @OnClick(R.id.tv_picture_name_4)
    void setTv_picture_name_4(){
        showCapturePictureDialogStart(getActivity(),pws_id+"_"+role+"_4.png");
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

    void dismissAndRefresh(){
        dismiss();
        if (Objects.requireNonNull(getActivity()).getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    private void showCapturePictureDialogStart(Context context, String image){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showCapturePictureDialogStartBody(builder, context, image);
    }

    private void showCapturePictureDialogStartBody(MaterialAlertDialogBuilder builder, Context context, String image) {

        LinearLayout layout = new LinearLayout(getActivity());

        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
        layoutParams.gravity= Gravity.CENTER;

        final ImageView imageView = new ImageView(getActivity());
        imageView.setLayoutParams(layoutParams);

        Bitmap image_bitmap;
        File imgFile;

        File ImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.PWS_ACTIVITY_PICTURE_LOCATION);
        String image_name = File.separator + image;
        imgFile = new File(ImgDirectory.getAbsoluteFile(),image_name);
        if(imgFile.exists()){
            image_bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }else{
            image_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_logo);
        }
        imageView.setImageBitmap(image_bitmap);

        layout.addView(imageView);

        builder.setTitle(image)
                .setView(layout)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                }).setCancelable(false)
                .show();
    }

    public void resetTextViewText(MaterialTextView textView, String text) {
        textView.setText(text);
    }

}

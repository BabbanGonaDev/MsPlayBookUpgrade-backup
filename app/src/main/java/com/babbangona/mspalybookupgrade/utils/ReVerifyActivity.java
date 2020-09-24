package com.babbangona.mspalybookupgrade.utils;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.MemberListRecycler.MemberListRecyclerModel;
import com.babbangona.mspalybookupgrade.ThreshingViews.FieldList;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReVerifyActivity extends AppCompatActivity {

    private SharedPrefs sharedPrefs;

    @BindView(R.id.btnCancel)
    MaterialButton btnCancel;

    @BindView(R.id.btnTryAgain)
    MaterialButton btnTryAgain;

    @BindView(R.id.btScanFace)
    MaterialButton btScanFace;

    @BindView(R.id.btnScanSuccessful)
    MaterialButton btnScanSuccessful;

    @BindView(R.id.scanned_image_iv)
    ImageView scanned_image_iv;

    @BindView(R.id.linear_layout_scan_success)
    LinearLayout linear_layout_scan_success;

    @BindView(R.id.linear_layout_scan_failed)
    LinearLayout linear_layout_scan_failed;

    @BindView(R.id.tv_question)
    TextView tv_question;

    @BindView(R.id.linear_layout_buttons)
    LinearLayout linear_layout_buttons;

    MemberListRecyclerModel.TemplateModel templateModel;

    private AppDatabase appDatabase;
    int count;
    int state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.re_verification_activity);
        ButterKnife.bind(this);
        sharedPrefs = new SharedPrefs(ReVerifyActivity.this);
        appDatabase = AppDatabase.getInstance(this);
        templateModel = new MemberListRecyclerModel.TemplateModel();
        count = 0;
        state = 0;

        setMemberImage(scanned_image_iv,sharedPrefs.getKeyThreshingUniqueMemberId());
        showLogic(state);
    }

    @OnClick(R.id.btScanFace)
    public void setBtScanFace(){
        Intent intent = new Intent(this, VerifyActivity.class);
        startActivityForResult(intent,419);
    }

    @OnClick(R.id.btnTryAgain)
    public void setBtnTryAgain() {
        if (count < 2) {
            Intent i = new Intent(getApplicationContext(), VerifyActivity.class);
            startActivityForResult(i,419);
        } else {
            Intent i = new Intent(getApplicationContext(), CaptureActivity.class);
            startActivityForResult(i,519);
        }
    }

    @OnClick(R.id.btnCancel)
    public void setBtnCancel() {
        onBackPressed();
    }

    @OnClick(R.id.btnScanSuccessful)
    public void setBtnScanSuccessful() {
        //go to fields activity
        startActivity(new Intent(ReVerifyActivity.this, FieldList.class));
    }



    /**
     * This method handles whatever happens after capture or authentication of template
     * @param requestCode might be 419 or 519 either for validation or registration respectively
     * @param resultCode 0 or 1 depending on whether success or failure of actions from request code
     * @param data Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int state = 0;
        if (requestCode == 519) {
            if (resultCode == Activity.RESULT_OK) {
                templateModel = Objects.requireNonNull(data.getExtras()).getParcelable("RESULT");
                if (templateModel != null) {
                    if (templateModel.getResult().equalsIgnoreCase("1")) {
                        //move to next activity
                        //save template to shared preference
                        setCapturedImage(scanned_image_iv, templateModel.getImage_person_large());
                        sharedPrefs.setKeyThreshingRecaptureFlag("0");
                        sharedPrefs.setKeyThreshingTemplate(templateModel.getTemplate());
                        sharedPrefs.setKeyThreshingPicture(templateModel.getImage_person_small());
                        state = 4;
                    } else {
                        //either remain here or move away from here.
                        state = 3;
                    }
                } else {
                    state = 3;
                }
            } else {
                state = 3;
            }

        } else if (requestCode == 419) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getIntExtra("RESULT", 0) == 1) {
                    //member passes authentication
                    state = 2;
                    sharedPrefs.setKeyThreshingRecaptureFlag("0");
                    sharedPrefs.setKeyThreshingTemplate("XXX");
                    sharedPrefs.setKeyThreshingPicture("XXX");
                } else {
                    //member_fails_authentication
                    count += 1;
                    if (count < 2) {
                        state = 1;
                    } else {
                        state = 3;
                    }
                }
            } else {
                count += 1;
                if (count < 2) {
                    count += 1;
                    state = 1;
                } else {
                    state = 3;
                }
            }

        }

        showLogic(state);
        super.onActivityResult(requestCode, resultCode, data);
    }

    void setCapturedImage(ImageView scanned_image_iv, String large_picture){
        this.runOnUiThread(()->{
            if (large_picture != null && !large_picture.equalsIgnoreCase("")){
                Bitmap myBitmap = getBitmap(large_picture);
                scanned_image_iv.setImageBitmap(myBitmap);
            }
        });

    }

    private Bitmap getBitmap(String member_picture_byte_array){
        byte[] imageAsBytes = new byte[0];
        if (member_picture_byte_array != null) {
            imageAsBytes = Base64.decode(member_picture_byte_array.getBytes(), Base64.DEFAULT);
        }
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    void showLogic(int state){
        switch(state){
            case 0:
                showView(btScanFace);
                hideView(linear_layout_scan_failed);
                hideView(linear_layout_scan_success);
                hideView(tv_question);
                hideView(linear_layout_buttons);
                break;
            case 1:
                hideView(btScanFace);
                showView(linear_layout_scan_failed);
                hideView(linear_layout_scan_success);
                showView(tv_question);
                showView(linear_layout_buttons);
                break;
            case 2:
                hideView(btScanFace);
                hideView(linear_layout_scan_failed);
                showView(linear_layout_scan_success);
                hideView(tv_question);
                hideView(linear_layout_buttons);
                break;
            case 3:
                hideView(btScanFace);
                showView(linear_layout_scan_failed);
                hideView(linear_layout_scan_success);
                hideView(tv_question);
                showView(linear_layout_buttons);
                btnTryAgain.setText(R.string.recapture);
                break;
            case 4:
                hideView(btScanFace);
                hideView(linear_layout_scan_failed);
                showView(linear_layout_scan_success);
                hideView(tv_question);
                hideView(linear_layout_buttons);
                btnScanSuccessful.setText(R.string.face_capture_success);
                break;
            default:
                break;
        }
    }

    public void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    public void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    void setMemberImage(ImageView scanned_image_iv, String unique_id){

        File ImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.MS_PLAYBOOK_INPUT_PICTURE_LOCATION);
        String image_name = File.separator + unique_id + "_thumb.jpg";
        File imgFile = new File(ImgDirectory.getAbsoluteFile(),image_name);

        if(imgFile.exists()){

            this.runOnUiThread(()->{
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                scanned_image_iv.setImageBitmap(myBitmap);
            });

        }else{
            this.runOnUiThread(()->{
                scanned_image_iv.setImageResource(R.drawable.bg_logo);
            });
        }


    }

    @Override
    public void onBackPressed() {
        finish();
        sharedPrefs.setKeyThreshingRecaptureFlag("0");
        sharedPrefs.setKeyThreshingTemplate("XXX");
        sharedPrefs.setKeyThreshingPicture("XXX");
        super.onBackPressed();
    }
}

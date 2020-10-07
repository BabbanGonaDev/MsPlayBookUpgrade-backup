package com.babbangona.mspalybookupgrade.RecyclerAdapters.MemberListRecycler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.ComingSoon;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.ThreshingViews.FieldList;
import com.babbangona.mspalybookupgrade.ThreshingViews.ThreshingActivity;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.babbangona.mspalybookupgrade.utils.ReVerifyActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemberListRecyclerViewAdapter extends PagedListAdapter<MemberListRecyclerModel, MemberListRecyclerViewAdapter.MyViewHolder> {

    private Context mCtx;
    SharedPrefs sharedPrefs;
    AppDatabase appDatabase;

    public MemberListRecyclerViewAdapter(Context context){
        super(USER_DIFF);
        this.mCtx = context;
        sharedPrefs = new SharedPrefs(mCtx);
        appDatabase = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_list_card_landscape, parent, false);
        return new MyViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        MemberListRecyclerModel memberListRecyclerModel = getItem(position);
        if (memberListRecyclerModel != null) {
            holder.nowBind(memberListRecyclerModel);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.leader_image)
        ImageView leader_image;

        @BindView(R.id.tv_member_name)
        TextView tv_member_name;

        @BindView(R.id.tv_member_r_id)
        TextView tv_member_r_id;

        @BindView(R.id.tv_village)
        TextView tv_village;

        @BindView(R.id.tv_ik_number)
        TextView tv_ik_number;

        @BindView(R.id.tv_role)
        TextView tv_role;

        @BindView(R.id.assignment_flag)
        View assignment_flag;

        @BindView(R.id.card_container)
        LinearLayout card_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void nowBind(MemberListRecyclerModel memberListRecyclerModel){
            setTextController(tv_member_name, memberListRecyclerModel.getMember_name());
            setTextController(tv_member_r_id, memberListRecyclerModel.getMember_r_id());
            setTextController(tv_role, memberListRecyclerModel.getRole());
            setTextController(tv_village, memberListRecyclerModel.getVillage());
            setTextController(tv_ik_number, memberListRecyclerModel.getIk_number());
            setLeader_image(leader_image,memberListRecyclerModel.getUnique_member_id(),mCtx);
            setAssignment_flag(memberListRecyclerModel.getStaff_id(), mCtx);
            card_container.setOnClickListener((view)->submit(memberListRecyclerModel));

        }

        void setTextController(TextView textView, String text) {
            textView.setText(text);
        }

        void setLeader_image(ImageView iv_picture, String unique_id, Context context){

            File ImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.MS_PLAYBOOK_INPUT_PICTURE_LOCATION);
            String image_name = File.separator + unique_id + "_thumb.jpg";
            File imgFile = new File(ImgDirectory.getAbsoluteFile(),image_name);

            if(imgFile.exists()){

                ((Activity)mCtx).runOnUiThread(() -> {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    iv_picture.setImageBitmap(myBitmap);
                });

            }else{
                iv_picture.setImageResource(R.drawable.bg_logo);
            }
        }

        void setAssignment_flag(String staff_id, Context context){
            if (staff_id != null) {
                if (staff_id.equalsIgnoreCase(sharedPrefs.getStaffID())){
                    assignment_flag.setBackground(ContextCompat.getDrawable(context,R.drawable.assignment_green));
                }else{
                    assignment_flag.setBackground(ContextCompat.getDrawable(context,R.drawable.assignment_red));
                }
            }
        }
    }

    private void showDialogForThresher(Context context, MemberListRecyclerModel memberListRecyclerModel) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForThresherBody(builder, context, memberListRecyclerModel);
    }

    private void showDialogForThresherBody(MaterialAlertDialogBuilder builder, Context context, MemberListRecyclerModel memberListRecyclerModel) {

        builder.setTitle(context.getResources().getString(R.string.thresher_title))
                .setIcon(context.getResources().getDrawable(R.drawable.ic_smiley_face))
                .setMessage(context.getResources().getString(R.string.thresher_question))
                .setPositiveButton(context.getResources().getString(R.string.thresher_bg), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    sharedPrefs.setKeyThresher("BG");
                    sharedPrefs.setKeyThreshingUniqueMemberId(memberListRecyclerModel.getUnique_member_id());
                    mCtx.startActivity(new Intent(mCtx, FieldList.class));
                })
                .setNegativeButton(context.getResources().getString(R.string.thresher_self), (dialog, which) -> {
                    dialog.dismiss();
                    sharedPrefs.setKeyThresher("Self");

                    confirmStatus(builder,context,memberListRecyclerModel);


                    /*
                    sharedPrefs.setKeyThreshingUniqueMemberId(memberListRecyclerModel.getUnique_member_id());
                    if (getLuxandFlag().equalsIgnoreCase("0")){
                        Intent intent = new Intent (mCtx, ReVerifyActivity.class);
                        mCtx.startActivity(intent);
                    }else{
                        mCtx.startActivity(new Intent(mCtx, FieldList.class));
                    }*/

                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    //this function asks the bgt if they are
    void confirmStatus(MaterialAlertDialogBuilder builder, Context context, MemberListRecyclerModel memberListRecyclerModel){
        GPSController.LocationGetter locationGetter;
        locationGetter = GPSController.initialiseLocationListener(context);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();

        builder.setTitle(context.getResources().getString(R.string.thresher_title))
                .setIcon(context.getResources().getDrawable(R.drawable.ic_smiley_face))
                .setMessage(context.getResources().getString(R.string.thresher_question2))
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    dialog.dismiss();



                   List<String> fields  =  new ArrayList<>();
                   fields = AppDatabase.getInstance(mCtx).fieldsDao().getFieldIDBYMEmber(memberListRecyclerModel.getUnique_member_id());

                   for (int i  = 0; i <fields.size(); i++) {


                       appDatabase.scheduleThreshingActivitiesFlagDao().insert(new ScheduledThreshingActivitiesFlag(
                                       fields.get(i),
                                       "Self",
                                       "0",
                                       sharedPrefs.getKeyThreshingTemplate(),
                                       "XXX",
                                       "XXX",
                                       "XXX",
                                       getDeviceID(),
                                       BuildConfig.VERSION_NAME,
                                       latitude+"",
                                       longitude+"",
                                       sharedPrefs.getStaffID(),
                                       getDate("spread"),
                                       "0",
                                       "XXX",
                                       sharedPrefs.getKeyThreshingIkNumber(),
                                       "0",
                                       "1",
                                       "0"
                               )
                       );

                       appDatabase.logsDao().insert(new Logs(fields.get(i), sharedPrefs.getStaffID(),
                               "Schedule threshing", getDate("normal"), sharedPrefs.getStaffRole(),
                               latitude+"", longitude+"", getDeviceID(), "0",
                               memberListRecyclerModel.getIk_number(), "Maize"));
                   }

                   mCtx.startActivity(new Intent(mCtx, ThreshingActivity.class));
                })
                .setNegativeButton(context.getResources().getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();


                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();

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

    void submit(MemberListRecyclerModel memberListRecyclerModel){

        if (memberListRecyclerModel.getStaff_id() != null) {
            if (memberListRecyclerModel.getStaff_id().equalsIgnoreCase(sharedPrefs.getStaffID())){
                String route = sharedPrefs.getKeyThreshingActivityRoute();
                switch (route){
                    case "1":
                        showDialogForThresher(mCtx,memberListRecyclerModel);
                        break;
                    case "2":
                    case "3":
                    case "4":
                        sharedPrefs.setKeyThreshingUniqueMemberId(memberListRecyclerModel.getUnique_member_id());
                        mCtx.startActivity(new Intent (mCtx, FieldList.class));
                        break;
                    case "5":
                        sharedPrefs.setKeyThreshingUniqueMemberId(memberListRecyclerModel.getUnique_member_id());
                        ((Activity)mCtx).finish();
                        mCtx.startActivity(new Intent (mCtx, FieldList.class));
                        break;
                    default:
                        sharedPrefs.setKeyThreshingUniqueMemberId(memberListRecyclerModel.getUnique_member_id());
                        mCtx.startActivity(new Intent (mCtx, ComingSoon.class));
                        break;
                }
            }else{
                //You cannot schedule for this guy
                showScheduleProblemStart(mCtx.getResources().getString(R.string.wrong_member_schedule),mCtx);
            }
        } else {
            showScheduleProblemStart(mCtx.getResources().getString(R.string.wrong_member_schedule),mCtx);
            /*String route = sharedPrefs.getKeyThreshingActivityRoute();
            switch (route){
                case "1":
                    showDialogForExit(mCtx,memberListRecyclerModel);
                    break;
                case "2":
                case "3":
                case "4":
                    sharedPrefs.setKeyThreshingUniqueMemberId(memberListRecyclerModel.getUnique_member_id());
                    mCtx.startActivity(new Intent (mCtx, FieldList.class));
                    break;
                default:
                    sharedPrefs.setKeyThreshingUniqueMemberId(memberListRecyclerModel.getUnique_member_id());
                    mCtx.startActivity(new Intent (mCtx, ComingSoon.class));
                    break;
            }*/
        }
    }

    private void showScheduleProblemStart(String message, Context context){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showScheduleProblemBody(builder, message, context);
    }

    private void showScheduleProblemBody(MaterialAlertDialogBuilder builder, String message,
                                     Context context) {

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setTitle(context.getResources().getString(R.string.oops))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private static DiffUtil.ItemCallback<MemberListRecyclerModel> USER_DIFF =
            new DiffUtil.ItemCallback<MemberListRecyclerModel>() {
                // MSB details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(MemberListRecyclerModel oldMemberListRecyclerModel,
                                               MemberListRecyclerModel newMemberListRecyclerModel) {
                    return oldMemberListRecyclerModel.getUnique_member_id().equals(newMemberListRecyclerModel.getUnique_member_id());
                }

                @Override
                public boolean areContentsTheSame(MemberListRecyclerModel oldMemberListRecyclerModel,
                                                  MemberListRecyclerModel newMemberListRecyclerModel) {
                    return oldMemberListRecyclerModel.getUnique_member_id().equals(newMemberListRecyclerModel.getUnique_member_id());
                }
            };


    private String getDeviceID(){
        String device_id;
        TelephonyManager tm = (TelephonyManager) Objects.requireNonNull(mCtx).getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(mCtx, Manifest.permission. READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED){
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

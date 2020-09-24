package com.babbangona.mspalybookupgrade.RecyclerAdapters.MemberListRecycler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.ComingSoon;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.ThreshingViews.FieldList;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.ReVerifyActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MemberListRecyclerViewAdapter extends PagedListAdapter<MemberListRecyclerModel, MemberListRecyclerViewAdapter.MyViewHolder> {

    private Context mCtx;
    SharedPrefs sharedPrefs;

    public MemberListRecyclerViewAdapter(Context context){
        super(USER_DIFF);
        this.mCtx = context;
        sharedPrefs = new SharedPrefs(mCtx);
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

        @BindView(R.id.assignment_flag)
        View assignment_flag;

        @BindView(R.id.card_container)
        LinearLayout card_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void nowBind(MemberListRecyclerModel memberListRecyclerModel){
            setTextController(tv_member_name, memberListRecyclerModel.getMember_name());
            setTextController(tv_member_r_id, memberListRecyclerModel.getMember_r_id());
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

    private void showDialogForExit(Context context, MemberListRecyclerModel memberListRecyclerModel) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForExitBody(builder, context, memberListRecyclerModel);
    }

    private void showDialogForExitBody(MaterialAlertDialogBuilder builder, Context context, MemberListRecyclerModel memberListRecyclerModel) {

        builder.setTitle(context.getResources().getString(R.string.thresher_title))
                .setIcon(context.getResources().getDrawable(R.drawable.ic_smiley_face))
                .setMessage(context.getResources().getString(R.string.thresher_question))
                .setPositiveButton(context.getResources().getString(R.string.thresher_bg), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    sharedPrefs.setKeyThresher("BG");
                    Intent intent = new Intent (mCtx, ReVerifyActivity.class);
                    sharedPrefs.setKeyThreshingUniqueMemberId(memberListRecyclerModel.getUnique_member_id());
                    mCtx.startActivity(intent);
                })
                .setNeutralButton(context.getResources().getString(R.string.thresher_self), (dialog, which) -> {
                    dialog.dismiss();
                    sharedPrefs.setKeyThresher("Self");
                    Intent intent = new Intent (mCtx, ReVerifyActivity.class);
                    sharedPrefs.setKeyThreshingUniqueMemberId(memberListRecyclerModel.getUnique_member_id());
                    mCtx.startActivity(intent);
                })
                .setCancelable(false)
                .show();
    }

    void submit(MemberListRecyclerModel memberListRecyclerModel){

        if (memberListRecyclerModel.getStaff_id() != null) {
            if (memberListRecyclerModel.getStaff_id().equalsIgnoreCase(sharedPrefs.getStaffID())){
                String route = sharedPrefs.getKeyThreshingActivityRoute();
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
                }
            }else{
                //You cannot schedule for this guy
                showScheduleProblemStart(mCtx.getResources().getString(R.string.wrong_member_schedule),mCtx);
            }
        } else {
            String route = sharedPrefs.getKeyThreshingActivityRoute();
            showScheduleProblemStart(mCtx.getResources().getString(R.string.wrong_member_schedule),mCtx);
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
            }
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
}

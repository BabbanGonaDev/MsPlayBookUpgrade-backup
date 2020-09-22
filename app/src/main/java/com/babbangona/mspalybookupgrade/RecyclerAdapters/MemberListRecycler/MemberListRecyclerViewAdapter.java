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

import java.io.File;

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

        void submit(MemberListRecyclerModel memberListRecyclerModel){

            if (memberListRecyclerModel.getStaff_id().equalsIgnoreCase(sharedPrefs.getStaffID())){
                String route = sharedPrefs.getKeyThreshingActivityRoute();
                Intent intent;
                switch (route){
                    case "1":
                        intent = new Intent (mCtx, ReVerifyActivity.class);
                        break;
                    case "2":
                    case "3":
                    case "4":
                        intent = new Intent (mCtx, FieldList.class);
                        break;
                    default:
                        intent = new Intent (mCtx, ComingSoon.class);
                        break;
                }
                sharedPrefs.setKeyThreshingUniqueMemberId(memberListRecyclerModel.getUnique_member_id());
                mCtx.startActivity(intent);
            }else{
                //You cannot schedule for this guy
                Toast.makeText(mCtx, "You cannot schedule for a member not assigned to you", Toast.LENGTH_SHORT).show();
            }
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
            if (staff_id.equalsIgnoreCase(sharedPrefs.getStaffID())){
                assignment_flag.setBackground(ContextCompat.getDrawable(context,R.drawable.assignment_green));
            }else{
                assignment_flag.setBackground(ContextCompat.getDrawable(context,R.drawable.assignment_red));
            }
        }
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

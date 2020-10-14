package com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpMembersRecycler;

import android.app.Activity;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FertilizerMembersRecyclerViewAdapter extends PagedListAdapter<FertilizerMembersRecyclerModel, FertilizerMembersRecyclerViewAdapter.MyViewHolder> {

    private Context mCtx;
    SharedPrefs sharedPrefs;
    AppDatabase appDatabase;

    public FertilizerMembersRecyclerViewAdapter(Context context){
        super(USER_DIFF);
        this.mCtx = context;
        sharedPrefs = new SharedPrefs(mCtx);
        appDatabase = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fertilizer_home_member_card, parent, false);
        return new MyViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        FertilizerMembersRecyclerModel fertilizerMembersRecyclerModel = getItem(position);
        if (fertilizerMembersRecyclerModel != null) {
            holder.nowBind(fertilizerMembersRecyclerModel);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.leader_image)
        ImageView leader_image;

        @BindView(R.id.tv_member_name)
        TextView tv_member_name;

        @BindView(R.id.tv_village)
        TextView tv_village;

        @BindView(R.id.tv_ik_number)
        TextView tv_ik_number;

        @BindView(R.id.card_container)
        LinearLayout card_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void nowBind(FertilizerMembersRecyclerModel fertilizerMembersRecyclerModel){
            setTextController(tv_member_name, fertilizerMembersRecyclerModel.getMember_name());
            setTextController(tv_village, fertilizerMembersRecyclerModel.getVillage());
            setTextController(tv_ik_number, fertilizerMembersRecyclerModel.getIk_number());
            setLeader_image(leader_image, fertilizerMembersRecyclerModel.getUnique_member_id(),mCtx);
            card_container.setOnClickListener((view)->submit(fertilizerMembersRecyclerModel));

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
                iv_picture.setImageResource(R.drawable.avatar);
            }
        }
    }

    void submit(FertilizerMembersRecyclerModel fertilizerMembersRecyclerModel){
        //move to select collection center after asking if member is available
    }

    private static DiffUtil.ItemCallback<FertilizerMembersRecyclerModel> USER_DIFF =
            new DiffUtil.ItemCallback<FertilizerMembersRecyclerModel>() {
                // MSB details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(FertilizerMembersRecyclerModel oldFertilizerMembersRecyclerModel,
                                               FertilizerMembersRecyclerModel newFertilizerMembersRecyclerModel) {
                    return oldFertilizerMembersRecyclerModel.getUnique_member_id().equals(newFertilizerMembersRecyclerModel.getUnique_member_id());
                }

                @Override
                public boolean areContentsTheSame(FertilizerMembersRecyclerModel oldFertilizerMembersRecyclerModel,
                                                  FertilizerMembersRecyclerModel newFertilizerMembersRecyclerModel) {
                    return oldFertilizerMembersRecyclerModel.getUnique_member_id().equals(newFertilizerMembersRecyclerModel.getUnique_member_id());
                }
            };

}

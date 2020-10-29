package com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpHomeRecycler;

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

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.FertilizerSignUpViews.FertilizerSignUpMembers;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FertilizerHomeRecyclerViewAdapter extends PagedListAdapter<FertilizerHomeRecyclerModel, FertilizerHomeRecyclerViewAdapter.MyViewHolder> {

    private final Context mCtx;
    SharedPrefs sharedPrefs;
    AppDatabase appDatabase;

    public FertilizerHomeRecyclerViewAdapter(Context context){
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
        FertilizerHomeRecyclerModel fertilizerHomeRecyclerModel = getItem(position);
        if (fertilizerHomeRecyclerModel != null) {
            holder.nowBind(fertilizerHomeRecyclerModel);
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

        @BindView(R.id.tv_phone_number)
        TextView tv_phone_number;

        @BindView(R.id.card_container)
        LinearLayout card_container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void nowBind(FertilizerHomeRecyclerModel fertilizerHomeRecyclerModel){
            setTextController(tv_member_name, mCtx.getResources().getString(R.string.tg_leader)+" "+fertilizerHomeRecyclerModel.getMember_name());
            setTextController(tv_village, mCtx.getResources().getString(R.string.location_constant)+" "+fertilizerHomeRecyclerModel.getVillage());
            setTextController(tv_ik_number, mCtx.getResources().getString(R.string.ik_number)+" "+fertilizerHomeRecyclerModel.getIk_number());
            setLeader_image(leader_image, fertilizerHomeRecyclerModel.getUnique_member_id());
            setTextController(tv_phone_number, mCtx.getResources().getString(R.string.phone_number_static_new)+" "+cleanInputText(fertilizerHomeRecyclerModel.getPhone_number()));
            card_container.setOnClickListener((view)->submit(fertilizerHomeRecyclerModel));

        }

        String cleanInputText(String input_text){
            if (input_text == null){
                input_text = "";
            }
            return input_text;
        }

        void setTextController(TextView textView, String text) {
            textView.setText(text);
        }

        void setLeader_image(ImageView iv_picture, String unique_id){

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

    void submit(FertilizerHomeRecyclerModel fertilizerHomeRecyclerModel){
        //move to fertilizer trust group member class
        sharedPrefs.setKeyFertilizerSignUpIkNumber(fertilizerHomeRecyclerModel.getIk_number());
        mCtx.startActivity(new Intent(mCtx, FertilizerSignUpMembers.class));
    }

    private static final DiffUtil.ItemCallback<FertilizerHomeRecyclerModel> USER_DIFF =
            new DiffUtil.ItemCallback<FertilizerHomeRecyclerModel>() {
                // MSB details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(FertilizerHomeRecyclerModel oldFertilizerHomeRecyclerModel,
                                               FertilizerHomeRecyclerModel newFertilizerHomeRecyclerModel) {
                    return oldFertilizerHomeRecyclerModel.getUnique_member_id().equals(newFertilizerHomeRecyclerModel.getUnique_member_id());
                }

                @Override
                public boolean areContentsTheSame(FertilizerHomeRecyclerModel oldFertilizerHomeRecyclerModel,
                                                  FertilizerHomeRecyclerModel newFertilizerHomeRecyclerModel) {
                    return oldFertilizerHomeRecyclerModel.getUnique_member_id().equals(newFertilizerHomeRecyclerModel.getUnique_member_id());
                }
            };

}

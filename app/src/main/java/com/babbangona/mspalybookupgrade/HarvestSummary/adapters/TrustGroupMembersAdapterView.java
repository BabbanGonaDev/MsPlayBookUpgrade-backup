package com.babbangona.mspalybookupgrade.HarvestSummary.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.HarvestSummary.MemberDetails;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class TrustGroupMembersAdapterView extends RecyclerView.Adapter<TrustGroupMembersAdapterView.MyViewHolder> {

    Context context;
    List<Members> allMembers;
    SharedPrefs sharedPrefs;

    public TrustGroupMembersAdapterView(Context context, List<Members> allMembers) {
        this.context = context;
        this.allMembers = allMembers;
        sharedPrefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trust_group_members_recycler, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Members members1 = allMembers.get(position);

        holder.ikNumber.setText(members1.getIk_number());
        holder.tgLeader.setText(members1.getFirst_name() + " " + members1.getLast_name());
        holder.location.setText(members1.getVillage_name());

        File ImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.MS_PLAYBOOK_INPUT_PICTURE_LOCATION);
        String image_name = File.separator + allMembers.get(position).getUnique_member_id() + "_thumb.jpg";

        Picasso
                .get()
                .load(new File(ImgDirectory.getAbsoluteFile(), image_name))
                .error(R.drawable.user)
                .into(holder.tgImage);

        holder.tgCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sharedPrefs.setKeyIndividualHarvestSummaryUniqueMemberId(members1.getUnique_member_id());
                sharedPrefs.setKeyIndividualHarvestSummaryName(members1.getFirst_name() + " " + members1.getLast_name());
                sharedPrefs.setKeyIndividualHarvestSummaryIkNumber(members1.getIk_number());
                Intent intent = new Intent(context, MemberDetails.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allMembers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ikNumber, tgLeader, location;
        ImageView tgImage;
        CardView tgCardLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ikNumber = (itemView).findViewById(R.id.ik_number_tv);
            tgLeader = (itemView).findViewById(R.id.tg_leader_tv);
            location = (itemView).findViewById(R.id.tg_location_tv);
            tgImage = (itemView).findViewById(R.id.tg_image);
            tgCardLayout = (itemView).findViewById(R.id.tg_card_layout);
        }
    }
}

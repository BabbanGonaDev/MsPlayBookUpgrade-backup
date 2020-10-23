package com.babbangona.mspalybookupgrade.HarvestSummary.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.babbangona.mspalybookupgrade.HarvestSummary.HarvestSummaryPage;
import com.babbangona.mspalybookupgrade.HarvestSummary.MemberDetails;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

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

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trust_group_members_recycler,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Members members1 = allMembers.get(position);

        holder.ikNumber.setText(members1.getIk_number());
        holder.tgLeader.setText(members1.getFirst_name() + " " + members1.getLast_name());
        holder.location.setText(members1.getVillage_name());

        holder.tgCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefs.setKeyIndividualHarvestSummaryIkNumber(members1.getIk_number());
                Intent intent = new Intent(context, MemberDetails.class);
                context.startActivity(intent);
                Toast.makeText(context, members1.getIk_number(), Toast.LENGTH_SHORT).show();
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

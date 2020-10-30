package com.babbangona.mspalybookupgrade.HarvestSummary.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.HarvestSummary.HarvestSummaryPage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HarvestTrustGroupListAdapterView extends RecyclerView.Adapter<HarvestTrustGroupListAdapterView.MyViewHolder> implements Filterable {

    Context context;
    public List<Members> allMembers;
    List<Members> filteredMembers;
    SharedPrefs sharedPrefs;

    public HarvestTrustGroupListAdapterView(Context context, List<Members> Members) {
        this.context = context;
        this.allMembers = Members;
        this.filteredMembers = Members;
        sharedPrefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public HarvestTrustGroupListAdapterView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trust_group_list_recycler,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HarvestTrustGroupListAdapterView.MyViewHolder holder, int position) {
        Members members1 = filteredMembers.get(position);

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

        /*File imageFile = new File(Objects.requireNonNull(context.getExternalFilesDir(null))
                .getAbsoluteFile(), DatabaseStringConstants.MS_PLAYBOOK_INPUT_PICTURE_LOCATION + "/" + filteredMembers.get(position).getUnique_member_id() + "_thumb.jpg");

        Log.d("creedpictures", members1.getUnique_member_id());

        if (imageFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            holder.tgImage.setImageBitmap(myBitmap);
        }*/

        holder.tgCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefs.setKeyHarvestSummaryIkNumber(members1.getIk_number());
                Intent intent = new Intent(context, HarvestSummaryPage.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredMembers.size();
    }

    @Override
    public Filter getFilter() {
        return arrayListFilter;
    }

    private Filter arrayListFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Members> filteredList = new ArrayList<>();
            if (charSequence.toString().isEmpty()){
                filteredMembers = allMembers;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Members item : allMembers){
                    String name = item.getFirst_name() + " " + item.getLast_name();
                    if (item.getIk_number().toLowerCase().contains(filterPattern) || name.toLowerCase().contains(filterPattern) || item.getVillage_name().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                filteredMembers = filteredList;
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredMembers;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            filteredMembers = (List<Members>) filterResults.values;
            notifyDataSetChanged();
        }
    };

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

package com.babbangona.mspalybookupgrade.donotpay.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.databinding.ItemDonotpayTrustGroupsBinding;
import com.babbangona.mspalybookupgrade.donotpay.data.models.TGList;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TrustGroupAdapter extends RecyclerView.Adapter<TrustGroupAdapter.ViewHolder> implements Filterable {

    private final OnItemClickListener listener;
    private List<TGList> list;
    private List<TGList> mFilteredList;
    private Context mCtx;

    public TrustGroupAdapter(Context mCtx, List<TGList> list, OnItemClickListener listener) {
        this.listener = listener;
        this.list = list;
        this.mFilteredList = list;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        ItemDonotpayTrustGroupsBinding donotpayTrustGroupsBinding = ItemDonotpayTrustGroupsBinding.inflate(inflater, parent, false);
        return new ViewHolder(donotpayTrustGroupsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TGList tg = mFilteredList.get(position);
        holder.bind(tg);
    }

    @Override
    public int getItemCount() {
        if (mFilteredList != null) {
            return mFilteredList.size();
        } else {
            return list.size();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String char_string = constraint.toString().trim();
                if (char_string.isEmpty()) {
                    mFilteredList = list;
                } else {
                    List<TGList> filtered_list = new ArrayList<>();
                    for (TGList tg : list) {
                        if (tg.getIk_number().toLowerCase().contains(char_string) || tg.getFirst_name().toLowerCase().contains(char_string)
                                || tg.getLast_name().toLowerCase().contains(char_string)) {
                            filtered_list.add(tg);
                        }
                    }
                    mFilteredList = filtered_list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList = (List<TGList>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onClick(TGList list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemDonotpayTrustGroupsBinding binding;

        public ViewHolder(ItemDonotpayTrustGroupsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                listener.onClick(mFilteredList.get(getLayoutPosition()));
            });
        }

        public void bind(TGList list) {
            binding.mtvTgLeader.setText("TG Leader: " + list.getFirst_name() + " " + list.getLast_name());
            binding.mtvIkNumber.setText("IK Number: " + list.getIk_number());
            binding.mtvLocation.setText("Location: " + list.getVillage_name());

            File ImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.MS_PLAYBOOK_INPUT_PICTURE_LOCATION);
            String image_name = File.separator + list.getUnique_member_id() + "_thumb.jpg";

            Picasso
                    .get()
                    .load(new File(ImgDirectory.getAbsoluteFile(), image_name))
                    .error(R.drawable.avatar)
                    .into(binding.imgViewTrustGroup);

            binding.executePendingBindings();
        }

    }
}

package com.babbangona.mspalybookupgrade.transporter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.databinding.ItemTransporterSelectionBinding;
import com.babbangona.mspalybookupgrade.transporter.data.models.CustomTransporter;

import java.util.ArrayList;
import java.util.List;

public class SelectTransporterAdapter extends RecyclerView.Adapter<SelectTransporterAdapter.ViewHolder> implements Filterable {

    private OnItemClickListener listener;
    private List<CustomTransporter> list;
    private List<CustomTransporter> mFilteredList;
    private Context mCtx;

    public SelectTransporterAdapter(Context mCtx, List<CustomTransporter> list, OnItemClickListener listener) {
        this.listener = listener;
        this.list = list;
        this.mFilteredList = list;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        ItemTransporterSelectionBinding selectBinding = ItemTransporterSelectionBinding.inflate(inflater, parent, false);
        return new ViewHolder(selectBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomTransporter custom = mFilteredList.get(position);
        holder.bind(custom);
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
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mFilteredList = list;
                } else {
                    List<CustomTransporter> filtered = new ArrayList<>();
                    for (CustomTransporter custom : list) {
                        if (custom.getPhone_number().contains(charString) ||
                                custom.getFirst_name().toLowerCase().contains(charString) || custom.getLast_name().toLowerCase().contains(charString)) {
                            filtered.add(custom);
                        }
                    }
                    mFilteredList = filtered;
                }
                FilterResults results = new FilterResults();
                results.values = mFilteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList = (List<CustomTransporter>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onClick(CustomTransporter transList);

        void makeFavourite(CustomTransporter trans);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransporterSelectionBinding binding;

        public ViewHolder(ItemTransporterSelectionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                listener.onClick(mFilteredList.get(getLayoutPosition()));
            });

            binding.imgBtnFavourite.setOnClickListener(v -> {
                listener.makeFavourite(mFilteredList.get(getLayoutPosition()));
            });
        }

        public void bind(CustomTransporter transport) {
            binding.mtvFullName.setText("Name: " + transport.getFirst_name() + " " + transport.getLast_name());
            binding.mtvAreaOfOperation.setText("Areas: " + transport.getAreas());
            binding.mtvPhoneNumber.setText("Phone: " + transport.getPhone_number());
            binding.mtvBagsTransported.setText("Bags Transported: " + transport.getBags_transported());
            binding.executePendingBindings();
        }
    }
}

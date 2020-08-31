package com.babbangona.mspalybookupgrade.transporter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.databinding.ItemTransporterViewRegTransportersBinding;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;

import java.util.ArrayList;
import java.util.List;

public class ViewTransportersAdapter extends RecyclerView.Adapter<ViewTransportersAdapter.ViewHolder> implements Filterable {

    private final OnItemClickListener listener;
    private List<TransporterTable> list;
    private List<TransporterTable> mFilteredList;
    private Context mCtx;

    public ViewTransportersAdapter(Context mCtx, List<TransporterTable> list, OnItemClickListener listener) {
        this.listener = listener;
        this.list = list;
        this.mFilteredList = list;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        ItemTransporterViewRegTransportersBinding transportersBinding = ItemTransporterViewRegTransportersBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(transportersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransporterTable t_list = mFilteredList.get(position);
        holder.bind(t_list);
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
                String char_string = constraint.toString();
                if (char_string.isEmpty()) {
                    mFilteredList = list;
                } else {
                    List<TransporterTable> filtered_list = new ArrayList<>();
                    for (TransporterTable transporter : list) {
                        if (transporter.getPhone_number().toLowerCase().contains(char_string) || transporter.getFirst_name().toLowerCase().contains(char_string) || transporter.getLast_name().toLowerCase().contains(char_string)) {
                            filtered_list.add(transporter);
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
                mFilteredList = (List<TransporterTable>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {
        void onClick(TransporterTable trans_list);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransporterViewRegTransportersBinding binding;

        public ViewHolder(ItemTransporterViewRegTransportersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                listener.onClick(mFilteredList.get(getLayoutPosition()));
            });
        }

        public void bind(TransporterTable transport) {
            binding.mtvFullName.setText("Full Name: " + transport.getFirst_name() + " " + transport.getLast_name());
            binding.mtvPhoneNumber.setText("Phone Number: " + transport.getPhone_number());
            binding.mtvPaymentOption.setText("Payment Option: " + transport.getPayment_option());
            binding.mtvDateRegistered.setText("Reg Date: " + transport.getReg_date());
            binding.executePendingBindings();
        }
    }
}

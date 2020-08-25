package com.babbangona.mspalybookupgrade.transporter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.databinding.ItemTransporterVehicleTypeBinding;
import com.babbangona.mspalybookupgrade.transporter.data.models.Vehicle;

import java.util.List;

public class VehicleTypeAdapter extends RecyclerView.Adapter<VehicleTypeAdapter.ViewHolder> {

    private final OnItemClickListener listener;
    private List<Vehicle> vehicle_list;
    private Context mCtx;


    public VehicleTypeAdapter(Context mCtx, List<Vehicle> vehicle_list, OnItemClickListener listener) {
        this.vehicle_list = vehicle_list;
        this.mCtx = mCtx;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTransporterVehicleTypeBinding transporterVehicleTypeBinding = ItemTransporterVehicleTypeBinding.inflate(inflater, parent, false);
        return new ViewHolder(transporterVehicleTypeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Vehicle vehicle = vehicle_list.get(position);
        holder.bind(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicle_list.size();
    }

    public interface OnItemClickListener {
        void onClick(Vehicle vehicle);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransporterVehicleTypeBinding binding;

        public ViewHolder(ItemTransporterVehicleTypeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                listener.onClick(vehicle_list.get(getLayoutPosition()));
            });
        }

        public void bind(Vehicle vehicle) {
            binding.mtvVehicle.setText(vehicle.getVehicle_type());
            binding.imgViewVehicle.setImageResource(vehicle.getVehicle_image());
            binding.executePendingBindings();
        }
    }
}

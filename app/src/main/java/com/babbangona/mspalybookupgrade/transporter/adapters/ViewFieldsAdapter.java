package com.babbangona.mspalybookupgrade.transporter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ItemTransporterViewFieldsBinding;
import com.babbangona.mspalybookupgrade.transporter.data.models.Fields;

import java.util.List;

public class ViewFieldsAdapter extends RecyclerView.Adapter<ViewFieldsAdapter.ViewHolder> {
    private OnItemClickListener listener;
    private List<Fields> fieldList;
    private Context mCtx;

    public ViewFieldsAdapter(Context mCtx, List<Fields> fieldList, OnItemClickListener listener) {
        this.listener = listener;
        this.fieldList = fieldList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        ItemTransporterViewFieldsBinding transporterViewFieldsBinding = ItemTransporterViewFieldsBinding.inflate(inflater, parent, false);
        return new ViewHolder(transporterViewFieldsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fields field = fieldList.get(position);
        holder.bind(field);
    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    public interface OnItemClickListener {
        void onClick(Fields field);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransporterViewFieldsBinding binding;

        public ViewHolder(ItemTransporterViewFieldsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                listener.onClick(fieldList.get(getLayoutPosition()));
            });
        }

        public void bind(Fields fields) {
            binding.mtvFieldId.setText("Field ID: " + fields.getUniqueFieldId());
            binding.mtvFieldLocation.setText("Location: N/A");
            binding.mtvFieldSize.setText("Field Size: " + fields.getFieldSize() + " HA");
            binding.mtvThreshingDate.setText("Threshing Date: Nothing yet");
            binding.imgViewField.setImageResource(R.drawable.ic_sand_clock_default);
            binding.executePendingBindings();
        }
    }
}

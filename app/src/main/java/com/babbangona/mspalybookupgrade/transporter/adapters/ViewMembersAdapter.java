package com.babbangona.mspalybookupgrade.transporter.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ItemTransporterViewMembersBinding;
import com.babbangona.mspalybookupgrade.transporter.data.models.Members;

public class ViewMembersAdapter extends PagedListAdapter<Members, ViewMembersAdapter.ViewHolder> {

    private static DiffUtil.ItemCallback<Members> DIFF_CALLBACK = new DiffUtil.ItemCallback<Members>() {
        @Override
        public boolean areItemsTheSame(@NonNull Members oldItem, @NonNull Members newItem) {
            return oldItem.getUniqueMemberId().equals(newItem.getUniqueMemberId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Members oldItem, @NonNull Members newItem) {
            return oldItem.equals(newItem);
        }
    };
    private final OnItemClickListener listener;

    public ViewMembersAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemTransporterViewMembersBinding transporterViewMembersBinding = ItemTransporterViewMembersBinding.inflate(inflater, parent, false);
        return new ViewHolder(transporterViewMembersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Members member = getItem(position);
        if (member != null) {
            holder.bindTo(member);
        } else {
            holder.clear();
        }
    }

    public interface OnItemClickListener {
        void goToFields(Members member);

        void assignTransporter(Members member);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransporterViewMembersBinding binding;

        public ViewHolder(@NonNull ItemTransporterViewMembersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.btnViewFields.setOnClickListener(v -> {
                listener.goToFields(getItem(getLayoutPosition()));
            });

            binding.btnAssignTransporter.setOnClickListener(v -> {
                listener.assignTransporter(getItem(getLayoutPosition()));
            });
        }

        public void bindTo(Members m) {
            binding.mtvMemberName.setText("Name: " + m.getFullName());
            binding.mtvFieldSize.setText("Field Size: " + m.getFieldSize() + " HA");
            binding.mtvMemberLocation.setText("Location: " + m.getVillageName());
            binding.imgViewMember.setImageResource(R.drawable.bg_logo);
        }

        void clear() {
            binding.mtvMemberLocation.invalidate();
            binding.mtvFieldSize.invalidate();
            binding.mtvMemberName.invalidate();
            binding.imgViewMember.invalidate();
        }
    }
}

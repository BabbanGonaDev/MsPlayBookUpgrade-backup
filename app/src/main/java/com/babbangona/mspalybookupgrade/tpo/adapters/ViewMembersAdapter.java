package com.babbangona.mspalybookupgrade.tpo.adapters;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.databinding.ItemTpoViewMembersBinding;
import com.babbangona.mspalybookupgrade.tpo.data.models.MemberModel;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ViewMembersAdapter extends PagedListAdapter<MemberModel, ViewMembersAdapter.ViewHolder> {

    private static DiffUtil.ItemCallback<MemberModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<MemberModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull MemberModel oldItem, @NonNull MemberModel newItem) {
            return oldItem.getUnique_member_id().equals(newItem.getUnique_member_id());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull MemberModel oldItem, @NonNull MemberModel newItem) {
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
        ItemTpoViewMembersBinding itemTpoViewMembersBinding = ItemTpoViewMembersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemTpoViewMembersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemberModel member = getItem(position);
        if (member != null) {
            holder.bind(member);
        } else {
            holder.clear();
        }
    }


    public interface OnItemClickListener {
        void markAttendance(MemberModel mem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTpoViewMembersBinding binding;

        public ViewHolder(@NonNull ItemTpoViewMembersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                listener.markAttendance(getItem(getLayoutPosition()));
            });
        }

        public void bind(MemberModel member) {

            binding.mtvMemberName.setText(member.getFirst_name() + " " + member.getLast_name());
            binding.mtvIkNumber.setText(member.getIk_number());
            binding.mtvPhoneNumber.setText(member.getPhone_number());

            //Bind image
            File ImgDirectory = new File(Environment.getExternalStorageDirectory().getPath(), DatabaseStringConstants.MS_PLAYBOOK_INPUT_PICTURE_LOCATION);
            String image_name = File.separator + member.getUnique_member_id() + "_thumb.jpg";

            Picasso
                    .get()
                    .load(new File(ImgDirectory.getAbsoluteFile(), image_name))
                    .error(R.drawable.avatar)
                    .into(binding.imgViewMember);

            binding.executePendingBindings();
        }

        void clear() {
            binding.mtvPhoneNumber.invalidate();
            binding.mtvIkNumber.invalidate();
            binding.mtvMemberName.invalidate();
            binding.imgViewMember.invalidate();
        }
    }
}

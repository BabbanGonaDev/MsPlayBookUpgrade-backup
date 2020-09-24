package com.babbangona.mspalybookupgrade.transporter.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.databinding.ItemTransporterSelectCcBinding;
import com.babbangona.mspalybookupgrade.transporter.data.models.CcModel;

import java.util.List;

public class SelectCcAdapter extends RecyclerView.Adapter<SelectCcAdapter.ViewHolder> {

    private final OnItemClickListener listener;
    private List<CcModel> cc_list;
    private Context mCtx;

    public SelectCcAdapter(Context mCtx, List<CcModel> cc_list, OnItemClickListener listener) {
        this.cc_list = cc_list;
        this.mCtx = mCtx;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        ItemTransporterSelectCcBinding transporterSelectCcBinding = ItemTransporterSelectCcBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(transporterSelectCcBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CcModel cc = cc_list.get(position);
        holder.bind(cc);
    }

    @Override
    public int getItemCount() {
        return cc_list.size();
    }

    public interface OnItemClickListener {
        void selectCc(CcModel current_cc);

        void deSelectCc(CcModel current_cc);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemTransporterSelectCcBinding binding;

        public ViewHolder(ItemTransporterSelectCcBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                CcModel cc = cc_list.get(getLayoutPosition());

                if (cc.getIs_selected()) {
                    //It has already been selected, now let's deselect it.
                    cc.setIs_selected(false);
                    notifyDataSetChanged();

                    listener.deSelectCc(cc);
                } else {
                    //Select it.
                    cc.setIs_selected(true);
                    notifyDataSetChanged();

                    listener.selectCc(cc);
                }
            });
        }

        public void bind(CcModel collection_center) {
            binding.mtvCcName.setText(collection_center.getCc_name());
            if (collection_center.getIs_selected()) {
                binding.mtvCcName.setTextColor(Color.WHITE);
                binding.mcvLayout.setCardBackgroundColor(mCtx.getResources().getColor(R.color.colorPrimary));
            } else {
                binding.mtvCcName.setTextColor(mCtx.getResources().getColor(R.color.colorPrimary));
                binding.mcvLayout.setCardBackgroundColor(Color.WHITE);
            }
            binding.executePendingBindings();
        }
    }
}

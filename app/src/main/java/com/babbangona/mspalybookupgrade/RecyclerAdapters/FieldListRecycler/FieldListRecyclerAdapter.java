package com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FieldListRecyclerAdapter extends PagedListAdapter<FieldListRecyclerModel, FieldListRecyclerAdapter.ViewHolder> {

    private Context context;
    private SharedPrefs sharedPrefs;

    public FieldListRecyclerAdapter(Context context) {
        super(USER_DIFF);
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.field_list_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        FieldListRecyclerModel fieldListRecyclerModel = getItem(position);
        if (fieldListRecyclerModel != null) {
            holder.nowBind(fieldListRecyclerModel);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.field_list_container)
        MaterialCardView field_list_container;

        @BindView(R.id.tv_field_r_id)
        TextView tv_field_r_id;

        @BindView(R.id.tv_member_name)
        TextView tv_member_name;

        @BindView(R.id.tv_phone_number)
        TextView tv_phone_number;

        @BindView(R.id.tv_field_size)
        TextView tv_field_size;

        @BindView(R.id.tv_village_name)
        TextView tv_village_name;

        @BindView(R.id.btn_location)
        ImageView btn_location;

        @BindView(R.id.tv_longitude)
        TextView tv_longitude;

        @BindView(R.id.tv_latitude)
        TextView tv_latitude;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(FieldListRecyclerModel fieldListRecyclerModel){
            tv_member_name.setText(fieldListRecyclerModel.getMember_name());
        }
    }

    private static DiffUtil.ItemCallback<FieldListRecyclerModel> USER_DIFF =
            new DiffUtil.ItemCallback<FieldListRecyclerModel>() {
                // MSB details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(FieldListRecyclerModel oldField, FieldListRecyclerModel newField) {
                    return oldField.getUnique_field_id().equals(newField.getUnique_field_id());
                }

                @Override
                public boolean areContentsTheSame(FieldListRecyclerModel oldField, FieldListRecyclerModel newField) {
                    return oldField.getUnique_field_id().equals(newField.getUnique_field_id());
                }
            };
}

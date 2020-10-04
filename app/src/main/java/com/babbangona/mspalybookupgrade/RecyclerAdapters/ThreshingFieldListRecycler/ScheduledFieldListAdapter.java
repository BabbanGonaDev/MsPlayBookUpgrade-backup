package com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScheduledFieldListAdapter extends RecyclerView.Adapter<ScheduledFieldListAdapter.ViewHolder>{

    private List<ThreshingFieldListRecyclerModel> threshingFieldListRecyclerModelList;
    private Context context;
    private SharedPrefs sharedPrefs;
    AppDatabase appDatabase;

    public ScheduledFieldListAdapter(List<ThreshingFieldListRecyclerModel> threshingFieldListRecyclerModelList, Context context) {
        this.threshingFieldListRecyclerModelList = threshingFieldListRecyclerModelList;
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
        appDatabase = AppDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public ScheduledFieldListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.threshing_field_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduledFieldListAdapter.ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        if (threshingFieldListRecyclerModelList != null && position < threshingFieldListRecyclerModelList.size()) {
            ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel = threshingFieldListRecyclerModelList.get(position);
            holder.nowBind(threshingFieldListRecyclerModel);
        }
    }

    @Override
    public int getItemCount() {
        if (threshingFieldListRecyclerModelList != null){
            return threshingFieldListRecyclerModelList.size();
        }else{
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_field_id)
        TextView tv_field_id;

        @BindView(R.id.tv_village)
        TextView tv_village;

        @BindView(R.id.tv_field_size)
        TextView tv_field_size;

        @BindView(R.id.iv_activity_signal)
        ImageView iv_activity_signal;

        @BindView(R.id.field_list_container)
        MaterialCardView field_list_container;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel){
            String village = context.getResources().getString(R.string.member_village)+ " " + threshingFieldListRecyclerModel.getVillage();
            String field_size = context.getResources().getString(R.string.field_size)+ " " + threshingFieldListRecyclerModel.getField_size();

            tv_field_id.setText(threshingFieldListRecyclerModel.getUnique_field_id());
            tv_village.setText(village);
            tv_field_size.setText(field_size);
            field_list_container.setOnClickListener((view)->{
                //do nothing
            });
            getStatus(threshingFieldListRecyclerModel.getUnique_field_id(),iv_activity_signal);
        }

        void getStatus(String unique_field_id, ImageView iv_activity_signal){
            int status = appDatabase.scheduleThreshingActivitiesFlagDao().getFieldScheduleStatus(unique_field_id);
            int urgent_status = appDatabase.scheduleThreshingActivitiesFlagDao().getFieldUrgentScheduleStatus(unique_field_id);
            int confirm_status = appDatabase.confirmThreshingActivitiesFlagDao().getFieldConfirmStatus(unique_field_id);
            if (confirm_status > 0){
                iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.view_green));
            }else{
                if (status > 0){
                    iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.light_green));
                }else if(urgent_status > 0){
                    iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.amber));
                }else{
                    iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                }
            }
        }
    }
}

package com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.ComingSoon;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.ThreshingViews.FieldList;
import com.babbangona.mspalybookupgrade.ThreshingViews.RescheduleThreshingDateSelectionActivity;
import com.babbangona.mspalybookupgrade.ThreshingViews.ThreshingDateSelectionActivity;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.ReVerifyActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThreshingFieldListAdapter extends RecyclerView.Adapter<ThreshingFieldListAdapter.ViewHolder>{

    private List<ThreshingFieldListRecyclerModel> threshingFieldListRecyclerModelList;
    private Context context;
    private SharedPrefs sharedPrefs;

    public ThreshingFieldListAdapter(List<ThreshingFieldListRecyclerModel> threshingFieldListRecyclerModelList, Context context) {
        this.threshingFieldListRecyclerModelList = threshingFieldListRecyclerModelList;
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
    }

    @NonNull
    @Override
    public ThreshingFieldListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.threshing_field_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreshingFieldListAdapter.ViewHolder holder, int position) {
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

        @BindView(R.id.field_list_container)
        MaterialCardView field_list_container;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel){
            tv_field_id.setText(threshingFieldListRecyclerModel.getUnique_field_id());
            tv_village.setText(threshingFieldListRecyclerModel.getVillage());
            tv_field_size.setText(threshingFieldListRecyclerModel.getField_size());
            field_list_container.setOnClickListener((view)->navigateToSelectThreshDatePage(threshingFieldListRecyclerModel));
        }

        void navigateToSelectThreshDatePage(ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel){

            String route = sharedPrefs.getKeyThreshingActivityRoute();
            if (route.equalsIgnoreCase("1")){
                Intent intent = new Intent (context, ThreshingDateSelectionActivity.class);
                sharedPrefs.setKeyThreshingUniqueFieldId(threshingFieldListRecyclerModel.getUnique_field_id());
                context.startActivity(intent);
            } else if (route.equalsIgnoreCase("2")){
                Intent intent = new Intent (context, RescheduleThreshingDateSelectionActivity.class);
                sharedPrefs.setKeyThreshingUniqueFieldId(threshingFieldListRecyclerModel.getUnique_field_id());
                context.startActivity(intent);
            } else if (route.equalsIgnoreCase("3")){
                // confirm threshing
            } else if (route.equalsIgnoreCase("4")){
                //log HG at risk
            }

        }
    }
}

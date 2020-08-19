package com.babbangona.mspalybookupgrade.RecyclerAdapters.ActivityListRecycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.ComingSoon;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityListAdapter extends RecyclerView.Adapter<ActivityListAdapter.ViewHolder>{

    private List<ActivityListRecyclerModel> activityListRecyclerModelList;
    private Context context;
    private SharedPrefs sharedPrefs;

    public ActivityListAdapter(List<ActivityListRecyclerModel> activityListRecyclerModelList, Context context) {
        this.activityListRecyclerModelList = activityListRecyclerModelList;
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
    }

    @NonNull
    @Override
    public ActivityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityListAdapter.ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        if (activityListRecyclerModelList != null && position < activityListRecyclerModelList.size()) {
            ActivityListRecyclerModel activityListRecyclerModel = activityListRecyclerModelList.get(position);
            holder.nowBind(activityListRecyclerModel);
        }
    }

    @Override
    public int getItemCount() {
        if (activityListRecyclerModelList != null){
            return activityListRecyclerModelList.size();
        }else{
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.mtv_act_name)
        MaterialTextView mtv_act_name;

        @BindView(R.id.mtv_act_desc)
        MaterialTextView mtv_act_desc;

        @BindView(R.id.btn_act_action)
        MaterialButton btn_act_action;

        @BindView(R.id.progress_bar)
        ProgressBar progress_bar;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(ActivityListRecyclerModel activityListRecyclerModel){
            mtv_act_name.setText(activityListRecyclerModel.getActivity_name());
            mtv_act_desc.setText(activityListRecyclerModel.getActivity_statistics());
            String button_name = context.getResources().getString(R.string.update) + " " + activityListRecyclerModel.getActivity_name();
            if (activityListRecyclerModel.getActivity_id().equalsIgnoreCase(DatabaseStringConstants.POOR_WEATHER_SUPPORT_ACTIVITY)){
                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.setMax(Integer.parseInt(activityListRecyclerModel.getTotal_field_count()));
                progress_bar.setProgress(Integer.parseInt(activityListRecyclerModel.getLogged_field_count()));
                progress_bar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_horizontal_hg));
                btn_act_action.setText(button_name);
            }else if (activityListRecyclerModel.getActivity_id().equalsIgnoreCase(DatabaseStringConstants.SET_PORTFOLIO_ACTIVITY)){
                progress_bar.setVisibility(View.GONE);
                btn_act_action.setText(context.getResources().getString(R.string.update_portfolio));
            }else if (activityListRecyclerModel.getActivity_id().equalsIgnoreCase(DatabaseStringConstants.LOG_HG_ACTIVITY)){
                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.setMax(Integer.parseInt(activityListRecyclerModel.getTotal_field_count()));
                progress_bar.setProgress(Integer.parseInt(activityListRecyclerModel.getLogged_field_count()));
                progress_bar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_horizontal_hg));
                btn_act_action.setText(button_name);
            }else{
                progress_bar.setVisibility(View.VISIBLE);
                progress_bar.setMax(Integer.parseInt(activityListRecyclerModel.getTotal_field_count()));
                progress_bar.setProgress(Integer.parseInt(activityListRecyclerModel.getLogged_field_count()));
                progress_bar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_drawable_horizontal));
                btn_act_action.setText(button_name);
            }
            btn_act_action.setOnClickListener((view)->navigateToActivityPage(activityListRecyclerModel));
        }

        void navigateToActivityPage(ActivityListRecyclerModel activityListRecyclerModel){
            try {
                //TODO get shared pref value
                if(activityListRecyclerModel.getActivity_priority().equals(String.valueOf(0))){
                    //Show dialog is App priority is '0'
                    context.startActivity(new Intent(context, ComingSoon.class));
                }else{
                    sharedPrefs.setKeyActivityType(activityListRecyclerModel.getActivity_id());
                    context.startActivity(new Intent(context, Class.forName(activityListRecyclerModel.getActivity_destination())));
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

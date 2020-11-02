package com.babbangona.mspalybookupgrade.ThreshingViews;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScheduleThreshingSummary extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.last_sync_date_tv)
    TextView last_sync_date_tv;

    @BindView(R.id.tv_staff_id)
    TextView tv_staff_id;

    @BindView(R.id.mtv_act_name)
    MaterialTextView mtv_act_name;

    @BindView(R.id.mtv_act_desc)
    TextView mtv_act_desc;

    @BindView(R.id.btn_act_action)
    MaterialButton btn_act_action;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    @BindView(R.id.layout_details)
    LinearLayout layout_details;

    @BindView(R.id.tv_assigned_threshed)
    TextView tv_assigned_threshed;

    @BindView(R.id.tv_total_assigned)
    TextView tv_total_assigned;

    /*@BindView(R.id.tv_unassigned_threshed)
    TextView tv_unassigned_threshed;*/

    AppDatabase appDatabase;

    SharedPrefs sharedPrefs;

    SetPortfolioMethods setPortfolioMethods;

    boolean show_other_layout = false;

    float total_assigned;
    float total_assigned_threshed;
    float total_fields_threshed_by_staff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_threshing_summary_top);
        ButterKnife.bind(ScheduleThreshingSummary.this);
        appDatabase = AppDatabase.getInstance(ScheduleThreshingSummary.this);
        sharedPrefs = new SharedPrefs(ScheduleThreshingSummary.this);
        setPortfolioMethods = new SetPortfolioMethods();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(setPortfolioMethods.getToolbarTitle(ScheduleThreshingSummary.this));
        layout_details.setVisibility(View.GONE);

        total_assigned = appDatabase.scheduleThreshingActivitiesFlagDao().getAssignedTotalFieldsSum(sharedPrefs.getStaffID());
        total_assigned_threshed = appDatabase.scheduleThreshingActivitiesFlagDao().getAssignedScheduleThreshSum(sharedPrefs.getStaffID());
        total_fields_threshed_by_staff = appDatabase.scheduleThreshingActivitiesFlagDao().getScheduleThreshSum(sharedPrefs.getStaffID());

        int percentage = (int) ((total_assigned_threshed/total_assigned)*100);
        String percent_text = percentage+ " %";
        mtv_act_desc.setText(percent_text);


        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        setPortfolioMethods.setFooter(last_sync_date_tv,tv_staff_id,ScheduleThreshingSummary.this);
        progressDialog(total_assigned,total_assigned_threshed);
    }

    @OnClick(R.id.btn_act_action)
    void setBtn_act_action(){
        show_other_layout = !show_other_layout;
        setBtn_act_action_text(show_other_layout,layout_details, btn_act_action);
    }

    private void setBtn_act_action_text(boolean show_other_layout, LinearLayout layout_details, MaterialButton btn_act_action){
        if (show_other_layout){
            layout_details.setVisibility(View.VISIBLE);
            btn_act_action.setText(getResources().getString(R.string.hide_details));
            setOtherTextViews();
        }else{
            layout_details.setVisibility(View.GONE);
            btn_act_action.setText(getResources().getString(R.string.view_details));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.threshing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle item selection
        /*if (item.getItemId() == R.id.schedule) {
            startActivity(new Intent(ScheduleThreshingSummary.this, CalenderViewActivity.class));
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        finish();
        loadPreviousActivity();
    }

    public void loadPreviousActivity() {
        startActivity(new Intent(ScheduleThreshingSummary.this, ThreshingActivity.class));
    }

    void progressDialog(float total, float progress){
        progress_bar.setMax((int) total);
        progress_bar.setProgress((int) progress);
        progress_bar.setProgressDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.progress_drawable_horizontal,getTheme()));
    }

    void setOtherTextViews(){
        String assigned_threshed = roundFieldSizeDouble(total_assigned_threshed) + "HA /";
        String assigned_total = roundFieldSizeDouble(total_assigned) + "HA";
        String unassigned_threshed = roundFieldSizeDouble(revertToZero(total_fields_threshed_by_staff-total_assigned_threshed)) + "HA";
        tv_assigned_threshed.setText(assigned_threshed);
        tv_total_assigned.setText(assigned_total);
        //tv_unassigned_threshed.setText(unassigned_threshed);
    }

    double revertToZero(double input_value){
        if (input_value < 0){
            input_value = input_value * (-1);
        }
        return input_value;
    }

    private double roundFieldSizeDouble(double result){
        return Math.round(result * 10) / 10.0;
    }

}
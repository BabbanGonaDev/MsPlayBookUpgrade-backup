package com.babbangona.mspalybookupgrade.RecyclerAdapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.ComingSoon;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.ActivityListRecycler.ActivityListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.entities.SyncSummary;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SyncSummaryAdapter extends RecyclerView.Adapter<SyncSummaryAdapter.ViewHolder>{

    private List<SyncSummary> syncSummaryList;
    private Context context;
    private SharedPrefs sharedPrefs;

    public SyncSummaryAdapter(List<SyncSummary> syncSummaryList, Context context) {
        this.syncSummaryList = syncSummaryList;
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
    }

    @NonNull
    @Override
    public SyncSummaryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sync_summary_recycler_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SyncSummaryAdapter.ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        if (syncSummaryList != null && position < syncSummaryList.size()) {
            SyncSummary syncSummary = syncSummaryList.get(position);
            holder.nowBind(syncSummary);
        }
    }

    @Override
    public int getItemCount() {
        if (syncSummaryList != null){
            return syncSummaryList.size();
        }else{
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_sync_status)
        ImageView iv_sync_status;

        @BindView(R.id.tv_table_name)
        TextView tv_table_name;

        @BindView(R.id.tv_sync_time)
        TextView tv_sync_time;

        @BindView(R.id.tv_remarks)
        TextView tv_remarks;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(SyncSummary syncSummary){
            tv_table_name.setText(syncSummary.getTable_name());
            String sync_time = context.getResources().getString(R.string.time) + " " + syncSummary.getSync_time();
            tv_sync_time.setText(sync_time);
            String sync_remarks = context.getResources().getString(R.string.remarks) + " " + syncSummary.getRemarks();
            tv_remarks.setText(sync_remarks);
            if (syncSummary.getStatus().equalsIgnoreCase("0")){
                iv_sync_status.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
            }else{
                iv_sync_status.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
            }
        }
    }
}

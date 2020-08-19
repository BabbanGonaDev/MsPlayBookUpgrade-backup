package com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFragmentRecycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.babbangona.mspalybookupgrade.utils.ViewPWSDialogFragment;
import com.google.android.material.card.MaterialCardView;


import butterknife.BindView;
import butterknife.ButterKnife;

public class PWSClaimAdapter extends PagedListAdapter<PWSFieldListRecyclerModel.PWSListModel, PWSClaimAdapter.ViewHolder> {

    private Context context;
    private SharedPrefs sharedPrefs;
    private AppDatabase appDatabase;

    public PWSClaimAdapter(Context context) {
        super(USER_DIFF);
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
        appDatabase = AppDatabase.getInstance(this.context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.pws_claim_recycler_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        PWSFieldListRecyclerModel.PWSListModel pwsListModel = getItem(position);
        if (pwsListModel != null) {
            holder.nowBind(pwsListModel);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.pws_list_container)
        MaterialCardView pws_list_container;

        @BindView(R.id.tv_pws_id)
        TextView tv_pws_id;

        @BindView(R.id.tv_claim_category)
        TextView tv_claim_category;

        @BindView(R.id.tv_pws_field_size)
        TextView tv_pws_field_size;

        @BindView(R.id.tv_date_logged)
        TextView tv_date_logged;

        @BindView(R.id.iv_activity_signal)
        ImageView iv_activity_signal;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(PWSFieldListRecyclerModel.PWSListModel pwsListModel){
            String pws_id = pwsListModel.getPws_id();
            String category = context.getResources().getString(R.string.claim_category) +" "+ pwsListModel.getCategory();
            String pws_size = context.getResources().getString(R.string.pws_size) +" "+ pwsListModel.getPws_area();
            String log_date = context.getResources().getString(R.string.pws_date_logged) +" "+ pwsListModel.getDate_logged();
            tv_pws_id.setText(pws_id);
            tv_claim_category.setText(category);
            tv_pws_field_size.setText(pws_size);
            tv_date_logged.setText(log_date);
            pws_list_container.setOnClickListener(v -> viewClaim(appDatabase.pwsActivitiesFlagDao().getFieldDetails(pwsListModel.getUnique_field_id()), pwsListModel.getPws_id()));
        }

        void viewClaim(PWSFieldListRecyclerModel pwsFieldListRecyclerModel, String pws_id){
            sharedPrefs.setKeyPWSFieldModel(pwsFieldListRecyclerModel);
            sharedPrefs.setKeyPwsId(pws_id);
            sharedPrefs.setKeyPWSViewRole("mik");

            FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
            ViewPWSDialogFragment newFragment = new ViewPWSDialogFragment();

            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack("").commit();
        }
    }

    private static DiffUtil.ItemCallback<PWSFieldListRecyclerModel.PWSListModel> USER_DIFF =
            new DiffUtil.ItemCallback<PWSFieldListRecyclerModel.PWSListModel>() {
                // MSB details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(PWSFieldListRecyclerModel.PWSListModel oldPWSListModel,
                                               PWSFieldListRecyclerModel.PWSListModel newPWSListModel) {
                    return oldPWSListModel.getPws_id().equals(newPWSListModel.getPws_id());
                }

                @Override
                public boolean areContentsTheSame(PWSFieldListRecyclerModel.PWSListModel oldPWSListModel,
                                                  PWSFieldListRecyclerModel.PWSListModel newPWSListModel) {
                    return oldPWSListModel.getPws_id().equals(newPWSListModel.getPws_id());
                }
            };

}

package com.babbangona.mspalybookupgrade.RecyclerAdapters.PCPWSHomePageRecycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.babbangona.mspalybookupgrade.utils.RevPWSDialogFragment;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.babbangona.mspalybookupgrade.utils.ViewPWSDialogFragment;
import com.google.android.material.card.MaterialCardView;


import butterknife.BindView;
import butterknife.ButterKnife;

public class PCPWSReviewedAdapter extends PagedListAdapter<PCPWSRecyclerModel, PCPWSReviewedAdapter.ViewHolder> {

    private Context context;
    private SharedPrefs sharedPrefs;
    private SetPortfolioMethods setPortfolioMethods;
    private AppDatabase appDatabase;

    public PCPWSReviewedAdapter(Context context) {
        super(USER_DIFF);
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
        setPortfolioMethods = new SetPortfolioMethods();
        appDatabase = AppDatabase.getInstance(this.context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.pc_pws_recycler_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("binderAdded", "onBindViewHolder:  i bind ");
        PCPWSRecyclerModel setPortfolioRecyclerModel = getItem(position);
        if (setPortfolioRecyclerModel != null) {
            holder.nowBind(setPortfolioRecyclerModel);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.pws_list_container)
        MaterialCardView pws_list_container;

        @BindView(R.id.tv_field_id)
        TextView tv_field_id;

        @BindView(R.id.tv_member_name)
        TextView tv_member_name;

        @BindView(R.id.tv_member_r_id)
        TextView tv_member_r_id;

        @BindView(R.id.tv_date_logged)
        TextView tv_date_logged;

        @BindView(R.id.tv_claim_category)
        TextView tv_claim_category;

        @BindView(R.id.btn_act_action)
        Button btn_act_action;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(PCPWSRecyclerModel pcpwsRecyclerModel){
            String unique_field_id = pcpwsRecyclerModel.getUnique_field_id();
            String memberName = context.getResources().getString(R.string.member_name) +" "+ pcpwsRecyclerModel.getMember_name();
            String memberRID = context.getResources().getString(R.string.member_r_id) +" "+ pcpwsRecyclerModel.getMember_r_id();
            String category = context.getResources().getString(R.string.claim_category) +" "+ pcpwsRecyclerModel.getCategory();
            String log_date = context.getResources().getString(R.string.pws_date_logged) +" "+ pcpwsRecyclerModel.getDate_logged();
            tv_field_id.setText(unique_field_id);
            tv_member_name.setText(memberName);
            tv_member_r_id.setText(memberRID);
            tv_claim_category.setText(category);
            tv_date_logged.setText(log_date);
            btn_act_action.setText(context.getResources().getString(R.string.pws_view_details));

            btn_act_action.setOnClickListener(v -> viewClaim(appDatabase.pwsActivitiesFlagDao().getFieldDetails(pcpwsRecyclerModel.getUnique_field_id()), pcpwsRecyclerModel.getPws_id()));
        }

        void viewClaim(PWSFieldListRecyclerModel pwsFieldListRecyclerModel, String pws_id){
            sharedPrefs.setKeyPWSFieldModel(pwsFieldListRecyclerModel);
            sharedPrefs.setKeyPwsId(pws_id);
            sharedPrefs.setKeyPWSViewRole("pc");

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

    private static DiffUtil.ItemCallback<PCPWSRecyclerModel> USER_DIFF =
            new DiffUtil.ItemCallback<PCPWSRecyclerModel>() {
                // Details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(PCPWSRecyclerModel oldPCPWSRecyclerModel, PCPWSRecyclerModel newPCPWSRecyclerModel) {
                    return oldPCPWSRecyclerModel.getPws_id().equals(newPCPWSRecyclerModel.getPws_id());
                }

                @Override
                public boolean areContentsTheSame(PCPWSRecyclerModel oldPCPWSRecyclerModel,
                                                  PCPWSRecyclerModel newPCPWSRecyclerModel) {
                    return oldPCPWSRecyclerModel.getPws_id().equals(newPCPWSRecyclerModel.getPws_id());
                }
            };

}

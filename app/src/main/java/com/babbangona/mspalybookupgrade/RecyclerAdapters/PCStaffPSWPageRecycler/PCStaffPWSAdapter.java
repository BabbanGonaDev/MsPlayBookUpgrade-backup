package com.babbangona.mspalybookupgrade.RecyclerAdapters.PCStaffPSWPageRecycler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.PCPWSHomePage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PCStaffPWSAdapter extends PagedListAdapter<PCStaffPWSRecyclerModel, PCStaffPWSAdapter.ViewHolder> {

    private Context context;
    private SharedPrefs sharedPrefs;
    private SetPortfolioMethods setPortfolioMethods;
    private AppDatabase appDatabase;

    public PCStaffPWSAdapter(Context context) {
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
        View view = layoutInflater.inflate(R.layout.pc_staff_pws_recycler_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        PCStaffPWSRecyclerModel pcStaffPWSRecyclerModel = getItem(position);
        if (pcStaffPWSRecyclerModel != null) {
            holder.nowBind(pcStaffPWSRecyclerModel);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.pc_staff_pws_list_container)
        MaterialCardView pc_staff_pws_list_container;

        @BindView(R.id.tv_staff_id)
        TextView tv_staff_id;

        @BindView(R.id.tv_staff_name)
        TextView tv_staff_name;

        @BindView(R.id.tv_unreviewed_claims)
        TextView tv_unreviewed_claims;

        @BindView(R.id.tv_total_claims)
        TextView tv_total_claims;

        @BindView(R.id.btn_phone_call)
        MaterialButton btn_phone_call;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(PCStaffPWSRecyclerModel pcStaffPWSRecyclerModel){
            String staff_id = pcStaffPWSRecyclerModel.getStaff_id();
            String staffName = context.getResources().getString(R.string.staff_name) +" "+ pcStaffPWSRecyclerModel.getStaff_name();
            String unreviewedClaims = context.getResources().getString(R.string.no_unreviewed_claims) +" "+(pcStaffPWSRecyclerModel.getNumber_of_claims() - pcStaffPWSRecyclerModel.getNumber_reviewed_claims()) ;
            String totalClaims = context.getResources().getString(R.string.no_total_claims) +" "+ pcStaffPWSRecyclerModel.getNumber_of_claims();
            tv_staff_id.setText(staff_id);
            tv_staff_name.setText(staffName);
            tv_unreviewed_claims.setText(unreviewedClaims);
            tv_total_claims.setText(totalClaims);

            pc_staff_pws_list_container.setOnClickListener(v -> viewClaimDistribution(pcStaffPWSRecyclerModel));
        }

        void viewClaimDistribution(PCStaffPWSRecyclerModel pcStaffPWSRecyclerModel){
            sharedPrefs.setKeyPcPwsHomeStaffId(pcStaffPWSRecyclerModel.getStaff_id());
            context.startActivity(new Intent(context, PCPWSHomePage.class));
            //start PCPSWHomePage
        }
    }

    private static DiffUtil.ItemCallback<PCStaffPWSRecyclerModel> USER_DIFF =
            new DiffUtil.ItemCallback<PCStaffPWSRecyclerModel>() {
                // Details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(PCStaffPWSRecyclerModel oldPCStaffPWSRecyclerModel, PCStaffPWSRecyclerModel newPCStaffPWSRecyclerModel) {
                    return oldPCStaffPWSRecyclerModel.getStaff_id().equals(newPCStaffPWSRecyclerModel.getStaff_id());
                }

                @Override
                public boolean areContentsTheSame(PCStaffPWSRecyclerModel oldPCStaffPWSRecyclerModel,
                                                  PCStaffPWSRecyclerModel newPCStaffPWSRecyclerModel) {
                    return oldPCStaffPWSRecyclerModel.getStaff_id().equals(newPCStaffPWSRecyclerModel.getStaff_id());
                }
            };

}

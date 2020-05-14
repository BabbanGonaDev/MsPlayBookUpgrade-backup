package com.babbangona.mspalybookupgrade.RecyclerAdapters.SetPortfolioRecycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetPortfolioAdapter extends PagedListAdapter<SetPortfolioRecyclerModel, SetPortfolioAdapter.ViewHolder> {

    private Context context;
    private SharedPrefs sharedPrefs;
    private SetPortfolioMethods setPortfolioMethods;

    public SetPortfolioAdapter(Context context) {
        super(USER_DIFF);
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
        setPortfolioMethods = new SetPortfolioMethods();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.portfolio_recycler_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        SetPortfolioRecyclerModel setPortfolioRecyclerModel = getItem(position);
        if (setPortfolioRecyclerModel != null) {
            holder.nowBind(setPortfolioRecyclerModel);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.staff_id)
        MaterialTextView staff_id;

        @BindView(R.id.staff_name)
        MaterialTextView staff_name;

        @BindView(R.id.staff_hub)
        MaterialButton staff_hub;

        @BindView(R.id.checkBox)
        MaterialCheckBox checkBox;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(SetPortfolioRecyclerModel setPortfolioRecyclerModel){
            Set<String> portfolioList = sharedPrefs.getKeyPortfolioList();
            staff_id.setText(setPortfolioRecyclerModel.getStaff_id());
            staff_name.setText(setPortfolioRecyclerModel.getStaff_name());
            staff_hub.setText(setPortfolioRecyclerModel.getStaff_hub());

            if (portfolioList != null) {
                if (portfolioList.contains(setPortfolioRecyclerModel.getStaff_id())){
                    setPortfolioRecyclerModel.setSelected(true);
                }else if(!portfolioList.contains(setPortfolioRecyclerModel.getStaff_id())){
                    setPortfolioRecyclerModel.setSelected(false);
                }
            }
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> setCheckBox(setPortfolioRecyclerModel,isChecked));
            checkBox.setChecked(setPortfolioRecyclerModel.isSelected());
        }

        void setCheckBox(SetPortfolioRecyclerModel setPortfolioRecyclerModel, boolean isChecked){
            Set<String> portfolio, portfolio1;
            if (isChecked ) {
                setPortfolioRecyclerModel.setSelected(true);
                portfolio = sharedPrefs.getKeyPortfolioList();
                portfolio1 = setPortfolioMethods.addStaff(portfolio,setPortfolioRecyclerModel.getStaff_id());
                sharedPrefs.setKeyPortfolioList(portfolio1);
            } else {
                portfolio = sharedPrefs.getKeyPortfolioList();
                setPortfolioRecyclerModel.setSelected(false);
                portfolio1 = setPortfolioMethods.removeStaff(portfolio,setPortfolioRecyclerModel.getStaff_id());
                sharedPrefs.setKeyPortfolioList(portfolio1);
                System.out.println(portfolio1);
            }
        }
    }

    private static DiffUtil.ItemCallback<SetPortfolioRecyclerModel> USER_DIFF =
            new DiffUtil.ItemCallback<SetPortfolioRecyclerModel>() {
                // MSB details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(SetPortfolioRecyclerModel oldMSB, SetPortfolioRecyclerModel newMSB) {
                    return oldMSB.getStaff_id().equals(newMSB.getStaff_id());
                }

                @Override
                public boolean areContentsTheSame(SetPortfolioRecyclerModel oldMSB,
                                                  SetPortfolioRecyclerModel newMSB) {
                    return oldMSB.getStaff_id().equals(newMSB.getStaff_id());
                }
            };

}

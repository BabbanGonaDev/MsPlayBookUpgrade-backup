package com.babbangona.mspalybookupgrade.RecyclerAdapters.GridDetailsRecycler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.FieldListPage;
import com.babbangona.mspalybookupgrade.HGFieldListPage;
import com.babbangona.mspalybookupgrade.PWSFieldListPage;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GridDetailsRecyclerAdapter extends RecyclerView.Adapter<GridDetailsRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<GridDetailsRecyclerModel.FirstGridModel> firstGridModelList;
    private SetPortfolioMethods setPortfolioMethods;
    private SharedPrefs sharedPrefs;
    private AppDatabase appDatabase;

    public GridDetailsRecyclerAdapter(Context context, List<GridDetailsRecyclerModel.FirstGridModel> firstGridModelList) {
        this.context = context;
        this.firstGridModelList = firstGridModelList;
        setPortfolioMethods = new SetPortfolioMethods();
        sharedPrefs = new SharedPrefs(this.context);
        appDatabase = AppDatabase.getInstance(this.context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.grid_distribution, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (firstGridModelList != null && position < firstGridModelList.size()) {
            GridDetailsRecyclerModel.FirstGridModel firstGridModel = firstGridModelList.get(position);
            holder.nowBind(firstGridModel,position);
        }
    }

    @Override
    public int getItemCount() {
        if (firstGridModelList != null){
            return firstGridModelList.size();
        }else{
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.vertical_recycler_container)
        LinearLayout vertical_recycler_container;

        TextView tv;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void nowBind(GridDetailsRecyclerModel.FirstGridModel firstGridModel, int position){

            int textSize, width,id;
            width = (int)(setPortfolioMethods.getScreenWidthInDPs(context)/5.5);
            textSize = width/8;
            id = 0;


            vertical_recycler_container.setOrientation(LinearLayout.HORIZONTAL);

            double min_lng = firstGridModel.getMin_lng();
            double lng_interval = firstGridModel.getLng_interval();
            for(int i = 0; i < 5; i++){
                double max_lng = min_lng + lng_interval;
                if (i == 0){
                    min_lng = min_lng - 0.1;
                }
                if (i == 4){
                    max_lng = max_lng + 0.1;
                }
                GridDetailsRecyclerModel gridDetailsRecyclerModel = new GridDetailsRecyclerModel(firstGridModel.getMax_lat(),firstGridModel.getMin_lat(),
                        max_lng,min_lng);
                min_lng = max_lng;
                tv = new TextView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tv.setTextColor(Color.parseColor("#333333"));

                tv.setTextSize((float) textSize);

                id = (id == 0) ? View.generateViewId() : id;
                tv.setId(id);
                tv.setPadding(20, 30, 20, 30);
                tv.setLayoutParams(params);
                tv.setWidth(width);
                vertical_recycler_container.addView(tv);
                setTvColour(tv,i,gridDetailsRecyclerModel,position);

            }
        }

        void setTvColour(TextView tv, int i,GridDetailsRecyclerModel gridDetailsRecyclerModelList, int position){
            String string_text;
            switch (position){
                case 0:
                    string_text="A";
                    break;
                case 1:
                    string_text="B";
                    break;
                case 2:
                    string_text="C";
                    break;
                case 3:
                    string_text="D";
                    break;
                default:
                    string_text="E";
            }
            int fieldPortionCount = appDatabase.normalActivitiesFlagDao().fieldPortionCount(
                    "%"+sharedPrefs.getStaffID()+"%", gridDetailsRecyclerModelList.getMin_lat(),
                    gridDetailsRecyclerModelList.getMax_lat(), gridDetailsRecyclerModelList.getMin_lng(),
                    gridDetailsRecyclerModelList.getMax_lng());
            String activity_type = sharedPrefs.getKeyActivityType();

            int fieldPortionCountByActivity;

            switch (activity_type){
                case(DatabaseStringConstants.FERT_1_ACTIVITY):
                    //fertilizer 1
                    fieldPortionCountByActivity = appDatabase.normalActivitiesFlagDao().fieldPortionCountForFertilizer1(
                            "%"+sharedPrefs.getStaffID()+"%", gridDetailsRecyclerModelList.getMin_lat(),
                            gridDetailsRecyclerModelList.getMax_lat(), gridDetailsRecyclerModelList.getMin_lng(),
                            gridDetailsRecyclerModelList.getMax_lng());
                    break;
                case(DatabaseStringConstants.FERT_2_ACTIVITY):
                    //fertilizer 2
                    fieldPortionCountByActivity = appDatabase.normalActivitiesFlagDao().fieldPortionCountForFertilizer2(
                            "%"+sharedPrefs.getStaffID()+"%", gridDetailsRecyclerModelList.getMin_lat(),
                            gridDetailsRecyclerModelList.getMax_lat(), gridDetailsRecyclerModelList.getMin_lng(),
                            gridDetailsRecyclerModelList.getMax_lng());
                    break;
                case(DatabaseStringConstants.LOG_HG_ACTIVITY):
                    //hg_log
                    fieldPortionCountByActivity = appDatabase.hgActivitiesFlagDao().fieldPortionCountForHG(
                            "%"+sharedPrefs.getStaffID()+"%", gridDetailsRecyclerModelList.getMin_lat(),
                            gridDetailsRecyclerModelList.getMax_lat(), gridDetailsRecyclerModelList.getMin_lng(),
                            gridDetailsRecyclerModelList.getMax_lng());
                    break;
                case(DatabaseStringConstants.POOR_WEATHER_SUPPORT_ACTIVITY):
                    //pws_logs
                    fieldPortionCountByActivity = appDatabase.pwsActivitiesFlagDao().fieldPortionCountForPWS(
                            "%"+sharedPrefs.getStaffID()+"%", gridDetailsRecyclerModelList.getMin_lat(),
                            gridDetailsRecyclerModelList.getMax_lat(), gridDetailsRecyclerModelList.getMin_lng(),
                            gridDetailsRecyclerModelList.getMax_lng());
                    break;
                default:
                    fieldPortionCountByActivity = 0;
            }

            if (fieldPortionCount > 0 ){
                /*Log.d("Locations"+position+"_"+i+":-",
                        gridDetailsRecyclerModelList.getMin_lat()+"|"+
                                gridDetailsRecyclerModelList.getMax_lat()+"|"+
                                gridDetailsRecyclerModelList.getMin_lng()+"|"+
                                gridDetailsRecyclerModelList.getMax_lng()+"|"
                );*/
                string_text = string_text + (i+1) + "\n#" + fieldPortionCount;
                tv.setText(string_text);
                int percent_field_count_by_activity;
                if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("3") ||
                        sharedPrefs.getKeyActivityType().equalsIgnoreCase("5")) {
                    percent_field_count_by_activity = fieldPortionCountByActivity * 100 / fieldPortionCount;
                    percent_field_count_by_activity = 100 - percent_field_count_by_activity;
                } else {
                    percent_field_count_by_activity = fieldPortionCountByActivity * 100 / fieldPortionCount;
                }
                if (percent_field_count_by_activity <= 25) {
                    tv.setBackground(context.getResources().getDrawable(R.drawable.grid_red));
                    tv.setTextColor(Color.parseColor("#ffffff"));
                } else if (percent_field_count_by_activity <= 50) {
                    tv.setBackground(context.getResources().getDrawable(R.drawable.grid_orange));
                    tv.setTextColor(Color.parseColor("#ffffff"));
                } else if (percent_field_count_by_activity <= 75) {
                    tv.setBackground(context.getResources().getDrawable(R.drawable.grid_yellow));
                } else if (percent_field_count_by_activity <= 99) {
                    tv.setBackground(context.getResources().getDrawable(R.drawable.grid_light_green));
                } else {
                    tv.setBackground(context.getResources().getDrawable(R.drawable.grid_green));
                    tv.setTextColor(Color.parseColor("#ffffff"));
                }

            }else{
                tv.setBackground(context.getResources().getDrawable(R.drawable.grid_gray));
                tv.setText(" \n ");
            }
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setOnClickListener(v -> tvClick(gridDetailsRecyclerModelList,fieldPortionCount));
        }

        void tvClick(GridDetailsRecyclerModel gridDetailsRecyclerModelList, int fieldPortionCount){
            if (fieldPortionCount > 0){
                Intent intent;
                if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("3")) {
                    intent = new Intent(context, HGFieldListPage.class);
                }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("5")) {
                    intent = new Intent(context, PWSFieldListPage.class);
                } else {
                    intent = new Intent(context, FieldListPage.class);
                }
                sharedPrefs.setKeyRouteType("1");
                Log.d("Locations_click",
                        gridDetailsRecyclerModelList.getMin_lat()+"|"+
                                gridDetailsRecyclerModelList.getMax_lat()+"|"+
                                gridDetailsRecyclerModelList.getMin_lng()+"|"+
                                gridDetailsRecyclerModelList.getMax_lng()+"|"
                );
                sharedPrefs.setKeyGridParameters(String.valueOf(gridDetailsRecyclerModelList.getMin_lat()),
                        String.valueOf(gridDetailsRecyclerModelList.getMax_lat()),
                        String.valueOf(gridDetailsRecyclerModelList.getMin_lng()),
                        String.valueOf(gridDetailsRecyclerModelList.getMax_lng())
                );
                context.startActivity(intent);
            }
        }
    }
}

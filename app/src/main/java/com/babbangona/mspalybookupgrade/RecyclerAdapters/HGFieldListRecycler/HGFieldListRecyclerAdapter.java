package com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HGFieldListRecyclerAdapter extends PagedListAdapter<HGFieldListRecyclerModel, HGFieldListRecyclerAdapter.ViewHolder> {

    private Context context;
    private SharedPrefs sharedPrefs;
    private AppDatabase appDatabase;
    private GPSController gpsController;
    private GPSController.LocationGetter locationGetter;

    public HGFieldListRecyclerAdapter(Context context) {
        super(USER_DIFF);
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
        appDatabase = AppDatabase.getInstance(this.context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hg_field_list_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        HGFieldListRecyclerModel hgFieldListRecyclerModel = getItem(position);
        if (hgFieldListRecyclerModel != null) {
            holder.nowBind(hgFieldListRecyclerModel);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.field_list_container)
        MaterialCardView field_list_container;

        @BindView(R.id.tv_field_r_id)
        TextView tv_field_r_id;

        @BindView(R.id.tv_member_name)
        TextView tv_member_name;

        @BindView(R.id.tv_phone_number)
        TextView tv_phone_number;

        @BindView(R.id.tv_field_size)
        TextView tv_field_size;

        @BindView(R.id.tv_village_name)
        TextView tv_village_name;

        @BindView(R.id.btn_location)
        ImageView btn_location;

        @BindView(R.id.tv_longitude)
        TextView tv_longitude;

        @BindView(R.id.tv_latitude)
        TextView tv_latitude;

        @BindView(R.id.btn_log_visitation)
        MaterialButton btn_log_visitation;

        @BindView(R.id.btn_log_activity)
        MaterialButton btn_log_activity;

        @BindView(R.id.btn_phone_call)
        MaterialButton btn_phone_call;

        @BindView(R.id.iv_activity_signal)
        ImageView iv_activity_signal;

        @BindView(R.id.tv_hg_list)
        TextView tv_hg_list;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(HGFieldListRecyclerModel hgFieldListRecyclerModel){
            String field_r_id = hgFieldListRecyclerModel.getField_r_id();
            String member_name = context.getResources().getString(R.string.member_name) +" "+ hgFieldListRecyclerModel.getMember_name();
            String phone_number = context.getResources().getString(R.string.member_phone_number) +" "+ hgFieldListRecyclerModel.getPhone_number();
            String field_size = context.getResources().getString(R.string.field_size) +" "+ hgFieldListRecyclerModel.getField_size();
            String village = context.getResources().getString(R.string.member_village) +" "+ hgFieldListRecyclerModel.getVillage_name();
            String latitude = "Lat.: " + (Double.parseDouble(hgFieldListRecyclerModel.getMin_lat())+Double.parseDouble(hgFieldListRecyclerModel.getMax_lat()))/2;
            String longitude = "Long.: " + (Double.parseDouble(hgFieldListRecyclerModel.getMin_lng())+Double.parseDouble(hgFieldListRecyclerModel.getMax_lng()))/2;
            tv_field_r_id.setText(field_r_id);
            tv_member_name.setText(member_name);
            tv_phone_number.setText(phone_number);
            tv_field_size.setText(field_size);
            tv_village_name.setText(village);
            tv_latitude.setText(latitude);
            tv_longitude.setText(longitude);
            getStatus(hgFieldListRecyclerModel,btn_log_activity,iv_activity_signal,tv_hg_list);
            btn_log_activity.setOnClickListener(v -> logActivity(hgFieldListRecyclerModel,context,getAdapterPosition()));
            btn_phone_call.setOnClickListener(v -> callMemberDialog(context,hgFieldListRecyclerModel));
        }

        void getStatus(HGFieldListRecyclerModel hgFieldListRecyclerModel,
                         MaterialButton btn_log_activity, ImageView iv_activity_signal,TextView tv_hg_list){
            int status = appDatabase.hgActivitiesFlagDao().countFieldInHGActivity(hgFieldListRecyclerModel.getUnique_field_id());
            String button_text = context.getResources().getString(R.string.hg_button_text);
            btn_log_activity.setText(button_text);
            if (status > 0){
                iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                tv_hg_list.setVisibility(View.VISIBLE);
                StringBuilder initialString = new StringBuilder(context.getResources().getString(R.string.hg_list_header) + "\n" + "---------------------\n");
                List<String> recorded_hg = appDatabase.hgActivitiesFlagDao().getAllFieldHGs(hgFieldListRecyclerModel.getUnique_field_id());
                for (int i = 0; i < recorded_hg.size(); i++) {
                    initialString.append(recorded_hg.get(i)).append("\n");
                }
                tv_hg_list.setText(initialString);
            }else{
                iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                tv_hg_list.setVisibility(View.GONE);
            }
        }

        private void logActivity(HGFieldListRecyclerModel hgFieldListRecyclerModel,
                                 Context context, int position){

            locationGetter = GPSController.initialiseLocationListener((Activity)context);
            double latitude = locationGetter.getLatitude();
            double longitude = locationGetter.getLongitude();
            double min_lat = Double.parseDouble(hgFieldListRecyclerModel.getMin_lat());
            double max_lat = Double.parseDouble(hgFieldListRecyclerModel.getMax_lat());
            double min_lng = Double.parseDouble(hgFieldListRecyclerModel.getMin_lng());
            double max_lng = Double.parseDouble(hgFieldListRecyclerModel.getMax_lng());
            double mid_lat = (max_lat+min_lat)/2.0;
            double mid_lng = (max_lng+min_lng)/2.0;

            double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
            double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

            Log.d("present_location",latitude + "|"+longitude);

            if (locationDistance <= allowedDistance){
                showLogDialogStarter(hgFieldListRecyclerModel, position,latitude,longitude);
            }else{
                locationMismatchedDialog(latitude,longitude,min_lat,max_lat,min_lng,max_lng,
                        context,hgFieldListRecyclerModel.getUnique_field_id(),
                        context.getResources().getString(R.string.wrong_location));
            }
        }
    }

    private void showLogDialogStarter(HGFieldListRecyclerModel hgFieldListRecyclerModel, int position,
                                      double latitude, double longitude){
        showLogDialog(context.getResources().getString(R.string.log_hg_question),
                context,hgFieldListRecyclerModel,position,latitude,longitude);
    }

    private void showLogDialog(String message, Context context, HGFieldListRecyclerModel hgFieldListRecyclerModel,
                               int position, double latitude, double longitude) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showUpdateDialog(builder,message,context,hgFieldListRecyclerModel,position,latitude,longitude);
    }

    private void showUpdateDialog(MaterialAlertDialogBuilder builder, String message, Context context,
                                  HGFieldListRecyclerModel hgFieldListRecyclerModel, int position,
                                  double latitude, double longitude) {

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 20;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        List<String> whole_hg_list = appDatabase.hgListDao().getAllHGs("%"+sharedPrefs.getStaffRole()+"%");

        ArrayAdapter hg_adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, whole_hg_list);

        final AutoCompleteTextView act_hg_list = new AutoCompleteTextView(context);
        act_hg_list.setHint(context.getResources().getString(R.string.hg_autocomplete_hint));
        act_hg_list.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
        act_hg_list.setTypeface(null, Typeface.ITALIC);
        act_hg_list.setDropDownVerticalOffset(1);
        act_hg_list.setAdapter(hg_adapter);
        act_hg_list.setThreshold(1);
        layout.addView(act_hg_list);

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_update_details))
                .setView(layout)
                .setTitle(context.getResources().getString(R.string.log_hg))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    String hg_selection = act_hg_list.getText().toString().trim();
                    if (whole_hg_list.contains(hg_selection)){
                        dialog.dismiss();
                        updateActivity(hgFieldListRecyclerModel,position,hg_selection,latitude,longitude);
                    }else{
                        Toast.makeText(context, context.getResources().getString(R.string.select_valid_hg), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private void updateActivity(HGFieldListRecyclerModel hgFieldListRecyclerModel,int position,
                                String hg_selected, double latitude, double longitude){
        String flag;
        String initial_activity = hg_selected;
        if (hg_selected.startsWith("Solve_")){
            flag = "0";
            hg_selected = hg_selected.replace("Solve_","");
        }else{
            flag = "1";
        }
        int field_hg_activity_existence = appDatabase.hgActivitiesFlagDao()
                .countFieldSpecificHGActivity(hgFieldListRecyclerModel.getUnique_field_id(),hg_selected);

        if (field_hg_activity_existence > 0){
            appDatabase.hgActivitiesFlagDao().updateHGFlag(hgFieldListRecyclerModel.getUnique_field_id(),hg_selected,flag,
                        getDate("spread"),sharedPrefs.getStaffID());
            appDatabase.logsDao().insert(new Logs(hgFieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                    initial_activity,getDate("normal"),sharedPrefs.getStaffRole(),
                    String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
            notifyItemChanged(position);
        }else{
            appDatabase.hgActivitiesFlagDao().insert(new HGActivitiesFlag(hgFieldListRecyclerModel.getUnique_field_id(),
                    hg_selected,getDate("spread"),flag, sharedPrefs.getStaffID(),"0"));
            appDatabase.logsDao().insert(new Logs(hgFieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                    initial_activity,getDate("normal"),sharedPrefs.getStaffRole(),
                    String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
            notifyItemChanged(position);
        }
    }

    private void locationMismatchedDialog(double latitude,
                                          double longitude,
                                          double min_lat,
                                          double max_lat,
                                          double min_lng,
                                          double max_lng,
                                          Context context,
                                          String unique_field_id,
                                          String message) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        locationMismatchedDialog(latitude, longitude, min_lat, max_lat,
                min_lng, max_lng, context, unique_field_id, builder, message);
    }

    private void locationMismatchedDialog(final double latitude,
                                          final double longitude,
                                          final double min_lat,
                                          final double max_lat,
                                          final double min_lng,
                                          final double max_lng,
                                          Context context,
                                          String unique_field_id,
                                          MaterialAlertDialogBuilder builder,
                                          String message) {

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 20;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        final TextView fieldId = new TextView(context);
        fieldId.setText("Field ID: " + unique_field_id);
        fieldId.setPadding(paddingPixel, 0, 0, 0);
        fieldId.setTypeface(null, Typeface.ITALIC);
        layout.addView(fieldId);

        final TextView minlat = new TextView(context);
        minlat.setText("\nMin/Max Lat= " + min_lat + "/"  + max_lat );
        minlat.setPadding(paddingPixel, 0, 0, 0);
        minlat.setTypeface(null, Typeface.ITALIC);
        layout.addView(minlat);

        final TextView minlng = new TextView(context);
        minlng.setText("Min/Max Long= " + min_lng + "/"  + max_lng );
        minlng.setPadding(paddingPixel, 0, 0, 0);
        minlng.setTypeface(null, Typeface.ITALIC);
        layout.addView(minlng);

        final TextView tvId = new TextView(context);
        tvId.setText("\nYou are on Latitude: " + latitude);
        tvId.setPadding(paddingPixel, 0, 0, 0);
        tvId.setTypeface(null, Typeface.ITALIC);
        layout.addView(tvId);

        final TextView tvIK = new TextView(context);
        tvIK.setText("You are on Longitude: " + longitude);
        tvIK.setPadding(paddingPixel, 0, 0, 0);
        tvIK.setTypeface(null, Typeface.ITALIC);
        layout.addView(tvIK);

        builder.setCancelable(false)
                .setView(layout)
                .setTitle("Not on Field.")
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, id) -> dialog.cancel())
                .show();
    }

    private void callMemberDialog(Context context,
                                  HGFieldListRecyclerModel hgFieldListRecyclerModel) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        callMember(builder,context, hgFieldListRecyclerModel);
    }

    private void callMember(MaterialAlertDialogBuilder builder, Context context,
                            HGFieldListRecyclerModel hgFieldListRecyclerModel){
        builder.setTitle(context.getResources().getString(R.string.contact_member))
                .setIcon(R.drawable.ic_communications)
                .setMessage(context.getResources().getString(R.string.call_question)+" "+
                        hgFieldListRecyclerModel.getMember_name()+" ?")
                .setCancelable(true)
                .setPositiveButton(context.getResources().getString(R.string.call), (dialog, id) -> {
                            String uri = "tel:" + hgFieldListRecyclerModel.getPhone_number();
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(uri));
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            context.startActivity(intent);
                })
                .setNegativeButton(context.getResources().getString(R.string.cancel), (dialog, id) -> dialog.cancel())
                .show();
    }

    private String getDate(String module){

        SimpleDateFormat dateFormat1;
        if (module.matches("concat")) {
            dateFormat1 = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault());
        }else if (module.matches("spread")) {
            dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }else{
            dateFormat1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }

        java.util.Date date1 = new java.util.Date();
        return dateFormat1.format(date1);
    }

    private double locationDistanceToFieldCentre(double lat1, double lon1, double lat2, double lon2) {
        double R = 6.371; // Radius of the earth in m
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private double allowedDistanceToFieldBoundary(double lat1, double lon1, double lat2, double lon2) {
        double R = 6.371; // Radius of the earth in m
        double dLat = deg2rad(Math.abs(lat2 - lat1));  // deg2rad below
        double dLon = deg2rad(Math.abs(lon2 - lon1));
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in m
        Log.d("distanceDami",String.valueOf(d));
        return d+0.0002;
        // return Math.sqrt((lat2 - lat1) * (lat2 - lat1) + (lon2 - lon1) * (lon2 - lon1)) * 110000;
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    private String getDeviceID(){
        String device_id;
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(context, Manifest.permission. READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED){
            device_id = tm.getDeviceId();
        } else{
            device_id = "";
        }
        return device_id;
    }

    private static DiffUtil.ItemCallback<HGFieldListRecyclerModel> USER_DIFF =
            new DiffUtil.ItemCallback<HGFieldListRecyclerModel>() {
                // MSB details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(HGFieldListRecyclerModel oldField, HGFieldListRecyclerModel newField) {
                    return oldField.getUnique_field_id().equals(newField.getUnique_field_id());
                }

                @Override
                public boolean areContentsTheSame(HGFieldListRecyclerModel oldField, HGFieldListRecyclerModel newField) {
                    return oldField.getUnique_field_id().equals(newField.getUnique_field_id());
                }
            };
}

package com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FieldListRecyclerAdapter extends PagedListAdapter<FieldListRecyclerModel, FieldListRecyclerAdapter.ViewHolder> {

    private Context context;
    private SharedPrefs sharedPrefs;
    private AppDatabase appDatabase;
    private GPSController gpsController;
    private GPSController.LocationGetter locationGetter;
    private String imei;

    public FieldListRecyclerAdapter(Context context) {
        super(USER_DIFF);
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
        appDatabase = AppDatabase.getInstance(this.context);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.field_list_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        FieldListRecyclerModel fieldListRecyclerModel = getItem(position);
        if (fieldListRecyclerModel != null) {
            holder.nowBind(fieldListRecyclerModel);
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

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(FieldListRecyclerModel fieldListRecyclerModel){
            String field_r_id = fieldListRecyclerModel.getField_r_id();
            String member_name = context.getResources().getString(R.string.member_name) +" "+ fieldListRecyclerModel.getMember_name();
            String phone_number = context.getResources().getString(R.string.member_phone_number) +" "+ fieldListRecyclerModel.getPhone_number();
            String field_size = context.getResources().getString(R.string.field_size) +" "+ fieldListRecyclerModel.getField_size();
            String village = context.getResources().getString(R.string.member_village) +" "+ fieldListRecyclerModel.getVillage_name();
            String latitude = "Lat.: " + (Double.parseDouble(fieldListRecyclerModel.getMin_lat())+Double.parseDouble(fieldListRecyclerModel.getMax_lat()))/2;
            String longitude = "Long.: " + (Double.parseDouble(fieldListRecyclerModel.getMin_lng())+Double.parseDouble(fieldListRecyclerModel.getMax_lng()))/2;
            tv_field_r_id.setText(field_r_id);
            tv_member_name.setText(member_name);
            tv_phone_number.setText(phone_number);
            tv_field_size.setText(field_size);
            tv_village_name.setText(village);
            tv_latitude.setText(latitude);
            tv_longitude.setText(longitude);
            String activity_status = getStatus(fieldListRecyclerModel,btn_log_activity,iv_activity_signal);
            btn_log_activity.setOnClickListener(v -> logActivity(fieldListRecyclerModel,activity_status,context,getAdapterPosition(),"activity"));
            btn_log_visitation.setOnClickListener(v -> logActivity(fieldListRecyclerModel,activity_status,context,getAdapterPosition(),"visitation"));
            btn_phone_call.setOnClickListener(v -> callMemberDialog(context,fieldListRecyclerModel));
        }

        String getStatus(FieldListRecyclerModel fieldListRecyclerModel,
                         MaterialButton btn_log_activity, ImageView iv_activity_signal){
            String status;
            String button_text;
            if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){
                try {
                    status = appDatabase.normalActivitiesFlagDao().getFert1Status(fieldListRecyclerModel.getUnique_field_id());
                } catch (Exception e) {
                    e.printStackTrace();
                    status = "0";
                }
                if (status == null || status.equalsIgnoreCase("")){
                    status = "0";
                }
                if (status.equalsIgnoreCase("0")){
                    button_text = context.getResources().getString(R.string.log_fertilizer_1);
                }else{
                    button_text = context.getResources().getString(R.string.reset_fertilizer_1);
                }
            }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("2")){
                try {
                    status = appDatabase.normalActivitiesFlagDao().getFert2Status(fieldListRecyclerModel.getUnique_field_id());
                } catch (Exception e) {
                    e.printStackTrace();
                    status = "0";
                }
                if (status == null || status.equalsIgnoreCase("")){
                    status = "0";
                }
                if (status.equalsIgnoreCase("0")){
                    button_text = context.getResources().getString(R.string.log_fertilizer_2);
                }else{
                    button_text = context.getResources().getString(R.string.reset_fertilizer_2);
                }
            }else{
                status = "0";
                button_text = context.getResources().getString(R.string.log_activity);
            }
            btn_log_activity.setText(button_text);
            if (status.equalsIgnoreCase("0")){
                iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
            }else{
                iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
            }
            return status;
        }

        private void logActivity(FieldListRecyclerModel fieldListRecyclerModel, String activity_status,
                                 Context context, int position, String module){

            locationGetter = GPSController.initialiseLocationListener((Activity)context);
            double latitude = locationGetter.getLatitude();
            double longitude = locationGetter.getLongitude();
            double min_lat = Double.parseDouble(fieldListRecyclerModel.getMin_lat());
            double max_lat = Double.parseDouble(fieldListRecyclerModel.getMax_lat());
            double min_lng = Double.parseDouble(fieldListRecyclerModel.getMin_lng());
            double max_lng = Double.parseDouble(fieldListRecyclerModel.getMax_lng());
            double mid_lat = (max_lat+min_lat)/2.0;
            double mid_lng = (max_lng+min_lng)/2.0;

            double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
            double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

            Log.d("present_location",latitude + "|"+longitude);

            if (locationDistance <= allowedDistance){
                if (module.equalsIgnoreCase("visitation")){
                    logVisitation(fieldListRecyclerModel,latitude,longitude);
                }else{
                    showLogDialogStarter(fieldListRecyclerModel, activity_status,position,longitude,latitude);
                }
            }else{
                locationMismatchedDialog(latitude,longitude,min_lat,max_lat,min_lng,max_lng,
                        context,fieldListRecyclerModel.getUnique_field_id(),
                        context.getResources().getString(R.string.wrong_location));
            }
        }

        private void logVisitation(FieldListRecyclerModel fieldListRecyclerModel, double latitude, double longitude){
            appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                    "Visitation",getDate("normal"),sharedPrefs.getStaffRole(),
                    String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
            Toast.makeText(context, context.getResources().getString(R.string.visitation_logged), Toast.LENGTH_SHORT).show();
        }

        private void showLogDialogStarter(FieldListRecyclerModel fieldListRecyclerModel,
                                          String activity_status, int position, double latitude,
                                          double longitude){

            if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){

                if (activity_status.equalsIgnoreCase("1")){
                    showLogDialog(context.getResources().getString(R.string.fert_1_reset_question),
                            context,fieldListRecyclerModel,"reset",position, latitude, longitude);
                }else if (activity_status.equalsIgnoreCase("0")){
                    showLogDialog(context.getResources().getString(R.string.fert_1_question),
                            context,fieldListRecyclerModel,"update",position, latitude, longitude);
                }

            }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("2")){

                if (activity_status.equalsIgnoreCase("1")){
                    showLogDialog(context.getResources().getString(R.string.fert_2_reset_question),
                            context,fieldListRecyclerModel,"reset",position, latitude, longitude);
                }else if (activity_status.equalsIgnoreCase("0")){
                    showLogDialog(context.getResources().getString(R.string.fert_2_question),
                            context,fieldListRecyclerModel,"update",position, latitude, longitude);
                }

            }else{
                Toast.makeText(context, "Activity not found", Toast.LENGTH_SHORT).show();
            }
        }

        private void showLogDialog(String message, Context context, FieldListRecyclerModel fieldListRecyclerModel,
                                   String module, int position, double latitude, double longitude) {
            MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
            if (module.equalsIgnoreCase("update")){
                showUpdateDialog(builder,message,context,fieldListRecyclerModel,position, latitude,longitude);
            }else if (module.equalsIgnoreCase("reset")){
                showResetDialog(builder,message,context,fieldListRecyclerModel,position, latitude, longitude);
            }
        }

        private void showUpdateDialog(MaterialAlertDialogBuilder builder, String message, Context context,
                                      FieldListRecyclerModel fieldListRecyclerModel, int position,
                                      double latitude, double longitude) {

            builder.setIcon(context.getResources().getDrawable(R.drawable.ic_update_details))
                    .setTitle(context.getResources().getString(R.string.log_activity))
                    .setMessage(message)
                    .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                        //this is to dismiss the dialog
                        dialog.dismiss();
                        updateActivity(fieldListRecyclerModel,position, latitude, longitude);
                    })
                    .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                        //this is to dismiss the dialog
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
        }

        private void updateActivity(FieldListRecyclerModel fieldListRecyclerModel,int position,
                                    double latitude, double longitude){
            int field_existence = appDatabase.normalActivitiesFlagDao().countFieldInNormalActivity(fieldListRecyclerModel.getUnique_field_id());
            if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){
                if (field_existence > 0){
                    appDatabase.normalActivitiesFlagDao().updateFert1Flag(fieldListRecyclerModel.getUnique_field_id(),"1",
                            getDate("spread"),sharedPrefs.getStaffID());
                    appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                            "Log Fertilizer 1",getDate("normal"),sharedPrefs.getStaffRole(),
                            String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
                    fieldListRecyclerModel.setFertilizer_1_status("1");
                    notifyItemChanged(getAdapterPosition());
                }else{
                    appDatabase.normalActivitiesFlagDao().insert(new NormalActivitiesFlag(fieldListRecyclerModel.getUnique_field_id(),
                            "1",getDate("spread"),"0","0000-00-00",
                            sharedPrefs.getStaffID(),"0"));
                    appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                            "Log Fertilizer 1",getDate("normal"),sharedPrefs.getStaffRole(),
                            String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
                    fieldListRecyclerModel.setFertilizer_1_status("1");
                    notifyItemChanged(getAdapterPosition());
                }
                Log.d("position_logger",position+"");
            }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("2")){
                if (field_existence > 0){
                    appDatabase.normalActivitiesFlagDao().updateFert2Flag(fieldListRecyclerModel.getUnique_field_id(),"1",
                            getDate("spread"),sharedPrefs.getStaffID());
                    appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                            "Log Fertilizer 2",getDate("normal"),sharedPrefs.getStaffRole(),
                            String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
                    fieldListRecyclerModel.setFertilizer_2_status("1");
                    notifyItemChanged(getAdapterPosition());
                }else{
                    appDatabase.normalActivitiesFlagDao().insert(new NormalActivitiesFlag(fieldListRecyclerModel.getUnique_field_id(),
                            "0","0000-00-00","1",getDate("spread"),
                            sharedPrefs.getStaffID(),"0"));
                    appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                            "Log Fertilizer 2",getDate("normal"),sharedPrefs.getStaffRole(),
                            String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
                    fieldListRecyclerModel.setFertilizer_2_status("1");
                    notifyItemChanged(getAdapterPosition());
                }
            }
        }

        private void showResetDialog(MaterialAlertDialogBuilder builder, String message, Context context,
                                     FieldListRecyclerModel fieldListRecyclerModel, int position,
                                     double latitude, double longitude) {

            builder.setIcon(context.getResources().getDrawable(R.drawable.ic_update_details))
                    .setTitle(context.getResources().getString(R.string.log_activity))
                    .setMessage(message)
                    .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                        //this is to dismiss the dialog
                        dialog.dismiss();
                        resetActivity(fieldListRecyclerModel,position,latitude,longitude);
                    })
                    .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                        //this is to dismiss the dialog
                        dialog.dismiss();
                    })
                    .setCancelable(false)
                    .show();
        }

        private void resetActivity(FieldListRecyclerModel fieldListRecyclerModel, int position,
                                   double latitude, double longitude){
            int field_existence = appDatabase.normalActivitiesFlagDao().countFieldInNormalActivity(fieldListRecyclerModel.getUnique_field_id());
            if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("1")){
                if (field_existence > 0){
                    appDatabase.normalActivitiesFlagDao().updateFert1Flag(fieldListRecyclerModel.getUnique_field_id(),"0",
                            getDate("spread"),sharedPrefs.getStaffID());
                    appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                            "Reset Fertilizer 1",getDate("normal"),sharedPrefs.getStaffRole(),
                            String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
                    fieldListRecyclerModel.setFertilizer_1_status("0");
                    notifyItemChanged(getAdapterPosition());
                }else{
                    appDatabase.normalActivitiesFlagDao().insert(new NormalActivitiesFlag(fieldListRecyclerModel.getUnique_field_id(),
                            "0",getDate("spread"),"0","0000-00-00",
                            sharedPrefs.getStaffID(),"0"));
                    appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                            "Reset Fertilizer 1",getDate("normal"),sharedPrefs.getStaffRole(),
                            String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
                    fieldListRecyclerModel.setFertilizer_1_status("0");
                    notifyItemChanged(getAdapterPosition());
                }
            }else if (sharedPrefs.getKeyActivityType().equalsIgnoreCase("2")){
                if (field_existence > 0){
                    appDatabase.normalActivitiesFlagDao().updateFert2Flag(fieldListRecyclerModel.getUnique_field_id(),"0",
                            getDate("spread"),sharedPrefs.getStaffID());
                    appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                            "Reset Fertilizer 2",getDate("normal"),sharedPrefs.getStaffRole(),
                            String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
                    fieldListRecyclerModel.setFertilizer_2_status("0");
                    notifyItemChanged(getAdapterPosition());
                }else{
                    appDatabase.normalActivitiesFlagDao().insert(new NormalActivitiesFlag(fieldListRecyclerModel.getUnique_field_id(),
                            "0","0000-00-00","0",getDate("spread"),
                            sharedPrefs.getStaffID(),"0"));
                    appDatabase.logsDao().insert(new Logs(fieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                            "Reset Fertilizer 2",getDate("normal"),sharedPrefs.getStaffRole(),
                            String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0"));
                    fieldListRecyclerModel.setFertilizer_2_status("0");
                    notifyItemChanged(getAdapterPosition());
                }
            }
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

        builder.setCancelable(false).setView(layout)
                .setTitle("Not on Field.")
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, id) -> dialog.cancel())
                .show();
    }

    private void callMemberDialog(Context context,
                                  FieldListRecyclerModel fieldListRecyclerModel) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        callMember(builder,context, fieldListRecyclerModel);
    }

    private void callMember(MaterialAlertDialogBuilder builder, Context context,
                            FieldListRecyclerModel fieldListRecyclerModel){
        builder.setTitle(context.getResources().getString(R.string.contact_member))
                .setIcon(R.drawable.ic_communications)
                .setMessage(context.getResources().getString(R.string.call_question)+" "+
                        fieldListRecyclerModel.getMember_name()+" ?")
                .setCancelable(true)
                .setPositiveButton(context.getResources().getString(R.string.call), (dialog, id) -> {
                            String uri = "tel:" + fieldListRecyclerModel.getPhone_number();
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

    private static DiffUtil.ItemCallback<FieldListRecyclerModel> USER_DIFF =
            new DiffUtil.ItemCallback<FieldListRecyclerModel>() {
                // MSB details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(FieldListRecyclerModel oldField, FieldListRecyclerModel newField) {
                    return oldField.getUnique_field_id().equals(newField.getUnique_field_id());
                }

                @Override
                public boolean areContentsTheSame(FieldListRecyclerModel oldField, FieldListRecyclerModel newField) {
                    return oldField.getUnique_field_id().equals(newField.getUnique_field_id());
                }
            };
}

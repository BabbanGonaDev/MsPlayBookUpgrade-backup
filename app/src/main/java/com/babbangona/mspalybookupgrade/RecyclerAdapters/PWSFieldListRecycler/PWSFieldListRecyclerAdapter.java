package com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.CustomDialogFragment;
import com.babbangona.mspalybookupgrade.utils.CustomDialogFragmentRedFlags;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.babbangona.mspalybookupgrade.utils.PWSDialogFragment;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PWSFieldListRecyclerAdapter extends PagedListAdapter<PWSFieldListRecyclerModel, PWSFieldListRecyclerAdapter.ViewHolder> {

    private Context context;
    private SharedPrefs sharedPrefs;
    private AppDatabase appDatabase;
    private GPSController gpsController;
    private GPSController.LocationGetter locationGetter;
    private SetPortfolioMethods setPortfolioMethods;

    public PWSFieldListRecyclerAdapter(Context context) {
        super(USER_DIFF);
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
        appDatabase = AppDatabase.getInstance(this.context);
        setPortfolioMethods = new SetPortfolioMethods();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.pws_field_list_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        PWSFieldListRecyclerModel pwsFieldListRecyclerModel = getItem(position);
        if (pwsFieldListRecyclerModel != null) {
            holder.nowBind(pwsFieldListRecyclerModel);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.field_list_container)
        MaterialCardView field_list_container;

        @BindView(R.id.tv_field_r_id)
        TextView tv_field_r_id;

        @BindView(R.id.tv_member_r_id)
        TextView tv_member_r_id;

        @BindView(R.id.tv_ik_number)
        TextView tv_ik_number;

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

        @BindView(R.id.tv_crop_type)
        TextView tv_crop_type;

        @BindView(R.id.btn_log_activity)
        MaterialButton btn_log_activity;

        @BindView(R.id.btn_phone_call)
        MaterialButton btn_phone_call;

        @BindView(R.id.iv_activity_signal)
        ImageView iv_activity_signal;

        @BindView(R.id.tv_pws_list)
        TextView tv_pws_list;

        @BindView(R.id.pws_list_container)
        LinearLayout pws_list_container;

        TextView tv;

        boolean show_pws_list = false;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        void nowBind(PWSFieldListRecyclerModel pwsFieldListRecyclerModel){
            String field_r_id = pwsFieldListRecyclerModel.getUnique_field_id();
            String ik_number = context.getResources().getString(R.string.ik_number) +" "+ pwsFieldListRecyclerModel.getIk_number();
            String member_name = context.getResources().getString(R.string.member_name) +" "+ pwsFieldListRecyclerModel.getMember_name();
            String phone_number = context.getResources().getString(R.string.member_phone_number) +" "+ pwsFieldListRecyclerModel.getPhone_number();
            String field_size = context.getResources().getString(R.string.field_size) +" "+ pwsFieldListRecyclerModel.getField_size();
            String village = context.getResources().getString(R.string.member_village) +" "+ pwsFieldListRecyclerModel.getVillage_name();
            String crop_type = context.getResources().getString(R.string.member_crop_type) +" "+ pwsFieldListRecyclerModel.getCrop_type();
            String member_r_id = context.getResources().getString(R.string.member_r_id) +" "+ pwsFieldListRecyclerModel.getField_r_id();
            String latitude = "Lat.: " + (Double.parseDouble(pwsFieldListRecyclerModel.getMin_lat())+Double.parseDouble(pwsFieldListRecyclerModel.getMax_lat()))/2;
            String longitude = "Long.: " + (Double.parseDouble(pwsFieldListRecyclerModel.getMin_lng())+Double.parseDouble(pwsFieldListRecyclerModel.getMax_lng()))/2;
            tv_field_r_id.setText(field_r_id);
            tv_ik_number.setText(ik_number);
            tv_member_name.setText(member_name);
            tv_phone_number.setText(phone_number);
            tv_field_size.setText(field_size);
            tv_village_name.setText(village);
            tv_crop_type.setText(crop_type);
            tv_latitude.setText(latitude);
            tv_longitude.setText(longitude);
            tv_member_r_id.setText(member_r_id);
            pws_list_container.setOrientation(LinearLayout.VERTICAL);
            getStatus(pwsFieldListRecyclerModel,iv_activity_signal,tv_pws_list,pws_list_container);
            btn_log_activity.setOnClickListener(v -> logActivity(pwsFieldListRecyclerModel,context,getAdapterPosition()));
            btn_phone_call.setOnClickListener(v -> callMemberDialog(context,pwsFieldListRecyclerModel));
            tv_pws_list.setOnClickListener(v -> {
                show_pws_list = !show_pws_list;
                setTv_pws_list(show_pws_list,pws_list_container,tv_pws_list);
            });
        }

        private void getStatus(PWSFieldListRecyclerModel pwsFieldListRecyclerModel, ImageView iv_activity_signal,
                               TextView tv_pws_list, LinearLayout pws_list_container){
            int status = appDatabase.pwsActivitiesFlagDao().countFieldInPWSActivity(pwsFieldListRecyclerModel.getUnique_field_id());

            if (status > 0){
                iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                tv_pws_list.setVisibility(View.VISIBLE);
                pws_list_container.setVisibility(View.GONE);
                tv_pws_list.setText(context.getResources().getString(R.string.show_all_pws));
                List<String> allFieldHGs = appDatabase.pwsActivitiesFlagDao().getAllFieldPWS(pwsFieldListRecyclerModel.getUnique_field_id());

                if(pws_list_container.getChildCount() > 0){
                    pws_list_container.removeAllViews();
                }

                for (int i = 0; i < allFieldHGs.size(); i++) {
                    TextView tv = new TextView(context);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                            (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                    tv.setTextSize((float) 12);
                    tv.setPadding(5, 5, 5, 5);
                    tv.setLayoutParams(params);
                    tv.setText(allFieldHGs.get(i));
                    params.setMargins(0,0,0,10);
                    tv.setBackgroundColor(context.getResources().getColor(R.color.view_red));
                    tv.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    pws_list_container.addView(tv);
                }

            }else{
                iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));
                tv_pws_list.setVisibility(View.GONE);
                pws_list_container.setVisibility(View.GONE);
            }
        }


    }

    private void setTv_pws_list(boolean show_pws_list, LinearLayout pws_list_container, TextView tv_pws_list){
        if (show_pws_list){
            pws_list_container.setVisibility(View.VISIBLE);
            tv_pws_list.setText(context.getResources().getString(R.string.hide_all_pws));
        }else{
            pws_list_container.setVisibility(View.GONE);
            tv_pws_list.setText(context.getResources().getString(R.string.show_all_pws));
        }
    }

    private void logActivity(PWSFieldListRecyclerModel pwsFieldListRecyclerModel,
                             Context context, int position){

        locationGetter = GPSController.initialiseLocationListener(context);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();
        double min_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lat());
        double max_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lat());
        double min_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lng());
        double max_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lng());
        double mid_lat = (max_lat+min_lat)/2.0;
        double mid_lng = (max_lng+min_lng)/2.0;

        double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
        double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

        Log.d("present_location",latitude + "|"+longitude);


        if (locationDistance <= allowedDistance){
            showDialog(pwsFieldListRecyclerModel);
        }else{
            locationMismatchedDialog(latitude,longitude,min_lat,max_lat,min_lng,max_lng,
                    context,pwsFieldListRecyclerModel.getUnique_field_id(),
                    context.getResources().getString(R.string.wrong_location));
        }
    }

    public void showDialog(PWSFieldListRecyclerModel pwsFieldListRecyclerModel) {
        sharedPrefs.setKeyPWSFieldModel(pwsFieldListRecyclerModel);

        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        PWSDialogFragment newFragment = new PWSDialogFragment();

            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack("").commit();
    }

    private void logSolveHG(PWSFieldListRecyclerModel pwsFieldListRecyclerModel,
                            Context context){

        locationGetter = GPSController.initialiseLocationListener((Activity)context);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();
        double min_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lat());
        double max_lat = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lat());
        double min_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMin_lng());
        double max_lng = Double.parseDouble(pwsFieldListRecyclerModel.getMax_lng());
        double mid_lat = (max_lat+min_lat)/2.0;
        double mid_lng = (max_lng+min_lng)/2.0;

        double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
        double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

        Log.d("present_location",latitude + "|"+longitude);

        if (locationDistance <= allowedDistance){

            Toast.makeText(context, context.getResources().getString(R.string.error_solve_hg), Toast.LENGTH_SHORT).show();
        }else{
            locationMismatchedDialog(latitude,longitude,min_lat,max_lat,min_lng,max_lng,
                    context,pwsFieldListRecyclerModel.getUnique_field_id(),
                    context.getResources().getString(R.string.wrong_location));
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
                                  PWSFieldListRecyclerModel hgFieldListRecyclerModel) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        callMember(builder,context, hgFieldListRecyclerModel);
    }

    private void callMember(MaterialAlertDialogBuilder builder, Context context,
                            PWSFieldListRecyclerModel hgFieldListRecyclerModel){
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
            try {
                device_id = tm.getDeviceId();
            } catch (Exception e) {
                e.printStackTrace();
                device_id = "";
            }
            if (device_id == null){
                device_id = "";
            }
        } else{
            device_id = "";
        }
        return device_id;
    }

    private static DiffUtil.ItemCallback<PWSFieldListRecyclerModel> USER_DIFF =
            new DiffUtil.ItemCallback<PWSFieldListRecyclerModel>() {
                // MSB details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(PWSFieldListRecyclerModel oldField, PWSFieldListRecyclerModel newField) {
                    return oldField.getUnique_field_id().equals(newField.getUnique_field_id());
                }

                @Override
                public boolean areContentsTheSame(PWSFieldListRecyclerModel oldField, PWSFieldListRecyclerModel newField) {
                    return oldField.getUnique_field_id().equals(newField.getUnique_field_id());
                }
            };
}

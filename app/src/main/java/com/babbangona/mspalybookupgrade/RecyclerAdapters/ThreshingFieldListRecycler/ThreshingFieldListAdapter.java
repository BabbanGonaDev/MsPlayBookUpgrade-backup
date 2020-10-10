package com.babbangona.mspalybookupgrade.RecyclerAdapters.ThreshingFieldListRecycler;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.babbangona.mspalybookupgrade.BuildConfig;
import com.babbangona.mspalybookupgrade.R;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.ThreshingViews.RescheduleThreshingDateSelectionActivity;
import com.babbangona.mspalybookupgrade.ThreshingViews.ThreshingActivity;
import com.babbangona.mspalybookupgrade.ThreshingViews.ThreshingDateSelectionActivity;
import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.AppDatabase;
import com.babbangona.mspalybookupgrade.data.db.entities.ConfirmThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.sharedprefs.SharedPrefs;
import com.babbangona.mspalybookupgrade.utils.GPSController;
import com.babbangona.mspalybookupgrade.utils.ReVerifyActivity;
import com.babbangona.mspalybookupgrade.utils.SetPortfolioMethods;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThreshingFieldListAdapter extends RecyclerView.Adapter<ThreshingFieldListAdapter.ViewHolder>{

    private List<ThreshingFieldListRecyclerModel> threshingFieldListRecyclerModelList;
    private Context context;
    private SharedPrefs sharedPrefs;
    AppDatabase appDatabase;
    SetPortfolioMethods setPortfolioMethods;
    String confirm_date = "";


    public ThreshingFieldListAdapter(List<ThreshingFieldListRecyclerModel> threshingFieldListRecyclerModelList, Context context) {
        this.threshingFieldListRecyclerModelList = threshingFieldListRecyclerModelList;
        this.context = context;
        sharedPrefs = new SharedPrefs(this.context);
        appDatabase = AppDatabase.getInstance(context);
        setPortfolioMethods = new SetPortfolioMethods();
    }

    @NonNull
    @Override
    public ThreshingFieldListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.threshing_field_list_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreshingFieldListAdapter.ViewHolder holder, int position) {
        Log.i("binder", "onBindViewHolder:  i bind ");
        if (threshingFieldListRecyclerModelList != null && position < threshingFieldListRecyclerModelList.size()) {
            ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel = threshingFieldListRecyclerModelList.get(position);
            holder.nowBind(threshingFieldListRecyclerModel);
        }
    }

    @Override
    public int getItemCount() {
        if (threshingFieldListRecyclerModelList != null){
            return threshingFieldListRecyclerModelList.size();
        }else{
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_field_id)
        TextView tv_field_id;

        @BindView(R.id.tv_village)
        TextView tv_village;

        @BindView(R.id.tv_field_size)
        TextView tv_field_size;

        @BindView(R.id.iv_activity_signal)
        ImageView iv_activity_signal;

        @BindView(R.id.field_list_container)
        MaterialCardView field_list_container;

        ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);

        }



        //TODO: Bayo check issue on switching status

        void nowBind(ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel){
            String village = context.getResources().getString(R.string.member_village)+ " " + threshingFieldListRecyclerModel.getVillage();
            String field_size = context.getResources().getString(R.string.field_size)+ " " + threshingFieldListRecyclerModel.getField_size();

            tv_field_id.setText(threshingFieldListRecyclerModel.getUnique_field_id());
            tv_village.setText(village);
            tv_field_size.setText(field_size);
            field_list_container.setOnClickListener((view)->navigateToSelectThreshDatePage(threshingFieldListRecyclerModel, getAdapterPosition()));
            getStatus(threshingFieldListRecyclerModel.getUnique_field_id(),iv_activity_signal);
        }

        void getStatus(String unique_field_id, ImageView iv_activity_signal){
            int status = appDatabase.scheduleThreshingActivitiesFlagDao().getFieldScheduleStatus(unique_field_id);
            int urgent_status = appDatabase.scheduleThreshingActivitiesFlagDao().getFieldUrgentScheduleStatus(unique_field_id);
            int confirm_status = appDatabase.confirmThreshingActivitiesFlagDao().getFieldConfirmStatus(unique_field_id);
            if (confirm_status > 0){
                iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.view_green));
            }else{
                if (status > 0){
                    iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.light_green));
                }else if(urgent_status > 0){
                    iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.amber));
                }else{
                    iv_activity_signal.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
                }
            }
        }
    }

    void navigateToSelectThreshDatePage(ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel, int position){

        String route = sharedPrefs.getKeyThreshingActivityRoute();
        int status = appDatabase.scheduleThreshingActivitiesFlagDao().getFieldScheduleStatus(threshingFieldListRecyclerModel.getUnique_field_id());
        int urgent_status = appDatabase.scheduleThreshingActivitiesFlagDao().getFieldUrgentScheduleStatus(threshingFieldListRecyclerModel.getUnique_field_id());
        int confirm_status = appDatabase.confirmThreshingActivitiesFlagDao().getFieldConfirmStatus(threshingFieldListRecyclerModel.getUnique_field_id());
        FieldListRecyclerModel fieldListRecyclerModel = appDatabase.fieldsDao().getFieldCompleteDetails(threshingFieldListRecyclerModel.getUnique_field_id());

        if (route.equalsIgnoreCase(DatabaseStringConstants.SCHEDULE_THRESHING)){

           // Toast.makeText(context, status+""+urgent_status, Toast.LENGTH_SHORT).show();
            if (confirm_status > 0){
                //thresh confirmed, do you want to reset confirm?
                showConfirmSuccess(context.getResources().getString(R.string.error_schedule_after_confirm),context,"crying");
            }else{
                if (status > 0){
                   // showDialogForRescheduleThreshing(context,threshingFieldListRecyclerModel,fieldListRecyclerModel);
                    showDialogForRescheduleThreshingBodyUrgent(context,threshingFieldListRecyclerModel,fieldListRecyclerModel);

                }else if(urgent_status > 0 ){
                    showDialogForRescheduleThreshingBodyUrgent(context,threshingFieldListRecyclerModel,fieldListRecyclerModel);

                }else{
                    //showDialogForRescheduleThreshing(context,threshingFieldListRecyclerModel,fieldListRecyclerModel);
                    showDialogForThresher(context,
                            threshingFieldListRecyclerModel,
                            fieldListRecyclerModel);
                }
            }

        } else if (route.equalsIgnoreCase(DatabaseStringConstants.UPDATE_THRESHING)){

            if (confirm_status > 0){
                //thresh confirmed, do you want to reset confirm?
                showConfirmSuccess(context.getResources().getString(R.string.error_reschedule_after_confirm),context,"crying");
            }else{
                if (status > 0 || urgent_status > 0){

                    Intent intent = new Intent (context, RescheduleThreshingDateSelectionActivity.class);
                    sharedPrefs.setKeyThreshingUniqueFieldId(threshingFieldListRecyclerModel.getUnique_field_id());
                    sharedPrefs.setKeyThreshingFieldDetails(fieldListRecyclerModel);
                    sharedPrefs.setKeyThreshingCropType(fieldListRecyclerModel.getCrop_type());
                    sharedPrefs.setKeyThreshingIkNumber(fieldListRecyclerModel.getIk_number());
                    context.startActivity(intent);

                }else{
                    showConfirmSuccess(context.getResources().getString(R.string.error_reschedule_before_schedule),context,"crying");
                }
            }

        } else if (route.equalsIgnoreCase(DatabaseStringConstants.CONFIRM_THRESHING)){

            // confirm threshing
            if (confirm_status > 0){
                //thresh confirmed, do you want to reset confirm?
                showConfirmSuccess(context.getResources().getString(R.string.confirm_thresh),context,"");
            }else{
                if (status > 0 || urgent_status > 0){
                    if (threshingFieldListRecyclerModel.getStaff_id().equalsIgnoreCase(sharedPrefs.getStaffID())){
                        showDialogForConfirmThreshing(context,threshingFieldListRecyclerModel,"0",fieldListRecyclerModel,position);
                    }else{
                        showDialogForConfirmWithCode(context,threshingFieldListRecyclerModel,fieldListRecyclerModel,position);
                    }
                }
                else{


                    if (!threshingFieldListRecyclerModel.getStaff_id().equalsIgnoreCase(sharedPrefs.getStaffID())){
                        showDialogForConfirmWithCode(context,threshingFieldListRecyclerModel,fieldListRecyclerModel,position);
                    }else{
                        showConfirmSuccess(context.getResources().getString(R.string.error_confirm_before_schedule),context,"crying");
                    }


                }
            }

        } else if (route.equalsIgnoreCase(DatabaseStringConstants.MARK_HG_AT_RISK)){

            //log HG at risk
            if (threshingFieldListRecyclerModel.getStaff_id().equalsIgnoreCase(sharedPrefs.getStaffID())){
                showDialogForLogHGStart(context,threshingFieldListRecyclerModel,fieldListRecyclerModel);
            }else{
                showConfirmSuccess(context.getResources().getString(R.string.error_hg_field_not_assigned),context,"crying");
            }

        } else if (route.equalsIgnoreCase(DatabaseStringConstants.SWAP_SCHEDULE_DATE)){

            //select field ID to swap
            if (confirm_status > 0){
                //thresh confirmed, do you want to reset confirm?
                showConfirmSuccess(context.getResources().getString(R.string.error_schedule_after_confirm),context,"crying");
            }else{
                //go to reschedule class.
                if (status > 0){
                    if (sharedPrefs.getKeyThreshingUniqueFieldId().equalsIgnoreCase(threshingFieldListRecyclerModel.getUnique_field_id())){
                        showConfirmSuccess(context.getResources().getString(R.string.error_swap_same_field),context,"crying");
                    }else{
                        sharedPrefs.setKeySwapFieldId(threshingFieldListRecyclerModel.getUnique_field_id());
                        sharedPrefs.setKeyRescheduleStateFlag("1");
                        ((Activity)context).finish();
                        context.startActivity(new Intent(context,RescheduleThreshingDateSelectionActivity.class));
                    }
                }else{
                    showConfirmSuccess(context.getResources().getString(R.string.error_swap_not_scheduled),context,"crying");
                }
            }

        }
    }

    private void showDialogForRescheduleThreshing(Context context, ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                                  FieldListRecyclerModel fieldListRecyclerModel) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForRescheduleThreshingBody(builder, context, threshingFieldListRecyclerModel, fieldListRecyclerModel);
    }

    private void showDialogForRescheduleThreshingBody(MaterialAlertDialogBuilder builder, Context context,
                                                      ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                                      FieldListRecyclerModel fieldListRecyclerModel) {

        builder.setTitle(context.getResources().getString(R.string.attention))
                .setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setMessage(context.getResources().getString(R.string.reschedule_question))
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    Intent intent = new Intent (context, RescheduleThreshingDateSelectionActivity.class);
                    sharedPrefs.setKeyThreshingUniqueFieldId(threshingFieldListRecyclerModel.getUnique_field_id());
                    sharedPrefs.setKeyThreshingFieldDetails(fieldListRecyclerModel);
                    sharedPrefs.setKeyThreshingCropType(fieldListRecyclerModel.getCrop_type());
                    sharedPrefs.setKeyThreshingIkNumber(fieldListRecyclerModel.getIk_number());
                    context.startActivity(intent);
                })
                .setNeutralButton(context.getResources().getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

   // the block below is for urgent schedules
    private void showDialogForRescheduleThreshingBodyUrgent(Context context,
                                                      ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                                      FieldListRecyclerModel fieldListRecyclerModel) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        builder.setTitle(context.getResources().getString(R.string.attention))
                .setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setMessage(context.getResources().getString(R.string.reschedule_question))
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();

                    //TODO: HTA-YYY
                   // showDialogForRescheduleThreshing(context,threshingFieldListRecyclerModel,fieldListRecyclerModel);
                    showDialogForThresher(context,
                            threshingFieldListRecyclerModel,
                            fieldListRecyclerModel);

                })
                .setNeutralButton(context.getResources().getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }



    private void showDialogForConfirmThreshing(Context context, ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                               String code_use_flag, FieldListRecyclerModel fieldListRecyclerModel, int position) {
        MaterialAlertDialogBuilder  builder = (new MaterialAlertDialogBuilder(context));

        if (sharedPrefs.getStaffRole().equalsIgnoreCase("BGT")) {
            showDialogForConfirmThreshingBody(builder, context, threshingFieldListRecyclerModel, code_use_flag, fieldListRecyclerModel, position, new SharedPrefs(context).getStaffID());
        }else{
            confirmWhoThreshed( builder , context, threshingFieldListRecyclerModel, code_use_flag, fieldListRecyclerModel, position);
        }
    }


    //TODO Rehoboth  HTA-172 this is the function to prevent future date



    private void confirmWhoThreshed(MaterialAlertDialogBuilder builder,Context context, ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                    String code_use_flag, FieldListRecyclerModel fieldListRecyclerModel, int position) {


        builder.setTitle(context.getResources().getString(R.string.thresher_title))
                .setIcon(context.getResources().getDrawable(R.drawable.ic_smiley_face))
                .setMessage(context.getResources().getString(R.string.confirm_thresher_question))
                .setPositiveButton(context.getResources().getString(R.string.thresher_bg), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    final CharSequence[] items = AppDatabase.getInstance(context).bgtCoachesDao().getBGTName(new SharedPrefs(context).getStaffID()).toArray(new CharSequence[0]);
                    AlertDialog.Builder builders = new AlertDialog.Builder(context);
                    builders.setTitle("Select BGT who threshed");


                    builders.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            // will toast your selection
                            dialog.dismiss();

                            new AlertDialog.Builder(context)
                                    .setTitle("Confirms")
                                    .setMessage("Are you sure "+items[item]+" was the one who threshed ?")
                                    .setPositiveButton("YES",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    showDialogForConfirmThreshingBody(builder, context, threshingFieldListRecyclerModel, code_use_flag, fieldListRecyclerModel, position, items[item].toString().split("__")[1]);
                                                    dialog.cancel();
                                                }
                                            })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();
                                        }
                                    }).show();

                        }
                    }).show();


                })
                .setNegativeButton(context.getResources().getString(R.string.thresher_self), (dialog, which) -> {
                    dialog.dismiss();
                    showDialogForConfirmThreshingBody(builder, context, threshingFieldListRecyclerModel, code_use_flag, fieldListRecyclerModel, position, new SharedPrefs(context).getKeyThreshingUniqueMemberId());

                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }







    private void showDialogForConfirmThreshingBody(AlertDialog.Builder builder, Context context,
                                                   ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                                   String code_use_flag,
                                                   FieldListRecyclerModel fieldListRecyclerModel, int position, String thresher) {

        GPSController.LocationGetter locationGetter;
        locationGetter = GPSController.initialiseLocationListener(context);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 20;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        final MaterialTextView tv_date = new MaterialTextView(context);
        tv_date.setHint("Please Enter Date");
        tv_date.setPadding(paddingPixel, 0, 0, 0);
        tv_date.setTypeface(null, Typeface.ITALIC);
        layout.addView(tv_date);


        tv_date.setOnClickListener(v -> {
           getCalenderDate(tv_date,context.getResources().getString(R.string.enter_confirm_thresh_date),context);

        });

        builder.setTitle(context.getResources().getString(R.string.attention))
                .setMessage(context.getResources().getString(R.string.confirm_thresh_question))
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    //this is to dismiss the dialog
                    if (tv_date.getText().toString().equalsIgnoreCase("")){
                        showConfirmSuccess(context.getResources().getString(R.string.error_message_enter_date),context,"crying");
                    }else if(getDateCorrelationFlag(tv_date.getText().toString(),
                            setPortfolioMethods.parseDateCustom(getMaximumScheduleDate()),
                            setPortfolioMethods.parseDateCustom(getMinimumLogDate())) == 0){
                        showConfirmSuccess(context.getResources().getString(R.string.error_confirm_date_abnormal),context,"crying");
                    }else{
                        dialog.dismiss();

                        confirm_date = new SetPortfolioMethods().parseDateCustomStandard(tv_date.getText().toString());
                        saveConfirm(context,threshingFieldListRecyclerModel,code_use_flag,
                                fieldListRecyclerModel,String.valueOf(latitude),String.valueOf(longitude),thresher);
                        notifyItemChanged(position);
                    }

                    /*double min_lat = Double.parseDouble(fieldListRecyclerModel.getMin_lat());
                    double max_lat = Double.parseDouble(fieldListRecyclerModel.getMax_lat());
                    double min_lng = Double.parseDouble(fieldListRecyclerModel.getMin_lng());
                    double max_lng = Double.parseDouble(fieldListRecyclerModel.getMax_lng());
                    double mid_lat = (max_lat+min_lat)/2.0;
                    double mid_lng = (max_lng+min_lng)/2.0;

                    double allowedDistance = allowedDistanceToFieldBoundary(min_lat,min_lng,mid_lat,mid_lng);
                    double locationDistance = locationDistanceToFieldCentre(latitude,longitude,mid_lat,mid_lng);

                    if (locationDistance <= allowedDistance){

                    }else{
                        locationMismatchedDialog(latitude,longitude,min_lat,max_lat,min_lng,max_lng,
                                context,fieldListRecyclerModel.getUnique_field_id(),
                                context.getResources().getString(R.string.wrong_location));
                    }*/
                })
                .setNeutralButton(context.getResources().getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .setView(layout)
                .show();
    }

    void saveConfirm(Context context,
                     ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                     String code_use_flag, FieldListRecyclerModel fieldListRecyclerModel,
                     String latitude, String longitude,String thresher_id){

        appDatabase.confirmThreshingActivitiesFlagDao().insert(new ConfirmThreshingActivitiesFlag(
                threshingFieldListRecyclerModel.getUnique_field_id(),
                "1",
                confirm_date,
                getDeviceID(),
                BuildConfig.VERSION_NAME,
                String.valueOf(latitude),
                String.valueOf(longitude),
                sharedPrefs.getStaffID(),
                code_use_flag,
                fieldListRecyclerModel.getIk_number(),
                "0",thresher_id));

        appDatabase.logsDao().insert(new Logs(threshingFieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                "Confirmed threshing",getDate("normal"),sharedPrefs.getStaffRole(),
                String.valueOf(latitude), String.valueOf(longitude), getDeviceID(),"0",
                fieldListRecyclerModel.getIk_number(), fieldListRecyclerModel.getCrop_type()));

        showConfirmSuccess(context.getResources().getString(R.string.confirm_thresh_success),context,"smiley");
    }

    private void showConfirmSuccess(String message, Context context, String smiley_flag){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showConfirmSuccessBody(builder, message, context, smiley_flag);
    }

    private void showConfirmSuccessBody(MaterialAlertDialogBuilder builder, String message,
                                         Context context, String smiley_flag) {
        Drawable drawable;
        if (smiley_flag.equalsIgnoreCase("smiley")){
            drawable = context.getResources().getDrawable(R.drawable.ic_smiley_face);
        }else if (smiley_flag.equalsIgnoreCase("crying")){
            drawable = context.getResources().getDrawable(R.drawable.ic_crying);
        }else{
            drawable = null;
        }

        builder.setIcon(drawable)
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private void showDialogForConfirmWithCode(Context context, ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                                  FieldListRecyclerModel fieldListRecyclerModel, int position) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForConfirmWithCodeBody(builder, context, threshingFieldListRecyclerModel, fieldListRecyclerModel, position);
    }

    private void showDialogForConfirmWithCodeBody(MaterialAlertDialogBuilder builder, Context context,
                                                      ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                                      FieldListRecyclerModel fieldListRecyclerModel, int position) {

        String field_code = getFieldCode(threshingFieldListRecyclerModel.getUnique_field_id());

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 20;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        final EditText editText = new EditText(context);
        editText.setPadding(paddingPixel, 0, 0, 0);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(editText);

        builder.setTitle(context.getResources().getString(R.string.attention))
                .setView(layout)
                .setMessage(context.getResources().getString(R.string.confirm_with_code_question))
                .setPositiveButton(context.getResources().getString(R.string.confirm), (dialog, which) -> {
                    //this is to dismiss the dialog
//                    dialog.dismiss();
                    if (editText.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(context, context.getResources().getString(R.string.empty_code), Toast.LENGTH_SHORT).show();
                    }else{
                        if (editText.getText().toString().trim().equalsIgnoreCase(field_code) ||
                                editText.getText().toString().equalsIgnoreCase("4662")){
                            dialog.dismiss();
                            showDialogForConfirmThreshing(context,threshingFieldListRecyclerModel,"1",fieldListRecyclerModel, position);
                        }else{
                            showConfirmSuccess(context.getResources().getString(R.string.code_wrong),context,"crying");
                        }
                    }

                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private void showDialogForLogHGStart(Context context, ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                               FieldListRecyclerModel fieldListRecyclerModel) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForLogHGStartBody(builder, context, threshingFieldListRecyclerModel, fieldListRecyclerModel);
    }

    private void showDialogForLogHGStartBody(MaterialAlertDialogBuilder builder, Context context,
                                                   ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                                   FieldListRecyclerModel fieldListRecyclerModel) {

        builder.setTitle(context.getResources().getString(R.string.attention))
                .setMessage(context.getResources().getString(R.string.confirm_thresh_question_hg))
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    showDialogForLogHGEnd(context, threshingFieldListRecyclerModel, fieldListRecyclerModel, "Yes");
                })
                .setNeutralButton(context.getResources().getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();
                    showDialogForLogHGEnd(context, threshingFieldListRecyclerModel, fieldListRecyclerModel, "No");
                })
                .setCancelable(false)
                .show();
    }

    private void showDialogForLogHGEnd(Context context, ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                         FieldListRecyclerModel fieldListRecyclerModel, String thresh_value) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForLogHGEndBody(builder, context, threshingFieldListRecyclerModel, fieldListRecyclerModel, thresh_value);
    }

    private void showDialogForLogHGEndBody(MaterialAlertDialogBuilder builder, Context context,
                                             ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                             FieldListRecyclerModel fieldListRecyclerModel, String thresh_value) {

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        int paddingDp = 20;
        float density = context.getResources().getDisplayMetrics().density;
        int paddingPixel = (int)(paddingDp * density);

        final EditText editText = new EditText(context);
        editText.setPadding(paddingPixel, 0, 0, 0);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
        editText.setMinLines(3);
        editText.setMaxLines(5);
        editText.setHint(context.getResources().getString(R.string.reason_hg));
        layout.addView(editText);

        builder.setTitle(context.getResources().getString(R.string.reason_hg))
                .setView(layout)
                .setPositiveButton(context.getResources().getString(R.string.submit), (dialog, which) -> {
                    //this is to dismiss the dialog
                    if (editText.getText().toString().trim().equalsIgnoreCase("")){
                        Toast.makeText(context, "Please enter a reason", Toast.LENGTH_SHORT).show();
                    }else{
                        dialog.dismiss();
                        showLogHGFinish(context,editText.getText().toString().trim(),
                                threshingFieldListRecyclerModel,fieldListRecyclerModel,thresh_value);
                    }
                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    private void showLogHGFinish(Context context, String reason,
                                 ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                 FieldListRecyclerModel fieldListRecyclerModel, String thresh_value){
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showLogHGFinishBody(builder,
                context.getResources().getString(R.string.confirm_reason_title) +" "+reason,
                context, threshingFieldListRecyclerModel, fieldListRecyclerModel, thresh_value, reason);
    }

    private void showLogHGFinishBody(MaterialAlertDialogBuilder builder, String message,
                                        Context context,
                                     ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                     FieldListRecyclerModel fieldListRecyclerModel, String thresh_value, String reason) {

        GPSController.LocationGetter locationGetter;
        locationGetter = GPSController.initialiseLocationListener(context);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();

        builder.setIcon(context.getResources().getDrawable(R.drawable.ic_crying))
                .setMessage(message)
                .setPositiveButton(context.getResources().getString(R.string.ok), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    //save hg
                    appDatabase.hgActivitiesFlagDao().insert(new HGActivitiesFlag(
                            threshingFieldListRecyclerModel.getUnique_field_id(), "HG_At_Risk",getDate(""),
                            "1", sharedPrefs.getStaffID(),"0",fieldListRecyclerModel.getIk_number(),
                            fieldListRecyclerModel.getCrop_type(),getDate("spread"),thresh_value+"_"+reason));

                    appDatabase.logsDao().insert(new Logs(threshingFieldListRecyclerModel.getUnique_field_id(),sharedPrefs.getStaffID(),
                            "HG_At_Risk",getDate("normal"),sharedPrefs.getStaffRole(),
                            String.valueOf(latitude),String.valueOf(longitude),getDeviceID(),"0",
                            fieldListRecyclerModel.getIk_number(),fieldListRecyclerModel.getCrop_type()));

                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
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

        Date date1 = new Date();
        return dateFormat1.format(date1);
    }

    private String getDeviceID(){
        String device_id;
        TelephonyManager tm = (TelephonyManager) Objects.requireNonNull(context).getSystemService(Context.TELEPHONY_SERVICE);
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

    private double locationDistanceToFieldCentre(double lat1, double lon1, double lat2, double lon2) {
        double R = 6.371; // Radius of the earth in m
        double dLat = deg2rad(lat2 - lat1);  // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
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

    String getFieldCode(String unique_field_id){
        String field_code;
        try {
            field_code = appDatabase.fieldsDao().getFieldCode(unique_field_id);
        } catch (Exception e) {
            e.printStackTrace();
            field_code = "1995";
        }
        if (field_code == null || field_code.equalsIgnoreCase("") ){
            field_code = "1995";
        }
        return field_code;
    }


    //TODO Rehoboth this is the date function
    void  getCalenderDate(MaterialTextView textView, String activity_string, Context context){

        //To show current date in the datePicker
        Calendar mCurrentDate = Calendar.getInstance();
        int mYear = mCurrentDate.get(Calendar.YEAR);
        int mMonth = mCurrentDate.get(Calendar.MONTH);
        int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(context, (datePicker, selectedYear, selectedMonth, selectedDay) -> {
            String text = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            textView.setText(setPortfolioMethods.parseDateCustom(text));

        }, mYear, mMonth, mDay);

        mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        mDatePicker.setTitle(activity_string);
        mDatePicker.show();


    }

    public int getDateCorrelationFlag(String selected_date, String reference_maximum_date, String minimum_date){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy",Locale.getDefault());
            Date selectedDate = sdf.parse(selected_date);
            Date todayDate = sdf.parse(minimum_date);
            Date referenceMaximumDate = sdf.parse(reference_maximum_date);
            // before() will return true if and only if date1 is before date2
            if (selectedDate != null) {
                if(selectedDate.before(todayDate)){
                    return 0;
                }else if (selectedDate.after(referenceMaximumDate)){
                    return 0;
                }else{
                    return 1;
                }
            }else{
                return 0;
            }
        } catch(ParseException ex){
            ex.printStackTrace();
            return 0;
        }
    }

    String getMinimumLogDate(){
        String minimum_log_date;
        try {
            minimum_log_date = appDatabase.appVariablesDao().getMinimumLogDate("1");
        } catch (Exception e) {
            e.printStackTrace();
            minimum_log_date = "2020-05-15";
        }
        if (minimum_log_date == null || minimum_log_date.equalsIgnoreCase("") ){
            minimum_log_date = "2020-05-15";
        }
        return minimum_log_date;
    }

    String getMaximumScheduleDate(){
        String maximum_schedule_date;
        try {
            maximum_schedule_date = appDatabase.appVariablesDao().getMaximumScheduleDate("1");
        } catch (Exception e) {
            e.printStackTrace();
            maximum_schedule_date = "2020-12-31";
        }
        if (maximum_schedule_date == null || maximum_schedule_date.equalsIgnoreCase("") ){
            maximum_schedule_date = "2020-12-31";
        }
        return maximum_schedule_date;
    }

    String getLuxandFlag(){
        String luxand_flag;
        try {
            luxand_flag = appDatabase.appVariablesDao().getLuxandFlag("1");
        } catch (Exception e) {
            e.printStackTrace();
            luxand_flag = "0";
        }
        if (luxand_flag == null || luxand_flag.equalsIgnoreCase("") ){
            luxand_flag = "0";
        }
        return luxand_flag;
    }

    private void showDialogForThresher(Context context,
                                       ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                       FieldListRecyclerModel fieldListRecyclerModel) {
        MaterialAlertDialogBuilder builder = (new MaterialAlertDialogBuilder(context));
        showDialogForThresherBody(builder, context, threshingFieldListRecyclerModel, fieldListRecyclerModel);
    }

    private void showDialogForThresherBody(MaterialAlertDialogBuilder builder, Context context, ThreshingFieldListRecyclerModel threshingFieldListRecyclerModel,
                                           FieldListRecyclerModel fieldListRecyclerModel) {

        builder.setTitle(context.getResources().getString(R.string.thresher_title))
                .setIcon(context.getResources().getDrawable(R.drawable.ic_smiley_face))
                .setMessage(context.getResources().getString(R.string.thresher_question))
                .setPositiveButton(context.getResources().getString(R.string.thresher_bg), (dialog, which) -> {
                    //this is to dismiss the dialog
                    dialog.dismiss();
                    sharedPrefs.setKeyThresher("BG");
                    if (getLuxandFlag().equalsIgnoreCase("0")){
                        Intent intent = new Intent (context, ReVerifyActivity.class);
                        sharedPrefs.setKeyThreshingUniqueFieldId(threshingFieldListRecyclerModel.getUnique_field_id());
                        sharedPrefs.setKeyThreshingFieldDetails(fieldListRecyclerModel);
                        sharedPrefs.setKeyThreshingCropType(fieldListRecyclerModel.getCrop_type());
                        sharedPrefs.setKeyThreshingIkNumber(fieldListRecyclerModel.getIk_number());
                        context.startActivity(intent);
                    }else{
                        Intent intent = new Intent (context, ThreshingDateSelectionActivity.class);
                        sharedPrefs.setKeyThreshingUniqueFieldId(threshingFieldListRecyclerModel.getUnique_field_id());
                        sharedPrefs.setKeyThreshingFieldDetails(fieldListRecyclerModel);
                        sharedPrefs.setKeyThreshingCropType(fieldListRecyclerModel.getCrop_type());
                        sharedPrefs.setKeyThreshingIkNumber(fieldListRecyclerModel.getIk_number());
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.thresher_self), (dialog, which) -> {
                    dialog.dismiss();
                    sharedPrefs.setKeyThresher("Self");

                    confirmStatus(builder,context,fieldListRecyclerModel);


                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

    //this function asks the bgt if they are
    void confirmStatus(MaterialAlertDialogBuilder builder, Context context, FieldListRecyclerModel fieldListRecyclerModel){
        GPSController.LocationGetter locationGetter;
        locationGetter = GPSController.initialiseLocationListener(context);
        double latitude = locationGetter.getLatitude();
        double longitude = locationGetter.getLongitude();

        builder.setTitle(context.getResources().getString(R.string.thresher_title))
                .setIcon(context.getResources().getDrawable(R.drawable.ic_smiley_face))
                .setMessage(context.getResources().getString(R.string.thresher_question2))
                .setPositiveButton(context.getResources().getString(R.string.yes), (dialog, which) -> {
                    dialog.dismiss();

                    List<String> fields  =  new ArrayList<>();

                        appDatabase.scheduleThreshingActivitiesFlagDao().insert(new ScheduledThreshingActivitiesFlag(
                                        fieldListRecyclerModel.getUnique_field_id(),
                                        "Self",
                                        "0",
                                        sharedPrefs.getKeyThreshingTemplate(),
                                        "0000-00-00",
                                        "None",
                                        "None",
                                        getDeviceID(),
                                        BuildConfig.VERSION_NAME,
                                        latitude+"",
                                        longitude+"",
                                        sharedPrefs.getStaffID(),
                                        getDate("spread"),
                                        "0",
                                        "XXX",
                                        sharedPrefs.getKeyThreshingIkNumber(),
                                        "0",
                                        "1",
                                        "0"
                                )
                        );

                        appDatabase.logsDao().insert(new Logs(  fieldListRecyclerModel.getUnique_field_id() , sharedPrefs.getStaffID(),
                                "Schedule threshing", getDate("normal"), sharedPrefs.getStaffRole(),
                                latitude+"", longitude+"", getDeviceID(), "0",
                                sharedPrefs.getKeyThreshingIkNumber(), "Maize"));


                    context.startActivity(new Intent(context, ThreshingActivity.class));
                })
                .setNegativeButton(context.getResources().getString(R.string.no), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setNeutralButton(context.getResources().getString(R.string.cancel), (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }
}

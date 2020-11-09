package com.babbangona.mspalybookupgrade.data.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.babbangona.mspalybookupgrade.RecyclerAdapters.FieldListRecycler.FieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.HGFieldListRecycler.HGFieldListRecyclerModel;
import com.babbangona.mspalybookupgrade.RecyclerAdapters.PWSFieldListRecycler.PWSFieldListRecyclerModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPrefs {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    //TO-DO: CHANGE THIS TO YOUR SHARED PREFS NEED
    private static final String PREF_NAME = "AndroidHivePref";


    //TODO: CHANGE THESE TO THE SPECIFIED STRINGS YOU NEED
    public static final String KEY_STAFF_NAME                       = "staff_name";
    public static final String KEY_STAFF_ID                         = "staff_id";
    public static final String KEY_STAFF_ROLE                       = "staff_role";
    public static final String KEY_STAFF_PROGRAM                    = "staff_program";

    //add additional string below this comment
    public static final String KEY_APP_LANGUAGE                     = "app_language";
    public static final String KEY_FIRST_TIME_DATA_FLAG             = "first_time_data_flag";
    public static final String KEY_FIRST_UPDATE_FLAG                = "first_time_update_flag";
    public static final String KEY_SECOND_UPDATE_FLAG               = "second_time_update_flag";
    public static final String KEY_PORTFOLIO_LIST                   = "portfolio_list";
    public static final String KEY_ADDED_PORTFOLIO_LIST             = "added_portfolio_list";
    public static final String KEY_ADAPTER_OFFSET                   = "adapter_offset";
    public static final String KEY_ADAPTER_OFFSET_1                 = "adapter_offset_1";
    public static final String KEY_ACTIVITY_TYPE                    = "activity_type";

    public static final String KEY_ROUTE_TYPE                       = "route_type";

    public static final String KEY_GRID_MIN_LAT                     = "min_lat";
    public static final String KEY_GRID_MAX_LAT                     = "max_lat";
    public static final String KEY_GRID_MIN_LNG                     = "min_lng";
    public static final String KEY_GRID_MAX_LNG                     = "max_lng";


    public static final String KEY_SEARCH_UNIQUE_FIELD_ID           = "unique_field_id";
    public static final String KEY_SEARCH_IK_NUMBER                 = "ik_number";
    public static final String KEY_SEARCH_MEMBER_ID                 = "member_id";
    public static final String KEY_SEARCH_MEMBER_NAME               = "member_name";
    public static final String KEY_SEARCH_VILLAGE                   = "village";
    public static final String KEY_ADAPTER_OFFSET_FIELD_LIST        = "adapter_offset_field_list";
    public static final String KEY_ADAPTER_OFFSET_HG_FIELD_LIST     = "adapter_offset_hg_field_list";

    public static final String KEY_PROGRESS_DIALOG_STATUS           = "progress_dialog_status";
    public static final String KEY_HARVEST_CC_UNIQUE_FIELD_ID       = "harvest_cc_unique_field_id";
    public static final String KEY_HARVEST_CC_IK_NUMBER             = "harvest_cc_ik_number";
    public static final String KEY_HARVEST_CC_CROP_TYPE             = "harvest_cc_crop_type";

    public static final String KEY_HG_FIELD_MODEL                   = "clearance_list";
    public static final String KEY_FIELD_MODEL                      = "field_model";
    public static final String KEY_IMAGE_CAPTURE_FLAG               = "image_capture_flag";

    public static final String KEY_PWS_FIELD_MODEL                  = "PWS_list";
    public static final String KEY_ADAPTER_OFFSET_PWS_LIST          = "adapter_offset_pws";
    public static final String KEY_PWS_CAPTURE_LIST                 = "pws_capture_list";

    public static final String KEY_PWS_ID                           = "pws_id";
    public static final String KEY_PWS_VIEW_ROLE                    = "pws_view_role";
    public static final String KEY_PC_PWS_HOME_STAFF_ID             = "pc_pws_home_staff_id";

    public static final String KEY_THRESHING_UNIQUE_MEMBER_ID       = "threshing_unique_member_id";
    public static final String KEY_THRESHING_UNIQUE_FIELD_ID        = "threshing_unique_field_id";
    public static final String KEY_THRESHING_ACTIVITY_ROUTE         = "threshing_activity_route";
    public static final String KEY_MEMBER_VILLAGE_LATITUDE          = "member_village_latitude";
    public static final String KEY_MEMBER_VILLAGE_LONGITUDE         = "member_village_longitude";
    public static final String KEY_THRESHING_TEMPLATE               = "threshing_template";
    public static final String KEY_THRESHING_PICTURE                = "threshing_picture";
    public static final String KEY_THRESHING_RECAPTURE_FLAG         = "threshing_recapture_flag";
    public static final String KEY_THRESHING_IK_NUMBER              = "threshing_ik_number";
    public static final String KEY_THRESHING_CROP_TYPE              = "threshing_crop_type";
    public static final String KEY_THRESHING_FIELD_DETAILS          = "threshing_field_details";
    public static final String KEY_THRESHER                         = "thresher";
    public static final String KEY_SWAP_FIELD_ID                    = "swap_field_id";
    public static final String KEY_RESCHEDULE_STATE_FLAG            = "reschedule_state_flag";
    public static final String KEY_THRESHING_THRESH_VALUE           = "hg_thresh_value";
    public static final String KEY_THRESHER_ID                      = "thresher_id";

    public static final String KEY_FERTILIZER_SIGN_UP_IK_NUMBER     = "fertilizer_sign_up_ik_number";
    public static final String KEY_FERTILIZER_SIGN_UP_MEMBER_ID     = "fertilizer_sign_up_unique_member_id";
    public static final String KEY_FERTILIZER_RECAPTURE_FLAG        = "fertilizer_recapture_flag";
    public static final String KEY_FERTILIZER_TEMPLATE              = "fertilizer_template";
    public static final String KEY_FERTILIZER_PICTURE               = "fertilizer_picture";
    public static final String KEY_FERTILIZER_MEMBER_PRESENCE       = "member_presence";
    public static final String KEY_IMEI                             = "imei";

    public static final String KEY_HARVEST_SUMMARY_IK_NUMBER                        = "harvest_summary_ik_number";
    public static final String INDIVIDUAL_KEY_HARVEST_SUMMARY_NAME                  = "individual_harvest_summary_name";
    public static final String INDIVIDUAL_KEY_HARVEST_SUMMARY_IK_NUMBER             = "individual_harvest_summary_ik_number";
    public static final String INDIVIDUAL_KEY_HARVEST_SUMMARY_UNIQUE_MEMBER_ID      = "individual_harvest_summary_unique_member_id";

    public static final String KEY_LAST_SYNC_TIME                                     = "last_sync_time";
    public static final String KEY_COLLECTION_CENTER_LAST_SYNC_TIME                   = "collection_center_last_sync_time";

    public static final String KEY_AUTO_SYNC_FLAG                   = "auto_sync_flag";

    // Constructor
    public SharedPrefs(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setKeyThresherId(String thresher_id){
        editor.putString(KEY_THRESHER_ID, thresher_id)
                .commit();
    }

    public void setKeyAutoSyncFlag(int auto_sync_flag){
        editor.putInt(KEY_AUTO_SYNC_FLAG, auto_sync_flag)
                .commit();
    }

    public void setKeyThreshingThreshValue(String hg_thresh_value){
        editor.putString(KEY_THRESHING_THRESH_VALUE, hg_thresh_value)
                .commit();
    }

    public void setKeyFertilizerMemberPresence(String member_presence){
        editor.putString(KEY_FERTILIZER_MEMBER_PRESENCE, member_presence)
                .commit();
    }

    public void setKeyFertilizerPicture(String fertilizer_picture){
        editor.putString(KEY_FERTILIZER_PICTURE, fertilizer_picture)
                .commit();
    }

    public void setKeyFertilizerTemplate(String fertilizer_template){
        editor.putString(KEY_FERTILIZER_TEMPLATE, fertilizer_template)
                .commit();
    }

    public void setKeyFertilizerRecaptureFlag(String fertilizer_recapture_flag){
        editor.putString(KEY_FERTILIZER_RECAPTURE_FLAG, fertilizer_recapture_flag)
                .commit();
    }

    public void setKeyFertilizerSignUpMemberId(String fertilizer_sign_up_unique_member_id){
        editor.putString(KEY_FERTILIZER_SIGN_UP_MEMBER_ID, fertilizer_sign_up_unique_member_id)
                .commit();
    }

    public void setKeyFertilizerSignUpIkNumber(String fertilizer_sign_up_ik_number){
        editor.putString(KEY_FERTILIZER_SIGN_UP_IK_NUMBER, fertilizer_sign_up_ik_number)
                .commit();
    }

    public void setKeyRescheduleStateFlag(String reschedule_state_flag){
        editor.putString(KEY_RESCHEDULE_STATE_FLAG, reschedule_state_flag)
                .commit();
    }

    public void setKeySwapFieldId(String swap_field_id){
        editor.putString(KEY_SWAP_FIELD_ID, swap_field_id)
                .commit();
    }

    public void setKeyThresher(String thresher){
        editor.putString(KEY_THRESHER, thresher)
                .commit();
    }

    public void setKeyThreshingFieldDetails(FieldListRecyclerModel fieldListRecyclerModel){
        Gson gson = new Gson();
        String json = gson.toJson(fieldListRecyclerModel);
        // Storing model in pref
        editor.putString(KEY_THRESHING_FIELD_DETAILS, json);

        // commit changes
        editor.commit();
    }

    public void setKeyThreshingCropType(String threshing_crop_type){
        editor.putString(KEY_THRESHING_CROP_TYPE, threshing_crop_type)
                .commit();
    }

    public void setKeyThreshingIkNumber(String threshing_ik_number){
        editor.putString(KEY_THRESHING_IK_NUMBER, threshing_ik_number)
                .commit();
    }

    public void setKeyThreshingRecaptureFlag(String threshing_recapture_flag){
        editor.putString(KEY_THRESHING_RECAPTURE_FLAG, threshing_recapture_flag)
                .commit();
    }

    public void setKeyThreshingPicture(String threshing_picture){
        editor.putString(KEY_THRESHING_PICTURE, threshing_picture)
                .commit();
    }

    public void setKeyThreshingTemplate(String threshing_template){
        editor.putString(KEY_THRESHING_TEMPLATE, threshing_template)
                .commit();
    }

    public void setKeyVillageLocation(String latitude, String longitude){
        editor.putString(KEY_MEMBER_VILLAGE_LATITUDE, latitude)
                .putString(KEY_MEMBER_VILLAGE_LONGITUDE, longitude)
                .commit();
    }

    public void setKeyThreshingUniqueFieldId(String threshing_unique_field_id){

        editor.putString(KEY_THRESHING_UNIQUE_FIELD_ID, threshing_unique_field_id);

        // commit changes
        editor.commit();
    }

    public void setKeyThreshingActivityRoute(String threshing_activity_route){

        editor.putString(KEY_THRESHING_ACTIVITY_ROUTE, threshing_activity_route);

        // commit changes
        editor.commit();
    }

    public void setKeyThreshingUniqueMemberId(String threshing_unique_member_id){

        editor.putString(KEY_THRESHING_UNIQUE_MEMBER_ID, threshing_unique_member_id);

        // commit changes
        editor.commit();
    }

    public void setKeySecondUpdateFlag(String second_time_update_flag){

        editor.putString(KEY_SECOND_UPDATE_FLAG, second_time_update_flag);

        // commit changes
        editor.commit();
    }

    public void setKeyPcPwsHomeStaffId(String pc_pws_home_staff_id){

        editor.putString(KEY_PC_PWS_HOME_STAFF_ID, pc_pws_home_staff_id);

        // commit changes
        editor.commit();
    }

    public void setKeyPWSViewRole(String pws_view_role){

        editor.putString(KEY_PWS_VIEW_ROLE, pws_view_role);

        // commit changes
        editor.commit();
    }

    public void setKeyPwsId(String pws_id){

        editor.putString(KEY_PWS_ID, pws_id);

        // commit changes
        editor.commit();
    }

    public void setKeyPWSCaptureList(Set<String> pws_capture_list){

        editor.putStringSet(KEY_PWS_CAPTURE_LIST, pws_capture_list);

        // commit changes
        editor.commit();
    }

    public void setKeyImageCaptureFlag(String image_capture_flag){
        // Storing name in pref
        editor.putString(KEY_IMAGE_CAPTURE_FLAG, image_capture_flag);

        // commit changes
        editor.commit();
    }

    public void setKeyFieldModel(FieldListRecyclerModel fieldListRecyclerModel){
        Gson gson = new Gson();
        String json = gson.toJson(fieldListRecyclerModel);
        // Storing model in pref
        editor.putString(KEY_FIELD_MODEL, json);

        // commit changes
        editor.commit();
    }

    public void setKeyHgFieldModel(HGFieldListRecyclerModel hgFieldListRecyclerModel){
        Gson gson = new Gson();
        String json = gson.toJson(hgFieldListRecyclerModel);
        // Storing model in pref
        editor.putString(KEY_HG_FIELD_MODEL, json);

        // commit changes
        editor.commit();
    }

    public void setKeyPWSFieldModel(PWSFieldListRecyclerModel pwsFieldListRecyclerModel){
        Gson gson = new Gson();
        String json = gson.toJson(pwsFieldListRecyclerModel);
        // Storing model in pref
        editor.putString(KEY_PWS_FIELD_MODEL, json);

        // commit changes
        editor.commit();
    }

    public void setKeyHarvestCcProperties(String harvest_cc_unique_field_id, String harvest_cc_ik_number,
                                             String harvest_cc_crop_type){
        // Storing role in pref
        editor.putString(KEY_HARVEST_CC_UNIQUE_FIELD_ID, harvest_cc_unique_field_id);
        editor.putString(KEY_HARVEST_CC_IK_NUMBER, harvest_cc_ik_number);
        editor.putString(KEY_HARVEST_CC_CROP_TYPE, harvest_cc_crop_type);


        // commit changes
        editor.commit();
    }

    public void setKeyProgressDialogStatus(int progress_dialog_status){
        // Storing role in pref
        editor.putInt(KEY_PROGRESS_DIALOG_STATUS, progress_dialog_status);


        // commit changes
        editor.commit();
    }

    public void setKeyAdapterOffsetHgFieldList(int offset){
        // Storing role in pref
        editor.putInt(KEY_ADAPTER_OFFSET_HG_FIELD_LIST, offset);


        // commit changes
        editor.commit();
    }

    public void setKeyAdapterOffsetFieldList(int offset){
        // Storing role in pref
        editor.putInt(KEY_ADAPTER_OFFSET_FIELD_LIST, offset);


        // commit changes
        editor.commit();
    }

    public void setKeySearchParameters(String unique_field_id, String ik_number, String member_id,
                                       String member_name, String village){
        editor.putString(KEY_SEARCH_UNIQUE_FIELD_ID, unique_field_id);
        editor.putString(KEY_SEARCH_IK_NUMBER, ik_number);
        editor.putString(KEY_SEARCH_MEMBER_ID, member_id);
        editor.putString(KEY_SEARCH_MEMBER_NAME, member_name);
        editor.putString(KEY_SEARCH_VILLAGE, village);
        editor.commit();
    }

    public void setKeyGridParameters(String min_lat, String max_lat, String min_lng, String max_lng){
        editor.putString(KEY_GRID_MIN_LAT, min_lat);
        editor.putString(KEY_GRID_MAX_LAT, max_lat);
        editor.putString(KEY_GRID_MIN_LNG, min_lng);
        editor.putString(KEY_GRID_MAX_LNG, max_lng);
        editor.commit();
    }

    public void setKeyRouteType(String route_type){
        editor.putString(KEY_ROUTE_TYPE, route_type);
        editor.commit();
    }

    public void setKeyActivityType(String activity_type){
        editor.putString(KEY_ACTIVITY_TYPE, activity_type);
        editor.commit();

    }

    public void setKeyAdapterOffset1(int offset){
        // Storing role in pref
        editor.putInt(KEY_ADAPTER_OFFSET_1, offset);


        // commit changes
        editor.commit();
    }

    public void setKeyAdapterOffsetPwsList(int offset){
        // Storing role in pref
        editor.putInt(KEY_ADAPTER_OFFSET_PWS_LIST, offset);


        // commit changes
        editor.commit();
    }

    public void setKeyAdapterOffset(int offset){
        // Storing role in pref
        editor.putInt(KEY_ADAPTER_OFFSET, offset);


        // commit changes
        editor.commit();
    }

    public void setKeyAddedPortfolioList(Set<String> added_portfolio_list){
        // Storing username in pref

        // Storing role in pref
        editor.putStringSet(KEY_ADDED_PORTFOLIO_LIST, added_portfolio_list);


        // commit changes
        editor.commit();
    }

    public void setKeyPortfolioList(Set<String> portfolio_list){
        // Storing username in pref

        // Storing role in pref
        editor.putStringSet(KEY_PORTFOLIO_LIST, portfolio_list);


        // commit changes
        editor.commit();
    }

    public void setKeyFirstTimeDataFlag(String first_time_data_flag){
        editor.putString(KEY_FIRST_TIME_DATA_FLAG, first_time_data_flag);
        editor.commit();
    }

    public void setKeyFirstUpdateFlag(String first_time_update_flag){
        editor.putString(KEY_FIRST_UPDATE_FLAG, first_time_update_flag);
        editor.commit();
    }

    public void setKeyAppLanguage(String app_language){
        editor.putString(KEY_APP_LANGUAGE, app_language);
        editor.commit();
    }

    public void setKeyIndividualHarvestSummaryName(String individualHarvestSummaryName){
        editor.putString(INDIVIDUAL_KEY_HARVEST_SUMMARY_NAME, individualHarvestSummaryName);
        editor.commit();
    }

    public void setKeyHarvestSummaryIkNumber(String harvestSummaryIkNumber){
        editor.putString(KEY_HARVEST_SUMMARY_IK_NUMBER, harvestSummaryIkNumber);
        editor.commit();
    }

    public void setKeyIndividualHarvestSummaryIkNumber(String individualHarvestSummaryIkNumber){
        editor.putString(INDIVIDUAL_KEY_HARVEST_SUMMARY_IK_NUMBER, individualHarvestSummaryIkNumber);
        editor.commit();
    }

    public void setKeyIndividualHarvestSummaryUniqueMemberId(String individualHarvestSummaryUniqueMemberId){
        editor.putString(INDIVIDUAL_KEY_HARVEST_SUMMARY_UNIQUE_MEMBER_ID, individualHarvestSummaryUniqueMemberId);
        editor.commit();
    }

    public void setKeyLastSyncTime(String temp) {
        editor.putString(KEY_LAST_SYNC_TIME, temp);
        editor.commit();
    }

    public void setKeyCollectionCenterLastSyncTime(String temp) {
        editor.putString(KEY_COLLECTION_CENTER_LAST_SYNC_TIME, temp);
        editor.commit();
    }


    /**
     *      Do not edit anything below
     * ____________________________________________________________________________________________
     */

    //THIS IMPLEMETATION SAVES STAFF DETAILS TO SHARED PREFERENCE
    //THIS DATA COMES FROM ACCESS CONTROL AND IT IS SAVED HERE

    public void CreateLoginSession(String staffname,
                                   String staffid,
                                   String staff_role,
                                   String staff_program){

        editor.putString(KEY_STAFF_NAME, staffname);
        editor.putString(KEY_STAFF_ID, staffid);
        editor.putString(KEY_STAFF_ROLE, staff_role);
        editor.putString(KEY_STAFF_PROGRAM, staff_program);

        editor.commit();
    }


    //This method returns all the data partaining to the staff

    public HashMap<String, String> getAllStaffData(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_STAFF_NAME, pref.getString(KEY_STAFF_NAME,     "cannot_find"));
        user.put(KEY_STAFF_ID, pref.getString(KEY_STAFF_ID,         "cannot_find"));
        user.put(KEY_STAFF_ROLE,pref.getString(KEY_STAFF_ROLE,      "cannot_find"));
        user.put(KEY_STAFF_PROGRAM,pref.getString(KEY_STAFF_PROGRAM,"cannot_find"));

        return user;
    }

    public String getStaffID(){      return pref.getString(KEY_STAFF_ID,"cannot_find"); }
    public String getStaffName(){    return pref.getString(KEY_STAFF_NAME,"cannot_find"); }
    public String getStaffRole(){    return pref.getString(KEY_STAFF_ROLE ,"cannot_find"); }
    public String getStaffProgram(){ return pref.getString(KEY_STAFF_PROGRAM ,"cannot_find");}

    /**
     *      Do not edit anything above
     * __________________________________________________________________________________
     *  *
     */

    //TODO: Add additional parameters below this line
    public String getKeyAppLanguage(){
        return pref.getString(KEY_APP_LANGUAGE ,"en");
    }

    public String getKeyFirstTimeDataFlag(){
        return pref.getString(KEY_FIRST_TIME_DATA_FLAG ,"0");
    }

    public String getKeyFirstUpdateFlag(){
        return pref.getString(KEY_FIRST_UPDATE_FLAG ,"0");
    }

    public Set<String > getKeyPortfolioList(){
        return pref.getStringSet(KEY_PORTFOLIO_LIST, new HashSet<>());
    }

    public Set<String > getKeyAddedPortfolioList(){
        return pref.getStringSet(KEY_ADDED_PORTFOLIO_LIST, new HashSet<>());
    }

    public int getKeyAdapterOffset(){
        return pref.getInt(KEY_ADAPTER_OFFSET ,0);
    }

    public int getKeyAdapterOffset1(){
        return pref.getInt(KEY_ADAPTER_OFFSET_1 ,0);
    }

    public String getKeyActivityType(){
        return pref.getString(KEY_ACTIVITY_TYPE,"8");
    }

    public String getKeyRouteType(){
        return pref.getString(KEY_ROUTE_TYPE,"0");
    }

    public String getKeyGridMinLat(){
        return pref.getString(KEY_GRID_MIN_LAT,"");
    }

    public String getKeyGridMaxLat(){
        return pref.getString(KEY_GRID_MAX_LAT,"");
    }

    public String getKeyGridMinLng(){
        return pref.getString(KEY_GRID_MIN_LNG,"");
    }

    public String getKeyGridMaxLng(){
        return pref.getString(KEY_GRID_MAX_LNG,"");
    }

    public String getKeySearchUniqueFieldId(){
        return pref.getString(KEY_SEARCH_UNIQUE_FIELD_ID,"");
    }

    public String getKeySearchIkNumber(){
        return pref.getString(KEY_SEARCH_IK_NUMBER,"");
    }

    public String getKeySearchMemberId(){
        return pref.getString(KEY_SEARCH_MEMBER_ID,"");
    }

    public String getKeySearchMemberName(){
        return pref.getString(KEY_SEARCH_MEMBER_NAME,"");
    }

    public String getKeySearchVillage(){
        return pref.getString(KEY_SEARCH_VILLAGE,"");
    }

    public int getKeyAdapterOffsetFieldList(){
        return pref.getInt(KEY_ADAPTER_OFFSET_FIELD_LIST ,0);
    }

    public int getKeyAdapterOffsetHgFieldList(){
        return pref.getInt(KEY_ADAPTER_OFFSET_HG_FIELD_LIST ,0);
    }

    public int getKeyProgressDialogStatus(){
        return pref.getInt(KEY_PROGRESS_DIALOG_STATUS ,1);
    }

    public String getKeyHarvestCcUniqueFieldId(){
        return pref.getString(KEY_HARVEST_CC_UNIQUE_FIELD_ID,"0");
    }

    public String getKeyHarvestCcIkNumber(){
        return pref.getString(KEY_HARVEST_CC_IK_NUMBER,"None");
    }

    public String getKeyHarvestCcCropType(){
        return pref.getString(KEY_HARVEST_CC_CROP_TYPE,"None");

    }

    public HGFieldListRecyclerModel getKeyHgFieldModel() {
        Gson gson = new Gson();
        String json = pref.getString(KEY_HG_FIELD_MODEL, "");
        return gson.fromJson(json, HGFieldListRecyclerModel.class);
    }

    public FieldListRecyclerModel getKeyFieldModel() {
        Gson gson = new Gson();
        String json = pref.getString(KEY_FIELD_MODEL, "");
        return gson.fromJson(json, FieldListRecyclerModel.class);
    }

    public String getKeyImageCaptureFlag(){
        return pref.getString(KEY_IMAGE_CAPTURE_FLAG,"0");
    }

    public PWSFieldListRecyclerModel getKeyPWSFieldModel() {
        Gson gson = new Gson();
        String json = pref.getString(KEY_PWS_FIELD_MODEL, "");
        return gson.fromJson(json, PWSFieldListRecyclerModel.class);
    }

    public int getKeyAdapterOffsetPWSList(){
        return pref.getInt(KEY_ADAPTER_OFFSET_PWS_LIST ,0);
    }

    public Set<String> getKeyPwsCaptureList() {
        return pref.getStringSet(KEY_PWS_CAPTURE_LIST, new HashSet<>());
    }

    public String getKeyPwsId() {
        return pref.getString(KEY_PWS_ID,"0");

    }

    public String getKeyPwsViewRole() {
        return pref.getString(KEY_PWS_VIEW_ROLE,"mik");

    }

    public String getKeyPcPwsHomeStaffId() {
        return pref.getString(KEY_PC_PWS_HOME_STAFF_ID,"nada");

    }

    public String getKeySecondUpdateFlag() {
        return pref.getString(KEY_SECOND_UPDATE_FLAG,"0");

    }

    public String getKeyThreshingUniqueMemberId() {
        return pref.getString(KEY_THRESHING_UNIQUE_MEMBER_ID,"0");

    }

    public String getKeyThreshingActivityRoute() {
        return pref.getString(KEY_THRESHING_ACTIVITY_ROUTE,"0");

    }

    public String getKeyThreshingUniqueFieldId() {
        return pref.getString(KEY_THRESHING_UNIQUE_FIELD_ID,"0");

    }

    public String getKeyMemberVillageLatitude() {
        return pref.getString(KEY_MEMBER_VILLAGE_LATITUDE, "0.00");
    }

    public String getKeyMemberVillageLongitude() {
        return pref.getString(KEY_MEMBER_VILLAGE_LONGITUDE, "0.00");
    }

    public String getKeyThreshingTemplate() {
        return pref.getString(KEY_THRESHING_TEMPLATE,"XXX");

    }

    public String getKeyThreshingPicture() {
        return pref.getString(KEY_THRESHING_PICTURE, "XXX");
    }

    public String getKeyThreshingRecaptureFlag() {
        return pref.getString(KEY_THRESHING_RECAPTURE_FLAG, "0");
    }

    public String getKeyThreshingIkNumber() {
        return pref.getString(KEY_THRESHING_IK_NUMBER, "0");
    }

    public String getKeyThreshingCropType() {
        return pref.getString(KEY_THRESHING_CROP_TYPE, "0");
    }

    public FieldListRecyclerModel getKeyThreshingFieldDetails() {
        Gson gson = new Gson();
        String json = pref.getString(KEY_THRESHING_FIELD_DETAILS, "");
        return gson.fromJson(json, FieldListRecyclerModel.class);
    }

    public String getKeyThresher() {
        return pref.getString(KEY_THRESHER, "BG");
    }

    public String getKeySwapFieldId() {
        return pref.getString(KEY_SWAP_FIELD_ID, "");
    }

    public String getKeyRescheduleStateFlag() {
        return pref.getString(KEY_RESCHEDULE_STATE_FLAG, "0");
    }

    public String getKeyIndividualHarvestSummaryName(){
        return pref.getString(INDIVIDUAL_KEY_HARVEST_SUMMARY_NAME,"");
    }

    public String getKeyHarvestSummaryIkNumber(){
        return pref.getString(KEY_HARVEST_SUMMARY_IK_NUMBER,"");
    }

    public String getKeyIndividualHarvestSummaryIkNumber(){
        return pref.getString(INDIVIDUAL_KEY_HARVEST_SUMMARY_IK_NUMBER,"");
    }

    public String getKeyIndividualHarvestSummaryUniqueMemberId(){
        return pref.getString(INDIVIDUAL_KEY_HARVEST_SUMMARY_UNIQUE_MEMBER_ID,"");
    }

    public String getKeyLastSyncTime() {
        return pref.getString(KEY_LAST_SYNC_TIME, "2020-06-28 00:00:00");
    }

    public String getCollectionCenterLastSyncTime() {
        return pref.getString(KEY_COLLECTION_CENTER_LAST_SYNC_TIME, "2018-01-01 00:00:00");
    }

    public String getKeyFertilizerSignUpIkNumber() {
        return pref.getString(KEY_FERTILIZER_SIGN_UP_IK_NUMBER, "IKSomething");
    }

    public String getKeyFertilizerSignUpMemberId() {
        return pref.getString(KEY_FERTILIZER_SIGN_UP_MEMBER_ID, "");
    }

    public String getKeyFertilizerRecaptureFlag() {
        return pref.getString(KEY_FERTILIZER_RECAPTURE_FLAG, "0");
    }

    public String getKeyFertilizerTemplate() {
        return pref.getString(KEY_FERTILIZER_TEMPLATE, "XXX");
    }

    public String getKeyFertilizerPicture() {
        return pref.getString(KEY_FERTILIZER_PICTURE, "XXX");
    }

    public String getKeyFertilizerMemberPresence() {
        return pref.getString(KEY_FERTILIZER_MEMBER_PRESENCE, "0");
    }

    public String getKeyThreshingThreshValue() {
        return pref.getString(KEY_THRESHING_THRESH_VALUE, "XX");
    }
    public void setIMEI(String deviceId) {
        editor.putString(KEY_IMEI, deviceId);
        // commit changes
        editor.commit();
    }

    public String getIMEI(){
        return pref.getString(KEY_IMEI, null);
    }

    public int getKeyAutoSyncFlag() {
        return pref.getInt(KEY_AUTO_SYNC_FLAG, 0);
    }

    public String getKeyThresherId() {
        return pref.getString(KEY_THRESHER_ID, "");
    }
}

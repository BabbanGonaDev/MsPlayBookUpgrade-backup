package com.babbangona.mspalybookupgrade.data.constants;

public class DatabaseStringConstants {

    /**
     * database particulars
     */
    public static final String MS_PLAYBOOK_DATABASE_NAME                = "ms_playbook_upgrade.db";
    public static final int MS_PLAYBOOK_DATABASE_VERSION                = 13;

    /**
     * Names of tables
     */
    public static final String ACTIVITY_LIST_TABLE                          = "activity_list";
    public static final String FIELDS_TABLE                                 = "fields";
    public static final String STAFF_TABLE                                  = "staff";
    public static final String MEMBERS_TABLE                                = "members";
    public static final String HG_LIST_TABLE                                = "hg_list";
    public static final String RF_LIST_TABLE                                = "rf_list";
    public static final String NORMAL_ACTIVITY_FLAGS_TABLE                  = "normal_activities_flag";
    public static final String HG_ACTIVITY_FLAGS_TABLE                      = "hg_activities_flag";
    public static final String RF_ACTIVITY_FLAGS_TABLE                      = "rf_activities_flag";
    public static final String LOGS_TABLE                                   = "logs";
    public static final String LAST_SYNC_TABLE                              = "last_sync";
    public static final String CATEGORY_TABLE                               = "category";
    public static final String PWS_ACTIVITY_CONTROLLER_TABLE                = "pws_activity_controller";
    public static final String SYNC_SUMMARY_TABLE                           = "sync_summary";
    public static final String HARVEST_LOCATION_TABLE                       = "harvest_location";
    public static final String APP_VARIABLES                                = "app_variables";
    public static final String TABLE_PICTURE_SYNC                           = "picture_sync";
    public static final String PWS_ACTIVITY_FLAGS_TABLE                     = "pws_activities_flag";
    public static final String PC_PWS_ACTIVITY_FLAGS_TABLE                  = "pc_pws_activities_flag";
    public static final String PWS_CATEGORY_LIST_TABLE                      = "pws_category_list";
    public static final String SCHEDULE_THRESHING_ACTIVITIES_FLAG_TABLE     = "schedule_threshing_activities_flag";
    public static final String CONFIRM_THRESHING_ACTIVITIES_FLAG_TABLE      = "confirm_threshing_activities_flag";
    public static final String LOCATIONS_TABLE                              = "locations_table";
    public static final String THRESHING_LOCATION                           = "threshing_location";
    public static final String BGT_COACHES_TABLE                            = "bgt_coaches";

    /**
     * activity_list column names
     */
    public static final String COL_ACTIVITY_ID                          = "activity_id";
    public static final String COL_LANGUAGE_ID                          = "language_id";
    public static final String COL_ACTIVITY_NAME                        = "activity_name";
    public static final String COL_ACTIVITY_DESTINATION                 = "activity_destination";
    public static final String COL_ACTIVITY_PRIORITY                    = "activity_priority";
    public static final String COL_USER_CATEGORY                        = "user_category";
    public static final String COL_DEACTIVATED                          = "deactivated";

    /**
     * normal_activity_flag column names
     */
    public static final String COL_UNIQUE_FIELD_ID                      = "unique_field_id";
    public static final String COL_FERTILIZER_1_STATUS                  = "fertilizer_1_status";
    public static final String COL_FERTILIZER_1_DATE                    = "fertilizer_1_date";
    public static final String COL_FERTILIZER_2_STATUS                  = "fertilizer_2_status";
    public static final String COL_FERTILIZER_2_DATE                    = "fertilizer_2_date";
    public static final String COL_STAFF_ID                             = "staff_id";
    public static final String COL_SYNC_FLAG                            = "sync_flag";
    public static final String COL_IK_NUMBER_NORMAL_ACTIVITIES          = "ik_number";
    public static final String COL_CROP_TYPE_NORMAL_ACTIVITIES          = "crop_type";
    public static final String COL_CC_HARVEST                           = "cc_harvest";
    public static final String COL_DATE_LOGGED_NORMAL                   = "date_logged";

    /**
     * fields column names
     */
    public static final String COL_UNIQUE_FIELD_ID_FIELDS               = "unique_field_id";
    public static final String COL_UNIQUE_MEMBER_ID                     = "unique_member_id";
    public static final String COL_FIELD_SIZE                           = "field_size";
    public static final String COL_STAFF_ID_FIELDS                      = "staff_id";
    public static final String COL_MIDDLE                               = "middle";
    public static final String COL_MIN_LAT                              = "min_lat";
    public static final String COL_MAX_LAT                              = "max_lat";
    public static final String COL_MIN_LNG                              = "min_lng";
    public static final String COL_MAX_LNG                              = "max_lng";
    public static final String COL_DEACTIVATE                           = "deactivate";
    public static final String COL_MSS                                  = "mss";
    public static final String COL_CROP_TYPE_FIELDS                     = "crop_type";
    public static final String COL_CODE                                 = "field_code";

    /**
     * staff column names
     */
    public static final String COL_STAFF_ID_STAFF                       = "staff_id";
    public static final String COL_STAFF_NAME                           = "staff_name";
    public static final String COL_STAFF_HUB                            = "staff_hub";

    /**
     * members column names
     */
    public static final String COL_UNIQUE_MEMBER_ID_MEMBERS             = "unique_member_id";
    public static final String COL_IK_NUMBER                            = "ik_number";
    public static final String COL_MEMBER_ID                            = "member_id";
    public static final String COL_FIRST_NAME                           = "first_name";
    public static final String COL_LAST_NAME                            = "last_name";
    public static final String COL_PHONE_NUMBER                         = "phone_number";
    public static final String COL_VILLAGE_NAME                         = "village_name";
    public static final String COL_TEMPLATE                             = "template";
    public static final String COL_ROLE_MEMBERS                         = "role";
    public static final String COL_BGT_ID_MEMBERS                       = "bgt_id";
    public static final String COL_COACH_ID_MEMBERS                     = "coach_id";

    /**
     * hg_activities column names
     */
    public static final String COL_UNIQUE_FIELD_ID_HG_ACTIVITY          = "unique_field_id";
    public static final String COL_HG_TYPE                              = "hg_type";
    public static final String COL_HG_DATE                              = "hg_date";
    public static final String COL_HG_STATUS                            = "hg_status";
    public static final String COL_STAFF_ID_HG_ACTIVITIES               = "staff_id";
    public static final String COL_SYNC_FLAG_HG_ACTIVITIES              = "sync_flag";
    public static final String COL_IK_NUMBER_HG_ACTIVITIES              = "ik_number";
    public static final String COL_CROP_TYPE_HG_ACTIVITIES              = "crop_type";
    public static final String COL_DATE_LOGGED_HG                       = "date_logged";
    public static final String COL_DESCRIPTION_HG                       = "description";

    /**
     * hg_list column names
     */
    public static final String COL_HG_TYPE_HG_LIST                      = "hg_type";
    public static final String COL_SUB_HG_TYPE_HG_LIST                  = "sub_hg_type";
    public static final String COL_HG_TYPE_DEACTIVATED_STATUS           = "deactivated_status";
    public static final String COL_USER_CATEGORY_HG_LIST                = "user_category";

    /**
     * logs column names
     */
    public static final String COL_UNIQUE_FIELD_ID_LOGS                 = "unique_field_id";
    public static final String COL_STAFF_ID_LOGS                        = "staff_id";
    public static final String COL_ACTIVITY_TYPE_LOGS                   = "activity_type";
    public static final String COL_DATE_LOGGED                          = "date_logged";
    public static final String COL_ROLE                                 = "role";
    public static final String COL_LATITUDE                             = "latitude";
    public static final String COL_LONGITUDE                            = "longitude";
    public static final String COL_IMEI                                 = "imei";
    public static final String COL_SYNC_FLAG_LOGS                       = "sync_flag";
    public static final String COL_IK_NUMBER_LOGS                       = "ik_number";
    public static final String COL_CROP_TYPE_LOGS                       = "crop_type";

    /**
     * last_sync column names
     */
    public static final String LAST_SYNC_ACTIVITY_LIST_TABLE                = "last_sync_activity_list";
    public static final String LAST_SYNC_UP_NORMAL_ACTIVITY_FLAGS_TABLE     = "last_sync_up_normal_activities_flag";
    public static final String LAST_SYNC_DOWN_NORMAL_ACTIVITY_FLAGS_TABLE   = "last_sync_down_normal_activities_flag";
    public static final String LAST_SYNC_FIELDS_TABLE                       = "last_sync_fields";
    public static final String LAST_SYNC_STAFF_TABLE                        = "last_sync_staff";
    public static final String LAST_SYNC_MEMBERS_TABLE                      = "last_sync_members";
    public static final String LAST_SYNC_UP_HG_ACTIVITY_FLAGS_TABLE         = "last_sync_up_hg_activities_flag";
    public static final String LAST_SYNC_DOWN_HG_ACTIVITY_FLAGS_TABLE       = "last_sync_down_hg_activities_flag";
    public static final String LAST_SYNC_HG_LIST_TABLE                      = "last_sync_hg_list";
    public static final String LAST_SYNC_UP_LOGS_TABLE                      = "last_sync_up_logs";
    public static final String LAST_SYNC_DOWN_LOGS_TABLE                    = "last_sync_down_logs";
    public static final String LAST_SYNC_CATEGORY_TABLE                     = "last_sync_category";
    public static final String LAST_SYNC_HARVEST_LOCATION                   = "last_sync_harvest_location";
    public static final String LAST_SYNC_RF_LIST_TABLE                      = "last_sync_rf_list";
    public static final String LAST_SYNC_UP_RF_ACTIVITY_FLAGS_TABLE         = "last_sync_up_rf_activities_flag";
    public static final String LAST_SYNC_DOWN_RF_ACTIVITY_FLAGS_TABLE       = "last_sync_down_rf_activities_flag";
    public static final String LAST_SYNC_PWS_CATEGORY_LIST_TABLE            = "last_sync_pws_category_list";
    public static final String LAST_SYNC_UP_PWS_ACTIVITY_FLAGS_TABLE        = "last_sync_up_pws_activities_flag";
    public static final String LAST_SYNC_DOWN_PWS_ACTIVITY_FLAGS_TABLE      = "last_sync_down_pws_activities_flag";
    public static final String LAST_SYNC_UP_PC_PWS_ACTIVITY_FLAGS_TABLE     = "last_sync_up_pc_pws_activities_flag";
    public static final String LAST_SYNC_DOWN_PC_PWS_ACTIVITY_FLAGS_TABLE   = "last_sync_down_pc_pws_activities_flag";
    public static final String LAST_SYNC_PWS_ACTIVITIES_CONTROLLER_TABLE    = "last_sync_pws_activities_controller";
    public static final String LAST_SYNC_UP_SCHEDULED_ACTIVITY_FLAGS_TABLE  = "last_sync_up_scheduled_activities_flag";
    public static final String LAST_SYNC_DOWN_SCHEDULED_ACTIVITY_FLAGS_TABLE= "last_sync_down_scheduled_activities_flag";
    public static final String LAST_SYNC_UP_CONFIRM_ACTIVITY_FLAGS_TABLE    = "last_sync_up_confirm_activities_flag";
    public static final String LAST_SYNC_DOWN_CONFIRM_ACTIVITY_FLAGS_TABLE  = "last_sync_down_confirm_activities_flag";
    public static final String LAST_SYNC_BGT_COACHES_TABLE                  = "last_sync_bgt_coaches";

    /**
     * category column names
     */
    public static final String COL_STAFF_ROLE                               = "role";
    public static final String COL_CATEGORY                                 = "category";

    /**
     * sync_summary column names
     */
    public static final String COL_TABLE_ID                                 = "table_id";
    public static final String COL_STAFF_ID_SYNC_SUMMARY                    = "staff_id";
    public static final String COL_TABLE_NAME                               = "table_name";
    public static final String COL_STATUS                                   = "status";
    public static final String COL_REMARKS                                  = "remarks";
    public static final String COL_SYNC_TIME                                = "sync_time";

    /**
     * sync_summary column names
     */
    public static final String COL_STATE                                    = "state";
    public static final String COL_LGA                                      = "lga";
    public static final String COL_WARD                                     = "ward";
    public static final String COL_CENTER                                   = "centre";
    public static final String COL_DEACTIVATE_HARVEST                       = "deactivate";

    /**
     * app_variables column names
     */
    public static final String VARIABLE_ID                                  = "variable_id";
    public static final String EDIT_HARVEST_LOCATION_FLAG                   = "edit_harvest_location_flag";
    public static final String MINIMUM_LOG_DATE_FLAG                        = "minimum_log_date";
    public static final String MAXIMUM_LOG_DATE_FLAG                        = "maximum_log_date";
    public static final String FIELDS_TRAVEL_TIME                           = "fields_travel_time";
    public static final String AVERAGE_TRANSITION_TIME                      = "average_transition_time";
    public static final String TIME_PER_HA                                  = "time_per_ha";
    public static final String MAXIMUM_SCHEDULE_DATE                        = "maximum_schedule_date";
    public static final String LUXAND_FLAG                                  = "luxand_flag";

    /**
     * rf_list column names
     */
    public static final String COL_RF_TYPE_RF_LIST                          = "rf_type";
    public static final String COL_RF_TYPE_DEACTIVATED_STATUS               = "deactivated_status";
    public static final String COL_USER_CATEGORY_RF_LIST                    = "user_category";

    /**
     * rf_activities column names
     */
    public static final String COL_UNIQUE_FIELD_ID_RF_ACTIVITY          = "unique_field_id";
    public static final String COL_RF_TYPE                              = "rf_type";
    public static final String COL_RF_DATE                              = "rf_date";
    public static final String COL_RF_STATUS                            = "rf_status";
    public static final String COL_STAFF_ID_RF_ACTIVITIES               = "staff_id";
    public static final String COL_SYNC_FLAG_RF_ACTIVITIES              = "sync_flag";
    public static final String COL_IK_NUMBER_RF_ACTIVITIES              = "ik_number";
    public static final String COL_CROP_TYPE_RF_ACTIVITIES              = "crop_type";
    public static final String COL_DATE_LOGGED_RF                       = "date_logged";

    /**
     * picture location
     */
    public static final String HG_ACTIVITY_PICTURE_LOCATION             = "MsPlaybookPictures/HG_Activities";
    public static final String RF_ACTIVITY_PICTURE_LOCATION             = "MsPlaybookPictures/RF_Activities";
    public static final String NORMAL_ACTIVITY_PICTURE_LOCATION         = "MsPlaybookPictures/Normal_Activities";
    public static final String PWS_ACTIVITY_PICTURE_LOCATION            = "MsPlaybookPictures/PWS_Activities";
    public static final String MEMBER_PICTURE_LOCATION                  = "MsPlaybookPictures/Recaptured_Member_Pictures";
    public static final String MS_PLAYBOOK_PICTURE_LOCATION             = "MsPlaybookPictures";
    public static final String MS_PLAYBOOK_INPUT_PICTURE_LOCATION       = "MsPlaybookInputPictures";
    public static final String FERT_1_ACTIVITY                          = "1";
    public static final String FERT_2_ACTIVITY                          = "2";
    public static final String LOG_HG_ACTIVITY                          = "3";
    public static final String SET_PORTFOLIO_ACTIVITY                   = "4";
    public static final String POOR_WEATHER_SUPPORT_ACTIVITY            = "5";
    public static final String BGT_THRESHING_ACTIVITY                   = "7";
    public static final String SCHEDULE_THRESHING                       = "1";
    public static final String UPDATE_THRESHING                         = "2";
    public static final String CONFIRM_THRESHING                        = "3";
    public static final String MARK_HG_AT_RISK                          = "4";
    public static final String SWAP_SCHEDULE_DATE                       = "5";

    /**
     * picture_sync column names
     */
    public static final String COL_PICTURE_NAME                     = "picture_name";

    /**
     * hg_activities column names
     */
    public static final String COL_PWS_ID                               = "pws_id";
    public static final String COL_UNIQUE_FIELD_ID_PWS                  = "unique_field_id";
    public static final String COL_IK_NUMBER_PWS                        = "ik_number";
    public static final String COL_PWS_AREA                             = "pws_area";
    public static final String COL_PWS_LAT_LNG                          = "lat_longs";
    public static final String COL_CATEGORY_PWS                         = "category";
    public static final String COL_LATITUDE_PWS                         = "latitude";
    public static final String COL_LONGITUDE_PWS                        = "longitude";
    public static final String COL_MIN_LAT_PWS                          = "min_lat";
    public static final String COL_MAX_LAT_PWS                          = "max_lat";
    public static final String COL_MIN_LONG_PWS                         = "min_long";
    public static final String COL_MAX_LONG_PWS                         = "max_long";
    public static final String COL_STAFF_ID_PWS                         = "staff_id";
    public static final String COL_SOLVE_PWS                            = "solve";
    public static final String COL_DEACTIVATE_PWS                       = "deactivate";
    public static final String COL_DATE_LOGGED_PWS                      = "date_logged";
    public static final String COL_DESCRIPTION                          = "description";
    public static final String COL_SYNC_FLAG_PWS                        = "sync_flag";
    public static final String COL_LOGGER_NAME                          = "member_name";
    public static final String COL_IMEI_PWS                             = "imei";
    public static final String COL_UNIQUE_MEMBER_ID_PWS                 = "unique_member_id";

    /**
     * pws_category_list column names
     */
    public static final String COL_PWS_CATEGORY                         = "pws_category";
    public static final String COL_PWS_CATEGORY_DEACTIVATED_STATUS      = "deactivated_status";
    public static final String COL_USER_CATEGORY_PWS                    = "user_category";

    /**
     * schedule_threshing_activities_flag column names
     */
    public static final String COL_UNIQUE_FIELD_ID_SCHEDULE             = "unique_field_id";
    public static final String COL_THRESHER_SCHEDULE                    = "thresher";
    public static final String COL_THRESHER_ID                          = "thresher_id";
    public static final String COL_FACE_SCAN_FLAG_SCHEDULE              = "face_scan_flag";
    public static final String COL_TEMPLATE_SCHEDULE                    = "template";
    public static final String COL_SCHEDULE_DATE                        = "schedule_date";
    public static final String COL_COLLECTION_CENTER                    = "collection_center";
    public static final String COL_PHONE_NUMBER_SCHEDULE                = "phone_number";
    public static final String COL_IMEI_SCHEDULE                        = "imei";
    public static final String COL_APP_VERSION_SCHEDULE                 = "app_version";
    public static final String COL_LATITUDE_SCHEDULE                    = "latitude";
    public static final String COL_LONGITUDE_SCHEDULE                   = "longitude";
    public static final String COL_STAFF_ID_SCHEDULE                    = "staff_id";
    public static final String COL_DATE_LOGGED_SCHEDULE                 = "date_logged";
    public static final String COL_SYNC_FLAG_SCHEDULE                   = "sync_flag";
    public static final String COL_RESCHEDULE_REASON                    = "reschedule_reason";
    public static final String COL_IK_NUMBER_SCHEDULE                   = "ik_number";
    public static final String COL_RESCHEDULE_FLAG                      = "reschedule_flag";
    public static final String COL_SCHEDULE_FLAG                        = "schedule_flag";
    public static final String COL_URGENT_FLAG                          = "urgent_flag";

    /**
     * confirm_threshing_activities_flag column names
     */
    public static final String COL_UNIQUE_FIELD_ID_CONFIRM              = "unique_field_id";
    public static final String COL_CONFIRM_THRESHING_FLAG               = "confirm_flag";
    public static final String COL_CONFIRM_DATE                         = "confirm_date";
    public static final String COL_IMEI_CONFIRM                         = "imei";
    public static final String COL_APP_VERSION_CONFIRM                  = "app_version";
    public static final String COL_LATITUDE_CONFIRM                     = "latitude";
    public static final String COL_LONGITUDE_CONFIRM                    = "longitude";
    public static final String COL_STAFF_ID_CONFIRM                     = "staff_id";
    public static final String COL_USED_CODE                            = "used_code";
    public static final String COL_SYNC_FLAG_CONFIRM                    = "sync_flag";
    public static final String COL_IK_NUMBER_CONFIRM                    = "ik_number";

    public static final String COL_THRESHER                   = "thresher";



    /**
     * locations_table Column Names
     */
    public static final String COL_STATE_LOC                            = "state";
    public static final String COL_LGA_LOC                              = "lga";
    public static final String COL_WARD_LOC                             = "ward";
    public static final String COL_LOCATION_ID                          = "location_id";

    /**
     * locations_table Column Names
     */
    public static final String COL_UNIQUE_MEMBER_ID_L                   = "unique_member_id";
    public static final String COL_LOCATION_ID_L                        = "location_id";
    public static final String COL_VILLAGE_NAME_L                       = "village_name";
    public static final String COL_STAFF_ID_L                           = "staff_id";
    public static final String COL_LATITUDE_L                           = "latitude";
    public static final String COL_LONGITUDE_L                          = "longitude";
    public static final String COL_SYNC_FLAG_L                          = "sync_flag";
    public static final String COL_STATE_L                              = "state";
    public static final String COL_LGA_L                                = "lga";
    public static final String COL_WARD_L                               = "ward";
    public static final String COL_APP_VERSION                          = "app_version";

    /**
     * locations_table Column Names
     */
    public static final String COL_BGT_ID                               = "bgt_id";
    public static final String COL_COACH_ID                             = "coach_id";
    public static final String COL_BGT_NAME                             = "bgt_name";

}

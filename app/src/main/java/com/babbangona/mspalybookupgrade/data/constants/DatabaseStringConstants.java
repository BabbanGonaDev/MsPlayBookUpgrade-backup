package com.babbangona.mspalybookupgrade.data.constants;

public class DatabaseStringConstants {

    /**
     * database particulars
     */
    public static final String MS_PLAYBOOK_DATABASE_NAME                = "ms_playbook_upgrade.db";
    public static final int MS_PLAYBOOK_DATABASE_VERSION                = 1;

    /**
     * Names of tables
     */
    public static final String ACTIVITY_LIST_TABLE                      = "activity_list";
    public static final String NORMAL_ACTIVITY_FLAGS_TABLE              = "normal_activities_flag";
    public static final String FIELDS_TABLE                             = "fields";
    public static final String STAFF_TABLE                              = "staff";
    public static final String MEMBERS_TABLE                            = "members";
    public static final String HG_ACTIVITY_FLAGS_TABLE                  = "hg_activities_flag";
    public static final String HG_LIST_TABLE                            = "hg_list";
    public static final String LOGS_TABLE                               = "logs";

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

    /**
     * hg_activities column names
     */
    public static final String COL_UNIQUE_FIELD_ID_HG_ACTIVITY          = "unique_field_id";
    public static final String COL_HG_TYPE                              = "hg_type";
    public static final String COL_HG_DATE                              = "hg_date";
    public static final String COL_HG_STATUS                            = "hg_status";
    public static final String COL_STAFF_ID_HG_ACTIVITIES               = "staff_id";
    public static final String COL_SYNC_FLAG_HG_ACTIVITIES              = "sync_flag";

    /**
     * hg_list column names
     */
    public static final String COL_HG_TYPE_HG_LIST                      = "hg_type";
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
}

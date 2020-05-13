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

}

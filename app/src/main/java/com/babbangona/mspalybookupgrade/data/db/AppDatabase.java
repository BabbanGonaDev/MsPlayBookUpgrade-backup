package com.babbangona.mspalybookupgrade.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.daos.ActivityListDao;
import com.babbangona.mspalybookupgrade.data.db.daos.AppVariablesDao;
import com.babbangona.mspalybookupgrade.data.db.daos.BGTCoachesDao;
import com.babbangona.mspalybookupgrade.data.db.daos.CategoryDao;
import com.babbangona.mspalybookupgrade.data.db.daos.ConfirmThreshingActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.daos.FieldsDao;
import com.babbangona.mspalybookupgrade.data.db.daos.HGActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.daos.HGListDao;
import com.babbangona.mspalybookupgrade.data.db.daos.HarvestLocationsDao;
import com.babbangona.mspalybookupgrade.data.db.daos.LastSyncTableDao;
import com.babbangona.mspalybookupgrade.data.db.daos.LogsDao;
import com.babbangona.mspalybookupgrade.data.db.daos.MembersDao;
import com.babbangona.mspalybookupgrade.data.db.daos.NormalActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.daos.PCPWSActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.daos.PWSActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.daos.PWSActivityControllerDao;
import com.babbangona.mspalybookupgrade.data.db.daos.PWSCategoryListDao;
import com.babbangona.mspalybookupgrade.data.db.daos.PictureSyncDao;
import com.babbangona.mspalybookupgrade.data.db.daos.RFActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.daos.RFListDao;
import com.babbangona.mspalybookupgrade.data.db.daos.ScheduleThreshingActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.daos.StaffListDao;
import com.babbangona.mspalybookupgrade.data.db.daos.SyncSummaryDao;
import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;
import com.babbangona.mspalybookupgrade.data.db.entities.AppVariables;
import com.babbangona.mspalybookupgrade.data.db.entities.BGTCoaches;
import com.babbangona.mspalybookupgrade.data.db.entities.Category;
import com.babbangona.mspalybookupgrade.data.db.entities.ConfirmThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.Fields;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.HGList;
import com.babbangona.mspalybookupgrade.data.db.entities.HarvestLocationsTable;
import com.babbangona.mspalybookupgrade.data.db.entities.LastSyncTable;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.PCPWSActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.PWSActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.PWSActivityController;
import com.babbangona.mspalybookupgrade.data.db.entities.PWSCategoryList;
import com.babbangona.mspalybookupgrade.data.db.entities.PictureSync;
import com.babbangona.mspalybookupgrade.data.db.entities.RFActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.RFList;
import com.babbangona.mspalybookupgrade.data.db.entities.ScheduledThreshingActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.StaffList;
import com.babbangona.mspalybookupgrade.data.db.entities.SyncSummary;


@Database(entities = {ActivityList.class, NormalActivitiesFlag.class, Fields.class, StaffList.class,
        Members.class, HGActivitiesFlag.class, HGList.class, Logs.class, LastSyncTable.class, Category.class,
        SyncSummary.class, HarvestLocationsTable.class, AppVariables.class, RFActivitiesFlag.class,
        RFList.class, PictureSync.class, PWSActivitiesFlag.class, PWSCategoryList.class,
        PCPWSActivitiesFlag.class, PWSActivityController.class,
        ScheduledThreshingActivitiesFlag.class, BGTCoaches.class, ConfirmThreshingActivitiesFlag.class},
        version = DatabaseStringConstants.MS_PLAYBOOK_DATABASE_VERSION, exportSchema = false)


public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;
    public abstract ActivityListDao activityListDao();
    public abstract NormalActivitiesFlagDao normalActivitiesFlagDao();
    public abstract FieldsDao fieldsDao();
    public abstract StaffListDao staffListDao();
    public abstract MembersDao membersDao();
    public abstract HGActivitiesFlagDao hgActivitiesFlagDao();
    public abstract HGListDao hgListDao();
    public abstract LogsDao logsDao();
    public abstract LastSyncTableDao lastSyncTableDao();
    public abstract CategoryDao categoryDao();
    public abstract SyncSummaryDao syncSummaryDao();
    public abstract HarvestLocationsDao harvestLocationsDao();
    public abstract AppVariablesDao appVariablesDao();
    public abstract RFActivitiesFlagDao rfActivitiesFlagDao();
    public abstract RFListDao rfListDao();
    public abstract PictureSyncDao pictureSyncDao();
    public abstract PWSActivitiesFlagDao pwsActivitiesFlagDao();
    public abstract PWSCategoryListDao pwsCategoryListDao();
    public abstract PCPWSActivitiesFlagDao pcpwsActivitiesFlagDao();
    public abstract PWSActivityControllerDao pwsActivityControllerDao();
    public abstract ScheduleThreshingActivitiesFlagDao scheduleThreshingActivitiesFlagDao();
    public abstract BGTCoachesDao bgtCoachesDao();
    public abstract ConfirmThreshingActivitiesFlagDao confirmThreshingActivitiesFlagDao();

    /**
     * Return instance of database creation
     * */
    public static AppDatabase getInstance(Context context) {
        if (null == appDatabase) {
            appDatabase = buildDatabaseInstance(context);
        }
        return appDatabase;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE hg_activities_flag ADD COLUMN 'ik_number' TEXT DEFAULT '0'");
            database.execSQL("ALTER TABLE normal_activities_flag ADD COLUMN 'ik_number' TEXT DEFAULT '0'");
            database.execSQL("ALTER TABLE logs ADD COLUMN 'ik_number' TEXT DEFAULT '0'");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE IF NOT EXISTS category (" +
                    "role TEXT PRIMARY KEY NOT NULL," +
                    "category TEXT)");

            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_category' TEXT DEFAULT '2019-01-01 00:00:00'");

            database.execSQL("CREATE TABLE IF NOT EXISTS sync_summary (" +
                    "table_id TEXT  NOT NULL," +
                    "staff_id TEXT  NOT NULL," +
                    "table_name TEXT," +
                    "status TEXT," +
                    "remarks TEXT," +
                    "sync_time TEXT," +
                    "PRIMARY KEY(table_id,staff_id))"
            );
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE normal_activities_flag ADD COLUMN 'crop_type' TEXT DEFAULT 'None'");
            database.execSQL("ALTER TABLE normal_activities_flag ADD COLUMN 'cc_harvest' TEXT DEFAULT '0'");
            database.execSQL("ALTER TABLE fields ADD COLUMN 'crop_type' TEXT DEFAULT 'None'");
            database.execSQL("ALTER TABLE hg_activities_flag ADD COLUMN 'crop_type' TEXT DEFAULT 'None'");
            database.execSQL("ALTER TABLE logs ADD COLUMN 'crop_type' TEXT DEFAULT 'None'");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_harvest_location' TEXT DEFAULT '2019-01-01 00:00:00'");

            database.execSQL("CREATE TABLE IF NOT EXISTS harvest_location (" +
                    "centre TEXT NOT NULL," +
                    "state TEXT," +
                    "lga TEXT," +
                    "ward TEXT," +
                    "deactivate TEXT," +
                    "PRIMARY KEY(centre))"
            );

            database.execSQL("CREATE TABLE IF NOT EXISTS app_variables (" +
                    "variable_id TEXT NOT NULL," +
                    "edit_harvest_location_flag TEXT," +
                    "PRIMARY KEY(variable_id))"
            );
        }
    };

    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE new_hg_list (" +
                    "hg_type TEXT NOT NULL," +
                    "sub_hg_type TEXT NOT NULL," +
                    "deactivated_status TEXT," +
                    "user_category TEXT," +
                    "PRIMARY KEY(hg_type,sub_hg_type))"
            );
            database.execSQL("DROP TABLE hg_list");
            database.execSQL("ALTER TABLE new_hg_list RENAME TO hg_list");

            //@NonNull String rf_type, String deactivated_status, String user_category

            database.execSQL("CREATE TABLE rf_list (" +
                    "rf_type TEXT NOT NULL," +
                    "deactivated_status TEXT," +
                    "user_category TEXT," +
                    "PRIMARY KEY(rf_type))"
            );

            database.execSQL("CREATE TABLE rf_activities_flag (" +
                    "unique_field_id TEXT NOT NULL," +
                    "rf_type TEXT NOT NULL," +
                    "rf_date TEXT," +
                    "rf_status TEXT," +
                    "staff_id TEXT," +
                    "sync_flag TEXT," +
                    "ik_number TEXT," +
                    "crop_type TEXT," +
                    "PRIMARY KEY(unique_field_id,rf_type))"
            );

            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_rf_list' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_up_rf_activities_flag' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_down_rf_activities_flag' TEXT DEFAULT '2019-01-01 00:00:00'");
        }
    };

    private static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE normal_activities_flag ADD COLUMN 'date_logged' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE hg_activities_flag ADD COLUMN 'date_logged' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE rf_activities_flag ADD COLUMN 'date_logged' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE app_variables ADD COLUMN 'minimum_log_date' TEXT DEFAULT '2020-05-15'");
            database.execSQL("ALTER TABLE app_variables ADD COLUMN 'maximum_log_date' TEXT DEFAULT '2020-08-15'");

            database.execSQL("CREATE TABLE IF NOT EXISTS picture_sync (" +
                    "picture_name TEXT NOT NULL," +
                    "PRIMARY KEY(picture_name))"
            );
        }
    };

    private static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE IF NOT EXISTS new_activity_list (" +
                    "activity_id TEXT NOT NULL," +
                    "language_id TEXT NOT NULL," +
                    "user_category TEXT NOT NULL," +
                    "activity_name TEXT," +
                    "activity_destination TEXT," +
                    "activity_priority TEXT," +
                    "deactivated TEXT," +
                    "PRIMARY KEY(activity_id,language_id,user_category))"
            );
            database.execSQL("DROP TABLE activity_list");
            database.execSQL("ALTER TABLE new_activity_list RENAME TO activity_list");

            database.execSQL("CREATE TABLE IF NOT EXISTS new_hg_list (" +
                    "hg_type TEXT NOT NULL," +
                    "sub_hg_type TEXT NOT NULL," +
                    "deactivated_status TEXT," +
                    "user_category TEXT NOT NULL," +
                    "PRIMARY KEY(hg_type,sub_hg_type,user_category))"
            );
            database.execSQL("DROP TABLE hg_list");
            database.execSQL("ALTER TABLE new_hg_list RENAME TO hg_list");

            database.execSQL("CREATE TABLE IF NOT EXISTS new_rf_list (" +
                    "rf_type TEXT NOT NULL," +
                    "user_category TEXT NOT NULL," +
                    "deactivated_status TEXT," +
                    "PRIMARY KEY(rf_type,user_category))"
            );
            database.execSQL("DROP TABLE rf_list");
            database.execSQL("ALTER TABLE new_rf_list RENAME TO rf_list");

            database.execSQL("CREATE TABLE IF NOT EXISTS pws_activities_flag (" +
                    "pws_id TEXT  NOT NULL," +
                    "unique_field_id TEXT," +
                    "ik_number TEXT," +
                    "category TEXT," +
                    "pws_area TEXT," +
                    "lat_longs TEXT," +
                    "min_lat TEXT," +
                    "max_lat TEXT," +
                    "min_long TEXT," +
                    "max_long TEXT," +
                    "latitude TEXT," +
                    "longitude TEXT," +
                    "staff_id TEXT," +
                    "solve TEXT," +
                    "deactivate TEXT," +
                    "date_logged TEXT," +
                    "description TEXT," +
                    "member_name TEXT," +
                    "imei TEXT," +
                    "unique_member_id TEXT," +
                    "sync_flag TEXT," +
                    "PRIMARY KEY(pws_id))"
            );

            database.execSQL("CREATE TABLE IF NOT EXISTS pc_pws_activities_flag (" +
                    "pws_id TEXT  NOT NULL," +
                    "unique_field_id TEXT," +
                    "ik_number TEXT," +
                    "category TEXT," +
                    "pws_area TEXT," +
                    "lat_longs TEXT," +
                    "min_lat TEXT," +
                    "max_lat TEXT," +
                    "min_long TEXT," +
                    "max_long TEXT," +
                    "latitude TEXT," +
                    "longitude TEXT," +
                    "staff_id TEXT," +
                    "solve TEXT," +
                    "deactivate TEXT," +
                    "date_logged TEXT," +
                    "description TEXT," +
                    "member_name TEXT," +
                    "imei TEXT," +
                    "unique_member_id TEXT," +
                    "sync_flag TEXT," +
                    "PRIMARY KEY(pws_id))"
            );

            database.execSQL("CREATE TABLE IF NOT EXISTS pws_category_list (" +
                    "pws_category TEXT NOT NULL," +
                    "user_category TEXT NOT NULL," +
                    "deactivated_status TEXT," +
                    "PRIMARY KEY(pws_category,user_category))"
            );

            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_pws_category_list' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_up_pws_activities_flag' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_down_pws_activities_flag' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_up_pc_pws_activities_flag' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_down_pc_pws_activities_flag' TEXT DEFAULT '2019-01-01 00:00:00'");
        }
    };

    private static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {


            database.execSQL("CREATE TABLE IF NOT EXISTS pws_activity_controller (" +
                    "role TEXT PRIMARY KEY NOT NULL," +
                    "category TEXT)");

            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_pws_activities_controller' TEXT DEFAULT '2019-01-01 00:00:00'");
        }
    };

    private static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE members ADD COLUMN 'template' TEXT");

        }
    };

    private static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE members ADD COLUMN 'role' TEXT");
            database.execSQL("ALTER TABLE members ADD COLUMN 'bgt_id' TEXT DEFAULT 'T-10000000000000BB'");
            database.execSQL("ALTER TABLE members ADD COLUMN 'coach_id' TEXT DEFAULT 'T-10000000000000AA'");
            database.execSQL("ALTER TABLE fields ADD COLUMN 'field_code' TEXT ");
            database.execSQL("ALTER TABLE app_variables ADD COLUMN 'fields_travel_time' TEXT");
            database.execSQL("ALTER TABLE app_variables ADD COLUMN 'average_transition_time' TEXT");
            database.execSQL("ALTER TABLE app_variables ADD COLUMN 'time_per_ha' TEXT");
            database.execSQL("ALTER TABLE app_variables ADD COLUMN 'maximum_schedule_date' TEXT");
            database.execSQL("ALTER TABLE hg_activities_flag ADD COLUMN 'description' TEXT");

            database.execSQL("CREATE TABLE IF NOT EXISTS schedule_threshing_activities_flag (" +
                    "unique_field_id TEXT  NOT NULL," +
                    "thresher TEXT," +
                    "face_scan_flag TEXT," +
                    "template TEXT," +
                    "schedule_date TEXT," +
                    "collection_center TEXT," +
                    "phone_number TEXT," +
                    "imei TEXT," +
                    "app_version TEXT," +
                    "latitude TEXT," +
                    "longitude TEXT," +
                    "date_logged TEXT," +
                    "staff_id TEXT," +
                    "sync_flag TEXT," +
                    "reschedule_reason TEXT," +
                    "ik_number TEXT," +
                    "PRIMARY KEY(unique_field_id))"
            );

            database.execSQL("CREATE TABLE IF NOT EXISTS confirm_threshing_activities_flag (" +
                    "unique_field_id TEXT  NOT NULL," +
                    "confirm_flag TEXT," +
                    "confirm_date TEXT," +
                    "imei TEXT," +
                    "app_version TEXT," +
                    "latitude TEXT," +
                    "longitude TEXT," +
                    "staff_id TEXT," +
                    "used_code TEXT," +
                    "sync_flag TEXT," +
                    "ik_number TEXT," +
                    "PRIMARY KEY(unique_field_id))"
            );

            database.execSQL("CREATE TABLE IF NOT EXISTS bgt_coaches (" +
                    "bgt_id TEXT  NOT NULL," +
                    "coach_id TEXT," +
                    "PRIMARY KEY(bgt_id))"
            );

            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_up_scheduled_activities_flag' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_down_scheduled_activities_flag' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_up_confirm_activities_flag' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_down_confirm_activities_flag' TEXT DEFAULT '2019-01-01 00:00:00'");
            database.execSQL("ALTER TABLE last_sync ADD COLUMN 'last_sync_bgt_coaches' TEXT DEFAULT '2019-01-01 00:00:00'");

        }
    };

    private static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE app_variables ADD COLUMN 'luxand_flag' TEXT DEFAULT '0'");

        }
    };

    private static final Migration MIGRATION_12_13 = new Migration(12, 13) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE schedule_threshing_activities_flag ADD COLUMN 'reschedule_flag' TEXT DEFAULT '0'");
            database.execSQL("ALTER TABLE schedule_threshing_activities_flag ADD COLUMN 'schedule_flag' TEXT DEFAULT '0'");
            database.execSQL("ALTER TABLE schedule_threshing_activities_flag ADD COLUMN 'urgent_flag' TEXT DEFAULT '0'");

        }
    };


    private static final Migration MIGRATION_13_14 = new Migration(13, 14) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("ALTER TABLE bgt_coaches ADD COLUMN 'bgt_name' TEXT ");
            database.execSQL("ALTER TABLE confirm_threshing_activities_flag ADD COLUMN 'thresher' TEXT ");
            database.execSQL("ALTER TABLE confirm_threshing_activities_flag ADD COLUMN 'thresher_id' TEXT");

        }
    };
    
    private static AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DatabaseStringConstants.MS_PLAYBOOK_DATABASE_NAME)
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4,MIGRATION_4_5,MIGRATION_5_6,
                        MIGRATION_6_7,MIGRATION_7_8,MIGRATION_8_9,MIGRATION_9_10,MIGRATION_10_11,
                        MIGRATION_11_12,MIGRATION_12_13,MIGRATION_13_14)
                .build();
//                .fallbackToDestructiveMigration()
    }

    public void cleanUp(){
        appDatabase = null;
    }
}

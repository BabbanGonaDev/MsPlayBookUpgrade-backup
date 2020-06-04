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
import com.babbangona.mspalybookupgrade.data.db.daos.CategoryDao;
import com.babbangona.mspalybookupgrade.data.db.daos.FieldsDao;
import com.babbangona.mspalybookupgrade.data.db.daos.HGActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.daos.HGListDao;
import com.babbangona.mspalybookupgrade.data.db.daos.HarvestLocationsDao;
import com.babbangona.mspalybookupgrade.data.db.daos.LastSyncTableDao;
import com.babbangona.mspalybookupgrade.data.db.daos.LogsDao;
import com.babbangona.mspalybookupgrade.data.db.daos.MembersDao;
import com.babbangona.mspalybookupgrade.data.db.daos.NormalActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.daos.StaffListDao;
import com.babbangona.mspalybookupgrade.data.db.daos.SyncSummaryDao;
import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;
import com.babbangona.mspalybookupgrade.data.db.entities.AppVariables;
import com.babbangona.mspalybookupgrade.data.db.entities.Category;
import com.babbangona.mspalybookupgrade.data.db.entities.Fields;
import com.babbangona.mspalybookupgrade.data.db.entities.HGActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.HGList;
import com.babbangona.mspalybookupgrade.data.db.entities.HarvestLocationsTable;
import com.babbangona.mspalybookupgrade.data.db.entities.LastSyncTable;
import com.babbangona.mspalybookupgrade.data.db.entities.Logs;
import com.babbangona.mspalybookupgrade.data.db.entities.Members;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;
import com.babbangona.mspalybookupgrade.data.db.entities.StaffList;
import com.babbangona.mspalybookupgrade.data.db.entities.SyncSummary;


@Database(entities = {ActivityList.class, NormalActivitiesFlag.class, Fields.class, StaffList.class,
        Members.class, HGActivitiesFlag.class, HGList.class, Logs.class, LastSyncTable.class, Category.class,
        SyncSummary.class, HarvestLocationsTable.class, AppVariables.class},
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


    private static AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DatabaseStringConstants.MS_PLAYBOOK_DATABASE_NAME)
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2,MIGRATION_2_3,MIGRATION_3_4,MIGRATION_4_5)
                .build();
//                .fallbackToDestructiveMigration()
    }

    public void cleanUp(){
        appDatabase = null;
    }
}

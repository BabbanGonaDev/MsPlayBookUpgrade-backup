package com.babbangona.mspalybookupgrade.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.babbangona.mspalybookupgrade.data.constants.DatabaseStringConstants;
import com.babbangona.mspalybookupgrade.data.db.daos.ActivityListDao;
import com.babbangona.mspalybookupgrade.data.db.daos.FieldsDao;
import com.babbangona.mspalybookupgrade.data.db.daos.NormalActivitiesFlagDao;
import com.babbangona.mspalybookupgrade.data.db.entities.ActivityList;
import com.babbangona.mspalybookupgrade.data.db.entities.Fields;
import com.babbangona.mspalybookupgrade.data.db.entities.NormalActivitiesFlag;


@Database(entities = {ActivityList.class, NormalActivitiesFlag.class, Fields.class},
        version = DatabaseStringConstants.MS_PLAYBOOK_DATABASE_VERSION, exportSchema = false)


public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase appDatabase;
    public abstract ActivityListDao activityListDao();
    public abstract NormalActivitiesFlagDao normalActivitiesFlagDao();
    public abstract FieldsDao fieldsDao();

    /**
     * Return instance of database creation
     * */
    public static AppDatabase getInstance(Context context) {
        if (null == appDatabase) {
            appDatabase = buildDatabaseInstance(context);
        }
        return appDatabase;
    }


    private static AppDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DatabaseStringConstants.MS_PLAYBOOK_DATABASE_NAME)
                .allowMainThreadQueries()
                .build();
//                .fallbackToDestructiveMigration()
    }

    public void cleanUp(){
        appDatabase = null;
    }
}

package com.babbangona.mspalybookupgrade.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.babbangona.mspalybookupgrade.data.db.daos.SampleDao;
import com.babbangona.mspalybookupgrade.data.db.entities.SampleEntity;


//TODO: Change this to your entity class name and db version number
@Database(entities = {SampleEntity.class},
        version = 1, exportSchema = false)


public abstract class AppDatabase extends RoomDatabase {

    public abstract SampleDao getSampleDao();
    private static AppDatabase appDatabase;

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
        return Room.databaseBuilder(context,
                AppDatabase.class,

                //TODO: change the database name below to the database name of you application
                "database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public void cleanUp(){
        appDatabase = null;
    }
}

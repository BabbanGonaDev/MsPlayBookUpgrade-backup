package com.babbangona.mspalybookupgrade.tpo.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.babbangona.mspalybookupgrade.tpo.data.room.dao.DeliveryAttendanceDAO;
import com.babbangona.mspalybookupgrade.tpo.data.room.tables.DeliveryAttendance;

@Database(entities = {DeliveryAttendance.class}, version = 1, exportSchema = false)
public abstract class TPODatabase extends RoomDatabase {
    private static TPODatabase instance;

    //Init of instance
    public static TPODatabase getInstance(Context mCtx) {
        if (instance == null) {
            instance = Room.databaseBuilder(mCtx,
                    TPODatabase.class, "tpo-db.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract DeliveryAttendanceDAO getDeliveryAttendanceDao();
}

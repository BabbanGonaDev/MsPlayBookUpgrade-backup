package com.babbangona.mspalybookupgrade.donotpay.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.babbangona.mspalybookupgrade.donotpay.data.room.dao.DoNotPayDAO;
import com.babbangona.mspalybookupgrade.donotpay.data.room.dao.DoNotPayReasonsDAO;
import com.babbangona.mspalybookupgrade.donotpay.data.room.tables.DoNotPayReasonsTable;
import com.babbangona.mspalybookupgrade.donotpay.data.room.tables.DoNotPayTable;

@Database(entities = {DoNotPayTable.class, DoNotPayReasonsTable.class}, version = 1, exportSchema = false)
public abstract class DNPDatabase extends RoomDatabase {
    private static DNPDatabase instance;

    //Init of instance
    public static DNPDatabase getInstance(Context mCtx) {
        if (instance == null) {
            instance = Room.databaseBuilder(mCtx,
                    DNPDatabase.class,
                    "donotpay-db.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public abstract DoNotPayDAO getDoNotPayDao();

    public abstract DoNotPayReasonsDAO getDoNotPayReasonsDao();
}

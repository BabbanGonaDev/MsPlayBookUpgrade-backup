package com.babbangona.mspalybookupgrade.transporter.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.babbangona.mspalybookupgrade.transporter.data.room.dao.CollectionCenterDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.OperatingAreasDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.TransporterDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CollectionCenterTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;

@Database(entities = {TransporterTable.class, CollectionCenterTable.class, OperatingAreasTable.class}, version = 1, exportSchema = false)
public abstract class TransporterDatabase extends RoomDatabase {
    private static TransporterDatabase INSTANCE;

    //Init of instance.
    public static TransporterDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    TransporterDatabase.class,
                    "transporter-db.db")
                    .createFromAsset("database/transporter.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract TransporterDAO getTransporterDao();

    public abstract CollectionCenterDAO getCcDao();

    public abstract OperatingAreasDAO getOpAreaDao();
}

package com.babbangona.mspalybookupgrade.transporter.data.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.babbangona.mspalybookupgrade.transporter.data.room.dao.CollectionCenterDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.OperatingAreasDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.TransporterDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CollectionCenterTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;

@Database(entities = {TransporterTable.class, CollectionCenterTable.class, OperatingAreasTable.class}, version = 1, exportSchema = false)
public abstract class TransporterDatabase extends RoomDatabase {
    private static TransporterDatabase INSTANCE;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //DO Nothing
        }
    };

    //Init of instance.
    public static TransporterDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    TransporterDatabase.class,
                    "transporter-db.db")
                    //.createFromAsset("database/transporter.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
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

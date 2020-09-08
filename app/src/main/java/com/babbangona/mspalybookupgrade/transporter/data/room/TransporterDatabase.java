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

@Database(entities = {TransporterTable.class, CollectionCenterTable.class, OperatingAreasTable.class}, version = 2, exportSchema = false)
public abstract class TransporterDatabase extends RoomDatabase {
    private static TransporterDatabase INSTANCE;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            //Move current table to temp
            db.execSQL("ALTER TABLE transporter_table RENAME TO transporter_table_temp");

            //Create new table structure
            db.execSQL("CREATE TABLE transporter_table (phone_number TEXT NOT NULL," +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "vehicle_type TEXT, " +
                    "payment_option TEXT, " +
                    "bg_card TEXT, " +
                    "account_number TEXT, " +
                    "account_name TEXT, " +
                    "account_mismatch_flag INTEGER, " +
                    "bank_name TEXT, " +
                    "template TEXT, " +
                    "facial_capture_flag INTEGER, " +
                    "reg_date TEXT, " +
                    "date_updated TEXT, " +
                    "sync_flag INTEGER, " +
                    "PRIMARY KEY(phone_number))");

            //Copy contents from temp table
            db.execSQL("INSERT INTO transporter_table (phone_number, first_name, last_name, vehicle_type, payment_option, bg_card, account_number, account_name, account_mismatch_flag, bank_name, template, reg_date, date_updated, sync_flag)" +
                    "SELECT phone_number, first_name, last_name, vehicle_type, payment_option, bg_card, account_number, account_name, account_mismatch_flag, bank_name, template, reg_date, date_updated, sync_flag FROM transporter_table_temp");

            //Clean-up
            db.execSQL("DROP TABLE transporter_table_temp");
        }
    };

    //Init of instance.
    public static TransporterDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    TransporterDatabase.class,
                    "transporter-db.db")
                    //.createFromAsset("database/transporter.db")
                    .addMigrations(MIGRATION_1_2)
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

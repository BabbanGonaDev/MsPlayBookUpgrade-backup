package com.babbangona.mspalybookupgrade.transporter.data.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.babbangona.mspalybookupgrade.transporter.data.room.dao.CardsDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.CoachLogsDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.CollectionCenterDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.FavouritesDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.OperatingAreasDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.TempTransporterDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.TpoLogsDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.dao.TransporterDAO;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CardsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CollectionCenterTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.FavouritesTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TempTransporterTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TpoLogsTable;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;

@Database(entities = {TransporterTable.class, CollectionCenterTable.class, OperatingAreasTable.class,
        CardsTable.class, CoachLogsTable.class, TpoLogsTable.class, TempTransporterTable.class, FavouritesTable.class}, version = 6, exportSchema = false)
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

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            //Create Cards table
            db.execSQL("CREATE TABLE cards_table (id TEXT, " +
                    "account_number TEXT NOT NULL, " +
                    "card_name TEXT NOT NULL, " +
                    "product_code TEXT, " +
                    "branch_number TEXT, " +
                    "card_number TEXT NOT NULL, " +
                    "PRIMARY KEY(account_number, card_name, card_number))");
        }
    };

    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
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
                    "invalid_card_flag INTEGER, " +
                    "account_number TEXT, " +
                    "account_name TEXT, " +
                    "account_mismatch_flag INTEGER, " +
                    "bank_name TEXT, " +
                    "template TEXT, " +
                    "facial_capture_flag INTEGER, " +
                    "staff_id TEXT, " +
                    "reg_date TEXT, " +
                    "date_updated TEXT, " +
                    "sync_flag INTEGER, " +
                    "PRIMARY KEY(phone_number))");

            //Copy contents from temp table
            db.execSQL("INSERT INTO transporter_table (phone_number, first_name, last_name, vehicle_type, payment_option, bg_card, account_number, account_name, account_mismatch_flag, bank_name, template, facial_capture_flag, reg_date, date_updated, sync_flag)" +
                    "SELECT phone_number, first_name, last_name, vehicle_type, payment_option, bg_card, account_number, account_name, account_mismatch_flag, bank_name, template, facial_capture_flag, reg_date, date_updated, sync_flag FROM transporter_table_temp");

            //Clean-up
            db.execSQL("DROP TABLE transporter_table_temp");
        }
    };

    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            /**
             * Added "imei" and "app_version" columns to the transporter table.
             */

            //Move current table to temp
            db.execSQL("ALTER TABLE transporter_table RENAME TO transporter_table_temp");

            //Create new table structure
            db.execSQL("CREATE TABLE transporter_table (phone_number TEXT NOT NULL," +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "vehicle_type TEXT, " +
                    "payment_option TEXT, " +
                    "bg_card TEXT, " +
                    "invalid_card_flag INTEGER, " +
                    "account_number TEXT, " +
                    "account_name TEXT, " +
                    "account_mismatch_flag INTEGER, " +
                    "bank_name TEXT, " +
                    "template TEXT, " +
                    "facial_capture_flag INTEGER, " +
                    "staff_id TEXT, " +
                    "imei TEXT, " +
                    "app_version TEXT, " +
                    "reg_date TEXT, " +
                    "date_updated TEXT, " +
                    "sync_flag INTEGER, " +
                    "PRIMARY KEY(phone_number))");

            //Copy contents from temp table
            db.execSQL("INSERT INTO transporter_table (phone_number, first_name, last_name, vehicle_type, payment_option, bg_card, invalid_card_flag, account_number, account_name, account_mismatch_flag, bank_name, template, facial_capture_flag, staff_id, reg_date, date_updated, sync_flag)" +
                    "SELECT phone_number, first_name, last_name, vehicle_type, payment_option, bg_card, invalid_card_flag, account_number, account_name, account_mismatch_flag, bank_name, template, facial_capture_flag, staff_id, reg_date, date_updated, sync_flag FROM transporter_table_temp");

            //Clean-up
            db.execSQL("DROP TABLE transporter_table_temp");
        }
    };

    private static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase db) {
            /**
             * Added coach_logs, tpo_logs, temp_transporter & favourites tables.
             */

            //Create Coach Logs.
            db.execSQL("CREATE TABLE coach_logs_table (member_id TEXT NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "transported_by TEXT, " +
                    "transporter_id TEXT NOT NULL, " +
                    "voucher_id TEXT, " +
                    "voucher_id_flag INTEGER, " +
                    "cc_id TEXT, " +
                    "instant_payment_flag INTEGER, " +
                    "date_logged TEXT NOT NULL, " +
                    "sync_flag INTEGER, " +
                    "PRIMARY KEY(member_id, quantity, transporter_id, date_logged))");

            //Create Tpo Logs.
            db.execSQL("CREATE TABLE tpo_logs_table (member_id TEXT NOT NULL, " +
                    "quantity INTEGER NOT NULL, " +
                    "transported_by TEXT, " +
                    "transporter_id TEXT NOT NULL, " +
                    "voucher_id TEXT, " +
                    "voucher_id_flag INTEGER, " +
                    "cc_id TEXT, " +
                    "instant_payment_flag INTEGER, " +
                    "date_logged TEXT NOT NULL, " +
                    "sync_flag INTEGER, " +
                    "PRIMARY KEY(member_id, quantity, transporter_id, date_logged))");

            //Create Temp Transporter
            db.execSQL("CREATE TABLE temp_transporter_table (temp_transporter_id TEXT NOT NULL, " +
                    "first_name TEXT, " +
                    "last_name TEXT, " +
                    "date_updated TEXT, " +
                    "sync_flag INTEGER, " +
                    "PRIMARY KEY(temp_transporter_id))");

            //Create Favourites table
            db.execSQL("CREATE TABLE favourites_table (staff_id TEXT NOT NULL, " +
                    "phone_number TEXT NOT NULL, " +
                    "active_flag INTEGER, " +
                    "sync_flag INTEGER, " +
                    "PRIMARY KEY(staff_id, phone_number))");
        }
    };


    //Init of instance.
    public static TransporterDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    TransporterDatabase.class,
                    "transporter-db.db")
                    //.createFromAsset("database/transporter.db")
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
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

    public abstract CardsDAO getCardsDao();

    public abstract TpoLogsDAO getTpoLogsDao();

    public abstract CoachLogsDAO getCoachLogsDao();

    public abstract TempTransporterDAO getTempTransporterDao();

    public abstract FavouritesDAO getFavouritesDao();
}

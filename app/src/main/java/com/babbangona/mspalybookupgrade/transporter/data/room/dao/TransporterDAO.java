package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;

import java.util.List;

@Dao
public interface TransporterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransporterList(List<TransporterTable> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleTransporter(TransporterTable transporter);

    @Update
    void updateTransporter(TransporterTable transporter);

    @Query("SELECT * FROM transporter_table WHERE phone_number = :phone_no")
    TransporterTable getTransporterDetails(String phone_no);

    @Query("SELECT * FROM transporter_table")
    LiveData<List<TransporterTable>> getAllTransporters();

    @Query("SELECT * FROM transporter_table WHERE sync_flag = 0")
    List<TransporterTable> getUnSyncedTransporterTable();

    @Query("UPDATE transporter_table SET sync_flag = :flag WHERE phone_number = :phone_number")
    void updateSyncResponse(String phone_number, Integer flag);
}

package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TempTransporterTable;

import java.util.List;

@Dao
public interface TempTransporterDAO {

    @Insert
    void insertSingleTempTransporter(TempTransporterTable single);

    @Insert
    void insertTempTransporterList(List<TempTransporterTable> list);

    @Query("SELECT * FROM temp_transporter_table WHERE sync_flag = 0")
    List<TempTransporterTable> getAllUnSynced();

    @Query("UPDATE temp_transporter_table SET sync_flag = :syncFlag WHERE temp_transporter_id = :tempTransId")
    void updateSyncFlag(String tempTransId, Integer syncFlag);
}

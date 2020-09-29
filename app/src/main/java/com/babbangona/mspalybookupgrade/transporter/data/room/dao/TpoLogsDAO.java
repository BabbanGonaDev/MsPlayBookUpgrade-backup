package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TpoLogsTable;

import java.util.List;

@Dao
public interface TpoLogsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleTpoLog(TpoLogsTable log);

    @Insert
    void insertTpoLogsList(List<TpoLogsTable> list);

    @Query("SELECT * FROM tpo_logs_table WHERE sync_flag = 0")
    List<TpoLogsTable> getUnSyncedTpoLogs();

    @Query("UPDATE tpo_logs_table SET sync_flag = :syncFlag WHERE member_id = :memberId AND quantity = :qty AND transporter_id = :transporterId AND date_logged = :dateLogged")
    void updateSyncFlags(String memberId, Integer qty, String transporterId, String dateLogged, Integer syncFlag);
}

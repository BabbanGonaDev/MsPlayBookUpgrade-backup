package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;

import java.util.List;

@Dao
public interface CoachLogsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleCoachLog(CoachLogsTable log);

    @Insert
    void insertCoachLogsList(List<CoachLogsTable> list);

    @Query("SELECT * FROM coach_logs_table WHERE sync_flag = 0")
    List<CoachLogsTable> getUnSyncedCoachLogs();

    @Query("UPDATE coach_logs_table SET sync_flag = :syncFlag WHERE member_id = :memberId AND quantity = :qty AND transporter_id = :transporterId AND date_logged = :dateLogged")
    void updateSyncFlags(String memberId, Integer qty, String transporterId, String dateLogged, Integer syncFlag);
}

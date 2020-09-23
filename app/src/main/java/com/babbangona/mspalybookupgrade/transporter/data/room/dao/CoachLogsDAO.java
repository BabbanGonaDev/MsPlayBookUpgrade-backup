package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CoachLogsTable;

import java.util.List;

@Dao
public interface CoachLogsDAO {

    @Insert
    void insertSingleCoachLog(CoachLogsTable log);

    @Insert
    void insertCoachLogsList(List<CoachLogsTable> list);
}

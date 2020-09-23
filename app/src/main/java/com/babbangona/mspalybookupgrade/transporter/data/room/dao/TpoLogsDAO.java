package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TpoLogsTable;

import java.util.List;

@Dao
public interface TpoLogsDAO {

    @Insert
    void insertSingleTpoLog(TpoLogsTable log);

    @Insert
    void insertTpoLogsList(List<TpoLogsTable> list);
}

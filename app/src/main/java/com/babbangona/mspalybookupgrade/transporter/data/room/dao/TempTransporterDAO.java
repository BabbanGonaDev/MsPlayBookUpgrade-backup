package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TempTransporterTable;

import java.util.List;

@Dao
public interface TempTransporterDAO {

    @Insert
    void insertSingleTempTransporter(TempTransporterTable single);

    @Insert
    void insertTempTransporterList(List<TempTransporterTable> list);
}

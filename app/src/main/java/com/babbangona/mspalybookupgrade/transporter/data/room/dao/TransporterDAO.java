package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

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

    @Insert
    void insertSingleTransporter(TransporterTable transporter);

    @Update
    void updateTransporter(TransporterTable transporter);

    @Query("SELECT * FROM transporter_table WHERE phone_number = :phone_no")
    TransporterTable getTransporterDetails(String phone_no);
}

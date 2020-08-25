package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.OperatingAreasTable;

import java.util.List;

@Dao
public interface OperatingAreasDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOpAreasList(List<OperatingAreasTable> list);

    @Query("SELECT * FROM operating_areas_table WHERE phone_number = :phoneNo")
    LiveData<List<OperatingAreasTable>> getTransporterOperatingAreas(String phoneNo);
}

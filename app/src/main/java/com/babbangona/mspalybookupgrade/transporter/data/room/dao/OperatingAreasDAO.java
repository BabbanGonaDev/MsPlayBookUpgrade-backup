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

    @Query("SELECT * FROM operating_areas_table WHERE sync_flag = 0")
    List<OperatingAreasTable> getUnSyncedOperatingAreas();

    @Query("UPDATE operating_areas_table SET sync_flag = :flag WHERE phone_number = :phone_no AND cc_id = :cc_id")
    void updateSyncResponse(String phone_no, String cc_id, Integer flag);
}

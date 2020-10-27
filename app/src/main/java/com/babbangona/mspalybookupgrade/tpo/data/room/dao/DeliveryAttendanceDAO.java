package com.babbangona.mspalybookupgrade.tpo.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.tpo.data.room.tables.DeliveryAttendance;

import java.util.List;

@Dao
public interface DeliveryAttendanceDAO {

    @Insert
    void insert(DeliveryAttendance delivery);

    @Query("SELECT * FROM delivery_attendance_table WHERE sync_flag = 0")
    List<DeliveryAttendance> getUnSyncedDelivery();

    @Query("UPDATE delivery_attendance_table SET sync_flag = :syncFlag WHERE unique_member_id = :uniqueMemberId AND cc_id = :ccId " +
            "AND qty_delivered = :qty AND date_delivered = :dateDelivered")
    void updateSyncFlag(String uniqueMemberId, String ccId, String qty, String dateDelivered, Integer syncFlag);
}

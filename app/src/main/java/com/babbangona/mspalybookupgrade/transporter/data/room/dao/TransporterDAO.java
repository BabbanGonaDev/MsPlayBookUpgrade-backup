package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.transporter.data.models.CustomTransporter;
import com.babbangona.mspalybookupgrade.transporter.data.room.tables.TransporterTable;

import java.util.List;

@Dao
public interface TransporterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTransporterList(List<TransporterTable> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleTransporter(TransporterTable transporter);

    @Update
    void updateTransporter(TransporterTable transporter);

    @Query("SELECT * FROM transporter_table WHERE phone_number = :phone_no")
    TransporterTable getTransporterDetailsByPhoneNo(String phone_no);

    @Query("SELECT * FROM transporter_table WHERE account_number = :acc_no AND bank_name = :bank_name")
    TransporterTable getTransporterDetailsByBank(String acc_no, String bank_name);

    @Query("SELECT * FROM transporter_table ORDER BY reg_date DESC")
    LiveData<List<TransporterTable>> getAllTransporters();

    @Query("SELECT * FROM transporter_table WHERE sync_flag = 0")
    List<TransporterTable> getUnSyncedTransporterTable();

    @Query("UPDATE transporter_table SET sync_flag = :flag WHERE phone_number = :phone_number")
    void updateSyncResponse(String phone_number, Integer flag);

    //Could use 'COALESCE' or 'IFNULL'
    @Query("SELECT a.first_name, a.last_name, a.phone_number, COALESCE(group_concat(DISTINCT c.cc_name), 'N/A') AS areas, " +
            "COALESCE(SUM(DISTINCT e.quantity), 0) AS bags_transported, COALESCE(d.active_flag, 0) AS active_favourite FROM transporter_table AS a " +
            "LEFT JOIN operating_areas_table AS b ON (a.phone_number = b.phone_number) " +
            "LEFT JOIN collection_center_table AS c ON (b.cc_id = c.cc_id) " +
            "LEFT JOIN favourites_table AS d ON (a.phone_number = d.phone_number) " +
            "LEFT JOIN coach_logs_table AS e ON (a.phone_number = e.transporter_id)" +
            "GROUP BY a.phone_number ORDER BY active_favourite DESC, bags_transported DESC")
    LiveData<List<CustomTransporter>> getTransporterForBooking();
}
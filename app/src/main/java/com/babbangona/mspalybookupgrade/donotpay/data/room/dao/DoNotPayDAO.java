package com.babbangona.mspalybookupgrade.donotpay.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.donotpay.data.room.tables.DoNotPayTable;

import java.util.List;

@Dao
public interface DoNotPayDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DoNotPayTable doNotPayTable);

    @Query("SELECT * FROM donotpay_table WHERE sync_flag = 0")
    List<DoNotPayTable> getUnsyncedDNP();

    @Query("SELECT COUNT(ik_number) FROM donotpay_table WHERE ik_number = :ikNumber")
    Integer checkTGMarked(String ikNumber);

    @Query("UPDATE donotpay_table SET sync_flag = :syncFlag WHERE ik_number = :ikNumber")
    void updateSyncFlag(Integer syncFlag, String ikNumber);
}

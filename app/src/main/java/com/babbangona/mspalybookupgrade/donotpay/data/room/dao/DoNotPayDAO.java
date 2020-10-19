package com.babbangona.mspalybookupgrade.donotpay.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.donotpay.data.room.tables.DoNotPayTable;

import java.util.List;

@Dao
public interface DoNotPayDAO {

    @Insert
    void insert(DoNotPayTable doNotPayTable);

    @Query("SELECT * FROM donotpay_table WHERE sync_flag = 0")
    List<DoNotPayTable> getUnsyncedDNP();

    @Query("SELECT COUNT(ik_number) FROM donotpay_table WHERE ik_number = :ikNumber")
    Integer checkTGMarked(String ikNumber);
}

package com.babbangona.mspalybookupgrade.donotpay.data.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.babbangona.mspalybookupgrade.donotpay.data.room.tables.DoNotPayReasonsTable;

import java.util.List;

@Dao
public interface DoNotPayReasonsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDNPReasons(DoNotPayReasonsTable single);

    @Query("SELECT reason FROM donotpay_reasons_table WHERE active_flag = 1")
    List<String> getDNPReasons();
}

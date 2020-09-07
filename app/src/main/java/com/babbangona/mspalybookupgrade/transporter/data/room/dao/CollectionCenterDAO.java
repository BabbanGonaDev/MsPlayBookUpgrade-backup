package com.babbangona.mspalybookupgrade.transporter.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.babbangona.mspalybookupgrade.transporter.data.room.tables.CollectionCenterTable;

import java.util.List;

@Dao
public interface CollectionCenterDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCCList(List<CollectionCenterTable> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSingleCC(CollectionCenterTable single);

    @Update
    void updateCC(CollectionCenterTable cc);

    @Query("SELECT DISTINCT state FROM collection_center_table")
    LiveData<List<String>> getAllStates();

    @Query("SELECT DISTINCT lga FROM collection_center_table WHERE state = :state")
    LiveData<List<String>> getAllLga(String state);

    @Query("SELECT * FROM collection_center_table WHERE state = :state AND lga = :lga")
    LiveData<List<CollectionCenterTable>> getCollectionCenters(String state, String lga);
}
